package model.CPCGA;
import java.util.ArrayList;

public class Patient {
	public String patientID;
	public ArrayList<DayOrder> daysOrderList;
	public ArrayList<Integer> orderList;

	public Patient() {
		daysOrderList = new ArrayList<DayOrder>();
		orderList = new ArrayList<Integer>();
	}

	public void addOrder(Order od) {
		if(daysOrderList.size()>0) {
			DayOrder dayOrder = daysOrderList.get(daysOrderList.size()-1);
			if(dayOrder.getTime().equals(od.getTime())){
				dayOrder.addOrder(od);
			}
			else {
				DayOrder newday = new DayOrder(od.getPatientID(),od.getTime());
				newday.addOrder(od);
				daysOrderList.add(newday);
			}

		}
		else {
			DayOrder newday = new DayOrder(od.getPatientID(),od.getTime());
			newday.addOrder(od);
			daysOrderList.add(newday);
		}
	}

	public ArrayList<DayOrder> getList() {
		return daysOrderList;
	}
	public void setPatientID(String s) {
		patientID = s;
	}

	public String getPatientID() {
		return patientID;
	}

	public boolean addDayOrder(DayOrder od) {
		return daysOrderList.add(od);
	}

	public DayOrder removeDayOrder(int index) {
		return daysOrderList.remove(index);
	}

	public DayOrder getDayOrder(int index) {
		return daysOrderList.get(index);
	}

	public DayOrder setDayOrder(int i, DayOrder od) {
		return daysOrderList.set(i, od);
	}

	public void setList(ArrayList<Integer> list) {
		// TODO Auto-generated method stub
		orderList = list;
	}
}
