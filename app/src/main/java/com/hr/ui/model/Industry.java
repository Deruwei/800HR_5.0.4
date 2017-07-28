package com.hr.ui.model;

import java.io.Serializable;

public class Industry implements Serializable {
    private static final long serialVersionUID = 1L;

    private int a_id;// 广告ID
    private int c_id;// 分类ID
    private String title;// 标题
    private int topic_type;// 1.专题网址，2.企业详情
    private String topic_url;// 专题网址
    private String enterprise_id;// 企业用户ID
    private String describe;// 描述
    private String pic_path;// 大尺寸图片路径
    private String pic_s_path;// 小尺寸图片路径
    private int click_num;// 点击量总计数器
    private int edit_time;// 最后编辑时间
    private String company_type;// 公司性质
    private String stuff_munber;// 公司规模
    private int industry;// 行业ID

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(int topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getPic_s_path() {
        return pic_s_path;
    }

    public void setPic_s_path(String pic_s_path) {
        this.pic_s_path = pic_s_path;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public int getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(int edit_time) {
        this.edit_time = edit_time;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getStuff_munber() {
        return stuff_munber;
    }

    public void setStuff_munber(String stuff_munber) {
        this.stuff_munber = stuff_munber;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "Industry{" +
                "a_id=" + a_id +
                ", c_id=" + c_id +
                ", title='" + title + '\'' +
                ", topic_type=" + topic_type +
                ", topic_url='" + topic_url + '\'' +
                ", enterprise_id='" + enterprise_id + '\'' +
                ", describe='" + describe + '\'' +
                ", pic_path='" + pic_path + '\'' +
                ", pic_s_path='" + pic_s_path + '\'' +
                ", click_num=" + click_num +
                ", edit_time=" + edit_time +
                ", company_type='" + company_type + '\'' +
                ", stuff_munber='" + stuff_munber + '\'' +
                ", industry=" + industry +
                '}';
    }
}
