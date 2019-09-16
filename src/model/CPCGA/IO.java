package model.CPCGA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class IO {
	HashMap<String, Patient> pidToPatientMap = new HashMap<String, Patient>();
	HashMap<String, Integer> c2iHashMap;
	HashMap<Integer, String> i2cHashMap;
	HashMap<String, Integer> a2iHashMap;
	HashMap<Integer, String> i2aHashMap;
	int dataSize = 0;
	ArrayList<String> deleteDetail;

	public IO() {
		pidToPatientMap = new HashMap<String, Patient>();
		c2iHashMap = new HashMap<String, Integer>();
		i2cHashMap = new HashMap<Integer, String>();
		a2iHashMap = new HashMap<String, Integer>();
		i2aHashMap = new HashMap<Integer, String>();
		deleteDetail = new ArrayList<String>();
	}

	public HashMap<String, Integer> getCToIMap() {
		return c2iHashMap;
	}

	public HashMap<Integer, String> getIToCMap() {
		return i2cHashMap;
	}

	public ArrayList<CPStage> readJson(String inputJson) throws Exception {
		System.out.println("正在读取国家标准临床路径文件......");
		ArrayList<CPStage> cList = new ArrayList<CPStage>();
		JSONObject jsonInput;
		jsonInput = readJsonFile(inputJson);
		int index = 0;
		for (int i = 0; i < jsonInput.length(); i++) {
			JSONArray jsarr = jsonInput.getJSONArray(String.valueOf(i+1));
			CPStage cps = new CPStage();
//			System.out.println(jsarr.toString());
			for (int j = 0; j < jsarr.length(); j++) {
				JSONObject obj = jsarr.getJSONObject(j);
				if (obj.keySet().size() == 1 && obj.keySet().contains("重点医嘱")) {
					JSONArray strs = obj.getJSONArray("重点医嘱");
					for (int k = 0; k < strs.length(); k++) {
						JSONObject objOrder = new JSONObject();
						objOrder = strs.getJSONObject(k);
						if (objOrder.keySet().contains("长期医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("长期医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								if (!a2iHashMap.containsKey(strs1.getString(l))) {
									a2iHashMap.put(strs1.getString(l), index);
									i2aHashMap.put(index, strs1.getString(l));
									index++;
								}
								cps.activityIDSet.add(a2iHashMap.get(strs1.getString(l)));
							}
						}
						if (objOrder.keySet().contains("临时医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("临时医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								if (!a2iHashMap.containsKey(strs1.getString(l))) {
									a2iHashMap.put(strs1.getString(l), index);
									i2aHashMap.put(index, strs1.getString(l));
									index++;
								}
								cps.activityIDSet.add(a2iHashMap.get(strs1.getString(l)));
							}
						}
						if (objOrder.keySet().contains("出院医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("出院医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								if (!a2iHashMap.containsKey(strs1.getString(l))) {
									a2iHashMap.put(strs1.getString(l), index);
									i2aHashMap.put(index, strs1.getString(l));
									index++;
								}
								cps.activityIDSet.add(a2iHashMap.get(strs1.getString(l)));
							}
						}
					}
				}
			}
			cList.add(cps);
		}
		return cList;
	}

	public JSONObject readJsonFile(String filepath) throws Exception{
        JSONTokener jsonTokener = new JSONTokener(new FileReader(new File(filepath)));
        JSONObject jsonObject = new JSONObject(jsonTokener);
//        System.out.println(jsonObject);
        return jsonObject;
    }

	public static void main(String[] args) throws IOException {
		String inputFile = "D:\\hjj\\清华\\实验室工作\\临床诊疗过程符合性判定\\全病种数据库\\Mapping_FMC_Data.csv";
		String[] ICDCodes = {"N20.002","S32.001","S72.301","H16.901","K40.902",
				"I25.104","J94.808","M06.991","K25.902","K40.903",
				"K29.502","J45.903","J06.902","K92.204","XJB00001",
				"M51.202","K52.912","K35.902","I27.901","I63.902",
				"I61.902","J44.901","J03.903","E14.901","J04.102",
				"I25.100","J20.904","J42.02","J06.903"};
		String outputFileAddress = "D:\\hjj\\清华\\实验室工作\\临床诊疗过程符合性判定\\GACP\\";
		generateCSV(inputFile,ICDCodes,outputFileAddress);

//		IO io = new IO();
//		io.getFirstDiagList(inputFile,outputFileAddress+"各病种只包含第一诊断的病人情况统计表.txt");
	}

	public void getFirstDiagList(String inputFile,String outputFile) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		String line = null;
		line = br.readLine();
		System.out.println(line);
		int indexOfPID = -1, indexOfJBID = -1, indexOfF_JBMC = -1, indexOfF_JBBM2 = -1, indexOfF_JBBM3 = -1;
		String[] words = line.split(",");
		for(int i = 0; i < words.length; i++) {
			if(words[i].equals("GRBM")) indexOfPID = i;
			if(words[i].equals("F_JBID")) indexOfJBID = i;
			if(words[i].equals("F_JBMC")) indexOfF_JBMC = i;
			if(words[i].equals("F_JBBM2")) indexOfF_JBBM2 = i;
			if(words[i].equals("F_JBBM3")) indexOfF_JBBM3 = i;
		}

		HashMap<String, Integer> diag2countMap = new HashMap<String, Integer>();
		HashMap<String, Integer> diag2count2Map = new HashMap<String, Integer>();
		HashMap<String, String> diag2chiNameMap = new HashMap<String, String>();
		HashSet<String> pIdSet = new HashSet<String>();
		while((line = br.readLine()) != null) {
			words = line.split(",");
			String pid = words[indexOfPID];
			String diag1 = words[indexOfJBID];
			String chiName = "";
			chiName = words[indexOfF_JBMC];
			String diag2 = ""; diag2 = words[indexOfF_JBBM2];
			String diag3 = ""; diag3 = words[indexOfF_JBBM3];

			if(diag2.equals("") && diag3.equals("")) {
				if(!pIdSet.contains(pid)) {
					diag2chiNameMap.put(diag1, chiName);
					pIdSet.add(pid);
					if (!diag2countMap.containsKey(diag1)) {
						diag2countMap.put(diag1, 1);
					}
					else {
						diag2countMap.put(diag1, 1+diag2countMap.get(diag1));
					}
				}
				if (!diag2count2Map.containsKey(diag1)) {
					diag2count2Map.put(diag1, 1);
				}
				else {
					diag2count2Map.put(diag1, 1+diag2count2Map.get(diag1));
				}
			}

		}
		br.close();
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))));
		for(String diag1:diag2chiNameMap.keySet()) {
			wr.write(diag1+","+diag2chiNameMap.get(diag1)+","+diag2countMap.get(diag1)+","+diag2count2Map.get(diag1));
			wr.newLine();
		}

		wr.close();

	}

	public static void generateCSV(String inputFile, String[] ICDCode, String outputFileAddress) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		String line = null;
		line = br.readLine();
//		System.out.println(line);
		int indexOfPID = -1, indexOfOdName = -1, indexOfTime = -1, indexOfJBID = -1, indexOfF_JBMC = -1, indexOfType = -1;
		String[] words = line.split(",");
		for(int i = 0; i < words.length; i++) {
			if(words[i].equals("GRBM")) indexOfPID = i;
			if(words[i].equals("F_MC")) indexOfOdName = i;
			if(words[i].equals("F_BCRQ")) indexOfTime = i;
			if(words[i].equals("F_JBID")) indexOfJBID = i;
			if(words[i].equals("F_JBMC")) indexOfF_JBMC = i;
			if(words[i].equals("F_LBBM")) indexOfType = i;
		}

		BufferedWriter[] wr = new BufferedWriter[ICDCode.length];
		for (int i = 0; i < wr.length; i++) {
			wr[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileAddress+ICDCode[i]+".csv"))));
			wr[i].write("GRBM,F_MC,F_RQ,F_LBBM");
			wr[i].newLine();
		}
		HashMap<String, Integer> name2indexMap = new HashMap<String,Integer>();
		for (int i = 0; i < ICDCode.length; i++) {
			name2indexMap.put(ICDCode[i], i);
		}
		HashMap<String, String> nameMap = new HashMap<String,String>();
		while((line = br.readLine())!= null) {
			words = line.split(",");
			String resString = words[indexOfPID]+","+words[indexOfOdName]+","+words[indexOfTime]+","+words[indexOfType];
			if (name2indexMap.containsKey(words[indexOfJBID])) {
				int i = name2indexMap.get(words[indexOfJBID]);
				wr[i].write(resString);
				wr[i].newLine();
//				System.out.println(resString);
				if (!nameMap.containsKey(words[indexOfJBID])) {
					nameMap.put(words[indexOfJBID], words[indexOfF_JBMC]);
				}

			}
		}
		br.close();

		for (int i = 0; i < wr.length; i++) {
			wr[i].close();
		}
		//输出疾病编码与对应的中文名称
		for (String name:nameMap.keySet()) {
			System.out.println(name+":"+nameMap.get(name));
		}
	}

	public ArrayList<Patient> readCSV(String inputFile) throws IOException {
		System.out.println("正在读取收费项数据："+inputFile+"   ......");
		ArrayList<Patient> pList = new ArrayList<Patient>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		String line = null;
		line = br.readLine();
		System.out.println(line);
		int indexOfPID = -1, indexOfOdName = -1, indexOfTime = -1, indexOfType = -1;
		String[] words = line.split(",");
		for(int i = 0; i < words.length; i++) {
			if(words[i].equals("GRBM")) indexOfPID = i;
			if(words[i].equals("F_MC")) indexOfOdName = i;
			if(words[i].equals("F_RQ")) indexOfTime = i;
			if(words[i].equals("F_LBBM")) indexOfType = i;

		}
		String PID = "", ODNAME = "", TIME = "", Type = "";
		int index = 0,countBed = 0, countZY = 0, countTSQC = 0;
		HashSet<String> deleteName = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			words = line.split(",");
			//跳过床位费、中草药费、特殊耗材费
			if (indexOfType != -1 && (words[indexOfType].equals("1") || words[indexOfType].equals("4") || words[indexOfType].equals("20")) ) {
				if (words[indexOfType].equals("1")) {
					countBed++;
				}
				if (words[indexOfType].equals("4")) {
					countZY++;
				}
				if(words[indexOfType].equals("20")){
					countTSQC++;
				}
				deleteName.add(words[indexOfOdName]);
				continue;
			}
			index++;
		}
		dataSize = index;
		br.close();
		String res = "";
		res = "被删除的床位费、中草药费、特殊耗材费的项目数量："+deleteName.size()+",床位费记录数："+countBed+",中草药记录数："+countZY+",特殊耗材费记录数："+countTSQC;
		System.out.println(res);
		deleteDetail.add(res);
		res = "";
		for(String name:deleteName) {
			res += name+",";
		}
		System.out.println(res);
		deleteDetail.add(res);
		res = "";
		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
		String clinicalOrder[][] = new String[index][3];
		index = 0;
		line = br.readLine();
		int c = 0, orderIndex = 0;
		while ((line = br.readLine()) != null) {
			words = line.split(",");
			if (indexOfType != -1 && (words[indexOfType].equals("1") || words[indexOfType].equals("4") || words[indexOfType].equals("20")) ) continue;
			PID = words[indexOfPID];
			ODNAME = words[indexOfOdName];
			TIME = words[indexOfTime];
//			System.out.println((c++)+":"+line);
			clinicalOrder[index][0] = PID;
			clinicalOrder[index][1] = ODNAME;
			clinicalOrder[index][2] = TIME;
			index++;
			line = null;
			words = null;

			if(!c2iHashMap.containsKey(ODNAME)) {
				c2iHashMap.put(ODNAME, orderIndex);
				i2cHashMap.put(orderIndex, ODNAME);
				orderIndex++;
			}
		}
		br.close();
/*按病人id排序*/
//		Arrays.sort(clinicalOrder, new Comparator<String[]>(){
//            @Override
//            public int compare(String[] o1, String[] o2) {
//                return o1[0].compareTo(o2[0]);
//            }
//        });

/*对每个病人的记录按时间排序*/
//		String curID = "",lastID = "", ordName;
//		int startIndex = 0, endIndex = 0;
//		int patientsSize = 0;
//		index = 0;
//		String curPatientName = "",lastPatientNameString = "";
//		for(int i = 0; i < clinicalOrder.length; i++){
//			curPatientName = clinicalOrder[i][0];
//			if (!curPatientName.equals(lastPatientNameString)) {
//				patientsSize++;
//				lastPatientNameString = curPatientName;
//			}
//			ordName = clinicalOrder[i][1];
//			if(!c2iHashMap.containsKey(ordName)) {
//				c2iHashMap.put(ordName, index);
//				i2cHashMap.put(index, ordName);
//				index++;
//			}
//			curID = clinicalOrder[i][0];
//			if(lastID == "") lastID = curID;
//			if(!curID.equals(lastID)) {
//				Arrays.sort(clinicalOrder, startIndex, endIndex+1, new Comparator<String[]>(){
//		            @Override
//		            public int compare(String[] o1, String[] o2) {
//		            	return o1[2].compareTo(o2[2]);
//		            }
//		        });
//				startIndex = i;
//				endIndex = i;
//			}
//			else endIndex = i;
//			lastID = curID;
//		}
//		Arrays.sort(clinicalOrder, startIndex, endIndex+1, new Comparator<String[]>(){
//            @Override
//            public int compare(String[] o1, String[] o2) {
//            	return o1[2].compareTo(o2[2]);
//            }
//        });

//		for (int i = 0; i < clinicalOrder.length; i++) {
//			System.out.println(clinicalOrder[i][0] + " " + clinicalOrder[i][1] + " " + clinicalOrder[i][2]);
//		}
		line = "";
		ArrayList<Integer> list = new ArrayList<Integer>();
		HashSet<Integer> dayOrders = new HashSet<Integer>();
		String lastIdString = "", curIdString = "", lastDateString = "", curDateString = "";
		System.out.println("正在生成病人诊疗过程收费项目序列......");
		for (int i = 0; i < clinicalOrder.length; i++) {
			curIdString = clinicalOrder[i][0];
			if(lastIdString == "") lastIdString = curIdString;
			curDateString = clinicalOrder[i][2];
			if(lastDateString == "") {
				lastDateString = curDateString;
			}
			if(curIdString.equals(lastIdString)) {
				if(curDateString.equals(lastDateString)) {
					dayOrders.add(c2iHashMap.get(clinicalOrder[i][1]));
				}
				else {
					if (!dayOrders.isEmpty()) {
						for (Integer integer : dayOrders) {
							line = line + integer.toString() + " ";
							list.add(integer);
						}
						line = line + "-1" + " ";
						list.add(-1);
						dayOrders.clear();
					}
					dayOrders.add(c2iHashMap.get(clinicalOrder[i][1]));
				}
			}
			else {
				if (!dayOrders.isEmpty()) {
					for (Integer integer : dayOrders) {
						line = line + integer.toString() + " ";
						list.add(integer);
					}
					line = line + "-1" + " "+"-2";
					list.add(-1);
					list.add(-2);
					dayOrders.clear();
				}
				Patient pt = new Patient();
				pt.setPatientID(curIdString);
				pt.setList(list);
				pList.add(pt);
				line = "";
				list = null;
				list = new ArrayList<Integer>();
				dayOrders.add(c2iHashMap.get(clinicalOrder[i][1]));
			}
			lastDateString = curDateString;
			lastIdString = curIdString;
		}
		if (!dayOrders.isEmpty()) {
			for (Integer integer : dayOrders) {
				line = line + integer.toString() + " ";
				list.add(integer);
			}
			line = line + "-1" + " "+"-2";
			list.add(-1);
			list.add(-2);
			dayOrders.clear();
		}
		Patient pt = new Patient();
		pt.setPatientID(curIdString);
		pt.setList(list);
		pList.add(pt);
		return pList;
	}

	public HashMap<String, Integer> getAToIMap() {
		// TODO Auto-generated method stub
		return a2iHashMap;
	}

	public HashMap<Integer, String> getIToAMap() {
		// TODO Auto-generated method stub
		return i2aHashMap;
	}

	public ArrayList<String> getdeleteDetail() {
		// TODO Auto-generated method stub
		return deleteDetail;
	}
}
