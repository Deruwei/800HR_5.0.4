package com.hr.ui.model;

/**
 * 地区分类
 * 
 *
 */
public class AreaCategory {
	private int AreaID; // 此条职位的ID
	private String AreaName;// 单位用户的ID
	private int PID;// 父ID。为0表示大类
	private int Flag;// 为0代表没有下级地区

	public AreaCategory() {
		// TODO Auto-generated constructor stub
	}

	public AreaCategory(int areaID, String areaName, int pID, int flag) {
		super();
		AreaID = areaID;
		AreaName = areaName;
		PID = pID;
		Flag = flag;
	}

	public int getAreaID() {
		return AreaID;
	}

	public void setAreaID(int areaID) {
		AreaID = areaID;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getFlag() {
		return Flag;
	}

	public void setFlag(int flag) {
		Flag = flag;
	}

}
