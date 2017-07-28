package com.hr.ui.model;

import java.io.Serializable;

/**
 * 个人培训信息
 *
 */
public class ResumePlant implements Serializable {
    public int id = -1;
    public String user_id;
    public String fromyear = "2016";
    public String frommonth = "1";
    public String toyear = "2016";
    public String tomonth = "1";
    public String institution;
    public String course;
    public String place;
    public String certification;
    public String traindetail;
    public String resume_id;
    public String resume_language;
    public String plant_id;

    public ResumePlant() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getTraindetail() {
        return traindetail;
    }

    public void setTraindetail(String traindetail) {
        this.traindetail = traindetail;
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

    @Override
    public String toString() {
        return "ResumePlant [id=" + id + ", user_id=" + user_id + ", fromyear="
                + fromyear + ", frommonth=" + frommonth + ", toyear=" + toyear
                + ", tomonth=" + tomonth + ", institution=" + institution
                + ", course=" + course + ", place=" + place
                + ", certification=" + certification + ", traindetail="
                + traindetail + ", resume_id=" + resume_id
                + ", resume_language=" + resume_language + ", plant_id="
                + plant_id + "]";
    }

}
