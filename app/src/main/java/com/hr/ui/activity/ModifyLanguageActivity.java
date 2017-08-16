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
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.IdSpineer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLanguageActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifynewlangue_back)
    ImageView ivResumeModifynewlangueBack;
    @Bind(R.id.sp_resume_modifylanguage_langname)
    IdSpineer spResumeModifylanguageLangname;
    @Bind(R.id.sp_resume_modifylanguage_speak_level)
    IdSpineer spResumeModifylanguageSpeakLevel;
    @Bind(R.id.sp_resume_modifylanguage_read_level)
    IdSpineer spResumeModifylanguageReadLevel;
    @Bind(R.id.tv_resume_item_languagemodify_delete)
    TextView tvResumeItemLanguagemodifyDelete;
    @Bind(R.id.tv_resume_item_llanguagemodify_save)
    TextView tvResumeItemLlanguagemodifySave;
    private Context context = ModifyLanguageActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeLanguageLevel resumeLanguageLevel;
    private String resumeId, resumeLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_language);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(context);
        resumeLanguageLevel = (ResumeLanguageLevel) getIntent().getSerializableExtra("resumeLanguageLevel");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        spResumeModifylanguageLangname.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_type_zh)));
        spResumeModifylanguageLangname.setIds(context.getResources().getStringArray(R.array.array_language_type_ids));
        spResumeModifylanguageLangname
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (spResumeModifylanguageLangname.idStrings != null) {
                                spResumeModifylanguageLangname.idString = spResumeModifylanguageLangname.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });
        spResumeModifylanguageReadLevel.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_readlevel_zh)));
        spResumeModifylanguageReadLevel.setIds(context.getResources()
                .getStringArray(R.array.array_language_readlevel_ids));
        spResumeModifylanguageReadLevel
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        try {
                            if (spResumeModifylanguageReadLevel.idStrings != null) {
                                spResumeModifylanguageReadLevel.idString = spResumeModifylanguageReadLevel.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spResumeModifylanguageSpeakLevel.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_readlevel_zh)));
        spResumeModifylanguageSpeakLevel.setIds(context.getResources()
                .getStringArray(R.array.array_language_speaklevel_ids));
        spResumeModifylanguageSpeakLevel
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (spResumeModifylanguageSpeakLevel.idStrings != null) {
                                spResumeModifylanguageSpeakLevel.idString = spResumeModifylanguageSpeakLevel.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        spResumeModifylanguageSpeakLevel.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_speaklevel_zh)));
        spResumeModifylanguageLangname.setSelectedItem(resumeLanguageLevel.getLangname());
        spResumeModifylanguageReadLevel.setSelectedItem(resumeLanguageLevel.getRead_level());
        spResumeModifylanguageSpeakLevel.setSelectedItem(resumeLanguageLevel.getSpeak_level());
    }

    @OnClick({R.id.iv_resume_modifynewlangue_back, R.id.tv_resume_item_languagemodify_delete, R.id.tv_resume_item_llanguagemodify_save})
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
        }
    }
    /*
     * 保存数据
     */
    private void saveData() {
        MyUtils.canResumeReflesh=true;
        String languageNameIdString = spResumeModifylanguageLangname.getSelectedId();
        String readlevelIdString = spResumeModifylanguageReadLevel.getSelectedId();
        String speaklevelString = spResumeModifylanguageSpeakLevel.getSelectedId();
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
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
