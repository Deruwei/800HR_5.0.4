package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeSkillExpLVAdapter;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.IdSpineer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifySkillActivity extends BaseResumeActivity {
    @Bind(R.id.iv_resume_modifynewskill_back)
    ImageView ivResumeModifynewskillBack;
    @Bind(R.id.et_resume_item_modifyskill_skillname)
    EditText etResumeItemModifyskillSkillname;
    @Bind(R.id.et_resume_item_modifyskill_skilltime)
    EditText etResumeItemModifyskillSkilltime;
    @Bind(R.id.sp_resume_modifyskill_level)
    IdSpineer spResumeModifyskillLevel;
    @Bind(R.id.tv_resume_item_skillmodify_delete)
    TextView tvResumeItemSkillmodifyDelete;
    @Bind(R.id.tv_resume_item_skillmodify_save)
    TextView tvResumeItemSkillmodifySave;
    private Context context = ModifySkillActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeSkill resumeSkill;
    private String resumeId, resumeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_skill);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(context);
        resumeSkill = (ResumeSkill) getIntent().getSerializableExtra("resumeSkill");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        spResumeModifyskillLevel.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_skilllevel_zh)));
        spResumeModifyskillLevel.setIds(context.getResources().getStringArray(R.array.array_skilllevel_ids));
        spResumeModifyskillLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try {
                    if (spResumeModifyskillLevel.idStrings != null) {
                        spResumeModifyskillLevel.idString = spResumeModifyskillLevel.idStrings[arg2];
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
        etResumeItemModifyskillSkillname.setText(resumeSkill.getSkilltitle());
        etResumeItemModifyskillSkilltime.setText(resumeSkill.getUsetime());
        spResumeModifyskillLevel.setSelectedItem(resumeSkill.getAbility());
    }

    @OnClick({R.id.iv_resume_modifynewskill_back, R.id.tv_resume_item_skillmodify_delete, R.id.tv_resume_item_skillmodify_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifynewskill_back:
                finish();
                break;
            case R.id.tv_resume_item_skillmodify_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_skillmodify_save:
                saveData();
                break;
        }
    }

    /*
    * 保存数据
    */
    private void saveData() {
        MyUtils.canResumeReflesh=true;
        if (etResumeItemModifyskillSkillname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入技能名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemModifyskillSkilltime.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入使用时间", Toast.LENGTH_LONG).show();
            return;
        }

        String skillnameString = etResumeItemModifyskillSkillname
                .getText().toString().trim();
        String skilltimeString = etResumeItemModifyskillSkilltime
                .getText().toString().trim();
        String idsString = spResumeModifyskillLevel.getSelectedId();
        resumeSkill.setSkilltitle(skillnameString);
        resumeSkill.setUsetime(skilltimeString);
        resumeSkill.setAbility(idsString);
        resumeSkill.setResume_id(resumeId);
        resumeSkill.setResume_language(resumeLanguage);
        resumeSkill.setUser_id(MyUtils.userID);
        if (resumeSkill.getId() == -1) {// 新建后保存
            ArrayList<ResumeSkill> arrayList = new ArrayList<ResumeSkill>();
            arrayList.add(resumeSkill);
            int resultInsert = dbOperator.Insert_ResumeSkill(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            MyUtils.canResumeReflesh=true;
            boolean resultUpdate = dbOperator.update_ResumeSkill(resumeSkill);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
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
                        deleteData(resumeSkill.getId());
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
        boolean delResule = dbOperator.Delete_Data("ResumeSkill", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            next();
            // 刷新简历完整度
        } else {
            next();
        }
    }

    private void next() {
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
