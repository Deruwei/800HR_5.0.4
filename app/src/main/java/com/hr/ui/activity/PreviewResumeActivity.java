package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.model.ResumeAssessInfo;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 简历预览页面
 */
public class PreviewResumeActivity extends BaseActivity {

    private static final String TAG = "PreviewResumeActivity";
    private Context mContext = PreviewResumeActivity.this;
    /**
     * SharedPreference工具类
     */
    private RelativeLayout rl_territory;
    private SharedPreferencesUtils sUtils;
    private ImageView iv_previewresume_back, iv_previewresume_head;
    private TextView tv_previewresume_resumename, tv_previewresume_sex, tv_previewresume_home, tv_resume_previewresume_desp, tv_previewresume_phonenum, tv_previewresume_birthday, tv_previewresume_state, tv_previewresume_func, tv_previewresume_places,
            tv_previewresume_email, tv_previewresume_beginjobtime, tv_previewresume_nation, tv_previewresume_postrank, tv_previewresume_salary,
            tv_previewresume_jobtype, tv_previewresume_jobnum, tv_previewresume_name, tv_previewresume_edutime, tv_previewresume_territory, tv_previewresume_majordes,
            tv_previewresume_eduname, tv_previewresume_major, tv_previewresume_jobtime, tv_myresume_selfassessment, tv_previewresume_comname, tv_previewresume_post, resume_previewresume_jobexpsalary, resume_previewresume_jobexppost, resume_previewresume_jobdesp;
    /**
     * 每个选项中的编辑按钮
     */
    private TextView tv_previewEductionResume_edit,tv_previewShixiExp_edit,tv_previewFindJobResume_edit,tv_previewPreExp_edit,tv_previewProfessionSkill_edit,tv_previewTrainingExp_edit,tv_previewLanguageSkill_edit;
    /**
     * 项目经验
     */
    private TextView tv_previewresume_projectname, tv_previewresume_projectpost, tv_previewresume_projectdesp, tv_previewresume_projectduty, tv_previewresume_projecttime;
    /**
     * 专业技能
     */
    private TextView tv_previewresume_skillname, tv_previewresume_skilltime, tv_previewresume_skilllevel;
    /**
     * 培训经历
     */
    private TextView tv_previewresume_traincertificate, tv_previewresume_trainplace, tv_previewresume_trainname, tv_previewresume_traincourse, tv_previewresume_traindesp, tv_previewresume_traintime;
    /**
     * 语言能力
     */
    private TextView tv_previewresume_languagename, tv_previewresume_languagespeaklevel, tv_previewresume_languagereadlevel;
    private LinearLayout activity_resume_scan_language_linnearlayout, activity_resume_scan_train_linnearlayout, activity_resume_scan_skill_linnearlayout, activity_resume_scan_workexperience_linnearlayout, activity_resume_scan_educationexperience_linnearlayout, activity_resume_scan_projectexp_linnearlayout;

    private RelativeLayout rl_preview_resume_jobexp;
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
    private boolean isCHS;
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
    private RefleshDialogUtils dialogUtils;

//    private TextView tv_myresumescan_persioninfo, tv_myresumescan_order, tv_myresumescan_edu, tv_myresumescan_jobexp, tv_myresumescan_project, tv_myresumescan_skill, tv_myresumescan_language, tv_myresumescan_train, tv_myresumescan_self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_resume);
        MobclickAgent.onEvent(this, "cv-preview");
        dialogUtils=new RefleshDialogUtils(this);
//        initData();
//        initUIL();
//        upDateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        initUIL();
        upDateUI();
    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
//        if (!isCHS) {
//            ((TextView) findViewById(R.id.Text_nowPage)).setText("Preview");
//        }
        calendar = Calendar.getInstance();
        dbOperator = new DAO_DBOperator(this);
//        new AsyncInitData().execute();
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
        resumeBaseInfo = dbOperator.query_ResumePersonInfo_Toone(resumelanguage);
        resumeTitle = dbOperator.query_ResumeTitle_info(resumeid, resumelanguage);
        resumeOrder = dbOperator.query_ResumeCareerObjective_Toone(resumeid, resumelanguage);
        resumeAssessInfo = dbOperator.query_ResumeTome_Toone(resumeid, resumelanguage);
        resumeExperiences = dbOperator.query_ResumeWorkExperience(resumeid, resumelanguage);
        resumeEducations = dbOperator.query_ResumeEducation(resumeid, resumelanguage);
        resumePlants = dbOperator.query_ResumeTraining(resumeid, resumelanguage);
        resumeLanguageLevels = dbOperator.query_ResumeLanguageLevel();
        // System.out.println("语言：" + resumeLanguageLevels.length);
        resumeSkills = dbOperator.query_ResumeSkill(resumeid, resumelanguage);
        resumeProjects = dbOperator.query_Resumeitem(resumeid, resumelanguage);
    }

    /**
     * 更新UI
     */
    private void upDateUI() {
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
    }

    /**
     * 培训经历
     */
    private void initPlant() {
        activity_resume_scan_train_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_train_linnearlayout);
        if (resumePlants != null) {
            activity_resume_scan_train_linnearlayout.removeAllViews();
            for (int i = 0; i < resumePlants.length; i++) {
                View plantView = LayoutInflater.from(this).inflate(
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
                tv_previewTrainingExp_edit= (TextView) plantView.findViewById(R.id.tv_myresume_train_modify);
                tv_previewTrainingExp_edit.setVisibility(View.GONE);
                // setdata
                // ----lable
                if (!isCHS) {
//                    TextView resume_scan_plant_title = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_title);
//                    TextView resume_scan_plant_lable_time = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_time);
//                    TextView resume_scan_plant_lable_organization = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_organization);
//                    TextView resume_scan_plant_lable_lession = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_lession);
//                    TextView resume_scan_plant_lable_descrip = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_descrip);
//                    TextView resume_scan_plant_lable_certificate = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_certificate);
//                    TextView resume_scan_plant_lable_place = (TextView) plantView
//                            .findViewById(R.id.resume_scan_plant_lable_place);
//                    resume_scan_plant_title.setText("Training");
//                    resume_scan_plant_lable_time.setText("Time");
//                    resume_scan_plant_lable_organization.setText("Institution");
//                    resume_scan_plant_lable_lession.setText("Course");
//                    resume_scan_plant_lable_descrip.setText("Description");
//                    resume_scan_plant_lable_certificate.setText("Certificate");
//                    resume_scan_plant_lable_place.setText("Location");
                }

                // ----data
                if (isCHS) {
//                    if (i != 0)
//                        resume_scan_plant_title.setText("培训经历" + (i + 1));
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
//                    if (i != 0)
//                        resume_scan_plant_title.setText("Training" + (i + 1));
//                    else {
//                        resume_scan_plant_title.setText("Training");
//                    }
//                    String startTime = resumePlants[i].getFromyear() + "-"
//                            + resumePlants[i].getFrommonth();
//                    String endTime = resumePlants[i].getToyear() + "-"
//                            + resumePlants[i].getTomonth();
//                    if ("0-0".equals(startTime)) {
//                        startTime = "present";
//                    }
//                    if ("0-0".equals(endTime)) {
//                        endTime = "present";
//                    }
//                    tv_previewresume_traintime.setText(startTime + "~"
//                            + endTime);
                }
                tv_previewresume_trainname
                        .setText(resumePlants[i].getInstitution());
                tv_previewresume_traincourse.setText(resumePlants[i]
                        .getCourse());
                tv_previewresume_traindesp.setText(resumePlants[i]
                        .getTraindetail());
                tv_previewresume_trainplace.setText(ResumeInfoIDToString
                        .getPlace(this, resumePlants[i].getPlace(), isCHS));
                tv_previewresume_traincertificate
                        .setText(resumePlants[i].getCertification());
            }
        }
    }

    /**
     * 专业技能
     */
    private void initSkill() {
        activity_resume_scan_skill_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_skill_linnearlayout);
        if (resumeSkills != null) {
            activity_resume_scan_skill_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeSkills.length; i++) {
                View skillView = LayoutInflater.from(this).inflate(
                        R.layout.item_resume_skill_preview, null);
                activity_resume_scan_skill_linnearlayout.addView(skillView);
                // initview
                tv_previewresume_skillname = (TextView) skillView
                        .findViewById(R.id.tv_previewresume_skillname);
                tv_previewresume_skilllevel = (TextView) skillView
                        .findViewById(R.id.tv_previewresume_skilllevel);
                tv_previewresume_skilltime = (TextView) skillView
                        .findViewById(R.id.tv_previewresume_skilltime);
                tv_previewProfessionSkill_edit= (TextView) skillView.findViewById(R.id.tv_myresume_skill_modify);
                tv_previewProfessionSkill_edit.setVisibility(View.GONE);
                // setdata
                // ---lable
                if (isCHS) {
                } else {
//                    TextView resume_scan_skill_title = (TextView) skillView
//                            .findViewById(R.id.resume_scan_skill_title);
//                    TextView resume_scan_skill_lable_major = (TextView) skillView
//                            .findViewById(R.id.resume_scan_skill_lable_major);
//                    TextView resume_scan_skill_lable_usertime = (TextView) skillView
//                            .findViewById(R.id.resume_scan_skill_lable_usertime);
//                    TextView resume_scan_skill_lable_level = (TextView) skillView
//                            .findViewById(R.id.resume_scan_skill_lable_level);
//                    resume_scan_skill_title.setText("Skill");
//                    resume_scan_skill_lable_major.setText("Skill Name");
//                    resume_scan_skill_lable_usertime.setText("Experience");
//                    resume_scan_skill_lable_level.setText("Level");
                }
                tv_previewresume_skillname.setText(resumeSkills[i]
                        .getSkilltitle());
                tv_previewresume_skilltime.setText(resumeSkills[i]
                        .getUsetime() + (isCHS == true ? "个月" : "months"));
                tv_previewresume_skilllevel.setText(ResumeInfoIDToString
                        .getSkillLevel(this, resumeSkills[i].getAbility(),
                                isCHS));
            }
        }
    }

    /**
     * 语言能力
     */
    private void initLanguage() {
        activity_resume_scan_language_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_language_linnearlayout);
        if (resumeLanguageLevels != null) {
            activity_resume_scan_language_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeLanguageLevels.length; i++) {
                View languagelevelView = LayoutInflater.from(this).inflate(
                        R.layout.item_resume_language_preview, null);
                activity_resume_scan_language_linnearlayout
                        .addView(languagelevelView);
                // initview
                tv_previewresume_languagename = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagename);
                tv_previewresume_languagespeaklevel = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagespeaklevel);
                tv_previewresume_languagereadlevel = (TextView) languagelevelView
                        .findViewById(R.id.tv_previewresume_languagereadlevel);
                tv_previewLanguageSkill_edit= (TextView) languagelevelView.findViewById(R.id.tv_myresume_langue_modify);
                tv_previewLanguageSkill_edit.setVisibility(View.GONE);
                // setdata
                // ---lable
                if (isCHS) {
                } else {
//                    TextView resume_scan_language_title = (TextView) languagelevelView.findViewById(R.id.resume_scan_language_title);
//                    TextView resume_scan_language_lable_type = (TextView) languagelevelView
//                            .findViewById(R.id.resume_scan_language_lable_type);
//                    TextView resume_scan_language_lable_readwrite = (TextView) languagelevelView
//                            .findViewById(R.id.resume_scan_language_lable_readwrite);
//                    TextView resume_scan_language_lable_listenspeak = (TextView) languagelevelView
//                            .findViewById(R.id.resume_scan_language_lable_listenspeak);
//                    resume_scan_language_title.setText("Language");
//                    resume_scan_language_lable_type.setText("Language");
//                    resume_scan_language_lable_readwrite.setText("Oral");
//                    resume_scan_language_lable_listenspeak
//                            .setText("Read&Write");
                }
                // ---data
//                if (isCHS) {
//                    if (i != 0)
//                        resume_scan_language_title.setText("语言能力" + (i + 1));
//                } else {
//                    if (i != 0)
//                        resume_scan_language_title
//                                .setText("Language" + (i + 1));
//                    else {
//                        resume_scan_language_title.setText("Language");
//                    }
//                }
                tv_previewresume_languagename.setText(ResumeInfoIDToString.getLanguageTpye(this,
                        resumeLanguageLevels[i].getLangname(), isCHS));
                tv_previewresume_languagereadlevel.setText(ResumeInfoIDToString.getLanguageReadLevel(this,
                        resumeLanguageLevels[i].getRead_level(), isCHS));
                tv_previewresume_languagespeaklevel.setText(ResumeInfoIDToString.getLanguageSpeakLevel(this,
                        resumeLanguageLevels[i].getSpeak_level(), isCHS));
            }
        }
    }

    /**
     * 自我评价
     */
    private void initAssess() {
        if (resumeAssessInfo != null) {
            tv_myresume_selfassessment.setText(resumeAssessInfo
                    .getIntroduction());
        }
    }

    /**
     * 项目经验
     */
    private void initProject() {
        activity_resume_scan_projectexp_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_projectexp_linnearlayout);
        if (resumeProjects != null) {
            activity_resume_scan_projectexp_linnearlayout.removeAllViews();
            for (int i = 0; i < resumeProjects.length; i++) {
                View projectView = LayoutInflater.from(this).inflate(
                        R.layout.item_resume_projectexp_preview, null);
                activity_resume_scan_projectexp_linnearlayout.addView(projectView);
                // initview
                tv_previewresume_projectname = (TextView) projectView.findViewById(R.id.tv_previewresume_projectname);
                tv_previewresume_projectpost = (TextView) projectView.findViewById(R.id.tv_previewresume_trainname);
                tv_previewresume_projectdesp = (TextView) projectView.findViewById(R.id.tv_previewresume_projectdesp);
                tv_previewresume_projectduty = (TextView) projectView.findViewById(R.id.tv_previewresume_projectduty);
                tv_previewresume_projecttime = (TextView) projectView.findViewById(R.id.tv_previewresume_traintime);
                tv_previewPreExp_edit= (TextView) projectView.findViewById(R.id.tv_myresume_project_modify);
                tv_previewPreExp_edit.setVisibility(View.GONE);
                // setdata

                // ---lable
//                if (isCHS) {
//                } else {
//                    TextView resume_scan_project_title = (TextView) projectView
//                            .findViewById(R.id.resume_scan_project_title);
//                    TextView resume_scan_project_lable_projecttime = (TextView) projectView
//                            .findViewById(R.id.resume_scan_project_lable_projecttime);
//                    TextView resume_scan_project_lable_projectname = (TextView) projectView
//                            .findViewById(R.id.resume_scan_project_lable_projectname);
//                    TextView resume_scan_project_lable_projectdesc = (TextView) projectView
//                            .findViewById(R.id.resume_scan_project_lable_projectdesc);
//                    TextView resume_scan_project_lable_responsibility = (TextView) projectView
//                            .findViewById(R.id.resume_scan_project_lable_responsibility);
//                    resume_scan_project_title.setText("Project Experience");
//                    resume_scan_project_lable_projecttime.setText("Time");
//                    resume_scan_project_lable_projectname.setText("Project");
//                    resume_scan_project_lable_projectdesc
//                            .setText("Description");
//                    resume_scan_project_lable_responsibility.setText("Duty");
//                }
                // ---data
                if (isCHS) {
                    if (i != 0) {
//                        resume_scan_project_title.setText("项目经验" + (i + 1));
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
//                    if (i != 0)
//                        resume_scan_project_title.setText("Project" + (i + 1));
//                    else {
//                        resume_scan_project_title.setText("Project");
//                    }
//                    String startTime = resumeProjects[i].getFromyear() + "-"
//                            + resumeProjects[i].getFrommonth();
//                    String endTime = resumeProjects[i].getToyear() + "-"
//                            + resumeProjects[i].getTomonth();
//                    if ("0-0".equals(startTime)) {
//                        startTime = "present";
//                    }
//                    if ("0-0".equals(endTime)) {
//                        endTime = "present";
//                    }
//                    activity_resume_scan_project_time.setText(startTime + "~"
//                            + endTime);
                }
                tv_previewresume_projectname.setText(resumeProjects[i]
                        .getProjectname());
                tv_previewresume_projectdesp.setText(resumeProjects[i]
                        .getProjectdesc());
                tv_previewresume_projectpost.setText(resumeProjects[i]
                        .getPosition());
                tv_previewresume_projectduty
                        .setText(resumeProjects[i].getResponsibility());
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
                //去掉编辑tv_myresume_edu_modify
                View educationExperienceView = LayoutInflater.from(this).inflate(R.layout.item_resume_educationexperience, null);
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
                tv_previewEductionResume_edit= (TextView) educationExperienceView.findViewById(R.id.tv_myresume_edu_modify);
                tv_previewEductionResume_edit.setVisibility(View.GONE);
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
                    tv_previewresume_edutime.setText(startTime + "--"
                            + endTime);
                }
                tv_previewresume_eduname.setText(resumeEducations[i].getSchoolname());
                tv_previewresume_major.setText(resumeEducations[i].getMoremajor());
                tv_previewresume_majordes.setText(" " + ResumeInfoIDToString.getEducationDegree(mContext, resumeEducations[i].getDegree(), isCHS));
                tv_resume_previewresume_desp.setText(resumeEducations[i].getEdudetail());
            }
        }
    }

    /**
     * 工作经验
     */
    private void initWorkExperience() {
        rl_preview_resume_jobexp = (RelativeLayout) findViewById(R.id.rl_preview_resume_jobexp);
        TextView resume_previewresume_shixi = (TextView) findViewById(R.id.resume_previewresume_shixi);
        if (resumeTitle != null) {
            if (resumeTitle.getResume_type().equals("1")) {
                resume_previewresume_shixi.setText("工作经验");
                if (resumeExperiences != null) {
                    activity_resume_scan_workexperience_linnearlayout.removeAllViews();
                    for (int i = 0; i < resumeExperiences.length; i++) {
//                        Toast.makeText(this,i+"",Toast.LENGTH_SHORT).show();
                        View workExperienceView = LayoutInflater.from(this).inflate(R.layout.item_resume_scan_workexperience, null);
                        activity_resume_scan_workexperience_linnearlayout.addView(workExperienceView);
                        // initview
                        tv_previewresume_jobtime = (TextView) workExperienceView
                                .findViewById(R.id.tv_previewresume_jobtime);
                        tv_previewresume_comname = (TextView) workExperienceView
                                .findViewById(R.id.tv_previewresume_comname);
                        //tv_myresume_workecperience_modify
                        tv_previewFindJobResume_edit= (TextView) workExperienceView.findViewById(R.id.tv_myresume_workecperience_modify);
                        tv_previewFindJobResume_edit.setVisibility(View.GONE);
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
                            String startTime = resumeExperiences[i].getFromyear() + "年"
                                    + resumeExperiences[i].getFrommonth() + "月";
                            String endTime = resumeExperiences[i].getToyear() + "年"
                                    + resumeExperiences[i].getTomonth() + "月";
                            if ("0年0月".equals(startTime)) {
                                startTime = "至今";
                            }
                            if ("0年0月".equals(endTime)) {
                                endTime = "至今";
                            }
                            tv_previewresume_jobtime.setText(startTime + "--"
                                    + endTime);
                        }
                        tv_previewresume_comname.setText(resumeExperiences[i].getCompany());
                        tv_previewresume_post.setText(resumeExperiences[i].getPosition());
                        resume_previewresume_jobexpsalary.setText(resumeExperiences[i].getSalary());
                        resume_previewresume_jobdesp.setText(resumeExperiences[i].getResponsibility());
                    }
                }
            } else if (resumeTitle.getResume_type().equals("2")) {
                resume_previewresume_shixi.setText("实习经验");
                activity_resume_scan_workexperience_linnearlayout.removeAllViews();
                for (int i = 0; i < resumeExperiences.length; i++) {
//                        Toast.makeText(this,i+"",Toast.LENGTH_SHORT).show();
                    View workExperienceView = LayoutInflater.from(this).inflate(R.layout.item_resume_scan_workexperience, null);
                    activity_resume_scan_workexperience_linnearlayout.addView(workExperienceView);
                    // initview
                    tv_previewShixiExp_edit= (TextView) workExperienceView.findViewById(R.id.tv_myresume_workecperience_modify);
                    tv_previewShixiExp_edit.setVisibility(View.GONE);
                    tv_previewresume_jobtime = (TextView) workExperienceView.findViewById(R.id.tv_previewresume_jobtime);
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
                }
            } else {
                rl_preview_resume_jobexp.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 求职意向
     */
    private void initOrder() {
        if (resumeOrder != null) {
            // ----data
            tv_previewresume_jobtype.setText(ResumeInfoIDToString
                    .getWorkType(this, resumeOrder.getJobtype(), isCHS));
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
                tv_previewresume_state.setText(ResumeInfoIDToString.getCurrentState(this, resumeBaseInfo.getCurrent_workstate(), isCHS));
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
        String[] itemLingyu = lingyuId.split(",");
        for (String string : itemLingyu) {
            if (string.contains(MyUtils.industryId + ":")) {
                curIndustryLingyu.add(string);// 11:111100
            }
        }

        if ("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId)
                || "14".equals(MyUtils.industryId)
                || "29".equals(MyUtils.industryId) || "22".equals(MyUtils.industryId)) {
            findViewById(R.id.rl_previewresume_territory).setVisibility(View.VISIBLE);
            for (String string : curIndustryLingyu) {
                String id = string.replace(MyUtils.industryId + ":", "");
                showStringBuffer.append(","
                        + ResumeInfoIDToString.getLingYuString(this,
                        MyUtils.industryId, id));
            }
        } else {
            findViewById(R.id.rl_previewresume_territory).setVisibility(View.GONE);
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
            if (resumeTitle != null) {
                tv_previewresume_resumename.setText(resumeTitle.getTitle());
            }
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
            tv_previewresume_nation.setText(ResumeInfoIDToString.getNation(this, resumeBaseInfo.getNationality(), isCHS));
            tv_previewresume_home.setText(ResumeInfoIDToString.getPlace(this, resumeBaseInfo.getLocation(), isCHS));
            if (resumeBaseInfo.getWork_beginyear().equals("-1")) {
                tv_previewresume_beginjobtime.setText("无工作经验");
            } else {
                tv_previewresume_beginjobtime.setText(resumeBaseInfo.getWork_beginyear());
            }
            tv_previewresume_postrank.setText(ResumeInfoIDToString.getZhiCheng(this, resumeBaseInfo.getPost_rank(), isCHS));
            tv_previewresume_phonenum.setText(resumeBaseInfo.getYdphone());
            tv_previewresume_email.setText(resumeBaseInfo.getEmailaddress());
        }
    }

    private void initView() {

        iv_previewresume_back = (ImageView) findViewById(R.id.iv_previewresume_back);
        iv_previewresume_head = (CircleImageView) findViewById(R.id.iv_previewresume_head);
        tv_previewresume_sex = (TextView) findViewById(R.id.tv_previewresume_sex);
        tv_previewresume_resumename = (TextView) findViewById(R.id.tv_previewresume_resumename);

        tv_previewresume_jobnum = (TextView) findViewById(R.id.tv_previewresume_jobnum);
        tv_previewresume_salary = (TextView) findViewById(R.id.tv_previewresume_salary);
        rl_territory= (RelativeLayout) findViewById(R.id.rl_previewresume_territory);
        tv_previewresume_func = (TextView) findViewById(R.id.tv_previewresume_func);
        tv_previewresume_name = (TextView) findViewById(R.id.tv_previewresume_name);
        tv_myresume_selfassessment = (TextView) findViewById(R.id.tv_myresume_selfassessment);
        tv_previewresume_phonenum = (TextView) findViewById(R.id.tv_previewresume_phonenum);
        tv_previewresume_birthday = (TextView) findViewById(R.id.tv_previewresume_birthday);
        tv_previewresume_email = (TextView) findViewById(R.id.tv_previewresume_email);
        tv_previewresume_beginjobtime = (TextView) findViewById(R.id.tv_previewresume_beginjobtime);
        tv_previewresume_nation = (TextView) findViewById(R.id.tv_previewresume_nation);
        tv_previewresume_postrank = (TextView) findViewById(R.id.tv_previewresume_postrank);
        tv_previewresume_state = (TextView) findViewById(R.id.tv_previewresume_state);
        tv_previewresume_jobtype = (TextView) findViewById(R.id.tv_previewresume_jobtype);
        tv_previewresume_places = (TextView) findViewById(R.id.tv_previewresume_places);
        tv_previewresume_territory = (TextView) findViewById(R.id.tv_previewresume_territory);
        tv_previewresume_home = (TextView) findViewById(R.id.tv_previewresume_home);
        if("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId) ||
                "14".equals(MyUtils.industryId) || "29".equals(MyUtils.industryId)||"22".equals(MyUtils.industryId)){
            rl_territory.setVisibility(View.VISIBLE);
        }else{
            rl_territory.setVisibility(View.GONE);
        }
//        tv_myresumescan_persioninfo = (TextView) findViewById(R.id.tv_myresumescan_persioninfo);
//        tv_myresumescan_order = (TextView) findViewById(R.id.tv_myresumescan_order);
//        tv_myresumescan_edu = (TextView) findViewById(R.id.tv_myresumescan_edu);
//        tv_myresumescan_jobexp = (TextView) findViewById(R.id.tv_myresumescan_jobexp);
//        tv_myresumescan_project = (TextView) findViewById(R.id.tv_myresumescan_project);
//        tv_myresumescan_skill = (TextView) findViewById(R.id.tv_myresumescan_skill);
//        tv_myresumescan_language = (TextView) findViewById(R.id.tv_myresumescan_language);
//        tv_myresumescan_train = (TextView) findViewById(R.id.tv_myresumescan_train);
//        tv_myresumescan_self = (TextView) findViewById(R.id.tv_myresumescan_self);
        activity_resume_scan_workexperience_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_workexperience_linnearlayout);
        activity_resume_scan_educationexperience_linnearlayout = (LinearLayout) findViewById(R.id.activity_resume_scan_educationexperience_linnearlayout);

        findViewById(R.id.activity_resume_scan_educationexperience_linnearlayout);
        findViewById(R.id.activity_resume_scan_workexperience_linnearlayout);
        iv_previewresume_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        tv_myresumescan_persioninfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("1");
//            }
//        });
//        tv_myresumescan_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("2");
//            }
//        });
//        tv_myresumescan_edu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("3");
//            }
//        });
//        tv_myresumescan_jobexp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("4");
//            }
//        });
//        tv_myresumescan_project.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("5");
//            }
//        });
//        tv_myresumescan_skill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("6");
//            }
//        });
//        tv_myresumescan_language.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("7");
//            }
//        });
//        tv_myresumescan_train.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("8");
//            }
//        });
//        tv_myresumescan_self.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MeFragment.meFragment.goMyResume("9");
//            }
//        });
    }

    /**
     * 切换视图
     *
     * @param cls
     */
    private void goActivity(Class cls) {
        Intent intent = new Intent(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (resumeID != null) {
            intent.putExtra("resumeId", resumeID);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
    }

    /**
     * 初始化UIL
     */
    private void initUIL() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.me_touxiang)   //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.me_touxiang)   //设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.me_touxiang)    //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)   //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)   //设置下载的图片是否缓存在SD卡中
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
        String func = resumeOrder.getFunc();
        StringBuffer showBuffer = new StringBuffer();
        if (isCHS) {
            func = resumeOrder.getFunc();
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
                    for (String string : curIndustryFuncArrayList) {
                        if (string.contains("|")) {// 例如：264101|10100
                            String[] funcAndZhixiStrings = string.split("\\|");
                            showBuffer.append(","
                                    + ResumeInfoIDToString.getFunc(this,
                                    MyUtils.industryId,
                                    funcAndZhixiStrings[0].trim())
                                    + ResumeInfoIDToString.getZhixiString(this,
                                    funcAndZhixiStrings[1].trim()));
                        } else {// 例如： 256101
                            showBuffer.append("," + ResumeInfoIDToString.getFunc(this, MyUtils.industryId, string.trim()));
                        }
                    }
                    if (showBuffer.toString().length() > 0) {// 显示数据
                        tv_previewresume_func.setText(showBuffer
                                .substring(1));
                    }
                }
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
                cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
            } else {
                if (isCHS) {
                    InputStream inputStream = getAssets().open("city_zh.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                } else {
                    InputStream inputStream = getAssets().open("city_en.json");
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
}
