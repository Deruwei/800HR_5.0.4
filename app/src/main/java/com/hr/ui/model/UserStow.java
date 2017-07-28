package com.hr.ui.model;
/**
 * 个人收藏夹，收藏职位，已浏览职位
 */
public class UserStow {
	private int id;//记录ID
	private int user_id;//个人用户user_id
	private int enterprise_id;//发信人企业用户的user_id
	private int box;//信箱类型（0邀请面试、1收藏箱、2垃圾箱3已浏览职位 4应聘职位
	private int job_id;//招聘信息job_id
	private String job_name;//招聘信息名称
	private String enterprise_name;//企业名称
	private String department;//职位部门
	private String enterprise_email;//企业email
	private int addfrom;//未用
	private int delfrom;//来自哪个信箱（0收件箱、1收藏箱、2垃圾箱）
	private int deltime;//删除到垃圾箱的时间
	private int isshow;//是否隐藏
	private int time;//纪录添加时间
	private String standby4;//未用
	private int isnew;//是否新纪录 暂未用
	public UserStow() {
		// TODO Auto-generated constructor stub
	}
	public UserStow(int id, int user_id, int enterprise_id, int box,
			int job_id, String job_name, String enterprise_name,
			String department, String enterprise_email, int addfrom,
			int delfrom, int deltime, int isshow, int time, String standby4,
			int isnew) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.enterprise_id = enterprise_id;
		this.box = box;
		this.job_id = job_id;
		this.job_name = job_name;
		this.enterprise_name = enterprise_name;
		this.department = department;
		this.enterprise_email = enterprise_email;
		this.addfrom = addfrom;
		this.delfrom = delfrom;
		this.deltime = deltime;
		this.isshow = isshow;
		this.time = time;
		this.standby4 = standby4;
		this.isnew = isnew;
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

	public int getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(int enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public int getBox() {
		return box;
	}

	public void setBox(int box) {
		this.box = box;
	}

	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEnterprise_email() {
		return enterprise_email;
	}

	public void setEnterprise_email(String enterprise_email) {
		this.enterprise_email = enterprise_email;
	}

	public int getAddfrom() {
		return addfrom;
	}

	public void setAddfrom(int addfrom) {
		this.addfrom = addfrom;
	}

	public int getDelfrom() {
		return delfrom;
	}

	public void setDelfrom(int delfrom) {
		this.delfrom = delfrom;
	}

	public int getDeltime() {
		return deltime;
	}

	public void setDeltime(int deltime) {
		this.deltime = deltime;
	}

	public int getIsshow() {
		return isshow;
	}

	public void setIsshow(int isshow) {
		this.isshow = isshow;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getStandby4() {
		return standby4;
	}

	public void setStandby4(String standby4) {
		this.standby4 = standby4;
	}

	public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}
}
