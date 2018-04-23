package model.CPMRM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.netlib.util.intW;

public class LofOutLier {
	ArrayList<String[]> inputData;
	HashMap<Integer,String> IndexOfCol;
	int rowSize;
	public LofOutLier() {
		// TODO Auto-generated constructor stub
		inputData = new ArrayList<String[]>();
		IndexOfCol = new HashMap<Integer,String>();
		rowSize = 0;
	}
	public Boolean readLofOutput(String filename){
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(" ");
//				System.out.println(words.length);
				String signSeq = "lof-outlier=";
				for (int i = 0; i < words.length; i++) {
					if(words[i].contains(signSeq) && i >= 1) {
						String[] row =  new String[2];
						String result1=new String(words[i-1]);
						String result2=new String(words[i].substring(signSeq.length()).getBytes(),"UTF-8");
						row[0] = result1;
						row[1] = result2;
						inputData.add(row);
						break;
					}
				}
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
 	public Boolean readFile(String filename){
 		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
			String firstline = br.readLine();
			String[] columnLists = firstline.split(",");
			rowSize = columnLists.length;
			System.out.println(columnLists.length);
			for (int i = 0; i < columnLists.length; i++) {
				if(!columnLists[i].replace(" ", "").equals("")) {
					IndexOfCol.put(i,columnLists[i]);
				}
			}
			System.out.println(IndexOfCol.size());
			String line = null;
			int row = 0;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(",");
				System.out.println(words.length);
				for (int i = 0; i < words.length; i++) {
					inputData.add(words);
				}
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
 	}
 	private void clearData() {
 		if(!inputData.isEmpty())
 			inputData.clear();
 		if(!IndexOfCol.isEmpty())
 			IndexOfCol.clear();
 		rowSize = 0;
	}
 	private void showData() {
		if(inputData.size()>0 && IndexOfCol.size()>0) {
			for (int i = 0; i < rowSize; i++) {
				System.out.print(IndexOfCol.get(i)+" ");
			}
			System.out.println();
			for (int i = 0; i < inputData.size(); i++) {
				for (int j = 0; j < inputData.get(i).length; j++) {
					System.out.print(inputData.get(i)[j]);
				}
				System.out.println();
			}
		}
		else {
			System.out.println("输入数据集为空，无法展示！");
		}
	}

 	private void showData(int Num) {
		if(inputData.size()>0) {
			for (int i = 0; i < Num; i++) {
				for (int j = 0; j < inputData.get(i).length; j++) {
					System.out.print(inputData.get(i)[j]+" ");
				}
				System.out.println();
			}
		}
		else {
			System.out.println("输入数据集为空，无法展示！");
		}
	}
 	public void writeData(int Num, String filename) {
		if(inputData.size()>0) {
			try {
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename))));
				for (int i = 0; i < Num; i++) {
					for (int j = 0; j < inputData.get(i).length; j++) {
						wr.write(inputData.get(i)[j]+" ");
					}
					wr.newLine();
				}
				wr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("输入数据集为空，无法展示！");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LofOutLier lofPlayer = new LofOutLier();
		String fileAddress = "C:\\Users\\lenovo\\Desktop\\医保1天\\";
//		String filename = "门诊交易诊断表20150824-20150824-utf8.csv";
//		lofPlayer.clearData();
//		if(lofPlayer.readFile(fileAddress+filename)){
//			lofPlayer.showData();
//		}
		String fileAddress1 = "D:\\result\\高血压test\\lof-case\\lof-outlier_order.txt\\";
		String filename1 = "lof-outlier_order.txt";
//		lofPlayer.clearData();
//		if (lofPlayer.readLofOutput(fileAddress1+filename1)) {
//			lofPlayer.showData(100);
//			lofPlayer.writeData(100, fileAddress1+"result-sub.txt");
//		}

		String fileAddress2 = "D:\\result\\高血压test\\lof-case-distinct\\";//D:\result\高血压test\lof-case-distinct
		String filename2 = "lof-outlier_order.txt";
		lofPlayer.clearData();
		if (lofPlayer.readLofOutput(fileAddress2+filename2)) {
			lofPlayer.showData(100);
			lofPlayer.writeData(100, fileAddress2+"result-sub.txt");
		}
	}

}
