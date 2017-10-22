/**
 * 
 */
package cn.edu.thu.thss.iise.pojo;

import java.util.Date;

/**
 * @author Tao Jin
 *
 */
public class SfmxPatientDailySummary {
	
	private String grbm;
	private String mc;
	private String dl;
	private float dj;
	private float zl = 0;
	private float zje = 0;
	private Date rq;

	public SfmxPatientDailySummary(Sfmx sfmx) {
		this.grbm = sfmx.getGrbm();
		this.rq = sfmx.getRq();
		this.mc = sfmx.getMc();
		this.dl = sfmx.getDl();
		this.dj = sfmx.getDj();
		this.zl = sfmx.getZl();
		this.zje = sfmx.getZje();
	}
	
	public SfmxPatientDailySummary() {
		
	}
	
	public SfmxPatientDailySummary summarize(Sfmx sfmx) {
		if (this.grbm.equals(sfmx.getGrbm()) && this.rq.equals(sfmx.getRq()) && this.mc.equals(sfmx.getMc())) {
			this.zl += sfmx.getZl();
			this.zje += sfmx.getZje();
		}
		return this;
	}
	
	
	/**
	 * @return the grbm
	 */
	public String getGrbm() {
		return grbm;
	}


	/**
	 * @param grbm the grbm to set
	 */
	public void setGrbm(String grbm) {
		this.grbm = grbm;
	}


	/**
	 * @return the mc
	 */
	public String getMc() {
		return mc;
	}


	/**
	 * @param mc the mc to set
	 */
	public void setMc(String mc) {
		this.mc = mc;
	}


	/**
	 * @return the dl
	 */
	public String getDl() {
		return dl;
	}


	/**
	 * @param dl the dl to set
	 */
	public void setDl(String dl) {
		this.dl = dl;
	}


	/**
	 * @return the dj
	 */
	public float getDj() {
		return dj;
	}


	/**
	 * @param dj the dj to set
	 */
	public void setDj(float dj) {
		this.dj = dj;
	}


	/**
	 * @return the zl
	 */
	public float getZl() {
		return zl;
	}


	/**
	 * @param zl the zl to set
	 */
	public void setZl(float zl) {
		this.zl = zl;
	}


	/**
	 * @return the zje
	 */
	public float getZje() {
		return zje;
	}


	/**
	 * @param zje the zje to set
	 */
	public void setZje(float zje) {
		this.zje = zje;
	}


	/**
	 * @return the rq
	 */
	public Date getRq() {
		return rq;
	}


	/**
	 * @param rq the rq to set
	 */
	public void setRq(Date rq) {
		this.rq = rq;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
