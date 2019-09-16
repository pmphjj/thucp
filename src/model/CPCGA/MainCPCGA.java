package model.CPCGA;
import java.awt.Choice;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.netlib.util.intW;


public class MainCPCGA extends GeneticAlgorithm{
	static int geneSize;
	static int activitySize;
	static String name;
	public MainCPCGA() {
		// TODO Auto-generated constructor stub
		super(geneSize,activitySize,name,100,300,10,0.8,0.1);
	}
	public MainCPCGA(int geneS,int act,String name,int pop_size, int max_iter_num, int max_epoch_num, double cross_rate, double mutate_rate){
		// TODO Auto-generated constructor stub
		super(geneS,act,name,pop_size,max_iter_num,max_epoch_num,cross_rate,mutate_rate);
	}



	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String address = "D:\\hjj\\清华\\实验室工作\\临床诊疗过程符合性判定\\CPLGA\\";
		/*
		 * 脑出血, 	脑出血临床路径.json
		 * ('O80.901', 52456)
		 * ('I63.902', 42386)  大面积脑梗死,		脑梗死临床路径.json
		 * ('J06.903', 31808)  上呼吸道感染,		急性上呼吸道感染.json
		 * ('J18.901', 17795)  非典型性肺炎
		 * ('I10.02', 14062)   高血压
		 * ('J98.402', 13246)  肺部典型增生
		 * ('J18.003', 10901)  支气管肺炎（小叶性肺炎）
		 * ('J42.02', 10590)   慢性支气管炎,		慢性支气管炎.json
		 * ('J20.904', 10555)	急性支气管炎,	15急性支气管炎临床路径.json
		 * ('I25.100', 7668)   动脉硬化性心脏病, 冠状动脉粥样硬化性心脏病.json
		 * ('J04.102', 7661)   气管炎,			急性气管支气管炎（县医院适用版）.json
		 * ('G45.001', 6737)	基底动脉尖综合征
		 * ('I69.301', 6578)	脑梗死后遗症
		 * ('K80.203', 6476)	胆囊结石嵌顿,
		 * ('E14.901', 6423)	糖尿病,			2型糖尿病.json
		 * ('J03.903', 6054)	急性扁桃体炎,	急性扁桃体炎.json
		 * ('J44.901', 5267)	慢性阻塞性肺疾病,	慢性阻塞性肺疾病.json
		 * ('I61.902', 5217)	脑出血,			脑出血临床路径.json
		 * ('I27.901', 5102)	肺源性心脏病,	慢性肺源性心脏病.json
		 * ('K52.912', 5095)	急性胃肠炎,		急性胃肠炎（县医院适用版）
		 * ('J06.802', 4726)	扁桃体咽炎
		 * K35.902:急性阑尾炎
		 * M06.991:类风湿性关节炎 NOS
		 * K92.204:上消化道出血
		 * S72.301:股骨干骨折
		 * N20.002:肾结石
		 * K29.502:慢性胃炎
		 * I25.104:冠状动脉粥样硬化性心脏病
		 * XJB00001:剖宫产
		 * K52.912:急性胃肠炎
		 * J45.903:支气管哮喘，非危重
		 * J94.808:胸腔积液
		 * H16.901:角膜炎
		 * M51.202:腰椎间盘脱出
		 * S32.001:腰椎骨折
		 * J06.902:急性上呼吸道感染
		 * K40.903:腹股沟斜疝(单侧)
		 * K40.902:腹股沟疝(单侧)
		 * K25.902:胃溃疡
		 *
		*/
		String infileName = "";
		String inJsonName = "";
		ArrayList<String> infilelist = new ArrayList<String>();
		ArrayList<String> inJsonlist = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(address+"通用性实验疾病名称表.txt"))));
		String line = "";
		while((line = in.readLine()) != null) {
			System.out.println(line);
			String[] words = line.split(",");
			infilelist.add(words[0]);
			inJsonlist.add(words[1]);
		}
		in.close();
		for (int i = 0; i < infilelist.size(); i++) {
			infileName = infilelist.get(i);
			inJsonName = inJsonlist.get(i);
			String fileType = ".csv";
			String inputJson = "D:\\hjj\\清华\\实验室工作\\jsonCP\\"+inJsonName+".json";
			String inputFile = address+infileName+fileType;
			String outputfile = address+infileName+"-PMINPUT.csv";
			IO io = new IO();
			ArrayList<Patient> patients = io.readCSV(inputFile);
			HashMap<String, Integer> c2iHashMap = io.getCToIMap();
			HashMap<Integer, String> i2cHashMap = io.getIToCMap();
			geneSize = c2iHashMap.size();
			ArrayList<CPStage> cpStages = io.readJson(inputJson);
			HashMap<String, Integer> a2iHashMap = io.getAToIMap();
			HashMap<Integer, String> i2aHashMap = io.getIToAMap();
			activitySize = a2iHashMap.size();
			name = inJsonName;

			MainCPCGA test = new MainCPCGA();
			test.setPatients(patients);
			test.setCPStages(cpStages);
			test.setDdWindow(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+infileName+"——适应度曲线"));
			test.setDdWindow1(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+infileName+"——入径率曲线"));
			test.setDdWindow2(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+infileName+"——变异率曲线"));
	        test.caculte();
	        test.show(i2cHashMap, i2aHashMap, cpStages,inJsonName,io.getdeleteDetail());
//	        test.showErrorOD(i2cHashMap);
	        test.getFileForPM(inputFile, outputfile, c2iHashMap,i2aHashMap);
		}


	}



	public void show(HashMap<Integer, String> i2cHashMap, HashMap<Integer, String> i2aHashMap, ArrayList<CPStage> cpStages,String cpName, ArrayList<String> deleteDetail) throws Exception {
		// TODO Auto-generated method stub
		Chromosome bestChro = this.getBestChromosome();
		ArrayList<Integer>[] actList = new ArrayList[activitySize];
		for (int i = 0; i < actList.length; i++) {
			actList[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < bestChro.gene.length; i++) {
			actList[bestChro.gene[i]].add(i);
		}
		String outputAddress = "D:\\hjj\\清华\\实验室工作\\临床诊疗过程符合性判定\\CPLGA\\新res2\\本地化临床路径\\";
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputAddress+cpName+".txt"))));
		for (int i = 0; i < deleteDetail.size(); i++) {
			wr.write(deleteDetail.get(i));
			wr.newLine();
		}

		for (int i = 0; i < cpStages.size(); i++) {
			wr.write("阶段 "+(i+1)+"  ******************************");
			wr.newLine();
			for(Integer activityId:cpStages.get(i).activityIDSet){
				wr.write(i2aHashMap.get(activityId)+":   ");
				for (int j = 0; j < actList[activityId].size(); j++) {
					int orderId = actList[activityId].get(j);
					wr.write(i2cHashMap.get(orderId));
					if (j != actList[activityId].size()-1) {
						wr.write(", ");
					}
				}
				wr.newLine();
				wr.newLine();
			}
		}

		wr.close();
	}



	@Override
	public void show(HashMap<Integer, String> i2cHashMap, HashMap<Integer, String> i2aHashMap,
			ArrayList<CPStage> cpStages) {
		// TODO Auto-generated method stub

	}


}
