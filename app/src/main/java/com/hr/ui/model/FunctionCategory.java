package com.hr.ui.model;

/**
 * 职能分类
 * 
 */
public class FunctionCategory {
	private int IndustryID;// 行业ID
	private int FunctionID;// 此条职位的ID
	private String FunctionName;// 单位用户的ID
	private int PID;// 父ID。为0表示大类

	public FunctionCategory() {
		// TODO Auto-generated constructor stub
	}

	public FunctionCategory(int industryID, int functionID,
			String functionName, int pID) {
		super();
		IndustryID = industryID;
		FunctionID = functionID;
		FunctionName = functionName;
		PID = pID;
	}

	public int getIndustryID() {
		return IndustryID;
	}

	public void setIndustryID(int industryID) {
		IndustryID = industryID;
	}

	public int getFunctionID() {
		return FunctionID;
	}

	public void setFunctionID(int functionID) {
		FunctionID = functionID;
	}

	public String getFunctionName() {
		return FunctionName;
	}

	public void setFunctionName(String functionName) {
		FunctionName = functionName;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

}
