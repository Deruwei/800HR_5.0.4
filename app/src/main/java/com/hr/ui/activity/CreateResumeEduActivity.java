package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.IdSpineer;

import java.util.ArrayList;

public class CreateResumeEduActivity extends BaseActivity implements View.OnClickListener {

    private EditText etResumeItemCreateduSchoolname;
    private TextView tvResumeItemCreateduStarttime;
    private TextView tvResumeItemCreateduEndtime;
    private IdSpineer spResumeItemCreateduDegree;
    private EditText etResumeItemCreateduMajorname;
    private TextView tvResumeItemNewcreateduAgao;
    private TextView tvResumeItemNewcreateduNext;
    private ImageView iv_create_resume_edu_back;
    private Context context = CreateResumeEduActivity.this;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    /**
     * 教育背景
     */
    private ResumeEducation[] resumeEducations;
    private ArrayList<ResumeEducation> listResumeEdu;

    public CreateResumeEduActivity createResumeEduActivity = null;

    /**
     * 只获取第一个
     */
    private int groupPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resume_edu);
        initView();
        initData();
    }

    private void initView() {
        etResumeItemCreateduSchoolname = (EditText) findViewById(R.id.et_resume_item_createdu_schoolname);
        tvResumeItemCreateduStarttime = (TextView) findViewById(R.id.tv_resume_item_createdu_starttime);
        tvResumeItemCreateduEndtime = (TextView) findViewById(R.id.tv_resume_item_createdu_endtime);
        spResumeItemCreateduDegree = (IdSpineer) findViewById(R.id.sp_resume_item_createdu_degree);
        etResumeItemCreateduMajorname = (EditText) findViewById(R.id.et_resume_item_createdu_majorname);
        tvResumeItemNewcreateduAgao = (TextView) findViewById(R.id.tv_resume_item_newcreatedu_agao);
        tvResumeItemNewcreateduNext = (TextView) findViewById(R.id.tv_resume_item_newcreatedu_next);
        iv_create_resume_edu_back = (ImageView) findViewById(R.id.iv_create_resume_edu_back);

        tvResumeItemNewcreateduAgao.setOnClickListener(this);
        iv_create_resume_edu_back.setOnClickListener(this);
        tvResumeItemNewcreateduNext.setOnClickListener(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ago();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initData() {
        createResumeEduActivity = CreateResumeEduActivity.this;
        dbOperator = new DAO_DBOperator(context);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeEducations = dbOperator.query_ResumeEducation(resumeIdString, resumeLanguageString);
        listResumeEdu = new ArrayList<>();
        for (ResumeEducation resumeEdu : resumeEducations) {
            listResumeEdu.add(resumeEdu);
        }
        if (listResumeEdu.size() == 0) {
            ResumeEducation resumeEdu = new ResumeEducation();
            listResumeEdu.add(resumeEdu);
        }
        spResumeItemCreateduDegree.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_degree_zh)));
        // 开始时间

        String fromyear = listResumeEdu.get(groupPosition).getFromyear();
        String frommonth = listResumeEdu.get(groupPosition).getFrommonth();
        if ("0".equals(fromyear) && "0".equals(frommonth)) {
            tvResumeItemCreateduStarttime.setText("至今");
        } else {
            tvResumeItemCreateduStarttime.setText(fromyear + "-" + frommonth);
        }
        // 结束时间
        String endyear = listResumeEdu.get(groupPosition).getToyear();
        String endmonth = listResumeEdu.get(groupPosition).getTomonth();
        if ("0".equals(endyear) && "0".equals(endmonth)) {
            tvResumeItemCreateduEndtime.setText("至今");
        } else {
            tvResumeItemCreateduEndtime.setText(endyear + "-" + endmonth);
        }

        tvResumeItemCreateduStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemCreateduStarttime, 2);
            }
        });
        tvResumeItemCreateduEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemCreateduEndtime, 2);
            }
        });
        spResumeItemCreateduDegree.setIds(context.getResources().getStringArray(
                R.array.array_degree_ids));
        spResumeItemCreateduDegree
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        try {
                            if (spResumeItemCreateduDegree.idStrings != null) {
                                spResumeItemCreateduDegree.idString = spResumeItemCreateduDegree.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
        etResumeItemCreateduSchoolname.setText(listResumeEdu.get(groupPosition).getSchoolname());
        etResumeItemCreateduMajorname.setText(listResumeEdu.get(groupPosition).getMoremajor());
        spResumeItemCreateduDegree.setSelectedItem(listResumeEdu.get(groupPosition).getDegree());
    }

    private void saveData() {
        ResumeEducation resumeEducation = listResumeEdu.get(groupPosition);
        if (etResumeItemCreateduSchoolname.getText().toString().trim()
                .length() == 0) {
            Toast.makeText(context, "请输入学校名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemCreateduStarttime.getText().toString().trim().contains("-")
                && !"至今".equals(tvResumeItemCreateduStarttime.getText().toString().trim())) {
            // System.out.println(start_time.getText().toString());
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemCreateduEndtime.getText().toString().trim().contains("-")
                && !"至今".equals(tvResumeItemCreateduEndtime.getText().toString().trim())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemCreateduMajorname.getText().toString().trim()
                .length() == 0) {
            Toast.makeText(context, "请输入专业", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = tvResumeItemCreateduStarttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemCreateduEndtime
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
            Toast.makeText(context,
                    context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth) {
            Toast.makeText(context, context.
                    getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolnameString = etResumeItemCreateduSchoolname.getText()
                .toString().trim();
        String majorString = etResumeItemCreateduMajorname.getText().toString()
                .trim();
        String idsString = spResumeItemCreateduDegree.getSelectedId();
        listResumeEdu.get(groupPosition).setResume_id(resumeIdString);
        listResumeEdu.get(groupPosition).setResume_language(resumeLanguageString);
        listResumeEdu.get(groupPosition).setUser_id(MyUtils.userID);
        listResumeEdu.get(groupPosition).setFromyear(starttimeStrings[0]);
        listResumeEdu.get(groupPosition).setFrommonth(starttimeStrings[1]);
        listResumeEdu.get(groupPosition).setToyear(endtimeStrings[0]);
        listResumeEdu.get(groupPosition).setTomonth(endtimeStrings[1]);
        listResumeEdu.get(groupPosition).setSchoolname(schoolnameString);
        listResumeEdu.get(groupPosition).setMoremajor(majorString);
        listResumeEdu.get(groupPosition).setDegree(idsString);

        if (listResumeEdu.get(groupPosition).getId() == -1) {// 新建后保存
            ArrayList<ResumeEducation> arrayList = new ArrayList<ResumeEducation>();
            arrayList.add(listResumeEdu.get(groupPosition));
            int resultInsert = dbOperator.Insert_ResumeEducation(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeIdString, resumeLanguageString);
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CreateResumeJobActivity.class);
                if (resumeIdString != null) {
                    intent.putExtra("resumeId", resumeIdString);
                    intent.putExtra("resumeLanguage", "zh");
                    intent.putExtra("isCHS", true);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新教育经历
            boolean resultUpdate = dbOperator
                    .update_ResumeEducation(resumeEducation);
            // System.out.println("传入DB的内容：" + resumeEducation.toString());
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeIdString, resumeLanguageString);
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CreateResumeJobActivity.class);
                if (resumeIdString != null) {
                    intent.putExtra("resumeId", resumeIdString);
                    intent.putExtra("resumeLanguage", "zh");
                    intent.putExtra("isCHS", true);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_create_resume_edu_back:
                ago();
                break;
            case R.id.tv_resume_item_newcreatedu_agao:
                ago();
                break;
            case R.id.tv_resume_item_newcreatedu_next:
                saveData();
                break;
        }
    }
    private void ago(){

        Intent intent = new Intent(context, CreateResumeJobOrderActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
