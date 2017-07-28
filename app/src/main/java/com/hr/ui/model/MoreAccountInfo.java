package com.hr.ui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 账号绑定的平台信息
 * 
 */
public class MoreAccountInfo implements Serializable {
	private String user_id;
	private ArrayList<MoreAccountInfoResumeItem> resume_list = new ArrayList<MoreAccountInfoResumeItem>();
	private String account_name;

	public MoreAccountInfo() {

	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public ArrayList<MoreAccountInfoResumeItem> getResume_list() {
		return resume_list;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Override
	public String toString() {
		return "MoreAccountInfo [user_id=" + user_id + ", resume_list="
				+ resume_list + ", account_name=" + account_name + "]";
	}
}
