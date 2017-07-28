package com.hr.ui.model;

public class HistoyInfo {

    int id;// 自增列
    String industry_id;// 行业ID
    String function_id;// 职能ID
    String place_id;// 地点ID
    String function_value;// 已选职能的值
    String place_value;// 已选地点的值
    String search_value;// 搜索关键字的值


    public int getId() {
        return id;
    }

    public String getSearch_value() {
        return search_value;
    }

    public void setSearch_value(String search_value) {
        this.search_value = search_value;
    }

    public String getFunction_value() {
        return function_value;
    }

    public void setFunction_value(String function_value) {
        this.function_value = function_value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndustry_id() {
        return industry_id;
    }

    public void setIndustry_id(String industry_id) {
        this.industry_id = industry_id;
    }

    public String getFunction_id() {
        return function_id;
    }

    public void setFunction_id(String function_id) {
        this.function_id = function_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_value() {
        return place_value;
    }

    public void setPlace_value(String place_value) {
        this.place_value = place_value;
    }

}
