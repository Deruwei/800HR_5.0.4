package com.hr.ui.model;

public class AccountInfo {
	private String user_name;	// 用户登录名
	private String user_pwd;	// 用户登录密码
	private int industry;		// 用户的所属行业id
	private String emailaddress;// 用户邮箱

	public AccountInfo() {
		// TODO Auto-generated constructor stub
	}

	public AccountInfo(String user_name, String user_pwd, int industry,
			String emailaddress) {
		super();
		this.user_name = user_name;
		this.user_pwd = user_pwd;
		this.industry = industry;
		this.emailaddress = emailaddress;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

}
