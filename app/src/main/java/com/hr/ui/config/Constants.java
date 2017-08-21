package com.hr.ui.config;

/**
 * 作者：Colin
 * 日期：2015/12/3 14:23
 * 邮箱：bestxt@qq.com
 * <p/>
 * sp中用到的变量名，各个开放平台的key和secret
 * 配置信息
 */
public class Constants {
    /**
     * 客户端使用的Api版本号
     */
    public static final String API_VER = "2.1.0";
    /**
     * 安装来源
     */
    public static final String DNFROM = "800hr";
    /**
     * 服务器接口访问地址http://172.16.0.78/svrdo.php
     */
    // public static final String SERVER_URL =
    // "http://118.144.89.106/svrdo.php";
    /**
     * 发布版本的真实地址 SERVER_URL，把HRService类中的TEST_SERVER_URL改为SERVER_URL
     */
    public static final String SERVER_URL = "https://api.800hr.com/svrdo.php";//真实
  // public static final String SERVER_URL = "https://api.800hr.com/svrdo_v0.php";//测试
    /**
     * 软件开发的测试地址 TEST_SERVER_URL
     */
    public static final String TEST_SERVER_URL = "https://api.800hr.com/svrdo_v0.php";
//    public static final String TEST_SERVER_URL = "https://api.800hr.com/svrdo.php";
    /**
     * 字符集编码
     */
    public static final String ENCODE = "utf-8";
    /**
     * SharedPreferences 名称（3代表app主版本）
     */
    public static final String PREFS_NAME = "SP800HR3";
    /**
     * SharedPreferences 参数是否自动登录
     */
    public static final String AUTO_LOGIN = "autoLogin1";
    /**
     * SharedPreferences 用户名
     */
    public static final String USERNAME = "username";
    public static final String USERPHONE = "user_phone";
    /**
     * SharedPreferences 密码
     */
    public static final String PASSWORD = "password";
    /**
     * SharedPreferences 用户id
     */
    public static final String USERID = "userid";
    /**
     * SharedPreferences 文件中 job 版本字段名
     */
    public static final String JOB_VER = "jobVer";
    /**
     * SharedPreferences 文件中 job 日期字段名
     */
    public static final String JOB_DATE = "jobDate";
    /**
     * SharedPreferences 文件中 city 版本字段名
     */
    public static final String CITY_VER = "cityVer";
    /**
     * SharedPreferences 文件中 city 日期字段名
     */
    public static final String CITY_DATE = "cityDate";
    /**
     * SharedPreferences 文件中纬线字段名
     */
    public static final String LATITUDE = "latitude";
    /**
     * SharedPreferences 文件中经线字段名
     */
    public static final String LONGITUDE = "longitude";
    /**
     * SharedPreferences 文件中是否打开过引导页面
     */
    public static final String IS_GUIDE = "GUIDE";
    /**
     * SharedPreferences 文件中用户设备id
     */
    public static final String DEVICE_USER_ID = "device_user_id";
    /**
     * SharedPreferences 文件中行业字段名
     */
    public static final String INDUSTRY = "industry";
    /**
     * SharedPreferences baidu_channel_id
     */
    public static final String BAIDU_CHANNEL_ID = "baidu_chanel_id";
    /**
     * SharedPreferences baidu_push_userId
     */
    public static final String BAIDU_PUSH_USERID = "baidu_push_userid";
    /**
     * 图片下载主地址
     */
    public static final String IMAGE_ROOTPATH = "http://file.800hr.com/";
    /**
     * 企业logo下载地址
     */
    public static final String LOGO_ROOTPATH = "http://img.800hr.com";

    /**
     * sin
     */
    public static final String CONSUMER_SECRET_SINA = "3a6e4dee55dbf34ce86178fd237e8f78";
    public static final String CONSUMER_KEY_SINA = "1135940687";
    public static final String CONSUMER_REDIRECTURL_SINA = "https://api.t.sina.com.cn/"; // url回调地址
    /**
     * renren
     */
    public static final String CONSUMER_SECRET_RENREN = "45a23259202b41a38d8bba29f9357d70";
    public static final String CONSUMER_KEY_RENREN = "f4b616f85f954c849d7bcaf5a5edc43c";
    /**
     * 行业名称对照 id
     */
    public static final int INDUSTRY_BUILD_ID = 11;// 建筑
    public static final int INDUSTRY_HEALT_ID = 14;// 医药
    public static final int INDUSTRY_CHEN_ID = 29;// 化工
    public static final int INDUSTRY_MEC_ID = 22;// 机械
    public static final int INDUSTRY_CLOTH_ID = 26;// 服装
    public static final int INDUSTRY_CTV_ID = 13;// 传媒
    public static final int INDUSTRY_CNIT_ID = 23;// IT
    public static final int INDUSTRY_ELE_ID = 19;// 电子
    public static final int INDUSTRY_CNEDU_ID = 15;// 教培
    public static final int INDUSTRY_BANK_ID = 12;// 金融
    public static final int INDUSTRY_LOGISTICS_ID = 16;// 物流
    public static final int INDUSTRY_EP_ID = 20;// 电力
    public static final int INDUSTRY_COM_ID = 21;// 通信
    public static final int INDUSTRY_TOUR_ID = 30;// 旅游
    public static final int INDUSTRY_HOTEL_ID = 40;// 酒店餐饮
    /**
     * logintag
     */
    public static final int PERSONCENTER_LOGIN = 100;
    public static final int RESUMECENTER_LOGIN = 200;
    public static final int CHOOESINDUSTRY_LOGIN = 300;
    public static final int FINDJOB_LOGIN = 400;
    public static final int PUSH_LOGIN = 500;
    /**
     * 找工作界面的广播
     */
    public static final String SEARCHJOB_UPDATEUI = "com.hr.ui.searchjob_updateui";
    /**
     * 判断是否选择行业
     */
    public static final String IS_CHOOSEINDUSTRY = "isChooseIndustry";

    /**
     * 打开次数
     */
    public static final String OPPEN_NUM = "oppen_num";
    /**
     * 是否已经登录
     */
    public static final String IS_LOGIN = "islogin";
    /**
     * 城市名
     */
    public static final String CITY_NAME = "cityName";
    public static final String LOCATION_CITY = "location_city";
    public static final String APPLY_NO_RESUME = "请先到\"我->简历\"设置默认简历";
    /**
     * 是否是新版本第一次
     */
    public static final String IS_FIRST = "isFirst52";
    public static final String IS_FIRST_GUIDE = "guideFirst52";
    /**
     * 是否已记录推荐
     */
    public static final String IS_HAVE_RECOMMEND = "isHaveRecommend";
    public static final String RECOMMEND_FUNCID = "recommendFuncid";
    public static final String AUTOCODENUM = "autocodeNum";
    /**
     * 验证码次数
     */
    public static final String RECOMMEND_AREAID = "autocodeNum";
    /**
     * 第一次预览
     */
    public static final String FIRSTPREVIEW = "firstpreview";
}

