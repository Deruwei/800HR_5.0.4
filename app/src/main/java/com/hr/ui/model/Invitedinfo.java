package com.hr.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Invitedinfo implements Parcelable {

    String id; // 邀请ID
    String job_id; // 职位ID
    String job_name; // 职位名称
    String enterprise_id; // 企业ID
    String enterprise_name; // 企业名称
    String is_new; // 是否阅读过
    String is_email; // 是否邮件
    String is_sms; // 是否短信
    String email_title; // 标题
    String email_content; // 邮件内容
    String sms_content; // 短信内容
    String nautica; // 企业所在坐标
    String invited_title;
    String record_id;// 记录ID,暂不明白干嘛用的
    String invited_time; // 时间格式Y-m-d
    String area; // 企业所在地区

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getEnterprise_name() {
        return enterprise_name;
    }

    public void setEnterprise_name(String enterprise_name) {
        this.enterprise_name = enterprise_name;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_email() {
        return is_email;
    }

    public void setIs_email(String is_email) {
        this.is_email = is_email;
    }

    public String getIs_sms() {
        return is_sms;
    }

    public void setIs_sms(String is_sms) {
        this.is_sms = is_sms;
    }

    public String getEmail_title() {
        return email_title;
    }

    public void setEmail_title(String email_title) {
        this.email_title = email_title;
    }

    public String getEmail_content() {
        return email_content;
    }

    public void setEmail_content(String email_content) {
        this.email_content = email_content;
    }

    public String getSms_content() {
        return sms_content;
    }

    public void setSms_content(String sms_content) {
        this.sms_content = sms_content;
    }

    public String getNautica() {
        return nautica;
    }

    public void setNautica(String nautica) {
        this.nautica = nautica;
    }

    public String getInvited_time() {
        return invited_time;
    }

    public void setInvited_time(String invited_time) {
        this.invited_time = invited_time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getInvited_title() {
        return invited_title;
    }

    public void setInvited_title(String invited_title) {
        this.invited_title = invited_title;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    // 无参数构造器方法，供外界创建类的实例时调用
    public Invitedinfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.job_id);
        dest.writeString(this.job_name);
        dest.writeString(this.enterprise_id);
        dest.writeString(this.enterprise_name);
        dest.writeString(this.is_new);
        dest.writeString(this.is_email);
        dest.writeString(this.is_sms);
        dest.writeString(this.email_title);
        dest.writeString(this.email_content);
        dest.writeString(this.sms_content);
        dest.writeString(this.nautica);
        dest.writeString(this.invited_title);
        dest.writeString(this.record_id);
        dest.writeString(this.invited_time);
        dest.writeString(this.area);
    }

    protected Invitedinfo(Parcel in) {
        this.id = in.readString();
        this.job_id = in.readString();
        this.job_name = in.readString();
        this.enterprise_id = in.readString();
        this.enterprise_name = in.readString();
        this.is_new = in.readString();
        this.is_email = in.readString();
        this.is_sms = in.readString();
        this.email_title = in.readString();
        this.email_content = in.readString();
        this.sms_content = in.readString();
        this.nautica = in.readString();
        this.invited_title = in.readString();
        this.record_id = in.readString();
        this.invited_time = in.readString();
        this.area = in.readString();
    }

    public static final Creator<Invitedinfo> CREATOR = new Creator<Invitedinfo>() {
        public Invitedinfo createFromParcel(Parcel source) {
            return new Invitedinfo(source);
        }

        public Invitedinfo[] newArray(int size) {
            return new Invitedinfo[size];
        }
    };
}
