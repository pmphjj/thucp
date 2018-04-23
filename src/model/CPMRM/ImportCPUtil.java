package model.CPMRM;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class ImportCPUtil {
    JSONObject jsonObjDay = new JSONObject();//创建json格式的数据;//创建json格式的数据
    JSONArray jsonDayArr ;
    JSONObject jsonObjName ;//时间名称
    JSONObject jsonObjCoreOrders ;//重点医嘱
    JSONObject jsonObj ;
    JSONArray jsonArr ;//json格式的数组
    JSONObject jsonObjCoreActivities ; //主要诊疗工作
    JSONObject jsonObjCoreService ;//主要护理工作


    private void generate() {
    	try {
	    	 //阶段一
    		 initialize();
	    	 jsonObjName.put("名称", "第1 天");

	    	 String[] strsAct = new String[] {"询问病史、体格检查", "采集中医四诊信息","进行中医证候判断","下医嘱开出各项检查单",
	    			  "完成入院记录初步诊断","初步拟定诊疗方案","密切观察基础疾病，必要时请专科会诊"};
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct);

	    	 String[] strsLong = new String[] {
	    			 "专科护理常规",
	    			 "分级护理",
	    			 "饮食调摄",
	    			 "卧床休息",
	    			 "疾病分期",
	    			 "辩证分型",
	    			 "物理治疗",
	    			 "其他治疗方法"
	    			 };
	    	 String[] strsCur = new String[] {
	    			 "血常规、尿常规、大便常规",
	    			 "腰椎X线片、CT/MRI",
	    			 "生化检查",
	    			 "心电图",
	    			 "胸透或胸部X线片",
	    			 "对症治疗" // "依据病情需要"
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong);
	    	 jsonObj.put("临时医嘱", strsCur);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ = new String[] {
	        		 "入院介绍",
	        		 "入院健康教育、饮食指导",
	        		 "介绍入院注意事项",
	        		 "执行诊疗护理措施",
	        		 "安排并指导陪护工作"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("1", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());

	         clear();
	       //阶段二
	         initialize();
	         jsonObjName.put("名称", "第2天");

	    	 String[] strsAct1 = new String[] {
	    			"实施各项实验室检查和影像学检查",
	    			"完成上级医师查房，进一步明确诊断，指导治疗",
	    			"向家属交代病情和治疗注意事项",
	    			"实施手法等治疗措施"
	    			};
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct1);

	    	 String[] strsLong1 = new String[] {
	    			 "专科护理常规",
	    			 "分级护理",
	    			 "饮食调摄",
	    			 "卧床休息",
	    			 "口服中药汤剂",
	    			 "口服中成药",
	    			 "静脉滴注中药注射剂",
	    			 "松解类手法治疗",
	    			 "整复类手法治疗",
	    			 "其他手法治疗",
	    			 "腰椎牵引疗法",
	    			 "物理治疗",
	    			 "针刺",
	    			 "灸法",
	    			 "其他治疗方法"
	    			 };
	    	 String[] strsCur1 = new String[] {
	    			 "必要时请相关科室会诊",
	    			 "对症治疗"
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong1);
	    	 jsonObj.put("临时医嘱", strsCur1);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ1 = new String[] {
	        		 "按医嘱完成护理操作、日常治疗",
	        		 "完成常规生命体征监测",
	        		 "功能指导训练"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ1);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("2", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());
	         clear();
	       //阶段三
	         initialize();
	    	 jsonObjName.put("名称", "第3-6天");

	    	 String[] strsAct2 = new String[] {
	    			  "上级医师查房明确诊断及诊疗评估",
	    			  "根据患者病情变化及时调整治疗方案"
	    			  };
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct2);

	    	 String[] strsLong2 = new String[] {
	    			 "专科护理常规",
	    			 "分级护理",
	    			 "饮食调摄",
	    			 "卧床休息",
	    			 "口服中药汤剂",
	    			 "口服中成药",
	    			 "静脉滴注中药注射剂",
	    			 "松解类手法治疗",
	    			 "整复类手法治疗",
	    			 "其他手法治疗",
	    			 "腰椎牵引疗法",
	    			 "物理治疗",
	    			 "针刺",
	    			 "灸法",
	    			 "其他治疗方法"
	    			 };
	    	 String[] strsCur2 = new String[] {
	    			 "必要时复查异常项目", //异常化验复查
	    			 "必要时请相关科室会诊并对症治疗",
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong2);
	    	 jsonObj.put("临时医嘱", strsCur2);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ2 = new String[] {
	        		 "按医嘱执行护理措施", //正确执行医嘱
	        		 "饮食指导",
	        		 "安抚疏导、健康教育",
	        		 "功能指导训练"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ2);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("3", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());
	         clear();
	       //阶段四
	         initialize();
	    	 jsonObjName.put("名称", "第7天");

	    	 String[] strsAct3 = new String[] {
	    			 "分析总结临床治疗效果",
	    			 "根据患者病情变化及时调整治疗方案",
	    			 "上级医师查房作出进一步的诊疗评估"
	    			 };
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct3);

	    	 String[] strsLong3 = new String[] {
	    			 "专科护理常规",
	    			 "分级护理",
	    			 "饮食调摄",
	    			 "卧床休息",
	    			 "口服中药汤剂",
	    			 "口服中成药",
	    			 "静脉滴注中药注射剂",
	    			 "松解类手法治疗",
	    			 "整复类手法治疗",
	    			 "其他手法治疗",
	    			 "腰椎牵引疗法",
	    			 "物理治疗",
	    			 "针刺",
	    			 "灸法",
	    			 "其他治疗方法"
	    	 		 };
	    	 String[] strsCur3 = new String[] {
	    			 "必要时复查异常项目",
	    			 "必要时相关科室会诊",
	    			 "对症治疗"
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong3);
	    	 jsonObj.put("临时医嘱", strsCur3);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ3 = new String[] {
	        		 "按医嘱执行诊疗护理措施", //正确执行医嘱
	        		 "饮食指导",
	        		 "安抚疏导、健康教育",
	        		 "功能指导训练"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ3);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("4", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());
	         clear();

	       //阶段五
	         initialize();
	    	 jsonObjName.put("名称", "第8-13 天");

	    	 String[] strsAct4 = new String[] {
	    			  "根据患者病情变化及时调整治疗方案",
	    			  "上级医师查房作出进一步的诊疗评估",
	    			  "强调运动疗法及康复疗法的应用"
	    			  };
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct4);

	    	 String[] strsLong4 = new String[] {
	    			 "专科护理常规",
	    			 "分级护理",
	    			 "饮食调摄",
	    			 "卧床休息",
	    			 "口服中药汤剂",
	    			 "口服中成药",
	    			 "静脉滴注中药注射剂",
	    			 "松解类手法治疗",
	    			 "整复类手法治疗",
	    			 "其他手法治疗",
	    			 "腰椎牵引疗法",
	    			 "物理治疗",
	    			 "针刺",
	    			 "灸法",
	    			 "其他治疗方法"
	    	 		};
	    	 String[] strsCur4 = new String[] {
	    			 "必要时复查异常项目",
	    			 "必要时相关科室会诊",
	    			 "对症治疗"
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong4);
	    	 jsonObj.put("临时医嘱", strsCur4);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ4 = new String[] {
	        		 "按医嘱执行诊疗护理措施", //正确执行医嘱
	        		 "饮食指导",
	        		 "安抚疏导、健康教育",
	        		 "功能指导训练"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ4);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("5", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());
	         clear();

	       //阶段六
	         initialize();
	    	 jsonObjName.put("名称", "第14 天（出院日）");

	    	 String[] strsAct5 = new String[] {
	    			 "交代出院注意事项、复查日期",
	    			 "完成出院记录",
	    			 "通知出院",
	    			 "制定康复计划，指导患者出院后功能锻炼",
	    			 "开具出院诊断书"
	    			 };
	    	 jsonObjCoreActivities.put("主要诊疗工作",strsAct5);

//	    	 String[] strsLong5 = new String[] {"通知出院", "依据病情给予出院带药及建议", "出院带药"};
//	    	 jsonObj.put("出院医嘱",strsLong5);
	    	 String[] strsLong5 = new String[] {
	    			 "停止所有长期医嘱"
	    	 		};
	    	 String[] strsCur5 = new String[] {
	    			 "开具出院医嘱",
	    			 "出院带药"
	    			 };
	    	 jsonObj.put("长期医嘱",strsLong5);
	    	 jsonObj.put("临时医嘱", strsCur5);
	         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
	         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。

	         String[] strsServ5 = new String[] {
	        		 "协助办理出院手续",
	        		 "送病人出院",
	        		 "交代出院后注意事项",
	        		 "功能指导训练"
	        		 };
	         jsonObjCoreService.put("主要护理工作", strsServ5);

	         jsonDayArr.put(jsonObjName);
	         jsonDayArr.put(jsonObjCoreActivities);
	         jsonDayArr.put(jsonObjCoreOrders);
	         jsonDayArr.put(jsonObjCoreService);
	         jsonObjDay.put("6", jsonDayArr);

//	         System.out.println(jsonObjDay.toString());
	         clear();
    	}
    	catch (JSONException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    public void initialize(){

        jsonDayArr = new JSONArray();
        jsonObjName = new JSONObject();
        jsonObjCoreOrders = new JSONObject();
        jsonObj = new JSONObject();
        jsonArr = new JSONArray();//json格式的数组
        jsonObjCoreActivities = new JSONObject();
        jsonObjCoreService = new JSONObject();
    }

    public void clear() {
    	jsonDayArr = null;
        jsonObjName = null;
        jsonObjCoreOrders = null;
        jsonObj = null;
        jsonArr = null;
        jsonObjCoreActivities = null;
        jsonObjCoreService = null;
	}

    public JSONObject getJsonStages(){
    	return jsonObjDay;
    }

    public void writeFile(String filePath, String sets)
            throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(sets);
        out.println();
        fw.close();
        out.close();
    }

    public JSONObject readJsonFile(String filepath) throws Exception{
        JSONTokener jsonTokener = new JSONTokener(new FileReader(new File(filepath)));
        JSONObject jsonObject = new JSONObject(jsonTokener);
//        System.out.println(jsonObject);
        return jsonObject;
    }

    public StandardCPStage[] readJsonInput(String filename) throws Exception {
    	JSONObject jsonInput;
		jsonInput = this.readJsonFile(filename);
		StandardCPStage[] CPS = new StandardCPStage[jsonInput.length()];
		for (int i = 0; i < CPS.length; i++) {
			CPS[i] = new StandardCPStage();
		}
//		System.out.println(CPS.length);
		for (int i = 0; i < CPS.length; i++) {
			JSONArray jsarr = jsonInput.getJSONArray(String.valueOf(i+1));
//			System.out.println(jsarr.toString());
			for (int j = 0; j < jsarr.length(); j++) {
				JSONObject obj = jsarr.getJSONObject(j);
				if (obj.keySet().size() == 1 && obj.keySet().contains("名称")) {
					CPS[i].name = obj.getString("名称");
				}
				if (obj.keySet().size() == 1 && obj.keySet().contains("主要诊疗工作")) {
					JSONArray strs = obj.getJSONArray("主要诊疗工作");
					for (int k = 0; k < strs.length(); k++) {
						CPS[i].coreActivities.add(strs.getString(k));
					}
//					System.out.println(CPS[i].coreActivities.toString());
				}
				if (obj.keySet().size() == 1 && obj.keySet().contains("重点医嘱")) {
					JSONArray strs = obj.getJSONArray("重点医嘱");
					for (int k = 0; k < strs.length(); k++) {
						JSONObject objOrder = new JSONObject();
						objOrder = strs.getJSONObject(k);
						if (objOrder.keySet().contains("长期医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("长期医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								CPS[i].coreOrdersLong.add(strs1.getString(l));
							}
						}
						if (objOrder.keySet().contains("临时医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("临时医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								CPS[i].coreOrdersCur.add(strs1.getString(l));
							}
						}
						if (objOrder.keySet().contains("出院医嘱")) {
							JSONArray strs1 = objOrder.getJSONArray("出院医嘱");
							for (int l = 0; l < strs1.length(); l++) {
								CPS[i].coreOrdersOut.add(strs1.getString(l));
							}
						}
//						CPS[i].coreActivities.add(strs.getString(k));
					}
//					System.out.println(CPS[i].coreActivities.toString());
				}
				if (obj.keySet().size() == 1 && obj.keySet().contains("主要护理工作")) {
					JSONArray strs = obj.getJSONArray("主要护理工作");
					for (int k = 0; k < strs.length(); k++) {
						CPS[i].coreServices.add(strs.getString(k));
					}
//					System.out.println(CPS[i].coreActivities.toString());
				}
			}
		}
		return CPS;
    }



	public static void main(String[] args) throws Exception {

     	//生成标准临床路径的json文件
		ImportCPUtil imcp = new ImportCPUtil();
		imcp.generate();
		JSONObject jsonData = imcp.getJsonStages();
		imcp.writeFile("./data/CPMRM/腰痛病(腰椎间盘突出症).json", jsonData.toString());

		//读取json文件
		StandardCPStage[] CPS;
		CPS = imcp.readJsonInput("./data/CPMRM/腰痛病(腰椎间盘突出症).json");

		System.out.println(CPS[0].getCoreActivities());
		System.out.println(CPS[0].getCoreOrders());
		System.out.println(CPS[0].getCoreServices());

		System.out.println(CPS.length);

	}


}
