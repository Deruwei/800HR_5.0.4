package com.hr.ui.model;

import java.io.Serializable;

/**
 * 语言能力
 * 
 */
public class ResumeLanguageLevel implements Serializable {
	public int id = -1;
	public String langname= "";
	public String user_id= "";
	public String read_level= "";
	public String speak_level= "";

	public ResumeLanguageLevel() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLangname() {
		return langname;
	}

	public void setLangname(String langname) {
		this.langname = langname;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRead_level() {
		return read_level;
	}

	public void setRead_level(String read_level) {
		this.read_level = read_level;
	}

	public String getSpeak_level() {
		return speak_level;
	}

	public void setSpeak_level(String speak_level) {
		this.speak_level = speak_level;
	}

	@Override
	public String toString() {
		return "ResumeLanguageLevel [id=" + id + ", langname=" + langname
				+ ", user_id=" + user_id + ", read_level=" + read_level
				+ ", speak_level=" + speak_level + "]";
	}

}
