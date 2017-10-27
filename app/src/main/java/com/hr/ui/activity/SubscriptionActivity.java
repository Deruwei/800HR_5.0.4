package com.hr.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.netutils.Async_GetNotice;
import com.hr.ui.utils.netutils.Async_SetRobJob;
import com.hr.ui.utils.netutils.Async_Set_Rob_Switch;
import com.hr.ui.view.custom.WheelMain;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订阅设置
 */
public class SubscriptionActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_subscription_back)
    ImageView ivSubscriptionBack;
    @Bind(R.id.et_subscription_keyword)
    EditText etSubscriptionKeyword;
    @Bind(R.id.tv_subscription_post)
    TextView tvSubscriptionPost;
    @Bind(R.id.tv_subscription_place)
    TextView tvSubscriptionPlace;
    @Bind(R.id.tv_subscription_jobexp)
    TextView tvSubscriptionJobexp;
    @Bind(R.id.tv_subscription_releasetime)
    TextView tvSubscriptionReleasetime;
    @Bind(R.id.tv_subscription_edu)
    TextView tvSubscriptionEdu;
    @Bind(R.id.tv_subscription_scaleofcom)
    TextView tvSubscriptionScaleofcom;
    @Bind(R.id.tv_subscription_salary)
    TextView tvSubscriptionSalary;
    @Bind(R.id.tv_subscription_category)
    TextView tvSubscriptionCategory;
    @Bind(R.id.tv_subscription_receivetime)
    TextView tvSubscriptionReceivetime;
    @Bind(R.id.iv_subscription_subsound)
    ImageView ivSubscriptionSubsound;
    @Bind(R.id.iv_subscription_subinfo)
    ImageView ivSubscriptionSubinfo;
    @Bind(R.id.iv_subscription_subcomemail)
    ImageView ivSubscriptionSubcomemail;
    @Bind(R.id.tv_subscription_save)
    TextView tvSubscriptionSave;
    @Bind(R.id.rl_subscription_save)
    RelativeLayout rlSubscriptionSave;

    private PopupWindow dialog;
    public static DisplayMetrics display;
    private String[] notification_timeString = new String[2];
    // 抢工作设置页数据
    private String func;
    private String area;
    private String searchword;
    private String workyear;
    private String worktype;
    private String issuedate;
    private String study;
    private String stuffmunber;
    private String salary;
    private HashMap<String, String> setVoicehashMap;
    private HashMap<String, String> getVoicehashMap;
    public static HashMap<String, String> dataHashMap;
    private int rushjob_state = 2, // 抢工作通知
            invite_state = 2,// 面试通知(人事经理来信)
            notification_voice = 2;// 消息提示音

    public String placeId = ""; // 地区 ID
    public String funcid;// 职能ID
    private String industry;
    private String cityName="";
    public static SubscriptionActivity subscriptionActivity = null;

    // data
    private List<FunctionBean> functionSelectedMap = new ArrayList<>();// key：职能id，value：职能名称
    private Map<String, String> zhixiSelectedMap = new HashMap<String, String>();
    /***
     * 根据网络返回数据，设置开关
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                getVoicehashMap = (HashMap<String, String>) msg.obj;
                try {
                    notification_voice = Integer.parseInt(getVoicehashMap.get("sound_state"));// 推送提示音1：开2：关
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    rushjob_state = Integer.parseInt(getVoicehashMap.get("rushjob_state"));// 抢工作通知状态1：开2：关
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    invite_state = Integer.parseInt(getVoicehashMap.get("invite_state"));// 企业来信状态1：开2：关
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String notice_bgntime = getVoicehashMap.get("notice_bgntime");// 允许通知开始时间（HH:MM）
                String notice_endtime = getVoicehashMap.get("notice_endtime");// 允许通知结束时间（HH:MM）

                setSwitch(notification_voice, ivSubscriptionSubsound);
                setSwitch(rushjob_state, ivSubscriptionSubinfo);
                setSwitch(invite_state, ivSubscriptionSubcomemail);
                tvSubscriptionReceivetime.setText(notice_bgntime + "~" + notice_endtime);
                notification_timeString[0] = notice_bgntime;
                notification_timeString[1] = notice_endtime;

                // 抢工作条件
                industry = getVoicehashMap.get("industry");
                func = getVoicehashMap.get("func");
                area = getVoicehashMap.get("area");
                searchword = getVoicehashMap.get("searchword");
                workyear = getVoicehashMap.get("workyear");
                worktype = getVoicehashMap.get("worktype");
                issuedate = getVoicehashMap.get("issuedate");
                study = getVoicehashMap.get("study");
                stuffmunber = getVoicehashMap.get("stuffmunber");
                salary = getVoicehashMap.get("salary");

                StringBuffer buffer = new StringBuffer();
                // 职能
                if (func != null && func.length() > 0 && !func.equals("0")) {
                    String[] fz = func.split(",");
                    String temp = "";
                    for (int i = 0; i < fz.length; i++) {
                        if (fz[i].contains("|")) {
                            if (!temp.equals(fz[i].substring(0,
                                    fz[i].indexOf("|")))) {
                                temp = fz[i].substring(0, fz[i].indexOf("|"));
                                buffer.append("、"
                                        + ResumeInfoIDToString.getFunc(
                                        SubscriptionActivity.this,
                                        industry, temp));
                            }
                        } else {
                            buffer.append("、"
                                    + ResumeInfoIDToString.getFunc(
                                    SubscriptionActivity.this,
                                    industry, fz[i]));
                        }
                    }
                    // 过滤"、"
                    while (buffer.toString().startsWith("、")) {
                        buffer.deleteCharAt(0);
                    }
                }
                // 地点
                if (area != null && area.length() > 0 && !area.equals("0")) {
                    buffer.append("+"
                            + ResumeInfoIDToString.getPlace(
                            SubscriptionActivity.this, area, true));
                }
                // 关键字
                if (searchword != null && searchword.length() > 0) {
                    buffer.append("+" + searchword);
                }
                // 工作经验
                HashMap<String, String> workyearHashMap = new HashMap<String, String>();
                // workyearHashMap.put("0", "不限");
                workyearHashMap.put("10", "无要求");
                workyearHashMap.put("11", "应届生");
                workyearHashMap.put("13", "一年以上");
                workyearHashMap.put("15", "三年以上");
                workyearHashMap.put("17", "五年以上");
                workyearHashMap.put("20", "八年以上");
                if (workyear != null && workyear.length() > 0
                        && !workyear.equals("0")) {
                    if (workyearHashMap.containsKey(workyear)) {
                        buffer.append("+" + workyearHashMap.get(workyear));
                    }
                }
               // private String timeStr[] = {"不限", "近一天","近两天","近三天", "近一周","近两周" ,"近一个月", "近六周","近两月"};
                //private String timeStrIds[] = {"0", "1", "2", "3", "7", "14","30","42","60"};
                workyearHashMap.clear();
                // 发布时间
                HashMap<String, String> issueHashMap = new HashMap<String, String>();
                 issueHashMap.put("0", "不限");
                issueHashMap.put("1", "近一天");
                issueHashMap.put("2", "近二天");
                issueHashMap.put("3", "近三天");
                issueHashMap.put("7", "近一周");
                issueHashMap.put("14", "近两周");
                issueHashMap.put("30", "近一个月");
                issueHashMap.put("42", "近六周");
                issueHashMap.put("60", "近两月");
                if (issuedate != null && issuedate.length() > 0
                        && !issuedate.equals("0")) {
                    if (issueHashMap.containsKey(issuedate)) {
                        buffer.append("+" + issueHashMap.get(issuedate));
                    }
                }
                issueHashMap.clear();
                // 学历要求
                HashMap<String, String> studyHashMap = new HashMap<String, String>();
                // studyHashMap.put("0", "不限");
                studyHashMap.put("10", "无要求");
                studyHashMap.put("11", "大专以下");
                studyHashMap.put("15", "大专及以上");
                studyHashMap.put("16", "本科及以上");
                studyHashMap.put("17", "硕士及以上");
                studyHashMap.put("19", "博士及以上");
                if (study != null && study.length() > 0 && !study.equals("0")) {
                    if (studyHashMap.containsKey(issuedate)) {
                        buffer.append("+" + studyHashMap.get(study));
                    }
                }
                studyHashMap.clear();
                // 公司规模
                HashMap<String, String> stuffHashMap = new HashMap<String, String>();
                // stuffHashMap.put("0", "不限");
                stuffHashMap.put("12", "1-49人");
                stuffHashMap.put("13", "50-99人");
                stuffHashMap.put("14", "100-499人");
                stuffHashMap.put("15", "500-999人");
                stuffHashMap.put("16", "1000人以上");
                if (stuffmunber != null && stuffmunber.length() > 0
                        && stuffmunber != "0") {
                    if (stuffHashMap.containsKey(stuffmunber)) {
                        buffer.append("+" + stuffHashMap.get(stuffmunber));
                    }
                }
                stuffHashMap.clear();
                // 薪资待遇
                HashMap<String, String> monthly_playHashMap = new HashMap<String, String>();
                // monthly_playHashMap.put("0", "不限");
               // private String salStr[] = {"不限", "2000以下", "2000-4000", "4000-6000", "6000-8000","8000-10000", "10000-15000", "15000-20000","20000-30000","30000-50000","50000以上"};
               // private String salStrIds[] = {"0","10","11", "12", "13", "14","15", "16", "17", "18","19"};
                monthly_playHashMap.put("0", "不限");
                monthly_playHashMap.put("10", "2000以下");
                monthly_playHashMap.put("11", "2000-4000");
                monthly_playHashMap.put("12", "4000-6000");
                monthly_playHashMap.put("13", "6000-8000");
                monthly_playHashMap.put("14", "8000-10000");
                monthly_playHashMap.put("15", "10000-15000");
                monthly_playHashMap.put("16", "15000-20000");
                monthly_playHashMap.put("17", "20000-30000");
                monthly_playHashMap.put("18", "30000-50000");
                monthly_playHashMap.put("19", "50000以上");
                if (salary != null && salary.length() > 0
                        && !salary.equals("0")) {
                    if (monthly_playHashMap.containsKey(salary)) {
                        buffer.append("+" + monthly_playHashMap.get(salary));
                    }
                }
                monthly_playHashMap.clear();
                // 工作性质
                if (worktype != null && worktype.length() > 0
                        && !worktype.equals("0")) {
                    buffer.append("+"
                            + ResumeInfoIDToString.getWorkType(
                            SubscriptionActivity.this, worktype, true));
                }

                // 过滤"+"
                while (buffer.toString().startsWith("+")) {
                    buffer.deleteCharAt(0);
                }

//                if (buffer.toString().equals("")) {
//                    set_rob_work_value.setVisibility(View.GONE);
//                } else {
//                    set_rob_work_value.setVisibility(View.VISIBLE);
//                    set_rob_work_value.setText(buffer.toString());
//                }
                if (buffer.length() > 1) {
                    buffer.delete(0, buffer.length() - 1);
                }
                buffer = null;
            }
            dataHashMap = new HashMap<String, String>();
            dataHashMap.put("industry", industry);// 行业
            dataHashMap.put("func", func);// 职能
            dataHashMap.put("area", area);// 地点
            dataHashMap.put("searchword", searchword);// 关键字
            dataHashMap.put("workyear", workyear);// 工作经验
            dataHashMap.put("issuedate", issuedate); // 发布时间
            dataHashMap.put("study", study);// 学历要求
            dataHashMap.put("stuffmunber", stuffmunber);// 公司规模
            dataHashMap.put("salary", salary);// 薪资待遇
            dataHashMap.put("worktype", worktype);// 工作性质
            initData();
        }

        ;
    };

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
        setContentView(R.layout.activity_subscription);
        ButterKnife.bind(this);
        display = getResources().getDisplayMetrics();
        refreshData();
        subscriptionActivity = SubscriptionActivity.this;
    }

    private void setSwitch(int sign, ImageView imageView) {
        if (sign == 2) {
            imageView.setImageResource(R.mipmap.kaiguan_guan);
        } else if (sign == 1) {
            imageView.setImageResource(R.mipmap.kaiguan_kai);
        }
    }

    /***
     * 获取网络数据
     */
    public void refreshData() {
        if (MyUtils.ALIAS == null || MyUtils.ALIAS.length() == 0) {
            Toast.makeText(this, "设备信息获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        new Async_GetNotice(SubscriptionActivity.this, handler).execute("job.getnotice", MyUtils.ALIAS);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.iv_subscription_back, R.id.tv_subscription_post, R.id.tv_subscription_place, R.id.tv_subscription_jobexp, R.id.tv_subscription_releasetime, R.id.tv_subscription_edu, R.id.tv_subscription_scaleofcom, R.id.tv_subscription_salary, R.id.tv_subscription_category, R.id.tv_subscription_receivetime, R.id.iv_subscription_subsound, R.id.iv_subscription_subinfo, R.id.iv_subscription_subcomemail, R.id.rl_subscription_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_subscription_back:
                finish();
                break;
            case R.id.tv_subscription_post:
                // 加载职能选择页
               /* if (SelectFunctionSubscriptionActivity!= null) {
                    SelectFunctionSubscriptionActivity.selectMap.clear();
                }*/
                Intent intentFunction = new Intent(this, MySelectFuncitonActivity.class);
                intentFunction.putExtra("filter", "subscription");
                intentFunction.putExtra("selectMap", (Serializable) functionSelectedMap);
                intentFunction.putExtra("value", "职能");
                startActivity(intentFunction);
                break;
            case R.id.tv_subscription_place:
                Intent intentPlace = new Intent(this, SelectCityActicity.class);
                intentPlace.putExtra("type", "1");
                intentPlace.putExtra("from", "subscription");
                startActivity(intentPlace);
                break;
            case R.id.tv_subscription_jobexp:
                Intent intentJobExp = new Intent(this, SubJobExpActivity.class);
                startActivity(intentJobExp);
                break;
            case R.id.tv_subscription_releasetime:
                Intent intentTime = new Intent(this, SubTimeActivity.class);
                startActivity(intentTime);
                break;
            case R.id.tv_subscription_edu:
                Intent intentEdu = new Intent(this, SubEduActivity.class);
                startActivity(intentEdu);
                break;
            case R.id.tv_subscription_scaleofcom:
                Intent intentComScale = new Intent(this, SubComScaleActivity.class);
                startActivity(intentComScale);
                break;
            case R.id.tv_subscription_salary:
                Intent intentSal = new Intent(this, SubSalaryActivity.class);
                startActivity(intentSal);
                break;
            case R.id.tv_subscription_category:
                Intent intentCategory = new Intent(this, SubJobNatureActivity.class);
                startActivity(intentCategory);
                break;
            case R.id.tv_subscription_receivetime:
                showDateTimePicker(view);
                break;
            case R.id.iv_subscription_subsound:
                NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.mipmap.logo_1);
                if (notification_voice == 1) {
                    ivSubscriptionSubsound.setImageResource(R.mipmap.kaiguan_guan);
                    notification_voice = 2;
                    builder.setSound(null);
                } else if (notification_voice == 2) {
                    ivSubscriptionSubsound.setImageResource(R.mipmap.kaiguan_kai);
                    notification_voice = 1;
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                }
                Notification notification=builder.build();
                manger.notify(1,notification);
                break;
            case R.id.iv_subscription_subinfo:
                if (rushjob_state == 2) {
                    rushjob_state = 1;
                } else if (rushjob_state == 1) {
                    rushjob_state = 2;
                } else {
                    return;
                }
                setSwitch(rushjob_state, ivSubscriptionSubinfo);
                break;
            case R.id.iv_subscription_subcomemail:
                if (invite_state == 2) {
                    invite_state = 1;
                } else if (invite_state == 1) {
                    invite_state = 2;
                } else {
                    return;
                }
                setSwitch(invite_state, ivSubscriptionSubcomemail);
                break;
            case R.id.rl_subscription_save:
                saveData();
                break;
        }
    }

    private Handler setRob_handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {// 抢工作设置成功
                try {
                    if (subscriptionActivity != null) {
                        subscriptionActivity.refreshData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(SubscriptionActivity.this, R.string.setRobJob_succes, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    };

    private void saveData() {
        new Async_Set_Rob_Switch(SubscriptionActivity.this, setRequestArgument()).execute();
        // 获得搜索关键字
        String searchword = etSubscriptionKeyword.getText().toString();
        // 获取职能id
        if (functionSelectedMap != null) {
            StringBuffer funcidBuffer = new StringBuffer();
            for (int i=0;i<functionSelectedMap.size();i++) {
                String string = functionSelectedMap.get(i).getId();
                if (zhixiSelectedMap != null && zhixiSelectedMap.size() > 0) {
                    if ((string.contains("263") || string.contains("264")
                            || string.contains("265")
                            || string.contains("266")
                            || string.contains("267") || string
                            .contains("268"))) {
                        for (Map.Entry<String, String> entry : zhixiSelectedMap
                                .entrySet()) {
                            funcidBuffer.append(string + "|"
                                    + entry.getKey() + ",");
                        }
                    } else {
                        funcidBuffer.append(string + ",");
                    }
                } else {
                    funcidBuffer.append(string + ",");
                }
            }
            if (funcidBuffer.length() > 0) {
                funcid = funcidBuffer.substring(0,
                        funcidBuffer.length() - 1);
            } else {
                funcid = "0";
            }
        }
        // 获得行业Id
        industry = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE).getInt(Constants.INDUSTRY, 11)
                + "";// 默认建筑
        // 验证信息
        if ((funcid == null || funcid.length() == 0 || "0".equals(funcid))
                && (searchword == null || searchword.trim().length() == 0)) {

            Toast.makeText(SubscriptionActivity.this, "请输入关键字或选择职能", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        //Log.i("城市的id","-----"+placeId+"");
        if (placeId == null||cityName.equals("")) {
            Toast.makeText(SubscriptionActivity.this, "请选择地点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MyUtils.ALIAS == null || MyUtils.ALIAS.length() == 0) {
            Toast.makeText(SubscriptionActivity.this, "手机IMEI获取失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (industry == null || industry.length() == 0) {
            Toast.makeText(SubscriptionActivity.this, "行业获取失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // System.out.println("end funcid==" + funcid);
        new Async_SetRobJob(SubscriptionActivity.this, setRob_handler).execute(
                "job.setrushjob", funcid == null ? "" : funcid, placeId,
                searchword, industry, MyUtils.ALIAS,
                dataHashMap.get("workyear"), dataHashMap.get("worktype"),
                dataHashMap.get("issuedate"), dataHashMap.get("study"),
                dataHashMap.get("stuffmunber"), dataHashMap.get("salary"));

    }

    public void showDateTimePicker(View v) {
        int with = Math.round(display.widthPixels / 5 * 4);
        // int with = Math.round(display.widthPixels / 5* 4);

        dialog = new PopupWindow(tvSubscriptionReceivetime, with,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.showtimedialog, null);

        final WheelMain main = new WheelMain(dialog, view);
        main.showDateTimePicker(v);

        Button btn_sure = (Button) view.findViewById(R.id.setTime);
        Button btn_cancel = (Button) view.findViewById(R.id.cannel);
        //
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int[] notification_timeInt = main.getTime();
                if (notification_timeInt[0] >= notification_timeInt[1]) {
                    Toast.makeText(SubscriptionActivity.this, "请正确选择时间", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    notification_timeString[0] = notification_timeInt[0] + ":00";
                    notification_timeString[1] = notification_timeInt[1] + ":00";
                    tvSubscriptionReceivetime.setText(notification_timeString[0] + "~" + notification_timeString[1]);
                }
            }
        });
        // 取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }


    /***
     * 配置请求参数
     */
    private HashMap<String, String> setRequestArgument() {
        setVoicehashMap = new HashMap<String, String>();
        setVoicehashMap.put("method", "job.setnotice");
        setVoicehashMap.put("rushjob_state", rushjob_state + "");
        setVoicehashMap.put("invite_state", invite_state + "");
        setVoicehashMap.put("sound_state", notification_voice + "");
        setVoicehashMap.put("notice_bgntime", notification_timeString[0]);
        setVoicehashMap.put("notice_endtime", notification_timeString[1]);
        setVoicehashMap.put("phonecode", MyUtils.ALIAS);
        setVoicehashMap.put("baidu_user_id", MyUtils.baidu_push_userId);
        setVoicehashMap.put("baidu_channel_id", MyUtils.baidu_channel_id);
        setVoicehashMap.put("push_way", "2");// （0原生，1百度，2极光）
        return setVoicehashMap;
    }

    public void initData() {
        if (dataHashMap != null) {
            // 初始化字段值
            if (dataHashMap.get("searchword").equals("0")) {
                etSubscriptionKeyword.setText("");// 关键字
            } else {
                etSubscriptionKeyword.setText(dataHashMap.get("searchword"));// 关键字
            }
            funcid = dataHashMap.get("func");
            // System.out.println("start funcID" + funcid);
            industry = dataHashMap.get("industry");
            if (funcid != null && funcid.length() > 0) {// 职能
                String[] fz = funcid.split(",");
                String tempfid = "";
                String tempfStr = "";
                StringBuffer funcTextBuffer = new StringBuffer();
                for (int i = 0; i < fz.length; i++) {
                    if (fz[i].contains("|")) {// 拼接的funcID|zhixiID
                        if (!tempfid.equals(fz[i].substring(0,
                                fz[i].indexOf("|")))) {
                            tempfid = fz[i].substring(0, fz[i].indexOf("|"));
                            tempfStr = ResumeInfoIDToString.getFunc(this,
                                    industry, tempfid);
                            funcTextBuffer.append("、" + tempfStr);
                            if (functionSelectedMap != null) {// 存到map，用于传送map到职位选择页
                                FunctionBean functionBean=new FunctionBean();
                                functionBean.setId(tempfid);
                                functionBean.setName(tempfStr);
                                functionBean.setSelect(true);
                                functionSelectedMap.add(functionBean);
                            }
                        }
                    } else {
                        if(!fz[i].equals("0")) {
                            tempfStr = ResumeInfoIDToString.getFunc(this, industry,
                                    fz[i]);
                            funcTextBuffer.append("、" + tempfStr);
                            if (functionSelectedMap != null) {// 存到map，用于传送map到职位选择页
                                FunctionBean functionBean = new FunctionBean();
                                functionBean.setId(fz[i]);
                                functionBean.setName(tempfStr);
                                Log.i("fzid", fz[i]);
                                Log.i("fzidname", tempfStr);
                                functionBean.setSelect(true);
                                functionSelectedMap.add(functionBean);
                            }
                        }
                    }
                }
                // 保存职系
                String tempZid = "";
                String tempZStr = "";
                for (int i = 0; i < fz.length; i++) {
                    if (fz[i].contains("|")) {// 有职系
                        if (tempfid.equals(fz[i].substring(0,
                                fz[i].indexOf("|")))) {
                            tempZid = fz[i].substring(fz[i].indexOf("|" + 1));
                            tempZStr = ResumeInfoIDToString.getZhixiString(
                                    this, tempZid);
                            if (zhixiSelectedMap != null) {
                                zhixiSelectedMap.put(tempZid, tempZStr);
                            }
                        }
                    }
                }
                // 过滤"、"
                while (funcTextBuffer.toString().startsWith("、")) {
                    funcTextBuffer.deleteCharAt(0);
                }
                tvSubscriptionPost.setText(funcTextBuffer.toString());
            }
            placeId = dataHashMap.get("area");
            if (ResumeInfoIDToString.getPlace(this, placeId, true).equals("")) {
                tvSubscriptionPlace.setText("全国");
                cityName="全国";
            } else {
                cityName=ResumeInfoIDToString.getPlace(this, placeId, true);
                tvSubscriptionPlace.setText(ResumeInfoIDToString.getPlace(this, placeId, true));// 地点
            }
            tvSubscriptionJobexp.setText(JudgeValue_workyear(dataHashMap.get("workyear")));// 工作经验
            tvSubscriptionScaleofcom.setText(JudgeValue_company_number(dataHashMap.get("stuffmunber")));// 公司规模
            tvSubscriptionEdu.setText(JudgeValue_education_background(dataHashMap.get("study")));// 学历要求
            tvSubscriptionReleasetime.setText(JudgeValue_Issue_date(dataHashMap.get("issuedate")));// 发布时间
            tvSubscriptionCategory.setText(JudgeValue_nature_number(dataHashMap.get("worktype")));// 工作性质
            tvSubscriptionSalary.setText(JudgeValue_text_money(dataHashMap.get("salary")));// 薪资待遇
        }
    }

    /***
     * 工作年限
     */
    private String JudgeValue_workyear(String text_jobid) {
        // System.out.println("******" + text_jobid);
        if ("0".equals(text_jobid)) {
            return "不限";
        } else if ("10".equals(text_jobid)) {
            return "无要求";
        } else if ("11".equals(text_jobid)) {
            return "应届生";
        } else if ("13".equals(text_jobid)) {
            return "一年以上";
        } else if ("15".equals(text_jobid)) {
            return "三年以上";
        } else if ("17".equals(text_jobid)) {
            return "五年以上";
        } else if ("20".equals(text_jobid)) {
            return "八年以上";
        }

        return "不限";
    }

    /***
     * 发布时间
     */
    private String JudgeValue_Issue_date(String text_timeid) {
        // private String timeStr[] = {"不限", "近一天","近两天","近三天", "近一周","近两周" ,"近一个月", "近六周","近两月"};
        //private String timeStrIds[] = {"0", "1", "2", "3", "7", "14","30","42","60"};
        if ("0".equals(text_timeid)) {
            return "不限";
        } else if ("1".equals(text_timeid)) {
            return "近一天";
        } else if ("2".equals(text_timeid)) {
            return "近二天";
        } else if ("3".equals(text_timeid)) {
            return "近三天";
        } else if ("7".equals(text_timeid)) {
            return "近一周";
        } else if ("14".equals(text_timeid)) {
            return "近两周";
        } else if ("30".equals(text_timeid)) {
            return "近一个月";
        } else if ("42".equals(text_timeid)) {
            return "近六周";
        } else if ("60".equals(text_timeid)) {
            return "近两月";
        }

        return "不限";
    }

    /***
     * 学历要求
     */
    private String JudgeValue_education_background(
            String text_education_backgroundid) {

        if ("0".equals(text_education_backgroundid)) {
            return "不限";
        } else if ("10".equals(text_education_backgroundid)) {
            return "无要求";
        } else if ("11".equals(text_education_backgroundid)) {
            return "大专以下";
        } else if ("15".equals(text_education_backgroundid)) {
            return "大专及以上";
        } else if ("16".equals(text_education_backgroundid)) {
            return "本科及以上";
        } else if ("17".equals(text_education_backgroundid)) {
            return "硕士及以上";
        } else if ("18".equals(text_education_backgroundid)) {
            return "博士及以上";
        }

        return "不限";

    }

    /***
     * 公司规模
     */
    private String JudgeValue_company_number(String text_scale_of_companyid) {
        if ("0".equals(text_scale_of_companyid)) {
            return "不限";
        } else if ("12".equals(text_scale_of_companyid)) {
            return "1-49人";
        } else if ("13".equals(text_scale_of_companyid)) {
            return "50-99人";
        } else if ("14".equals(text_scale_of_companyid)) {
            return "100-499人";
        } else if ("15".equals(text_scale_of_companyid)) {
            return "500-999人";
        } else if ("16".equals(text_scale_of_companyid)) {
            return "1000人以上";
        }
        return "不限";
    }

    /***
     * 期望薪资
     */
    //str = new String[]{"不限", "2000以下", "2000-4000", "4000-6000", "6000-8000","8000-10000", "10000-15000", "15000-20000","20000-30000","30000-50000","50000以上"};
   // strIds = new String[]{"0","10","11", "12", "13", "14","15", "16", "17", "18","19"};
    private String JudgeValue_text_money(String text_moneyid) {
        if ("0".equals(text_moneyid)) {
            return "不限";
        } else if ("10".equals(text_moneyid)) {
            return "2000以下";
        } else if ("11".equals(text_moneyid)) {
            return "2000-4000";
        } else if ("12".equals(text_moneyid)) {
            return "4000-6000";
        } else if ("13".equals(text_moneyid)) {
            return "6000-8000";
        } else if ("14".equals(text_moneyid)) {
            return "8000-10000";
        } else if ("15".equals(text_moneyid)) {
            return "10000-15000";
        } else if ("16".equals(text_moneyid)) {
            return "15000-20000";
        } else if ("17".equals(text_moneyid)) {
            return "20000-30000";
        } else if ("18".equals(text_moneyid)) {
            return "30000-50000";
        } else if ("19".equals(text_moneyid)) {
            return "50000以上";
        }

        return "不限";
    }

    /***
     * 工作性质
     */
    private String JudgeValue_nature_number(String text_job_natureid) {
        if ("0".equals(text_job_natureid)) {
            return "不限";
        } else if ("14".equals(text_job_natureid)) {
            return "无要求";
        } else if ("13".equals(text_job_natureid)) {
            return "全职";
        } else if ("12".equals(text_job_natureid)) {
            return "兼职";
        } else if ("11".equals(text_job_natureid)) {
            return "实习";
        }
        return "不限";
    }

    /**
     * 添加选择项
     */
    public void setFunctionSelected(List<FunctionBean> selectMap) {
        // System.out.println("setFunc:" + selectMap.toString());
        functionSelectedMap.clear();
        functionSelectedMap.addAll(selectMap);
        showText();
    }

    /**
     * 显示文本信息
     */
    private void showText() {
        if (functionSelectedMap.size() == 0) {
            tvSubscriptionPost.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<functionSelectedMap.size();i++ ) {
            String keyString = functionSelectedMap.get(i).getName();
            buffer.append(keyString + "、");
        }
        tvSubscriptionPost.setText(buffer.toString()
                .subSequence(0, buffer.length() - 1).toString().trim());
    }

    /**
     * 订阅地点设置
     *
     * @param value
     */
    protected void setPlaceText(String value) {
        // TODO Auto-generated method stub
        cityName=value;
        tvSubscriptionPlace.setText(value);
    }

    public void setPlaceId(String string) {
        // TODO Auto-generated method stub
        placeId = string;
    }
}
