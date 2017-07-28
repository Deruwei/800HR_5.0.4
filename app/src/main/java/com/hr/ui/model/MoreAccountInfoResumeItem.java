package com.hr.ui.model;

/**
 * 第三方绑定多行业账号中，单一行业下简历信息
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class MoreAccountInfoResumeItem {
	private String title;
	private String fill_scale;

	public MoreAccountInfoResumeItem() {
		// TODO Auto-generated constructor stub
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFill_scale() {
		return fill_scale;
	}

	public void setFill_scale(String fill_scale) {
		this.fill_scale = fill_scale;
	}

	@Override
	public String toString() {
		return "ResumeList [title=" + title + ", fill_scale=" + fill_scale
				+ "]";
	}
}
