package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeTrainExpLVAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.BeautifulDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPlantActivity extends BaseResumeActivity {

    @Bind(R.id.iv_resume_modifynewtrain_back)
    ImageView ivResumeModifynewtrainBack;
    @Bind(R.id.et_resume_item_modifytrain_comname)
    EditText etResumeItemModifytrainComname;
    @Bind(R.id.tv_resume_item_modifytrain_starttime)
    TextView tvResumeItemModifytrainStarttime;
    @Bind(R.id.tv_resume_item_modifytrain_endtime)
    TextView tvResumeItemModifytrainEndtime;
    @Bind(R.id.et_resume_item_modifytrain_course)
    EditText etResumeItemModifytrainCourse;
    @Bind(R.id.et_resume_item_modifytrain_certificate)
    EditText etResumeItemModifytrainCertificate;
    @Bind(R.id.tv_resume_item_modifytrain_trainplace)
    TextView tvResumeItemModifytrainTrainplace;
    @Bind(R.id.et_resume_item_modifytrain_describe)
    EditText etResumeItemModifytrainDescribe;
    @Bind(R.id.tv_resume_item_modifytrain_delete)
    TextView tvResumeItemModifytrainDelete;
    @Bind(R.id.tv_resume_item_modifytrain_save)
    TextView tvResumeItemModifytrainSave;
    private Context context = ModifyPlantActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumePlant resumePlant;
    private String resumeId, resumeLanguage;
    private static String placeId = ""; // 地区 ID
    public static ModifyPlantActivity modifyPlantActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_plant);
        ButterKnife.bind(this);
        modifyPlantActivity = ModifyPlantActivity.this;
        initData();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(context);
        resumePlant = (ResumePlant) getIntent().getSerializableExtra("resumePlant");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");

        String startTime = resumePlant.getFromyear() + "-" + resumePlant.getFrommonth();
        String endTime = resumePlant.getToyear() + "-" + resumePlant.getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        tvResumeItemModifytrainStarttime.setText(startTime);
        tvResumeItemModifytrainEndtime.setText(endTime);
        etResumeItemModifytrainCertificate.setText(resumePlant.getCertification());
        etResumeItemModifytrainCourse.setText(resumePlant.getCourse());
        etResumeItemModifytrainDescribe.setText(resumePlant.getTraindetail());
        etResumeItemModifytrainComname.setText(resumePlant.getInstitution());
        tvResumeItemModifytrainStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemModifytrainStarttime, 2);
            }
        });
        tvResumeItemModifytrainEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, tvResumeItemModifytrainEndtime, 2);
            }
        });
        // 期望工作地点
        tvResumeItemModifytrainTrainplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPlaceToResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromtag", 103);
                intent.putExtra("isCHS", true);
                intent.putExtra("filter", "place");
                intent.putExtra("value", "省市选择");
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
            placeId = resumePlant.getPlace();
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(placeId)) {
                    tvResumeItemModifytrainTrainplace.setText(object.getString(placeId));
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPlaceText(String value) {
        tvResumeItemModifytrainTrainplace.setText(value);
    }

    public void setPlaceId(String string) {
        placeId = string;
    }

    @OnClick({R.id.iv_resume_modifynewtrain_back, R.id.tv_resume_item_modifytrain_delete, R.id.tv_resume_item_modifytrain_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifynewtrain_back:
                finish();
                break;
            case R.id.tv_resume_item_modifytrain_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_modifytrain_save:
                saveData();
                break;
        }
    }
    private void saveData() {
        if (etResumeItemModifytrainComname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入培训机构名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemModifytrainStarttime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemModifytrainStarttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemModifytrainEndtime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemModifytrainEndtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemModifytrainCourse.getText().toString()
                .trim().length() == 0) {
            Toast.makeText(context, "请输入培训课程", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemModifytrainCertificate.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "请输入所获得证书", Toast.LENGTH_LONG).show();
            return;
        }
        if (tvResumeItemModifytrainTrainplace.getText()
                .toString().trim().length() == 0 && tvResumeItemModifytrainTrainplace.getText()
                .toString().trim().equals("请选择地点")) {
            Toast.makeText(context, "请输入培训地点", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemModifytrainDescribe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入培训描述", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = tvResumeItemModifytrainStarttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemModifytrainEndtime.getText().toString().trim();
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
        String companynameString = etResumeItemModifytrainComname
                .getText().toString().trim();
        String courseString = etResumeItemModifytrainCourse.getText().toString().trim();
        // 所任职位
        String certificationString = etResumeItemModifytrainCertificate.getText().toString().trim();
        // 职责描述
        String responsibilityString = etResumeItemModifytrainDescribe.getText().toString().trim();
        String workPlaceId = CityNameConvertCityID.convertCityID(context, tvResumeItemModifytrainTrainplace.getText().toString());
        resumePlant.setFromyear(starttimeStrings[0]);
        resumePlant.setFrommonth(starttimeStrings[1]);
        resumePlant.setToyear(endtimeStrings[0]);
        resumePlant.setTomonth(endtimeStrings[1]);
        resumePlant.setPlace(workPlaceId);
        resumePlant.setInstitution(companynameString);
        resumePlant.setCourse(courseString);
        resumePlant.setCertification(certificationString);
        resumePlant.setTraindetail(responsibilityString);
        resumePlant.setResume_id(resumeId);
        resumePlant.setResume_language(resumeLanguage);
        resumePlant.setUser_id(MyUtils.userID);
        if (resumePlant.getId() == -1) {// 新建后保存

            ArrayList<ResumePlant> arrayList = new ArrayList<ResumePlant>();
            arrayList.add(resumePlant);
            int resultInsert = dbOperator.Insert_ResumeTraining(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator.update_ResumeTraining(resumePlant);
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
                    public void onClick(DialogInterface dialog, int which) {
                        deleteData(resumePlant.getId());
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
        boolean delResule = dbOperator.Delete_Data("ResumeTraining", id);
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
