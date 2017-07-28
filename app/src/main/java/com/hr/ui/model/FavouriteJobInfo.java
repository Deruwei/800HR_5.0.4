package com.hr.ui.model;

/**
 * 个人中心-收藏的职位
 * 
 */
public class FavouriteJobInfo {
	// {
	// "error_code":0,
	// "navpage_info":{"total_pages":1,"record_nums":"11","page_nums":"20","current_page":1},
	// "jobs_list":[{
	// "enterprise_name":"英皇（北京）房地产开发有限公司",
	// "area":"北京",
	// "job_name":"结构工程师",
	// "area_title":"北京",
	// "favourite_date":"2013-05-24",
	// "enterprise_id":"6BOo4",
	// "job_status":1,
	// "record_id":"143186967",
	// "job_id":"remxs"]}}
	private String enterprise_name;// 职位名称
	private String area;
	private String area_title;
	private String job_name;
	private String favourite_date;// 申请时间
	private String enterprise_id;// 职位id
	private String job_id;// 工作id
	private String job_status;
	private String record_id;
	private String delete;

	public FavouriteJobInfo() {
		// TODO Auto-generated constructor stub
	}

	public FavouriteJobInfo(String enterprise_name, String area,
			String area_title, String job_name, String favourite_date,
			String enterprise_id, String job_id, String job_status,
			String record_id) {
		super();
		this.enterprise_name = enterprise_name;
		this.area = area;
		this.area_title = area_title;
		this.job_name = job_name;
		this.favourite_date = favourite_date;
		this.enterprise_id = enterprise_id;
		this.job_id = job_id;
		this.job_status = job_status;
		this.record_id = record_id;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea_title() {
		return area_title;
	}

	public void setArea_title(String area_title) {
		this.area_title = area_title;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getFavourite_date() {
		return favourite_date;
	}

	public void setFavourite_date(String favourite_date) {
		this.favourite_date = favourite_date;
	}

	public String getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(String enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public String getDelete() {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public String getJob_status() {
		return job_status;
	}

	public void setJob_status(String job_status) {
		this.job_status = job_status;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	@Override
	public String toString() {
		return "FavouriteJobInfo [enterprise_name=" + enterprise_name
				+ ", area=" + area + ", area_title=" + area_title
				+ ", job_name=" + job_name + ", favourite_date="
				+ favourite_date + ", enterprise_id=" + enterprise_id
				+ ", job_id=" + job_id + ", job_status=" + job_status
				+ ", record_id=" + record_id + "]";
	}

}
