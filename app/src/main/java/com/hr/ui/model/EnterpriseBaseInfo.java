package com.hr.ui.model;

/**
 * 企业基本信息表
 * 
 */
public class EnterpriseBaseInfo {
	private int user_id;// 用户ID
	private String enterprise_name;// 企业名称
	private String start_day;// 单位成立日期
	private String address;// 单位地址
	private String homepage;// 单位主页
	private String synopsis;// 单位简介
	private int company;// 企业性质
	private String ent_logo;// 企业logo
	private String Mapmark;// 企业地图标志
	private String map_lon;// 0.00000 mapbar地图经度
	private String map_lat;// 0.00000 mapbar地图纬度
	private String Google_map_lon;// 0.00000 google地图经度
	private String Google_map_lat;// 0.00000 google地图纬度
	
	public EnterpriseBaseInfo() {
		// TODO Auto-generated constructor stub
	}

	public EnterpriseBaseInfo(int user_id, String enterprise_name,
			String start_day, String address, String homepage, String synopsis,
			int company, String ent_logo, String mapmark, String map_lon,
			String map_lat, String google_map_lon, String google_map_lat) {
		super();
		this.user_id = user_id;
		this.enterprise_name = enterprise_name;
		this.start_day = start_day;
		this.address = address;
		this.homepage = homepage;
		this.synopsis = synopsis;
		this.company = company;
		this.ent_logo = ent_logo;
		Mapmark = mapmark;
		this.map_lon = map_lon;
		this.map_lat = map_lat;
		Google_map_lon = google_map_lon;
		Google_map_lat = google_map_lat;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public String getStart_day() {
		return start_day;
	}

	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public int getCompany() {
		return company;
	}

	public void setCompany(int company) {
		this.company = company;
	}

	public String getEnt_logo() {
		return ent_logo;
	}

	public void setEnt_logo(String ent_logo) {
		this.ent_logo = ent_logo;
	}

	public String getMapmark() {
		return Mapmark;
	}

	public void setMapmark(String mapmark) {
		Mapmark = mapmark;
	}

	public String getMap_lon() {
		return map_lon;
	}

	public void setMap_lon(String map_lon) {
		this.map_lon = map_lon;
	}

	public String getMap_lat() {
		return map_lat;
	}

	public void setMap_lat(String map_lat) {
		this.map_lat = map_lat;
	}

	public String getGoogle_map_lon() {
		return Google_map_lon;
	}

	public void setGoogle_map_lon(String google_map_lon) {
		Google_map_lon = google_map_lon;
	}

	public String getGoogle_map_lat() {
		return Google_map_lat;
	}

	public void setGoogle_map_lat(String google_map_lat) {
		Google_map_lat = google_map_lat;
	}
}
