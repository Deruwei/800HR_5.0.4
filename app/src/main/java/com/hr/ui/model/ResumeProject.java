package com.hr.ui.model;

import java.io.Serializable;

/**
 * 项目经历
 *
 */
public class ResumeProject implements Serializable {
    public int id = -1;
    public String fromyear = "2016";
    public String frommonth = "1";
    public String toyear = "2016";
    public String tomonth = "1";
    public String projectname = "";
    public String position;
    public String projectdesc;
    public String responsibility;
    public String resume_id;
    public String resume_language;
    public String project_id;

    public ResumeProject() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProjectdesc() {
        return projectdesc;
    }

    public void setProjectdesc(String projectdesc) {
        this.projectdesc = projectdesc;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    @Override
    public String toString() {
        return "ResumeProject [id=" + id + ", fromyear=" + fromyear
                + ", frommonth=" + frommonth + ", toyear=" + toyear
                + ", tomonth=" + tomonth + ", projectname=" + projectname
                + ", position=" + position + ", projectdesc=" + projectdesc
                + ", responsibility=" + responsibility + ", resume_id="
                + resume_id + ", resume_language=" + resume_language
                + ", project_id=" + project_id + "]";
    }

}
