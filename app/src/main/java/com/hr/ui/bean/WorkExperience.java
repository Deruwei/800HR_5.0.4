package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 16:35
 * 邮箱：bestxt@qq.com
 * 工作经验
 */
public class WorkExperience {
    private Long id;
    /**
     * 工作经历ID
     */
    private String experience_id = "";
    /**
     * 单位名称
     */
    private String company = "";
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
     * 公司地址
     */
    private String companyaddress = "";

    public String getResponsiblity() {
        return responsiblity;
    }

    public void setResponsiblity(String responsiblity) {
        this.responsiblity = responsiblity;
    }

    public String getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(String experience_id) {
        this.experience_id = experience_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    /**
     * 工资
     */
    private String salary = "";
    /**
     * 职位
     */
    private String position = "";
    /**
     * 职位描述
     */
    private String responsiblity = "";
}
