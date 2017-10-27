package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeIsAppResumePopupLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.model.ResumeAssessInfo;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeList;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.datautils.DataUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeComplete;
import com.hr.ui.utils.datautils.ResumeInfoToJsonString;
import com.hr.ui.utils.datautils.ResumeListStringJsonParser;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncResumeCenterGetResumesDetail;
import com.hr.ui.utils.netutils.AsyncResumeUpdate;
import com.hr.ui.utils.netutils.AsyncResumeUpgrade;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.MyProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 我的简历
 */
public class MyResumeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MyResumeActivity";
    private static String resumeId = null;// 简历id
    private String resumeModifyTime = null;// 简历时间戳
    private static String resumeType = null;// 简历类型

    private String onlyOne = "1";
    /**
     * 存储个人信息的Map
     */
    private HashMap<String, String> personInfoMap = new HashMap<>();

    private View viewPopIsApp;
    private View viewPopMoreSetting;
    private PopupWindow popwindowIsAPPResume, popwindowMoreSetting;
    private RelativeLayout rl_myresume_preview, rl_myresume_practiceexp, rl_myresume_introduction, rl_myresume_skills, rl_myresume_language, rl_myresume_training, rl_myresume_projects, rl_myresume_experience, rl_myresume_personalinfo, rl_myresume_objective, rl_myresume_education;
    private TextView tv_myresume_complete, tv_myresume_practiceexp, tv_myresume_projects, tv_myresume_refreshresume, tv_myresume_skills, tv_myresume_introduction, tv_myresume_updatatime, tv_myresume_training, tv_myresume_language;
    private ImageView iv_myresume_back, iv_myresume_setting, iv_myresume_refreshresume, iv_resume_personalinfo, iv_resume_objective, iv_myresume_experience, iv_resume_education;
    private LinearLayout rl_myresume_progressbar;
    private Context mContext = MyResumeActivity.this;
    public boolean shouldUpdate = false;
    private SharedPreferencesUtils sUtils;
    private int LOADING_RESUMELIST = 0x1000;// 简历对比完成后，开始加载简历列表标识
    private ResumeTitle resumeTitle;
    private static DAO_DBOperator dbOperator;
    // 网络简历列表信息
    public static ArrayList<ResumeBaseInfo> listBaseInfos;
    public static ArrayList<ResumeLanguageLevel> listLanguageLevels;
    public static ArrayList<ResumeList> listResumeLists;
    private static ArrayList<ResumeList> listResume = null;
    private ArrayList<ResumeList> listResumeIsApp = null;
    private String userId;
    /**
     * 第一次获取
     */
    private int firstGo = 1;
    private ResumeComplete resumeComplete;
    private boolean isCHS = true;
    private ResumeBaseInfo baseInfoZh, baseInfoEn;
    private int stateNum = 0;
    /**
     * 有无工作经验
     */
    boolean hasExperience;

    private String listResumeJsonString;

    public static MyResumeActivity myResumeActivity = null;
    private boolean canUpdate = false;
    /**
     * 记录listResume中 app简历位置
     */
    private int isAppPosition;
    private boolean isHaveAppResume;
    private RefleshDialogUtils dialogUtils;
    private BeautifulDialog.Builder builderRef;
    /**
     * 管理简历预览跳转
     */
    private String goType;
    /**
     * 从网络 获取数据后返回的结果
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dialogUtils.dismissDialog();
            resumeTitle = dbOperator.query_ResumeTitle_info(resumeId, "zh");
            canUpdate = true;
            upDataAll();
        }
    };
    /**
     * 升级分发简历
     */
    private Handler handlerUpdateResume = new Handler() {
        public void handleMessage(Message msg) {
            dialogUtils.dismissDialog();
            executeRefresh();
        }
    };
    private Handler handlerIsApp = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                dialogUtils.dismissDialog();
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                            sUtils.setStringValue("is_app_resumeid" + userId, resumeId);
                            MeFragment.meFragment.execute();
                            executeRefresh();
                            break;
                        default:
                            Toast.makeText(mContext, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialogUtils.dismissDialog();
                Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 更新判断语句
     */
    private Handler handlerUploadResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                int value = msg.what;
                switch (value) {
                    case -1:
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:// 中文简历已上传成功
                        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
                        refreshUI();
                        break;
                    default:
                        break;
                }

            }
        }

        ;
    };
    private Handler handlerPersonInfo = new Handler() {
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
                        handlerPersonInfo.sendMessage(message);
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("base_info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.getString("resume_language").equals("zh") && onlyOne.equals("1")) {
                            personInfoMap.put("user_id", jsonObject1.getString("user_id"));
                            personInfoMap.put("sex", jsonObject1.getString("sex"));
                            personInfoMap.put("name", jsonObject1.getString("name"));
                            personInfoMap.put("ydphone", jsonObject1.getString("ydphone"));
                            personInfoMap.put("pic_filekey", jsonObject1.getString("pic_filekey"));
                            onlyOne = "2";
                        }
                    }
                    JSONArray jsonArrayResumeList = jsonObject.getJSONArray("resume_list");
                    listResumeJsonString = jsonArrayResumeList.toString();
                    LogTools.i(TAG, "======jsonArray1" + jsonArrayResumeList.toString());
                    for (int i = 0; i < jsonArrayResumeList.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayResumeList.getJSONObject(i);
                        personInfoMap.put("resume_id", jsonObject1.getString("resume_id"));
                    }
                    initIsApp();
                    Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT).show();
                    refreshUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }
        setContentView(R.layout.activity_my_resume);
        MobclickAgent.onEvent(this,"cv-mycv");
        dialogUtils=new RefleshDialogUtils(this);
        myResumeActivity = MyResumeActivity.this;
        dbOperator = new DAO_DBOperator(mContext);
        listResume = new ArrayList<ResumeList>();
        sUtils = new SharedPreferencesUtils(mContext);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (goType != null) {
            if (goType.equals("10")) {
                finish();
            }
        }
        if (canUpdate) {
            upDataAll();
        }
    }

    /**
     * 刷新界面
     */
    public void refreshUI() {
        Intent intentResume = new Intent(mContext, MyResumeActivity.class);
        intentResume.putExtra("listResumeJsonObj", listResumeJsonString);
        intentResume.putExtra("user_id", personInfoMap.get("user_id"));
        intentResume.putExtra("goType", goType);
        startActivity(intentResume);
        finish();
    }

    /**
     * 访问网络 获取个人信息
     */
    protected void executeRefresh() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.resumelist");
            NetService service = new NetService(mContext, handlerPersonInfo);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化App简历
     */
    private void initIsApp() {
        /**
         * 先判断本地有没有app简历
         */
        //保证唯一性
        if (firstGo == 1) {
            userId = getIntent().getStringExtra("user_id");
            goType = getIntent().getStringExtra("goType");
            listResumeJsonString = getIntent().getStringExtra("listResumeJsonObj");
            firstGo = 2;
        }
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
        // 网络获取的有app简历
        try {
            JSONArray jsonArray2 = new JSONArray(listResumeJsonString);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                if (jsonObject.getString("is_app").equals("1")) {
                    resumeId = jsonObject.getString("resume_id");
                    resumeType = jsonObject.getString("resume_type");
                    sUtils.setStringValue("is_app_resumeid" + userId, resumeId);
                    /**
                     * 保存的是简历ID
                     */
                    isHaveAppResume = true;
                    refreshResume();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (listResumeIsApp.size() == 0&&isHaveAppResume==false) {
            MeFragment.newAppResume = true;
            MeFragment.isLoad = true;
        }
        //网络没有
        if (!isHaveAppResume) {
            chooseIsApp();
        }
        //本地有
    }

    public void upDataAll() {

        refreshItemInfoComplete();
        updataComplete();

        if (sUtils.getBooleanValue("isFirstResume" + MyUtils.userID + "resume" + resumeId, true)) {
            sUtils.setBooleanValue("isFirstResume" + MyUtils.userID + "resume" + resumeId, false);
            executeRefresh();
        } else {
            if (goType != null) {
                switch (goType) {
                    case "0":
                        break;
                    case "1":
                        goType = "10";
                        goActivity(ResumePersonInfoActivity.class);
                        break;
                    case "2":
                        goType = "10";
                        goActivity(ResumeJobOrderActivity.class);
                        break;
                    case "3":
                        goType = "10";
                        goActivity(ResumeEduInfoActivity.class);
                        break;
                    case "4":
                        goType = "10";
                        goActivity(ResumeJobExpActivity.class);
                        break;
                    case "5":
                        goType = "10";
                        goActivity(ResumeProjectExpActivity.class);
                        break;
                    case "6":
                        goType = "10";
                        goActivity(ResumeSkillInfoActivity.class);
                        break;
                    case "7":
                        goType = "10";
                        goActivity(ResumeLanguageInfoActivity.class);
                        break;
                    case "8":
                        goType = "10";
                        goActivity(ResumeTrainExpActivity.class);
                        break;
                    case "9":
                        goType = "10";
                        goActivity(ResumeSelfAssessActivity.class);
                        break;
                    case "10":
                        finish();
                        break;
                    case "11":
                        goType = "10";
                        resumeScan();
                        break;
                }
            }
        }

    }

    private void updataComplete() {
        //更新完整度 有无工作经验
        ResumeList resumeObj = null;

        for (int i = 0; i < listResume.size(); i++) {
            if (listResume.get(i).getResume_id().equals(resumeId)) {
                resumeObj = listResume.get(i);
            }
            if (resumeObj != null) {
                if (resumeObj.getResume_type() == null) {
                    hasExperience = false;
                    rl_myresume_experience.setVisibility(View.GONE);
                    rl_myresume_practiceexp.setVisibility(View.VISIBLE);
                } else {
                    //毕业生简历
                    if ("2".equals(resumeObj.getResume_type())) {
                        hasExperience = false;
                        rl_myresume_experience.setVisibility(View.GONE);
                        rl_myresume_practiceexp.setVisibility(View.VISIBLE);
                    } else {
                        rl_myresume_experience.setVisibility(View.VISIBLE);
                        rl_myresume_practiceexp.setVisibility(View.GONE);
                        hasExperience = true;
                    }
                }
            }
            resumeComplete = new ResumeComplete(this, resumeId, "zh");
            int fillScallInt = resumeComplete.getFullScale(hasExperience, true);
            float fillScallFloat = fillScallInt;
            rl_myresume_progressbar.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, fillScallFloat));
            if (fillScallInt >= 60) {
                rl_myresume_progressbar.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.linear_yuanhu_bg));
                tv_myresume_complete.setText("简历完整度" + fillScallInt + "%");
                if (fillScallInt >= 70) {
                    tv_myresume_complete.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv_myresume_complete.setTextColor(Color.parseColor("#666666"));
                }
                findViewById(R.id.linear_myresume_progressbarwaimian).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.linear_border));
            } else {
                rl_myresume_progressbar.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.linear_yuanhu_bg_hui));
                tv_myresume_complete.setText("简历完整度" + fillScallInt + "%");
                tv_myresume_complete.setTextColor(Color.parseColor("#666666"));
                findViewById(R.id.linear_myresume_progressbarwaimian).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.linear_border_hui));
            }
        }
        tv_myresume_updatatime.setText(DataUtils.timet(resumeTitle.getModify_time()));
        stateNum = 0;
        if (baseInfoZh != null) {
            stateNum += baseInfoZh.getIsUpdate();
        }
        if (baseInfoEn != null) {
            stateNum += baseInfoEn.getIsUpdate();
        }
        iv_myresume_refreshresume.setImageResource(R.mipmap.jianlishuaxin);
        tv_myresume_refreshresume.setText("刷新简历");
        tv_myresume_refreshresume.setTextColor(Color.parseColor("#F39800"));
//        }
        baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
        baseInfoEn = dbOperator.query_ResumePersonInfo_Toone("en");
    }

    private void initData() {
        getResumeList();
    }

    private void initView() {
        tv_myresume_complete = (TextView) findViewById(R.id.tv_myresume_complete);
        iv_myresume_back = (ImageView) findViewById(R.id.iv_myresume_back);
        rl_myresume_education = (RelativeLayout) findViewById(R.id.rl_myresume_education);
        rl_myresume_experience = (RelativeLayout) findViewById(R.id.rl_myresume_experience);
        rl_myresume_projects = (RelativeLayout) findViewById(R.id.rl_myresume_projects);
        rl_myresume_skills = (RelativeLayout) findViewById(R.id.rl_myresume_skills);
        rl_myresume_language = (RelativeLayout) findViewById(R.id.rl_myresume_language);
        rl_myresume_training = (RelativeLayout) findViewById(R.id.rl_myresume_training);
        rl_myresume_introduction = (RelativeLayout) findViewById(R.id.rl_myresume_introduction);
        rl_myresume_preview = (RelativeLayout) findViewById(R.id.rl_myresume_preview);
        rl_myresume_personalinfo = (RelativeLayout) findViewById(R.id.rl_myresume_personalinfo);
        rl_myresume_objective = (RelativeLayout) findViewById(R.id.rl_myresume_objective);
        rl_myresume_progressbar = (LinearLayout) findViewById(R.id.rl_myresume_progressbar);
        rl_myresume_practiceexp = (RelativeLayout) findViewById(R.id.rl_myresume_practiceexp);

        iv_resume_personalinfo = (ImageView) findViewById(R.id.iv_resume_personalinfo);
        iv_resume_objective = (ImageView) findViewById(R.id.iv_resume_objective);
        iv_resume_education = (ImageView) findViewById(R.id.iv_resume_education);
        iv_myresume_experience = (ImageView) findViewById(R.id.iv_myresume_experience);
        iv_myresume_refreshresume = (ImageView) findViewById(R.id.iv_myresume_refreshresume);
        iv_myresume_setting = (ImageView) findViewById(R.id.iv_myresume_setting);


        tv_myresume_projects = (TextView) findViewById(R.id.tv_myresume_projects);
        tv_myresume_skills = (TextView) findViewById(R.id.tv_myresume_skills);
        tv_myresume_language = (TextView) findViewById(R.id.tv_myresume_language);
        tv_myresume_training = (TextView) findViewById(R.id.tv_myresume_training);
        tv_myresume_introduction = (TextView) findViewById(R.id.tv_myresume_introduction);
        tv_myresume_updatatime = (TextView) findViewById(R.id.tv_myresume_updatatime);
        tv_myresume_refreshresume = (TextView) findViewById(R.id.tv_myresume_refreshresume);
        tv_myresume_practiceexp = (TextView) findViewById(R.id.tv_myresume_practiceexp);


        rl_myresume_personalinfo.setOnClickListener(this);
        rl_myresume_preview.setOnClickListener(this);
        iv_myresume_back.setOnClickListener(this);
        rl_myresume_objective.setOnClickListener(this);
        rl_myresume_education.setOnClickListener(this);
        rl_myresume_experience.setOnClickListener(this);
        rl_myresume_projects.setOnClickListener(this);
        rl_myresume_training.setOnClickListener(this);
        rl_myresume_language.setOnClickListener(this);
        rl_myresume_skills.setOnClickListener(this);
        rl_myresume_introduction.setOnClickListener(this);
        iv_myresume_refreshresume.setOnClickListener(this);
        iv_myresume_setting.setOnClickListener(this);
        rl_myresume_practiceexp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (listResume == null || listResume.size() == 0) {
            sUtils.setBooleanValue("isFirstCreateResume", true);
            MyUtils.firstResume = true;
        } else {
            sUtils.setBooleanValue("isFirstCreateResume", false);
            MyUtils.firstResume = false;
        }
        switch (v.getId()) {
            case R.id.rl_myresume_preview:
//                LogTools.i(TAG, "======rl_myresume_preview已点击");
                resumeScan();
                break;
            case R.id.iv_myresume_back:
//                // 判断此按钮状态
//                if (tv_myresume_refreshresume.getText().equals("刷新简历")) {// 刷新简历
                finish();
//                } else if (tv_myresume_refreshresume.getText().equals("上传简历")) {// 上传简历
//                    isUploadDialog();
//                }
                break;
            case R.id.rl_myresume_personalinfo:
                goActivity(ResumePersonInfoActivity.class);
                break;
            case R.id.rl_myresume_objective:
                goActivity(ResumeJobOrderActivity.class);
                break;
            case R.id.rl_myresume_education:
                goActivity(ResumeEduInfoActivity.class);
                break;
            case R.id.rl_myresume_experience:
                goActivity(ResumeJobExpActivity.class);
                break;
            case R.id.rl_myresume_projects:
                goActivity(ResumeProjectExpActivity.class);
                break;
            case R.id.rl_myresume_training:
                goActivity(ResumeTrainExpActivity.class);
                break;
            case R.id.rl_myresume_language:
                goActivity(ResumeLanguageInfoActivity.class);
                break;
            case R.id.rl_myresume_skills:
                goActivity(ResumeSkillInfoActivity.class);
                break;
            case R.id.rl_myresume_introduction:
                goActivity(ResumeSelfAssessActivity.class);
                break;
            case R.id.iv_myresume_refreshresume:
                resumeUpdate();
                break;
            case R.id.iv_myresume_setting:
                moreSetting();
                break;
            case R.id.tv_morepopup_updateresume:
                popwindowMoreSetting.dismiss();
                dialogUtils.showDialog();
                AsyncResumeUpgrade asyncResumeUpgrade = new AsyncResumeUpgrade(mContext, handlerUpdateResume);
                asyncResumeUpgrade.execute(resumeId, "zh");
                break;
            case R.id.tv_morepopup_sendresume:
                Intent intentResumeSend = new Intent(mContext, ResumeSendActivity.class);
                intentResumeSend.putExtra("resumeId", resumeId);
                intentResumeSend.putExtra("resumeLanguage", "zh");
                startActivity(intentResumeSend);
                popwindowMoreSetting.dismiss();
                break;
            case R.id.rl_myresume_practiceexp:
                goActivity(ResumeJobExpActivity.class);
                break;
            case R.id.iv_morepopup_back:
                popwindowMoreSetting.dismiss();
                break;
        }
    }

    /**
     * 简历设置
     */
    private void moreSetting() {
        viewPopMoreSetting = LayoutInflater.from(mContext).inflate(R.layout.item_popup_myresume_more, null);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        popwindowMoreSetting = new PopupWindow(this);
        popwindowMoreSetting.setContentView(viewPopMoreSetting);
        popwindowMoreSetting.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindowMoreSetting.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        popwindowMoreSetting.setFocusable(true);
        popwindowMoreSetting.setTouchable(true);
        popwindowMoreSetting.setBackgroundDrawable(new BitmapDrawable());
        popwindowMoreSetting.showAtLocation(viewPopMoreSetting, Gravity.CENTER, 0, 0);

        TextView tv_morepopup_updateresume = (TextView) viewPopMoreSetting.findViewById(R.id.tv_morepopup_updateresume);
        TextView tv_morepopup_sendresume = (TextView) viewPopMoreSetting.findViewById(R.id.tv_morepopup_sendresume);
        ImageView iv_morepopup_back = (ImageView) viewPopMoreSetting.findViewById(R.id.iv_morepopup_back);

        tv_morepopup_sendresume.setOnClickListener(this);
        tv_morepopup_updateresume.setOnClickListener(this);
        iv_morepopup_back.setOnClickListener(this);
        if (resumeType != null) {
            if (!resumeType.equals("2")) {
                tv_morepopup_updateresume.setVisibility(View.GONE);
            } else {
                tv_morepopup_updateresume.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 打开预览简历
     */
    private void startScanResume(String resumeId, String resumeLanguage) {
        Intent intent = new Intent(mContext, PreviewResumeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("resumeId", resumeId);
        intent.putExtra("resumeLanguage", resumeLanguage);
        startActivity(intent);
    }

    /**
     * 从本地 获取简历列表
     */
    public void getResumeList() {
        dialogUtils.showDialog();
        AsyncGetResumeList asyncGetResumeList = new AsyncGetResumeList(mContext);
        asyncGetResumeList.execute();
    }

    /**
     * 刷新简历
     */
    private void refreshResume() {
        dialogUtils.showDialog();
        AsyncResumeCenterGetResumesDetail resumesDetail = new AsyncResumeCenterGetResumesDetail(mContext, handler, resumeId, "zh");
        resumesDetail.execute();
    }

    /**
     * 切换视图
     *
     * @param cls
     */
    private void goActivity(Class cls) {
        Intent intent = new Intent(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (resumeId != null) {
            intent.putExtra("resumeId", resumeId);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
    }

    /**
     * 简历预览
     */
    private void resumeScan() {
        startScanResume(resumeId, "zh");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * 如果弹出popwindowIsAPPResume 屏蔽返回键
         */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popwindowIsAPPResume != null) {
                if (popwindowIsAPPResume.isShowing()) {
                    popwindowIsAPPResume.dismiss();
                    finish();
                } else {
                    finish();
                    return true;
                }
            } else {
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 向服务器发送 修改简历app
     */
    private void sendIsApp() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.setisapp");
            requestParams.put("resume_id", resumeId);
            requestParams.put("is_app", "1");
            dialogUtils.showDialog();
            NetService service = new NetService(mContext, handlerIsApp);
            service.execute(requestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtils.dismissDialog();
        builderRef.create().dismiss();

    }

    /**
     * 获取简历列表信息
     */
    private class AsyncGetResumeList {
        private Context context;
        private Handler handService = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    dialogUtils.dismissDialog();
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
//                    isAppResume();
                    dialogUtils.dismissDialog();
                    initIsApp();
                } else if (msg.what == -1) {
                    dialogUtils.dismissDialog();
                    Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(msg.arg1), Toast.LENGTH_SHORT).show();
                }
            }

            ;
        };

        public AsyncGetResumeList(Context context) {
            this.context = context;
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
                    ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(resumeTitle.getResume_id(), resumeTitle.getResume_language());
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

    /**
     * 简历刷新或上传
     */
    private void resumeUpdate() {
        // 判断此按钮状态
//        int tagInt = (Integer) iv_myresume_refreshresume.getTag();
//        if (tv_myresume_refreshresume.getText().equals("刷新简历")) {// 刷新简历
        refreshDialog();
//        } else if (tv_myresume_refreshresume.getText().equals("上传简历")) {// 上传简历
//            uploadDialog();
//        }
    }

    /**
     * 刷新简历(下载简历)
     */
    private void refreshDialog() {
        builderRef = new BeautifulDialog.Builder(mContext);
        builderRef.setMessage("确认要刷新简历么？");
        builderRef.setTitle("提示");
        builderRef.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("-1".equals(resumeId)) {
                    Toast.makeText(mContext, "请先上传简历", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                executeRefresh();
            }
        });
        builderRef.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderRef.create().show();
    }

    /**
     * 刷新各项信息完整与不完整判断（无问题）
     */
    public void refreshItemInfoComplete() {
        // 个人信息
        try {
            ResumeBaseInfo baseInfo = dbOperator
                    .query_ResumePersonInfo_Toone(isCHS == true ? "zh" : "en");
            if (baseInfo != null) {
                if (baseInfo.getName().trim().length() == 0
                        || baseInfo.getSex().trim().length() == 0
                        || baseInfo.getYear().trim().length() == 0
//                        || baseInfo.getNationality().trim().length() == 0
                        || baseInfo.getLocation().trim().length() == 0
                        || baseInfo.getWork_beginyear().trim().length() == 0
                        || baseInfo.getWork_beginyear() == "0"
                        || baseInfo.getPost_rank().trim().length() == 0
//                        || baseInfo.getCardtype().trim().length() == 0
                        || baseInfo.getYdphone().trim().length() == 0
                        || baseInfo.getEmailaddress().trim().length() == 0) {
                    iv_resume_personalinfo.setVisibility(View.INVISIBLE);
                } else {
                    iv_resume_personalinfo.setVisibility(View.VISIBLE);
                }
            } else {
                iv_resume_personalinfo.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 求职意向
        try {
            ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
            if (order != null) {
//                if (isCHS) {
                // 领域
                int numLingYu = 0;
                System.out.println(MyUtils.industryId + "------");
                if ("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId) || "14".equals(MyUtils.industryId)
                        || "29".equals(MyUtils.industryId)) {// 有领域行业
                    if (order.getLingyu().trim().toString().length() > 0) {
                        String[] itemLingyu = order.getLingyu().trim().toString().split(",");
                        for (String string : itemLingyu) {
                            if (string.contains(MyUtils.industryId + ":")) {
                                ++numLingYu;// 11:111100
                            }
                        }
                        if (numLingYu == 0) {// 特殊行业，不存在领域的时候，为不完整
                            System.out.println("error1");
                            iv_resume_objective.setVisibility(View.INVISIBLE);
                        } else {// 特殊行业，存在领域的时候
                            if (order.getFunc().trim().toString().length() == 0
                                    || !order.getFunc().trim().contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                                    || order.getWorkarea().trim().toString().length() == 0
                                    || order.getJobtype().trim().toString().length() == 0
                                    || order.getOrder_salary().trim().toString().length() == 0) {
                                System.out.println("error2");
                                iv_resume_objective.setVisibility(View.INVISIBLE);
                            } else {
                                iv_resume_objective.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        iv_resume_objective.setVisibility(View.INVISIBLE);
                    }
                } else {// 非特殊行业不判断领域
                    if (order.getFunc().trim().toString().length() == 0
                            || !order.getFunc().trim().contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                            || order.getWorkarea().trim().toString().length() == 0
                            || order.getJobtype().trim().toString().length() == 0
                            || order.getOrder_salary().trim().toString().length() == 0) {
                        iv_resume_objective.setVisibility(View.INVISIBLE);
                    } else {
                        iv_resume_objective.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                iv_resume_objective.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 教育背景
        try {
            ResumeEducation[] resumeEducations = dbOperator
                    .query_ResumeEducation(resumeTitle.getResume_id(),
                            isCHS == true ? "zh" : "en");
            if (resumeEducations == null || resumeEducations.length == 0) {
                iv_resume_education.setVisibility(View.INVISIBLE);
            } else {
                for (int i = 0; i < resumeEducations.length; i++) {
                    if (resumeEducations[i].getFromyear().trim().length() == 0
                            || resumeEducations[i].getToyear().trim().length() == 0
                            || resumeEducations[i].getSchoolname().toString()
                            .trim().length() == 0
                            || resumeEducations[i].getDegree().toString()
                            .trim().length() == 0
                            || resumeEducations[i].getMoremajor().toString()
                            .trim().length() == 0) {
                        iv_resume_education.setVisibility(View.INVISIBLE);
                    } else {
                        iv_resume_education.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //语言能力
        try {
            ResumeLanguageLevel[] languageLevels = dbOperator
                    .query_ResumeLanguageLevel();
            if (languageLevels == null || languageLevels.length == 0) {
                tv_myresume_language.setBackgroundResource(R.mipmap.touming_bg);
                tv_myresume_language.setText("+5%");
            } else {
                tv_myresume_language.setBackgroundResource(R.mipmap.duihao);
                tv_myresume_language.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 工作经验
        ResumeExperience[] experiences = dbOperator.query_ResumeWorkExperience(resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
        if (experiences == null || experiences.length == 0) {
            iv_myresume_experience.setVisibility(View.INVISIBLE);// 实习经验
        } else {
            for (int i = 0; i < experiences.length; i++) {
                LogTools.i(TAG, "==========experiences[i].toString()" + experiences[i].toString());
                // System.out.println(experiences[i].toString());
                if (experiences[i].getCompany().toString().trim().length() == 0
                        || experiences[i].getFromyear().trim().length() == 0
                        || experiences[i].getToyear().trim().length() == 0
                        || experiences[i].getCompanyaddress().toString().trim()
                        .length() == 0
                        || experiences[i].getSalary().toString().trim()
                        .length() == 0
                        || experiences[i].getPosition().toString().trim()
                        .length() == 0
                        || experiences[i].getResponsibility().toString().trim()
                        .length() == 0) {
                    iv_myresume_experience.setVisibility(View.INVISIBLE);
                    tv_myresume_practiceexp.setBackgroundResource(R.mipmap.touming_bg);
                    tv_myresume_practiceexp.setText("+5%");
                    // System.out.println("工作经验不完整");
                } else {
                    // System.out.println("工作经验完整");
                    iv_myresume_experience.setVisibility(View.VISIBLE);
                    tv_myresume_practiceexp.setBackgroundResource(R.mipmap.duihao);
                    tv_myresume_practiceexp.setText("");
                }
            }
        }

        // 自我评价
        try {
            ResumeAssessInfo assessInfo = dbOperator.query_ResumeTome_Toone(
                    resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
            if (assessInfo == null
                    || assessInfo.getIntroduction().trim().length() == 0) {
                tv_myresume_introduction.setBackgroundResource(R.mipmap.touming_bg);
                tv_myresume_introduction.setText("+5%");
            } else {
                tv_myresume_introduction.setBackgroundResource(R.mipmap.duihao);
                tv_myresume_introduction.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 项目经验
        try {
            ResumeProject[] projects = dbOperator.query_Resumeitem(
                    resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
            if (projects == null || projects.length == 0) {
                tv_myresume_projects.setBackgroundResource(R.mipmap.touming_bg);
                tv_myresume_projects.setText("+5%");
            } else {
                for (int j = 0; j < projects.length; j++) {
                    if (projects[j].getFromyear().toString().trim().length() == 0
                            || projects[j].getToyear().toString().trim()
                            .length() == 0
                            || projects[j].getProjectname().toString().length() == 0) {
                        tv_myresume_projects.setBackgroundResource(R.mipmap.touming_bg);
                        tv_myresume_projects.setText("+5%");
                    } else {
                        tv_myresume_projects.setBackgroundResource(R.mipmap.duihao);
                        tv_myresume_projects.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 专业技能
        try {
            ResumeSkill[] skills = dbOperator.query_ResumeSkill(
                    resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
            if (skills == null || skills.length == 0) {
                tv_myresume_skills.setBackgroundResource(R.mipmap.touming_bg);
                tv_myresume_skills.setText("+5%");
            } else {
                for (int i = 0; i < skills.length; i++) {
                    if (skills[i].getSkilltitle().toString().trim().length() == 0
                            || skills[i].getUsetime().toString().trim()
                            .length() == 0
                            || skills[i].getAbility().toString().trim()
                            .length() == 0) {
                        tv_myresume_skills.setBackgroundResource(R.mipmap.touming_bg);
                        tv_myresume_skills.setText("+5%");
                    } else {
                        tv_myresume_skills.setBackgroundResource(R.mipmap.duihao);
                        tv_myresume_skills.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 培训经历
        try {
            ResumePlant[] plants = dbOperator.query_ResumeTraining(
                    resumeTitle.getResume_id(), isCHS == true ? "zh" : "en");
            if (plants == null || plants.length == 0) {
                tv_myresume_training.setBackgroundResource(R.mipmap.touming_bg);
                tv_myresume_training.setText("+5%");
            } else {
                for (int i = 0; i < plants.length; i++) {
                    if (plants[i].getFromyear().toString().trim().length() == 0
                            || plants[i].getToyear().toString().trim().length() == 0
                            || plants[i].getInstitution().toString().trim()
                            .length() == 0
                            || plants[i].getCourse().toString().trim().length() == 0) {
                        tv_myresume_training.setBackgroundResource(R.mipmap.touming_bg);
                        tv_myresume_training.setText("+5%");
                    } else {
                        tv_myresume_training.setBackgroundResource(R.mipmap.duihao);
                        tv_myresume_training.setText("");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传简历（无问题）
     */
    public void uploadData() {
        if (listResume != null && listResume.size() == 6
                && "-1".equals(resumeId)) {
            Toast.makeText(mContext, "已有5份简历，不能再上传", Toast.LENGTH_SHORT).show();
            return;
        }
        // ***********************中文简历********************//
        // 检测本地中文简历
        ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info(resumeId, "zh");
        // 检测本地中文基本信息
        ResumeBaseInfo baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
        int completeInt = 0;
        ResumeComplete complete = new ResumeComplete(MyResumeActivity.this, resumeId, "zh");
        if ("1".equals(resumeType)) {// 1为社会人简历，且有工作经验
            completeInt = complete.getFullScale(true, false);
        } else {
            completeInt = complete.getFullScale(false, false);
        }
        if ((titleZhUpdate != null && titleZhUpdate.getIsUpdate() == 1)
                || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 1)) {
            // --------- 上传中文简历
            ResumeInfoToJsonString resumeInfoToJsonString = new ResumeInfoToJsonString(
                    MyResumeActivity.this, resumeId, "zh");
            String baseInfoString = resumeInfoToJsonString
                    .getBaseInofJsonString();
            String languageString = resumeInfoToJsonString
                    .getLanguageJsonString();
            String resumeInfoString = resumeInfoToJsonString
                    .getResumeDetailInfoJsonString();
            AsyncResumeUpdate asyncResumeUpdate = new AsyncResumeUpdate(
                    mContext, handlerUploadResume, baseInfoString,
                    languageString, resumeInfoString, resumeId, "zh");
            asyncResumeUpdate.execute();
        } else if ((titleZhUpdate != null && titleZhUpdate
                .getIsUpdate() == 0)
                || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 0)) {
            // ---------中文不需要上传,检测英文简历
            Message message = handlerUploadResume.obtainMessage();
            message.what = 0;
            message.arg1 = Integer.parseInt(resumeId);
            handlerUploadResume.sendMessage(message);
        }
    }

    /**
     * 选择app简历（无问题）
     */
    private void chooseIsApp() {
        viewPopIsApp = LayoutInflater.from(mContext).inflate(R.layout.item_popupwindow_isappresume, null);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        popwindowIsAPPResume = new PopupWindow(this);
        popwindowIsAPPResume.setContentView(viewPopIsApp);
//        popwindowIsAPPResume.setWidth(width / 6 * 5);
//        popwindowIsAPPResume.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popwindowIsAPPResume.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindowIsAPPResume.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        popwindowIsAPPResume.setFocusable(true);
        popwindowIsAPPResume.setTouchable(true);
//        popwindowIsAPPResume.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_popup_isresumeapp));
        popwindowIsAPPResume.setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗外可点击，默认为false
//        popwindowIsAPPResume.setAnimationStyle(R.style.popwindow);
//        popwindowIsAPPResume.showAtLocation(viewPopIsApp, Gravity.CENTER, 0, 0);
        popwindowIsAPPResume.showAtLocation(viewPopIsApp, Gravity.CENTER, 0, 0);
        ListView lv_item_popupwindow_appresume = (ListView) viewPopIsApp.findViewById(R.id.lv_item_popupwindow_appresume);
        RelativeLayout rl__item_popupwindow_appresume_save = (RelativeLayout) viewPopIsApp.findViewById(R.id.rl__item_popupwindow_appresume_save);
        final ResumeIsAppResumePopupLVAdapter resumeIsAppAdapter = new ResumeIsAppResumePopupLVAdapter(mContext, listResumeIsApp);
        resumeIsAppAdapter.setselectedPosition(0);
        isAppPosition = 0;
        lv_item_popupwindow_appresume.setAdapter(resumeIsAppAdapter);
        lv_item_popupwindow_appresume.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resumeIsAppAdapter.setselectedPosition(position);
                isAppPosition = position;
                resumeIsAppAdapter.notifyDataSetChanged();
            }
        });
        rl__item_popupwindow_appresume_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUtils.setStringValue("is_app_resumeid" + resumeId, listResumeIsApp.get(isAppPosition).getResume_id());
                resumeId = listResumeIsApp.get(isAppPosition).getResume_id();
                resumeType = listResumeIsApp.get(isAppPosition).getResume_type();
                sendIsApp();
                refreshResume();
                popwindowIsAPPResume.dismiss();
            }
        });
    }
}