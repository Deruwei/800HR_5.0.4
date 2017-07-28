package com.hr.ui.model;

/**
 * 个人中心-申请的职位
 * 
 */
public class PositionInfo {
	// {"applied_list":
	// [{"enterprise_name":"建筑测试企业",
	// "applied_time":"2013-06-13 15:09:00",
	// "applied_num":"9",
	// "record_id":"145010801",
	// "is_expire":1,
	// "resume_id_lang":"5_zh",
	// "area":null,
	// "job_name":"多地区1",
	// "nautica":"116.361273,39.923993",
	// "enterprise_id":"rfSaK",
	// "is_apply":0,
	// "resume_title":"",
	// "is_favourite":0,
	// "job_id":"remkQ"}
	// ],
	// "error_code":0,
	// "navpage_info":{"total_pages":1,"record_nums":"1","page_nums":"20","current_page":1}
	// }

	private String enterprise_name;// 职位名称
	private String applied_time;// 申请时间
	private String applied_num;// 申请人数
	private String record_id;//
	private String is_expire;//
	private String resume_id_lang;//
	private String area;// 领域
	private String job_name;//
	private String nautica;//
	private String enterprise_id;// 职位id
	private String is_apply;//
	private String resume_title;//
	private String is_favourite;//
	private String job_id;// 工作id

	public PositionInfo() {
		// TODO Auto-generated constructor stub
	}

	public PositionInfo(String enterprise_name, String applied_time,
			String applied_num, String record_id, String is_expire,
			String resume_id_lang, String area, String job_name,
			String nautica, String enterprise_id, String is_apply,
			String resume_title, String is_favourite, String job_id) {
		super();
		this.enterprise_name = enterprise_name;
		this.applied_time = applied_time;
		this.applied_num = applied_num;
		this.record_id = record_id;
		this.is_expire = is_expire;
		this.resume_id_lang = resume_id_lang;
		this.area = area;
		this.job_name = job_name;
		this.nautica = nautica;
		this.enterprise_id = enterprise_id;
		this.is_apply = is_apply;
		this.resume_title = resume_title;
		this.is_favourite = is_favourite;
		this.job_id = job_id;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getApplied_time() {
		return applied_time;
	}

	public void setApplied_time(String applied_time) {
		this.applied_time = applied_time;
	}

	public String getApplied_num() {
		return applied_num;
	}

	public void setApplied_num(String applied_num) {
		this.applied_num = applied_num;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getIs_expire() {
		return is_expire;
	}

	public void setIs_expire(String is_expire) {
		this.is_expire = is_expire;
	}

	public String getResume_id_lang() {
		return resume_id_lang;
	}

	public void setResume_id_lang(String resume_id_lang) {
		this.resume_id_lang = resume_id_lang;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getNautica() {
		return nautica;
	}

	public void setNautica(String nautica) {
		this.nautica = nautica;
	}

	public String getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(String enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public String getIs_apply() {
		return is_apply;
	}

	public void setIs_apply(String is_apply) {
		this.is_apply = is_apply;
	}

	public String getResume_title() {
		return resume_title;
	}

	public void setResume_title(String resume_title) {
		this.resume_title = resume_title;
	}

	public String getIs_favourite() {
		return is_favourite;
	}

	public void setIs_favourite(String is_favourite) {
		this.is_favourite = is_favourite;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	@Override
	public String toString() {
		return "PositionInfo [enterprise_name=" + enterprise_name
				+ ", applied_time=" + applied_time + ", applied_num="
				+ applied_num + ", record_id=" + record_id + ", is_expire="
				+ is_expire + ", resume_id_lang=" + resume_id_lang + ", area="
				+ area + ", job_name=" + job_name + ", nautica=" + nautica
				+ ", enterprise_id=" + enterprise_id + ", is_apply=" + is_apply
				+ ", resume_title=" + resume_title + ", is_favourite="
				+ is_favourite + ", job_id=" + job_id + "]";
	}

}
