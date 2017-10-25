package model.CPMRM;

import java.util.ArrayList;

public class StandardCPStage {
	public String name; //阶段时间
	public ArrayList<String> coreActivities;//主要诊疗活动
	public ArrayList<String> coreOrdersLong;//长期医嘱
	public ArrayList<String> coreOrdersCur;//临时医嘱
	public ArrayList<String> coreOrdersOut;//出院医嘱
	public ArrayList<String> coreServices;//主要护理

	public StandardCPStage(){
		name = new String();
		coreActivities = new ArrayList<String>();
		coreOrdersLong = new ArrayList<String>();
		coreOrdersCur = new ArrayList<String>();
		coreOrdersOut = new ArrayList<String>();
		coreServices = new ArrayList<String>();
	}

}
