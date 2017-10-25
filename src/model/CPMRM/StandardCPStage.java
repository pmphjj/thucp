package model.CPMRM;

import java.util.ArrayList;

public class StandardCPStage {
	String name; //阶段时间
	ArrayList<String> coreActivities;//主要诊疗活动
	ArrayList<String> coreOrdersLong;//长期医嘱
	ArrayList<String> coreOrdersCur;//临时医嘱
	ArrayList<String> coreServices;//主要护理

}
