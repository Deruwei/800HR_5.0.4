package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hr.ui.R;
import com.hr.ui.bean.LanguageLevelBean;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CustomDatePicker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLanguageActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifynewlangue_back)
    ImageView ivResumeModifynewlangueBack;
    @Bind(R.id.sp_resume_modifylanguage_langname)
    TextView spResumeModifylanguageLangname;
    @Bind(R.id.sp_resume_modifylanguage_speak_level)
    TextView spResumeModifylanguageSpeakLevel;
    @Bind(R.id.sp_resume_modifylanguage_read_level)
    TextView spResumeModifylanguageReadLevel;
    @Bind(R.id.tv_resume_item_languagemodify_delete)
    TextView tvResumeItemLanguagemodifyDelete;
    @Bind(R.id.tv_resume_item_llanguagemodify_save)
    TextView tvResumeItemLlanguagemodifySave;
    @Bind(R.id.rl_ModifyLanguage_TestLevel)
    RelativeLayout rlModifyLanguageTestLevel;
    @Bind(R.id.et_ModifyLanguage_TestNumber)
    EditText etModifyLanguageTestNumber;
    @Bind(R.id.ll_modifyLanguage_AddView)
    LinearLayout llModifyLanguageAddView;
    @Bind(R.id.tv_modifyLanguage_addView)
    TextView tvModifyLanguageAddView;
    @Bind(R.id.ll_modifyLanguageButton)
    LinearLayout llModifyLanguageButton;
    @Bind(R.id.tv_ModifyLanguage_TestType)
    TextView tvModifyLanguageTestType;
    @Bind(R.id.ll_testLevel)
    LinearLayout llTestLevel;
    private Context context = ModifyLanguageActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeLanguageLevel resumeLanguageLevel;
    private String resumeId, resumeLanguage;
    private String selectLauguageType, selectListenSkill, selectReadWriteSkill, selectTestLevelId;
    private String selectLanguageTypeFirst;
    private List<SelectBean> lauguageTypeList = new ArrayList<>();
    private List<SelectBean> listenSkillList = new ArrayList<>();
    private List<SelectBean> readWriteSkillList = new ArrayList<>();
    private List<SelectBean> testLevelList = new ArrayList<>();
    private CustomDatePicker datePickerLaugageType, datePickerListenSkill, datePickerReadWriteSkill, datePickerTestLevel;
    private int index = 0;
    private List<LanguageLevelBean.LanguageListBean.GradeExamBean> gradeExamBeanList=new ArrayList<>();
    private JSONArray jsonArray;
    private LanguageLevelBean levelbean;
    private List<EditText> etList=new ArrayList<>();
    private List<String> selectTestLevelList=new ArrayList<>();
    private Handler handlerService = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    finish();
                    MyResumeFragment.isRefresh = true;
                    MyUtils.canResumeReflesh = true;
                    MyUtils.canReflesh = true;
                    Toast.makeText(ModifyLanguageActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Handler handlerDelete = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    finish();
                    MyResumeFragment.isRefresh = true;
                    MyUtils.canResumeReflesh = true;
                    MyUtils.canReflesh = true;
                    Toast.makeText(ModifyLanguageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Handler handlerLanguageGet=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String data= (String) msg.obj;
                    Gson gson=new Gson();
                    levelbean=gson.fromJson(data,LanguageLevelBean.class);
                    initData();
                    break;
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
        setContentView(R.layout.activity_modify_language);
        ButterKnife.bind(this);
        getData();
        initDialog();
    }

    /**
     * 获取当前语言能力的数据
     */
    private void getData() {
        lauguageTypeList = GetResumeArrayList.getLaugageTypeListFromArray(this);
        listenSkillList = GetResumeArrayList.getListenSkillFromArray(this);
        readWriteSkillList = GetResumeArrayList.getReadWriteSkillFromArray(this);
        testLevelList = GetResumeArrayList.getTestLevelListFromArray(this);
        resumeLanguageLevel = (ResumeLanguageLevel) getIntent().getSerializableExtra("resumeLanguageLevel");
        if(resumeLanguageLevel.getLangname()!=null&&!"".equals(resumeLanguageLevel.getLangname())){
            llTestLevel.setVisibility(View.VISIBLE);
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.languageget");
            requestParams.put("langname", resumeLanguageLevel.getLangname());
            NetService service = new NetService(context, handlerLanguageGet);
            service.execute(requestParams);
        }else{
            llTestLevel.setVisibility(View.GONE);
        }
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        if (getIntent().getStringExtra("isAdd").equals("1")) {
            tvResumeItemLanguagemodifyDelete.setVisibility(View.GONE);
        }
    }

    private void initData() {
        //dbOperator = new DAO_DBOperator(context);
        if(levelbean!=null) {
            if (levelbean.getLanguage_list().get(0).getLangname() != null) {
                String languageId = levelbean.getLanguage_list().get(0).getLangname();
                selectLauguageType = languageId;
                selectLanguageTypeFirst=selectLauguageType;
                for (int i = 0; i < lauguageTypeList.size(); i++) {
                    if (languageId.equals(lauguageTypeList.get(i).getId())) {
                        spResumeModifylanguageLangname.setText(lauguageTypeList.get(i).getName());
                        break;
                    }
                }
            }
            if (levelbean.getLanguage_list().get(0).getRead_level() != null) {
                String readLevelId = levelbean.getLanguage_list().get(0).getRead_level();
                selectReadWriteSkill = readLevelId;
                for (int i = 0; i < readWriteSkillList.size(); i++) {
                    if (readLevelId.equals(readWriteSkillList.get(i).getId())) {
                        spResumeModifylanguageReadLevel.setText(readWriteSkillList.get(i).getName());
                        break;
                    }
                }
            }
            if (levelbean.getLanguage_list().get(0).getSpeak_level() != null) {
                String speakLevelId = levelbean.getLanguage_list().get(0).getSpeak_level();
                selectListenSkill = speakLevelId;
                for (int i = 0; i < listenSkillList.size(); i++) {
                    if (speakLevelId.equals(listenSkillList.get(i).getId())) {
                        spResumeModifylanguageSpeakLevel.setText(listenSkillList.get(i).getName());
                        break;
                    }
                }
            }
            gradeExamBeanList = levelbean.getLanguage_list().get(0).getGrade_exam();
            if(gradeExamBeanList!=null&&gradeExamBeanList.size()!=0) {
                if (gradeExamBeanList.get(0).getId() != null) {
                    String testLevel = gradeExamBeanList.get(0).getId();
                    selectTestLevelId = testLevel;
                    for (int i = 0; i < testLevelList.size(); i++) {
                        if (testLevel.equals(testLevelList.get(i).getId())) {
                            tvModifyLanguageTestType.setText(testLevelList.get(i).getName());
                            break;
                        }
                    }
                }
                etModifyLanguageTestNumber.setText(gradeExamBeanList.get(0).getScore());
                if(gradeExamBeanList.size()>1){
                    for(int i=1;i<gradeExamBeanList.size();i++){
                        selectTestLevelList.add(gradeExamBeanList.get(i).getId());
                        addTestLevelView(gradeExamBeanList.get(i));
                    }
                }
            }
            initTestLevelDialog(levelbean.getLanguage_list().get(0).getLangname());

        }

    }

    /**
     * 第一个等级考试类型的初始化
     * @param nowLauguageType
     */
    private void initTestLevelDialog(String nowLauguageType) {
        List<String> stringList = new ArrayList<>();

        String[] s = getResources().getStringArray(R.array.array_languageTestLevel_ids);
        for (int i = 0; i < s.length; i++) {
            if (nowLauguageType.equals(s[i].substring(0, 2))) {
                stringList.add(s[i]);
            }
        }
        if(stringList==null||stringList.size()==0){
            llTestLevel.setVisibility(View.GONE);
        }else {
            llTestLevel.setVisibility(View.VISIBLE);
            String[] nameStr = new String[stringList.size()];
            for (int i = 0; i < stringList.size(); i++) {
                for (int j = 0; j < testLevelList.size(); j++) {
                    if (testLevelList.get(j).getId().equals(stringList.get(i))) {
                        nameStr[i] = testLevelList.get(j).getName();
                        break;
                    }
                }
            }
            datePickerTestLevel = new CustomDatePicker(ModifyLanguageActivity.this, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) {
                    if ("".equals(time) || time == null) {
                        tvModifyLanguageTestType.setText("请选择");
                    } else {
                        tvModifyLanguageTestType.setText(time);
                        for (int i = 0; i < testLevelList.size(); i++) {
                            if (time.equals(testLevelList.get(i).getName())) {
                                selectTestLevelId = testLevelList.get(i).getId();
                                break;
                            }
                        }
                    }
                }
            }, nameStr);
        }
    }

    /**
     * 多个等级考试列表中类型选择框的初始化
     * @param nowLauguageType
     * @param tvModifyLanguageTestType2
     */
    private void initTestLevelDialog2(String nowLauguageType, final TextView tvModifyLanguageTestType2) {
        List<String> stringList = new ArrayList<>();

        String[] s = getResources().getStringArray(R.array.array_languageTestLevel_ids);
        for (int i = 0; i < s.length; i++) {
            if (nowLauguageType.equals(s[i].substring(0, 2))) {
                stringList.add(s[i]);
            }
        }
        String[] nameStr = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < testLevelList.size(); j++) {
                if (testLevelList.get(j).getId().equals(stringList.get(i))) {
                    nameStr[i] = testLevelList.get(j).getName();
                    break;
                }
            }
        }
        datePickerTestLevel = new CustomDatePicker(ModifyLanguageActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    tvModifyLanguageTestType2.setText("请选择");
                } else {
                    tvModifyLanguageTestType2.setText(time);
                    for (int i = 0; i < testLevelList.size(); i++) {
                        if (time.equals(testLevelList.get(i).getName())) {
                           /* selectTestLevelId = testLevelList.get(i).getId();*/
                           selectTestLevelList.add(testLevelList.get(i).getId());
                            break;
                        }
                    }
                }
            }
        }, nameStr);
    }

    /**
     * 语言，读写，听说选择框的初始化
     */
    private void initDialog() {
        datePickerLaugageType = new CustomDatePicker(ModifyLanguageActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    spResumeModifylanguageLangname.setText("请选择");
                } else {
                    spResumeModifylanguageLangname.setText(time);
                    for (int i = 0; i < lauguageTypeList.size(); i++) {
                        if (time.equals(lauguageTypeList.get(i).getName())) {
                            selectLauguageType = lauguageTypeList.get(i).getId();
                            if(!selectLanguageTypeFirst.equals(selectLauguageType)) {
                                tvModifyLanguageTestType.setText("请选择");
                                llModifyLanguageAddView.removeAllViews();
                                etModifyLanguageTestNumber.setText("");
                            }
                            initTestLevelDialog(selectLauguageType);
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_language_type_zh));
        datePickerListenSkill = new CustomDatePicker(ModifyLanguageActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    spResumeModifylanguageSpeakLevel.setText("请选择");
                } else {
                    spResumeModifylanguageSpeakLevel.setText(time);
                    for (int i = 0; i < listenSkillList.size(); i++) {
                        if (time.equals(listenSkillList.get(i).getName())) {
                            selectListenSkill = listenSkillList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_language_speaklevel_zh));
        datePickerReadWriteSkill = new CustomDatePicker(ModifyLanguageActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    spResumeModifylanguageReadLevel.setText("请选择");
                } else {
                    spResumeModifylanguageReadLevel.setText(time);
                    for (int i = 0; i < readWriteSkillList.size(); i++) {
                        if (time.equals(readWriteSkillList.get(i).getName())) {
                            selectReadWriteSkill = readWriteSkillList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_language_readlevel_zh));
    }

    @OnClick({R.id.iv_resume_modifynewlangue_back, R.id.tv_resume_item_languagemodify_delete, R.id.tv_resume_item_llanguagemodify_save, R.id.sp_resume_modifylanguage_speak_level, R.id.sp_resume_modifylanguage_read_level, R.id.sp_resume_modifylanguage_langname, R.id.tv_modifyLanguage_addView, R.id.rl_ModifyLanguage_TestLevel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifynewlangue_back:
                finish();
                break;
            case R.id.tv_resume_item_languagemodify_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_llanguagemodify_save:
                saveData();
                break;
            case R.id.sp_resume_modifylanguage_speak_level:
                datePickerListenSkill.show(spResumeModifylanguageSpeakLevel.getText().toString());
                break;
            case R.id.sp_resume_modifylanguage_read_level:
                datePickerReadWriteSkill.show(spResumeModifylanguageReadLevel.getText().toString());
                break;
            case R.id.sp_resume_modifylanguage_langname:
                datePickerLaugageType.show(spResumeModifylanguageLangname.getText().toString());
                break;
            case R.id.tv_modifyLanguage_addView:
                if (selectLauguageType != null && !"".equals(selectLauguageType)) {
                    addTestLevelView(null);
                } else {
                    Toast.makeText(this, "请选择你要填写的语言", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_ModifyLanguage_TestLevel:
                if (selectLauguageType != null && !"".equals(selectLauguageType)) {
                    datePickerTestLevel.show(tvModifyLanguageTestType.getText().toString());
                } else {
                    Toast.makeText(this, "请选择你要填写的语言", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 添加多个等级考试列表
     * @param gradeExamBean
     */
    private void addTestLevelView(final LanguageLevelBean.LanguageListBean.GradeExamBean gradeExamBean) {
        if (llModifyLanguageAddView.getChildCount() >= 0) {
            final View view = View.inflate(this, R.layout.item_modifylanguage_addview, null);
            RelativeLayout rl_selectTestType = (RelativeLayout) view.findViewById(R.id.rl_ModifyLanguage_TestLevel1);
            final TextView tv_testTypeName = (TextView) view.findViewById(R.id.tv_ModifyLanguage_TestType1);
            final EditText et_testNumber = (EditText) view.findViewById(R.id.et_ModifyLanguage_TestNumber1);
            TextView tv_testDelete = (TextView) view.findViewById(R.id.tv_modifyLanguage_deleteView);


            if(gradeExamBean!=null){
                addViewDateReflesh(gradeExamBean,et_testNumber,tv_testTypeName);
            }
            tv_testDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llModifyLanguageAddView.removeView(view);
                    etList.remove(et_testNumber);
                    selectTestLevelList.remove(gradeExamBean.getId());
                }
            });
            rl_selectTestType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerTestLevel.show(tv_testTypeName.getText().toString());
                }
            });
            etList.add(et_testNumber);
            llModifyLanguageAddView.addView(view);
            initTestLevelDialog2(selectLauguageType, tv_testTypeName);
        }
    }

    /**
     * 多个等级考试列表的数据填入
     * @param gradeExamBean
     * @param et
     * @param tv
     */
    private void addViewDateReflesh(LanguageLevelBean.LanguageListBean.GradeExamBean gradeExamBean,EditText et,TextView tv){
        if (gradeExamBean.getId() != null) {
            String testLevel = gradeExamBean.getId();
            /*selectTestLevelId = testLevel;*/
            for (int i = 0; i < testLevelList.size(); i++) {
                if (testLevel.equals(testLevelList.get(i).getId())) {
                    tv.setText(testLevelList.get(i).getName());
                    break;
                }
            }
        }
        et.setText(gradeExamBean.getScore());
    }
    /**
     * 保存数据
     */
    private void saveData() {
        if (selectLauguageType == null || "".equals(selectLauguageType)) {
            Toast.makeText(this, "请选择语言", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectReadWriteSkill == null || "".equals(selectReadWriteSkill)) {
            Toast.makeText(this, "请选择听说", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectListenSkill == null || "".equals(selectListenSkill)) {
            Toast.makeText(this, "请选择读写", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!selectLanguageTypeFirst.equals(selectLauguageType)){
            deleteData();
        }
        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", "user_resume.languageset");
        requestParams.put("resume_id", resumeId);
        requestParams.put("langname", selectLauguageType);
        requestParams.put("read_level", selectReadWriteSkill);
        requestParams.put("speak_level", selectListenSkill);
        if(selectTestLevelId!=null&&!"".equals(selectTestLevelId)){
            if(etModifyLanguageTestNumber.getText().toString()!=null&&!"".equals(etModifyLanguageTestNumber.getText().toString())){
                gradeExamBeanList.clear();
                LanguageLevelBean.LanguageListBean.GradeExamBean gradeExamBean=new LanguageLevelBean.LanguageListBean.GradeExamBean();
                gradeExamBean.setId(selectTestLevelId);
                gradeExamBean.setScore(etModifyLanguageTestNumber.getText().toString());
                gradeExamBeanList.add(gradeExamBean);
                if(llModifyLanguageAddView.getChildCount()>0){
                    for(int i=0;i<llModifyLanguageAddView.getChildCount();i++){
                        if(selectTestLevelList.get(i)!=null&&!"".equals(selectTestLevelList.get(i))&&!"".equals(etList.get(i).getText().toString())&&etList.get(i).getText().toString()!=null){
                            LanguageLevelBean.LanguageListBean.GradeExamBean gradeExamBean2=new LanguageLevelBean.LanguageListBean.GradeExamBean();
                            gradeExamBean2.setId(selectTestLevelList.get(i));
                            gradeExamBean2.setScore(etList.get(i).getText().toString());
                            gradeExamBeanList.add(gradeExamBean2);
                        }
                    }
                }
                jsonArray=new JSONArray();
                for(LanguageLevelBean.LanguageListBean.GradeExamBean a : gradeExamBeanList){
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("id", a.getId());
                        jo.put("score", a.getScore());
                        jsonArray.put(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                requestParams.put("grade_exam",jsonArray.toString());
            }
        }
        NetService service = new NetService(context, handlerService);
        service.execute(requestParams);
    }

    /*  private void saveData() {
          MyUtils.canResumeReflesh = true;
          String languageNameIdString = selectLauguageType;
          String readlevelIdString = selectListenSkill;
          String speaklevelString = selectReadWriteSkill;
          resumeLanguageLevel.setLangname(languageNameIdString);
          resumeLanguageLevel.setUser_id(MyUtils.userID);
          resumeLanguageLevel.setRead_level(readlevelIdString);
          resumeLanguageLevel.setSpeak_level(speaklevelString);
          if (resumeLanguageLevel.getId() == -1) {// 新建后保存
              ArrayList<ResumeLanguageLevel> arrayList = new ArrayList<ResumeLanguageLevel>();
              arrayList.add(resumeLanguageLevel);
              int resultInsert = dbOperator.Insert_ResumeLanguageLevel(arrayList);
              if (resultInsert != 0) {
                  ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
                  next();
              } else {
                  Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
              }
          } else {// 修改后保存
              // 更新到数据库
              MyUtils.canResumeReflesh = true;
              boolean resultUpdate = dbOperator.update_ResumeLanguageLevel(resumeLanguageLevel);
              if (resultUpdate) {
                  ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
                  next();
              } else {
                  Toast.makeText(context, "编辑失败", Toast.LENGTH_LONG).show();
              }
          }
      }
  */

    /**
     * 删除数据
     */
    private void deleteData() {
        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", "user_resume.languagedel");
        requestParams.put("langname", selectLanguageTypeFirst);
        NetService service = new NetService(context, handlerDelete);
        service.execute(requestParams);
    }

    /**
     * 弹出是否删除提示框
     */
    private void deleteConfirm() {
        BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(context);
        dialog.setTitle("温馨提示");
        dialog.setMessage("您确定要删除吗？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        deleteData();
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.create().show();
    }

  /*  *//**
     * 删除数据
     *//*
    private void deleteData(int id) {
        boolean delResule = dbOperator.Delete_Data("ResumeLanguageLevel", id);
        if (delResule) {
            ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
            next();
            // 刷新简历完整度
        } else {
            next();
        }
    }*/

  /*  private void next() {
        MyUtils.canResumeReflesh = true;
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }*/
}
