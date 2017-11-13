package outlier.cost;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.in.XParserRegistry;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.jbpt.petri.Flow;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.astar.petrinet.AbstractPetrinetReplayer;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithILP;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithoutILP;
import org.processmining.plugins.astar.petrinet.impl.AbstractPILPDelegate;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

import nl.tue.astar.AStarException;
import test.AlignmentTest;

public class Test {

	public static int iteration = 0;

//	static {
//		try {
//			System.loadLibrary("lpsolve55");
//			System.loadLibrary("lpsolve55j");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) throws Exception {
		test(args);
	}

	public static void test(String[] args) throws Exception {
		//		DummyUIPluginContext context = new DummyUIPluginContext(new DummyGlobalContext(), "label");
//		AbstractPILPDelegate.setDebugMode(new File("D:\\temp\\alignmentDebugTest\\"));
		AbstractPILPDelegate.setDebugMode(null);
		
		ComputeCostBasedOnFreq ins = new ComputeCostBasedOnFreq();
		ins.initPreMapping("D:/data4code/replay/pre.csv");
		ins.getEventSeqForEachPatient("D:/data4code/cluster/LogBasedOnKmeansPlusPlus-14.csv");
		ins.removeOneLoop();

		PetrinetGraph net = null;
		Marking initialMarking = null;
		Marking[] finalMarkings = null; // only one marking is used so far
		XLog log = null;
		Map<Transition, Integer> costMOS = null; // movements on system
		Map<XEventClass, Integer> costMOT = null; // movements on trace
		TransEvClassMapping mapping = null;

		net = constructNet("D:/data4code/mm.pnml");
		initialMarking = getInitialMarking(net);
		finalMarkings = getFinalMarkings(net);
		XParserRegistry temp=XParserRegistry.instance();
		temp.setCurrentDefault(new XesXmlParser());
		log = temp.currentDefault().parse(new File("D:/data4code/mm.xes")).get(0);
//		log = XParserRegistry.instance().currentDefault().parse(new File("d:/temp/BPI2013all90.xes.gz")).get(0);
		//			log = XParserRegistry.instance().currentDefault().parse(new File("d:/temp/BPI 730858110.xes.gz")).get(0);
		//			log = XFactoryRegistry.instance().currentDefault().openLog();
		costMOS = constructMOSCostFunction(net);
		XEventClass dummyEvClass = new XEventClass("DUMMY", 99999);
		XEventClassifier eventClassifier = XLogInfoImpl.STANDARD_CLASSIFIER;
		costMOT = constructMOTCostFunction(net, log, eventClassifier, dummyEvClass);
		mapping = constructMapping(net, log, dummyEvClass, eventClassifier);

		int cost1 = AlignmentTest.computeCost(costMOS, costMOT, initialMarking, finalMarkings, null, net, log,
				mapping, false);
		System.out.println(cost1);
		
//		int iteration = 0;
//		while (true) {
//			System.out.println("start: " + iteration);
//			long start = System.currentTimeMillis();
//			int cost1 = AlignmentTest.computeCost(costMOS, costMOT, initialMarking, finalMarkings, null, net, log,
//					mapping, false);
//			long mid = System.currentTimeMillis();
//			System.out.println("   With ILP cost: " + cost1 + "  t: " + (mid - start));
//
//			long mid2 = System.currentTimeMillis();
//			int cost2 = AlignmentTest.computeCost(costMOS, costMOT, initialMarking, finalMarkings, null, net, log,
//					mapping, false);
//			long end = System.currentTimeMillis();
//
//			System.out.println("   No ILP   cost: " + cost2 + "  t: " + (end - mid2));
//			if (cost1 != cost2) {
//				System.err.println("ERROR");
//			}
//			//System.gc();
//			System.out.flush();
//			iteration++;
//		}

	}

	public static int computeCost(Map<Transition, Integer> costMOS, Map<XEventClass, Integer> costMOT,
			Marking initialMarking, Marking[] finalMarkings, PluginContext context, PetrinetGraph net, XLog log,
			TransEvClassMapping mapping, boolean useILP) {
		AbstractPetrinetReplayer<?, ?> replayEngine;
		if (useILP) {
			replayEngine = new PetrinetReplayerWithILP();
		} else {
			replayEngine = new PetrinetReplayerWithoutILP();
		}

		IPNReplayParameter parameters = new CostBasedCompleteParam(costMOT, costMOS);
		parameters.setInitialMarking(initialMarking);
		parameters.setFinalMarkings(finalMarkings[0]);
		parameters.setGUIMode(false);
		parameters.setCreateConn(false);
		parameters.setNumThreads(8);

		int cost = 0;
		try {
			PNRepResult result = replayEngine.replayLog(context, net, log, mapping, parameters);

			for (SyncReplayResult res : result) {
				cost += ((int) res.getInfo().get(PNRepResult.RAWFITNESSCOST).doubleValue())
						* res.getTraceIndex().size();
			}
		} catch (AStarException e) {
			e.printStackTrace();
		}

		return cost;
	}

	private static PetrinetGraph constructNet(String netFile) {
		PNMLSerializer PNML = new PNMLSerializer();
		NetSystem sys = PNML.parse(netFile);

		//System.err.println(sys.getMarkedPlaces());

		//		int pi, ti;
		//		pi = ti = 1;
		//		for (org.jbpt.petri.Place p : sys.getPlaces())
		//			p.setName("p" + pi++);
		//		for (org.jbpt.petri.Transition t : sys.getTransitions())
		//				t.setName("t" + ti++);

		PetrinetGraph net = PetrinetFactory.newPetrinet(netFile);

		// places
		Map<org.jbpt.petri.Place, Place> p2p = new HashMap<org.jbpt.petri.Place, Place>();
		for (org.jbpt.petri.Place p : sys.getPlaces()) {
			Place pp = net.addPlace(p.toString());
			p2p.put(p, pp);
		}

		// transitions
		int l = 0;
		Map<org.jbpt.petri.Transition, Transition> t2t = new HashMap<org.jbpt.petri.Transition, Transition>();
		for (org.jbpt.petri.Transition t : sys.getTransitions()) {
			Transition tt = net.addTransition(t.getLabel());
			tt.setInvisible(t.isSilent());
			t2t.put(t, tt);
		}

		// flow
		for (Flow f : sys.getFlow()) {
			if (f.getSource() instanceof org.jbpt.petri.Place) {
				net.addArc(p2p.get(f.getSource()), t2t.get(f.getTarget()));
			} else {
				net.addArc(t2t.get(f.getSource()), p2p.get(f.getTarget()));
			}
		}

		// add unique start node
		if (sys.getSourceNodes().isEmpty()) {
			Place i = net.addPlace("START_P");
			Transition t = net.addTransition("");
			t.setInvisible(true);
			net.addArc(i, t);

			for (org.jbpt.petri.Place p : sys.getMarkedPlaces()) {
				net.addArc(t, p2p.get(p));
			}

		}

		return net;
	}

	private static Marking[] getFinalMarkings(PetrinetGraph net) {
		Marking finalMarking = new Marking();

		for (Place p : net.getPlaces()) {
			if (net.getOutEdges(p).isEmpty())
				finalMarking.add(p);
		}

		Marking[] finalMarkings = new Marking[1];
		finalMarkings[0] = finalMarking;

		return finalMarkings;
	}

	private static Marking getInitialMarking(PetrinetGraph net) {
		Marking initMarking = new Marking();

		for (Place p : net.getPlaces()) {
			if (net.getInEdges(p).isEmpty())
				initMarking.add(p);
		}

		return initMarking;
	}

	private static Map<Transition, Integer> constructMOSCostFunction(PetrinetGraph net) {
		Map<Transition, Integer> costMOS = new HashMap<Transition, Integer>();

		for (Transition t : net.getTransitions())
			if (t.isInvisible() || t.getLabel().equals(""))
				costMOS.put(t, 0);
			else
				costMOS.put(t, 1);

		return costMOS;
	}

	private static Map<XEventClass, Integer> constructMOTCostFunction(PetrinetGraph net, XLog log,
			XEventClassifier eventClassifier, XEventClass dummyEvClass) {
		Map<XEventClass, Integer> costMOT = new HashMap<XEventClass, Integer>();
		XLogInfo summary = XLogInfoFactory.createLogInfo(log, eventClassifier);

		for (XEventClass evClass : summary.getEventClasses().getClasses()) {
			costMOT.put(evClass, 1);
		}

		//		costMOT.put(dummyEvClass, 1);

		return costMOT;
	}

	private static TransEvClassMapping constructMapping(PetrinetGraph net, XLog log, XEventClass dummyEvClass,
			XEventClassifier eventClassifier) {
		TransEvClassMapping mapping = new TransEvClassMapping(eventClassifier, dummyEvClass);

		XLogInfo summary = XLogInfoFactory.createLogInfo(log, eventClassifier);

		for (Transition t : net.getTransitions()) {
			boolean mapped = false;

			for (XEventClass evClass : summary.getEventClasses().getClasses()) {
				String id = evClass.getId();

				if (t.getLabel().equals(id)) {
					mapping.put(t, evClass);
					mapped = true;
					break;
				}
			}

			//			if (!mapped && !t.isInvisible()) {
			//				mapping.put(t, dummyEvClass);
			//			}

		}

		return mapping;
	}
}

