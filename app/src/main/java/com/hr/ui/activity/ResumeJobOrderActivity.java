package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.IdSpineer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 求职意向
 *
 * @author Colin
 */
public class ResumeJobOrderActivity extends BaseResumeActivity implements View.OnClickListener {
    private static final String TAG = "ResumeJobOrderActivity";
    private Context mContext = ResumeJobOrderActivity.this;
    private List<FunctionBean> funcSelectedMap = new ArrayList<>();// key：职能id，value：职能名称
    private TextView sp_resume_joborder_state, sp_resume_joborder_worktype;
    private TextView tv_resume_personinfo_place, tv_resume_joborder_territory, tv_resume_joborder_position;
    private EditText et_resume_personinfo_salay;
    private ImageView iv_resume_joborder_back, iv_resume_joborder_salary;
    private String resumeIdString;
    private String resumeLanguageString;
    private boolean isCHS;
    private DAO_DBOperator dbOperator;
    private RelativeLayout rl_resume_joborder_save, rl_resume_joborder_state, rl_resume_joborder_worktype;
    private ResumeBaseInfo resumeBaseInfo;
    private ResumeOrder resumeOrder;
    private String selectJobTypeId,selectFindJobId;
    private String placeId = ""; // 地区 ID
    private String funcIdString = "";// 职能id
    private String lingyuIdString = "";// 领域id
    private CustomDatePicker datePickerFindJob,datePickerJobType;

    private static ResumeJobOrderActivity resumeJobOrderActivity = null;
    private List<SelectBean> jobTypeList=new ArrayList<>();
    private List<SelectBean> findJobList=new ArrayList<>();

    public static ResumeJobOrderActivity getInstance() {
        return resumeJobOrderActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_resume_job_order);
        modification = false;
        initView();
        initData();
        initDialog();
    }

    private void initData() {
        resumeJobOrderActivity = ResumeJobOrderActivity.this;
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeBaseInfo = dbOperator
                .query_ResumePersonInfo_Toone(resumeLanguageString);
        resumeOrder = dbOperator.query_ResumeCareerObjective_Toone(
                resumeIdString, resumeLanguageString);
        // System.out.println("基本信息：" + resumeBaseInfo);
        // System.out.println("求职意向：" + resumeOrder);
        if (resumeOrder == null) {
            resumeOrder = new ResumeOrder();
        } else {
            setDataToUI(resumeBaseInfo, resumeOrder);
        }
        if (resumeBaseInfo == null) {
            resumeBaseInfo = new ResumeBaseInfo();
        }
    }

    private void initView() {
        jobTypeList= GetResumeArrayList.getJobTypeListFromArray(this);
        findJobList=GetResumeArrayList.getFindJobListFromArray(this);
        sp_resume_joborder_state = (TextView) findViewById(R.id.sp_resume_joborder_state);
        tv_resume_joborder_position = (TextView) findViewById(R.id.tv_resume_joborder_position);
        tv_resume_joborder_territory = (TextView) findViewById(R.id.tv_resume_joborder_territory);
        sp_resume_joborder_worktype = (TextView) findViewById(R.id.sp_resume_joborder_worktype);
        et_resume_personinfo_salay = (EditText) findViewById(R.id.et_resume_personinfo_salay);

        rl_resume_joborder_save = (RelativeLayout) findViewById(R.id.rl_resume_joborder_save);
        rl_resume_joborder_state = (RelativeLayout) findViewById(R.id.rl_resume_joborder_state);
        rl_resume_joborder_worktype = (RelativeLayout) findViewById(R.id.rl_resume_joborder_worktype);

        tv_resume_personinfo_place = (TextView) findViewById(R.id.tv_resume_personinfo_place);

        iv_resume_joborder_back = (ImageView) findViewById(R.id.iv_resume_joborder_back);
        iv_resume_joborder_salary = (ImageView) findViewById(R.id.iv_resume_joborder_salary);

//        if (MyUtils.firstResume) {
//            rl_resume_joborder_save.setText("保存并进入下一项");
//        } else {
//            rl_resume_joborder_save.setText("保存修改");
//        }
        rl_resume_joborder_save.setOnClickListener(this);
        iv_resume_joborder_back.setOnClickListener(this);
        tv_resume_personinfo_place.setOnClickListener(this);
        tv_resume_joborder_territory.setOnClickListener(this);
        tv_resume_joborder_position.setOnClickListener(this);
        iv_resume_joborder_salary.setOnClickListener(this);
        rl_resume_joborder_state.setOnClickListener(this);
        rl_resume_joborder_worktype.setOnClickListener(this);

        et_resume_personinfo_salay.setOnClickListener(this);


        if ("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId)
                || "14".equals(MyUtils.industryId)
                || "29".equals(MyUtils.industryId)) {
            findViewById(R.id.rl_myresumejob_territory).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rl_myresumejob_territory).setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_resume_personinfo_salay:
                modification = true;
                break;
            case R.id.rl_resume_joborder_state:
                modification = true;
                datePickerFindJob.show(sp_resume_joborder_state.getText().toString());
                break;
            case R.id.rl_resume_joborder_worktype:
                modification = true;
                datePickerJobType.show(sp_resume_joborder_worktype.getText().toString());
                break;
            case R.id.rl_resume_joborder_save:
                if (MyUtils.ableInternet) {
                    saveToDB();
                } else {
                    Toast.makeText(ResumeJobOrderActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_resume_personinfo_place:
                modification = true;
                Intent intent = new Intent(this, SelectPlaceToResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromtag", 101);
                intent.putExtra("placeId", placeId);
                intent.putExtra("isCHS", isCHS);
                intent.putExtra("ableselected", 5);
                intent.putExtra("filter", "place");
                intent.putExtra("value", "选择城市");
                startActivity(intent);
                break;
            case R.id.tv_resume_joborder_position:
                modification = true;
                // 加载职能选择页
                Intent function = new Intent(mContext, MySelectFuncitonActivity.class);
                function.putExtra("filter", "resumeJobOrder");
                function.putExtra("selectMap", (Serializable) funcSelectedMap);
                function.putExtra("value", "职能");
                startActivity(function);
                break;
            case R.id.tv_resume_joborder_territory:
                modification = true;
//                Toast.makeText(mContext, "有反应", Toast.LENGTH_SHORT).show();
                //加载领域选择页
                Intent intentLingyu = new Intent(mContext, SelectTerritoryActivity.class);
                intentLingyu.putExtra("lingyuIdString", lingyuIdString);
                startActivity(intentLingyu);
                break;
            case R.id.iv_resume_joborder_back:
                showSaveDialog();
                break;
            case R.id.iv_resume_joborder_salary:
                modification = true;
                if (resumeOrder.getOrder_salary_noshow().equals("1")) {
                    iv_resume_joborder_salary.setImageResource(R.mipmap.hui);
                    resumeOrder.setOrder_salary_noshow("0");
                } else {
                    iv_resume_joborder_salary.setImageResource(R.mipmap.lv);
                    resumeOrder.setOrder_salary_noshow("1");
                }
                break;
        }
    }
    private void initDialog() {
        datePickerFindJob = new CustomDatePicker(ResumeJobOrderActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if("".equals(time)||time==null){
                    sp_resume_joborder_state.setText("请选择");
                }else {
                    sp_resume_joborder_state.setText(time);
                    for(int i=0;i<findJobList.size();i++) {
                        if(time.equals(findJobList.get(i).getName())){
                            selectFindJobId=findJobList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        },  getResources().getStringArray(R.array.array_workstate_zh));
        datePickerJobType = new CustomDatePicker(ResumeJobOrderActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if("".equals(time)||time==null){
                   sp_resume_joborder_worktype.setText("请选择");
                }else {
                    sp_resume_joborder_worktype.setText(time);
                    for(int i=0;i<jobTypeList.size();i++) {
                        if(time.equals(jobTypeList.get(i).getName())){
                            selectJobTypeId=jobTypeList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_jobtype_zh));
    }
    /**
     * 初始化工作地区
     *
     * @param placeIdString
     */
    private void initWorkPlace(String placeIdString) {
        placeId = placeIdString;
        if (placeIdString == null || placeIdString.length() == 0) {// 选择的城市为空
            tv_resume_personinfo_place.setText("");
            return;
        }
        JSONArray cityJSONArray = null;
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
            StringBuffer showText = new StringBuffer();
            if (placeId != null && placeId.length() > 0) {
                String[] placeStrings = placeId.split(",");
                for (String string : placeStrings) {
                    for (int i = 0; i < cityJSONArray.length(); i++) {
                        JSONObject object = cityJSONArray.getJSONObject(i);
                        if (object.has(string)) {
                            showText.append("," + object.getString(string));
                            break;
                        }
                    }
                }
                if (showText.length() > 0) {
                    showText.deleteCharAt(0);
                }
                tv_resume_personinfo_place.setText(showText
                        .toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 加载数据到UI
	 */
    private void setDataToUI(ResumeBaseInfo resumeBaseInfo, ResumeOrder resumeOrder) {
        if (resumeOrder == null) {
            return;
        }
        // 求职状态
        if (resumeBaseInfo != null) {
            String wokestateString = resumeBaseInfo.getCurrent_workstate();
            if (wokestateString != null) {
                selectFindJobId=wokestateString;
                for(int i=0;i<findJobList.size();i++){
                    if(wokestateString.equals(findJobList.get(i).getId())){
                        sp_resume_joborder_state.setText(findJobList.get(i).getName());
                        break;
                    }
                }
            }
        }
        // 期望工作性质
        String jobTypeString = resumeOrder.getJobtype();
        if (jobTypeString != null) {
            selectJobTypeId=jobTypeString;
            for(int i=0;i<jobTypeList.size();i++){
                if(jobTypeString.equals(jobTypeList.get(i).getId())){
                    sp_resume_joborder_worktype.setText(jobTypeList.get(i).getName());
                    break;
                }
            }
        }
        if (isCHS) {
            // 期望期望领域
            lingyuIdString = resumeOrder.getLingyu();
            initLingyu(lingyuIdString);
            // 期望期望职位
            StringBuffer showBuffer = new StringBuffer();
            funcIdString = resumeOrder.getFunc();
            if (funcIdString.length() > 0) {
                String[] itemFuncStrings = funcIdString.split(",");// 所有行业信息
                ArrayList<String> curIndustryFuncArrayList = new ArrayList<String>();// 当前行业职能
                if (itemFuncStrings != null && itemFuncStrings.length > 0) {
                    for (int i = 0; i < itemFuncStrings.length; i++) {// 过滤出当前行业下的职能
                        if (itemFuncStrings[i].contains(MyUtils.industryId
                                + ":")) {
                            curIndustryFuncArrayList.add(itemFuncStrings[i]
                                    .replace(MyUtils.industryId + ":", ""));
                        }
                    }
                    for (int i=0;i<curIndustryFuncArrayList.size();i++) {
                        if (curIndustryFuncArrayList.get(i).contains("|")) {// 例如：264101|10100
                            String[] funcAndZhixiStrings = curIndustryFuncArrayList.get(i).split("\\|");
                            String func0 = ResumeInfoIDToString.getFunc(this,
                                    MyUtils.industryId,
                                    funcAndZhixiStrings[0].trim());
                            String zhixi0 = ResumeInfoIDToString
                                    .getZhixiString(this,
                                            funcAndZhixiStrings[1].trim());
                            showBuffer.append("," + func0 + zhixi0);
                            FunctionBean functionBean=new FunctionBean();
                            functionBean.setId(funcAndZhixiStrings[0].trim() + "|" + funcAndZhixiStrings[1].trim());
                            functionBean.setName(func0 + zhixi0);
                            functionBean.setSelect(true);
                            funcSelectedMap.add(functionBean);
                        } else {// 例如： 256101
                            String func1 = ResumeInfoIDToString.getFunc(this, MyUtils.industryId, curIndustryFuncArrayList.get(i).trim());
                            showBuffer.append("," + func1);
                            FunctionBean functionBean=new FunctionBean();
                            functionBean.setName(func1);
                            functionBean.setId( curIndustryFuncArrayList.get(i));
                            functionBean.setSelect(true);
                            funcSelectedMap.add(functionBean);
                        }
                    }
                    if (showBuffer.toString().length() > 0) {// 显示数据
                        tv_resume_joborder_position
                                .setText(showBuffer.substring(1));
                    }
                }
            }
            // 工作地区
            initWorkPlace(resumeOrder.getWorkarea());
            et_resume_personinfo_salay.setText(resumeOrder.getOrder_salary());
            if (resumeOrder.getOrder_salary_noshow().equals("1")) {
                iv_resume_joborder_salary.setImageResource(R.mipmap.lv);
            } else {
                iv_resume_joborder_salary.setImageResource(R.mipmap.hui);
            }
        }
    }

    /**
     * 添加选择项
     */
    public void setFunctionSelected(List<FunctionBean> selectMap) {
        // 12-20 16:58:22.572: I/System.out(29412): {257101= 无机合成研发,
        // 264101|10300= 大内科护师, 264101|10400= 大内科技师}
        funcSelectedMap.clear();
        funcSelectedMap.addAll(selectMap);
        // 过滤原数据
        String[] itemFuncStrings = funcIdString.split(",");// 所有行业信息
        StringBuffer buffer0 = new StringBuffer();// 除当前行业以外的职能
        if (itemFuncStrings != null && itemFuncStrings.length > 0) {
            for (int i = 0; i < itemFuncStrings.length; i++) {// 过滤出非当前行业的职能
                if (!itemFuncStrings[i].contains(MyUtils.industryId + ":")) {
                    buffer0.append("," + itemFuncStrings[i]);
                }
            }
        }
        // 拼装新数据
        StringBuffer buffer1 = new StringBuffer();
        for (int i=0;i<funcSelectedMap.size();i++) {
            buffer1.append("," + MyUtils.industryId + ":" + funcSelectedMap.get(i).getId());
        }
        // 结合
        while (buffer0.toString().startsWith(",")) {// 去除开头“，”
            buffer0.deleteCharAt(0);
        }
        while (buffer1.toString().startsWith(",")) {// 去除开头“，”
            buffer1.deleteCharAt(0);
        }
        if (buffer0.length() > 0) {
            funcIdString = buffer0.toString() + "," + buffer1.toString();
        } else {
            funcIdString = buffer1.toString();
        }
        // System.out.println("结果：" + funcIdString);

        showText(funcSelectedMap);
    }

    /**
     * 显示文本信息
     */
    private void showText(List<FunctionBean> selectMap) {
        if (selectMap.size() == 0) {
            tv_resume_joborder_position.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<funcSelectedMap.size();i++) {
            buffer.append(selectMap.get(i).getName().trim() + ",");
        }
        tv_resume_joborder_position.setText(buffer.toString().subSequence(0, buffer.length() - 1).toString().trim());
    }

    public void setPlaceText(String value) {
        // TODO Auto-generated method stub
        tv_resume_personinfo_place.setText(value);
    }

    public void setPlaceId(String string) {
        // TODO Auto-generated method stub
        placeId = string;
    }

    public void setPlaceId(HashMap<String, Boolean> checkStateHashMap) {
        Set<String> set = checkStateHashMap.keySet();
        StringBuffer pBuffer = new StringBuffer();
        for (String string : set) {
            pBuffer.append("," + string);
        }
        if (pBuffer.length() > 0) {
            pBuffer.deleteCharAt(0);
        }
        initWorkPlace(pBuffer.toString());
    }

    /**
     * 初始化领域
     */
    private void initLingyu(String lingyuId) {
//        Toast.makeText(mContext, "lingyuId:" + lingyuId.toString(), Toast.LENGTH_SHORT).show();
        // lingyu=11: 111100, 11: 111200,11: 111300,14: 141100, 14: 141200
        if (lingyuId.length() == 0) {
            tv_resume_joborder_territory.setText("");
            return;
        }
        StringBuffer showStringBuffer = new StringBuffer();// 要显示的文字
        // 过滤出当前行业下领域
        ArrayList<String> curIndustryLingyu = new ArrayList<String>();
        String[] itemLingyu = lingyuId.split(",");
        for (String string : itemLingyu) {
            if (string.contains(MyUtils.industryId + ":")) {
                curIndustryLingyu.add(string);// 11:111100
            }
        }
        if ("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId)
                || "14".equals(MyUtils.industryId)
                || "29".equals(MyUtils.industryId)|| "22".equals(MyUtils.industryId)) {
            findViewById(R.id.rl_myresumejob_territory).setVisibility(View.VISIBLE);
            for (String string : curIndustryLingyu) {
                String id = string.replace(MyUtils.industryId + ":", "");
                showStringBuffer.append("," + ResumeInfoIDToString.getLingYuString(this, MyUtils.industryId, id));
            }
        } else {
            findViewById(R.id.rl_myresumejob_territory).setVisibility(View.GONE);
        }

        if (showStringBuffer.toString().startsWith(",")) {// 去除首个“，”
            showStringBuffer.deleteCharAt(0);
        }
        if (showStringBuffer.toString().endsWith(",")) {// 去除末尾“，”
            showStringBuffer.deleteCharAt(showStringBuffer.length() - 1);
        }
        tv_resume_joborder_territory.setText(showStringBuffer.toString());
        // System.out.println("领域：" + showStringBuffer.toString());
    }

    /**
     * 设置领域
     */
    public void setLingyu(String lingyuIdString) {
        this.lingyuIdString = lingyuIdString;
        initLingyu(lingyuIdString);
    }

    /**
     * 保存数据
     */
    private void saveToDB() {
        MyUtils.canResumeReflesh=true;
        if (isCHS) {// zh
            if (tv_resume_joborder_position.getText().toString().length() == 0 || tv_resume_joborder_position.getText().toString().equals("请选择职位")) {
                Toast.makeText(this, "请选择期望职位", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("11".equals(MyUtils.industryId) || "12".equals(MyUtils.industryId)
                    || "14".equals(MyUtils.industryId)
                    || "29".equals(MyUtils.industryId)|| "22".equals(MyUtils.industryId)) {
                if (tv_resume_joborder_territory.getText().toString().length() == 0) {
                    if (lingyuIdString == null || !lingyuIdString.contains(MyUtils.industryId + ":")) {
                        Toast.makeText(this, "请选择期望领域", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            if (tv_resume_personinfo_place.getText().toString()
                    .length() == 0) {
                Toast.makeText(this, "请选择工作地点", Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_resume_personinfo_salay.getText().toString()
                    .length() == 0) {
                Toast.makeText(this, "请输入期望薪资", Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_resume_personinfo_salay.getText().toString()
                    .trim().substring(0, 1).equals("0")) {
                Toast.makeText(this, "请输入大于0的薪资", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {// en

            if (tv_resume_joborder_position.getText()
                    .toString().length() == 0) {
                Toast.makeText(this, "Please Input Desired Position",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (tv_resume_personinfo_place.getText().toString()
                    .length() == 0) {
                Toast.makeText(this, "Please Input Desired Location",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_resume_personinfo_salay.getText().toString()
                    .length() == 0) {
                Toast.makeText(this, "Please Input Desired Salary",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        }

        // 求职状态
        String workStateString = selectFindJobId;
        // 工作性质
        String jobTypeString = selectJobTypeId;

        // 期望职位
        String funcId = funcIdString;
        // 领域
        String lingyuString = lingyuIdString;
        // Postion--en
        String positionString = tv_resume_joborder_position
                .getText().toString();
        // 工作地区
        String placeIdString = placeId + "";
        // System.out.println("placeId:" + placeIdString);
        // 薪资
        String salaryString = et_resume_personinfo_salay.getText().toString().trim();
        if (!workStateString.equals(resumeBaseInfo.getCurrent_workstate())) {
            resumeBaseInfo.setIsUpdate(1);
        }
        resumeBaseInfo.setCurrent_workstate(workStateString);
        resumeBaseInfo.setResume_language(resumeLanguageString);
        resumeBaseInfo.setUser_id(MyUtils.userID);
        // resumeBaseInfo.setName(MyUtils.username);
        resumeOrder.setJobtype(jobTypeString);
        if (isCHS) {
            resumeOrder.setFunc(funcId);
            resumeOrder.setLingyu(lingyuString);
        } else {
            resumeOrder.setJobname(positionString);
        }
        resumeOrder.setWorkarea(placeIdString);
        resumeOrder.setOrder_salary(salaryString);
        resumeOrder.setIndustry(MyUtils.industryId);
        resumeOrder.setUser_id(MyUtils.userID);
        resumeOrder.setResume_id(resumeIdString);
        resumeOrder.setResume_language(resumeLanguageString);

        if (resumeOrder.getId() == -1) {// 新建后保存
            int insertOrderResult = dbOperator
                    .Insert_ResumeCareerObjective(resumeOrder);
            boolean updateBaseinfoResult = false;
            long insertBaseinfoResult = 0;
            if (resumeBaseInfo.getId() == -1) {
                insertBaseinfoResult = dbOperator.Insert_ResumePersonInfo(resumeBaseInfo);
            } else {
                updateBaseinfoResult = dbOperator.update_ResumePersonInfo(resumeBaseInfo);
            }
            // System.out.println("保存的基本信息：" + resumeBaseInfo);
            // System.out.println("保存的求职意向：" + resumeOrder);
            if (insertOrderResult > 0 && updateBaseinfoResult
                    || insertOrderResult > 0 && insertBaseinfoResult > 0) {
                ResumeTitle resumeTitle = dbOperator.query_ResumeTitle_info(resumeIdString, resumeLanguageString);
                // 标记此份简历已修改
                if (resumeTitle != null) {
                    resumeTitle.setIsUpdate(1);
                    boolean isReWriteTitle = dbOperator.update_ResumeList(resumeTitle);
                }
                if (resumeIdString!=null){
                    uploadData(resumeIdString);
                }
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
//                finish();
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
            }
        } else {// 修改
            boolean updateOrderResult = dbOperator
                    .update_ResumeCareerObjective(resumeOrder);
            boolean updateBaseinfoResult = dbOperator
                    .update_ResumePersonInfo(resumeBaseInfo);
            // System.out.println("保存的基本信息：" + resumeBaseInfo);
            // System.out.println("保存的求职意向：" + resumeOrder);
            if (updateOrderResult && updateBaseinfoResult) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(this, dbOperator,
                        resumeIdString, resumeLanguageString);
                uploadData(resumeIdString);
                MyUtils.canReflesh=true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                // 刷新简历完整度
//                finish();
            } else {
                Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
