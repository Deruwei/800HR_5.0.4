package com.hr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.netutils.AsyncCompanyIntroduce;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * 公司详情页
 */
public class CompanyParticularActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CompanyParticularActivity";

    private ImageView iv_companyparticular_back;
    private TextView tv_companyparticular_jobnum;
    private ScrollView sl_companyparticular2;
    private ImageView iv_companyparticular_comlogo2;
    private TextView tv_companyparticular_comname2;
    private TextView tv_companyparticular_nature;
    private TextView tv_companyparticular_scale;
    private TextView tv_companyparticular_place;
    private TextView tv_companyparticular_comparticular;
    private RelativeLayout rl_companyparticular_clickother;
    /**
     * logo地址
     */
    private String logoUrl;
    /**
     * 公司ID
     */
    private String comId;
    /**
     * UIL配置信息
     */
    /*private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();*/
    private RefleshDialogUtils dialogUtils;
    /**
     * 返回的公司详情
     */
    private HashMap<String, String> resultComMap;
    Handler handlerForCom = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null) {
                resultComMap = (HashMap<String, String>) msg.obj;
                tv_companyparticular_comname2.setText(resultComMap.get("enterprise_name"));
                tv_companyparticular_nature.setText(resultComMap.get("company_type"));
                tv_companyparticular_place.setText(resultComMap.get("address"));
                tv_companyparticular_scale.setText(resultComMap.get("stuff_munber"));
                tv_companyparticular_comparticular.setText(resultComMap.get("synopsis"));
                logoUrl = resultComMap.get("ent_logo").toString();
                if (!logoUrl.equals("")) {
                    Glide.with(CompanyParticularActivity.this).load(Constants.LOGO_ROOTPATH + logoUrl).into(iv_companyparticular_comlogo2);
                    /*imageLoader.displayImage(Constants.LOGO_ROOTPATH + logoUrl, iv_companyparticular_comlogo2, options);*/
                }
            }
            dialogUtils.dismissDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_particular);
        MobclickAgent.onEvent(this, "job-show-company-info");
        dialogUtils=new RefleshDialogUtils(this);
        initView();
        initData();
    }
    private void initView() {
        iv_companyparticular_back = (ImageView) findViewById(R.id.iv_companyparticular_back);
        tv_companyparticular_jobnum = (TextView) findViewById(R.id.tv_companyparticular_jobnum);
        sl_companyparticular2 = (ScrollView) findViewById(R.id.sl_companyparticular2);
        iv_companyparticular_comlogo2 = (ImageView) findViewById(R.id.iv_companyparticular_comlogo2);
        tv_companyparticular_comname2 = (TextView) findViewById(R.id.tv_companyparticular_comname2);
        tv_companyparticular_nature = (TextView) findViewById(R.id.tv_companyparticular_nature);
        tv_companyparticular_scale = (TextView) findViewById(R.id.tv_companyparticular_scale);
        tv_companyparticular_place = (TextView) findViewById(R.id.tv_companyparticular_place);
        tv_companyparticular_comparticular = (TextView) findViewById(R.id.tv_companyparticular_comparticular);
        rl_companyparticular_clickother = (RelativeLayout) findViewById(R.id.rl_companyparticular_clickother);
        rl_companyparticular_clickother.setOnClickListener(this);
        iv_companyparticular_back.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        comId = intent.getStringExtra("Enterprise_id");
        loadComNetData();
    }

    /**
     * 加载公司详情数据
     */
    private void loadComNetData() {
        dialogUtils.showDialog();
        new AsyncCompanyIntroduce(this, handlerForCom).execute("job.enterprise", comId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_companyparticular_clickother:
                Intent intent = new Intent(CompanyParticularActivity.this, CompanyOtherJobActivity.class);
                intent.putExtra("enterprise_id", comId);
                startActivity(intent);
                break;
            case R.id.iv_companyparticular_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtils.dismissDialog();
    }
}
