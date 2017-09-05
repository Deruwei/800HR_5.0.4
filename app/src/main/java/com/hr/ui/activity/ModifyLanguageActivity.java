package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeLanguageExpLVAdapter;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.IdSpineer;
import com.hr.ui.view.pulltorefresh.LoadingLayout;

import java.util.ArrayList;
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
    private Context context = ModifyLanguageActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeLanguageLevel resumeLanguageLevel;
    private String resumeId, resumeLanguage;
    private String selectLauguageType,selectListenSkill,selectReadWriteSkill;
    private List<SelectBean> lauguageTypeList=new ArrayList<>();
    private List<SelectBean> listenSkillList=new ArrayList<>();
    private List<SelectBean> readWriteSkillList=new ArrayList<>();
    private CustomDatePicker datePickerLaugageType,datePickerListenSkill,datePickerReadWriteSkill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_language);
        ButterKnife.bind(this);
        initData();
        initDialog();
    }

    private void initData() {
        lauguageTypeList= GetResumeArrayList.getLaugageTypeListFromArray(this);
        listenSkillList=GetResumeArrayList.getListenSkillFromArray(this);
        readWriteSkillList=GetResumeArrayList.getReadWriteSkillFromArray(this);
        dbOperator = new DAO_DBOperator(context);
        resumeLanguageLevel = (ResumeLanguageLevel) getIntent().getSerializableExtra("resumeLanguageLevel");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        if(getIntent().getStringExtra("isAdd").equals("1")){
            tvResumeItemLanguagemodifyDelete.setVisibility(View.GONE);
        }
        if (resumeLanguageLevel.getLangname() != null) {
            String languageId = resumeLanguageLevel.getLangname();
            selectLauguageType = languageId;
            for (int i = 0; i < lauguageTypeList.size(); i++) {
                if (languageId.equals(lauguageTypeList.get(i).getId())) {
                    spResumeModifylanguageLangname.setText(lauguageTypeList.get(i).getName());
                    break;
                }
            }
        }
        if (resumeLanguageLevel.getRead_level() != null) {
            String readLevelId = resumeLanguageLevel.getRead_level();
            selectReadWriteSkill = readLevelId;
            for (int i = 0; i < readWriteSkillList.size(); i++) {
                if (readLevelId.equals(readWriteSkillList.get(i).getId())) {
                    spResumeModifylanguageReadLevel.setText(readWriteSkillList.get(i).getName());
                    break;
                }
            }
        }
        if (resumeLanguageLevel.getSpeak_level() != null) {
            String speakLevelId = resumeLanguageLevel.getSpeak_level();
            selectListenSkill = speakLevelId;
            for (int i = 0; i < listenSkillList.size(); i++) {
                if (speakLevelId.equals(listenSkillList.get(i).getId())) {
                    spResumeModifylanguageSpeakLevel.setText(listenSkillList.get(i).getName());
                    break;
                }
            }
        }
    }
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
    @OnClick({R.id.iv_resume_modifynewlangue_back, R.id.tv_resume_item_languagemodify_delete, R.id.tv_resume_item_llanguagemodify_save,R.id.sp_resume_modifylanguage_speak_level,R.id.sp_resume_modifylanguage_read_level,R.id.sp_resume_modifylanguage_langname})
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
        }
    }
    /*
     * 保存数据
     */
    private void saveData() {
        MyUtils.canResumeReflesh=true;
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
            MyUtils.canResumeReflesh=true;
            boolean resultUpdate = dbOperator.update_ResumeLanguageLevel(resumeLanguageLevel);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void deleteConfirm() {
        BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(context);
        dialog.setTitle("温馨提示");
        dialog.setMessage("您确定要删除吗？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        deleteData(resumeLanguageLevel.getId());
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
    /**
     * 删除数据
     */
    private void deleteData(int id) {
        boolean delResule = dbOperator.Delete_Data("ResumeLanguageLevel", id);
        if (delResule) {
            ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
            next();
            // 刷新简历完整度
        } else {
            next();
        }
    }
    private void next(){
        MyUtils.canResumeReflesh=true;
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
