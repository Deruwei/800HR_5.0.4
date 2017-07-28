package com.hr.ui.model;

/**
 * 用户英文信息扩展信息
 * 
 */
public class ResumeInfo_extend {

	private int id=-1;
	private int user_id;// 用户id
	private String name_en= "";// 英文名字
	private String address_en= "";// 英文通讯地址
	private String last_position= "";// 最后从事职位
	private String order_position= "";// 期望从事职位（暂未用）
	private String resume_language= "";// 简历语言
	private String add_time= "";// 创建时间

	public ResumeInfo_extend() {
		// TODO Auto-generated constructor stub
	}

	public ResumeInfo_extend(int id, int user_id, String name_en,
			String address_en, String last_position, String order_position,
			String resume_language, String add_time) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name_en = name_en;
		this.address_en = address_en;
		this.last_position = last_position;
		this.order_position = order_position;
		this.resume_language = resume_language;
		this.add_time = add_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public String getAddress_en() {
		return address_en;
	}

	public void setAddress_en(String address_en) {
		this.address_en = address_en;
	}

	public String getLast_position() {
		return last_position;
	}

	public void setLast_position(String last_position) {
		this.last_position = last_position;
	}

	public String getOrder_position() {
		return order_position;
	}

	public void setOrder_position(String order_position) {
		this.order_position = order_position;
	}

	public String getResume_language() {
		return resume_language;
	}

	public void setResume_language(String resume_language) {
		this.resume_language = resume_language;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

}
