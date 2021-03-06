package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CreateResumeJobExpLVAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.NoDoubleClickListener;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeInfoToJsonString;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncResumeUpdate;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.MyCustomDatePicker;
import com.mob.tools.utils.LocationHelper;
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
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateResumeJobActivity extends BaseActivity {

    @Bind(R.id.iv_createresume_job_back)
    ImageView ivCreateresumeJobBack;
    @Bind(R.id.tv_isjobexp_open)
    TextView tvIsjobexpOpen;
    @Bind(R.id.et_resume_item_job_comname)
    EditText etResumeItemJobComname;
    @Bind(R.id.tv_resume_item_job_starttime)
    TextView tvResumeItemJobStarttime;
    @Bind(R.id.tv_resume_item_job_endtime)
    TextView tvResumeItemJobEndtime;
    @Bind(R.id.tv_resume_item_job_workplace)
    TextView tvResumeItemJobWorkplace;
    @Bind(R.id.cb_itemjobexp_ischeck)
    CheckBox cbItemjobexpIscheck;
    @Bind(R.id.et_resume_item_job_salary)
    EditText etResumeItemJobSalary;
    @Bind(R.id.et_resume_item_job_post)
    EditText etResumeItemJobPost;
    @Bind(R.id.et_resume_item_job_describe)
    EditText etResumeItemJobDescribe;
    @Bind(R.id.tv_resume_item_newresumeedu_ago)
    TextView tvResumeItemNewresumeeduDelete;
    @Bind(R.id.tv_resume_item_resumejob_save)
    TextView tvResumeItemResumejobSave;
    @Bind(R.id.lr_jobactivity)
    LinearLayout lrJobactivity;
    private Context mContext = CreateResumeJobActivity.this;
    public  static CreateResumeJobActivity createResumeJobActivity;
    private SharedPreferencesUtils sUtils;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeExperience[] resumeExperience, resumeExperience2;
    private ArrayList<ResumeExperience> listResumeJobExp, listResumeJobExp2;
    private CreateResumeJobExpLVAdapter adapter;
    private RelativeLayout rl_createresume_info_save;
    private LinearLayout linear_createresume_jobexp;
    private View footView;
    private boolean isSubmit;
    private String resumeType;
    private String resumeAppId;
    private int groupPosition = 0;
    private String placeId;
    private MyCustomDatePicker datePickerBeginTime,datePickerEndTime;

    private Handler handlerUploadResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                int value = msg.what;
                resumeAppId = String.valueOf(msg.arg1);
                switch (value) {
                    case -1:
                        isSubmit=false;
                        tvResumeItemNewresumeeduDelete.setBackgroundResource(R.drawable.btn_1chang);
                        tvResumeItemResumejobSave.setBackgroundResource(R.drawable.btn_1chang);
                        Toast.makeText(mContext, "简历上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:// 中文简历已上传成功
                        MyUtils.canReflesh=true;
                        MainActivity.instanceMain.isLoad = true;
                        MainActivity.instanceMain.newAppResume = false;
                        goMainActivity();
                        /*sendIsApp();*/
                        Toast.makeText(mContext, "简历填写成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        tvResumeItemNewresumeeduDelete.setBackgroundResource(R.drawable.btn_1chang);
                        tvResumeItemResumejobSave.setBackgroundResource(R.drawable.btn_1chang);
                        isSubmit=false;
                        break;
                }
            }
        }
        ;
    };

    private Handler handlerIsApp = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
//                            Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                            sUtils.setStringValue("is_app_resumeid" + MyUtils.userID, resumeAppId);
                           // Log.i("简历的状态----------------","设置为默认简历成功");
//                            MeFragment.meFragment.execute();
                            goMainActivity();
                            break;
                        default:
                            //Log.i("简历的状态----------------","设置为默认简历失败");
                            Toast.makeText(mContext, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            goMainActivity();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                }
            } else {
               // Log.i("简历的状态----------------","没有到达");
//                Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void goMainActivity(){
        Intent intent=new Intent(CreateResumeJobActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
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
        setContentView(R.layout.activity_create_resume_job);
        ButterKnife.bind(this);
        createResumeJobActivity = CreateResumeJobActivity.this;
        initData();
        initListener();
        initDatePicker();
    }

    private void initDatePicker() {
        datePickerBeginTime=new MyCustomDatePicker(CreateResumeJobActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemJobStarttime.setText(time);
            }
        });
        datePickerBeginTime.showSpecificYearAndMonth(false);
        datePickerEndTime=new MyCustomDatePicker(CreateResumeJobActivity.this, new MyCustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tvResumeItemJobEndtime.setText(time);
            }
        });
        datePickerEndTime.showSpecificYearAndMonth(false);
    }

    private void initListener() {
        tvResumeItemResumejobSave.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

                if(isSubmit==false) {
                    if (!"".equals(resumeType)) {
                        if ("1".equals(resumeType)) {
                            saveData();
                        } else {
                            resumeUpdate();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isSubmit==false) {
                ago();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeExperience = dbOperator.query_ResumeWorkExperience(resumeIdString,
                resumeLanguageString);
        listResumeJobExp = new ArrayList<>();
        ResumeTitle resumeTitle1 = dbOperator.query_ResumeTitle_info("-1", resumeLanguageString);
        resumeType = resumeTitle1.getResume_type();
        if (resumeExperience!=null) {
            for (ResumeExperience resumeJobExp : resumeExperience) {
                listResumeJobExp.add(resumeJobExp);
            }
        }
        if (listResumeJobExp.size() == 0) {
            ResumeExperience resumeJobExp = new ResumeExperience();
            listResumeJobExp.add(resumeJobExp);
        }
        etResumeItemJobComname.setText(listResumeJobExp.get(groupPosition).getCompany());
        String startTime = listResumeJobExp.get(groupPosition).getFromyear() + "-" + listResumeJobExp.get(groupPosition).getFrommonth();
        String endTime = listResumeJobExp.get(groupPosition).getToyear() + "-" + listResumeJobExp.get(groupPosition).getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        if ("0".equals(listResumeJobExp.get(groupPosition).getSalary_hide())) {
            cbItemjobexpIscheck.setChecked(false);
        } else if ("1".equals(listResumeJobExp.get(groupPosition).getSalary_hide())) {
            cbItemjobexpIscheck.setChecked(true);
        }
        cbItemjobexpIscheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listResumeJobExp.get(groupPosition).setSalary_hide("1");
                } else {
                    listResumeJobExp.get(groupPosition).setSalary_hide("0");
                }
            }
        });
        tvResumeItemJobStarttime.setText(startTime);
        tvResumeItemJobEndtime.setText(endTime);
        etResumeItemJobPost.setText(listResumeJobExp.get(groupPosition).getPosition());
        etResumeItemJobSalary.setText(listResumeJobExp.get(groupPosition).getSalary());
        etResumeItemJobDescribe.setText(listResumeJobExp.get(groupPosition).getResponsibility());
        tvResumeItemJobStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                if(tvResumeItemJobStarttime.getText().toString()!=null){
                    s=tvResumeItemJobStarttime.getText().toString();
                }else{
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M");//格式为 2013-9
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    s = formatter.format(curDate);
                }
               datePickerBeginTime.show(s+"-1",1);
            }
        });
        tvResumeItemJobEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                if(tvResumeItemJobEndtime.getText().toString()!=null) {
                    s = tvResumeItemJobEndtime.getText().toString();
                }else{
                    s="至今";
                }
                if(!"至今".equals(s)){
                    s=s+"-1";
                }
                datePickerEndTime.show(s,2);
            }
        });
        tvResumeItemJobWorkplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectCityActicity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type", 1);
                intent.putExtra("from", "createJob");
                mContext.startActivity(intent);
            }
        });
        JSONArray cityJSONArray = null;
        try {
            if (MyUtils.USE_ONLINE_ARRAY) {
                cityJSONArray = NetService.getCityAsJSONArray(mContext, "city.json");
            } else {
                InputStream inputStream = mContext.getAssets().open("city_zh.json");
                cityJSONArray = new JSONArray(NetUtils.readAsString(
                        inputStream, Constants.ENCODE));
            }
            placeId = listResumeJobExp.get(groupPosition).getCompanyaddress();
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(placeId)) {
                    tvResumeItemJobWorkplace
                            .setText(object.getString(placeId));
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

    /**
     * 向服务器发送 修改默认简历app
     */
    private void sendIsApp() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.setimportant");
            requestParams.put("resume_id", resumeAppId);
            requestParams.put("resume_language", "zh");
            requestParams.put("important", "1");
            NetService service = new NetService(mContext, handlerIsApp);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlaceText(String value) {
        tvResumeItemJobWorkplace.setText(value);
    }

    public void setPlaceId(String string) {
        placeId = string;
    }

    private void saveData() {
        ResumeExperience resumeExperience = listResumeJobExp.get(groupPosition);
        if (etResumeItemJobComname.getText()
                .toString().length() == 0) {
            Toast.makeText(mContext, "请输入公司名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemJobStarttime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemJobStarttime
                .getText().toString())) {
            Toast.makeText(mContext, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!tvResumeItemJobEndtime.getText()
                .toString().contains("-")
                && !"至今".equals(tvResumeItemJobEndtime
                .getText().toString())) {
            Toast.makeText(mContext, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (tvResumeItemJobWorkplace.getText()
                .toString().length() == 0 || "请选择地点".equals(tvResumeItemJobWorkplace.getText().toString())) {
            Toast.makeText(mContext, "请选择工作地点", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemJobSalary.getText().toString()
                .length() == 0) {
            Toast.makeText(mContext, "请输入税前月薪", Toast.LENGTH_LONG).show();
            return;
        }
        if ("0".equals(etResumeItemJobSalary.getText().toString()
                .trim().substring(0, 1))) {
            Toast.makeText(mContext, "请输入大于0的税前月薪", Toast.LENGTH_LONG).show();
            return;
        }

        if (etResumeItemJobPost.getText()
                .toString().length() == 0) {
            Toast.makeText(mContext, "请输入所任职位", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemJobDescribe.getText()
                .toString().length() == 0) {
            Toast.makeText(mContext, "请输入职位描述", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = tvResumeItemJobStarttime
                .getText().toString().trim();
        String[] starttimeStrings;

        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = tvResumeItemJobEndtime
                .getText().toString().trim();
        String[] endtimeStrings;
        if ("至今".equals(endtimeString)) {
            endtimeStrings = new String[]{"0", "0"};
        } else {
            endtimeStrings = endtimeString.split("-");
        }
        if (starttimeStrings.length < 1) {
            Toast.makeText(mContext, "请填写开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endtimeStrings.length < 1) {
            Toast.makeText(mContext, "请填写结束时间", Toast.LENGTH_SHORT).show();
            return;
        }
        // 时间段过滤
        int startYear = Integer.parseInt(starttimeStrings[0]);
        int startMonth = Integer.parseInt(starttimeStrings[1]);
        int endYear = Integer.parseInt(endtimeStrings[0]);
        int endMonth = Integer.parseInt(endtimeStrings[1]);
        if (endYear < startYear&&!"至今".equals(tvResumeItemJobEndtime.getText().toString())) {
            Toast.makeText(mContext, mContext.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth&&!"至今".equals(tvResumeItemJobEndtime.getText().toString())) {
            Toast.makeText(mContext, mContext.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        String companynameString = etResumeItemJobComname
                .getText().toString().trim();
        String salaryString = etResumeItemJobSalary.getText().toString().trim();
        // 所任职位
        String positionString = etResumeItemJobPost
                .getText().toString().trim();
        // 职责描述
        String responsibilityString = etResumeItemJobDescribe
                .getText().toString().trim();
        String workPlaceId = CityNameConvertCityID.convertCityID(mContext, tvResumeItemJobWorkplace.getText().toString());
        resumeExperience.setFromyear(starttimeStrings[0]);
        resumeExperience.setFrommonth(starttimeStrings[1]);
        resumeExperience.setToyear(endtimeStrings[0]);
        resumeExperience.setTomonth(endtimeStrings[1]);
        resumeExperience.setCompanyaddress(workPlaceId);
        resumeExperience.setCompany(companynameString);
        resumeExperience.setSalary(salaryString);
        resumeExperience.setPosition(positionString);
        resumeExperience.setResponsibility(responsibilityString);
        resumeExperience.setResume_id(resumeIdString);
        resumeExperience.setResume_language(resumeLanguageString);
        resumeExperience.setUser_id(MyUtils.userID);
        if (resumeExperience.getId() == -1) {// 新建后保存

            ArrayList<ResumeExperience> arrayList = new ArrayList<ResumeExperience>();
            arrayList.add(resumeExperience);
            int resultInsert = dbOperator
                    .Insert_ResumeWorkExperience(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(mContext, dbOperator,
                        resumeIdString, resumeLanguageString);
//                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                if (MyUtils.ableInternet) {
                    resumeUpdate();
                } else {
                    Toast.makeText(mContext, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator
                    .update_ResumeWorkExperience(resumeExperience);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(mContext, dbOperator,
                        resumeIdString, resumeLanguageString);
                if (MyUtils.ableInternet) {
                    resumeUpdate();
                } else {
                    Toast.makeText(mContext, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 简历刷新或上传
     */
    private void resumeUpdate() {
        if (dbOperator!=null){
            //记录本地信息
            ResumeTitle titleZhUpdate22 = dbOperator.query_ResumeTitle_info("-1", "zh");
            titleZhUpdate22.setResume_type(resumeType);
//        boolean updateBaseinfoResult = dbOperator.update_ResumeTitle_info(titleZhUpdate22);
            // 检测本地中文基本信息
            ResumeBaseInfo baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
            ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info("-1", "zh");
            /**
             * 中文简历完整度达到60%，才能确定是否上传英文简历
             *
             */
            if ((titleZhUpdate != null && titleZhUpdate.getIsUpdate() == 1)
                    || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 1)) {
                // --------- 上传中文简历
                ResumeInfoToJsonString resumeInfoToJsonString = new ResumeInfoToJsonString(
                        mContext, "-1", "zh");
                String baseInfoString = resumeInfoToJsonString
                        .getBaseInofJsonString();
                String languageString = resumeInfoToJsonString
                        .getLanguageJsonString();
                String resumeInfoString = resumeInfoToJsonString
                        .getResumeDetailInfoJsonString();
                isSubmit=true;
                tvResumeItemNewresumeeduDelete.setBackgroundResource(R.drawable.btn_1changgray);
                tvResumeItemResumejobSave.setBackgroundResource(R.drawable.btn_1changgray);
                AsyncResumeUpdate asyncResumeUpdate = new AsyncResumeUpdate(
                        mContext, handlerUploadResume, baseInfoString,
                        languageString, resumeInfoString, "-1", "zh");
                asyncResumeUpdate.execute();
            } else if ((titleZhUpdate != null && titleZhUpdate
                    .getIsUpdate() == 0)
                    || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 0)) {
                // ---------中文不需要上传,检测英文简历
                Message message = handlerUploadResume.obtainMessage();
                message.what = 0;
                message.arg1 = Integer.parseInt("-1");
                handlerUploadResume.sendMessage(message);
            }
        }
    }

    @OnClick({R.id.iv_createresume_job_back, R.id.tv_isjobexp_open, R.id.cb_itemjobexp_ischeck, R.id.tv_resume_item_newresumeedu_ago, R.id.tv_resume_item_resumejob_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_createresume_job_back:
                if(isSubmit==false) {
                    ago();
                }
                break;
            case R.id.tv_isjobexp_open:
                //如果有工作经验 点击变为无工作经验
                Drawable dra1 = getResources().getDrawable(R.mipmap.kaiguan_1);
                Drawable dra2 = getResources().getDrawable(R.mipmap.kaiguan_2);
                dra1.setBounds(0, 0, dra1.getMinimumWidth(), dra1.getMinimumHeight());
                dra2.setBounds(0, 0, dra2.getMinimumWidth(), dra2.getMinimumHeight());
                if ("1".equals(resumeType)) {
                    tvIsjobexpOpen.setCompoundDrawablePadding(2);
                    tvIsjobexpOpen.setCompoundDrawables(dra1, null, null, null);
                    tvIsjobexpOpen.setText("没有工作经历");
                    lrJobactivity.setVisibility(View.GONE);
                    resumeType = "2";
                    ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info("-1", "zh");
                    titleZhUpdate.setResume_type(resumeType);
                    dbOperator.Delete_Data("ResumeList");
                    dbOperator.Insert_ResumeList(titleZhUpdate);
//                    dbOperator.update_ResumeTitle_info(titleZhUpdate);
                    // 检测本地中文基本信息
                } else {
                    //如果没有工作经验 点击变为有工作经验
                    tvIsjobexpOpen.setCompoundDrawablePadding(2);
                    tvIsjobexpOpen.setCompoundDrawables(dra2, null, null, null);
                    tvIsjobexpOpen.setText("有工作经历");
                    lrJobactivity.setVisibility(View.VISIBLE);
                    resumeType = "1";
                    ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info("-1", "zh");
                    titleZhUpdate.setResume_type(resumeType);
                    dbOperator.Delete_Data("ResumeList");
                    dbOperator.Insert_ResumeList(titleZhUpdate);
                    // 检测本地中文基本信息
                }
                break;
            case R.id.tv_resume_item_newresumeedu_ago:
                if(isSubmit==false) {
                    ago();
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void ago() {
        Intent intent = new Intent(mContext, CreateResumeEduActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
