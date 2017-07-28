package com.hr.ui.model;

/**
 * 搜索历史
 * 
 */
public class SettingSearcherHistory {

	private String Keyword;
	private String function;
	private String Area;
	private String Salary;
	private String issue_date;

	public SettingSearcherHistory() {
		// TODO Auto-generated constructor stub
	}

	public SettingSearcherHistory(String keyword, String function, String area,
			String salary, String issue_date) {
		super();
		Keyword = keyword;
		this.function = function;
		Area = area;
		Salary = salary;
		this.issue_date = issue_date;
	}

	public String getKeyword() {
		return Keyword;
	}

	public void setKeyword(String keyword) {
		Keyword = keyword;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String area) {
		Area = area;
	}

	public String getSalary() {
		return Salary;
	}

	public void setSalary(String salary) {
		Salary = salary;
	}

	public String getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}

}
