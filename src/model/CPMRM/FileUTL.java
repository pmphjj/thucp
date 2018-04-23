package model.CPMRM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.tools.ant.util.FileTokenizer;

public class FileUTL {

	ArrayList<ArrayList<String>> inputData;
	ArrayList<String> IndexOfCol;
	HashSet<String> tempOrderSet;
	HashSet<String> orderSet;
 	int rowSize;
	public FileUTL() {
		// TODO Auto-generated constructor stub
		inputData = new ArrayList<ArrayList<String>>();
		IndexOfCol = new ArrayList<String>();
		rowSize = 0;
	}


	public void readFile(String filename,String splitSign,String dateSign) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
			String firstLine = br.readLine();
			String[] columnLists = firstLine.split(splitSign);
			rowSize = columnLists.length;
			for (int i = 0; i < rowSize; i++) {
				String name = new String(columnLists[i].substring(1, columnLists[i].length()-1));
				IndexOfCol.add(name);
				System.out.println(name);
			}
			IndexOfCol.add("住院天数");
			rowSize++;
			String line = "";
			int indexOfSt = 5;//医嘱 起始时间
			int indexOfEt = 6;// 停嘱时间
			int indexOfType = 2;
			while ((line=br.readLine())!= null) {
				String[] words = line.split(splitSign);
				ArrayList<String> row = new ArrayList<String>();
				for (int i = 0; i < words.length; i++) {
					String value = words[i].substring(1, words[i].length()-1);
					words[i] = value;
					if (value.isEmpty()) {
						value = "-";
					}


					row.add(value);
				}
				String startTime = words[indexOfSt];
				String endTime = words[indexOfEt];
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				Date beginDate = format.parse(startTime);
				Date endDate = format.parse(endTime);
				long day = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
				System.out.println(day);
				row.add(String.valueOf(day));
				inputData.add(row);

			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileUTL fUtl = new FileUTL();
		String fileAddres = "C:\\Users\\lenovo\\Desktop\\医嘱和对应路径信息\\";
		String filename = fileAddres+"12个病人的医嘱信息-utf8.csv";
		String splitSign = ",";
		String dateSign = "/";
		fUtl.readFile(filename, splitSign, dateSign);
	}

}
