package outlier.launch;

import java.io.File;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.packages.PackageManager;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.impl.PluginManagerImpl;
import org.processmining.log.csv.CSVFileReferenceUnivocityImpl;
import org.processmining.log.csv.ICSVReader;
import org.processmining.log.csv.config.CSVConfig;
import org.processmining.log.csvimport.CSVConversion;
import org.processmining.log.csvimport.CSVConversion.ConversionResult;
import org.processmining.log.csvimport.CSVConversion.NoOpProgressListenerImpl;
import org.processmining.log.csvimport.CSVConversion.ProgressListener;
import org.processmining.log.csvimport.config.CSVConversionConfig;
import org.processmining.log.csvimport.config.CSVConversionConfig.CSVEmptyCellHandlingMode;
import org.processmining.log.csvimport.config.CSVConversionConfig.CSVErrorHandlingMode;
import org.processmining.log.csvimport.config.CSVConversionConfig.CSVMapping;
import org.processmining.log.csvimport.config.CSVConversionConfig.Datatype;
import org.processmining.log.csvimport.exception.CSVConversionException;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMi;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.petrinet.PetriNetVisualization;

import com.google.common.collect.ImmutableList;

public class IMLaunch {
	public static JComponent resultJComponent;
	public static void lauch(String logPath) throws CSVConversionException {
		
		PackageManager packages = PackageManager.getInstance();
		//		 Then the plugin manager, as it listens to the package manager
		PluginManagerImpl.initialize(UIPluginContext.class);
		//		
		UIContext globalContext;
		globalContext = new UIContext();
		globalContext.initialize();
		//		final UITopiaController controller = new UITopiaController(globalContext);
		//		globalContext.setController(controller);
		//		globalContext.setFrame(controller.getFrame());
		//		controller.getFrame().setIconImage(ImageLoader.load("prom_icon_32x32.png"));
		//		controller.getFrame().setVisible(true);
		////		controller.getMainView().showWorkspaceView();
		////		controller.getMainView().getWorkspaceView().showFavorites();
		////		globalContext.startup();

		File file = new File(logPath);
		CSVFileReferenceUnivocityImpl csvFile = new CSVFileReferenceUnivocityImpl(file.toPath());
		CSVConfig config = new CSVConfig(csvFile);
		try (ICSVReader reader = csvFile.createReader(config)) {
			CSVConversion conversion = new CSVConversion();
			CSVConversionConfig conversionConfig = new CSVConversionConfig(csvFile, config);
			conversionConfig.autoDetect();
			conversionConfig.setCaseColumns(ImmutableList.of("case"));
			conversionConfig.setEventNameColumns(ImmutableList.of("event"));
			conversionConfig.setCompletionTimeColumn("time");
			conversionConfig.setEmptyCellHandlingMode(CSVEmptyCellHandlingMode.SPARSE);
			conversionConfig.setErrorHandlingMode(CSVErrorHandlingMode.ABORT_ON_ERROR);
			Map<String, CSVMapping> conversionMap = conversionConfig.getConversionMap();
			CSVMapping mapping = conversionMap.get("time");
			mapping.setDataType(Datatype.TIME);
			mapping.setPattern("yyyy/MM/dd");

			final ProgressListener progressListener = new NoOpProgressListenerImpl();
			ConversionResult<XLog> result = conversion.doConvertCSVToXES(progressListener, csvFile, config,
					conversionConfig);
			XLog log = result.getResult();
			System.out.println(log.size());
			PluginContext context = globalContext.getMainPluginContext();
			IMPetriNet ins = new IMPetriNet();
			Object[] re = ins.minePetriNet(context, log, new MiningParametersIMi());
			PetriNetVisualization v = new PetriNetVisualization();
			resultJComponent = v.visualize(context, (Petrinet) re[0], (Marking) (re[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
