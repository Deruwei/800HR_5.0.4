package com.hr.ui.model;

/**
 * 第三放帐户信息
 * 
 *
 */
public class AccountInfo_Third {
	private String third_code; 		// 第三方网站代号
	private String third_uid; 		// 第三方uid
	private String user_name; 		// 用户姓名（不是登陆用户名）
	private String emailaddress; 	// 用户邮箱
	private int industry; 			// 用户的所属行业id
	private String third_userinfo; 	// 序列化后的用户信息字符串

	public AccountInfo_Third() {
		// TODO Auto-generated constructor stub
	}

	public AccountInfo_Third(String third_code, String third_uid,
			String user_name, String emailaddress, int industry,
			String third_userinfo) {
		super();
		this.third_code = third_code;
		this.third_uid = third_uid;
		this.user_name = user_name;
		this.emailaddress = emailaddress;
		this.industry = industry;
		this.third_userinfo = third_userinfo;
	}

	public String getThird_code() {
		return third_code;
	}

	public void setThird_code(String third_code) {
		this.third_code = third_code;
	}

	public String getThird_uid() {
		return third_uid;
	}

	public void setThird_uid(String third_uid) {
		this.third_uid = third_uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public String getThird_userinfo() {
		return third_userinfo;
	}

	public void setThird_userinfo(String third_userinfo) {
		this.third_userinfo = third_userinfo;
	}

}
