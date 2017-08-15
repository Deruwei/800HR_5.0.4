package com.hr.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.networkbench.agent.impl.NBSAppAgent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 作者：Colin
 * 日期：2015/12/2 10:44
 * 邮箱：bestxt@qq.com
 */
public class HrApplication extends MobApplication {

    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    private static Context mContext;
    public static boolean isPhoneState = true;
    private HrApplication hrApplication = null;
    public static String resumeTime = "";// 更新时间
    public static String userJob = "";// 您的职位
    private static HrApplication sInstance;
    private RequestQueue mRequestQueue;

    /**
     * sutils
     */
    SharedPreferencesUtils sUtils;
    // 用于存放倒计时时间
    public static Map<String, Long> map;

    public static Map<String, Long> getMap() {
        return map;
    }

    public static void setMap(Map<String, Long> map) {
        HrApplication.map = map;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        checkNetState(getApplicationContext());
        mContext = getApplicationContext();
        /*NBSAppAgent.setLicenseKey("8a97e06a76944ee3886dafe60f20a809").withLocationServiceEnabled(true).start(mContext);*/
        SpeechUtility.createUtility(HrApplication.this, "appid=" + getString(R.string.xunfei_app_id));
        initUIL();
    }
    public static synchronized HrApplication getInstance() {
        return sInstance;
    }
//    public static HrApplication getInstance() {
//        return getInstance();
//    }

    public static Context getContextObject() {
        return mContext;
    }

    public static void cleaar() {
        isPhoneState = true;
        resumeTime = "";// 更新时间
        userJob = "";// 您的职位
    }
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * }
     * /**
     * 检查手机网络状态是否发生变化
     *
     * @param context
     */

    private void checkNetState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
//            Toast.makeText(context, "手机联网成功！", Toast.LENGTH_LONG).show();
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//                Toast.makeText(context, "手机连接移动网络成功！", Toast.LENGTH_LONG).show();
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
//                Toast.makeText(context, "手机连接WIFI网络成功！", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(context, "手机连接未知网络成功！", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "无网络，请重新检查网络连接！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化UIL
     */
    private void initUIL() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}