package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.service.carrier.CarrierService;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.IdSpineer;
import com.hr.ui.view.custom.MyCustomDatePicker;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人信息修改页面
 */
public class ResumePersonInfoActivity extends BaseResumeActivity implements View.OnClickListener {
    private static final String TAG = "ResumePersonInfoActivity";
    private Context mContext = ResumePersonInfoActivity.this;
    private ResumeBaseInfo resumeBaseInfo;
    private String resumeIdString;
    private String resumeLanguageString;
    private boolean isCHS;
    private DAO_DBOperator dbOperator;
    private SharedPreferencesUtils sUtils;
    private static int RequestCode=1005;
    private String address;
    private CustomDatePicker datePickerBeginJob,datePickerFunc;
    private MyCustomDatePicker datePickerBirth;
    private int sexId=1;
    private TextView tv_man,tv_woman;
    /**
     * 控件
     */
//    rl_resume_personinfo_nation
    private RelativeLayout rl_resume_personinfo_save,  rl_resume_personinfo_jobbegintime, rl_resume_personinfo_func;
    private EditText et_resume_personinfo_name, et_resume_personinfo_email;
    private LinearLayout rb_resume_personinfo_man, rb_resume_personinfo_woman;
    private TextView tv_resume_personinfo_birthday, tv_phone_confirm;
    private static TextView tv_resume_personinfo_home, et_resume_personinfo_phonenum, tv_image_phone, tv_image_phone2;
    private TextView  sp_resume_personinfo_jobbegintime, sp_resume_personinfo_func;
    private ImageView iv_resume_personinfo_back;
//    sp_resume_personinfo_nation,
//    private LinearLayout lr_resumepersoninfo;
    private List<SelectBean> selectBeginJobList=new ArrayList<>();
    private List<SelectBean> selectFuncList=new ArrayList<>();
    private static String placeIdNowPlace;
    private String selectBenginId,selectFuncId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_person_info);
        sUtils = new SharedPreferencesUtils(mContext);
        initView();
        initData();
        initDialog();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);

        resumeBaseInfo = dbOperator.query_ResumePersonInfo_Toone(resumeLanguageString);
        if (resumeBaseInfo != null) {
            // System.out.println("baseInfo:" + resumeBaseInfo);
            setResumeBaseInfoToUI(resumeBaseInfo);
        } else {
            resumeBaseInfo = new ResumeBaseInfo();
        }
    }
    private void initDialog() {
        datePickerBeginJob = new CustomDatePicker(ResumePersonInfoActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if("".equals(time)||time==null){
                    sp_resume_personinfo_jobbegintime.setText("请选择");
                }else {
                    sp_resume_personinfo_jobbegintime.setText(time);
                    for(int i=0;i<selectBeginJobList.size();i++) {
                        if(time.equals(selectBeginJobList.get(i).getName())){
                            selectBenginId=selectBeginJobList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_persioninfo_workbeginyear_zh));
        datePickerFunc = new CustomDatePicker(ResumePersonInfoActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if("".equals(time)||time==null){
                    sp_resume_personinfo_func.setText("请选择");
                }else {
                    sp_resume_personinfo_func.setText(time);
                    for(int i=0;i<selectFuncList.size();i++) {
                        if(time.equals(selectFuncList.get(i).getName())){
                            selectFuncId=selectFuncList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_zhicheng_zh));
        datePickerBirth=new MyCustomDatePicker(ResumePersonInfoActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_resume_personinfo_birthday.setText(time);
            }
        });
    }


    private void setResumeBaseInfoToUI(ResumeBaseInfo resumeBaseInfo) {
//        Toast.makeText(this, resumeBaseInfo.getYdphone_verify_status(), Toast.LENGTH_SHORT).show();
        if (resumeBaseInfo.getYdphone_verify_status().equals("1")) {
            tv_image_phone.setVisibility(View.VISIBLE);
            tv_image_phone2.setVisibility(View.GONE);
        } else if (resumeBaseInfo.getYdphone_verify_status().equals("2")) {
            tv_image_phone.setVisibility(View.GONE);
            tv_image_phone2.setVisibility(View.VISIBLE);
        }
        //性别
        et_resume_personinfo_name.setText(resumeBaseInfo.getName());
        // 性别
        String sex = resumeBaseInfo.getSex();
        if ("1".equalsIgnoreCase(sex)) {// 1男，2女
            sexId=1;
            tv_woman.setTextColor(ContextCompat.getColor(this,R.color.gray));
            tv_man.setTextColor(ContextCompat.getColor(this,R.color.white));
            rb_resume_personinfo_man.setBackgroundResource(R.drawable.bg_sex_orange);
            rb_resume_personinfo_woman.setBackgroundResource(R.drawable.bg_sex_gray);
        } else if ("2".equalsIgnoreCase(sex)) {
            sexId=2;
            tv_woman.setTextColor(ContextCompat.getColor(this,R.color.white));
            tv_man.setTextColor(ContextCompat.getColor(this,R.color.gray));
            rb_resume_personinfo_man.setBackgroundResource(R.drawable.bg_sex_gray_left);
            rb_resume_personinfo_woman.setBackgroundResource(R.drawable.bg_sex_orange_right);
        }
        // 出生日期
        if ("".equals(resumeBaseInfo.getYear())) {
            tv_resume_personinfo_birthday.setText("请选择");
        } else {
            tv_resume_personinfo_birthday
                    .setText(resumeBaseInfo.getYear() + "-"
                            + resumeBaseInfo.getMonth() + "-"
                            + resumeBaseInfo.getDay());
        }
        // 国家或地区
//        sp_resume_personinfo_nation.setSelectedItem(resumeBaseInfo.getNationality());
        // 参加工作时间或毕业时间
        String workyearsString = resumeBaseInfo.getWork_beginyear();
        if ("0".equals(workyearsString) || workyearsString == null) {
            sp_resume_personinfo_jobbegintime.setText("请选择");// "-1"为无工作经验
        } else {
            selectBenginId=workyearsString;
            for(int i=0;i<selectBeginJobList.size();i++){
                if(workyearsString.equals(selectBeginJobList.get(i).getId())){
                    sp_resume_personinfo_jobbegintime.setText(selectBeginJobList.get(i).getName());
                    break;
                }
            }
        }
        // 现有职称
        String idZhiChengString = resumeBaseInfo.getPost_rank();
        if (idZhiChengString != null) {
            selectFuncId=idZhiChengString;
            for(int i=0;i<selectFuncList.size();i++){
                if(idZhiChengString.equals(selectFuncList.get(i).getId())){
                    sp_resume_personinfo_func.setText(selectFuncList .get(i).getName());
                    break;
                }
            }

        }
        //电话
//        if (resumeBaseInfo.getYdphone().length() > 11) {
//            et_resume_personinfo_phonenum.setText(resumeBaseInfo.getYdphone().substring(0, 10) + "...");
//        } else {
        et_resume_personinfo_phonenum.setText(resumeBaseInfo.getYdphone());
//        }
        et_resume_personinfo_email.setText(resumeBaseInfo.getEmailaddress());
        // 现居住地+户口所在地
        JSONArray cityJSONArray;
        try {
            if (MyUtils.USE_ONLINE_ARRAY && isCHS) {
                cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
            } else {
                if (isCHS) {
                    InputStream inputStream = getAssets().open("city_zh.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                } else {
                    InputStream inputStream = getAssets().open("city_en.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                }

            }
            String locationId = resumeBaseInfo.getLocation();
            String hukouId = resumeBaseInfo.getHukou();
            placeIdNowPlace = locationId;
            //Log.i("城市的名字",placeIdNowPlace);
            // 现居住地
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(locationId)) {
                    placeName=object.getString(locationId);
                    tv_resume_personinfo_home.setText(object.getString(locationId));
                    break;
                } else {
                    if (isCHS) {
                        tv_resume_personinfo_home.setText("请选择");
                    } else {
                        tv_resume_personinfo_home.setText("Please Select");
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initView() {
        selectBeginJobList= GetResumeArrayList.getBeginJobListFromArray(this);
        selectFuncList=GetResumeArrayList.getFuncListFromArray(this);
//        lr_resumepersoninfo = (LinearLayout) findViewById(R.id.lr_resumepersoninfo);
        et_resume_personinfo_name = (EditText) findViewById(R.id.et_resume_personinfo_name);
        rb_resume_personinfo_man = (LinearLayout) findViewById(R.id.ll_resume_personinfo_man);
        rb_resume_personinfo_woman = (LinearLayout) findViewById(R.id.ll_resume_personinfo_woman);

        tv_resume_personinfo_birthday = (TextView) findViewById(R.id.tv_resume_personinfo_birthday);
        rl_resume_personinfo_save = (RelativeLayout) findViewById(R.id.rl_resume_personinfo_save);
//        rl_resume_personinfo_nation = (RelativeLayout) findViewById(R.id.rl_resume_personinfo_nation);
        rl_resume_personinfo_func = (RelativeLayout) findViewById(R.id.rl_resume_personinfo_func);
        rl_resume_personinfo_jobbegintime = (RelativeLayout) findViewById(R.id.rl_resume_personinfo_jobbegintime);
        tv_man= (TextView) findViewById(R.id.tv_resume_man);
        tv_woman= (TextView) findViewById(R.id.tv_resume_woman);
        tv_resume_personinfo_home = (TextView) findViewById(R.id.tv_resume_personinfo_home);
        tv_phone_confirm = (TextView) findViewById(R.id.tv_phone_confirm);
        tv_image_phone = (TextView) findViewById(R.id.tv_image_phone);
        tv_image_phone2 = (TextView) findViewById(R.id.tv_image_phone2);
        iv_resume_personinfo_back = (ImageView) findViewById(R.id.iv_resume_personinfo_back);
        et_resume_personinfo_email = (EditText) findViewById(R.id.et_resume_personinfo_email);
        et_resume_personinfo_phonenum = (TextView) findViewById(R.id.et_resume_personinfo_phonenum);
//        sp_resume_personinfo_nation = (IdSpineer) findViewById(R.id.sp_resume_personinfo_nation);
        sp_resume_personinfo_func = (TextView) findViewById(R.id.sp_resume_personinfo_func);
        sp_resume_personinfo_jobbegintime = (TextView) findViewById(R.id.sp_resume_personinfo_jobbegintime);
        rl_resume_personinfo_save.setOnClickListener(this);
        tv_resume_personinfo_birthday.setOnClickListener(this);
//        lr_resumepersoninfo.setOnClickListener(this);
        iv_resume_personinfo_back.setOnClickListener(this);
        rb_resume_personinfo_man.setOnClickListener(this);
        rb_resume_personinfo_woman.setOnClickListener(this);
        tv_resume_personinfo_home.setOnClickListener(this);
        et_resume_personinfo_email.setOnClickListener(this);
        et_resume_personinfo_phonenum.setOnClickListener(this);
        et_resume_personinfo_name.setOnClickListener(this);
        rl_resume_personinfo_func.setOnClickListener(this);
        rl_resume_personinfo_jobbegintime.setOnClickListener(this);
//        rl_resume_personinfo_nation.setOnClickListener(this);
        tv_phone_confirm.setOnClickListener(this);
    }

    private static String placeName;

    public static void setPlaceText(String value) {
        tv_resume_personinfo_home.setText(value);
        placeName =value;
    }

    public static void setPhone(String value) {
        et_resume_personinfo_phonenum.setText(value);
        tv_image_phone2.setVisibility(View.VISIBLE);
        tv_image_phone.setVisibility(View.GONE);
    }

    public static void setPlaceId(String string) {
        placeIdNowPlace = string;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_resume_personinfo_name:
                modification = true;
                break;
            case R.id.et_resume_personinfo_email:
                modification = true;
                break;
            case R.id.et_resume_personinfo_phonenum:
                modification = true;
                Intent phoneIntent = new Intent(mContext, VerifyPhoneNumActivity.class);
                mContext.startActivity(phoneIntent);
                break;
            case R.id.tv_phone_confirm:
                modification = true;
                Intent phoneIntent2 = new Intent(mContext, VerifyPhoneNumActivity.class);
                mContext.startActivity(phoneIntent2);
                break;
//            case R.id.rl_resume_personinfo_nation:
//                modification = true;
//                break;
            case R.id.rl_resume_personinfo_jobbegintime:
                datePickerBeginJob.show(sp_resume_personinfo_jobbegintime.getText().toString());
                break;
            case R.id.rl_resume_personinfo_func:
                datePickerFunc.show(sp_resume_personinfo_func.getText().toString());
                break;


            case R.id.rl_resume_personinfo_save:
//                modification=true;
                if (MyUtils.ableInternet) {
                    saveData();
                } else {
                    Toast.makeText(ResumePersonInfoActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_resume_personinfo_birthday:
                modification = true;
                datePickerBirth.show(tv_resume_personinfo_birthday.getText().toString(),3);
                break;
            case R.id.iv_resume_personinfo_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_personinfo_home:
                modification = true;
                Intent intent = new Intent(mContext, SelectCityActicity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("from", "resumePersonInfo");
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.ll_resume_personinfo_man:
                modification = true;
                sexId=1;
                tv_woman.setTextColor(ContextCompat.getColor(this,R.color.gray));
                tv_man.setTextColor(ContextCompat.getColor(this,R.color.white));
                rb_resume_personinfo_man.setBackgroundResource(R.drawable.bg_sex_orange);
                rb_resume_personinfo_woman.setBackgroundResource(R.drawable.bg_sex_gray);
                break;
            case R.id.ll_resume_personinfo_woman:
                modification = true;
                sexId=2;
                tv_woman.setTextColor(ContextCompat.getColor(this,R.color.white));
                tv_man.setTextColor(ContextCompat.getColor(this,R.color.gray));
                rb_resume_personinfo_man.setBackgroundResource(R.drawable.bg_sex_gray_left);
                rb_resume_personinfo_woman.setBackgroundResource(R.drawable.bg_sex_orange_right);
                break;
        }
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RequestCode &&requestCode==SelectPlaceToResumeActivity.ResultCode){
            address=data.getStringExtra("address");
            placeName=address;
            setPlaceText(placeName);
        }
    }*/

    /*
             * 保存数据
             */
    private void saveData() {
        MyUtils.canResumeReflesh=true;
        if (isCHS) {
            if (et_resume_personinfo_name.getText().toString().trim().length() == 0) {
                Toast.makeText(mContext, "请输入姓名",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i("性别的名称：",sexId+"");
            if (sexId!=1&&sexId!=2) {
                Toast.makeText(mContext, "请选择性别", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!tv_resume_personinfo_birthday.getText().toString().contains("-")) {
                Toast.makeText(mContext, "请选择出生日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if(MyUtils.currentCityZh!=null&&"".equals(MyUtils.currentCityZh)) {
                if (placeName.equals(MyUtils.currentCityZh)) {
                    placeIdNowPlace = ResumeInfoIDToString.getCityID(this, placeName, true);
                }
            }
            if (placeIdNowPlace == null || "".equals(placeIdNowPlace)
                    || "0".equals(placeIdNowPlace)) {
                Toast.makeText(mContext, "请选择现居住地", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("0".equals(selectFuncId)||selectFuncId==null||"".equals(selectFuncId)) {
                Toast.makeText(mContext, "请选择参加工作年份", Toast.LENGTH_SHORT).show();
                return;
            }
            // 手机号"^1[3|4|5|8]\d{9}$"

            if (et_resume_personinfo_email.getText().toString().trim().length() == 0) {
                Toast.makeText(mContext, "请输入电子邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!et_resume_personinfo_email.getText().toString().trim().contains("@")
                    || !et_resume_personinfo_email.getText().toString().trim()
                    .contains(".com")) {
                Toast.makeText(mContext, "请正确输入电子邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String mobileString = et_resume_personinfo_phonenum.getText().toString().trim();
        String[] mobileArrayString = null;
        if (mobileString.length() != 0) {
            mobileString = mobileString.replaceAll("，", ",");
            // String regExp = "^1[3|4|5|8]\\d{9}$";
            String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
            Pattern p = Pattern.compile(regExp);
            if (mobileString != null) {
                mobileArrayString = mobileString.split(",");
                for (int i = 0; i < mobileArrayString.length; i++) {
                    Matcher m = p.matcher(mobileArrayString[i].trim());
                    if (!m.find()) {
                        if (isCHS) {
                            Toast.makeText(ResumePersonInfoActivity.this, "请输入正确的手机号码(用\",\"分开)", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResumePersonInfoActivity.this, "Please Input Correct Mobile Phone", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        String phoneString = et_resume_personinfo_phonenum.getText().toString().trim();
        String[] phoneStrings = null;
        if (phoneString.length() != 0) {
            phoneString = phoneString.replaceAll("，", ",");
            if (phoneString != null) {
                phoneStrings = phoneString.split(",");
            }
        }

        if (phoneStrings != null) {
//            if ((mobileArrayString.length + phoneStrings.length) > 4) {
//                Toast.makeText(mContext, "手机号和固话最多4个",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (mobileArrayString.length > 4) {
                Toast.makeText(mContext, "手机号和固话最多4个",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (mobileArrayString.length > 4) {
                Toast.makeText(mContext, "手机号和固话最多4个",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        resumeBaseInfo.setUser_id(MyUtils.userID);
        resumeBaseInfo.setResume_language(resumeLanguageString);
        resumeBaseInfo.setName(et_resume_personinfo_name.getText().toString());
        resumeBaseInfo.setLocation(placeIdNowPlace + "");
        resumeBaseInfo.setSex(sexId+"");
        String birthdayString = tv_resume_personinfo_birthday.getText().toString();
        String[] birthStrings = birthdayString.split("-");
        resumeBaseInfo.setYear(birthStrings[0]);
        resumeBaseInfo.setMonth(birthStrings[1]);
        resumeBaseInfo.setDay(birthStrings[2]);
        resumeBaseInfo.setWork_beginyear(selectBenginId);
        resumeBaseInfo.setPost_rank(selectFuncId);
        resumeBaseInfo.setYdphone(mobileString);
        resumeBaseInfo.setEmailaddress(et_resume_personinfo_email.getText().toString());
//        resumeBaseInfo.setNationality(sp_resume_personinfo_nation.getSelectedId());
        resumeBaseInfo.setNationality("11");
        boolean updateResult = dbOperator.update_ResumePersonInfo(resumeBaseInfo);
        if (updateResult) {// 修改成功
            ResumeIsUpdateOperator.setBaseInfoIsUpdate(this, dbOperator, resumeLanguageString);
            uploadData(resumeIdString);
        } else {// 修改失败
            Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
        }
    }
}
