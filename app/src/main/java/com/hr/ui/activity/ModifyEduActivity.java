package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.MyCustomDatePicker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyEduActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifyeduinfo_back)
    ImageView ivResumeModifyeduinfoBack;
    @Bind(R.id.rl_resume_modifyeduinfo_top)
    RelativeLayout rlResumeModifyeduinfoTop;
    @Bind(R.id.et_resume_item_newresumeedu_schoolname)
    EditText etResumeItemNewresumeeduSchoolname;
    @Bind(R.id.tv_resume_item_newresumeedu_starttime)
    TextView tvResumeItemNewresumeeduStarttime;
    @Bind(R.id.tv_resume_item_newresumeedu_endtime)
    TextView tvResumeItemNewresumeeduEndtime;
    @Bind(R.id.sp_resume_item_newresumeedu_degree)
    TextView spResumeItemNewresumeeduDegree;
    @Bind(R.id.et_resume_item_newresumeedu_majorname)
    EditText etResumeItemNewresumeeduMajorname;
    @Bind(R.id.tv_resume_item_newresumeedu_delete)
    TextView tvResumeItemNewresumeeduDelete;
    @Bind(R.id.tv_resume_item_newresumeedu_save)
    TextView tvResumeItemNewresumeeduSave;
    private Context context = ModifyEduActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeEducation resumeEducation;
    private String resumeId, resumeLanguage;
    private String isAdd;
    private CustomDatePicker datePickerEdu;
    private MyCustomDatePicker datePickerStart, datePickerEnd;
    private List<SelectBean> eduList = new ArrayList<>();
    private String selectEduId;

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
        setContentView(R.layout.activity_modify_edu);
        ButterKnife.bind(this);
        initDialog();
        initData();

    }

    private void initData() {
        eduList = GetResumeArrayList.getEduListFromArray(this);
        dbOperator = new DAO_DBOperator(context);
        isAdd = getIntent().getStringExtra("isAdd");
        if (isAdd.equals("1")) {
            tvResumeItemNewresumeeduDelete.setVisibility(View.GONE);
        }
        resumeEducation = (ResumeEducation) getIntent().getSerializableExtra("resumeEducation");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        String fromyear = resumeEducation.getFromyear();
        String frommonth = resumeEducation.getFrommonth();
        if ("0".equals(fromyear) && "0".equals(frommonth)) {
            tvResumeItemNewresumeeduStarttime.setText("至今");
        } else {
            tvResumeItemNewresumeeduStarttime.setText(fromyear + "-" + frommonth);
        }
        // 结束时间
        String endyear = resumeEducation.getToyear();
        String endmonth = resumeEducation.getTomonth();
        if ("0".equals(endyear) && "0".equals(endmonth)) {
            tvResumeItemNewresumeeduEndtime.setText("至今");
        } else {
            tvResumeItemNewresumeeduEndtime.setText(endyear + "-" + endmonth);
        }
        etResumeItemNewresumeeduSchoolname.setText(resumeEducation.getSchoolname());
        etResumeItemNewresumeeduMajorname.setText(resumeEducation.getMoremajor());
        if (resumeEducation.getDegree() != null) {
            String eduId = resumeEducation.getDegree();
            selectEduId = eduId;
            for (int i = 0; i < eduList.size(); i++) {
                if (eduId.equals(eduList.get(i).getId())) {
                    spResumeItemNewresumeeduDegree.setText(eduList.get(i).getName());
                    break;
                }
            }
        }

    }

    private void initDialog() {
        datePickerEdu = new CustomDatePicker(ModifyEduActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    spResumeItemNewresumeeduDegree.setText("请选择");
                } else {
                    spResumeItemNewresumeeduDegree.setText(time);
                    for (int i = 0; i < eduList.size(); i++) {
                        if (time.equals(eduList.get(i).getName())) {
                            selectEduId = eduList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_degree_zh));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        datePickerStart = new MyCustomDatePicker(ModifyEduActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemNewresumeeduStarttime.setText(time);
            }
        });
        datePickerStart.showSpecificYearAndMonth(false);
        datePickerEnd = new MyCustomDatePicker(ModifyEduActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemNewresumeeduEndtime.setText(time);
            }
        });
        datePickerEnd.showSpecificYearAndMonth(false);
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
                        deleteData(resumeEducation.getId());
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

    private void deleteData(int id) {
        boolean delResule = dbOperator.Delete_Data("ResumeEducation", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            next();
            finish();

            // 刷新简历完整度
        } else {
            next();

        }
        MyUtils.canResumeReflesh = true;
    }

    private void saveData() {
        MyUtils.canResumeReflesh = true;
        if (etResumeItemNewresumeeduSchoolname.getText().toString().trim()
                .length() == 0) {
            Toast.makeText(context, "请输入学校名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemNewresumeeduStarttime.getText().toString().trim().contains("-")
                && !"至今".equals(tvResumeItemNewresumeeduStarttime.getText().toString().trim())) {
            // System.out.println(start_time.getText().toString());
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemNewresumeeduEndtime.getText().toString().trim().contains("-")
                && !"至今".equals(tvResumeItemNewresumeeduEndtime.getText().toString().trim())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemNewresumeeduMajorname.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "请输入专业", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = tvResumeItemNewresumeeduStarttime.getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemNewresumeeduEndtime
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
        if (endYear < startYear&&!tvResumeItemNewresumeeduEndtime.getText().toString().equals("至今")) {
            Toast.makeText(context,
                    context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if ((endYear == startYear && endMonth < startMonth)&&!tvResumeItemNewresumeeduEndtime.getText().toString().equals("至今")) {
            Toast.makeText(context, context.
                    getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolnameString = etResumeItemNewresumeeduSchoolname.getText().toString().trim();
        String majorString = etResumeItemNewresumeeduMajorname.getText().toString().trim();
        String idsString = selectEduId;
        resumeEducation.setResume_id(resumeId);
        resumeEducation.setResume_language(resumeLanguage);
        resumeEducation.setUser_id(MyUtils.userID);
        resumeEducation.setFromyear(starttimeStrings[0]);
        resumeEducation.setFrommonth(starttimeStrings[1]);
        resumeEducation.setToyear(endtimeStrings[0]);
        resumeEducation.setTomonth(endtimeStrings[1]);
        resumeEducation.setSchoolname(schoolnameString);
        resumeEducation.setMoremajor(majorString);
        resumeEducation.setDegree(idsString);

        if (resumeEducation.getId() == -1) {// 新建后保存
            ArrayList<ResumeEducation> arrayList = new ArrayList<ResumeEducation>();
            arrayList.add(resumeEducation);
            int resultInsert = dbOperator.Insert_ResumeEducation(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();

            }
        } else {// 修改后保存

            // 更新教育经历
            boolean resultUpdate = dbOperator.update_ResumeEducation(resumeEducation);
            // System.out.println("传入DB的内容：" + resumeEducation.toString());
            if (resultUpdate) {
                MyUtils.canResumeReflesh = true;
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void next() {
        MyUtils.canResumeReflesh = true;
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.iv_resume_modifyeduinfo_back,R.id.tv_resume_item_newresumeedu_starttime, R.id.tv_resume_item_newresumeedu_endtime, R.id.tv_resume_item_newresumeedu_delete, R.id.tv_resume_item_newresumeedu_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifyeduinfo_back:
                finish();
                break;
            case R.id.tv_resume_item_newresumeedu_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_newresumeedu_save:
                saveData();
                break;
            case R.id.tv_resume_item_newresumeedu_endtime:
                String s=tvResumeItemNewresumeeduEndtime.getText().toString();
                if(!"至今".equals(s)) {
                    s = s + "-1";
                }
                datePickerEnd.show(s,4);
                break;
            case R.id.tv_resume_item_newresumeedu_starttime:
                String a=tvResumeItemNewresumeeduStarttime.getText().toString();
                a=a+"-1";
                datePickerStart.show(a,1);
                break;
        }
    }

    @OnClick(R.id.sp_resume_item_newresumeedu_degree)
    public void onViewClicked() {
        datePickerEdu.show(spResumeItemNewresumeeduDegree.getText().toString());
    }
}
