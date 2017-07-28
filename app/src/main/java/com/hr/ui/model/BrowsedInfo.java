package com.hr.ui.model;

/**
 * 个人中心-谁看过我的简历
 * 
 */
public class BrowsedInfo {
	private String enterprise_name;
	private String enterprise_id;
	private String browsed_time;
	private String stuffmunber;

	public BrowsedInfo() {
		// TODO Auto-generated constructor stub
	}

	public BrowsedInfo(String enterprise_name, String enterprise_id,
			String browsed_time, String stuffmunber) {
		super();
		this.enterprise_name = enterprise_name;
		this.enterprise_id = enterprise_id;
		this.browsed_time = browsed_time;
		this.stuffmunber = stuffmunber;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(String enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public String getBrowsed_time() {
		return browsed_time;
	}

	public void setBrowsed_time(String browsed_time) {
		this.browsed_time = browsed_time;
	}

	public String getStuffmunber() {
		return stuffmunber;
	}

	public void setStuffmunber(String stuffmunber) {
		this.stuffmunber = stuffmunber;
	}

	@Override
	public String toString() {
		return "BrowsedInfo [enterprise_name=" + enterprise_name
				+ ", enterprise_id=" + enterprise_id + ", browsed_time="
				+ browsed_time + ", stuffmunber=" + stuffmunber + "]";
	}

}
