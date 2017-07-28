package com.hr.ui.model;

import java.io.Serializable;

/**
 * 个人基本信息
 */
public class ResumeBaseInfo implements Serializable {
    public int id = -1;
    public int isUpdate = 0;// 是否修改过（0未修改过；1修改过）

    public String cardtype = "";// 证件类型
    public String work_beginyear = "";// 工作开始年
    public String sex = "";// 性别ID
    public String location = "";// 现在居住城市ID
    public String current_workstate = "";// 当前求职状态
    public String post_rank = "";// 现有职称ID
    public String height = "";// 身高
    public String emailaddress = "";// 邮箱
    public String name = "";// 名称
    public String year = "2016";// 出生年
    public String user_id = "";// 用户ID
    public String im_account = "";// 即时通讯帐号
    public String modify_time = "";// 时间戳
    public String ydphone = "";// 手机号码
    public String pic_filekey = "";// 头像地址加串，真实地址前面加http://file.800hr.com/
    //	public String name_echo_yes = "";// 保密全名、只显示姓氏，0不保密，1保密
    public String zipcode = "";// 邮政编码
    public String polity = "";// 政治面貌ID
    public String idnumber = "";// 证件号码
    public String blood = "";// 血型
    public String marriage = "";// 婚姻状态ID
    public String resume_language = "";// 简历语言
    public String homepage = "";//
    public String nationality = "";// 国籍ID
    public String address = "";// 通讯地址
    public String hukou = "";// 户口所在城市ID
    public String month = "1";// 出生月
    public String im_type = "";// 即时通讯类型
    public String day = "1";// 出生日
    public String telephone = "";// 固定电话
    public String echo_yes = "";//保密全名、只显示姓氏，0不保密，1保密
    public String ydphone_verify_status = "";//是否验证过手机号，1未验证，2以验证

    public String getYdphone_verify_status() {
        return ydphone_verify_status;
    }

    public void setYdphone_verify_status(String ydphone_verify_status) {
        this.ydphone_verify_status = ydphone_verify_status;
    }

    public String getEcho_yes() {
        return echo_yes;
    }

    public void setEcho_yes(String echo_yes) {
        this.echo_yes = echo_yes;
    }

    public ResumeBaseInfo() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getWork_beginyear() {
        return work_beginyear;
    }

    public void setWork_beginyear(String work_beginyear) {
        this.work_beginyear = work_beginyear;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrent_workstate() {
        return current_workstate;
    }

    public void setCurrent_workstate(String current_workstate) {
        this.current_workstate = current_workstate;
    }

    public String getPost_rank() {
        return post_rank;
    }

    public void setPost_rank(String post_rank) {
        this.post_rank = post_rank;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIm_account() {
        return im_account;
    }

    public void setIm_account(String im_account) {
        this.im_account = im_account;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public String getYdphone() {
        return ydphone;
    }

    public void setYdphone(String ydphone) {
        this.ydphone = ydphone;
    }

    public String getPic_filekey() {
        return pic_filekey;
    }

    public void setPic_filekey(String pic_filekey) {
        this.pic_filekey = pic_filekey;
    }

//	public String getName_echo_yes() {
//		return name_echo_yes;
//	}
//
//	public void setName_echo_yes(String name_echo_yes) {
//		this.name_echo_yes = name_echo_yes;
//	}

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPolity() {
        return polity;
    }

    public void setPolity(String polity) {
        this.polity = polity;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getResume_language() {
        return resume_language;
    }

    public void setResume_language(String resume_language) {
        this.resume_language = resume_language;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHukou() {
        return hukou;
    }

    public void setHukou(String hukou) {
        this.hukou = hukou;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getIm_type() {
        return im_type;
    }

    public void setIm_type(String im_type) {
        this.im_type = im_type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    @Override
    public String toString() {
        return "ResumeBaseInfo [id=" + id + ", isUpdate=" + isUpdate
                + ", cardtype=" + cardtype + ", work_beginyear="
                + work_beginyear + ", sex=" + sex + ", location=" + location
                + ", current_workstate=" + current_workstate + ", post_rank="
                + post_rank + ", height=" + height + ", emailaddress="
                + emailaddress + ", name=" + name + ", year=" + year
                + ", user_id=" + user_id + ", im_account=" + im_account
                + ", modify_time=" + modify_time + ", ydphone=" + ydphone
                + ", pic_filekey=" + pic_filekey + ", echo_yes="
                + echo_yes + ", zipcode=" + zipcode + ", polity=" + polity
                + ", idnumber=" + idnumber + ", blood=" + blood + ", marriage="
                + marriage + ", resume_language=" + resume_language
                + ", homepage=" + homepage + ", nationality=" + nationality
                + ", address=" + address + ", hukou=" + hukou + ", month="
                + month + ", im_type=" + im_type + ", day=" + day
                + ", telephone=" + telephone + ",ydphone_verify_status" + ydphone_verify_status + "]";
    }
}
