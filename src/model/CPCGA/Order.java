package model.CPCGA;

public class Order extends OD {
	String orderName;

	public Order() {

	}

	public Order(String pId, String name, String t) {
		this.setPatientId(pId);
		this.setOrderName(name);
		this.setTime(t);
	}

	public void setOrderName(String s) {
		orderName = s;
	}

	public String getOrderName() {
		return orderName;
	}
}
