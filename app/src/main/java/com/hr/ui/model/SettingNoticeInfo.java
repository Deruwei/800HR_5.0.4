package com.hr.ui.model;

/**
 * 通知设置
 * 
 */
public class SettingNoticeInfo {

	private int rushjob_state;// 1：开 2：关
	private int invite_state;// 面试通知状态 1：开 2：关
	private String notice_bgntime= ""; // 允许通知开始时间 （HH:MM）
	private String notice_endtime= "";// 允许通知结束时间 （HH:MM）
	private int notice_type;// 通知类型
	public SettingNoticeInfo() {
		// TODO Auto-generated constructor stub
	}
	public SettingNoticeInfo(int rushjob_state, int invite_state,
			String notice_bgntime, String notice_endtime, int notice_type) {
		super();
		this.rushjob_state = rushjob_state;
		this.invite_state = invite_state;
		this.notice_bgntime = notice_bgntime;
		this.notice_endtime = notice_endtime;
		this.notice_type = notice_type;
	}

	public int getRushjob_state() {
		return rushjob_state;
	}

	public void setRushjob_state(int rushjob_state) {
		this.rushjob_state = rushjob_state;
	}

	public int getInvite_state() {
		return invite_state;
	}

	public void setInvite_state(int invite_state) {
		this.invite_state = invite_state;
	}

	public String getNotice_bgntime() {
		return notice_bgntime;
	}

	public void setNotice_bgntime(String notice_bgntime) {
		this.notice_bgntime = notice_bgntime;
	}

	public String getNotice_endtime() {
		return notice_endtime;
	}

	public void setNotice_endtime(String notice_endtime) {
		this.notice_endtime = notice_endtime;
	}

	public int getNotice_type() {
		return notice_type;
	}

	public void setNotice_type(int notice_type) {
		this.notice_type = notice_type;
	}
}
