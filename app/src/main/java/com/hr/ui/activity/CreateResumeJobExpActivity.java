package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CreateResumeJobExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.ResumeInfoToJsonString;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncResumeUpdate;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Colin 工作经验
 */
public class CreateResumeJobExpActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateResumeJobExpActivity";
    private Context mContext = CreateResumeJobExpActivity.this;
    private ExpandableListView elv_createresume_jobexp;
    private ImageView iv_createresume_jobexp_back, iv_havejobexp_check;
    private TextView tv_createresume_item_addeud, tv_createresume_info_save;
    private SharedPreferencesUtils sUtils;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeExperience[] resumeExperience, resumeExperience2;
    private ArrayList<ResumeExperience> listResumeJobExp, listResumeJobExp2;
    private CreateResumeJobExpLVAdapter adapter;
    private RelativeLayout rl_createresume_info_save;
    private RelativeLayout rl_createresume_info_save2;
    private static LinearLayout linear_resumejob_haveexpe;
    private LinearLayout linear_createresume_jobexp;
    private View footView;
    private String resumeType;
    private String resumeAppId;
    private Handler handlerUploadResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                int value = msg.what;
                resumeAppId = String.valueOf(msg.arg1);
                switch (value) {
                    case -1:
                        Toast.makeText(mContext, "简历上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:// 中文简历已上传成功
                        Toast.makeText(mContext, "新建完毕", Toast.LENGTH_SHORT).show();
                        MainActivity.instanceMain.execute();
                        MainActivity.instanceMain.isLoad = true;
                        MainActivity.instanceMain.newAppResume = false;
                        sendIsApp();
                        finish();
                        break;
                    default:
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
                            MeFragment.meFragment.execute();
                            break;
                        default:
                            Toast.makeText(mContext, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resume_job_exp);
        sUtils = new SharedPreferencesUtils(mContext);
        initData();
        initView();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeExperience = dbOperator.query_ResumeWorkExperience(resumeIdString,
                resumeLanguageString);
        listResumeJobExp = new ArrayList<>();

        ResumeTitle resumeTitle1 = dbOperator.query_ResumeTitle_info("-1", "zh");
        resumeType = resumeTitle1.getResume_type();
//        Toast.makeText(mContext, "" + resumeType, Toast.LENGTH_SHORT).show();

        for (ResumeExperience resumeJobExp : resumeExperience) {
            listResumeJobExp.add(resumeJobExp);
        }
        adapter = new CreateResumeJobExpLVAdapter(mContext, listResumeJobExp, resumeIdString, resumeLanguageString, CreateResumeJobExpActivity.this);
    }

    private void initView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_createresume_foot, null);
        elv_createresume_jobexp = (ExpandableListView) findViewById(R.id.elv_createresume_jobexp);
        iv_createresume_jobexp_back = (ImageView) findViewById(R.id.iv_createresume_jobexp_back);
        tv_createresume_item_addeud = (TextView) footView.findViewById(R.id.tv_createresume_item_addeud);
        tv_createresume_info_save = (TextView) footView.findViewById(R.id.tv_createresume_info_save);
        rl_createresume_info_save = (RelativeLayout) footView.findViewById(R.id.rl_createresume_info_save);
        rl_createresume_info_save2 = (RelativeLayout) findViewById(R.id.rl_createresume_info_save2);
        iv_havejobexp_check = (ImageView) findViewById(R.id.iv_havejobexp_check);
        linear_createresume_jobexp = (LinearLayout) findViewById(R.id.linear_createresume_jobexp);
        linear_resumejob_haveexpe = (LinearLayout) findViewById(R.id.linear_resumejob_haveexpe);

        rl_createresume_info_save2.setOnClickListener(this);
        iv_havejobexp_check.setOnClickListener(this);
        elv_createresume_jobexp.setVisibility(View.GONE);
        iv_createresume_jobexp_back.setOnClickListener(this);
        rl_createresume_info_save.setOnClickListener(this);

        /**
         * 将setAdapter延后处理 解决oppo手机listview footview 不现实问题
         */
        elv_createresume_jobexp.addFooterView(footView);
        elv_createresume_jobexp.setAdapter(adapter);

        tv_createresume_item_addeud.setText("+添加工作经验");
        tv_createresume_info_save.setText("创建完成");
        /**
         * 初始化
         */
        if (resumeType.equals("2")) {
            iv_havejobexp_check.setImageResource(R.mipmap.kaiguan_guan);
            elv_createresume_jobexp.setVisibility(View.GONE);
            linear_createresume_jobexp.setVisibility(View.VISIBLE);
        } else {
            iv_havejobexp_check.setImageResource(R.mipmap.kaiguan_kai);
            elv_createresume_jobexp.setVisibility(View.VISIBLE);
            linear_createresume_jobexp.setVisibility(View.GONE);
        }

        tv_createresume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeJobExp.add(new ResumeExperience());
                adapter.notifyDataSetChanged();
            }
        });
        /**
         *是否隐藏我有工作经验条
         */
        resumeExperience2 = dbOperator.query_ResumeWorkExperience(resumeIdString,
                resumeLanguageString);
        if (resumeExperience2.length > 0) {
            linear_resumejob_haveexpe.setVisibility(View.GONE);
        } else {
            linear_resumejob_haveexpe.setVisibility(View.VISIBLE);
        }
        /**
         * 是否上传简历
         */
        rl_createresume_info_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeExperience2 = dbOperator.query_ResumeWorkExperience(resumeIdString, resumeLanguageString);
//                listResumeJobExp2 = new ArrayList<>();
//                for (ResumeExperience resumeExp : resumeExperience2) {
//                    listResumeJobExp2.add(resumeExp);
//                }
                LogTools.i(TAG, "==========resumeExperience2" + resumeExperience2.length + "  " + resumeExperience2.toString());
                if (resumeExperience2.length > 0) {
                    resumeUpdate();
                } else {
                    Toast.makeText(mContext, "请填写完整", Toast.LENGTH_SHORT).show();
                }
            }
        });
        elv_createresume_jobexp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_createresume_jobexp.collapseGroup(i);
                    }
                }
            }
        });
    }

    /**
     *
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
    public static void goneLinear() {
        linear_resumejob_haveexpe.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_createresume_jobexp_back:
                finish();
                break;
            case R.id.iv_havejobexp_check:
                //如果有工作经验 点击变为无工作经验
                if (resumeType.equals("1")) {
                    iv_havejobexp_check.setImageResource(R.mipmap.kaiguan_guan);
                    elv_createresume_jobexp.setVisibility(View.GONE);
                    resumeType = "2";
                    linear_createresume_jobexp.setVisibility(View.VISIBLE);
                    /**
                     * 先删除后添加
                     */
                    ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info("-1", "zh");
                    titleZhUpdate.setResume_type(resumeType);
                    dbOperator.Delete_Data("ResumeList");
                    dbOperator.Insert_ResumeList(titleZhUpdate);
//                    dbOperator.update_ResumeTitle_info(titleZhUpdate);
                    // 检测本地中文基本信息
                } else {
                    //如果没有工作经验 点击变为有工作经验
                    iv_havejobexp_check.setImageResource(R.mipmap.kaiguan_kai);
                    elv_createresume_jobexp.setVisibility(View.VISIBLE);
                    resumeType = "1";
                    linear_createresume_jobexp.setVisibility(View.GONE);
                    /**
                     * 先删除后添加
                     */
                    ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info("-1", "zh");
                    titleZhUpdate.setResume_type(resumeType);
                    dbOperator.Delete_Data("ResumeList");
                    dbOperator.Insert_ResumeList(titleZhUpdate);
//                    dbOperator.update_ResumeTitle_info(titleZhUpdate);
                    // 检测本地中文基本信息
                }
                break;
            /**
             * 学生简历
             */
            case R.id.rl_createresume_info_save2:
                if (MyUtils.ableInternet) {
                    resumeUpdate();
                } else {
                    Toast.makeText(CreateResumeJobExpActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void refresh() {
        Intent intent = new Intent(mContext, CreateResumeJobExpActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }




    /**
     * 简历刷新或上传
     */
    private void resumeUpdate() {
        //记录本地信息
        ResumeTitle titleZhUpdate22 = dbOperator.query_ResumeTitle_info("-1", "zh");
        titleZhUpdate22.setResume_type(resumeType);
//        boolean updateBaseinfoResult = dbOperator.update_ResumeTitle_info(titleZhUpdate22);
        // 检测本地中文基本信息
        ResumeBaseInfo baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
//        int completeInt = 0;
//        ResumeComplete complete = new ResumeComplete(mContext, "-1", "zh");
//        if ("1".equals(resumeType)) {// 1为社会人简历，且有工作经验
//            completeInt = complete.getFullScale(true, false);
//        } else {
//            completeInt = complete.getFullScale(false, false);
//        }
        ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info(
                "-1", "zh");
//        // 检测本地中文基本信息
//        if ("1".equals(resumeType)) {// 1为社会人简历，且有工作经验
//            completeInt = complete.getFullScale(true, false);
//        } else {
//            completeInt = complete.getFullScale(false, false);
//        }
        /**
         * 中文简历完整度达到60%，才能确定是否上传英文简历
         *
         */
//        if (completeInt >= 60) {
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
//        } else {
//            Toast.makeText(mContext, "中文简历完整度不足", Toast.LENGTH_SHORT).show();
//        }
    }
}
