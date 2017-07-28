package com.hr.ui.model;

/**
 * 作者：Colin
 * 日期：2016/1/26 14:57
 * 邮箱：bestxt@qq.com
 *
 * 关键词搜索历史记录
 */
public class KeyWorldHistory {
    public int getId() {
        return id;
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

    public String getSearch_value() {
        return search_value;
    }

    public void setSearch_value(String search_value) {
        this.search_value = search_value;
    }

    int id;// 自增列
    String industry_id;// 行业ID
    String place_id;// 地点ID
    String place_value;// 已选地点的值
    String search_value;// 搜索关键字的值

    public String getWordtype() {
        return wordtype;
    }

    public void setWordtype(String wordtype) {
        this.wordtype = wordtype;
    }

    private String wordtype;



}
