package com.hr.ui.utils.datautils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hr.ui.config.Constants;

/**
 * 作者：Colin
 * 日期：2015/12/4 10:54
 * 邮箱：bestxt@qq.com
 * SharedPreference工具类
 */
public class SharedPreferencesUtils {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    /**
     * @param context
     * @param name
     */
    public SharedPreferencesUtils(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(name,
                context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * the name of sharedpreferenced is sp800hr.
     *
     * @param context
     */
    public SharedPreferencesUtils(Context context) {
        if (context!=null){
            sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public String getStringValue(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }
    /**
     * @param key
     * @param defValue
     * @return
     */
    public int getIntValue(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public float getFloatValue(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBooleanValue(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }
    /**
     * @param key
     * @param value
     */
    public void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * @param key
     * @param value
     */
    public void setFloatValue(String key, Float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * @param key
     * @param value
     */
    public void setBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * delete all
     */
    public void deleteAll() {
        editor.clear();
        editor.commit();
    }

    /**
     * delete the information of current user
     */
    public void deleteUserInfo() {
        editor.remove(Constants.USERNAME);
        editor.remove(Constants.PASSWORD);
        editor.remove(Constants.AUTO_LOGIN);
        editor.commit();
    }

}
