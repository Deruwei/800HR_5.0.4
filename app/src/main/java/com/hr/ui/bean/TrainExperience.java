
package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 17:40
 * 邮箱：bestxt@qq.com
 *
 * 培训经历
 */
public class TrainExperience {

    private long id;

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
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

    public String getTraindetail() {
        return traindetail;
    }

    public void setTraindetail(String traindetail) {
        this.traindetail = traindetail;
    }

    /**
     * 培训经历ID
     */
    private String plant_id = "";

    /**
     * 机构名称
     */
    private String institution = "";

    /**
     * 课程
     */
    private String course = "";

    /**
     * 培训地点
     */
    private String place = "";

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
     * 专业描述
     */
    private String traindetail = "";
}
