package com.hr.ui.model;

import java.io.Serializable;

/**
 * 教育经历
 *
 */
public class ResumeEducation implements Serializable {
    public int id = -1;
    public String user_id;
    public String frommonth = "1";
    public String fromyear = "2016";
    public String tomonth = "1";
    public String toyear = "2016";
    public String schoolname = "";
    public String moremajor = "";
    public String degree = "";
    public String edudetail = "";
    public String is_overseas = "";
    public String country = "";
    public String resume_id = "";
    public String resume_language = "";
    public String education_id = "";

    public ResumeEducation() {
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

    public String getFrommonth() {
        return frommonth;
    }

    public void setFrommonth(String frommonth) {
        this.frommonth = frommonth;
    }

    public String getFromyear() {
        return fromyear;
    }

    public void setFromyear(String fromyear) {
        this.fromyear = fromyear;
    }

    public String getTomonth() {
        return tomonth;
    }

    public void setTomonth(String tomonth) {
        this.tomonth = tomonth;
    }

    public String getToyear() {
        return toyear;
    }

    public void setToyear(String toyear) {
        this.toyear = toyear;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getMoremajor() {
        return moremajor;
    }

    public void setMoremajor(String moremajor) {
        this.moremajor = moremajor;
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

    public String getEducation_id() {
        return education_id;
    }

    public void setEducation_id(String education_id) {
        this.education_id = education_id;
    }

    @Override
    public String toString() {
        return "ResumeEducation [id=" + id + ", user_id=" + user_id
                + ", frommonth=" + frommonth + ", fromyear=" + fromyear
                + ", tomonth=" + tomonth + ", toyear=" + toyear
                + ", schoolname=" + schoolname + ", moremajor=" + moremajor
                + ", degree=" + degree + ", eduaddress=" + ", edudetail="
                + edudetail + ", is_overseas=" + is_overseas + ", country="
                + country + ", resume_id=" + resume_id + ", resume_language="
                + resume_language + ", education_id=" + education_id + "]";
    }

}
