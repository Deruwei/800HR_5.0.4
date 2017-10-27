package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.IdSpineer;
import com.hr.ui.view.custom.MyCustomDatePicker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class CreateResumeEduActivity extends BaseActivity implements View.OnClickListener {

    private EditText etResumeItemCreateduSchoolname;
    private TextView tvResumeItemCreateduStarttime;
    private TextView tvResumeItemCreateduEndtime;
    private TextView spResumeItemCreateduDegree;
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
    private CustomDatePicker datePickerEdu;
    private List<SelectBean> eduList=new ArrayList<>();
    private String selectEduId;

    /**
     * 只获取第一个
     */
    private int groupPosition = 0;
    private MyCustomDatePicker datePickerBeginTime,datePickerEndTime;

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
        setContentView(R.layout.activity_create_resume_edu);
        initView();
        initData();
        initDialog();
    }

    private void initView() {
        eduList= GetResumeArrayList.getEduListFromArray(this);
        etResumeItemCreateduSchoolname = (EditText) findViewById(R.id.et_resume_item_createdu_schoolname);
        tvResumeItemCreateduStarttime = (TextView) findViewById(R.id.tv_resume_item_createdu_starttime);
        tvResumeItemCreateduEndtime = (TextView) findViewById(R.id.tv_resume_item_createdu_endtime);
        spResumeItemCreateduDegree = (TextView) findViewById(R.id.sp_resume_item_createdu_degree);
        etResumeItemCreateduMajorname = (EditText) findViewById(R.id.et_resume_item_createdu_majorname);
        tvResumeItemNewcreateduAgao = (TextView) findViewById(R.id.tv_resume_item_newcreatedu_agao);
        tvResumeItemNewcreateduNext = (TextView) findViewById(R.id.tv_resume_item_newcreatedu_next);
        iv_create_resume_edu_back = (ImageView) findViewById(R.id.iv_create_resume_edu_back);

        tvResumeItemNewcreateduAgao.setOnClickListener(this);
        iv_create_resume_edu_back.setOnClickListener(this);
        tvResumeItemNewcreateduNext.setOnClickListener(this);
        spResumeItemCreateduDegree.setOnClickListener(this);
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
               datePickerBeginTime.show(tvResumeItemCreateduStarttime.getText().toString()+"-1",1);
            }
        });
        tvResumeItemCreateduEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=tvResumeItemCreateduEndtime.getText().toString();
                if(!"至今".equals(s)){
                    s=s+"-1";
                }
                datePickerEndTime.show(s,4);
            }
        });
        etResumeItemCreateduSchoolname.setText(listResumeEdu.get(groupPosition).getSchoolname());
        etResumeItemCreateduMajorname.setText(listResumeEdu.get(groupPosition).getMoremajor());

        if(listResumeEdu.get(groupPosition).getDegree()!=null) {
            String eduId = listResumeEdu.get(groupPosition).getDegree();
            selectEduId=eduId;
            for(int i=0;i<eduList.size();i++) {
                if(eduId.equals(eduList.get(i).getId())) {
                    spResumeItemCreateduDegree.setText(eduList.get(i).getName());
                    break;
                }
            }
        }
    }
    private void initDialog() {
        datePickerEdu = new CustomDatePicker(CreateResumeEduActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if("".equals(time)||time==null){
                    spResumeItemCreateduDegree.setText("请选择");
                }else {
                    spResumeItemCreateduDegree.setText(time);
                    for(int i=0;i<eduList.size();i++) {
                        if(time.equals(eduList.get(i).getName())){
                            selectEduId=eduList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        },  getResources().getStringArray(R.array.array_degree_zh));
        datePickerBeginTime=new MyCustomDatePicker(CreateResumeEduActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemCreateduStarttime.setText(time);
            }
        });
        datePickerBeginTime.showSpecificYearAndMonth(false);
        datePickerEndTime=new MyCustomDatePicker(CreateResumeEduActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemCreateduEndtime.setText(time);
            }
        });
        datePickerEndTime.showSpecificYearAndMonth(false);
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
        if (endYear < startYear&&!tvResumeItemCreateduEndtime.getText().toString().equals("至今")) {
            Toast.makeText(context,
                    context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth&&!tvResumeItemCreateduEndtime.getText().toString().equals("至今")) {
            Toast.makeText(context, context.
                    getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolnameString = etResumeItemCreateduSchoolname.getText()
                .toString().trim();
        String majorString = etResumeItemCreateduMajorname.getText().toString()
                .trim();
        String idsString = selectEduId;
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
                Intent intent = new Intent(context, CreateResumeJobActivity.class);
                if (resumeIdString != null) {
                    intent.putExtra("resumeId", resumeIdString);
                    intent.putExtra("resumeLanguage", "zh");
                    intent.putExtra("isCHS", true);
                }
                startActivity(intent);
                finish();
            } else {
            }
        } else {// 修改后保存
            // 更新教育经历
            boolean resultUpdate = dbOperator
                    .update_ResumeEducation(resumeEducation);
            // System.out.println("传入DB的内容：" + resumeEducation.toString());
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeIdString, resumeLanguageString);
                Intent intent = new Intent(context, CreateResumeJobActivity.class);
                if (resumeIdString != null) {
                    intent.putExtra("resumeId", resumeIdString);
                    intent.putExtra("resumeLanguage", "zh");
                    intent.putExtra("isCHS", true);
                }
                startActivity(intent);
                finish();
            } else {
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
            case R.id.sp_resume_item_createdu_degree:
                datePickerEdu.show(spResumeItemCreateduDegree.getText().toString());
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
