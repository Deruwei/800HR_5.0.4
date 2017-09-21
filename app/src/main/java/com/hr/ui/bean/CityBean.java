package com.hr.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wdr on 2017/8/25.
 */

public class CityBean implements Serializable {
    private String id;
    private String name;
    private boolean isCheck;
    private boolean sign;
    public CityBean(){

    }
    public CityBean(String id,String name,boolean isCheck,boolean sign){
       this.id=id;
        this.name=name;
        this.isCheck=isCheck;
        this.sign=sign;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isCheck=" + isCheck +
                ", sign=" + sign +
                '}';
    }
}
