package com.hr.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hr.ui.HrApplication;
import com.hr.ui.R;
import com.hr.ui.activity.AppSettingActivity;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.CompanyEmailListActivity;
import com.hr.ui.activity.CreateResumePersonInfoActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.MainSelectCityToKeywordActivity;
import com.hr.ui.activity.MeFragmentSelectCityActivity;
import com.hr.ui.activity.MyCollectionActivity;
import com.hr.ui.activity.MyPositionActivity;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.activity.PayQueryActivity;
import com.hr.ui.activity.PreviewResumeActivity;
import com.hr.ui.activity.RefuseCompanyActivity;
import com.hr.ui.activity.ResumeSendActivity;
import com.hr.ui.activity.SubscriptionActivity;
import com.hr.ui.activity.VerifyPhoneNumStateActivity;
import com.hr.ui.activity.VocationalCounselActivity1;
import com.hr.ui.activity.WhoLookMeActivity;
import com.hr.ui.bean.ProjectExperience;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeList;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.AndroidMarketEvaluate;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeComplete;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeListStringJsonParser;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.datautils.TestSD;
import com.hr.ui.utils.netutils.AsyncLogout;
import com.hr.ui.utils.netutils.AsyncPhoneStates;
import com.hr.ui.utils.netutils.AsyncUpPhoto;
import com.hr.ui.utils.netutils.Async_MyResume_Open;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CircleImageView;
import com.hr.ui.view.custom.MyProgressDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import me.shaohui.advancedluban.Luban;


/**
 * 我 Fragment界面
 */
public class MeFragment extends TakePhotoFragment implements View.OnClickListener {

    private static final String TAG = "MEFragment";
    /**
     * MeFragment View
     */
    private View view;
    /**
     * 各个按钮
     */
    private RelativeLayout rl_me_applyjob, rl_me_collect, rl_me_comemail, rl_me_jobguide, rl_me_lookme, rl_me_resume,
            rl_me_searchsallery, rl_me_subscribe, rl_me_nologin, rl_me_refusecom, rl_me_lookresume;

    private LinearLayout rl_me_islogin;
    //    linear_resume_scan
    private ResumeComplete resumeComplete;// 简历完整度操作类
    private TextView tv_me_name, tv_me_phone, tv_me_back, tv_me_loginout, tv_me_assess, tv_me_job, tv_me_city, tv_me_resumeupdata;
    private ImageView iv_me_myresume;
    //    iv_fragment_guide
    private static CircleImageView iv_me_head;
    /**
     * 注册登录iv
     */
    private ImageView iv_me_login;
    private TextView tv_me_setting;
    /**
     * 存储个人信息的Map
     */
    private HashMap<String, String> personInfoMap;
    /**
     * UIL配置信息
     */
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private String resumeType = null;// 简历类型
    /**
     * 简历信息
     */
    private ResumeTitle resumeTitle;
    private DAO_DBOperator dbOperator;
    private boolean isHaveResume = false;

    public final static int ToMyResume = 100, ToResumeScan = 200;
    //ToResumeScan获取到网络简历信息,跳转到预览  获取到网络简历信息,跳转到MyResume
    private ProjectExperience projectExperience;
    static final int REQUEST_CODE0 = 100;// 拍照execute
    static final int REQUEST_CODE1 = 101;// 截取图片
    static final int REQUEST_CODE2 = 102;// 从相册选取图片
    private SharedPreferencesUtils sUtils;
    /**
     * 是否需要刷新个人信息
     */
    public static boolean isLoad = true;
    public static boolean newAppResume = false;
    //    public static String onlyOne = "1";
    private boolean isPhoneState = false;
    private String listResumeJsonString;
    private TakePhoto takePhoto;
    private RefleshDialogUtils dialogUtils;
    public static MeFragment meFragment;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                dialogUtils.dismissDialog();
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
//                        linear_resume_scan.setVisibility(View.GONE);
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
                    if (listResumeIsApp.size() == 0&&isHaveAppResume==false) {
                        MeFragment.newAppResume = true;
                        MeFragment.isLoad = true;
                    }
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                dialogUtils.dismissDialog();
            }
        }
    };
    private Handler handlerPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Intent intentPhoneState = new Intent(getActivity(), VerifyPhoneNumStateActivity.class);
                    startActivity(intentPhoneState);
                    break;
                case 2:
                    break;
                default:
                    Toast.makeText(getActivity(), "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Handler handlerPhoneState = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    isPhoneState = false;
                    if (personInfoMap.get("sex").equals("1")) {
                        tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nan), null, getResources().getDrawable(R.mipmap.weiyanzheng1), null);
                    } else {
                        tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nv), null, getResources().getDrawable(R.mipmap.weiyanzheng1), null);
                    }
                    break;
                case 2:
                    isPhoneState = true;
                    if (personInfoMap.get("sex").equals("1")) {
                        tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nan), null, getResources().getDrawable(R.mipmap.yiyanzheng), null);
                    } else {
                        tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nv), null, getResources().getDrawable(R.mipmap.yiyanzheng), null);
                    }
                    break;
                default:
                    Toast.makeText(getActivity(), "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOperator = new DAO_DBOperator(getActivity());
        meFragment = MeFragment.this;

    }

    /**
     *
     *
     * TakePhoto自带相册
     */
    private void initTakePhoto1() {
        takePhoto = getTakePhoto();
//        takePhoto.onEnableCompress(null, false);
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(300 >= 400 ? 300 : 400)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, true);
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
//        CompressConfig config;
//        LubanOptions option = new LubanOptions.Builder()
//                .setGear(Luban.CUSTOM_GEAR)
//                .setMaxHeight(400)
//                .setMaxWidth(300)
//                .setMaxSize(102400)
//                .create();
//        config = CompressConfig.ofLuban(option);
//        takePhoto.onEnableCompress(config, true);
    }

    /**
     * 系统自带相册
     */
    private void initTakePhoto2() {
        takePhoto = getTakePhoto();
//        takePhoto.onEnableCompress(null, false);
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)
                .setMaxPixel(300 >= 400 ? 300 : 400)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, true);
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        takePhoto.setTakePhotoOptions(builder.create());
//        CompressConfig config;
//        LubanOptions option = new LubanOptions.Builder()
//                .setGear(Luban.CUSTOM_GEAR)
//                .setMaxHeight(400)
//                .setMaxWidth(300)
//                .setMaxSize(102400)
//                .create();
//        config = CompressConfig.ofLuban(option);
//        takePhoto.onEnableCompress(config, true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refreshData() {
        if (MyUtils.isLogin) {
            if (HrApplication.isPhoneState) {
                HrApplication.isPhoneState = false;
                AsyncPhoneStates asyncPhoneStates = new AsyncPhoneStates(getActivity(), handlerPhone);
                asyncPhoneStates.execute();
            }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        view = inflater.inflate(R.layout.fragment_me, container, false);
        sUtils = new SharedPreferencesUtils(getActivity());
        dialogUtils=new RefleshDialogUtils(getActivity());
        initView();
        initUIL();
        refreshData();
        return view;
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
//        Log.i("======", images.get(images.size() - 1).getCompressPath());
        AsyncUpPhoto asyncUpPhoto = new AsyncUpPhoto(getActivity(), images.get(images.size() - 1).getCompressPath());
        asyncUpPhoto.execute();
    }

    /**
     * 初始化控件
     */
    private void initView() {
//        iv_me_login = (ImageView) view.findViewById(R.id.iv_me_login);
//        iv_fragment_guide = (ImageView) view.findViewById(R.id.iv_fragment_guide);
        rl_me_applyjob = (RelativeLayout) view.findViewById(R.id.rl_me_applyjob);
        rl_me_collect = (RelativeLayout) view.findViewById(R.id.rl_me_collect);
        rl_me_refusecom = (RelativeLayout) view.findViewById(R.id.rl_me_refusecom);
        rl_me_comemail = (RelativeLayout) view.findViewById(R.id.rl_me_comemail);
        rl_me_jobguide = (RelativeLayout) view.findViewById(R.id.rl_me_jobguide);
        rl_me_lookme = (RelativeLayout) view.findViewById(R.id.rl_me_lookme);
        rl_me_resume = (RelativeLayout) view.findViewById(R.id.rl_me_resume);
        rl_me_searchsallery = (RelativeLayout) view.findViewById(R.id.rl_me_searchsallery);
        rl_me_subscribe = (RelativeLayout) view.findViewById(R.id.rl_me_subscribe);
//        rl_me_islogin = (LinearLayout) view.findViewById(R.id.rl_me_islogin);
//        linear_resume_scan = (LinearLayout) view.findViewById(R.id.linear_resume_scan);
//        rl_me_nologin = (RelativeLayout) view.findViewById(R.id.rl_me_nologin);
        tv_me_setting = (TextView) view.findViewById(R.id.tv_me_setting);
//        iv_me_myresume = (ImageView) view.findViewById(R.id.iv_me_myresume);
//        rl_me_lookresume = (RelativeLayout) view.findViewById(R.id.rl_me_lookresume);

        tv_me_name = (TextView) view.findViewById(R.id.tv_me_name);
//        tv_me_phone = (TextView) view.findViewById(R.id.tv_me_phone);
        iv_me_head = (CircleImageView) view.findViewById(R.id.iv_me_head);
        tv_me_back = (TextView) view.findViewById(R.id.tv_me_back);

        tv_me_loginout = (TextView) view.findViewById(R.id.tv_me_loginout);
        tv_me_assess = (TextView) view.findViewById(R.id.tv_me_assess);
        tv_me_job = (TextView) view.findViewById(R.id.tv_me_job);
        tv_me_resumeupdata = (TextView) view.findViewById(R.id.tv_me_resumeupdata);
        tv_me_city = (TextView) view.findViewById(R.id.tv_me_city);

//        iv_fragment_guide.setVisibility(View.GONE);

        tv_me_loginout.setOnClickListener(this);
        tv_me_assess.setOnClickListener(this);
//        iv_me_login.setOnClickListener(this);
        rl_me_applyjob.setOnClickListener(this);
        rl_me_collect.setOnClickListener(this);
        rl_me_comemail.setOnClickListener(this);
        rl_me_jobguide.setOnClickListener(this);
        rl_me_resume.setOnClickListener(this);
        rl_me_lookme.setOnClickListener(this);
        rl_me_searchsallery.setOnClickListener(this);
        rl_me_subscribe.setOnClickListener(this);
        tv_me_setting.setOnClickListener(this);
        iv_me_head.setOnClickListener(this);
        rl_me_refusecom.setOnClickListener(this);
        tv_me_back.setOnClickListener(this);
        tv_me_city.setOnClickListener(this);
//        rl_me_lookresume.setOnClickListener(this);
//        linear_resume_scan.setOnClickListener(this);
//        if (sUtils.getBooleanValue(Constants.IS_FIRST_GUIDE, true)) {
//            iv_fragment_guide.setVisibility(View.VISIBLE);
//        }
//        isgoneView();
    }

    @Override
    public void onClick(View v) {
        if (MyUtils.isLogin) {
            switch (v.getId()) {
                case R.id.rl_me_applyjob:
                    Intent intentApplyJob = new Intent(getActivity(), MyPositionActivity.class);
                    startActivity(intentApplyJob);
                    break;
                case R.id.rl_me_collect:
                    Intent intentCollection = new Intent(getActivity(), MyCollectionActivity.class);
                    startActivity(intentCollection);
                    break;
                case R.id.rl_me_comemail:
                    Intent intentComEmail = new Intent(getActivity(), CompanyEmailListActivity.class);
                    startActivity(intentComEmail);
                    break;
                case R.id.rl_me_jobguide:
                    Intent intentJobGuide = new Intent(getActivity(), VocationalCounselActivity1.class);
                    startActivity(intentJobGuide);
                    break;
                case R.id.rl_me_resume:
//                    goMyResume("0");
                    Intent intentResumeSend = new Intent(getActivity(), ResumeSendActivity.class);
                    intentResumeSend.putExtra("resumeId", resumeId);
                    intentResumeSend.putExtra("resumeLanguage", "zh");
                    startActivity(intentResumeSend);

                    break;
                case R.id.rl_me_lookme:
                    Intent intentLookMe = new Intent(getActivity(), WhoLookMeActivity.class);
                    startActivity(intentLookMe);
                    break;
                case R.id.rl_me_searchsallery:
                    Intent intentSearchSallery = new Intent(getActivity(), PayQueryActivity.class);
                    startActivity(intentSearchSallery);
                    break;
                case R.id.rl_me_subscribe:
                    Intent intentSubscribe = new Intent(getActivity(), SubscriptionActivity.class);
                    startActivity(intentSubscribe);
                    break;
                case R.id.tv_me_setting:
                    Intent intentSet = new Intent(getActivity(), AppSettingActivity.class);
                    startActivity(intentSet);
                    break;
                case R.id.rl_me_refusecom:
                    Intent intentRefuse = new Intent(getActivity(), RefuseCompanyActivity.class);
                    startActivity(intentRefuse);
                    break;
                case R.id.iv_me_head:
                    showPhotoDialog();
                    break;
                case R.id.tv_me_assess:
                    AndroidMarketEvaluate.goMarket(getActivity());
                    break;
                case R.id.tv_me_loginout:
                    logOut();
                    break;

//                case R.id.iv_fragment_guide:
//                    sUtils.setBooleanValue(Constants.IS_FIRST_GUIDE, false);
//                    iv_fragment_guide.setVisibility(View.GONE);
//                    break;
//                case R.id.linear_resume_scan:
//                    if (sUtils.getBooleanValue(MyUtils.userID + Constants.FIRSTPREVIEW, false)) {
//                        startScanResume();
//                    } else {
//                        sUtils.setBooleanValue(MyUtils.userID + Constants.FIRSTPREVIEW, true);
//                        goMyResume("11");
//                    }
//                    break;
                case R.id.tv_me_back:
                    MainActivity.instanceMain.closeD();
                    break;
                case R.id.tv_me_city:
                    Intent intentCity = new Intent(getActivity(), MeFragmentSelectCityActivity.class);
                    intentCity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentCity.putExtra("value", "选择地点");
                    intentCity.putExtra("filter", "place");
                    startActivity(intentCity);
                    break;
            }
        } else {
            switch (v.getId()) {
//                case R.id.iv_me_login:
//                    Intent intent = new Intent(getActivity(), NewLoginActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.tv_me_back:
//                    Intent intentChoose = new Intent(getActivity(), ChooseIndustriesActivity.class);
//                    startActivity(intentChoose);
//                    getActivity().finish();
                    MainActivity.instanceMain.closeD();
                    break;
                case R.id.rl_me_applyjob:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.rl_me_collect:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.rl_me_comemail:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.rl_me_jobguide:
                    Intent intentJobGuide = new Intent(getActivity(), VocationalCounselActivity1.class);
                    startActivity(intentJobGuide);
                    break;
                case R.id.rl_me_resume:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.rl_me_lookme:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.rl_me_searchsallery:
//                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    Intent intentSearchSallery = new Intent(getActivity(), PayQueryActivity.class);
                    startActivity(intentSearchSallery);
                    break;
                case R.id.rl_me_subscribe:
                    Intent intentSubscribe = new Intent(getActivity(), SubscriptionActivity.class);
                    startActivity(intentSubscribe);
                    break;
                case R.id.rl_me_refusecom:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.iv_me_head:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
                case R.id.tv_me_setting:
                    Intent intentSet = new Intent(getActivity(), AppSettingActivity.class);
                    startActivity(intentSet);
                    break;
//                case R.id.iv_fragment_guide:
//                    sUtils.setBooleanValue(Constants.IS_FIRST_GUIDE, false);
//                    iv_fragment_guide.setVisibility(View.GONE);
//                    break;
//                case R.id.rl_me_lookresume:
//                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
//                    goLoginActivity();
//                    break;
                case R.id.tv_me_assess:
                    AndroidMarketEvaluate.goMarket(getActivity());
                    break;
                case R.id.tv_me_loginout:
                    Intent intentChoose = new Intent(getActivity(), ChooseIndustriesActivity.class);
                    startActivity(intentChoose);
                    getActivity().finish();
                    break;
                case R.id.tv_me_city:
                    Toast.makeText(getActivity(), "请登录后尝试", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                    break;
            }
        }
    }

    private String placeId, cityName;

    /**
     * 设置城市ID
     *
     * @param string
     */
    public void setPlaceId(String string) {
        // TODO Auto-generated method stub
        placeId = string;
    }

    /**
     * 设置地点
     *
     * @param value
     */
    public void setPlaceText(String value) {
        // TODO Auto-generated method stub
        tv_me_city.setText(value);
        cityName = value;
    }

    private String resumeId;
    private static ArrayList<ResumeList> listResume = null;
    private ArrayList<ResumeList> listResumeIsApp = null;
    private boolean isHaveAppResume;

    /**
     * 打开预览简历
     */
    private void startScanResume() {
        initIsApp();
    }

    /**
     * 初始化App简历
     */
    private void initIsApp() {
        /**
         * 先判断本地有没有app简历
         */
        listResumeIsApp = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(listResumeJsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
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
            MeFragment.newAppResume = true;
            MeFragment.isLoad = true;
//            finish();
        }
        // 网络获取的有app简历
        try {
            JSONArray jsonArray2 = new JSONArray(listResumeJsonString);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                if (jsonObject.get("important").equals("1")) {
                    resumeId = jsonObject.getString("resume_id");
                    resumeType = jsonObject.getString("resume_type");
                    sUtils.setStringValue("is_app_resumeid" + personInfoMap.get("user_id"), resumeId);
                    /**
                     * 保存的是简历ID
                     */
                    isHaveAppResume = true;
//                    refreshResume();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //网络没有
        if (!isHaveAppResume) {
            Toast.makeText(getActivity(), "请先选择默认简历", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getActivity(), PreviewResumeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("username", MyUtils.username);
            intent.putExtra("resumeId", resumeId);
            intent.putExtra("resumeLanguage", "zh");
            startActivity(intent);
        }
        //本地有
    }

    private BeautifulDialog.Builder beautifulDialog;

    /**
     *
     */
    private void logOut() {

        if (MyUtils.isLogin) {
            beautifulDialog = new BeautifulDialog.Builder(getActivity());
            beautifulDialog.setMessage("转换行业需要重新登录");
//        beautifulDialog.setTitle("提示");
            beautifulDialog.setNegativeButton("朕意已决", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AsyncLogout asyncLogout = new AsyncLogout(getActivity());
                    asyncLogout.execute();
                    // 注销成功后，取消自动登录
                    sUtils.deleteUserInfo();
                    // 发送消息，关闭所有界面，回到行业选择
                    sUtils.setBooleanValue(Constants.IS_CHOOSEINDUSTRY, true);
                    dialog.dismiss();
//                                Intent intent = new Intent(mContext, MainActivity.class);
//                                startActivity(intent);
                }
            });
            beautifulDialog.setPositiveButton("朕要三思", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            beautifulDialog.create().show();
        } else {
            Toast.makeText(getActivity(), "尚未登录", Toast.LENGTH_LONG).show();
        }
    }

    public void goMyResume(String goType) {
        if (MyUtils.ableInternet) {
            if (isHaveResume) {
                Intent intentResume = new Intent(getActivity(), MyResumeActivity.class);
                intentResume.putExtra("listResumeJsonObj", listResumeJsonString);
                intentResume.putExtra("user_id", personInfoMap.get("user_id"));
                intentResume.putExtra("goType", goType);
                startActivity(intentResume);
            } else {
                Intent intentResume2 = new Intent(getActivity(), CreateResumePersonInfoActivity.class);
                intentResume2.putExtra("resumeId", "-1");
                intentResume2.putExtra("resumeLanguage", "zh");
                intentResume2.putExtra("isCHS", true);
                startActivity(intentResume2);
            }
        } else {
            Toast.makeText(getActivity(), "无网络,请稍候重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void goLoginActivity() {
        Intent intentLogin = new Intent(getActivity(), NewLoginActivity.class);
        startActivity(intentLogin);
    }

    /**
     * 初始化UIL
     */
    private void initUIL() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.touxiang_weidenglu)
                .showImageForEmptyUri(R.mipmap.touxiang_weidenglu)
                .showImageOnFail(R.mipmap.touxiang_weidenglu)
                .cacheInMemory(false)
                .cacheOnDisc(false)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
    }

    public void updateUI() {
//        isgoneView();
//        Toast.makeText(getActivity(), "姓名" + personInfoMap.get("name"), Toast.LENGTH_SHORT).show();
        String phoneNum = personInfoMap.get("ydphone");
        if (phoneNum.length() > 11) {
            phoneNum = phoneNum.substring(0, 11);
        }
//        if (phoneNum.equals("")) {
//            tv_me_phone.setVisibility(View.GONE);
//        } else {
//            tv_me_phone.setVisibility(View.VISIBLE);
//            tv_me_phone.setText(phoneNum);

//        }

        if (personInfoMap.get("name").equals("")) {
//            tv_me_name.setVisibility(View.GONE);
            tv_me_name.setText("姓名");
        } else {
            tv_me_name.setVisibility(View.VISIBLE);
            tv_me_name.setText(personInfoMap.get("name"));
//            if (personInfoMap.get("sex").equals("1")) {
//                if (isPhoneState) {
//                    tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nan), null, getResources().getDrawable(R.mipmap.yiyanzheng), null);
//                } else {
//                    tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nan), null, getResources().getDrawable(R.mipmap.yiyanzheng), null);
//                }
//            } else {
//                if (isPhoneState) {
//                    tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nv), null, getResources().getDrawable(R.mipmap.weiyanzheng1), null);
//                } else {
//                    tv_me_name.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.nv), null, getResources().getDrawable(R.mipmap.weiyanzheng1), null);
//                }
//            }
        }
        tv_me_resumeupdata.setText("更新时间：" + (HrApplication.resumeTime + ""));
        if(!"".equals(MyUtils.currentCityZh)&&MyUtils.currentCityZh!=null) {
            setPlaceText(MyUtils.currentCityZh);
            setPlaceId(ResumeInfoIDToString.getCityID(getActivity(), MyUtils.currentCityZh, true));
        }else{
            setPlaceText("北京");
            setPlaceId(ResumeInfoIDToString.getCityID(getActivity(), "北京", true));
        }
        if (HrApplication.userJob == null && HrApplication.userJob.toString().length() == 0) {
            tv_me_job.setText("无职位");
        } else {
            tv_me_job.setText(HrApplication.userJob);
        }
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
        imageLoader.displayImage(Constants.IMAGE_ROOTPATH + personInfoMap.get("pic_filekey"), iv_me_head, options);
    }

    public boolean isGoResume = false;

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(3).setAspectY(4);
        builder.setWithOwnCrop(false);
        return builder.create();
    }

    /*
     * 简历头像选取
     */
    public void showPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setItems(new String[]{"相机", "相册", "取消"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(Environment.getExternalStorageDirectory(), MyUtils.IMAGE_TEMP_PATH + "/" + MyUtils.IMAGE_TEMP_NAME);
                        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                        Uri imageUri = Uri.fromFile(file);
                        switch (which) {
                            case 0:
                                /**
                                 * 从相机获取图片并裁剪
                                 * @param outPutUri 图片裁剪之后保存的路径
                                 * @param options 裁剪配置
                                 */
                                Log.i("======", "/temp/" + System.currentTimeMillis() + ".jpg");
                                initTakePhoto2();
                                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                                break;
                            case 1:
                                /**
                                 * 从相册中获取图片并裁剪
                                 * @param outPutUri 图片裁剪之后保存的路径
                                 * @param options 裁剪配置
                                 */
                                initTakePhoto1();
                                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).create().show();
    }

    /**
     * 访问网络 获取个人信息
     */
    public void execute() {
        try {
            /*Log.i("=========加载数据", "1");*/
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.resumelist");
            dialogUtils.showDialog();
            NetService service = new NetService(getActivity(), handler);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 网络简历列表信息
    public static ArrayList<ResumeBaseInfo> listBaseInfos;
    public static ArrayList<ResumeLanguageLevel> listLanguageLevels;
    public static ArrayList<ResumeList> listResumeLists;
    private int LOADING_RESUMELIST = 0x1000;// 简历对比完成后，开始加载简历列表标识

    /**
     * 从本地 获取简历列表
     */
    public void getResumeList() {
        dbOperator = new DAO_DBOperator(getActivity());
        listResume = new ArrayList<ResumeList>();
        AsyncGetResumeList asyncGetResumeList = new AsyncGetResumeList(getActivity());
        asyncGetResumeList.execute();
    }

    /**
     * 获取简历列表信息
     */
    private class AsyncGetResumeList {
        private Context context;
        private MyProgressDialog dialog;
        private Handler handService = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    final String jsonString = (String) msg.obj;
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                int error_code = jsonObject.getInt("error_code");
                                if (error_code != 0) {
                                    Message message = new Message();
                                    message.what = -1;
                                    message.arg1 = error_code;
                                    handService.sendMessage(message);
                                    return;
                                }
                                // 0.清空数据
                                listResume.clear();
                                // --------加载离线简历--------//
                                loadOffLineResume(dbOperator);

                                // 1.获取线上json信息
                                JSONArray baseinfoJsonArray = jsonObject.getJSONArray("base_info");
                                JSONArray languageinfoJsonArray = jsonObject.getJSONArray("language_list");
                                JSONArray resumeArray = jsonObject.getJSONArray("resume_list");
                                // 2.解析json信息
                                ResumeListStringJsonParser parser = new ResumeListStringJsonParser();
                                listBaseInfos = parser.getBaseInfos(baseinfoJsonArray);
                                listLanguageLevels = parser.getLanguageLevels(languageinfoJsonArray);
                                listResumeLists = parser.getResumeLists(resumeArray);
                                // 3.简历对比
                                DAO_DBOperator dbOperator = new DAO_DBOperator(context);
                                // ----A.
                                boolean hasResumeLocal;// 记录本地是否存在该简历
                                // 遍历中文简历
                                ResumeTitle[] listLocalZh0 = dbOperator.query_ResumeList("zh");
                                for (ResumeTitle resumeTitleLocal : listLocalZh0) {
                                    hasResumeLocal = false;
                                    for (ResumeList resumeItem : listResumeLists) {
                                        if (resumeTitleLocal.getResume_id().equals(resumeItem.getResume_id())) {// 如果线下id=线上id
                                            hasResumeLocal = true;
                                        }
                                    }
                                    if (!hasResumeLocal) {// 如果本地存在，而线上不存在，则删除本地该简历
                                        if (!"-1".equals(resumeTitleLocal.getResume_id())) {// (离线简历除外)
                                            // 删除中文简历
                                            boolean resuleDelZh = dbOperator.Delete_Data(resumeTitleLocal.getResume_id(), "zh");
                                            // 删除英文简历
                                            boolean resuleDelEn = dbOperator.Delete_Data(resumeTitleLocal.getResume_id(), "en");
                                        }
                                    }
                                }
                                // ----B.
                                ResumeTitle[] listLocalZh1 = dbOperator.query_ResumeList("zh");
                                ResumeBaseInfo baseInfoZh1 = dbOperator.query_ResumePersonInfo_Toone("zh");
                                ResumeBaseInfo baseInfoEn1 = dbOperator.query_ResumePersonInfo_Toone("en");

                                // **************************个人信息+语言能力*****************************//

                                // 中文个人信息操作
                                if (baseInfoZh1 == null) {// 中文个人信息不存在
                                    // 写入非语言能力部分
                                    for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                        if ("zh".equals(baseInfo.getResume_language())) {
                                            long resultInsert = dbOperator.Insert_ResumePersonInfo(baseInfo);
                                        }
                                    }
                                    // 写入语言能力
                                    // if (resumeLanguageLevels1.length == 0) {
                                    ArrayList<ResumeLanguageLevel> languageLevels = new ArrayList<ResumeLanguageLevel>();
                                    for (ResumeLanguageLevel resumeLanguageLevel : listLanguageLevels) {
                                        languageLevels.add(resumeLanguageLevel);
                                    }
                                    if (languageLevels.size() > 0) {
                                        long resultInsert = dbOperator.Insert_ResumeLanguageLevel(languageLevels);
                                    }
                                    // }else{
                                    // Log.e("============", "不空啊");
                                    // }
                                } else {// 中文个人信息存在，比价时间戳
//                                    if (baseInfoZh1.getIsUpdate() == 0) {// 若本地未修改过基本信息，则将覆盖本地
                                    // 一、非语言部分
                                    for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                        if ("zh".equals(baseInfo.getResume_language())) {
                                            boolean result = dbOperator.update_ResumePersonInfo(baseInfo);
                                        }
                                    }

                                    // 二、语言部分
                                    ResumeLanguageLevel[] languageLevelsLocal = dbOperator.query_ResumeLanguageLevel();
                                    for (ResumeLanguageLevel resumeLanguageLevel : languageLevelsLocal) {
                                        // 1.遍历删除简历
                                        boolean delResule = dbOperator.Delete_Data("ResumeLanguageLevel", resumeLanguageLevel.getId());
                                    }
                                    // 2.将语言信息写入本地到本地
                                    ArrayList<ResumeLanguageLevel> languageLevels = new ArrayList<ResumeLanguageLevel>();
                                    for (ResumeLanguageLevel resumeLanguageLevel : listLanguageLevels) {
                                        languageLevels.add(resumeLanguageLevel);
                                    }
                                    if (languageLevels.size() > 0) {
                                        long resultInsert = dbOperator.Insert_ResumeLanguageLevel(languageLevels);
                                    }
//                                    }
                                }
                                // 英文个人信息操作
                                if (baseInfoEn1 == null) {// 英文个人信息不存在
                                    // 非语言部分
                                    for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                        if ("en".equals(baseInfo.getResume_language())) {
                                            long resultInsert = dbOperator.Insert_ResumePersonInfo(baseInfo);
                                        }
                                    }
                                } else {// 英文个人信息存在
                                    // 非语言部分
                                    if (baseInfoEn1.getIsUpdate() == 0) {// 本地未修改过英文个人信息
                                        // 一、非语言部分
                                        for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                            if ("en".equals(baseInfo.getResume_language())) {
                                                boolean result = dbOperator.update_ResumePersonInfo(baseInfo);
                                            }
                                        }
                                    }
                                }
                                // **************************简历信息*****************************//
                                boolean hasResume = false;
                                for (ResumeList resumeOnLine : listResumeLists) {
                                    hasResume = false;
                                    if (listLocalZh1 == null || listLocalZh1.length == 0) {
                                        listResume.add(resumeOnLine);
                                        continue;
                                    }
                                    for (ResumeTitle resumeTitleLocalZh1 : listLocalZh1)


                                        if (!hasResume) {
                                            listResume.add(resumeOnLine);
                                        }
                                }
                                handService.sendEmptyMessage(LOADING_RESUMELIST);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else if (msg.what == LOADING_RESUMELIST) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
//                    isAppResume();
//                    initIsApp();
                } else if (msg.what == -1) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(msg.arg1), Toast.LENGTH_SHORT).show();
                }
            }

            ;
        };

        public AsyncGetResumeList(Context context) {
            this.context = context;
            this.dialog = new MyProgressDialog(context);
            if (dialog == null) {
                dialog = new MyProgressDialog(getActivity());
            }
        }

        protected void execute() {
            try {
                /*
                 * 获取服务器简历
				 */
                HashMap<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("method", "user_resume.resumelist");
                NetService service = new NetService(context, handService);
                service.execute(requestParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 加载离线简历
         *
         * @param dbOperator
         */
        private void loadOffLineResume(DAO_DBOperator dbOperator) {
            ResumeTitle[] resumeTitles = dbOperator.query_ResumeList("zh");
            for (ResumeTitle resumeTitle : resumeTitles) {
                if ("-1".equals(resumeTitle.getResume_id())) {
                    ResumeList offLineResume = new ResumeList();
                    offLineResume.setUser_id(MyUtils.userID);
                    offLineResume.setTitle(resumeTitle.getTitle());
                    offLineResume.setResume_id(resumeTitle.getResume_id());
                    offLineResume.setOpen(resumeTitle.getOpen());
                    offLineResume.setUptime(resumeTitle.getUptime());
                    offLineResume.setAdd_time(resumeTitle.getAdd_time());
                    offLineResume.setCastbehalf(resumeTitle.getCastbehalf());
                    offLineResume.setImportant(resumeTitle.getImportant());
                    offLineResume.setIsUpdate(resumeTitle.getIsUpdate());
                    ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(
                            resumeTitle.getResume_id(), resumeTitle.getResume_language());
                    if (order != null) {
                        offLineResume.setFunc(order.getFunc());
                        offLineResume.setJobtype(order.getJobtype());
                        offLineResume.setOrder_salary(order.getOrder_salary());
                    }
                    listResume.add(offLineResume);
                }
            }
        }
    }

}
