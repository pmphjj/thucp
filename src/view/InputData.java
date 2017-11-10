package view;

import java.util.ArrayList;

public class InputData {
	String key;
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	ArrayList<InputDataRowType> data;

	public InputData() {
		data = new ArrayList<InputDataRowType>();
	}

	public ArrayList<InputDataRowType> getData() {
		return data;
	}

	public void setData(ArrayList<InputDataRowType> data) {
		this.data = data;
	}

}
