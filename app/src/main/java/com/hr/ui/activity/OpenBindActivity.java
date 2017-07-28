package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.AndroidUtils;
import com.hr.ui.utils.tools.LogTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 绑定账号页面
 */
public class OpenBindActivity extends BaseActivity implements OnClickListener {

    private EditText activity_openbind_truename, et_openbind_pwd,
            activity_openbind_email, et_login_username;
    private ImageView iv_login_bind_back;
    private TextView activity_openbind_showinfoTextView;
    private String uid;
    private String opf;
    private String name;
    private String birthday;
    private String nickname;
    private String tinyurl;
    private String gender;
    private int industry;
    private boolean isFromPush = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openbind);
        getData();
        initView();
    }

    private void initView() {
        findViewById(R.id.rl_openbind_login_btn).setOnClickListener(this);
        findViewById(R.id.rl_openbind_login_btn2).setOnClickListener(this);
        activity_openbind_truename = (EditText) findViewById(R.id.activity_openbind_truename);
        activity_openbind_email = (EditText) findViewById(R.id.activity_openbind_email);
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_openbind_pwd = (EditText) findViewById(R.id.et_openbind_pwd);
        activity_openbind_showinfoTextView = (TextView) findViewById(R.id.activity_openbind_showinfo);

        iv_login_bind_back = (ImageView) findViewById(R.id.iv_login_bind_back);
        iv_login_bind_back.setOnClickListener(this);
        activity_openbind_showinfoTextView.setText("已有" + getResources().getString(AndroidUtils.getIndustryResourceId(industry)) + "英才网账号");
    }

    /**
     * 获取数据
     */
    private void getData() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        opf = intent.getStringExtra("platform");
        name = intent.getStringExtra("name");
        birthday = intent.getStringExtra("birthday");
        nickname = intent.getStringExtra("nickname");
        tinyurl = intent.getStringExtra("tinyurl");
        gender = intent.getStringExtra("gender");
        isFromPush = intent.getBooleanExtra("isFromPush", false);
        SharedPreferences sp = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(OpenBindActivity.this);
        industry = sUtils.getIntValue(Constants.INDUSTRY, 11);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_bind_back:// 菜单
                finish();
                break;
            case R.id.rl_openbind_login_btn:
                // 登录
                String truenameString = activity_openbind_truename
                        .getText().toString().trim();
                String emailString = activity_openbind_email.getText()
                        .toString().trim();
                if (truenameString.length() == 0) {
                    Toast.makeText(this, "请填写真实姓名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailString == null || emailString.length() == 0) {
                    Toast.makeText(this, getString(R.string.email_null),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(emailString);
                boolean isMatched = matcher.matches();
                if (!isMatched) {
                    Toast.makeText(this, "请填写正确的常用邮箱！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (isFromPush) {
//                    FirstBind firstBind = new FirstBind(this);
//                    firstBind.execute(truenameString, emailString);
//                } else {
                FirstBind firstBind = new FirstBind(OpenBindActivity.this);
                firstBind.execute(truenameString, emailString);
//                }
                break;
            case R.id.rl_openbind_login_btn2:
                // 绑定并登录
                String un = et_login_username.getText().toString();
                String up = et_openbind_pwd.getText().toString();
                if ((un.length() < 4 | un.length() > 25) | (up.length() < 6)) {
                    Toast.makeText(OpenBindActivity.this, "请填写正确的用户名和密码！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (isFromPush) {
//                    ThirdBind thirdBind = new ThirdBind(this);
//                    thirdBind.execute(un, up);
//                } else {
                ThirdBind thirdBind = new ThirdBind(OpenBindActivity.this);
                thirdBind.execute(un, up);
//                }

                break;
            default:
                break;
        }

    }

    /**
     * 第三方账号注册
     *
     * @author 800hr:xuebaohua
     */
    private class FirstBind {
        private Context context;
        private Handler handlerService = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    try {
                        String jsonString = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int error_code = jsonObject.getInt("error_code");
                        switch (error_code) {
                            case 0:
                                resetModules(context, jsonObject);
                                break;

                            default:
                                LogTools.i("OpenBindActivity", "==================到这了4");
                                Toast.makeText(
                                        context,
                                        Rc4Md5Utils
                                                .getErrorResourceId(error_code),
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        public FirstBind(Context context) {
            this.context = context;
        }

        protected void execute(String truenameString, String emailString) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.thirdregister");
            requestParams.put("third_code", opf);
            requestParams.put("third_uid", uid);
            requestParams.put("user_name", truenameString);
            requestParams.put("emailaddress", emailString);
            JSONObject jo = new JSONObject();
            try {
                jo.put("name", name);
                jo.put("birthday", birthday);
                jo.put("nickname", nickname);
                jo.put("tinyurl", tinyurl);
                jo.put("sex", gender);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            requestParams.put("third_userinfo", jo.toString());
            requestParams.put("industry", "" + industry);
            LogTools.i("OpenBindActivity", "==================到这了3");
            LogTools.i("OpenBindActivity", "==================到这了3" + requestParams.toString());
            try {
                NetService service = new NetService(context, handlerService);
                service.execute(requestParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绑定已有账号
     *
     * @author 800hr:xuebaohua
     */
    private class ThirdBind {
        private Context context;
        private Handler handlerService = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    String json = (String) msg.obj;
                    try {
                        String jsonString = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int error_code = jsonObject.getInt("error_code");
                        switch (error_code) {
                            case 0:
                                resetModules(context, jsonObject);
                                break;
                            default:
                                LogTools.i("OpenBindActivity", "==================到这了1");
                                Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
//                                LoginActivity.loginActivity.finish();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        public ThirdBind(Context context) {
            this.context = context;
        }

        public void execute(String username, String pwd) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.thirdbinding");
            if (opf.equals("")) {
                opf = "0";
            }
            requestParams.put("third_code", opf);
            if (uid.equals("")) {
                uid = "0";
            }
            requestParams.put("third_uid", uid);
            if (username.equals("")) {
                username = "username";
            }
            requestParams.put("user_name", username);
            if (pwd.equals("")) {
                pwd = "user_pwd";
            }
            requestParams.put("user_pwd", pwd);
            JSONObject jo = new JSONObject();
            try {
                jo.put("name", name);
                jo.put("birthday", birthday);
                jo.put("nickname", nickname);
                jo.put("tinyurl", tinyurl);
                jo.put("sex", gender);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            requestParams.put("third_userinfo", jo.toString());
            requestParams.put("industry", "" + industry);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        }
    }

    /**
     * 复位其他模块信息
     *
     * @param jsonObject
     * @throws JSONException
     */
    private void resetModules(Context context, JSONObject jsonObject)
            throws JSONException {
        LogTools.i("OpenBindActivity", "==================到这了2");
        MyUtils.isLogin = true;
        MyUtils.isLogouted = false;
        MyUtils.isThiredLogin = true;
        MyUtils.username = nickname;
        MyUtils.userID = jsonObject.getString("user_id");
        MyUtils.industryId = industry + "";
        Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT)
                .show();

        // 成功登录后记录数据
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(
                context);
        sUtils.setStringValue(Constants.USERNAME, nickname);
        sUtils.setStringValue(Constants.PASSWORD, et_openbind_pwd.getText().toString());
        sUtils.setIntValue(Constants.INDUSTRY, industry);

        finish();
//        LoginActivity.loginActivity.finish();
    }
}
