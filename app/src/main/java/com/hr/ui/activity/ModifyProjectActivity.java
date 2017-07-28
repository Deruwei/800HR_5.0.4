package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeProjectExpAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyProjectActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifyproject_back)
    ImageView ivResumeModifyprojectBack;
    @Bind(R.id.et_resume_item_modifyproject_projectame)
    EditText etResumeItemModifyprojectProjectame;
    @Bind(R.id.tv_resume_item_modifyproject_starttime)
    TextView tvResumeItemModifyprojectStarttime;
    @Bind(R.id.tv_resume_item_modifyproject_endtime)
    TextView tvResumeItemModifyprojectEndtime;
    @Bind(R.id.et_resume_item_modifyproject_post)
    EditText etResumeItemModifyprojectPost;
    @Bind(R.id.et_resume_item_modifyproject_describe)
    EditText etResumeItemModifyprojectDescribe;
    @Bind(R.id.et_resume_item_modifyproject_duty)
    EditText etResumeItemModifyprojectDuty;
    @Bind(R.id.tv_resume_item_modifyproject_delete)
    TextView tvResumeItemModifyprojectDelete;
    @Bind(R.id.tv_resume_item_modifyproject_save)
    TextView tvResumeItemModifyprojectSave;
    private Context context = ModifyProjectActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeProject resumeProject;
    private String resumeId, resumeLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_project);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(context);
        resumeProject = (ResumeProject) getIntent().getSerializableExtra("resumeProject");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        etResumeItemModifyprojectProjectame.setText(resumeProject.getProjectname());
        String startTime = resumeProject.getFromyear() + "-" + resumeProject.getFrommonth();
        String endTime = resumeProject.getToyear() + "-" + resumeProject.getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        tvResumeItemModifyprojectStarttime.setText(startTime);
        tvResumeItemModifyprojectEndtime.setText(endTime);
        etResumeItemModifyprojectPost.setText(resumeProject.getPosition());
        etResumeItemModifyprojectDescribe.setText(resumeProject.getProjectdesc());
        etResumeItemModifyprojectDuty.setText(resumeProject.getResponsibility());
        tvResumeItemModifyprojectStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemModifyprojectStarttime, 2);
            }
        });
        tvResumeItemModifyprojectEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemModifyprojectEndtime, 2);
            }
        });
    }


    @OnClick({R.id.iv_resume_modifyproject_back, R.id.tv_resume_item_modifyproject_delete, R.id.tv_resume_item_modifyproject_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifyproject_back:
                finish();
                break;
            case R.id.tv_resume_item_modifyproject_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_modifyproject_save:
                saveData();
                break;
        }
    }
    private void saveData() {
        if (etResumeItemModifyprojectProjectame.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemModifyprojectStarttime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemModifyprojectStarttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemModifyprojectEndtime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemModifyprojectEndtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemModifyprojectPost.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目职务", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemModifyprojectDescribe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目描述", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemModifyprojectDuty.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目职责", Toast.LENGTH_LONG).show();
            return;
        }


        String starttimeString = tvResumeItemModifyprojectStarttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemModifyprojectEndtime
                .getText().toString().trim();
        String[] endtimeStrings;
        if ("至今".equals(endtimeString)) {
            endtimeStrings = new String[]{"0", "0"};
        } else {
            endtimeStrings = endtimeString.split("-");
        }
        if (starttimeStrings.length < 1) {
            Toast.makeText(context, "请填写开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endtimeStrings.length < 1) {
            Toast.makeText(context, "请填写结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        // 时间段过滤
        int startYear = Integer.parseInt(starttimeStrings[0]);
        int startMonth = Integer.parseInt(starttimeStrings[1]);
        int endYear = Integer.parseInt(endtimeStrings[0]);
        int endMonth = Integer.parseInt(endtimeStrings[1]);
        if (endYear < startYear) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        String companynameString = etResumeItemModifyprojectProjectame
                .getText().toString().trim();
        // 所任职位
        String positionString = etResumeItemModifyprojectPost
                .getText().toString().trim();
        // 职责描述
        String projectDes = etResumeItemModifyprojectDescribe
                .getText().toString().trim();
        String responsibilityString = etResumeItemModifyprojectDuty
                .getText().toString().trim();
        resumeProject.setFromyear(starttimeStrings[0]);
        resumeProject.setFrommonth(starttimeStrings[1]);
        resumeProject.setToyear(endtimeStrings[0]);
        resumeProject.setTomonth(endtimeStrings[1]);
        resumeProject.setProjectname(companynameString);
        resumeProject.setPosition(positionString);
        resumeProject.setResponsibility(responsibilityString);
        resumeProject.setProjectdesc(projectDes);
        resumeProject.setResume_id(resumeId);
        resumeProject.setResume_language(resumeLanguage);
        if (resumeProject.getId() == -1) {// 新建后保存

            ArrayList<ResumeProject> arrayList = new ArrayList<ResumeProject>();
            arrayList.add(resumeProject);
            int resultInsert = dbOperator
                    .Insert_Resumeitem(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator
                    .update_Resumeitem(resumeProject);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
                // 刷新简历完整度
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
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
                        deleteData(resumeProject.getId());
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
        boolean delResule = dbOperator.Delete_Data("ResumeProject", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
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
