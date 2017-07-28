package com.hr.ui.model;

/**
 * 新闻资讯内容
 * 
 */
public class News {
	private int NewsID;// 此条职位的ID
	private String Title;// 单位用户的ID
	private String body;// 职位名称
	private int issue_date;// 发布（刷新）日期
	private String Category;// 分类名称

	public News() {
		// TODO Auto-generated constructor stub
	}

	public News(int newsID, String title, String body, int issue_date,
			String category) {
		super();
		NewsID = newsID;
		Title = title;
		this.body = body;
		this.issue_date = issue_date;
		Category = category;
	}

	public int getNewsID() {
		return NewsID;
	}

	public void setNewsID(int newsID) {
		NewsID = newsID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(int issue_date) {
		this.issue_date = issue_date;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

}
