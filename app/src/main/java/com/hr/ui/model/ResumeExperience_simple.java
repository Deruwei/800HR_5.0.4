package com.hr.ui.model;

/**
 * 快速简历存储用户的工作经验
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class ResumeExperience_simple {
	private int user_id; // 用户的ID
	private String experience; //    工作经验内容
	private int resume_id;//  简历序号
	private String resume_language; //  简历语言
	private int echo_yes; //  是否显示

	public ResumeExperience_simple() {
		// TODO Auto-generated constructor stub
	}

	public ResumeExperience_simple(int user_id, String experience,
			int resume_id, String resume_language, int echo_yes) {
		super();
		this.user_id = user_id;
		this.experience = experience;
		this.resume_id = resume_id;
		this.resume_language = resume_language;
		this.echo_yes = echo_yes;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getResume_id() {
		return resume_id;
	}

	public void setResume_id(int resume_id) {
		this.resume_id = resume_id;
	}

	public String getResume_language() {
		return resume_language;
	}

	public void setResume_language(String resume_language) {
		this.resume_language = resume_language;
	}

	public int getEcho_yes() {
		return echo_yes;
	}

	public void setEcho_yes(int echo_yes) {
		this.echo_yes = echo_yes;
	}

}
