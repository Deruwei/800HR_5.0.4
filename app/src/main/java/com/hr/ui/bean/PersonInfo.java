package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/12 19:04
 * 邮箱：bestxt@qq.com
 * <p/>
 * 个人信息
 */
public class PersonInfo {

    private Long id;
    /**
     * 用户ID
     */
    private String user_id = "";
    /**
     * 用户姓名
     */
    private String name = "";
    /**
     * 用户性别
     */
    private String sex = "";
    /**
     * 出生年
     */
    private String year = "";
    /**
     * 出生月
     */
    private String month = "";
    /**
     * 出生日
     */
    private String day = "";
    /**
     * 国籍
     */
    private String nation = "";
    /**
     * 开始工作时间
     */
    private String work_beginyear = "";
    /**
     * 现有职称
     */
    private String post_rank = "";
    /**
     * 电话
     */
    private String ydphone = "";
    /**
     * 邮箱
     */
    private String emailaddress = "";

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getWork_beginyear() {
        return work_beginyear;
    }

    public void setWork_beginyear(String work_beginyear) {
        this.work_beginyear = work_beginyear;
    }

    public String getPost_rank() {
        return post_rank;
    }

    public void setPost_rank(String post_rank) {
        this.post_rank = post_rank;
    }

    public String getYdphone() {
        return ydphone;
    }

    public void setYdphone(String ydphone) {
        this.ydphone = ydphone;
    }
}
