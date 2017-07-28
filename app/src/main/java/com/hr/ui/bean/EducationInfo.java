package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 15:56
 * 邮箱：bestxt@qq.com
 */
public class EducationInfo {
    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getFromyear() {
        return fromyear;
    }

    public void setFromyear(String fromyear) {
        this.fromyear = fromyear;
    }

    public String getFrommonth() {
        return frommonth;
    }

    public void setFrommonth(String frommonth) {
        this.frommonth = frommonth;
    }

    public String getToyear() {
        return toyear;
    }

    public void setToyear(String toyear) {
        this.toyear = toyear;
    }

    public String getTomonth() {
        return tomonth;
    }

    public void setTomonth(String tomonth) {
        this.tomonth = tomonth;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEdudetail() {
        return edudetail;
    }

    public void setEdudetail(String edudetail) {
        this.edudetail = edudetail;
    }

    public String getMoremajor() {
        return moremajor;
    }

    public void setMoremajor(String moremajor) {
        this.moremajor = moremajor;
    }

    private Long id;

    /**
     * 院校名称
     */
    private String schoolname = "";
    /**
     * 开始年
     */
    private String fromyear = "";
    /**
     * 开始月
     */
    private String frommonth = "";
    /**
     * 结束年
     */
    private String toyear = "";
    /**
     * 结束月
     */
    private String tomonth = "";
    /**
     * 学历学位
     */
    private String degree = "";
    /**
     * 专业描述
     */
    private String edudetail = "";
    /**
     * 专业名称
     */
    private String moremajor = "";
}
