package com.hr.ui.utils;

import com.hr.ui.HrApplication;
import com.hr.ui.utils.datautils.FileUtil;

/**
 * 数据常量 请求数据加密 session industryid loginstate
 */
public class MyUtils extends HrApplication {
    //13141083433
    public static String session_key;
    public static final String pre_secret_key = "2cb7G62b2drGdJobcQJ1O8813aV7fds7";
    public static String init_secret_key = "2cb7G62b2drGdJobcQJ1O8813aV7fds7.800hr.com.800hr.";
    public static String svr_api_ver;
    public static boolean isLogin = false; // 记录用户是否登录
    public static boolean isLogouted = false;// 是否已经注销
    public static boolean isFirst = false;// 是否首次运行
    public static boolean isThiredLogin = false;// 是否是第三方登录
    public static String username = "";// 用户名
    public static String userphone = "";// 用户名
    public static String userID = "";// 用户id
    public static String userJob = "";// 职位

    public static String industryId;// 行业
    public static double latitude;// 当前经纬度
    public static double longitude;
    public static boolean firstIn;
    public static String currentCityZh = "";// 当前城市zh
    public static String selectCityZh = "";
    public static String selectCityId = "";
    public static String currentCityEn = "";// 当前城市en
    public static String currentCityId = "";// 当前城市id
    public static boolean ableInternet = true;// 网络状态
    public static boolean isZH = true;//是否为中文简历
    public static String network_type;
    public static boolean firstResume = true;
    public static String ALIAS;// 设备唯一别名
    // 职位详情页
    public static String posterPathStringFindjob;// 找工作-职位海报路径
    // public static String posterPathStringPerson;// 个人中心-职位海报路径
    public static int clickIndexFindjob;// 找工作-单击的职位索引值
    public static int clickIndexPerson;// 个人中心-单击的职位索引值
    // 图片缓存位置
    // public static String IMAGE_TEMP_PATH = Environment
    // .getExternalStorageDirectory().getAbsolutePath()
    // + File.separator
    // + ".cache800hr";
    public static String IMAGE_TEMP_PATH = FileUtil.getRootDir() + "/800HR/Cache";
    public static String IMAGE_TEMP_NAME = "temp.jpg";
    public static String baidu_channel_id = null;// 百度推送手机唯一标识ID
    public static String baidu_push_userId = null;// 百度推送用户ID

    // 是否使用网络城市和职位数组
    public static final boolean USE_ONLINE_ARRAY = true;

    // 简历中心当前显示的简历下标
    public static int resume_index = 0;
    //保存刚注册时邮箱
    public static String emailAddress = "";

    public static void clear() {
        isLogin = false; // 记录用户是否登录
        isLogouted = false;// 是否已经注销
        isFirst = false;// 是否首次运行
        isThiredLogin = false;// 是否是第三方登录
        username = "";// 用户名
        userphone = "";// 用户名
        userID = "";// 用户id
        userJob = "";// 职位
        industryId = "";// 行业
        currentCityZh = "";// 当前城市zh
        currentCityEn = "";// 当前城市en
        currentCityId = "";// 当前城市id
        ableInternet = true;// 网络状态
        isZH = true;//是否为中文简历
        network_type = "";
        firstResume = true;
    }
}
