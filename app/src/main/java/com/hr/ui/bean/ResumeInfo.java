package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 11:43
 * 邮箱：bestxt@qq.com
 * <p/>
 * 求职意向表
 */
public class ResumeInfo {


    private Long id;

    public String getResume_id() {
        return resume_id;
    }

    public void setResume_id(String resume_id) {
        this.resume_id = resume_id;
    }

    /**
     * 简历ID
     */

    private String resume_id = "";
    /**
     * 期望工作性质
     */
    private String jobtype = "";
    /**
     * 从事职位
     */
    private String func = "";
    /**
     * 从事领域
     */
    private String lingyu = "";
    /**
     * 期望工作地
     */
    private String workarea = "";
    /**
     * 希望月薪
     */
    private String order_salary = "";
    /**
     * 求职状态
     */
    private String current_workstate = "";

    public String getCurrent_workstate() {
        return current_workstate;
    }

    public void setCurrent_workstate(String current_workstate) {
        this.current_workstate = current_workstate;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String funcc) {
        this.func = funcc;
    }

    public String getLingyu() {
        return lingyu;
    }

    public void setLingyu(String lingyu) {
        this.lingyu = lingyu;
    }

    public String getWorkarea() {
        return workarea;
    }

    public void setWorkarea(String workarea) {
        this.workarea = workarea;
    }

    public String getOrder_salary() {
        return order_salary;
    }

    public void setOrder_salary(String order_salary) {
        this.order_salary = order_salary;
    }


}
