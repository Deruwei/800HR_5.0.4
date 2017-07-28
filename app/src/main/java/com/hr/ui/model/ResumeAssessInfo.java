package com.hr.ui.model;

import java.io.Serializable;

/**
 * 个人自我评价
 * 
 */
public class ResumeAssessInfo implements Serializable{
	public int id = -1;
	public String user_id = "";
	public String introduction = "";
	public String resume_id = "";
	public String resume_language = "";

	public ResumeAssessInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
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

	@Override
	public String toString() {
		return "ResumeAssessInfo [id=" + id + ", user_id=" + user_id
				+ ", introduction=" + introduction + ", resume_id=" + resume_id
				+ ", resume_language=" + resume_language + "]";
	}

}
