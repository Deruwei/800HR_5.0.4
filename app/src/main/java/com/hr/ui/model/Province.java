package com.hr.ui.model;

/**
 * 省份
 * 
 */
public class Province {
	private int provinceID;
	private String provinceName;

	public Province() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param provinceID
	 * @param provinceName
	 */
	public Province(int provinceID, String provinceName) {
		super();
		this.provinceID = provinceID;
		this.provinceName = provinceName;
	}

	public int getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(int provinceID) {
		this.provinceID = provinceID;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

}
