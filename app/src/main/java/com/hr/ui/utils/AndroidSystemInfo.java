package com.hr.ui.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/**
 * 作者：Colin
 * 日期：2015/12/14 10:26
 * 邮箱：bestxt@qq.com
 * <p/>
 * 获取系统相关信息
 */
public class AndroidSystemInfo {
    /**
     * 获取系统版本
     */
    /**
     * 获得版本名称
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            String packName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("版本名称获取异常", e.getMessage());
        }
        return verName;
    }

    /**
     * 获取手机硬件信息 ---BRAND:手机系统定制商; DEVICE:设备参数; MODEL:版本
     *
     * @return 手机硬件信息
     */
    public static String get_model() {
        String model = Build.BRAND + " " + Build.DEVICE + "(" + Build.MODEL
                + ")";
        return model;
    }
}
