package com.hr.ui.model;

import java.io.Serializable;


/**
 * 简历标题信息
 *
 */
public class ResumeTitle implements Serializable {
    public String user_id = "";
    public String resume_id = "";
    public String title = "";
    public String key_word = "";
    public String resume_language = "";
    public String resume_type = "";// 简历类型 1：社会人才简历，2学生简历，3附件简历0简单简历
    public String open = "";
    public String uptime = "";
    public String fill_scale = "";
    public String castbehalf = "";
    public String add_time = "";
    public String important = "";
    public String modify_time = "";
    public int id = -1;
    public int isUpdate = 0;// 是否修改过（0未修改过；1修改过）


    public String getIs_app() {
        return is_app;
    }

    public void setIs_app(String is_app) {
        this.is_app = is_app;
    }

    public String is_app ="0";
    public ResumeTitle() {
        // TODO Auto-generated constructor stub
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getResume_id() {
        return resume_id;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }

    public String getResume_language() {
        return resume_language;
    }

    public void setResume_language(String resume_language) {
        this.resume_language = resume_language;
    }

    public String getResume_type() {
        return resume_type;
    }

    public void setResume_type(String resume_type) {
        this.resume_type = resume_type;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getFill_scale() {
        return fill_scale;
    }

    public void setFill_scale(String fill_scale) {
        this.fill_scale = fill_scale;
    }

    public String getCastbehalf() {
        return castbehalf;
    }

    public void setCastbehalf(String castbehalf) {
        this.castbehalf = castbehalf;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }
    @Override
    public String toString() {
        return "ResumeTitle [user_id=" + user_id + ", resume_id=" + resume_id
                + ", title=" + title + ", key_word=" + key_word
                + ", resume_language=" + resume_language + ", resume_type="
                + resume_type + ", open=" + open + ", uptime=" + uptime
                + ", fill_scale=" + fill_scale + ", castbehalf=" + castbehalf
                + ", add_time=" + add_time + ", important=" + important
                + ", modify_time=" + modify_time + ", id=" + id + ", isUpdate="
                + isUpdate + "]";
    }
}
