package view;

import java.util.ArrayList;
import java.util.HashMap;

import model.CPCGA.Patient;
import model.CPCGA.IO;
import model.CPMRM.StandardCPStage;

public class InputData {
	String type;// "log" or "stdClinicalPathway"
	String file_name;
	String key;
	IO cpcgaIO;

	ArrayList<Patient> dataForPatients;
	ArrayList<InputDataRowType> dataForLog;
	HashMap<Integer,String> iTosMap;
	StandardCPStage[] dataForCP;

	public InputData() {
		dataForPatients = new ArrayList<Patient>();
		dataForLog = new ArrayList<InputDataRowType>();
		file_name = "";
		cpcgaIO = new IO();
		iTosMap = new HashMap<Integer,String>();

	}

	public StandardCPStage[] getDataForCP() {
		return dataForCP;
	}

	public void setDataForCP(StandardCPStage[] CPS) {
		this.dataForCP = CPS;
	}

	public ArrayList<Patient> getDataForPatients() {
		return dataForPatients;
	}

	public ArrayList<InputDataRowType> getDataForLog() {
		return dataForLog;
	}

	public void setDataForPatient(ArrayList<Patient> data) {
		this.dataForPatients = data;
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
	public void setName(String name) {
		this.file_name = name;
	}

	public String getKey() {
		return key;
	}
	public String getName() {
		return file_name;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
