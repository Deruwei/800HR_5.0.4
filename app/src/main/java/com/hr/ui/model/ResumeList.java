package com.hr.ui.model;


/**
 * 简历列表model
 *
 */
public class ResumeList {
    private String user_id = "";
    private String resume_id = "-1";
    private String resume_type = "";// 0.1.2.3(1，2才能修改和上传)1，2是社会简历，毕业生简历。0，是简历简历3，是附件简历
    private String title = "";
    private String open = "";
    private String uptime = "";
    private String castbehalf = "";
    private String important = "";
    private String fill_scale = "0";
    private String modifyTime = "";
    private String func = "";
    private String jobtype = "";
    private String add_time = "";
    private String order_salary = "";
    private String other_language = "";
    private int isUpdate = 0;// 是否修改过（0未修改过；1修改过）

    public String getIs_app() {
        return is_app;
    }

    public void setIs_app(String is_app) {
        this.is_app = is_app;
    }

    /**
     * 是否是APP 简历
     */
    private String is_app = "";

    public ResumeList() {
        // TODO Auto-generated constructor stub
    }

    public ResumeList(String user_id, String resume_id, String resume_type,
                      String title, String open, String uptime, String castbehalf,
                      String important, String fill_scale, String modifyTime,
                      String func, String jobtype, String add_time, String order_salary,
                      String other_language, int isUpdate) {
        super();
        this.user_id = user_id;
        this.resume_id = resume_id;
        this.resume_type = resume_type;
        this.title = title;
        this.open = open;
        this.uptime = uptime;
        this.castbehalf = castbehalf;
        this.important = important;
        this.fill_scale = fill_scale;
        this.modifyTime = modifyTime;
        this.func = func;
        this.jobtype = jobtype;
        this.add_time = add_time;
        this.order_salary = order_salary;
        this.other_language = other_language;
        this.isUpdate = isUpdate;
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

    public String getResume_type() {
        return resume_type;
    }

    public void setResume_type(String resume_type) {
        this.resume_type = resume_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getCastbehalf() {
        return castbehalf;
    }

    public void setCastbehalf(String castbehalf) {
        this.castbehalf = castbehalf;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getFill_scale() {
        return fill_scale;
    }

    public void setFill_scale(String fill_scale) {
        this.fill_scale = fill_scale;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getOrder_salary() {
        return order_salary;
    }

    public void setOrder_salary(String order_salary) {
        this.order_salary = order_salary;
    }

    public String getOther_language() {
        return other_language;
    }

    public void setOther_language(String other_language) {
        this.other_language = other_language;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    @Override
    public String toString() {
        return "ResumeList [user_id=" + user_id + ", resume_id=" + resume_id
                + ", resume_type=" + resume_type + ", title=" + title
                + ", open=" + open + ", uptime=" + uptime + ", castbehalf="
                + castbehalf + ", important=" + important + ", fill_scale="
                + fill_scale + ", modifyTime=" + modifyTime + ", func=" + func
                + ", jobtype=" + jobtype + ", add_time=" + add_time
                + ", order_salary=" + order_salary + ", other_language="
                + other_language + ", isUpdate=" + isUpdate + "]";
    }

}
