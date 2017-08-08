package com.hr.ui.utils;

import android.util.Log;

import com.hr.ui.model.Industry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by wdr on 2017/8/8.
 */

public class GetJssonList {
    /**
     * 获取发现中专题活动的数据列表
     * @param ad_type
     * @param json_result
     * @return
     */
    public static ArrayList<Industry> getEnterpriseJson(int ad_type,String json_result) {
        ArrayList<Industry> enterprise_data=new ArrayList<>();
        Log.i("json", "======json" + json_result.toString());
        try {
            JSONObject jsonObject = new JSONObject(json_result);
            int error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return null;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.opt(i);
                Industry industry = new Industry();
                industry.setA_id(obj.getInt("a_id"));
                industry.setC_id(obj.getInt("c_id"));
                industry.setTitle(obj.getString("title"));
                industry.setTopic_type(obj.getInt("topic_type"));
                industry.setTopic_url(obj.getString("topic_url"));
                industry.setEnterprise_id(obj.getString("enterprise_id"));
                industry.setDescribe(obj.getString("ad_txt"));
                industry.setPic_path(obj.getString("pic_path"));
                industry.setPic_s_path(obj.getString("pic_s_path"));
                industry.setClick_num(obj.getInt("click_num"));
                industry.setEdit_time(obj.getInt("edit_time"));
                industry.setCompany_type(obj.getString("company_type"));
                industry.setStuff_munber(obj.getString("stuff_munber"));
                if (ad_type == 1) {
                    enterprise_data.add(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enterprise_data;
    }

    /**
     * 获取发现中炫公司的数据集合
     * @param ad_type
     * @param json_result
     * @return
     */
    public static ArrayList<Industry> getDazzleJson(int ad_type,String json_result) {
        ArrayList<Industry> dazzle_data=new ArrayList<>();
        Log.i("json", "======json" + json_result.toString());
        try {
            JSONObject jsonObject = new JSONObject(json_result);
            int error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return null;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.opt(i);
                Industry industry = new Industry();
                industry.setA_id(obj.getInt("a_id"));
                industry.setC_id(obj.getInt("c_id"));
                industry.setTitle(obj.getString("title"));
                industry.setTopic_type(obj.getInt("topic_type"));
                industry.setTopic_url(obj.getString("topic_url"));
                industry.setEnterprise_id(obj.getString("enterprise_id"));
                industry.setDescribe(obj.getString("ad_txt"));
                industry.setPic_path(obj.getString("pic_path"));
                industry.setPic_s_path(obj.getString("pic_s_path"));
                industry.setClick_num(obj.getInt("click_num"));
                industry.setEdit_time(obj.getInt("edit_time"));
                industry.setCompany_type(obj.getString("company_type"));
                industry.setStuff_munber(obj.getString("stuff_munber"));
                if (ad_type == 2) {
                    dazzle_data.add(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dazzle_data;
    }

    /**
     * 获取发现中品牌招聘的数据集合
     * @param ad_type
     * @param json_result
     * @return
     */
    public static ArrayList<Industry> getSpecialJson(int ad_type,String json_result) {
        ArrayList<Industry> special_data=new ArrayList<>();
        Log.i("json", "======json" + json_result.toString());
        try {
            JSONObject jsonObject = new JSONObject(json_result);
            int error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return null;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.opt(i);
                Industry industry = new Industry();
                industry.setA_id(obj.getInt("a_id"));
                industry.setC_id(obj.getInt("c_id"));
                industry.setTitle(obj.getString("title"));
                industry.setTopic_type(obj.getInt("topic_type"));
                industry.setTopic_url(obj.getString("topic_url"));
                industry.setEnterprise_id(obj.getString("enterprise_id"));
                industry.setDescribe(obj.getString("ad_txt"));
                industry.setPic_path(obj.getString("pic_path"));
                industry.setPic_s_path(obj.getString("pic_s_path"));
                industry.setClick_num(obj.getInt("click_num"));
                industry.setEdit_time(obj.getInt("edit_time"));
                industry.setCompany_type(obj.getString("company_type"));
                industry.setStuff_munber(obj.getString("stuff_munber"));
                if (ad_type == 3) {
                    special_data.add(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return special_data;
    }

    /**
     * 获取职位推荐的数据集合
     * @param json_result
     * @return
     */
    public static ArrayList<HashMap<String,Object>> searchResult_json(String json_result) {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String,Object>> dataList = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(json_result);
//            LogTools.i(TAG, "====jsonObject" + jsonObject.toString());
            int error_code = jsonObject.getInt("error_code");
//            jobNum = jsonObject.getString("totals");
            if (error_code != 0) {
                return null;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("jobs_list");
//            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOb = (JSONObject) jsonArray.get(i);
                HashMap<String,Object>  hs = new HashMap<String, Object>();
                hs.put("job_name", jsonOb.getString("job_name"));
                hs.put("enterprise_name", jsonOb.getString("enterprise_name"));
                hs.put("issue_date", jsonOb.getString("issue_date"));
                hs.put("workplace", jsonOb.getString("workplace"));
                hs.put("posterimg", jsonOb.getString("posterimg"));
                hs.put("job_id", jsonOb.getString("job_id"));
                hs.put("enterprise_id", jsonOb.getString("enterprise_id"));
                hs.put("nautica", jsonOb.getString("nautica"));
                hs.put("study", jsonOb.getString("study"));
                hs.put("is_expire", jsonOb.getString("is_expire"));
                hs.put("is_apply", jsonOb.getString("is_apply"));//是否投递过，0没有，1有
                hs.put("is_favourite", jsonOb.getString("is_favourite"));//是否投递过，0没有，1有
                dataList.add(hs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
