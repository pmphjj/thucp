package view;

import java.util.ArrayList;

public class InputData {
	String type;// "log" or "stdClinicalPathway"

	String key;

	ArrayList<InputDataRowType> dataForLog;

	public InputData() {
		dataForLog = new ArrayList<InputDataRowType>();
	}

	public ArrayList<InputDataRowType> getDataForLog() {
		return dataForLog;
	}

	public void setDataForLog(ArrayList<InputDataRowType> data) {
		this.dataForLog = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
