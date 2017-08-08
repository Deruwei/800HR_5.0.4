package com.hr.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hr.ui.HrApplication;
import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.FindFragment;
import com.hr.ui.fragment.FindjobFragment;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.model.Industry;
import com.hr.ui.model.ResumeList;
import com.hr.ui.receiver.NetBroadcastReceiver;
import com.hr.ui.utils.AndroidMarketEvaluate;
import com.hr.ui.utils.GetBaiduLocation;
import com.hr.ui.utils.MyLocationListenner;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.PermissionCheck;
import com.hr.ui.utils.PermissionHelper;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncLogin;
import com.hr.ui.utils.netutils.AsyncNewAutoLoginPhone;
import com.hr.ui.utils.netutils.AsyncNewLoginPhone;
import com.hr.ui.utils.netutils.AsyncPhoneStates;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.IsBind;
import com.hr.ui.view.custom.BeautifulDialog;
import com.networkbench.agent.impl.NBSAppAgent;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 首页
 *
 * @author Colin
 */
public class MainActivity extends BaseFragmentActivity implements View.OnClickListener, View.OnTouchListener {
    private BeautifulDialog.Builder builderRef;
    public static MainActivity instanceMain = null;
    /**
     * TAG
     */
    private static final String TAG = "MainActivity";
    /**
     * 上下文环境
     */
    private Context mContext = MainActivity.this;
    /**
     * 第一次点击时间
     */
    private long firstClickTime;
    /**
     * 是否需要刷新个人信息
     */
    public static boolean isLoad = true;
    public static boolean newAppResume = false;
    private String listResumeJsonString;
    public String resumeId;
    public static ArrayList<ResumeList> listResume = null;
    public ArrayList<ResumeList> listResumeIsApp = null;
    public boolean isHaveAppResume;
    public String resumeType = null;// 简历类型
    private boolean isHaveResume = false;
    /**
     * 控件名
     */
    private FrameLayout framLayout_main_content;
    private LinearLayout linearlayout_main_tab;
    private LinearLayout linear_main_findjob;
    private ImageView iv_main_findjob;
    private TextView tv_main_findjob;
    private LinearLayout linear_main_find, linear_main_recommendjob;
    private ImageView iv_main_find;
    private TextView tv_main_find;
    public DrawerLayout drawer_layout;
    private LinearLayout linear_main_me;
    private ImageView iv_main_me, iv_main_recommendjob;
    private TextView tv_main_me, tv_main_recommendjob;
    private RelativeLayout main_layout;
    private RelativeLayout right_layout;
    private String thirdCode, thirdUid, thirdIndustry, PhoneName, phonePsw, userName, psw;
    private int Industry;
    private static final int BAIDU_READ_PHONE_STATE =100;
    private GetBaiduLocation baiduLocation;
    /**
     * Fragment管理器
     */
    private FragmentManager fragmentManager;
    /**
     * 声明Fragment对象
     */
    private FindFragment findFragment;
    private FindjobFragment findjobFragment;
    public MyResumeFragment meFragment;
    private SharedPreferencesUtils sUtils;
    private RecommendJobFragment recommendJobFragment;
    private PermissionHelper helper;
//    private boolean isDrawer = false;
    private static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE} ;
   // 定义一个请求码

    private static final int REQUEST_CONTACTS = 1000;
    /**
     * 检测网路状态
     */
    NetBroadcastReceiver mReceiver = new NetBroadcastReceiver();
    IntentFilter mFilter = new IntentFilter();
    /**
     * 接收信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
            }
        }
    };
//    private LocationClient locationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        baiduLocation=new GetBaiduLocation(this);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
            baiduLocation.loadLocation();
        }
        /**
         * 听云
         */
//        NBSAppAgent.setLicenseKey("018b83f2a7c7413abc8d6225c7ea3573").withLocationServiceEnabled(true).start(mContext);
        instanceMain = MainActivity.this;
        isLogin();
        initView();
        initData();


    }
    @Override
    public void onResume() {
        super.onResume();
        whatState();
    }
    public void whatState() {
        if (MyUtils.isLogin) {
            if (newAppResume) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
//                createNewResume();
            } else {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动
            }
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动
        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //关闭手势滑动
        }
    }
    /**
     * 先判断是否登录
     * <p/>
     * 没有登陆进入行业选择界面
     * 每次启动欢迎页刷新 isGoIndustry  在application中添加 退出整个程序没有被杀死
     */
    private void isLogin() {
        sUtils = new SharedPreferencesUtils(mContext);
        thirdCode = sUtils.getStringValue("autoLoginThird_code", "");
        thirdIndustry = sUtils.getStringValue("autoLoginIndustry", "");
        thirdUid = sUtils.getStringValue("autoLoginThird_uid", "");
        userName = sUtils.getStringValue(Constants.USERNAME, "");
        psw = sUtils.getStringValue(Constants.PASSWORD, "");
        PhoneName = sUtils.getStringValue(Constants.USERPHONE, "");
        Industry = sUtils.getIntValue(Constants.INDUSTRY, 11);
        /*
         * 是否已经登录
         */
        if (!MyUtils.isLogin) {
            /*
             * 是否自动登录
             */
            if (sUtils.getBooleanValue(Constants.AUTO_LOGIN, false)) {
                if (sUtils.getStringValue("autoLoginThired", "0").equals("1")) {
                    IsBind isBindSina = new IsBind(thirdCode, thirdUid, MainActivity.this, thirdIndustry, "");
                    isBindSina.execute();
//                    Toast.makeText(mContext,"     isBindSina.execute();",Toast.LENGTH_SHORT).show();
                } else if (sUtils.getStringValue("autoLoginThired", "0").equals("2")) {
//                    Toast.makeText(mContext,"      asyncLogin.execute;",Toast.LENGTH_SHORT).show();
                    if (!"".equals(PhoneName) && !"".equals(psw)) {
                        AsyncNewAutoLoginPhone asyncLogin = new AsyncNewAutoLoginPhone(mContext, handler);
                        asyncLogin.execute(PhoneName, psw, Industry + "");
                    }
                } else {
                    if (!"".equals(userName) && !"".equals(psw)) {
                        AsyncLogin asyncLogin = new AsyncLogin(mContext, handler);
                        asyncLogin.execute(userName, psw, Industry + "");
                    }
                }
                MyUtils.isLogin=true;
            } else if (sUtils.getBooleanValue(Constants.IS_CHOOSEINDUSTRY, false)) {
                goActivity(ChooseIndustriesActivity.class);
                MyUtils.isLogin = false;
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

//    /**
//     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
//     */
//    private class LocationListenner implements BDLocationListener {
//        public void onReceiveLocation(BDLocation location) {
//            if (location != null) {
//                if (location.getCity() != null && !location.getCity().equals("")) {
//                    MyUtils.currentCityZh = location.getCity();
//                    locationClient.stop();
//                } else {
//                    MyUtils.currentCityZh = "北京";
////                    Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
//                }
//            } else {
////                Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**
     * 初始化控件
     */
    private void initView() {
        framLayout_main_content = (FrameLayout) findViewById(R.id.framLayout_main_content);
        linearlayout_main_tab = (LinearLayout) findViewById(R.id.linearlayout_main_tab);
        linear_main_findjob = (LinearLayout) findViewById(R.id.linear_main_findjob);
        linear_main_recommendjob = (LinearLayout) findViewById(R.id.linear_main_recommendjob);
        iv_main_findjob = (ImageView) findViewById(R.id.iv_main_findjob);
        tv_main_findjob = (TextView) findViewById(R.id.tv_main_findjob);
        linear_main_find = (LinearLayout) findViewById(R.id.linear_main_find);
        iv_main_find = (ImageView) findViewById(R.id.iv_main_find);
        tv_main_find = (TextView) findViewById(R.id.tv_main_find);
        linear_main_me = (LinearLayout) findViewById(R.id.linear_main_me);
        iv_main_me = (ImageView) findViewById(R.id.iv_main_me);
        tv_main_me = (TextView) findViewById(R.id.tv_main_me);
        iv_main_recommendjob = (ImageView) findViewById(R.id.iv_main_recommendjob);
        tv_main_recommendjob = (TextView) findViewById(R.id.tv_main_recommendjob);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
        right_layout = (RelativeLayout) findViewById(R.id.right_layout);
        /*
        设置监听
         */
        linear_main_find.setOnClickListener(this);
        linear_main_findjob.setOnClickListener(this);
        linear_main_me.setOnClickListener(this);
        linear_main_recommendjob.setOnClickListener(this);

        drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //设置主布局随菜单滑动而滑动
                int drawerViewWidth = drawerView.getWidth();
                main_layout.setTranslationX(-drawerViewWidth * slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                MeFragment.meFragment.refreshData();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!MyResumeFragment.isRefresh) {
                    MyResumeFragment.isRefresh = true;
                    MyResumeFragment.myResumeFragment.getData();
                } else {

                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * activity间的跳转
     */
    private void goActivity(Class c) {
        Intent intent = new Intent(mContext, c);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * 退出整个应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (MyUtils.isLogin) {
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClickTime > 1000) {
                    firstClickTime = secondClick;
                    Toast.makeText(mContext, "再次点击确认退出应用", Toast.LENGTH_SHORT).show();

                    return true;
                } else {
                    if (sUtils.getIntValue(Constants.OPPEN_NUM, 0) <= 3) {
                        evaluateDialog();
                    } else {
                        finish();
                        System.exit(0);//正常退出App
                    }
                    return true;
                }
            } else {
                Intent intent = new Intent(mContext, ChooseIndustriesActivity.class);
                startActivity(intent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 五星好评
     */
    private void evaluateDialog() {
        if (AndroidMarketEvaluate.hasAnyMarketInstalled(mContext)) {
            builderRef = new BeautifulDialog.Builder(mContext);
            builderRef.setMessage("给个五星好评吧，我们会更努力!");
            builderRef.setTitle("提示");
            builderRef.setPositiveButton("马上评论", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intentMark = new Intent(Intent.ACTION_VIEW, uri);
                    intentMark.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMark);
                    sUtils.setIntValue(Constants.OPPEN_NUM, 4);
                    dialog.dismiss();
                }
            });
            builderRef.setNegativeButton("以后评论", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);//正常退出App
                    dialog.dismiss();
                }
            });
            builderRef.create().show();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    /**
     * 点击监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_main_recommendjob:
                setTabSelect(0);
                break;
            case R.id.linear_main_findjob:
                setTabSelect(1);
                break;
            case R.id.linear_main_find:
                setTabSelect(2);
                break;
            case R.id.linear_main_me:
                if (MyUtils.ableInternet) {
                    if (MyUtils.isLogin) {
                        if (newAppResume) {
                            createNewResume();
                        } else {
                            setTabSelect(3);
                        }
                    } else {
                        Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(mContext, NewLoginActivity.class);
//                        loginIntent.putExtra("goType","1");
                        startActivity(loginIntent);
                    }
                } else {
                    Toast.makeText(mContext, "无网络,请稍候重试", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void openD() {
        if (MyUtils.ableInternet) {
            if (MyUtils.isLogin) {
                drawer_layout.openDrawer(Gravity.RIGHT);
            } else {
                Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(mContext, NewLoginActivity.class);
                startActivity(loginIntent);
            }
        } else {
            Toast.makeText(mContext, "无网络,请稍候重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void closeD() {
        drawer_layout.closeDrawers();
    }

    /**
     * 根据传入的index选择Tab页
     *
     * @param index
     */
    public void setTabSelect(int index) {
        resetButton();
        //开启新事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                iv_main_recommendjob.setImageResource(R.mipmap.jian2);
                tv_main_recommendjob.setTextColor(getResources().getColor(R.color.new_main));
                if (recommendJobFragment == null) {
                    recommendJobFragment = new RecommendJobFragment();
                    transaction.add(R.id.framLayout_main_content, recommendJobFragment);
                } else {
                    transaction.show(recommendJobFragment);
                }
                break;
            case 1:
                iv_main_findjob.setImageResource(R.mipmap.zhaogongzuo2);
                tv_main_findjob.setTextColor(getResources().getColor(R.color.new_main));
                if (findjobFragment == null) {
                    findjobFragment = new FindjobFragment();
                    transaction.add(R.id.framLayout_main_content, findjobFragment);
                } else {
                    transaction.show(findjobFragment);
                }
                break;
            case 2:
                iv_main_find.setImageResource(R.mipmap.faxian2);
                tv_main_find.setTextColor(getResources().getColor(R.color.new_main));
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.framLayout_main_content, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 3:
                iv_main_me.setImageResource(R.mipmap.jianli2);
                tv_main_me.setTextColor(getResources().getColor(R.color.new_main));
                if (meFragment == null) {
                    meFragment = new MyResumeFragment();
                    transaction.add(R.id.framLayout_main_content, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 隐藏所有Fragment
     */
    @SuppressLint("NewApi")
    private void hideFragment(FragmentTransaction transaction) {
        if (recommendJobFragment != null) {
            transaction.hide(recommendJobFragment);
        }
        if (findjobFragment != null) {
            transaction.hide(findjobFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }

        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }

    /**
     * 重置按钮状态
     */
    private void resetButton() {
        iv_main_find.setImageResource(R.mipmap.btn_faxian1);
        iv_main_findjob.setImageResource(R.mipmap.btn_zhaogongzuo1);
        iv_main_me.setImageResource(R.mipmap.jianli);
        iv_main_recommendjob.setImageResource(R.mipmap.btn_recommend1);
        tv_main_recommendjob.setTextColor(Color.parseColor("#333333"));
        tv_main_find.setTextColor(Color.parseColor("#333333"));
        tv_main_findjob.setTextColor(Color.parseColor("#333333"));
        tv_main_me.setTextColor(Color.parseColor("#333333"));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sUtils.setBooleanValue(Constants.IS_CHOOSEINDUSTRY, true);
        fragmentManager = getSupportFragmentManager();
        setTabSelect(0);
    }
    /**
     * 判断获取的权限的返回码
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    baiduLocation.loadLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 动态获取百度地图的定位权限
     *
     */
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
        }else{
            baiduLocation.loadLocation();
        }
    }

    /**
     * 存储个人信息的Map
     */
    private HashMap<String, String> personInfoMap;
    private Handler handlerPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Intent intentPhoneState = new Intent(MainActivity.this, VerifyPhoneNumStateActivity.class);
                    startActivity(intentPhoneState);
                    break;
                case 2:
                    break;
                default:
                    Toast.makeText(MainActivity.this, "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 加载刷新个人信息
     */
    public void refreshBaseInfo() {
        if (MyUtils.isLogin) {
            if (isLoad) {
                if (newAppResume) {
                    newAppResume = false;
                    isLoad = false;
                    execute();
                } else {
                    execute();
                    isLoad = false;
                }
            } else {
                execute();
            }
        }
    }

    /**
     * 访问网络 获取个人信息
     */
    public void execute() {
        try {
            //Log.i("=========加载数据", "1");
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.resumelist");
            NetService service = new NetService(mContext, handlerRefresh);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 装载简历的数据
     */
    private Handler handlerRefresh = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                final String jsonString = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    int error_code = jsonObject.getInt("error_code");
                    if (error_code != 0) {
                        Message message = new Message();
                        message.what = -1;
                        message.arg1 = error_code;
                        handler.sendMessage(message);
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("base_info");

                    if (!((jsonArray.toString().replace("[", "")).replace("]", "").replace(",", "")).equals("")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("resume_language").equals("zh")) {
                                personInfoMap = new HashMap<>();
                                personInfoMap.put("user_id", jsonObject1.getString("user_id"));
                                personInfoMap.put("sex", jsonObject1.getString("sex"));
                                personInfoMap.put("name", jsonObject1.getString("name"));
                                personInfoMap.put("ydphone", jsonObject1.getString("ydphone"));
                                personInfoMap.put("pic_filekey", jsonObject1.getString("pic_filekey"));
                            }
                        }
                    } else {
                        personInfoMap = new HashMap<>();
                        personInfoMap.put("user_id", MyUtils.userID);
                        personInfoMap.put("sex", "");
                        personInfoMap.put("name", "");
                        personInfoMap.put("ydphone", "");
                        personInfoMap.put("pic_filekey", "");
                    }
                    JSONArray jsonArrayResumeList = jsonObject.getJSONArray("resume_list");
                    listResumeJsonString = jsonArrayResumeList.toString();
                    if (jsonArrayResumeList.toString().equals("[]")) {
//                        iv_me_myresume.setImageResource(R.mipmap.nohaveresume);
                        isHaveResume = false;
                    } else {
//                        linear_resume_scan.setVisibility(View.VISIBLE);
                        isHaveResume = true;
//                        iv_me_myresume.setImageResource(R.mipmap.btn_1);
                    }
                    for (int i = 0; i < jsonArrayResumeList.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayResumeList.getJSONObject(i);
                        personInfoMap.put("resume_id", jsonObject1.getString("resume_id"));
                    }
                    sUtils.setStringValue("is_app_resumeid" + MyUtils.userID, "000");
                    // 网络获取的有app简历
                    try {
                        JSONArray jsonArray2 = new JSONArray(listResumeJsonString);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObjectIsApp = jsonArray2.getJSONObject(i);
                            if (jsonObjectIsApp.getString("important").equals("1")) {
                                String resumeId = jsonObjectIsApp.getString("resume_id");
                                resumeType = jsonObjectIsApp.getString("resume_type");
                                sUtils.setStringValue("is_app_resumeid" + MyUtils.userID, resumeId);
                                /**
                                 * 保存的是简历ID
                                 */
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 先判断本地有没有app简历
                     */
                    listResumeIsApp = new ArrayList<>();
                    try {
                        JSONArray jsonArray3 = new JSONArray(listResumeJsonString);
                        for (int i = 0; i < jsonArray3.length(); i++) {
                            JSONObject jsonObject1 = jsonArray3.getJSONObject(i);
                            ResumeList resumeList = new ResumeList();
                            resumeId = jsonObject1.getString("resume_id");
                            resumeType = jsonObject1.getString("resume_type");
                            resumeList.setResume_id(resumeId);
                            resumeList.setResume_type(resumeType);
                            resumeList.setAdd_time(jsonObject1.getString("uptime"));//uptime刷新时间modify_time修改时间
                            resumeList.setFill_scale(jsonObject1.getString("fill_scale"));
                            resumeList.setTitle(jsonObject1.getString("title"));
                            listResumeIsApp.add(resumeList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (listResumeIsApp.size() == 0) {
                        newAppResume = true;
                        isLoad = true;
                        createNewResume();
//            finish();
                    }
                    whatState();
                    // 网络获取的有app简历
                    try {
                        JSONArray jsonArray2 = new JSONArray(listResumeJsonString);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                            if (jsonObject2.get("important").equals("1")) {
                                resumeId = jsonObject2.getString("resume_id");
                                resumeType = jsonObject2.getString("resume_type");
                                sUtils.setStringValue("is_app_resumeid" + personInfoMap.get("user_id"), resumeId);
                                /**
                                 * 保存的是简历ID
                                 */
                                isHaveAppResume = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 跳转到新用户创建建立页面
     */
    public void createNewResume() {
        if (MyUtils.ableInternet) {
            if (!isHaveResume) {
                Intent intentResume2 = new Intent(mContext, CreateResumePersonInfoActivity.class);
                intentResume2.putExtra("resumeId", "-1");
                intentResume2.putExtra("resumeLanguage", "zh");
                intentResume2.putExtra("isCHS", true);
                startActivity(intentResume2);
                finish();
            }
        } else {
            Toast.makeText(mContext, "无网络,请稍候重试", Toast.LENGTH_SHORT).show();
        }
    }

}