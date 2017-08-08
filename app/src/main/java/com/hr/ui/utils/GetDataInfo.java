package com.hr.ui.utils;

import android.content.Context;

import com.hr.ui.config.Constants;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.HashMap;

/**
 * Created by wdr on 2017/8/8.
 */

public class GetDataInfo {
    /**
     * 初始化请求参数
     *
     * @param
     * @return
     */
    public static HashMap<String, String> getData(int ad_type, Context context) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "mobilead.list");
        params.put("a_id", "");
        params.put("c_id", "");
        if (ad_type == 1) {// 名企推广
            params.put("c_type", "4");
        } else if (ad_type == 2) {// 炫公司
            params.put("c_type", "7");
        } else if (ad_type == 3) {// 专题活动
            params.put("c_type", "5");
        }
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
        int industry_id = sUtils.getIntValue(Constants.INDUSTRY, 11);
        params.put("industry", "" + industry_id);
        params.put("page", "");
        params.put("page_nums", "20");
        return params;
    }
}
