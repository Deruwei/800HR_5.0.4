package com.hr.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.HrApplication;
import com.hr.ui.R;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.ModifyEduActivity;
import com.hr.ui.activity.ModifyExpActivity;
import com.hr.ui.activity.ModifyLanguageActivity;
import com.hr.ui.activity.ModifyPlantActivity;
import com.hr.ui.activity.ModifyProjectActivity;
import com.hr.ui.activity.ModifySkillActivity;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.MyResumeJobAdapter;
import com.hr.ui.activity.NewPhoneRegisterActivity;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.activity.PreviewResumeActivity;
import com.hr.ui.activity.ResumeEduInfoActivity;
import com.hr.ui.activity.ResumeJobExpActivity;
import com.hr.ui.activity.ResumeJobOrderActivity;
import com.hr.ui.activity.ResumeLanguageInfoActivity;
import com.hr.ui.activity.ResumePersonInfoActivity;
import com.hr.ui.activity.ResumeProjectExpActivity;
import com.hr.ui.activity.ResumeSelfAssessActivity;
import com.hr.ui.activity.ResumeSkillInfoActivity;
import com.hr.ui.activity.ResumeTrainExpActivity;
import com.hr.ui.activity.VerifyPhoneNumStateActivity;
import com.hr.ui.adapter.ResumeIsAppResumePopupLVAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.Industry;
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
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.datautils.DataUtils;
import com.hr.ui.utils.datautils.FileUtil;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeComplete;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeListStringJsonParser;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegisterAutoCode;
import com.hr.ui.utils.netutils.AsyncPhoneStates;
import com.hr.ui.utils.netutils.AsyncResumeCenterGetResumesDetail;
import com.hr.ui.utils.netutils.AsyncResumeUpgrade;
import com.hr.ui.utils.netutils.AsyncSetResumeName;
import com.hr.ui.utils.netutils.Async_MyResume_Open;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.utils.tools.CodeUtils;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CircleImageView;
import com.hr.ui.view.custom.MyProgressDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 简历预览页面
 */
public class MyResumeFragment extends Fragment {

    private static final String TAG = "PreviewResumeActivity";
//    private Context mContext = getActivity();
    /**
     * SharedPreference工具类
     */
    private SharedPreferencesUtils sUtils;
    private ImageView iv_previewresume_head;
//            iv_previewresume_sex;

    private TextView tv_myresume_baseinfo, tv_myresume_edu_modify, tv_previewresume_sex, tv_previewresume_back, tv_gome, tv_previewresume_home, tv_resume_previewresume_desp, tv_previewresume_phonenum, tv_previewresume_birthday, tv_previewresume_state, tv_previewresume_func, tv_previewresume_places,
            tv_previewresume_email, tv_previewresume_beginjobtime, tv_previewresume_nation, tv_previewresume_postrank, tv_previewresume_salary,
            tv_previewresume_jobtype, tv_previewresume_jobnum, tv_previewresume_name, tv_previewresume_edutime, tv_previewresume_territory, tv_previewresume_majordes,
            tv_previewresume_eduname, tv_previewresume_major, tv_previewresume_jobtime, tv_myresume_selfassessment, tv_previewresume_comname, tv_previewresume_post, resume_previewresume_jobexpsalary, resume_previewresume_jobexppost, resume_previewresume_jobdesp;
    /**
     * 项目经验
     */
    private TextView tv_previewresume_projectname, tv_myresume_project_modify, tv_previewresume_projectpost, tv_previewresume_projectdesp, tv_previewresume_projectduty, tv_previewresume_projecttime;
    /**
     * 专业技能
     */
    private TextView tv_previewresume_skillname, tv_previewresume_skilltime, tv_previewresume_skilllevel, tv_myresume_skill_modify;
    /**
     * 培训经历
     */
    private TextView tv_previewresume_traincertificate, tv_myresume_train_modify, tv_previewresume_trainplace, tv_previewresume_trainname, tv_previewresume_traincourse, tv_previewresume_traindesp, tv_previewresume_traintime;
    /**
     * 语言能力
     */
    private TextView tv_previewresume_languagename, tv_myresume_langue_modify, tv_previewresume_languagespeaklevel, tv_previewresume_languagereadlevel;
    private LinearLayout activity_resume_scan_language_linnearlayout, activity_resume_scan_train_linnearlayout, activity_resume_scan_skill_linnearlayout, activity_resume_scan_workexperience_linnearlayout, activity_resume_scan_educationexperience_linnearlayout, activity_resume_scan_projectexp_linnearlayout;

    private LinearLayout rl_myresume_addmyself;
    private LinearLayout rl_preview_resume_jobexp;
    /**
     * 基本信息
     */
    private ResumeBaseInfo resumeBaseInfo;
    /**
     * 简历头信息
     */
    private ResumeTitle resumeTitle;
    /**
     * 求职意向
     */
    private ResumeOrder resumeOrder;
    /**
     * 自我评价
     */
    private ResumeAssessInfo resumeAssessInfo;
    /**
     * 工作经验
     */
    private ResumeExperience[] resumeExperiences;
    /**
     * 教育背景
     */
    private ResumeEducation[] resumeEducations;
    /**
     * 培训经历
     */
    private ResumePlant[] resumePlants;
    /**
     * 语言能力
     */
    private ResumeLanguageLevel[] resumeLanguageLevels;
    /**
     * 专业技能
     */
    private ResumeSkill[] resumeSkills;
    /**
     * 项目经验
     */
    private ResumeProject[] resumeProjects;
    /**
     * UIL配置信息
     */
    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    /**
     * 简历ID
     */
    private String resumeID;
    /**
     * 简历ID
     */
    private String resumeIdString;
    /**
     * 简历语言
     */
    private String resumeLanguageString;
    private DAO_DBOperator dbOperator;
    private Calendar calendar;
    public static ArrayList<ResumeBaseInfo> listBaseInfos;
    public static ArrayList<ResumeLanguageLevel> listLanguageLevels;
    public static ArrayList<ResumeList> listResumeLists;
    private static ArrayList<ResumeList> listResume = null;
    private ArrayList<ResumeList> listResumeIsApp = null;
    private int LOADING_RESUMELIST = 0x1000;// 简历对比完成后，开始加载简历列表标识
    private TextView tv_myresume_upresume, tv_myresume_open, tv_compnum, myresume_preview, myresume_refresh, tv_myresumescan_persioninfo, tv_myresumescan_order, tv_myresumescan_edu, tv_myresumescan_jobexp, tv_myresumescan_project, tv_myresumescan_skill, tv_myresumescan_language, tv_myresumescan_train, tv_myresumescan_self;
    private View fragmentView;
    public static boolean isRefresh = true;
    public static MyResumeFragment myResumeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        fragmentView = inflater.inflate(R.layout.myresume_fragment, container, false);
        myResumeFragment = MyResumeFragment.this;
        initData();
        initUIL();
        initView();
        return fragmentView;
    }

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

    @Override
    public void onResume() {
        super.onResume();
        if (MyUtils.isLogin) {
            if (HrApplication.isPhoneState) {
                HrApplication.isPhoneState = false;
                AsyncPhoneStates asyncPhoneStates = new AsyncPhoneStates(getActivity(), handlerPhone);
                asyncPhoneStates.execute();
            } else {
                getData();
            }
        }
//        upDateUI();
    }


    public void getData() {
        getResumeList();
    }

    /**
     * 从本地 获取简历列表
     */
    public void getResumeList() {
        dbOperator = new DAO_DBOperator(getActivity());
        listResume = new ArrayList<ResumeList>();
        AsyncGetResumeList asyncGetResumeList = new AsyncGetResumeList(getActivity());
        asyncGetResumeList.execute();
    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(getActivity());
        resumeIdString = MainActivity.instanceMain.resumeId;
        resumeLanguageString = "zh";
        isCHS = true;
        calendar = Calendar.getInstance();
        dbOperator = new DAO_DBOperator(getActivity());
        initDBData(resumeIdString, resumeLanguageString);
    }

    /**
     * 从数据库中获取数据
     */
    private void initDBData(String resumeid, String resumelanguage) {
        resumeBaseInfo = null;
        resumeTitle = null;
        resumeOrder = null;
        resumeAssessInfo = null;
        resumeExperiences = null;
        resumeEducations = null;
        resumePlants = null;
        resumeLanguageLevels = null;
        resumeSkills = null;
        resumeProjects = null;
        getDataBase(resumeid, resumelanguage);
    }

    private void getDataBase(String resumeid, String resumelanguage) {
        resumeBaseInfo = dbOperator.query_ResumePersonInfo_Toone(resumelanguage);
        resumeTitle = dbOperator.query_ResumeTitle_info(resumeid, resumelanguage);
        resumeOrder = dbOperator.query_ResumeCareerObjective_Toone(resumeid, resumelanguage);
        resumeAssessInfo = dbOperator.query_ResumeTome_Toone(resumeid, resumelanguage);
        resumeExperiences = dbOperator.query_ResumeWorkExperience(resumeid, resumelanguage);
        resumeEducations = dbOperator.query_ResumeEducation(resumeid, resumelanguage);
        resumePlants = dbOperator.query_ResumeTraining(resumeid, resumelanguage);
        resumeLanguageLevels = dbOperator.query_ResumeLanguageLevel();
        resumeSkills = dbOperator.query_ResumeSkill(resumeid, resumelanguage);
        resumeProjects = dbOperator.query_Resumeitem(resumeid, resumelanguage);
    }

    /**
     * 更新UI
     */
    private void upDateUI() {
        getDataBase(resumeID, resumeLanguageString);
        /*
        个人信息部分
         */
        initBaseInfo();

        // 求职意向//
        initOrder();

        // 工作经验//
        initWorkExperience();

        // 教育背景//
        initEducation();

        // 项目经验//
        initProject();

        // 自我评价//
        initAssess();

        // 语言能力//
        initLanguage();
        // 专业技能//
        initSkill();

        // 培训经历//
        initPlant();
        //更新完整度 有无工作经验
        updataComplete();
        //是否公开简历
        isOpen();
    }

    /**
     * 升级分发简历
     */
    private Handler handlerUpdateResume = new Handler() {
        public void handleMessage(Message msg) {
            int msgInt = msg.arg1;
            refreshNow();
        }
    };

    private void upResume() {
        AsyncResumeUpgrade asyncResumeUpgrade = new AsyncResumeUpgrade(getActivity(), handlerUpdateResume);
        asyncResumeUpgrade.execute(resumeID, "zh");
    }

    private void updataComplete() {
        //更新完整度 有无工作经验
        ResumeList resumeObj = null;

        for (int i = 0; i < listResume.size(); i++) {
            if (listResume.get(i).getResume_id().equals(resumeID)) {
                resumeObj = listResume.get(i);
            }
            if (resumeObj != null) {
                if (resumeObj.getResume_type() == null) {
                    hasExperience = false;
                } else {
                    //毕业生简历
                    if ("2".equals(resumeObj.getResume_type())) {
                        hasExperience = false;
                    } else {
                        hasExperience = true;
                    }
                }
            }
            resumeComplete = new ResumeComplete(getActivity(), resumeID, "zh");
            int fillScallInt = resumeComplete.getFullScale(hasExperience, true);
            float fillScallFloat = fillScallInt;
            if (fillScallInt >= 60) {
                tv_compnum.setText(fillScallInt + "%");
                if (fillScallInt >= 70) {
                    tv_compnum.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
                } else {
                    tv_compnum.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
                }
            } else {
                tv_compnum.setText(fillScallInt + "%");
                tv_compnum.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
            }
        }
//        Toast.makeText(getActivity(),MyUtils.resumeTime,Toast.LENGTH_SHORT).show();
//        MeFragment.meFragment.updateUI();
        stateNum = 0;
        if (baseInfoZh != null) {
            stateNum += baseInfoZh.getIsUpdate();
        }
        if (baseInfoEn != null) {
            stateNum += baseInfoEn.getIsUpdate();
        }
        baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
        baseInfoEn = dbOperator.query_ResumePersonInfo_Toone("en");
        if (resumeTitle != null) {
            HrApplication.resumeTime = DataUtils.timeYearDay(resumeTitle.getModify_time());
            tv_myresume_baseinfo.setText(resumeTitle.getTitle());
        }
    }

    /**
     * 培训经历
     */
    private void initPlant() {
        activity_resume_scan_train_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_train_linnearlayout);
        if (resumePlants != null) {
            activity_resume_scan_train_linnearlayout.removeAllViews();
            for (int i = 0; i < resumePlants.length; i++) {
                View plantView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_resume_train_preview, null);
                activity_resume_scan_train_linnearlayout.addView(plantView);
                // initview
                tv_previewresume_traintime = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_traintime);
                tv_previewresume_traincourse = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_traincourse);
                tv_previewresume_traindesp = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_traindesp);
                tv_previewresume_trainname = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_trainname);
                tv_previewresume_trainplace = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_trainplace);
                tv_previewresume_traincertificate = (TextView) plantView
                        .findViewById(R.id.tv_previewresume_traincertificate);
                tv_myresume_train_modify = (TextView) plantView
                        .findViewById(R.id.tv_myresume_train_modify);
                // setdata
                // ----lable
                if (!isCHS) {
                }
                if (isCHS) {
                    String startTime = resumePlants[i].getFromyear() + "年"
                            + resumePlants[i].getFrommonth() + "月";
                    String endTime = resumePlants[i].getToyear() + "年"
                            + resumePlants[i].getTomonth() + "月";
                    if ("0年0月".equals(startTime)) {
                        startTime = "至今";
                    }
                    if ("0年0月".equals(endTime)) {
                        endTime = "至今";
                    }
                    tv_previewresume_traintime.setText(startTime + "--"
                            + endTime);
                } else {
                }
                tv_previewresume_trainname
                        .setText(resumePlants[i].getInstitution());
                tv_previewresume_traincourse.setText(resumePlants[i]
                        .getCourse());
                tv_previewresume_traindesp.setText(resumePlants[i]
                        .getTraindetail());
                tv_previewresume_trainplace.setText(ResumeInfoIDToString
                        .getPlace(getActivity(), resumePlants[i].getPlace(), isCHS));
                tv_previewresume_traincertificate
                        .setText(resumePlants[i].getCertification());
                final int finalI = i;
                tv_myresume_train_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ModifyPlantActivity.class);
                        intent.putExtra("resumeId", resumeID);
                        intent.putExtra("resumeLanguage", "zh");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumePlant", resumePlants[finalI]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (resumePlants.length == 0) {
                activity_resume_scan_train_linnearlayout.removeAllViews();
                addView(activity_resume_scan_train_linnearlayout, "添加培训经历", "琴棋书画，样样精通", "8");
            } else {
                addViewAdd(activity_resume_scan_train_linnearlayout, "+添加培训经历", "8");
            }
        }

    }

    /**
     * 专业技能
     */
    private void initSkill() {
        activity_resume_scan_skill_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_skill_linnearlayout);
        if (resumeSkills != null) {
            activity_resume_scan_skill_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeSkills.length; i++) {
                View skillView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_resume_skill_preview, null);
                activity_resume_scan_skill_linnearlayout.addView(skillView);
                // initview
                tv_previewresume_skillname = (TextView) skillView
                        .findViewById(R.id.tv_previewresume_skillname);
                tv_previewresume_skilllevel = (TextView) skillView.findViewById(R.id.tv_previewresume_skilllevel);
                tv_myresume_skill_modify = (TextView) skillView.findViewById(R.id.tv_myresume_skill_modify);
                tv_previewresume_skilltime = (TextView) skillView
                        .findViewById(R.id.tv_previewresume_skilltime);
                if (isCHS) {
                } else {
                }
                tv_previewresume_skillname.setText(resumeSkills[i]
                        .getSkilltitle());
                tv_previewresume_skilltime.setText(resumeSkills[i]
                        .getUsetime() + (isCHS == true ? "个月" : "months"));
                tv_previewresume_skilllevel.setText(ResumeInfoIDToString
                        .getSkillLevel(getActivity(), resumeSkills[i].getAbility(),
                                isCHS));
                final int finalI = i;
                tv_myresume_skill_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ModifySkillActivity.class);
                        intent.putExtra("resumeId", resumeID);
                        intent.putExtra("resumeLanguage", "zh");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumeSkill", resumeSkills[finalI]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (resumeSkills.length == 0) {
                activity_resume_scan_skill_linnearlayout.removeAllViews();
                addView(activity_resume_scan_skill_linnearlayout, "添加专业技能", "快来展示你的专业素养", "6");
            } else {
                addViewAdd(activity_resume_scan_skill_linnearlayout, "+添加专业技能", "6");
            }
        }
    }

    /**
     * 语言能力
     */
    private void initLanguage() {
        activity_resume_scan_language_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_language_linnearlayout);
        if (resumeLanguageLevels != null) {
            activity_resume_scan_language_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeLanguageLevels.length; i++) {
                View languagelevelView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_resume_language_preview, null);
                activity_resume_scan_language_linnearlayout
                        .addView(languagelevelView);
                tv_previewresume_languagename = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagename);
                tv_previewresume_languagespeaklevel = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagespeaklevel);
                tv_previewresume_languagereadlevel = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagereadlevel);
                tv_myresume_langue_modify = (TextView) languagelevelView
                        .findViewById(R.id.tv_myresume_langue_modify);
                if (isCHS) {
                } else {
                }
                tv_previewresume_languagename.setText(ResumeInfoIDToString.getLanguageTpye(getActivity(),
                        resumeLanguageLevels[i].getLangname(), isCHS));
                tv_previewresume_languagereadlevel.setText(ResumeInfoIDToString.getLanguageReadLevel(getActivity(),
                        resumeLanguageLevels[i].getRead_level(), isCHS));
                tv_previewresume_languagespeaklevel.setText(ResumeInfoIDToString.getLanguageSpeakLevel(getActivity(),
                        resumeLanguageLevels[i].getSpeak_level(), isCHS));
                final int finalI = i;
                tv_myresume_langue_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ModifyLanguageActivity.class);
                        intent.putExtra("resumeId", resumeID);
                        intent.putExtra("resumeLanguage", "zh");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumeLanguageLevel", resumeLanguageLevels[finalI]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (resumeLanguageLevels.length == 0) {
                activity_resume_scan_language_linnearlayout.removeAllViews();
                addView(activity_resume_scan_language_linnearlayout, "添加语言能力", "能说会道，你将更受欢迎哦", "7");
            } else {
                addViewAdd(activity_resume_scan_language_linnearlayout, "+添加语言能力", "7");
            }
        }
    }

    /**
     * 自我评价
     */
    private void initAssess() {
        rl_myresume_addmyself = (LinearLayout) fragmentView.findViewById(R.id.rl_myresume_addmyself);
        if (resumeAssessInfo != null) {
            rl_myresume_addmyself.removeAllViews();
            rl_myresume_addmyself.addView(tv_myresume_selfassessment);
            tv_myresume_selfassessment.setText(resumeAssessInfo.getIntroduction());
        } else {
            rl_myresume_addmyself.removeAllViews();
            addView(rl_myresume_addmyself, "添加自我评价", "夸夸自己吧", "9");
        }
    }

    /**
     * 项目经验
     */
    private void initProject() {
        activity_resume_scan_projectexp_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_projectexp_linnearlayout);
        if (resumeProjects != null) {
            activity_resume_scan_projectexp_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeProjects.length; i++) {
                View projectView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_resume_projectexp_preview, null);
                activity_resume_scan_projectexp_linnearlayout.addView(projectView);
                // initview
                tv_previewresume_projectname = (TextView) projectView.findViewById(R.id.tv_previewresume_projectname);
                tv_previewresume_projectpost = (TextView) projectView.findViewById(R.id.tv_previewresume_trainname);
                tv_previewresume_projectdesp = (TextView) projectView.findViewById(R.id.tv_previewresume_projectdesp);
                tv_previewresume_projectduty = (TextView) projectView.findViewById(R.id.tv_previewresume_projectduty);
                tv_previewresume_projecttime = (TextView) projectView.findViewById(R.id.tv_previewresume_traintime);
                tv_myresume_project_modify = (TextView) projectView.findViewById(R.id.tv_myresume_project_modify);
                if (isCHS) {
                    if (i != 0) {
                    }
                    String startTime = resumeProjects[i].getFromyear() + "年"
                            + resumeProjects[i].getFrommonth() + "月";
                    String endTime = resumeProjects[i].getToyear() + "年"
                            + resumeProjects[i].getTomonth() + "月";
                    if ("0年0月".equals(startTime)) {
                        startTime = "至今";
                    }
                    if ("0年0月".equals(endTime)) {
                        endTime = "至今";
                    }
                    tv_previewresume_projecttime.setText(startTime + "--"
                            + endTime);
                } else {
                }
                tv_previewresume_projectname.setText(resumeProjects[i]
                        .getProjectname());
                tv_previewresume_projectdesp.setText(resumeProjects[i]
                        .getProjectdesc());
                tv_previewresume_projectpost.setText(resumeProjects[i]
                        .getPosition());
                tv_previewresume_projectduty
                        .setText(resumeProjects[i].getResponsibility());
                final int finalI = i;
                tv_myresume_project_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ModifyProjectActivity.class);
                        intent.putExtra("resumeId", resumeID);
                        intent.putExtra("resumeLanguage", "zh");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumeProject", resumeProjects[finalI]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (resumeProjects.length == 0) {
                activity_resume_scan_projectexp_linnearlayout.removeAllViews();
                addView(activity_resume_scan_projectexp_linnearlayout, "添加项目经验", "谈谈你的光荣历史", "5");
            } else {
                addViewAdd(activity_resume_scan_projectexp_linnearlayout, "+添加项目经验", "5");
            }
        }
    }


    /**
     * 教育背景
     */
    private void initEducation() {
        if (resumeEducations != null) {
            activity_resume_scan_educationexperience_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeEducations.length; i++) {
                View educationExperienceView = LayoutInflater.from(getActivity()).inflate(R.layout.item_resume_educationexperience, null);
                activity_resume_scan_educationexperience_linnearlayout
                        .addView(educationExperienceView);
                // initview
                tv_previewresume_edutime = (TextView) educationExperienceView
                        .findViewById(R.id.tv_previewresume_edutime);
                tv_previewresume_eduname = (TextView) educationExperienceView
                        .findViewById(R.id.tv_previewresume_eduname);
                tv_previewresume_major = (TextView) educationExperienceView
                        .findViewById(R.id.tv_previewresume_major);
                tv_previewresume_majordes = (TextView) educationExperienceView
                        .findViewById(R.id.tv_previewresume_majordes);
                tv_resume_previewresume_desp = (TextView) educationExperienceView
                        .findViewById(R.id.tv_resume_previewresume_desp);
                tv_myresume_edu_modify = (TextView) educationExperienceView
                        .findViewById(R.id.tv_myresume_edu_modify);
                // ---data
                if (isCHS) {
                    String startTime = resumeEducations[i].getFromyear() + "年"
                            + resumeEducations[i].getFrommonth() + "月";
                    String endTime = resumeEducations[i].getToyear() + "年"
                            + resumeEducations[i].getTomonth() + "月";
                    if ("0年0月".equals(startTime)) {
                        startTime = "至今";
                    }

                    if ("0年0月".equals(endTime)) {
                        endTime = "至今";
                    }

                    tv_previewresume_edutime.setText(startTime + "--" + endTime);
                }
                tv_previewresume_eduname.setText(resumeEducations[i].getSchoolname());
                tv_previewresume_major.setText(resumeEducations[i].getMoremajor());
                tv_previewresume_majordes.setText(" " + ResumeInfoIDToString.getEducationDegree(getActivity(), resumeEducations[i].getDegree(), isCHS));
                tv_resume_previewresume_desp.setText(resumeEducations[i].getEdudetail());
                final int finalI = i;
                tv_myresume_edu_modify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ModifyEduActivity.class);
                        intent.putExtra("resumeId", resumeID);
                        intent.putExtra("resumeLanguage", "zh");
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("resumeEducation", resumeEducations[finalI]);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            if (resumeEducations.length == 0) {
                activity_resume_scan_educationexperience_linnearlayout.removeAllViews();
                addView(activity_resume_scan_educationexperience_linnearlayout, "添加教育背景", "告诉我，你师出何门", "3");
            } else {
                addViewAdd(activity_resume_scan_educationexperience_linnearlayout, "+添加教育背景", "3");
            }
        }
    }

    /**
     * 工作经验
     */
    private TextView tv_myresume_workecperience_modify;

    private void initWorkExperience() {
        rl_preview_resume_jobexp = (LinearLayout) fragmentView.findViewById(R.id.rl_preview_resume_jobexp);
        TextView resume_previewresume_shixi = (TextView) fragmentView.findViewById(R.id.resume_previewresume_shixi);
        int ecpTime = 0;
        String maxEcp = "";
        HrApplication.userJob = "无职位";
        if (resumeTitle != null) {
            if (resumeTitle.getResume_type().equals("1")) {
                resume_previewresume_shixi.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(),R.mipmap.bitian), null);
                resume_previewresume_shixi.setText("工作经验");
                tv_myresume_upresume.setVisibility(View.GONE);
                if (resumeExperiences != null) {
                    activity_resume_scan_workexperience_linnearlayout.removeAllViews();
                    for (int i = 0; i < resumeExperiences.length; i++) {
//                        Toast.makeText(this,i+"",Toast.LENGTH_SHORT).show();
                        View workExperienceView = LayoutInflater.from(getActivity()).inflate(R.layout.item_resume_scan_workexperience, null);
                        activity_resume_scan_workexperience_linnearlayout.addView(workExperienceView);
                        // initview
                        tv_myresume_workecperience_modify = (TextView) workExperienceView
                                .findViewById(R.id.tv_myresume_workecperience_modify);
                        tv_previewresume_jobtime = (TextView) workExperienceView
                                .findViewById(R.id.tv_previewresume_jobtime);
                        tv_previewresume_comname = (TextView) workExperienceView
                                .findViewById(R.id.tv_previewresume_comname);
                        tv_previewresume_post = (TextView) workExperienceView
                                .findViewById(R.id.tv_previewresume_post);
                        resume_previewresume_jobexpsalary = (TextView) workExperienceView
                                .findViewById(R.id.resume_previewresume_jobexpsalary);
                        resume_previewresume_jobexppost = (TextView) workExperienceView
                                .findViewById(R.id.resume_previewresume_jobexppost);
                        resume_previewresume_jobdesp = (TextView) workExperienceView
                                .findViewById(R.id.resume_previewresume_jobdesp);
                        // -----data
                        if (isCHS) {
                            String startTime = resumeExperiences[i].getFromyear() + "年" + resumeExperiences[i].getFrommonth() + "月";
                            String endTime = resumeExperiences[i].getToyear() + "年" + resumeExperiences[i].getTomonth() + "月";
                            if ("0年0月".equals(startTime)) {
                                startTime = "至今";
                            }
                            if ("0年0月".equals(endTime)) {
                                endTime = "至今";
                            }
                            tv_previewresume_jobtime.setText(startTime + "--" + endTime);
                        }
                        tv_previewresume_comname.setText(resumeExperiences[i].getCompany());
                        tv_previewresume_post.setText(resumeExperiences[i].getPosition());
                        resume_previewresume_jobexpsalary.setText(resumeExperiences[i].getSalary());
                        resume_previewresume_jobdesp.setText(resumeExperiences[i].getResponsibility());
                        if (Integer.parseInt(resumeExperiences[i].getToyear() + resumeExperiences[i].getTomonth()) > ecpTime) {
                            ecpTime = Integer.parseInt(resumeExperiences[i].getToyear() + resumeExperiences[i].getTomonth());
                            HrApplication.userJob = resumeExperiences[i].getPosition();
                        }
                        final int finalI = i;
                        tv_myresume_workecperience_modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ModifyExpActivity.class);
                                intent.putExtra("resumeId", resumeID);
                                intent.putExtra("resumeLanguage", "zh");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("resumeExperience", resumeExperiences[finalI]);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                }
            } else if (resumeTitle.getResume_type().equals("2")) {
                resume_previewresume_shixi.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tv_myresume_upresume.setVisibility(View.VISIBLE);
                resume_previewresume_shixi.setText("实习经验");
                activity_resume_scan_workexperience_linnearlayout.removeAllViews();
                for (int i = 0; i < resumeExperiences.length; i++) {
//                        Toast.makeText(this,i+"",Toast.LENGTH_SHORT).show();
                    View workExperienceView = LayoutInflater.from(getActivity()).inflate(R.layout.item_resume_scan_workexperience, null);
                    activity_resume_scan_workexperience_linnearlayout.addView(workExperienceView);
                    // initview
                    tv_previewresume_jobtime = (TextView) workExperienceView.findViewById(R.id.tv_previewresume_jobtime);
                    tv_myresume_workecperience_modify = (TextView) workExperienceView.findViewById(R.id.tv_myresume_workecperience_modify);
                    tv_previewresume_comname = (TextView) workExperienceView.findViewById(R.id.tv_previewresume_comname);
                    tv_previewresume_post = (TextView) workExperienceView.findViewById(R.id.tv_previewresume_post);
                    resume_previewresume_jobexpsalary = (TextView) workExperienceView.findViewById(R.id.resume_previewresume_jobexpsalary);
                    resume_previewresume_jobexppost = (TextView) workExperienceView.findViewById(R.id.resume_previewresume_jobexppost);
                    resume_previewresume_jobdesp = (TextView) workExperienceView.findViewById(R.id.resume_previewresume_jobdesp);
                    // -----data
                    if (isCHS) {
                        String startTime = resumeExperiences[i].getFromyear() + "年" + resumeExperiences[i].getFrommonth() + "月";
                        String endTime = resumeExperiences[i].getToyear() + "年" + resumeExperiences[i].getTomonth() + "月";
                        if ("0年0月".equals(startTime)) {
                            startTime = "至今";
                        }
                        if ("0年0月".equals(endTime)) {
                            endTime = "至今";
                        }
                        tv_previewresume_jobtime.setText(startTime + "--" + endTime);
                    }
                    tv_previewresume_comname.setText(resumeExperiences[i].getCompany());
                    tv_previewresume_post.setText(resumeExperiences[i].getPosition());
                    resume_previewresume_jobexpsalary.setText(resumeExperiences[i].getSalary());
                    resume_previewresume_jobdesp.setText(resumeExperiences[i].getResponsibility());
                    if (Integer.parseInt(resumeExperiences[i].getToyear() + resumeExperiences[i].getTomonth()) > ecpTime) {
                        ecpTime = Integer.parseInt(resumeExperiences[i].getToyear() + resumeExperiences[i].getTomonth());
                        HrApplication.userJob = resumeExperiences[i].getPosition();
                    }
                    final int finalI = i;
                    tv_myresume_workecperience_modify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ModifyExpActivity.class);
                            intent.putExtra("resumeId", resumeID);
                            intent.putExtra("resumeLanguage", "zh");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("resumeExperience", resumeExperiences[finalI]);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }

            } else {
                rl_preview_resume_jobexp.setVisibility(View.GONE);
            }
        }
        if (resumeExperiences.length == 0) {
            activity_resume_scan_workexperience_linnearlayout.removeAllViews();
            addView(activity_resume_scan_workexperience_linnearlayout, "添加工作经历", "展现你的魅力，让企业更关注你", "4");
        } else {
            addViewAdd(activity_resume_scan_workexperience_linnearlayout, "+添加工作经历", "4");
        }
    }

    /**
     * 求职意向
     */
    private void initOrder() {
        if (resumeOrder != null) {
            // ----data
            tv_previewresume_jobtype.setText(ResumeInfoIDToString.getWorkType(getActivity(), resumeOrder.getJobtype(), isCHS));
            // 从事职位
            initOrderFunc();

            // 工作地区
            initWorkPlace(resumeOrder.getWorkarea(),
                    tv_previewresume_places);

            if (isCHS) {
                tv_previewresume_salary.setText(resumeOrder.getOrder_salary() + "元/月");
            } else {
                tv_previewresume_salary.setText(resumeOrder.getOrder_salary() + "Yuan/Month");
            }
            if (resumeBaseInfo != null) {
                tv_previewresume_state.setText(ResumeInfoIDToString.getCurrentState(getActivity(), resumeBaseInfo.getCurrent_workstate(), isCHS));
                // System.out.println("个人信息：" + resumeBaseInfo);
            }
            initLingyu(resumeOrder.getLingyu());

        }
    }

    /**
     * 初始化领域
     */
    private void initLingyu(String lingyuId) {
//        Toast.makeText(mContext, "lingyuId:" + lingyuId.toString(), Toast.LENGTH_SHORT).show();
        // lingyu=11: 111100, 11: 111200,11: 111300,14: 141100, 14: 141200
        if (lingyuId.length() == 0) {
            tv_previewresume_territory.setText("");
            return;
        }
        StringBuffer showStringBuffer = new StringBuffer();// 要显示的文字
        // 过滤出当前行业下领域
        ArrayList<String> curIndustryLingyu = new ArrayList<String>();
        if(lingyuId.indexOf(",")!=-1) {
            String[] itemLingyu = lingyuId.split(",");
            for (String string : itemLingyu) {
                if (string.contains(MyUtils.industryId + ":")) {
                    curIndustryLingyu.add(string);// 11:111100
                }
            }
        }else{
            if (lingyuId.contains(MyUtils.industryId + ":")) {
                curIndustryLingyu.add(lingyuId);
            }
        }
        String industryID=null;
        if(curIndustryLingyu.size()!=0) {
            industryID = curIndustryLingyu.get(0).substring(0, curIndustryLingyu.get(0).indexOf(":"));
            if (industryID.equals(MyUtils.industryId)) {
                fragmentView.findViewById(R.id.rl_previewresume_territory).setVisibility(View.VISIBLE);
                for (String string : curIndustryLingyu) {
                    String id = string.replace(MyUtils.industryId + ":", "");
                    showStringBuffer.append(","
                            + ResumeInfoIDToString.getLingYuString(getActivity(),
                            MyUtils.industryId, id));
                }
            } else {
                fragmentView.findViewById(R.id.rl_previewresume_territory).setVisibility(View.GONE);
            }
        }

        if (showStringBuffer.toString().startsWith(",")) {// 去除首个“，”
            showStringBuffer.deleteCharAt(0);
        }
        if (showStringBuffer.toString().endsWith(",")) {// 去除末尾“，”
            showStringBuffer.deleteCharAt(showStringBuffer.length() - 1);
        }
        tv_previewresume_territory.setText(showStringBuffer.toString());
        // System.out.println("领域：" + showStringBuffer.toString());
    }

    /**
     * 基本信息
     */
    private void initBaseInfo() {

        if (resumeBaseInfo != null) {
            tv_previewresume_name.setText(resumeBaseInfo.getName());

            if ("1".equals(resumeBaseInfo.getSex())) {
                tv_previewresume_sex.setText("男");
            } else if ("2".equals(resumeBaseInfo.getSex())) {
                tv_previewresume_sex.setText("女");
            }
            imageLoader.displayImage(Constants.IMAGE_ROOTPATH + resumeBaseInfo.getPic_filekey(), iv_previewresume_head, options);
            // 年龄
            String yearString = resumeBaseInfo.getYear();
            String monthString = resumeBaseInfo.getMonth();
            String dayString = resumeBaseInfo.getDay();

            int curYear = calendar.get(Calendar.YEAR);
            int yearStringInt = 0;
            try {
                if (yearString != null && yearString.length() > 0) {
                    yearStringInt = Integer.parseInt(yearString.trim());
                }
                int ageInt = curYear - yearStringInt;
                if (isCHS) {// zh
                    if (yearString != null && yearString.length() > 0) {
                        tv_previewresume_birthday.setText(ageInt + "岁("
                                + yearString + "年" + monthString + "月"
                                + dayString + "日)");
                    }
                } else {// en
                    if (yearString != null && yearString.length() > 0) {
                        tv_previewresume_birthday.setText(ageInt
                                + " Years old(" + yearString + "-"
                                + monthString + "-" + dayString + ")");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //现居地

            //现居地区国家
            if (resumeBaseInfo.getNationality() != null) {
                tv_previewresume_nation.setText(ResumeInfoIDToString.getNation(getActivity(), resumeBaseInfo.getNationality(), isCHS));
            }
            if (resumeBaseInfo.getLocation() != null) {
                tv_previewresume_home.setText(ResumeInfoIDToString.getPlace(getActivity(), resumeBaseInfo.getLocation(), isCHS));
            }
            if (resumeBaseInfo.getWork_beginyear().equals("-1")) {
                tv_previewresume_beginjobtime.setText("无工作经验");
            } else {
                tv_previewresume_beginjobtime.setText(resumeBaseInfo.getWork_beginyear());
            }
            if (resumeBaseInfo.getPost_rank() != null) {
                tv_previewresume_postrank.setText(ResumeInfoIDToString.getZhiCheng(getActivity(), resumeBaseInfo.getPost_rank(), isCHS));
            }
            if (resumeBaseInfo.getYdphone() != null) {
                tv_previewresume_phonenum.setText(resumeBaseInfo.getYdphone());
            }
            if (resumeBaseInfo.getEmailaddress() != null) {
                tv_previewresume_email.setText(resumeBaseInfo.getEmailaddress());
            }
        }
    }

    private void initView() {
        tv_previewresume_back = (TextView) fragmentView.findViewById(R.id.tv_previewresume_back);
        iv_previewresume_head = (CircleImageView) fragmentView.findViewById(R.id.iv_previewresume_head);
//        iv_previewresume_sex = (ImageView) fragmentView.findViewById(R.id.iv_previewresume_sex);
        myresume_preview = (TextView) fragmentView.findViewById(R.id.myresume_preview);
        myresume_refresh = (TextView) fragmentView.findViewById(R.id.myresume_refresh);

        tv_previewresume_jobnum = (TextView) fragmentView.findViewById(R.id.tv_previewresume_jobnum);
        tv_previewresume_salary = (TextView) fragmentView.findViewById(R.id.tv_previewresume_salary);
        tv_previewresume_func = (TextView) fragmentView.findViewById(R.id.tv_previewresume_func);
        tv_previewresume_name = (TextView) fragmentView.findViewById(R.id.tv_previewresume_name);
        tv_previewresume_sex = (TextView) fragmentView.findViewById(R.id.tv_previewresume_sex);
        tv_myresume_selfassessment = (TextView) fragmentView.findViewById(R.id.tv_myresume_selfassessment);
        tv_previewresume_phonenum = (TextView) fragmentView.findViewById(R.id.tv_previewresume_phonenum);
        tv_previewresume_birthday = (TextView) fragmentView.findViewById(R.id.tv_previewresume_birthday);
        tv_previewresume_email = (TextView) fragmentView.findViewById(R.id.tv_previewresume_email);
        tv_previewresume_beginjobtime = (TextView) fragmentView.findViewById(R.id.tv_previewresume_beginjobtime);
        tv_previewresume_nation = (TextView) fragmentView.findViewById(R.id.tv_previewresume_nation);
        tv_previewresume_postrank = (TextView) fragmentView.findViewById(R.id.tv_previewresume_postrank);
        tv_previewresume_state = (TextView) fragmentView.findViewById(R.id.tv_previewresume_state);
        tv_previewresume_jobtype = (TextView) fragmentView.findViewById(R.id.tv_previewresume_jobtype);
        tv_previewresume_places = (TextView) fragmentView.findViewById(R.id.tv_previewresume_places);
        tv_previewresume_territory = (TextView) fragmentView.findViewById(R.id.tv_previewresume_territory);
        tv_previewresume_home = (TextView) fragmentView.findViewById(R.id.tv_previewresume_home);

        tv_myresumescan_persioninfo = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_persioninfo);
        tv_myresumescan_order = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_order);
//        tv_myresumescan_edu = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_edu);
//        tv_myresumescan_jobexp = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_jobexp);
//        tv_myresumescan_project = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_project);
//        tv_myresumescan_skill = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_skill);
//        tv_myresumescan_language = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_language);
//        tv_myresumescan_train = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_train);
        tv_myresumescan_self = (TextView) fragmentView.findViewById(R.id.tv_myresumescan_self);
        tv_compnum = (TextView) fragmentView.findViewById(R.id.tv_compnum);
        tv_myresume_open = (TextView) fragmentView.findViewById(R.id.tv_myresume_open);
        tv_myresume_baseinfo = (TextView) fragmentView.findViewById(R.id.tv_myresume_baseinfo);
        tv_myresume_upresume = (TextView) fragmentView.findViewById(R.id.tv_myresume_upresume);

        tv_gome = (TextView) fragmentView.findViewById(R.id.tv_gome);
        activity_resume_scan_workexperience_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_workexperience_linnearlayout);
        activity_resume_scan_educationexperience_linnearlayout = (LinearLayout) fragmentView.findViewById(R.id.activity_resume_scan_educationexperience_linnearlayout);

        fragmentView.findViewById(R.id.activity_resume_scan_educationexperience_linnearlayout);
        fragmentView.findViewById(R.id.activity_resume_scan_workexperience_linnearlayout);

        tv_previewresume_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseIndustriesActivity.class);
                startActivity(intent);
            }
        });
        tv_myresumescan_persioninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType("1");
            }
        });
        tv_myresumescan_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType("2");
            }
        });
//        tv_myresumescan_edu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("3");
//            }
//        });
//        tv_myresumescan_jobexp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("4");
//            }
//        tv_myresumescan_project.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("5");
//            }
//        });
//        tv_myresumescan_skill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("6");
//            }
//        });
//        tv_myresumescan_language.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("7");
//            }
//        });
//        tv_myresumescan_train.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseType("8");
//            }
//        });
        tv_myresumescan_self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType("9");
            }
        });
        tv_gome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instanceMain.openD();
            }
        });
        tv_myresume_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpen();
            }
        });
        /**
         * 屏蔽返回键
         */
        if (MyUtils.isLogin) {
            tv_previewresume_back.setVisibility(View.GONE);
        } else {
            tv_previewresume_back.setVisibility(View.VISIBLE);
        }

        myresume_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshNow();
                MobclickAgent.onEvent(getActivity(), " cv-refresh");
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
        myresume_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreviewResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("resumeId", resumeID);
                intent.putExtra("resumeLanguage", "zh");
                startActivity(intent);
            }
        });
        tv_myresume_baseinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResumeName();
            }
        });
        tv_myresume_upresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderUpResume = new BeautifulDialog.Builder(getActivity());
                builderUpResume.setMessage("确认要升级简历么？");
                builderUpResume.setTitle("提示");
                builderUpResume.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upResume();
                        dialog.dismiss();
                    }
                });
                builderUpResume.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderUpResume.create().show();
            }
        });
        iv_previewresume_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeFragment.meFragment.showPhotoDialog();
            }
        });
    }
    public void refreshNow() {
        isRefresh = true;
        isHaveAppResume = false;
        getData();
    }
    private BeautifulDialog.Builder builderUpResume;

    private void setOpen() {
        Async_MyResume_Open resumesDetail = new Async_MyResume_Open(getActivity());

        if (resumeTitle.getOpen().equals("0")) {
            resumesDetail.execute("2", resumeID);
        } else if (resumeTitle.getOpen().equals("2")) {
            resumesDetail.execute("0", resumeID);
        }
    }

    //防止加载不完整
    private boolean isOpen = true;

    private void isOpen() {
        //0公开2保密
        Drawable dra1 = getResources().getDrawable(R.mipmap.kaiguan_1);
        Drawable dra2 = getResources().getDrawable(R.mipmap.kaiguan_2);
        dra1.setBounds(0, 0, dra1.getMinimumWidth(), dra1.getMinimumHeight());
        dra2.setBounds(0, 0, dra2.getMinimumWidth(), dra2.getMinimumHeight());
        if (resumeTitle != null) {
            if (resumeTitle.getOpen().equals("0")) {
                //公开
                tv_myresume_open.setCompoundDrawablePadding(2);
                tv_myresume_open.setCompoundDrawables(dra2, null, null, null);
                tv_myresume_open.setText("公开");
            } else if (resumeTitle.getOpen().equals("2")) {
                //保密
                tv_myresume_open.setCompoundDrawablePadding(2);
                tv_myresume_open.setCompoundDrawables(dra1, null, null, null);
                tv_myresume_open.setText("保密");
            }
        } else if (isOpen) {
            isOpen = false;
            refreshNow();
        }
    }

    private PopupWindow setResumeNamePopWinow;
    private View viewSetResume;
    private String getCode = null;
    private SharedPreferencesUtils sharedPreferencedUtils;

    private void setResumeName() {
        viewSetResume = LayoutInflater.from(getActivity()).inflate(R.layout.item_setresume, null);
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        setResumeNamePopWinow = new PopupWindow(getActivity());
        setResumeNamePopWinow.setContentView(viewSetResume);
        setResumeNamePopWinow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setResumeNamePopWinow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setResumeNamePopWinow.setTouchable(true);
        setResumeNamePopWinow.setFocusable(true);
        setResumeNamePopWinow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        setResumeNamePopWinow.setOutsideTouchable(true);
        //设置弹出窗体需要软键盘，
        setResumeNamePopWinow.showAtLocation(viewSetResume, Gravity.CENTER, 0, 0);
        setResumeNamePopWinow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 设置弹窗外可点击，默认为false
        setResumeNamePopWinow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        RelativeLayout rl_item_setresume_confirm = (RelativeLayout) viewSetResume.findViewById(R.id.rl_item_setresume_confirm);
        final EditText et_myresume_resumename = (EditText) viewSetResume.findViewById(R.id.et_myresume_resumename);
        rl_item_setresume_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncSetResumeName asyncSetResumeName = new AsyncSetResumeName(getActivity());
                asyncSetResumeName.execute(resumeID, et_myresume_resumename.getText().toString());
            }
        });
    }

    public void closeStResumeNamePopWinow() {
        if (setResumeNamePopWinow.isShowing()) {
            setResumeNamePopWinow.dismiss();
            isRefresh = true;
            refreshResume();

        }
    }

    /**
     * 切换视图
     *
     * @param cls
     */
    private void goActivity(Class cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (resumeID != null) {
            intent.putExtra("resumeId", resumeID);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
    }

    public void chooseType(String goType) {

//        refreshItemInfoComplete();
//        updataComplete();
//
//        if (sUtils.getBooleanValue("isFirstResume" + MyUtils.userID + "resume" + resumeId, true)) {
//            sUtils.setBooleanValue("isFirstResume" + MyUtils.userID + "resume" + resumeId, false);
//            executeRefresh();
//        } else {
        if (goType != null) {
            switch (goType) {
                case "0":
                    break;
                case "1":
                    goActivity(ResumePersonInfoActivity.class);
                    break;
                case "2":
                    goActivity(ResumeJobOrderActivity.class);
                    break;
                case "3":
                    ResumeEducation resumeEducation = new ResumeEducation();
                    Intent intent = new Intent(getActivity(), ModifyEduActivity.class);
                    intent.putExtra("resumeId", resumeID);
                    intent.putExtra("resumeLanguage", "zh");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resumeEducation", resumeEducation);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case "4":
                    ResumeExperience resumeExperience = new ResumeExperience();
                    Intent intent4 = new Intent(getActivity(), ModifyExpActivity.class);
                    intent4.putExtra("resumeId", resumeID);
                    intent4.putExtra("resumeLanguage", "zh");
                    Bundle bundle4 = new Bundle();
                    bundle4.putSerializable("resumeExperience", resumeExperience);
                    intent4.putExtras(bundle4);
                    startActivity(intent4);
                    break;
                case "5":
                    ResumeProject resumeProject = new ResumeProject();
                    Intent intent5 = new Intent(getActivity(), ModifyProjectActivity.class);
                    intent5.putExtra("resumeId", resumeID);
                    intent5.putExtra("resumeLanguage", "zh");
                    Bundle bundle5 = new Bundle();
                    bundle5.putSerializable("resumeProject", resumeProject);
                    intent5.putExtras(bundle5);
                    startActivity(intent5);
                    break;
                case "6":
                    ResumeSkill resumeSkill = new ResumeSkill();
                    Intent intent6 = new Intent(getActivity(), ModifySkillActivity.class);
                    intent6.putExtra("resumeId", resumeID);
                    intent6.putExtra("resumeLanguage", "zh");
                    Bundle bundle6 = new Bundle();
                    bundle6.putSerializable("resumeSkill", resumeSkill);
                    intent6.putExtras(bundle6);
                    startActivity(intent6);
                    break;
                case "7":
                    ResumeLanguageLevel resumeLanguageLevel = new ResumeLanguageLevel();
                    Intent intent7 = new Intent(getActivity(), ModifyLanguageActivity.class);
                    intent7.putExtra("resumeId", resumeID);
                    intent7.putExtra("resumeLanguage", "zh");
                    Bundle bundle7 = new Bundle();
                    bundle7.putSerializable("resumeLanguageLevel", resumeLanguageLevel);
                    intent7.putExtras(bundle7);
                    startActivity(intent7);
                    break;
                case "8":
                    ResumePlant resumePlant = new ResumePlant();
                    Intent intent8 = new Intent(getActivity(), ModifyPlantActivity.class);
                    intent8.putExtra("resumeId", resumeID);
                    intent8.putExtra("resumeLanguage", "zh");
                    Bundle bundle8 = new Bundle();
                    bundle8.putSerializable("resumePlant", resumePlant);
                    intent8.putExtras(bundle8);
                    startActivity(intent8);
                    break;
                case "9":
                    goActivity(ResumeSelfAssessActivity.class);
                    break;
//                }
            }
        }

    }

    /**
     * 初始化UIL
     */
    private void initUIL() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.me_touxiang)   //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.me_touxiang)   //设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.me_touxiang)    //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)   //设置下载的图片是否缓存在内存中
                .cacheOnDisc(false)   //设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)   //设置图片以如何的缩放类型
                .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片质量——解码类型
                .resetViewBeforeLoading(true)   //设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(100))     //是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    /**
     * 职位
     */
    private void initOrderFunc() {
        StringBuffer showBuffer = new StringBuffer();
        if (isCHS) {
            String func = resumeOrder.getFunc();
            Log.i("期望职位",func);
            if (func.length() > 0) {
                String[] itemFuncStrings = func.split(",");// 所有行业信息
                ArrayList<String> curIndustryFuncArrayList = new ArrayList<String>();// 当前行业职能
                if (itemFuncStrings != null && itemFuncStrings.length > 0) {
                    for (int i = 0; i < itemFuncStrings.length; i++) {// 过滤出当前行业下的职能
                        if (itemFuncStrings[i].contains(MyUtils.industryId
                                + ":")) {
                            curIndustryFuncArrayList.add(itemFuncStrings[i]
                                    .replace(MyUtils.industryId + ":", ""));
                        }
                    }
                    if(curIndustryFuncArrayList.size()!=0) {
                        for (String string : curIndustryFuncArrayList) {
                            if (string.contains("|")) {// 例如：264101|10100
                                String[] funcAndZhixiStrings = string.split("\\|");
                                showBuffer.append(","
                                        + ResumeInfoIDToString.getFunc(getActivity(),
                                        MyUtils.industryId,
                                        funcAndZhixiStrings[0].trim())
                                        + ResumeInfoIDToString.getZhixiString(getActivity(),
                                        funcAndZhixiStrings[1].trim()));
                            } else {// 例如： 256101
                                showBuffer.append("," + ResumeInfoIDToString.getFunc(getActivity(), MyUtils.industryId, string.trim()));
                            }
                        }
                    }else{
                        showBuffer=new StringBuffer();
                    }

                }
            }
            if (showBuffer.toString().length() > 0) {// 显示数据
                tv_previewresume_func.setText(showBuffer.substring(1));

            }else{
                tv_previewresume_func.setText("");
            }
        } else {
            tv_previewresume_func.setText(resumeOrder.getJobname());
        }
    }

    /**
     * 初始化工作地区
     *
     * @param placeIdString
     */
    private void initWorkPlace(String placeIdString, TextView textView) {
        if (placeIdString == null || placeIdString.length() == 0) {// 选择的城市为空
            textView.setText("");
            return;
        }
        JSONArray cityJSONArray = null;
        try {
            if (MyUtils.USE_ONLINE_ARRAY && isCHS) {
                cityJSONArray = NetService.getCityAsJSONArray(getActivity(), "city.json");
            } else {
                if (isCHS) {
                    InputStream inputStream = getActivity().getAssets().open("city_zh.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                } else {
                    InputStream inputStream = getActivity().getAssets().open("city_en.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                }
            }
            StringBuffer showText = new StringBuffer();
            if (placeIdString != null && placeIdString.length() > 0) {
                String[] placeStrings = placeIdString.split(",");
                for (String string : placeStrings) {
                    for (int i = 0; i < cityJSONArray.length(); i++) {
                        JSONObject object = cityJSONArray.getJSONObject(i);
                        if (object.has(string)) {
                            showText.append("," + object.getString(string));
                            break;
                        }
                    }
                }
                if (showText.length() > 0) {
                    showText.deleteCharAt(0);
                }
                textView.setText(showText.toString());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 网络简历列表信息


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
                                // 1.获取线上json信息
                                JSONArray baseinfoJsonArray = jsonObject.getJSONArray("base_info");
                                JSONArray languageinfoJsonArray = jsonObject.getJSONArray("language_list");
                                JSONArray resumeArray = jsonObject.getJSONArray("resume_list");
                                listResumeJsonString = resumeArray.toString();

                                // 2.解析json信息
                                ResumeListStringJsonParser parser = new ResumeListStringJsonParser();
                                listBaseInfos = parser.getBaseInfos(baseinfoJsonArray);
                                listLanguageLevels = parser.getLanguageLevels(languageinfoJsonArray);
                                listResumeLists = parser.getResumeLists(resumeArray);
                                // 3.简历对比
                                DAO_DBOperator dbOperator = new DAO_DBOperator(context);
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
                                    ArrayList<ResumeLanguageLevel> languageLevels = new ArrayList<ResumeLanguageLevel>();
                                    for (ResumeLanguageLevel resumeLanguageLevel : listLanguageLevels) {
                                        languageLevels.add(resumeLanguageLevel);
                                    }
                                    if (languageLevels.size() > 0) {
                                        long resultInsert = dbOperator.Insert_ResumeLanguageLevel(languageLevels);
                                    }
                                } else {// 中文个人信息存在，比价时间戳
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
                    initIsApp();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
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
            listResume = new ArrayList<ResumeList>();
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
     * 第一次获取
     */
    private int firstGo = 1;
    private ResumeComplete resumeComplete;
    private boolean isCHS = true;
    private ResumeBaseInfo baseInfoZh, baseInfoEn;
    private int stateNum = 0;
    private static String resumeType = null;// 简历类型
    /**
     * 有无工作经验
     */
    boolean hasExperience;
    private boolean isHaveAppResume;
    private String listResumeJsonString = "";
    private String userId;
    private boolean canUpdate = false;

    /**
     * 初始化App简历
     */
    private void initIsApp() {
        /**
         * 先判断本地有没有app简历
         */
        //保证唯一性
//        if (firstGo == 1) {
        userId = MyUtils.userID;
//            firstGo = 2;
//        }
        listResumeIsApp = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(listResumeJsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ResumeList resumeList = new ResumeList();
                resumeID = jsonObject1.getString("resume_id");
                resumeType = jsonObject1.getString("resume_type");
                resumeList.setResume_id(resumeID);
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
            MainActivity.instanceMain.createNewResume();
            return;
        }
        // 网络获取的有app简历
        try {
            JSONArray jsonArray2 = new JSONArray(listResumeJsonString);
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonObject = jsonArray2.getJSONObject(i);
                if (jsonObject.getString("important").equals("1")) {
                    resumeID = jsonObject.getString("resume_id");
                    resumeType = jsonObject.getString("resume_type");
                    sUtils.setStringValue("is_app_resumeid" + userId, resumeID);
                    /**
                     * 保存的是简历ID
                     */
                    isHaveAppResume = true;
                    if (isRefresh) {
                        isRefresh = false;
                        refreshResume();
                    } else {
                        upDateUI();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //网络没有
        if (!isHaveAppResume&&listResumeIsApp.size()>1) {
            chooseIsApp();
        }else{
            if(Integer.parseInt(listResumeIsApp.get(0).getFill_scale())>=60&&isRefresh==true){
                sUtils.setStringValue("is_app_resumeid" + resumeID, listResumeIsApp.get(0).getResume_id());
                resumeID = listResumeIsApp.get(0).getResume_id();
                resumeType = listResumeIsApp.get(0).getResume_type();
                sendIsApp();
            }
            isRefresh = false;
            refreshResume();
        }
        //本地有
    }

    /**
     * 刷新简历
     */
    private void refreshResume() {
        AsyncResumeCenterGetResumesDetail resumesDetail = new AsyncResumeCenterGetResumesDetail(getActivity(), handlerRefreshResume, resumeID, "zh");
        resumesDetail.execute();
    }

    /**
     * 从网络 获取数据后返回的结果
     */
    private Handler handlerRefreshResume = new Handler() {
        public void handleMessage(Message msg) {
            int msgInt = msg.arg1;
            resumeTitle = dbOperator.query_ResumeTitle_info(resumeID, "zh");
            Log.i("刷新的标题",resumeTitle+"");
            canUpdate = true;
//            getData();
            upDateUI();
        }
    };
    /**
     * 记录listResume中 app简历位置
     */
    private int isAppPosition;
    private PopupWindow popwindowIsAPPResume, popwindowMoreSetting;
    private View viewPopIsApp;

    /**
     * 选择app简历（无问题）
     */
    private void chooseIsApp() {
        viewPopIsApp = LayoutInflater.from(getActivity()).inflate(R.layout.item_popupwindow_isappresume, null);
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        popwindowIsAPPResume = new PopupWindow(getActivity());
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
        RecyclerView lv_item_popupwindow_appresume = (RecyclerView) viewPopIsApp.findViewById(R.id.lv_item_popupwindow_appresume);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_item_popupwindow_appresume.setLayoutManager(linearLayoutManager);
        RelativeLayout rl__item_popupwindow_appresume_save = (RelativeLayout) viewPopIsApp.findViewById(R.id.rl__item_popupwindow_appresume_save);
        final MyResumeJobAdapter resumeIsAppAdapter = new MyResumeJobAdapter(getActivity(), listResumeIsApp);
        resumeIsAppAdapter.setSelectedPosition(0);
        isAppPosition = 0;
        lv_item_popupwindow_appresume.setAdapter(resumeIsAppAdapter);
        resumeIsAppAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void ItemClick(View view, int position) {
                resumeIsAppAdapter.setSelectedPosition(position);
                isAppPosition = position;
                resumeIsAppAdapter.notifyDataSetChanged();
            }
        });
        rl__item_popupwindow_appresume_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fillScallInt = Integer.parseInt(listResumeIsApp.get(isAppPosition).getFill_scale());
                sUtils.setStringValue("is_app_resumeid" + resumeID, listResumeIsApp.get(isAppPosition).getResume_id());
                resumeID = listResumeIsApp.get(isAppPosition).getResume_id();
                resumeType = listResumeIsApp.get(isAppPosition).getResume_type();
                sendIsApp();
            }
        });
    }

    /**
     * 向服务器发送 修改默认简历
     */
    private void sendIsApp() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.setimportant");
            requestParams.put("resume_id", resumeID);
            requestParams.put("resume_language", "zh");
            requestParams.put("important", "1");
            NetService service = new NetService(getActivity(), handlerIsApp);
            service.execute(requestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handlerIsApp = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                            sUtils.setStringValue("is_app_resumeid" + userId, resumeID);
                            MeFragment.meFragment.execute();
                            getData();
                            popwindowIsAPPResume.dismiss();
                            break;
                        case 404:
                            if(listResumeIsApp.size()==1){
                                sUtils.setStringValue("is_app_resumeid" + userId, resumeID);
                                MeFragment.meFragment.execute();
                                getData();
                                popwindowIsAPPResume.dismiss();
                            }else {
                                builderUpResume = new BeautifulDialog.Builder(getActivity());
                                builderUpResume.setMessage("完整度低于60%不能设置为默认简历，请您选择完整度达标的简历，或者访问m.800hr.com完善简历后再来设置。");
                                builderUpResume.setTitle("提示");
                                builderUpResume.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builderUpResume.create().show();
                            }
                            break;
                        default:
                            Toast.makeText(getActivity(), Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "设置失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void addView(ViewGroup layout, String nameStr, String someStr, final String type) {
        TextView tv_add_myresume_name, tv_add_myresume_some;
        LinearLayout lr_myresume_add;
        View myresumeAddView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_myresume, null);
        layout.addView(myresumeAddView);
        // initview
        tv_add_myresume_name = (TextView) myresumeAddView.findViewById(R.id.tv_add_myresume_name);
        tv_add_myresume_some = (TextView) myresumeAddView.findViewById(R.id.tv_add_myresume_some);
        lr_myresume_add = (LinearLayout) myresumeAddView.findViewById(R.id.lr_myresume_add);
        tv_add_myresume_name.setText(nameStr);
        tv_add_myresume_some.setText(someStr);
        lr_myresume_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(type);
            }
        });
    }

    private void addViewAdd(ViewGroup layout, String someStr, final String type) {
        TextView tv_add_myresume_some;
        LinearLayout lr_myresume_add;
        View myresumeAddView = LayoutInflater.from(getActivity()).inflate(R.layout.item_add_myresume_add, null);
        layout.addView(myresumeAddView);
        // initview
        tv_add_myresume_some = (TextView) myresumeAddView.findViewById(R.id.tv_add_myresume_some);
        lr_myresume_add = (LinearLayout) myresumeAddView.findViewById(R.id.lr_myresume_add);
        tv_add_myresume_some.setText(someStr);
        lr_myresume_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseType(type);
            }
        });
    }
}
