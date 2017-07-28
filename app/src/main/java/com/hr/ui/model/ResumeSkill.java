package com.hr.ui.model;

import java.io.Serializable;

/**
 * 个人技能
 * 
 */
public class ResumeSkill implements Serializable{
	public int id = -1;
	public String user_id;
	public String skilltitle;
	public String usetime= "";
	public String ability;
	public String resume_id;
	public String resume_language;
	public String skill_id;

	public ResumeSkill() {
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

	public String getSkilltitle() {
		return skilltitle;
	}

	public void setSkilltitle(String skilltitle) {
		this.skilltitle = skilltitle;
	}

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public String getAbility() {
		return ability;
	}

	public void setAbility(String ability) {
		this.ability = ability;
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

	public String getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(String skill_id) {
		this.skill_id = skill_id;
	}

	@Override
	public String toString() {
		return "ResumeSkill [id=" + id + ", user_id=" + user_id
				+ ", skilltitle=" + skilltitle + ", usetime=" + usetime
				+ ", ability=" + ability + ", resume_id=" + resume_id
				+ ", resume_language=" + resume_language + ", skill_id="
				+ skill_id + "]";
	}

}
