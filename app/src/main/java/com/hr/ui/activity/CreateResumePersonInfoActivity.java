package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeList;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeComplete;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.datautils.ResumeListStringJsonParser;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.IdSpineer;
import com.hr.ui.view.custom.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人信息修改页面
 */
public class CreateResumePersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateResumePersonInfoActivity";
    private Context mContext = CreateResumePersonInfoActivity.this;
    private ResumeBaseInfo resumeBaseInfo;
    private String resumeIdString;
    private String resumeLanguageString;


    private SharedPreferencesUtils sUtils;
    private int LOADING_RESUMELIST = 0x1000;// 简历对比完成后，开始加载简历列表标识
    private ResumeTitle resumeTitle;
    private DAO_DBOperator dbOperator;
    // 网络简历列表信息
    public static ArrayList<ResumeBaseInfo> listBaseInfos;
    public static ArrayList<ResumeLanguageLevel> listLanguageLevels;
    public static ArrayList<ResumeList> listResumeLists;
    private ArrayList<ResumeList> listResume = null;
    public final static int ToMyResume = 100, ToResumeScan = 200;
    //ToResumeScan获取到网络简历信息,跳转到预览  获取到网络简历信息,跳转到MyResume
    private boolean isCHS = true;
    private ResumeBaseInfo baseInfoZh, baseInfoEn;
    private int stateNum = 0;
    private long firstClickTime;
    /**
     * 控件
     */
    private RelativeLayout rl_createresume_personinfo_save;
    private EditText et_createresume_personinfo_name, et_createresume_personinfo_email;
    private RadioButton rb_createresume_personinfo_man, rb_createresume_personinfo_woman;
    private TextView tv_createresume_personinfo_birthday, et_createresume_personinfo_phonenum;
    private static TextView tv_createresume_personinfo_home;
    private IdSpineer sp_createresume_personinfo_jobbegintime, sp_createresume_personinfo_func;
    //    sp_createresume_personinfo_nation,
    private ResumeComplete resumeComplete;// 简历完整度操作类

    private static String placeIdNowPlace;
    private ImageView iv_createresume_personinfo_back;
    private String resumeId = "-1";// 简历id
    private String resumeModifyTime = null;// 简历时间戳
    private String resumeType = null;// 简历类型
    private Handler handlerIsApp = new Handler() {
        public void handleMessage(Message msg) {
            int msgInt = msg.arg1;
            final String jsonString = (String) msg.obj;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        if (jsonObject.getString("error_code").equals("0")) {
                            Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resume_person_info);
        sUtils = new SharedPreferencesUtils(mContext);
        initView();
        initData();
        checkIsAddResume();
    }

    private void checkIsAddResume() {
        Calendar c = Calendar.getInstance();
        String curDate = c.get(Calendar.YEAR) + "-"
//                + (c.get(Calendar.MONDAY) + 1) + "-"
                + (1) + "-"
                + c.get(Calendar.DAY_OF_MONTH);
        resumeComplete = new ResumeComplete(mContext, "-1",
                "zh");
        // 点击的是添加按钮
        resumeTitle = new ResumeTitle();
        resumeTitle.setUser_id(MyUtils.userID);
        resumeTitle.setTitle("我的简历");
        resumeTitle.setResume_language("zh");
        resumeTitle.setResume_type("1");//默认为社会简历
        resumeTitle.setOpen("2");// 默认保密
        resumeTitle.setUptime("");
        resumeTitle.setFill_scale(resumeComplete.getFullScale(false, false) + "");
        resumeTitle.setCastbehalf("1");// 默认简历未委托状态
        resumeTitle.setAdd_time(curDate);
        resumeTitle.setImportant("0");// 默认为非首选简历
        resumeTitle.setModify_time("");
        resumeTitle.setResume_id("-1");// 此处resumeIdString=-1
        resumeTitle.setIsUpdate(1);// 状态为未上传
        // 保存到数据库
        int result = dbOperator.Insert_ResumeList(resumeTitle);
        if (result > 0) {
//            Toast.makeText(mContext, "简历新建成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        listResume = new ArrayList<ResumeList>();
        sUtils = new SharedPreferencesUtils(mContext);
        getResumeList();
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeBaseInfo = dbOperator
                .query_ResumePersonInfo_Toone(resumeLanguageString);
        resumeTitle = dbOperator.query_ResumeTitle_info(resumeIdString,
                resumeLanguageString);
        initSpinnerData();

        placeName=MyUtils.currentCityZh;
        if (resumeBaseInfo != null) {
            // System.out.println("baseInfo:" + resumeBaseInfo);
            setResumeBaseInfoToUI(resumeBaseInfo);
        } else {
            resumeBaseInfo = new ResumeBaseInfo();
            setResumeBaseInfoToUI(resumeBaseInfo);
        }
    }

    /**
     * 获取简历列表
     */
    public void getResumeList() {
        AsyncGetResumeList asyncGetResumeList = new AsyncGetResumeList(mContext);
        asyncGetResumeList.execute();
    }

    private void initSpinnerData() {
        if (isCHS) {
            sp_createresume_personinfo_jobbegintime
                    .setAdapter(new SpinnerAdapter(this,
                            android.R.layout.simple_spinner_item,
                            getResources().getStringArray(
                                    R.array.array_persioninfo_workbeginyear_zh)));
        } else {
            sp_createresume_personinfo_jobbegintime
                    .setAdapter(new SpinnerAdapter(this,
                            android.R.layout.simple_spinner_item,
                            getResources().getStringArray(
                                    R.array.array_persioninfo_workbeginyear_en)));
        }
//        if (!isCHS) {// en
//            sp_createresume_personinfo_nation
//                    .setAdapter(new SpinnerAdapter(this,
//                            android.R.layout.simple_spinner_item,
//                            getResources().getStringArray(
//                                    R.array.array_nationality_en)));
//        } else {// zh
//            sp_createresume_personinfo_nation
//                    .setAdapter(new SpinnerAdapter(this,
//                            android.R.layout.simple_spinner_item,
//                            getResources().getStringArray(
//                                    R.array.array_nationality_zh)));
//        }
        if (!isCHS) {// en
            sp_createresume_personinfo_func.setAdapter(new SpinnerAdapter(this,
                    android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.array_zhicheng_en)));
        } else {// zh
            sp_createresume_personinfo_func.setAdapter(new SpinnerAdapter(this,
                    android.R.layout.simple_spinner_item,
                    getResources().getStringArray(R.array.array_zhicheng_zh)));
        }
        sp_createresume_personinfo_jobbegintime
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (sp_createresume_personinfo_jobbegintime.idStrings != null) {
                                sp_createresume_personinfo_jobbegintime.idString = sp_createresume_personinfo_jobbegintime.idStrings[arg2];
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
        sp_createresume_personinfo_func
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (sp_createresume_personinfo_func.idStrings != null) {
                                sp_createresume_personinfo_func.idString = sp_createresume_personinfo_func.idStrings[arg2];
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
//        sp_createresume_personinfo_nation
//                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                               int arg2, long arg3) {
//                        try {
//                            if (sp_createresume_personinfo_nation.idStrings != null) {
//                                sp_createresume_personinfo_nation.idString = sp_createresume_personinfo_nation.idStrings[arg2];
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        sp_createresume_personinfo_nation.setIds(getResources().getStringArray(R.array.array_nationality_ids));
        sp_createresume_personinfo_func.setIds(getResources().getStringArray(R.array.array_zhicheng_ids));
        sp_createresume_personinfo_jobbegintime.setIds(getResources().getStringArray(R.array.array_persioninfo_workbeginyear_ids));
    }

    private static String placeName;

    public static void setPlaceText(String value) {
        placeName = value;
        tv_createresume_personinfo_home.setText(value);
    }

    public static void setPlaceId(String string) {
        placeIdNowPlace = string;
    }

    private void setResumeBaseInfoToUI(ResumeBaseInfo resumeBaseInfo) {
        //性别
        et_createresume_personinfo_name.setText(resumeBaseInfo.getName());
        // 性别
        String sex = resumeBaseInfo.getSex();
        if ("1".equalsIgnoreCase(sex)) {// 1男，2女
            rb_createresume_personinfo_man.setChecked(true);
        } else if ("2".equalsIgnoreCase(sex)) {
            rb_createresume_personinfo_woman.setChecked(true);
        }
        // 出生日期
        if ("".equals(resumeBaseInfo.getYear())) {
            tv_createresume_personinfo_birthday.setText("请选择");
        } else {
            tv_createresume_personinfo_birthday.setText(resumeBaseInfo.getYear() + "-" + resumeBaseInfo.getMonth() + "-" + resumeBaseInfo.getDay());
        }
        // 国家或地区
//        sp_createresume_personinfo_nation.setSelectedItem(resumeBaseInfo.getNationality());
        // 参加工作时间或毕业时间
        String workyearsString = resumeBaseInfo.getWork_beginyear();
        if ("0".equals(workyearsString) || workyearsString == null) {
            sp_createresume_personinfo_jobbegintime.setSelectedItem("-1");// "-1"为无工作经验
        } else {
            sp_createresume_personinfo_jobbegintime.setSelectedItem(workyearsString);
        }
        // 现有职称
        String idZhiChengString = resumeBaseInfo.getPost_rank();
        if (idZhiChengString != null) {
            sp_createresume_personinfo_func.setSelectedItem(idZhiChengString);
        }
        //电话
        et_createresume_personinfo_phonenum.setText(resumeBaseInfo.getYdphone());
        et_createresume_personinfo_phonenum.setText(MyUtils.userphone);
//        if (resumeBaseInfo.getEmailaddress().equals("")) {
//            et_createresume_personinfo_email.setText(MyUtils.emailAddress);
//        } else {
        et_createresume_personinfo_email.setText(resumeBaseInfo.getEmailaddress());
//        }
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
            if(locationId!=null){
                placeIdNowPlace = locationId;
                MyUtils.currentCityId=placeIdNowPlace;
            }else{
                placeIdNowPlace=ResumeInfoIDToString.getCityID(this,MyUtils.currentCityZh,true);
                MyUtils.currentCityId=placeIdNowPlace;
            }

            // 现居住地
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(locationId)) {
                    placeName=object.getString(locationId);
                    tv_createresume_personinfo_home.setText(object.getString(locationId));
                    break;
                } else {
                    if (isCHS) {
                        tv_createresume_personinfo_home.setText("请选择");
                    } else {
                        tv_createresume_personinfo_home.setText("Please Select");
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initView() {
        et_createresume_personinfo_name = (EditText) findViewById(R.id.et_createresume_personinfo_name);
        rb_createresume_personinfo_man = (RadioButton) findViewById(R.id.rb_createresume_personinfo_man);
        rb_createresume_personinfo_woman = (RadioButton) findViewById(R.id.rb_createresume_personinfo_woman);
        iv_createresume_personinfo_back= (ImageView) findViewById(R.id.iv_createresume_personinfo_back);

        tv_createresume_personinfo_birthday = (TextView) findViewById(R.id.tv_createresume_personinfo_birthday);
        rl_createresume_personinfo_save = (RelativeLayout) findViewById(R.id.rl_createresume_personinfo_save);
        tv_createresume_personinfo_home = (TextView) findViewById(R.id.tv_createresume_personinfo_home);


        et_createresume_personinfo_email = (EditText) findViewById(R.id.et_createresume_personinfo_email);
        et_createresume_personinfo_phonenum = (TextView) findViewById(R.id.et_createresume_personinfo_phonenum);

//        sp_createresume_personinfo_nation = (IdSpineer) findViewById(R.id.sp_createresume_personinfo_nation);
        sp_createresume_personinfo_func = (IdSpineer) findViewById(R.id.sp_createresume_personinfo_func);
        sp_createresume_personinfo_jobbegintime = (IdSpineer) findViewById(R.id.sp_createresume_personinfo_jobbegintime);
//        iv_createresume_personinfo_back = (ImageView) findViewById(R.id.iv_createresume_personinfo_back);

        rl_createresume_personinfo_save.setOnClickListener(this);
        tv_createresume_personinfo_birthday.setOnClickListener(this);

        tv_createresume_personinfo_home.setOnClickListener(this);
       iv_createresume_personinfo_back.setOnClickListener(this);
    }
    private BeautifulDialog.Builder builderCreateResume;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_createresume_personinfo_save:
                saveData();
                break;
            case R.id.iv_createresume_personinfo_back:
                chooseIsExit();
                break;
            case R.id.tv_createresume_personinfo_birthday:
                DataPickerDialog.showDialog(mContext, tv_createresume_personinfo_birthday, 3);
                break;
            case R.id.tv_createresume_personinfo_home:
                Intent intent = new Intent(mContext, CreateSelectPlaceToResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromtag", 100);
                intent.putExtra("filter", "place");
                intent.putExtra("isCHS", true);
                intent.putExtra("value", "省市选择");
                mContext.startActivity(intent);
                break;
        }
    }
    /**
     * 退出整个应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            chooseIsExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void chooseIsExit() {
        builderCreateResume = new BeautifulDialog.Builder(mContext);
        builderCreateResume.setMessage("确认退出创建简历吗？\n 好职位需要简历投递呀，亲！");
        builderCreateResume.setTitle("提示");
        builderCreateResume.setPositiveButton("暂不创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(CreateResumePersonInfoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builderCreateResume.setNegativeButton("继续创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderCreateResume.create().show();
    }
    /*
     * 保存数据
     */
    private void saveData() {

        if (isCHS) {
            if (et_createresume_personinfo_name.getText().toString().trim().length() == 0) {
                Toast.makeText(mContext, "请输入姓名",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (rb_createresume_personinfo_man.isChecked() == false && rb_createresume_personinfo_woman.isChecked() == false) {
                Toast.makeText(mContext, "请选择性别", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!tv_createresume_personinfo_birthday.getText().toString().contains("-")) {
                Toast.makeText(mContext, "请选择出生日期", Toast.LENGTH_SHORT).show();
                return;
            }
            if(MyUtils.currentCityZh.equals(placeName)){
                placeIdNowPlace = ResumeInfoIDToString.getCityID(this, placeName, true);
            }
            if (placeIdNowPlace == null || "".equals(placeIdNowPlace)
                    || "0".equals(placeIdNowPlace)) {
                Toast.makeText(mContext, "请选择现居住地", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("0".equals(sp_createresume_personinfo_jobbegintime.getSelectedId())) {
                Toast.makeText(mContext, "请选择参加工作年份", Toast.LENGTH_SHORT).show();
                return;
            }
            // 手机号"^1[3|4|5|8]\d{9}$"

            if (et_createresume_personinfo_email.getText().toString().trim().length() == 0) {
                Toast.makeText(mContext, "请输入电子邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!et_createresume_personinfo_email.getText().toString().trim().contains("@")
                    || !et_createresume_personinfo_email.getText().toString().trim()
                    .contains(".com")) {
                Toast.makeText(mContext, "请正确输入电子邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String mobileString = et_createresume_personinfo_phonenum.getText().toString().trim();
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
                            Toast.makeText(CreateResumePersonInfoActivity.this,
                                    "请输入正确的手机号码(用\",\"分开)", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(CreateResumePersonInfoActivity.this,
                                    "Please Input Correct Mobile Phone",
                                    Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneString = et_createresume_personinfo_phonenum.getText().toString().trim();
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
        resumeBaseInfo.setName(et_createresume_personinfo_name.getText().toString());
        resumeBaseInfo.setLocation(placeIdNowPlace + "");
        resumeBaseInfo.setSex(rb_createresume_personinfo_man.isChecked() == true ? "1" : "2");
        String birthdayString = tv_createresume_personinfo_birthday.getText().toString();
        String[] birthStrings = birthdayString.split("-");
        resumeBaseInfo.setYear(birthStrings[0]);
        resumeBaseInfo.setMonth(birthStrings[1]);
        resumeBaseInfo.setDay(birthStrings[2]);
        resumeBaseInfo.setEcho_yes("1");
        resumeBaseInfo.setWork_beginyear(sp_createresume_personinfo_jobbegintime.getSelectedId() + "");
        resumeBaseInfo.setPost_rank(sp_createresume_personinfo_func.getSelectedId());
        resumeBaseInfo.setYdphone(mobileString);
        resumeBaseInfo.setEmailaddress(et_createresume_personinfo_email.getText().toString());
//        resumeBaseInfo.setNationality(sp_createresume_personinfo_nation.getSelectedId());
        resumeBaseInfo.setNationality("11");
        // System.out.println("保存信息：" + resumeBaseInfo.toString());
        boolean updateResult = dbOperator.update_ResumePersonInfo(resumeBaseInfo);

        if (updateResult) {// 修改成功
            ResumeIsUpdateOperator.setBaseInfoIsUpdate(this, dbOperator, resumeLanguageString);
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            // System.out.println(resumeBaseInfo.getName() + "为执行修改后的名字");
            // 刷新简历完整度
//            MyResume myResume = (MyResume) MyUtils.currentGroup
//                    .getLocalActivityManager().getActivity("MyResume");
//            if (myResume != null) {
//                myResume.refreshComplete();
//                myResume.refreshItemInfoComplete();
//            }
//            MyUtils.currentGroup.back();
            Intent intent = new Intent(mContext, CreateResumeJobOrderActivity.class);
            intent.putExtra("resumeId", "-1");
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
            startActivity(intent);
            finish();
        } else {// 修改失败
            Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取简历列表信息
     */
    private class AsyncGetResumeList {
        private Context context;
        private MyProgressDialog dialog;
        private Handler handService = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
//                    if (dialog != null && !dialog.isShowing()) {
//                        dialog.show();
//                    }
                    final String jsonString = (String) msg.obj;
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(
                                        jsonString);
                                int error_code = jsonObject
                                        .getInt("error_code");
                                if (error_code != 0) {
                                    Message message = new Message();
                                    message.what = -1;
                                    message.arg1 = error_code;
                                    handService.sendMessage(message);
                                    return;
                                }
                                // 0.清空数据
                                listResume.clear();

                                // --------加载离线简历--------//
                                loadOffLineResume(dbOperator);

                                // 1.获取线上json信息
                                JSONArray baseinfoJsonArray = jsonObject
                                        .getJSONArray("base_info");
                                JSONArray languageinfoJsonArray = jsonObject
                                        .getJSONArray("language_list");
                                JSONArray resumeArray = jsonObject
                                        .getJSONArray("resume_list");
                                // 2.解析json信息
                                ResumeListStringJsonParser parser = new ResumeListStringJsonParser();
                                listBaseInfos = parser
                                        .getBaseInfos(baseinfoJsonArray);
                                listLanguageLevels = parser
                                        .getLanguageLevels(languageinfoJsonArray);
                                listResumeLists = parser
                                        .getResumeLists(resumeArray);
                                // 3.简历对比
                                DAO_DBOperator dbOperator = new DAO_DBOperator(context);
                                // ----A.
                                boolean hasResumeLocal;// 记录本地是否存在该简历
                                // 遍历中文简历
                                ResumeTitle[] listLocalZh0 = dbOperator
                                        .query_ResumeList("zh");
                                for (ResumeTitle resumeTitleLocal : listLocalZh0) {
                                    hasResumeLocal = false;
                                    for (ResumeList resumeItem : listResumeLists) {
                                        if (resumeTitleLocal.getResume_id()
                                                .equals(resumeItem
                                                        .getResume_id())) {// 如果线下id=线上id
                                            hasResumeLocal = true;
                                        }
                                    }
                                    if (!hasResumeLocal) {// 如果本地存在，而线上不存在，则删除本地该简历
                                        if (!"-1".equals(resumeTitleLocal
                                                .getResume_id())) {// (离线简历除外)
                                            // 删除中文简历
                                            boolean resuleDelZh = dbOperator.Delete_Data(resumeTitleLocal.getResume_id(), "zh");
                                            // 删除英文简历
                                            boolean resuleDelEn = dbOperator.Delete_Data(resumeTitleLocal.getResume_id(), "en");
                                        }
                                    }
                                }
                                // ----B.
                                ResumeTitle[] listLocalZh1 = dbOperator
                                        .query_ResumeList("zh");
                                ResumeBaseInfo baseInfoZh1 = dbOperator
                                        .query_ResumePersonInfo_Toone("zh");
                                ResumeBaseInfo baseInfoEn1 = dbOperator
                                        .query_ResumePersonInfo_Toone("en");
                                // ResumeLanguageLevel[] resumeLanguageLevels1 =
                                // dbOperator
                                // .query_ResumeLanguageLevel();

                                // **************************个人信息+语言能力*****************************//

                                // 中文个人信息操作
                                if (baseInfoZh1 == null) {// 中文个人信息不存在
                                    // 写入非语言能力部分
                                    for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                        if ("zh".equals(baseInfo
                                                .getResume_language())) {
                                            long resultInsert = dbOperator
                                                    .Insert_ResumePersonInfo(baseInfo);
                                        }
                                    }
                                    // 写入语言能力
                                    // if (resumeLanguageLevels1.length == 0) {
                                    ArrayList<ResumeLanguageLevel> languageLevels = new ArrayList<ResumeLanguageLevel>();
                                    for (ResumeLanguageLevel resumeLanguageLevel : listLanguageLevels) {
                                        languageLevels.add(resumeLanguageLevel);
                                    }
                                    if (languageLevels.size() > 0) {
                                        long resultInsert = dbOperator
                                                .Insert_ResumeLanguageLevel(languageLevels);
                                    }
                                    // }else{
                                    // Log.e("============", "不空啊");
                                    // }
                                } else {// 中文个人信息存在，比价时间戳
                                    if (baseInfoZh1.getIsUpdate() == 0) {// 若本地未修改过基本信息，则将覆盖本地
                                        // 一、非语言部分
                                        for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                            if ("zh".equals(baseInfo
                                                    .getResume_language())) {
                                                boolean result = dbOperator
                                                        .update_ResumePersonInfo(baseInfo);
                                            }
                                        }

                                        // 二、语言部分
                                        ResumeLanguageLevel[] languageLevelsLocal = dbOperator
                                                .query_ResumeLanguageLevel();
                                        for (ResumeLanguageLevel resumeLanguageLevel : languageLevelsLocal) {
                                            // 1.遍历删除简历
                                            boolean delResule = dbOperator
                                                    .Delete_Data(
                                                            "ResumeLanguageLevel",
                                                            resumeLanguageLevel
                                                                    .getId());
                                        }
                                        // 2.将语言信息写入本地到本地
                                        ArrayList<ResumeLanguageLevel> languageLevels = new ArrayList<ResumeLanguageLevel>();
                                        for (ResumeLanguageLevel resumeLanguageLevel : listLanguageLevels) {
                                            languageLevels
                                                    .add(resumeLanguageLevel);
                                        }
                                        if (languageLevels.size() > 0) {
                                            long resultInsert = dbOperator
                                                    .Insert_ResumeLanguageLevel(languageLevels);
                                        }
                                    }
                                }
                                // 英文个人信息操作
                                if (baseInfoEn1 == null) {// 英文个人信息不存在
                                    // 非语言部分
                                    for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                        if ("en".equals(baseInfo
                                                .getResume_language())) {
                                            long resultInsert = dbOperator
                                                    .Insert_ResumePersonInfo(baseInfo);
                                        }
                                    }

                                } else {// 英文个人信息存在
                                    // 非语言部分
                                    if (baseInfoEn1.getIsUpdate() == 0) {// 本地未修改过英文个人信息
                                        // 一、非语言部分
                                        for (ResumeBaseInfo baseInfo : listBaseInfos) {
                                            if ("en".equals(baseInfo
                                                    .getResume_language())) {
                                                boolean result = dbOperator
                                                        .update_ResumePersonInfo(baseInfo);
                                            }
                                        }

                                    }

                                }

                                // **************************简历信息*****************************//

                                boolean hasResume = false;
                                for (ResumeList resumeOnLine : listResumeLists) {
                                    hasResume = false;
                                    if (listLocalZh1 == null || listLocalZh1.length == 0) {
                                        listResume.add(resumeOnLine);
                                        continue;
                                    }
                                    for (ResumeTitle resumeTitleLocalZh1 : listLocalZh1) {
                                        if (resumeOnLine.getResume_id().equals(
                                                resumeTitleLocalZh1
                                                        .getResume_id())) {// 线上和线下简历id相等
                                            hasResume = true;
                                            ResumeTitle resumeTitleEn = dbOperator.query_ResumeTitle_info(
                                                    resumeTitleLocalZh1
                                                            .getResume_id()
                                                            + "", "en");

                                            boolean resumeEnIsUpdate = false;
                                            if (resumeTitleEn != null
                                                    && resumeTitleEn
                                                    .getIsUpdate() == 1) {
                                                resumeEnIsUpdate = true;
                                            }
                                            if (resumeTitleLocalZh1
                                                    .getIsUpdate() == 1
                                                    || resumeEnIsUpdate) {// 本地简历有修改,加载本地
                                                ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(resumeTitleLocalZh1.getResume_id(), "zh");
                                                ResumeList resumeItem = new ResumeList();
                                                resumeItem
                                                        .setUser_id(MyUtils.userID);
                                                resumeItem
                                                        .setResume_id(resumeTitleLocalZh1
                                                                .getResume_id());
                                                resumeItem
                                                        .setUptime(resumeTitleLocalZh1
                                                                .getUptime());
                                                resumeItem
                                                        .setAdd_time(resumeTitleLocalZh1
                                                                .getAdd_time());
                                                // *公开状态，默认状态，委托状态每次都用线上信息
                                                resumeItem.setOpen(resumeOnLine
                                                        .getOpen());
                                                resumeItem
                                                        .setCastbehalf(resumeOnLine
                                                                .getCastbehalf());
                                                resumeItem
                                                        .setImportant(resumeOnLine
                                                                .getImportant());
                                                // 简历摘要信息
                                                if (order != null) {
                                                    resumeItem.setFunc(order
                                                            .getFunc());
                                                    resumeItem.setJobtype(order
                                                            .getJobtype());
                                                    resumeItem
                                                            .setOrder_salary(order
                                                                    .getOrder_salary());
                                                }
                                                // 简历名称
                                                resumeItem.setTitle(resumeTitleLocalZh1.getTitle());
                                                // 时间戳
                                                resumeItem.setModifyTime(resumeTitleLocalZh1.getModify_time());
                                                // //
                                                // 如果本地中文个人信息有修改，则所有本地简历标记为未上传
                                                // ResumeBaseInfo baseInfoZh =
                                                // dbOperator
                                                // .query_ResumePersonInfo_Toone("zh");
                                                // if (baseInfoZh != null
                                                // && baseInfoZh.getIsUpdate()
                                                // == 1) {
                                                resumeItem.setIsUpdate(1);
                                                // }
                                                listResume.add(resumeItem);
                                                continue;
                                            } else {// ----C.本地简历无修改
                                                // 简历
                                                listResume.add(resumeOnLine);
                                                continue;
                                            }
                                        }
                                    }
                                    if (!hasResume) {
                                        listResume.add(resumeOnLine);
                                    }
                                }
                                handService.sendEmptyMessage(LOADING_RESUMELIST);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else if (msg.what == LOADING_RESUMELIST) {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                } else if (msg.what == -1) {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                    Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(msg.arg1), Toast.LENGTH_SHORT).show();
                }
            }

            ;
        };

        public AsyncGetResumeList(Context context) {
            this.context = context;
//            this.dialog = new MyProgressDialog(context);
//            if (dialog == null) {
//                dialog = new MyProgressDialog(MyResumeActivity.this);
//            }
        }

        protected void execute() {
            try {
                /*
                 * 获取服务器简历
				 */
                HashMap<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("method", "user_resume.resumelist");
                NetService service = new NetService(context, handService);
                service.execute(requestParams);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * 加载离线简历
         *
         * @param dbOperator
         */
        private void loadOffLineResume(DAO_DBOperator dbOperator) {
            ResumeTitle[] resumeTitles = dbOperator.query_ResumeList("zh");
            for (ResumeTitle resumeTitle : resumeTitles) {
                if ("-1".equals(resumeTitle.getResume_id())) {
                    ResumeList offLineResume = new ResumeList();
                    offLineResume.setUser_id(MyUtils.userID);
                    offLineResume.setTitle(resumeTitle.getTitle());
                    offLineResume.setResume_id(resumeTitle.getResume_id());
                    offLineResume.setOpen(resumeTitle.getOpen());
                    offLineResume.setUptime(resumeTitle.getUptime());
                    offLineResume.setAdd_time(resumeTitle.getAdd_time());
                    offLineResume.setCastbehalf(resumeTitle.getCastbehalf());
                    offLineResume.setImportant(resumeTitle.getImportant());
                    offLineResume.setIsUpdate(resumeTitle.getIsUpdate());
                    ResumeOrder order = dbOperator
                            .query_ResumeCareerObjective_Toone(
                                    resumeTitle.getResume_id(),
                                    resumeTitle.getResume_language());
                    if (order != null) {
                        offLineResume.setFunc(order.getFunc());
                        offLineResume.setJobtype(order.getJobtype());
                        offLineResume.setOrder_salary(order.getOrder_salary());
                    }
                    listResume.add(offLineResume);
                }
            }
        }
    }

//    /**
//     * 向服务器发送 修改简历app
//     */
//    private void sendIsApp() {
//        try {
//            HashMap<String, String> requestParams = new HashMap<String, String>();
//            requestParams.put("method", "user_resume.setisapp");
//            requestParams.put("resume_id", resumeId);
//            requestParams.put("is_app", "1");
//            NetService service = new NetService(mContext, handlerIsApp);
//            service.execute(requestParams);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
