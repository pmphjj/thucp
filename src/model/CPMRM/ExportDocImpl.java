package model.CPMRM;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableCellDescriptor;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.json.JSONArray;
import org.json.JSONObject;
import org.netlib.util.intW;


/**
 *
 * 读取word表格，支持doc、docx
 */
public class ExportDocImpl {
	public static void main(String[] args) throws Exception {
		ExportDocImpl test = new ExportDocImpl();
		//String filePath="E:\\java导入word表格.doc";
		String basicAddress = "D:\\hjj\\清华\\实验室工作\\所有临床路径";
		String filePath1 = basicAddress + "\\(1011-1212)2017年发布的临床路径\\拟发布临床路径\\耳鼻咽喉科（12个）\\鼻咽部血管瘤.docx";
		String filePath2 = basicAddress + "\\(1011-1212)2017年发布的临床路径\\拟发布临床路径\\精神科（6个）\\县级\\非器质性失眠症（县医院适用版）.docx";
		String filePath3 = basicAddress + "\\(1-524)2016年新发布的临床路径\\100无晶状体眼临床路径.docx";
		String filePath4 = basicAddress + "\\(1-524)2016年新发布的临床路径\\21肾癌内科治疗临床路径.docx";
		String filePath5 = basicAddress + "\\(1-524)2016年新发布的临床路径\\517颊癌临床路径.doc";
		String filePath6 = basicAddress + "\\(587-686)2012年发布的临床路径\\2012年已发布的临床路径(（587-686）\\乳腺癌化疗.doc";
		String filePath7 = basicAddress + "\\(1-524)2016年新发布的临床路径\\107前交通动脉瘤开颅夹闭术临床路径.doc";
		String filePath8 = basicAddress + "\\(1-524)2016年新发布的临床路径\\269.慢性肾衰竭（CKD 5期）临床路径.docx";
		String filePath9 = basicAddress + "\\test.doc";
		String allFileNames = basicAddress + "all.csv";

//		test.readFile(basicAddress);
//		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(allFileNames))));
//		test.readFile(basicAddress,wr);
//		wr.close();
//		test.translateToJson(filePath2);

		ImportCPUtil imcp = new ImportCPUtil();
		String filePath = filePath9;
		//读.docx
//		JSONObject jobj =  test.translateToJson(filePath);
//		String jsonFileName = filePath.substring(0, filePath.length()-4)+"json";
//		if (jobj != null && jobj.length() != 0)
//			imcp.writeFile(jsonFileName, jobj.toString());
//		else
//			System.out.println("haha");
//		读.doc
//		JSONObject jobj =  test.translateToJson(filePath);
//		String jsonFileName = filePath.substring(0, filePath.length()-3)+"json";
//		if (jobj != null && jobj.length() != 0)
//			imcp.writeFile(jsonFileName, jobj.toString());
//		else
//			System.out.println("haha");

	}
	private void readFile(String fileDir, BufferedWriter wr) throws IOException  {
		// TODO Auto-generated method stub

		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		File[] files = file.listFiles();
		if(files == null) return;


		//遍历，目录下所有文件
		for(File f:files) {
			if(f.isFile()) {
				fileList.add(f);
			}
			else if (f.isDirectory()) {
//				System.out.println(f.getAbsolutePath());
				readFile(f.getAbsolutePath(),wr);
			}
		}
		for(File f:fileList) {
			if (f.getName().contains("~")) {
				continue;
			}
//			System.out.println(f.getName());
			if (f.getAbsolutePath().toLowerCase().endsWith("docx") || f.getAbsolutePath().toLowerCase().endsWith("doc")) {
				ImportCPUtil imcp = new ImportCPUtil();
				JSONObject jobj =  translateToJson(f.getAbsolutePath());
				String fileNames = "";
				if (jobj != null && jobj.length() != 0) {
					if (f.getAbsolutePath().toLowerCase().endsWith("docx")) {
						fileNames = f.getName().substring(0, f.getName().length()-5);
					}
					else if(f.getAbsolutePath().toLowerCase().endsWith("doc")) {
						fileNames = f.getName().substring(0, f.getName().length()-4);
					}
					int index = 0;
					for(int i = 0; i < fileNames.length(); i++) {
						if(fileNames.charAt(i) == '.'||(fileNames.charAt(i) >= '0' && fileNames.charAt(i) <= '9')) index++;
						else break;
					}

					wr.write(fileNames.substring(index).trim());
					wr.newLine();
				}
				else {
					System.out.println(f.getName());
				}

			}

		}

	}
	public void readFile(String fileDir) throws IOException {
		List<File> fileList = new ArrayList<File>();
		File file = new File(fileDir);
		File[] files = file.listFiles();
		if(files == null) return;


		//遍历，目录下所有文件
		for(File f:files) {
			if(f.isFile()) {
				fileList.add(f);
			}
			else if (f.isDirectory()) {
//				System.out.println(f.getAbsolutePath());
				readFile(f.getAbsolutePath());
			}
		}
		for(File f:fileList) {
			if (f.getName().contains("~")) {
				continue;
			}
//			System.out.println(f.getName());
			if (f.getAbsolutePath().toLowerCase().endsWith("docx") || f.getAbsolutePath().toLowerCase().endsWith("doc")) {
				ImportCPUtil imcp = new ImportCPUtil();
				JSONObject jobj =  translateToJson(f.getAbsolutePath());
				String jsonFileName = "";
				if (jobj != null && jobj.length() != 0) {
					if (f.getAbsolutePath().toLowerCase().endsWith("docx")) {
						jsonFileName= f.getName().substring(0, f.getName().length()-4)+"json";
					}
					else if(f.getAbsolutePath().toLowerCase().endsWith("doc")) {
						jsonFileName= f.getName().substring(0, f.getName().length()-3)+"json";
					}
					String name = "";
					int n = 1;
					File curFile = new File("D:\\hjj\\清华\\实验室工作\\jsonCP\\"+jsonFileName);
					while(curFile.exists()){
						name = String.valueOf(n++)+"-"+jsonFileName;
						File curFile1 = new File("D:\\hjj\\清华\\实验室工作\\jsonCP\\"+name);
						if(!curFile1.exists()) {
							jsonFileName = name;
							System.out.println(f.getAbsolutePath());
							break;
						}
					}
					jsonFileName = "D:\\hjj\\清华\\实验室工作\\jsonCP\\"+jsonFileName;
					imcp.writeFile(jsonFileName, jobj.toString());
				}
				else {
					System.out.println(f.getName());
				}

			}

		}
	}


	public JSONObject translateToJson(String filePath){
		  try{
		     FileInputStream in = new FileInputStream(filePath);//载入文档
		     JSONObject jsonObjDay = new JSONObject();//创建json格式的数据;//创建json格式的数据
		     //如果是office2007  docx格式
		     if(filePath.toLowerCase().endsWith("docx")){
		    	//word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
		    	 XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息
//		    	 List<XWPFParagraph> listParagraphs = xwpf.getParagraphs();//得到段落信息
		    	 Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格
		    	 int paperCount = 1;
		    	 int dayCount = 1;
		    	 while(it.hasNext()){
		    		 XWPFTable table = it.next();

		    		 List<XWPFTableRow> rows=table.getRows();
		    		 if (rows.size() < 2) {
						continue;
					}
		    		 if (rows.get(1).getTableCells().get(0).getText().contains("诊") == false) {
//		    			 System.out.println(rows.get(1).getTableCells().get(0).getText());
						continue;
					}
		    		 int rowSize = rows.size(), lineSize = rows.get(0).getTableCells().size();

		    		 boolean[] stMerge = new boolean[4];
		    		 for (int i = 0; i < stMerge.length; i++) {
						stMerge[i] = false;
		    		 }
		    		 int[] allMergeCount = new int[4];

		    		 ArrayList<HashMap<Integer,Integer>> mergeCountMap = new ArrayList<HashMap<Integer,Integer>>();
		    		 for (int i = 0; i < stMerge.length; i++) {
		    			 HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		    			 mergeCountMap.add(map);
		    		 }
		    		 ArrayList<HashMap<Integer, JSONObject>>  mergeJsonMap = new ArrayList<HashMap<Integer,JSONObject>>();
		    		 for (int i = 0; i < stMerge.length; i++) {
		    			 HashMap<Integer,JSONObject> map = new HashMap<Integer,JSONObject>();
		    			 mergeJsonMap.add(map);
		    		 }
		    		 int index = 0,r;

		    		 //按列读取数据
		    		 for (int i = 1; i < lineSize; i++) {

		    			    JSONArray jsonDayArr = new JSONArray();
		    			    JSONObject jsonObjName = new JSONObject();//时间名称
		    			    JSONObject  jsonObjCoreOrders = new JSONObject();//重点医嘱
		    			    JSONObject jsonObj = new JSONObject();
		    			    JSONArray  jsonArr = new JSONArray();//json格式的数组
		    			    JSONObject jsonObjCoreActivities = new JSONObject();//主要诊疗工作
		    			    JSONObject jsonObjCoreService = new JSONObject();//主要护理工作
		    			    List<XWPFParagraph> paragraphs;
		    			    //存时间阶段
		    			    r = 0;
		    			    jsonObjName.put("名称", rows.get(r++).getTableCells().get(i).getText());

		    			    //存主要诊疗工作
		    			    ArrayList<String> strsAct = new ArrayList<String>();
		    			    if(rowSize > 1) {
		    			    	 index = 1;
						    		if (stMerge[index]) {
						    			jsonObjCoreActivities = mergeJsonMap.get(index).get(index);
						    			int v = mergeCountMap.get(index).get(index);
						    			v--;
						    			mergeCountMap.get(index).put(index, v);
						    			if (v == 1) {
											stMerge[index] = false;
										}
									}
						    		else {
						    			while(rows.get(r).getTableCells().size() == 0) r++;
						    			XWPFTableCell cell1 = rows.get(index).getTableCells().get(i-allMergeCount[index]);
						    			paragraphs =  rows.get(index).getTableCells().get(i-allMergeCount[index]).getParagraphs();
						    			stMerge[index] = cell1.getCTTc().getTcPr().isSetGridSpan();
						    			if (stMerge[index]) {
						    				BigInteger grids3 = cell1.getCTTc().getTcPr().getGridSpan().getVal();
											mergeCountMap.get(index).put(index, grids3.intValue());
											allMergeCount[index] += (grids3.intValue()-1);
										}
						    			  for (int k = 0; k < paragraphs.size(); k++) {
							    	    		 XWPFParagraph p = paragraphs.get(k);
							    	    		 if(p.getRuns().size() == 0) continue;
							    	   			 String textString = getString(p);
							    	   			 if(textString.startsWith("□")) textString = textString.substring(textString.indexOf("□")+1);
//							    	    		 System.out.println(textString);
							    	    		 strsAct.add(textString);

											}
						    		    jsonObjCoreActivities.put("主要诊疗工作",strsAct.toArray());
						    		    mergeJsonMap.get(index).put(index, jsonObjCoreActivities);
						    		}
		    			    }





		    		    	//存重点医嘱
		    		    	ArrayList<String> strsLong = new ArrayList<String>();
		    		    	ArrayList<String> strsCur = new ArrayList<String>();
		    		    	if(rowSize > 2) {
		    		    		index = 2;
					    		if (stMerge[index]) {
					    			jsonObjCoreOrders = mergeJsonMap.get(index).get(index);
					    			int v = mergeCountMap.get(index).get(index);
					    			v--;
					    			mergeCountMap.get(index).put(index, v);
					    			if (v == 1) {
										stMerge[index] = false;
									}
								}
					    		else {
					    			XWPFTableCell cell2 = rows.get(index).getTableCells().get(i-allMergeCount[index]);
					    			paragraphs =  rows.get(index).getTableCells().get(i-allMergeCount[index]).getParagraphs();
					    			stMerge[index] = cell2.getCTTc().getTcPr().isSetGridSpan();
					    			if (stMerge[index]) {
					    				BigInteger grids3 = cell2.getCTTc().getTcPr().getGridSpan().getVal();
										mergeCountMap.get(index).put(index, grids3.intValue());
										allMergeCount[index] += (grids3.intValue()-1);
									}

				    		    	int sign = 0;
				    		         for (int k = 0; k < paragraphs.size(); k++) {
					    	    		 XWPFParagraph p = paragraphs.get(k);
					    	    		 if(p.getRuns().size() == 0) continue;
					    	    		 String textString = getString(p);
					    	    		 if(textString.startsWith("□")) textString = textString.substring(textString.indexOf("□")+1);
//					    	    		 System.out.println(textString);
					    	    		 if (textString.contains("长期医嘱")) {
											sign = 0;
											continue;
										}
					    	    		 else if (textString.contains("临时医嘱")) {
											sign = 1;
											continue;
										}
					    	    		if (sign == 0) {
					    	    			strsLong.add(textString);
										}
					    	    		else if (sign == 1) {
					    	    			strsCur.add(textString);
										}

									}

				    		    	 jsonObj.put("长期医嘱",strsLong.toArray());
				    		    	 jsonObj.put("临时医嘱", strsCur.toArray());
				    		         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
				    		         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
				    		         mergeJsonMap.get(index).put(index, jsonObjCoreOrders);
								}
		    		    	}




		    		         //存主要护理工作
		    		         ArrayList<String> strsServ = new ArrayList<String>();
		    		         if(rowSize > 3) {
		    		        	 index = 3;
			    		         if (stMerge[index]) {
						    			jsonObjCoreService = mergeJsonMap.get(index).get(index);
						    			int v = mergeCountMap.get(index).get(index);
						    			v--;
						    			mergeCountMap.get(index).put(index, v);
						    			if (v == 1) {
											stMerge[index] = false;
										}
								}
						    	else {
						    		XWPFTableCell cell3 = rows.get(index).getTableCells().get(i-allMergeCount[index]);
					    			paragraphs =  rows.get(index).getTableCells().get(i-allMergeCount[index]).getParagraphs();
					    			stMerge[index] = cell3.getCTTc().getTcPr().isSetGridSpan();
					    			if (stMerge[index]) {
					    				BigInteger grids3 = cell3.getCTTc().getTcPr().getGridSpan().getVal();
										mergeCountMap.get(index).put(index, grids3.intValue());
										allMergeCount[index] += (grids3.intValue()-1);
									}
				    		         for (int k = 0; k < paragraphs.size(); k++) {
					    	    		 XWPFParagraph p = paragraphs.get(k);
					    	    		 if(p.getRuns().size() == 0) continue;
					    	    		 String textString = getString(p);
					    	    		 if(textString.startsWith("□")) textString = textString.substring(textString.indexOf("□")+1);
		//			    	    		 System.out.println(textString);
					    	    		 strsServ.add(textString);
									 }
				    		         jsonObjCoreService.put("主要护理工作", strsServ.toArray());
				    		         mergeJsonMap.get(index).put(index, jsonObjCoreService);
								}
		    		         }



		    		         jsonDayArr.put(jsonObjName);
		    		         jsonDayArr.put(jsonObjCoreActivities);
		    		         jsonDayArr.put(jsonObjCoreOrders);
		    		         jsonDayArr.put(jsonObjCoreService);
		    		         jsonObjDay.put(String.valueOf(dayCount), jsonDayArr);
		    		         dayCount++;
					}

//		    		 //读取每一行数据
//		    		 for (int i = 0; i < rows.size(); i++) {
//		    			 XWPFTableRow  row = rows.get(i);
//		    			 //读取每一列数据 cells
//			    	     List<XWPFTableCell> cells = row.getTableCells();
//			    	     for (int j = 0; j < cells.size(); j++) {
//			    	    	 XWPFTableCell cell=cells.get(j);
//			    	    	 //输出当前的单元格的数据
//			    	    	 System.out.println(cell.getText());
////			    	    	 System.out.println(cell.getTextRecursively());
//			    	    	 List<XWPFParagraph> paragraphs =  cell.getParagraphs();
//			    	    	 for (int k = 0; k < paragraphs.size(); k++) {
//			    	    		 XWPFParagraph p = paragraphs.get(k);
//			    	    		 System.out.println(p.getRuns().get(0).toString());
//							}
//
//						}
//		    		 }
		    		 paperCount++;
		    	 }
		     }
		     else if(filePath.toLowerCase().endsWith("doc")) {
		    	 //如果是office2003  doc格式
			      POIFSFileSystem pfs = new POIFSFileSystem(in);
			      HWPFDocument hwpf = new HWPFDocument(pfs);
			      Range range = hwpf.getRange();//得到文档的读取范围
			      TableIterator it = new TableIterator(range);
			     //迭代文档中的表格
			      int paperCount = 1;
			      int dayCount = 1;

			      while (it.hasNext()) {

			          Table tb = (Table) it.next();
			          if(tb.numRows() < 2 ) continue;
			          int R = 1;
			          while(tb.getRow(R).numCells() == 0) R++;
			          if (tb.getRow(R).getCell(0).text().contains("诊") == false) {
			        	  String s = tb.getRow(1).getCell(0).text();
	                      //去除后面的特殊符号
	                      if(null!=s&&!"".equals(s)){
	                    	  s=s.substring(0, s.length()-1);
	                      }
//			        	  System.out.println(s);
			        	  continue;
			          }
//			          System.out.println("第"+ paperCount +"页");
//			          System.out.println("行数："+tb.numRows());
			          int rowSize = tb.numRows(), lineSize = tb.getRow(0).numCells();

			          boolean[] stMerge = new boolean[4];
			          for (int i = 0; i < stMerge.length; i++) {
			        	  stMerge[i] = false;
			          }
			          int[] allMergeCount = new int[4];

			          ArrayList<HashMap<Integer,Short>> mergeCountMap = new ArrayList<HashMap<Integer,Short>>();
			          for (int i = 0; i < stMerge.length; i++) {
			        	  HashMap<Integer,Short> map = new HashMap<Integer,Short>();
			        	  mergeCountMap.add(map);
			          }
			          ArrayList<HashMap<Integer, JSONObject>>  mergeJsonMap = new ArrayList<HashMap<Integer,JSONObject>>();
			          for (int i = 0; i < stMerge.length; i++) {
			        	  HashMap<Integer,JSONObject> map = new HashMap<Integer,JSONObject>();
			        	  mergeJsonMap.add(map);
			          }
			          int index = 0, r = 0;
			          short[] widthList = new short[lineSize];
			          for (int i = 0; i < widthList.length; i++) {
			        	  widthList[i] = tb.getRow(0).getCell(i).getDescriptor().getWWidth();
			          }


			          for (int i = 1; i < lineSize; i++) {
			        	  JSONArray jsonDayArr = new JSONArray();
		    			  JSONObject jsonObjName = new JSONObject();//时间名称
		    			  JSONObject  jsonObjCoreOrders = new JSONObject();//重点医嘱
		    			  JSONObject jsonObj = new JSONObject();
		    			  JSONArray  jsonArr = new JSONArray();//json格式的数组
		    			  JSONObject jsonObjCoreActivities = new JSONObject();//主要诊疗工作
		    			  JSONObject jsonObjCoreService = new JSONObject();//主要护理工作
		    			  r = 0;
		    			  //存时间阶段
		    			  while(tb.getRow(r).numCells() == 0) r++;
		    			  jsonObjName.put("名称", tb.getRow(r++).getCell(i).text());

		    			  //存主要诊疗工作
		    			  ArrayList<String> strsAct = new ArrayList<String>();
		    			  if(rowSize <= 1) continue;
		    			  index = 1;
		    			  if (stMerge[index]) {
		    				  jsonObjCoreActivities = mergeJsonMap.get(index).get(index);
		    				  short v = mergeCountMap.get(index).get(index);
				    			v = (short) (v - widthList[i]);
				    			allMergeCount[index]++;
				    			mergeCountMap.get(index).put(index, v);
				    			if (v == 0) {
									stMerge[index] = false;
									allMergeCount[index]--;
								}
				    			r++;
		    			  }
		    			  else {
		    				  while(tb.getRow(r).numCells() == 0) r++;
		    				  TableCell cell1 = tb.getRow(r++).getCell(i-allMergeCount[index]);
		    				  short wd = cell1.getDescriptor().getWWidth();
		    				  stMerge[index] = (wd == widthList[i])?false:true;
		    				  if (stMerge[index]) {
		    					  mergeCountMap.get(index).put(index, (short) (wd-widthList[i]));
		    					  allMergeCount[index]++;
		    				  }
			    			  for (int k = 0; k < cell1.numParagraphs(); k++) {
			    				  Paragraph para =cell1.getParagraph(k);
			                      String s = para.text();
			                      //去除后面的特殊符号
			                      if(null!=s&&!"".equals(s)){
			                    	  s=s.substring(0, s.length()-1);
			                      }
			                      if (s.length() == 0) continue;
			                      if(s.startsWith("□")) s = s.substring(s.indexOf("□")+1);
//			                      	System.out.println(s);
			                      strsAct.add(s);

			    			  }
			    			  jsonObjCoreActivities.put("主要诊疗工作",strsAct.toArray());
			    			  mergeJsonMap.get(index).put(index, jsonObjCoreActivities);
		    			  }

		    			  //存重点医嘱
		    		    	ArrayList<String> strsLong = new ArrayList<String>();
		    		    	ArrayList<String> strsCur = new ArrayList<String>();
		    		    	if(rowSize <= 2) continue;

		    		    	index = 2;
				    		if (stMerge[index]) {
				    			jsonObjCoreOrders = mergeJsonMap.get(index).get(index);
				    			short v = mergeCountMap.get(index).get(index);
				    			v = (short) (v - widthList[i]);
				    			allMergeCount[index]++;
				    			mergeCountMap.get(index).put(index, v);
				    			if (v == 0) {
									stMerge[index] = false;
									allMergeCount[index]--;
								}
				    			r++;
							}
				    		else {
//				    			if (tb.getRow(index).numCells() <= i - allMergeCount[index]) {
//									continue;
//								}
				    			while(tb.getRow(r).numCells() == 0) r++;
				    			TableCell cell1 = tb.getRow(r++).getCell(i-allMergeCount[index]);

				    			short wd = cell1.getDescriptor().getWWidth();
				    			stMerge[index] = (wd == widthList[i])?false:true;
				    			if (stMerge[index]) {
									mergeCountMap.get(index).put(index, (short) (wd-widthList[i]));
									allMergeCount[index]++;
								}

			    		    	int sign = 0;
			    		        for (int k = 0; k < cell1.numParagraphs(); k++) {
			    		        	Paragraph para =cell1.getParagraph(k);
				                    String s = para.text();
				                    //去除后面的特殊符号
				                    if(null!=s&&!"".equals(s)){
				                   	  s=s.substring(0, s.length()-1);
				                    }
				                    if (s.length() == 0) continue;
				                    if(s.startsWith("□")) s = s.substring(s.indexOf("□")+1);
//				    	    		 System.out.println(textString);
				    	    		if (s.contains("长期医嘱")) {
										sign = 0;
										continue;
									}
				    	    		else if (s.contains("临时医嘱")) {
										sign = 1;
										continue;
									}
				    	    		if (sign == 0) {
				    	    			strsLong.add(s);
									}
				    	    		else if (sign == 1) {
				    	    			strsCur.add(s);
									}

								}

			    		    	 jsonObj.put("长期医嘱",strsLong.toArray());
			    		    	 jsonObj.put("临时医嘱", strsCur.toArray());
			    		         jsonArr.put(jsonObj);//将json格式的数据放到json格式的数组里
			    		         jsonObjCoreOrders.put("重点医嘱", jsonArr);//再将这个json格式的的数组放到最终的json对象中。
			    		         mergeJsonMap.get(index).put(index, jsonObjCoreOrders);
							}


				    		//存主要护理工作
		    		         ArrayList<String> strsServ = new ArrayList<String>();
		    		         if(rowSize <= 3) continue;

		    		         index=3;
		    		         if (stMerge[index]) {
					    			jsonObjCoreService = mergeJsonMap.get(index).get(index);
					    			short v = mergeCountMap.get(index).get(index);
					    			v = (short) (v - widthList[i]);
					    			allMergeCount[index]++;
					    			mergeCountMap.get(index).put(index, v);
					    			if (v == 0) {
										stMerge[index] = false;
										allMergeCount[index]--;
									}
					    			r++;
							}
					    	else {
					    		while(tb.getRow(r).numCells() == 0) r++;
					    		TableCell cell1 = tb.getRow(r++).getCell(i-allMergeCount[index]);
					    		short wd = cell1.getDescriptor().getWWidth();
				    			stMerge[index] = (wd == widthList[i])?false:true;
				    			if (stMerge[index]) {
									mergeCountMap.get(index).put(index, (short) (wd-widthList[i]));
									allMergeCount[index]++;
								}
					    		for (int k = 0; k < cell1.numParagraphs(); k++) {
			    		        	Paragraph para =cell1.getParagraph(k);
				                    String s = para.text();
				                    //去除后面的特殊符号
				                    if(null!=s&&!"".equals(s)){
				                   	  s=s.substring(0, s.length()-1);
				                    }
				                    if (s.length() == 0) continue;
				                    if(s.startsWith("□")) s = s.substring(s.indexOf("□")+1);
	//			    	    		 System.out.println(textString);
				    	    		strsServ.add(s);
								 }
			    		         jsonObjCoreService.put("主要护理工作", strsServ.toArray());
			    		         mergeJsonMap.get(index).put(index, jsonObjCoreService);
							}

		    			  jsonDayArr.put(jsonObjName);
		    		      jsonDayArr.put(jsonObjCoreActivities);
		    		      jsonDayArr.put(jsonObjCoreOrders);
		    		      jsonDayArr.put(jsonObjCoreService);
		    		      jsonObjDay.put(String.valueOf(dayCount), jsonDayArr);
		    		      dayCount++;

			          }


//			          //迭代行，默认从0开始
//			          for (int i = 0; i < tb.numRows(); i++) {
//			              TableRow tr = tb.getRow(i);
//			              //迭代列，默认从0开始
//			              for (int j = 0; j < tr.numCells(); j++) {
//			                  TableCell td = tr.getCell(j);//取得单元格
//
//			                  //取得单元格的内容
//			                  for(int k=0;k<td.numParagraphs();k++){
//			                      Paragraph para =td.getParagraph(k);
//			                      String s = para.text();
//			                      //去除后面的特殊符号
//			                      if(null!=s&&!"".equals(s)){
//			                    	  s=s.substring(0, s.length()-1);
//			                      }
//			                      System.out.println(s);
//			                  }
//			              }
//			          }
			          paperCount++;
			      }
		     }
		     return jsonObjDay;
		  }catch(Exception e){
		   e.printStackTrace();
		   System.out.println(filePath);
		  }
		return null;

	}
	private String getString(XWPFParagraph p) {
		// TODO Auto-generated method stub
		String res = "";
		for (int i = 0; i < p.getRuns().size(); i++) {
			res += p.getRuns().get(i).toString();
		}
		return res;
	}



}
