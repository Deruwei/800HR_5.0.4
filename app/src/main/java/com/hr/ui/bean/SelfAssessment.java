package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 17:50
 * 邮箱：bestxt@qq.com
 * <p/>
 * 自我评价
 */
public class SelfAssessment {
    private Long id;
    /**
     * 自我评价内容
     */
    private String introduction = "";

    public String getResume_id() {
        return resume_id;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * 用户ID
     */
    private String user_id = "";
    /**
     * 简历ID
     */
    private String resume_id = "";
}
