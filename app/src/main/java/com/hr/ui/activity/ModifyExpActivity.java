package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.MyCustomDatePicker;
import com.hr.ui.view.custom.MyDatePicker;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyExpActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifynewjobexp_back)
    ImageView ivResumeModifynewjobexpBack;
    @Bind(R.id.et_resume_item_newjobexp_comname)
    EditText etResumeItemNewjobexpComname;
    @Bind(R.id.tv_resume_item_newjobexp_starttime)
    TextView tvResumeItemNewjobexpStarttime;
    @Bind(R.id.tv_resume_item_newjobexp_endtime)
    TextView tvResumeItemNewjobexpEndtime;
    @Bind(R.id.tv_resume_item_newjobexp_workplace)
    TextView tvResumeItemNewjobexpWorkplace;
    @Bind(R.id.cb_item_ischeck)
    CheckBox cbItemIscheck;
    @Bind(R.id.et_resume_item_newjobexp_salary)
    EditText etResumeItemNewjobexpSalary;
    @Bind(R.id.et_resume_item_newjobexp_post)
    EditText etResumeItemNewjobexpPost;
    @Bind(R.id.et_resume_item_newjobexp_describe)
    EditText etResumeItemNewjobexpDescribe;
    @Bind(R.id.tv_resume_item_newresumejob_delete)
    TextView tvResumeItemNewresumejobDelete;
    @Bind(R.id.tv_resume_item_newresumejob_save)
    TextView tvResumeItemNewresumejobSave;
    private Context context = ModifyExpActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeExperience resumeExperience;
    private String resumeId, resumeLanguage;
    public static ModifyExpActivity modifyExpActivity;
    private String placeId,isAdd;
    private MyCustomDatePicker datePickerBeginTime,datePickerEndtime;

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
        setContentView(R.layout.activity_modify_exp);
        ButterKnife.bind(this);
        modifyExpActivity = ModifyExpActivity.this;
        initData();
        initDatePicker();
    }

    private void initDatePicker() {
        datePickerBeginTime=new MyCustomDatePicker(ModifyExpActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemNewjobexpStarttime.setText(time);
            }
        });
        datePickerBeginTime.showSpecificYearAndMonth(false);
        datePickerEndtime=new MyCustomDatePicker(ModifyExpActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemNewjobexpEndtime.setText(time);
            }
        });
        datePickerEndtime.showSpecificYearAndMonth(false);
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(context);
        resumeExperience = (ResumeExperience) getIntent().getSerializableExtra("resumeExperience");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        isAdd=getIntent().getStringExtra("isAdd");
        if("1".equals(isAdd)){
            tvResumeItemNewresumejobDelete.setVisibility(View.GONE);
        }

        etResumeItemNewjobexpComname.setText(resumeExperience.getCompany());
        String startTime = resumeExperience.getFromyear() + "-" + resumeExperience.getFrommonth();
        String endTime = resumeExperience.getToyear() + "-" + resumeExperience.getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        if ("0".equals(resumeExperience.getSalary_hide())) {
            cbItemIscheck.setChecked(false);
        } else if ("1".equals(resumeExperience.getSalary_hide())) {
            cbItemIscheck.setChecked(true);
        }
        cbItemIscheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    resumeExperience.setSalary_hide("1");
                } else {
                    resumeExperience.setSalary_hide("0");
                }
            }
        });
        tvResumeItemNewjobexpStarttime.setText(startTime);
        tvResumeItemNewjobexpEndtime.setText(endTime);
        etResumeItemNewjobexpPost.setText(resumeExperience.getPosition());
        etResumeItemNewjobexpSalary.setText(resumeExperience.getSalary());
        etResumeItemNewjobexpDescribe.setText(resumeExperience.getResponsibility());
        tvResumeItemNewjobexpStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               datePickerBeginTime.show(tvResumeItemNewjobexpStarttime.getText().toString()+"-1",1);
            }
        });

        tvResumeItemNewjobexpEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=tvResumeItemNewjobexpEndtime.getText().toString();
                if(!"至今".equals(s)) {
                    s = s + "-1";
                }
               datePickerEndtime.show(s,2);
            }
        });
        tvResumeItemNewjobexpWorkplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectCityActicity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type","1");
                context.startActivity(intent);
            }
        });
        JSONArray cityJSONArray = null;
        try {
            if (MyUtils.USE_ONLINE_ARRAY) {
                cityJSONArray = NetService.getCityAsJSONArray(context, "city.json");
            } else {
                InputStream inputStream = context.getAssets().open("city_zh.json");
                cityJSONArray = new JSONArray(NetUtils.readAsString(
                        inputStream, Constants.ENCODE));
            }
            placeId = resumeExperience.getCompanyaddress();
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(placeId)) {
                    tvResumeItemNewjobexpWorkplace.setText(object.getString(placeId));
                    break;
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlaceText(String value) {
        tvResumeItemNewjobexpWorkplace.setText(value);
    }

    public void setPlaceId(String string) {
        placeId = string;
    }

    private void saveData() {
        MyUtils.canResumeReflesh=true;
        if (etResumeItemNewjobexpComname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入公司名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemNewjobexpStarttime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemNewjobexpStarttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemNewjobexpEndtime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemNewjobexpEndtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (tvResumeItemNewjobexpWorkplace.getText()
                .toString().trim().length() == 0 ||"请选择地点".equals(tvResumeItemNewjobexpWorkplace.getText().toString().trim())) {
            Toast.makeText(context, "请选择工作地点", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemNewjobexpSalary.getText().toString()
                .trim().length() == 0) {
            Toast.makeText(context, "请输入税前月薪", Toast.LENGTH_LONG).show();
            return;
        }
        if ("0".equals(etResumeItemNewjobexpSalary.getText().toString()
                .trim().substring(0, 1))) {
            Toast.makeText(context, "请输入大于0的税前月薪", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemNewjobexpPost.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入所任职位", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemNewjobexpDescribe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入职位描述", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = tvResumeItemNewjobexpStarttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemNewjobexpEndtime
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
        if (endYear < startYear&&!"至今".equals(tvResumeItemNewjobexpEndtime.getText().toString())) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth&&!"至今".equals(tvResumeItemNewjobexpEndtime.getText().toString())) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String companynameString = etResumeItemNewjobexpComname
                .getText().toString().trim();
        String salaryString = etResumeItemNewjobexpSalary.getText().toString().trim();
        // 所任职位
        String positionString = etResumeItemNewjobexpPost
                .getText().toString().trim();
        // 职责描述
        String responsibilityString = etResumeItemNewjobexpDescribe
                .getText().toString().trim();
        String workPlaceId = CityNameConvertCityID.convertCityID(context, tvResumeItemNewjobexpWorkplace.getText().toString());

        resumeExperience.setFromyear(starttimeStrings[0]);
        resumeExperience.setFrommonth(starttimeStrings[1]);
        resumeExperience.setToyear(endtimeStrings[0]);
        resumeExperience.setTomonth(endtimeStrings[1]);
        resumeExperience.setCompanyaddress(workPlaceId);
        resumeExperience.setCompany(companynameString);
        resumeExperience.setSalary(salaryString);
        resumeExperience.setPosition(positionString);
        resumeExperience.setResponsibility(responsibilityString);
        resumeExperience.setResume_id(resumeId);
        resumeExperience.setResume_language(resumeLanguage);
        resumeExperience.setUser_id(MyUtils.userID);

        if (resumeExperience.getId() == -1) {// 新建后保存
            ArrayList<ResumeExperience> arrayList = new ArrayList<ResumeExperience>();
            arrayList.add(resumeExperience);
            int resultInsert = dbOperator
                    .Insert_ResumeWorkExperience(arrayList);
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
                    .update_ResumeWorkExperience(resumeExperience);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                next();
                MyUtils.canResumeReflesh=true;
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @OnClick({R.id.iv_resume_modifynewjobexp_back, R.id.tv_resume_item_newresumejob_delete, R.id.tv_resume_item_newresumejob_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifynewjobexp_back:
                finish();
                break;
            case R.id.tv_resume_item_newresumejob_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_newresumejob_save:
                saveData();
                break;
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
                        deleteData(resumeExperience.getId());
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
        boolean delResule = dbOperator.Delete_Data("ResumeWorkExperience", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
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
