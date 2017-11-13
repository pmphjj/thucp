package outlier.cost;


import java.util.Date;

public class Event {
	String patientId;
	String label;
	Date date;
	public Event(String patientId,String label,Date date){
		this.patientId=patientId;
		this.label=label;
		this.date=date;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}

