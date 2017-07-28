package com.hr.ui.utils.tools;

import android.util.Log;

/**
 * 作者：Colin
 * 日期：2016/2/24 11:50
 * 邮箱：bestxt@qq.com
 *
 * log管理工具
 */
public class LogTools {
    /**
     * 设置为6 LOG将全部打印
     */
    public static int LOG_LEVEL = 0;
    public static int ERROR = 1;
    public static int WARN = 2;
    public static int INFO = 3;
    public static int DEBUG = 4;
    public static int VERBOS = 5;


    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN)
            Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG)
            Log.d(tag, msg);
    }
    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOS)
            Log.v(tag, msg);
    }
}