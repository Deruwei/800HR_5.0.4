package com.hr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.model.Invitedinfo;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.NetService;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 人事经理来信详细信息页
 */
public class CompanyEmailActivity extends Activity implements View.OnClickListener {
    private TextView activity_myletter2_theme, From_company, release_time,
            Interview_Notice;
    private Invitedinfo invitedData;
    private ImageView iv_companyemail_back;

    // 推送
    private boolean isFromPush = false;
    private String idString, uid;// 来信id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        setContentView(R.layout.activity_company_email);
        getDate();
        init();
    }

    private void init() {
        activity_myletter2_theme = (TextView) findViewById(R.id.activity_myletter2_theme);
        From_company = (TextView) findViewById(R.id.From_company);
        release_time = (TextView) findViewById(R.id.release_time);
        Interview_Notice = (TextView) findViewById(R.id.Interview_Notice);

        iv_companyemail_back = (ImageView) findViewById(R.id.iv_companyemail_back);
        iv_companyemail_back.setOnClickListener(this);
        if (isFromPush) {
            if (MyUtils.isLogin) {
                System.err.println("id:" + idString + "  uid:" + uid);
                // 根据id获取详细信息
                if (idString != null && idString.length() > 0 && uid != null
                        && uid.length() > 0) {
                    HashMap<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("method", "user_stow.userinvited");
                    requestParams.put("id", idString);
                    requestParams.put("user_id", uid);
                    NetService service = new NetService(this, handlerService);
                    service.execute(requestParams);
                }
            } else {
                // 先登录
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                Intent intentLogin = new Intent(CompanyEmailActivity.this, NewLoginActivity.class);
                startActivity(intentLogin);
            }
        } else {
            refreshUI(invitedData);
        }
    }

    private Handler handlerService = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String error_codeString = jsonObject
                            .getString("error_code");
                    int error_code = Integer.parseInt(error_codeString);
                    switch (error_code) {
                        case 0:
                            JSONArray invited_list = jsonObject
                                    .getJSONArray("invited_list");
                            if (invited_list != null) {
                                for (int i = 0; i < invited_list.length(); i++) {
                                    invitedData = new Invitedinfo();
                                    invitedData.setEnterprise_name(invited_list
                                            .getJSONObject(i).getString(
                                                    "enterprise_name"));
                                    invitedData.setEnterprise_id(invited_list
                                            .getJSONObject(i).getString(
                                                    "enterprise_id"));
                                    invitedData.setJob_name(invited_list.getJSONObject(i).getString(
                                            "job_name"));
                                    invitedData.setJob_id(invited_list.getJSONObject(i).getString("job_id"));
                                    invitedData.setIs_new(invited_list.getJSONObject(i).getString("is_new"));
                                    invitedData.setIs_email(invited_list.getJSONObject(i).getString("is_email"));
                                    invitedData.setEmail_content(invited_list.getJSONObject(i).getString("email_content"));
                                    invitedData.setInvited_time(invited_list.getJSONObject(i).getString("invited_time"));
                                    invitedData.setSms_content(invited_list.getJSONObject(i).getString("sms_content"));
                                    invitedData.setInvited_title(invited_list.getJSONObject(i).getString("invited_title"));

                                }
                                refreshUI(invitedData);
                            }
                            break;
                        case -1:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };

    /**
     */
    private void refreshUI(Invitedinfo invitedInfo) {
        if (invitedInfo != null) {
            if (invitedData.getInvited_title().trim().equals("")) {
                activity_myletter2_theme.setText(("（无主题）"));
            } else {
                activity_myletter2_theme.setText(invitedInfo.getInvited_title());
            }
            From_company.setText(invitedInfo.getEnterprise_name());
            release_time.setText(invitedInfo.getInvited_time());
            if (invitedInfo.getEmail_content() == null || invitedInfo.getEmail_content().length() == 0) {
                Interview_Notice.setText(invitedInfo.getSms_content());
            } else {
                Interview_Notice.setText(invitedInfo.getEmail_content());
            }
        }
    }

    private void getDate() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        if (intent != null) {
//            isFromPush = intent.getBooleanExtra("isFromPush", false);
//            if (isFromPush) {
//                idString = intent.getStringExtra("id");
//                uid = intent.getStringExtra("uid");
//            } else {
            Bundle bundle = getIntent().getExtras();
            invitedData = bundle.getParcelable("invitedInfo");
//            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_companyemail_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            init();
        }
    }
}
