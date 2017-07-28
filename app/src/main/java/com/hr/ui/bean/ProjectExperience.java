package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 17:04
 * 邮箱：bestxt@qq.com
 *
 * 项目经验
 */
public class ProjectExperience {

    private Long id;
    /**
     * 项目名称
     */
    private String projectname = "";
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
     * 项目描述
     */
    private String projectdesc = "";
    /**
     * 项目职务
     */
    private String position = "";

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
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

    public String getProjectdesc() {
        return projectdesc;
    }

    public void setProjectdesc(String projectdesc) {
        this.projectdesc = projectdesc;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
}
