package com.hr.ui.model;

import java.io.Serializable;

/**
 * 工作经验
 */
public class ResumeExperience implements Serializable {
    public int id = -1;
    public String user_id;
    public String fromyear = "2016";
    public String frommonth = "1";
    public String toyear = "2016";
    public String tomonth = "1";
    public String company = "";
    public String companyhide = "";
    public String industry = "";
    public String companytype = "";
    public String stuffmunber = "";
    public String division = "";
    public String companyaddress = "";
    public String position = "";
    public String responsiblity = "";
    public String offreason = "";
    public String zhixi = "";
    public String is_overseas = "";
    public String country = "";
    public String resume_id = "";
    public String resume_language = "";
    public String lingyu = "";
    public String func = "";
    public String salary = "";
    public String salary_hide = "";
    public String experience_id = "";
    public String zhicheng = "";

    public ResumeExperience() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyhide() {
        return companyhide;
    }

    public void setCompanyhide(String companyhide) {
        this.companyhide = companyhide;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }

    public String getStuffmunber() {
        return stuffmunber;
    }

    public void setStuffmunber(String stuffmunber) {
        this.stuffmunber = stuffmunber;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getResponsibility() {
        return responsiblity;
    }

    public void setResponsibility(String responsibility) {
        this.responsiblity = responsibility;
    }

    public String getOffreason() {
        return offreason;
    }

    public void setOffreason(String offreason) {
        this.offreason = offreason;
    }

    public String getZhixi() {
        return zhixi;
    }

    public void setZhixi(String zhixi) {
        this.zhixi = zhixi;
    }

    public String getIs_overseas() {
        return is_overseas;
    }

    public void setIs_overseas(String is_overseas) {
        this.is_overseas = is_overseas;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getResume_id() {
        return resume_id;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    public String getResume_language() {
        return resume_language;
    }

    public void setResume_language(String resume_language) {
        this.resume_language = resume_language;
    }

    public String getLingyu() {
        return lingyu;
    }

    public void setLingyu(String lingyu) {
        this.lingyu = lingyu;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSalary_hide() {
        return salary_hide;
    }

    public void setSalary_hide(String salary_hide) {
        this.salary_hide = salary_hide;
    }

    public String getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(String experience_id) {
        this.experience_id = experience_id;
    }

    public String getZhicheng() {
        return zhicheng;
    }

    public void setZhicheng(String zhicheng) {
        this.zhicheng = zhicheng;
    }

    @Override
    public String toString() {
        return "ResumeExperience [id=" + id + ", user_id=" + user_id
                + ", fromyear=" + fromyear + ", frommonth=" + frommonth
                + ", toyear=" + toyear + ", tomonth=" + tomonth + ", company="
                + company + ", companyhide=" + companyhide + ", industry="
                + industry + ", companytype=" + companytype + ", stuffmunber="
                + stuffmunber + ", division=" + division + ", companyaddress="
                + companyaddress + ", position=" + position
                + ", responsiblity=" + responsiblity + ", offreason="
                + offreason + ", zhixi=" + zhixi + ", is_overseas="
                + is_overseas + ", country=" + country + ", resume_id="
                + resume_id + ", resume_language=" + resume_language
                + ", lingyu=" + lingyu + ", func=" + func + ", salary="
                + salary + ", salary_hide=" + salary_hide + ", experience_id="
                + experience_id + ", zhicheng=" + zhicheng + "]";
    }

}
