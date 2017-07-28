package com.hr.ui.model;

/**
 * 企业职位信息表
 * 
 */
public class EnterpriseInvite {

	private int job_id;// 此条职位的ID
	private int user_id;// 单位用户的ID
	private String job_name;// 职位名称
	private int issue_date;// 发布（刷新）日期
	private String enterprise_name;// 公司名称
	private int work_area;// 招聘地区
	private String synopsis;// 职位介绍
	private String monthly_play;// 工资待遇 结束
	private String department;// 工作部门
	private int effect_time;// 有效天数
	private int number;// 招聘人数
	private String email;// 公司email
	private String phone;// 公司电话
	private int workyear;// 要求的工作年限
	private int counts;// 此记录的被访问次数
	private int quyu;// 工作地区
	private int last_refresh_time;// 最后刷新时间
	private String linkman;// 职位联系人
	private String email2;// 职位联系人信箱
	private int post_rank;// 职称
	private int work_type;// 工作类别
	private int deltime;// 客户删除，标记时间定期删除
	private int hy;// 行业代码
	private int recruiter_count;// 职位应聘人次
	private int did;// 部门id 1:公司; 0:旧职位;大于1新建的部门id
	private int is_show_number_some;// 招聘人数显示为若干 091113
	private int is_show_pay_interview;// 工资显示为面议
	private int parent_job_id;// 父职位id 多地区 10.9.6
	private int expiry_date;// 职位有效发布日期 10.11.2
	private int monthly_play_to;// 工资待遇

	public EnterpriseInvite() {
		// TODO Auto-generated constructor stub
	}

	public EnterpriseInvite(int job_id, int user_id, String job_name,
			int issue_date, String enterprise_name, int work_area,
			String synopsis, String monthly_play, String department,
			int effect_time, int number, String email, String phone,
			int workyear, int counts, int quyu, int last_refresh_time,
			String linkman, String email2, int post_rank, int work_type,
			int deltime, int hy, int recruiter_count, int did,
			int is_show_number_some, int is_show_pay_interview,
			int parent_job_id, int expiry_date, int monthly_play_to) {
		super();
		this.job_id = job_id;
		this.user_id = user_id;
		this.job_name = job_name;
		this.issue_date = issue_date;
		this.enterprise_name = enterprise_name;
		this.work_area = work_area;
		this.synopsis = synopsis;
		this.monthly_play = monthly_play;
		this.department = department;
		this.effect_time = effect_time;
		this.number = number;
		this.email = email;
		this.phone = phone;
		this.workyear = workyear;
		this.counts = counts;
		this.quyu = quyu;
		this.last_refresh_time = last_refresh_time;
		this.linkman = linkman;
		this.email2 = email2;
		this.post_rank = post_rank;
		this.work_type = work_type;
		this.deltime = deltime;
		this.hy = hy;
		this.recruiter_count = recruiter_count;
		this.did = did;
		this.is_show_number_some = is_show_number_some;
		this.is_show_pay_interview = is_show_pay_interview;
		this.parent_job_id = parent_job_id;
		this.expiry_date = expiry_date;
		this.monthly_play_to = monthly_play_to;
	}

	public int getJob_id() {
		return job_id;
	}

	public void setJob_id(int job_id) {
		this.job_id = job_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public int getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(int issue_date) {
		this.issue_date = issue_date;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public int getWork_area() {
		return work_area;
	}

	public void setWork_area(int work_area) {
		this.work_area = work_area;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getMonthly_play() {
		return monthly_play;
	}

	public void setMonthly_play(String monthly_play) {
		this.monthly_play = monthly_play;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getEffect_time() {
		return effect_time;
	}

	public void setEffect_time(int effect_time) {
		this.effect_time = effect_time;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getWorkyear() {
		return workyear;
	}

	public void setWorkyear(int workyear) {
		this.workyear = workyear;
	}

	public int getCounts() {
		return counts;
	}

	public void setCounts(int counts) {
		this.counts = counts;
	}

	public int getQuyu() {
		return quyu;
	}

	public void setQuyu(int quyu) {
		this.quyu = quyu;
	}

	public int getLast_refresh_time() {
		return last_refresh_time;
	}

	public void setLast_refresh_time(int last_refresh_time) {
		this.last_refresh_time = last_refresh_time;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public int getPost_rank() {
		return post_rank;
	}

	public void setPost_rank(int post_rank) {
		this.post_rank = post_rank;
	}

	public int getWork_type() {
		return work_type;
	}

	public void setWork_type(int work_type) {
		this.work_type = work_type;
	}

	public int getDeltime() {
		return deltime;
	}

	public void setDeltime(int deltime) {
		this.deltime = deltime;
	}

	public int getHy() {
		return hy;
	}

	public void setHy(int hy) {
		this.hy = hy;
	}

	public int getRecruiter_count() {
		return recruiter_count;
	}

	public void setRecruiter_count(int recruiter_count) {
		this.recruiter_count = recruiter_count;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getIs_show_number_some() {
		return is_show_number_some;
	}

	public void setIs_show_number_some(int is_show_number_some) {
		this.is_show_number_some = is_show_number_some;
	}

	public int getIs_show_pay_interview() {
		return is_show_pay_interview;
	}

	public void setIs_show_pay_interview(int is_show_pay_interview) {
		this.is_show_pay_interview = is_show_pay_interview;
	}

	public int getParent_job_id() {
		return parent_job_id;
	}

	public void setParent_job_id(int parent_job_id) {
		this.parent_job_id = parent_job_id;
	}

	public int getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(int expiry_date) {
		this.expiry_date = expiry_date;
	}

	public int getMonthly_play_to() {
		return monthly_play_to;
	}

	public void setMonthly_play_to(int monthly_play_to) {
		this.monthly_play_to = monthly_play_to;
	}

}
