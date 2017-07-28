package com.hr.ui.model;

import java.io.Serializable;

/**
 * 个人求职意向
 * 
 */
public class ResumeOrder implements Serializable{

	public String user_id = "";
	public String jobtype = "";
	public String industry = "";
	public String func = "";// (包含职系信息)
	public String workarea = "";
	public String order_salary = "";
	public String order_salary_noshow = "";
	public String resume_id = "";
	public String resume_language = "";
	public String jobname = "";
	public String lingyu = "";
	public int id = -1;// 主键

	public ResumeOrder() {
		// TODO Auto-generated constructor stub
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getWorkarea() {
		return workarea;
	}

	public void setWorkarea(String workarea) {
		this.workarea = workarea;
	}

	public String getOrder_salary() {
		return order_salary;
	}

	public void setOrder_salary(String order_salary) {
		this.order_salary = order_salary;
	}

	public String getOrder_salary_noshow() {
		return order_salary_noshow;
	}

	public void setOrder_salary_noshow(String order_salary_noshow) {
		this.order_salary_noshow = order_salary_noshow;
	}

	public String getResume_id() {
		return resume_id;
	}

	public void setResume_id(String resume_id) {
		this.resume_id = resume_id;
	}

	public String getResume_language() {
		return resume_language;
	}

	public void setResume_language(String resume_language) {
		this.resume_language = resume_language;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getLingyu() {
		return lingyu;
	}

	public void setLingyu(String lingyu) {
		this.lingyu = lingyu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ResumeOrder [user_id=" + user_id + ", jobtype=" + jobtype
				+ ", industry=" + industry + ", func=" + func + ", workarea="
				+ workarea + ", order_salary=" + order_salary
				+ ", order_salary_noshow=" + order_salary_noshow
				+ ", resume_id=" + resume_id + ", resume_language="
				+ resume_language + ", jobname=" + jobname + ", lingyu="
				+ lingyu + ", id=" + id + "]";
	}

}
