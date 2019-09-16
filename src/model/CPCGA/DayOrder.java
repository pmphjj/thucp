package model.CPCGA;
import java.util.ArrayList;

public class DayOrder extends OD{
	ArrayList<Order> orderList;
	private int size;
	public DayOrder() {
		orderList = new ArrayList<Order>();
		size = 0;
	}
	public int size() {
		return size;
	}

	public DayOrder(String pId, String t) {
		patientId = pId;
		time = t;
		orderList = new ArrayList<Order>();
		size = 0;
	}

	public void addOrder(Order od) {
		if (orderList.contains(od)) {
		}
		else {
			 orderList.add(od);
			 size = orderList.size();
		}
	}

	public Order removeOrder(int index) {
		size = orderList.size()-1;
		return orderList.remove(index);
	}

	public Order getOrder(int index) {
		return orderList.get(index);
	}

	public Order setOrder(int i, Order od) {
		return orderList.set(i, od);
	}
}
