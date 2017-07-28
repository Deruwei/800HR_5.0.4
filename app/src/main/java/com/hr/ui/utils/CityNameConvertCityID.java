package com.hr.ui.utils;

import android.content.Context;

import com.hr.ui.config.Constants;
import com.hr.ui.utils.netutils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：Colin
 * 日期：2015/12/22 17:20
 * 邮箱：bestxt@qq.com
 */
public class CityNameConvertCityID {
    private static final String TAG = "CityNameConvertCityID";

    public static String convertCityID(Context context, String cityName) {
//        Toast.makeText(context, cityName.toString(), Toast.LENGTH_SHORT).show();
        if (cityName != null && !(cityName.equals(""))) {
            InputStream in = null;
            JSONArray cityJSONArray = null;
            try {
//                Log.i(TAG, "======= 下面的都没运行 ");
                in = context.getAssets().open("city_zh.json");
//                Log.i(TAG, "======= in " + in.toString());
                cityJSONArray = new JSONArray(NetUtils.readAsString(in, Constants.ENCODE));
                ArrayList<HashMap<String, String>> data = getCityArray(cityJSONArray, "0");
//                Log.i(TAG, "=======data " + data.toString());
//                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show();
                for (HashMap<String, String> map : data) {
                    if ((map.get("value").trim().substring(0, 2)).equals(cityName.trim().substring(0, 2))) {
                        String cityID = map.get("key");
                        return cityID;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }


    private static ArrayList<HashMap<String, String>> getArrayList(
            JSONArray jsonArray)
            throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        int len = jsonArray.length();
        JSONObject jsonObject = null;

        for (int i = 0; i < len; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            String key = (String) jsonObject.keys().next();

            HashMap<String, String> item = new HashMap<String, String>();
            item.put("key", key);
            item.put("value", jsonObject.getString(key));
            list.add(item);
        }
        return list;
    }

    /**
     * 获得 city 数组信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> getCityArray(
            JSONArray jsonArray, String id) throws Exception {
        return getArrayList(jsonArray);
    }

    /**
     * 获得 city 数组信息
     *
     * @return
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> getCityArrayGo(Context context) throws Exception {
        InputStream in = null;
        JSONArray cityJSONArray = null;
        in = context.getAssets().open("city_zh.json");
        cityJSONArray = new JSONArray(NetUtils.readAsString(in, Constants.ENCODE));
        return getArrayList(cityJSONArray);
    }
}
