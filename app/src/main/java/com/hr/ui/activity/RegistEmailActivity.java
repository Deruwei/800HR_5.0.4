package com.hr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncRegister;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistEmailActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_regist_email_save;
    private EditText et_regist_email, et_regist_email_username, et_login_password1, et_login_password2;
    private ImageView iv_login_email_back, iv_regist_check;
    private TextView tv_regist_agreement;
    private String emailString, usernameString, password0String, password1String;
    private int industryID;
    private boolean isCheck = true;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                MyUtils.emailAddress = et_regist_email.getText().toString();
                finish();
            }
        }
        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_email);
        initView();
        initData();
    }

    private void initData() {
        SharedPreferencesUtils sharedPreferencedUtils = new SharedPreferencesUtils(this);
        int industry = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        industryID = industry;
    }

    private void initView() {
        rl_regist_email_save = (RelativeLayout) findViewById(R.id.rl_regist_email_save);
        et_regist_email = (EditText) findViewById(R.id.et_regist_email);
        et_regist_email_username = (EditText) findViewById(R.id.et_regist_email_username);
        et_login_password1 = (EditText) findViewById(R.id.et_login_password1);
        et_login_password2 = (EditText) findViewById(R.id.et_login_password2);
        iv_login_email_back = (ImageView) findViewById(R.id.iv_login_email_back);
        iv_regist_check = (ImageView) findViewById(R.id.iv_regist_check);
        tv_regist_agreement = (TextView) findViewById(R.id.tv_regist_agreement);

        iv_login_email_back.setOnClickListener(this);
        rl_regist_email_save.setOnClickListener(this);
        iv_regist_check.setOnClickListener(this);
        tv_regist_agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_email_back:
                finish();
                break;
            case R.id.rl_regist_email_save:
                emailString = et_regist_email.getText().toString().trim();
                usernameString = et_regist_email_username.getText().toString().trim();
                password0String = et_login_password1.getText().toString().trim();
                password1String = et_login_password2.getText().toString().trim();
                if (!checkData()) {
                    return;
                }
                AsyncRegister asyncRegister = new AsyncRegister(RegistEmailActivity.this, handler);
                asyncRegister.execute(emailString, usernameString, password1String, industryID + "");
                break;
            case R.id.iv_regist_check:
                if (isCheck) {
                    iv_regist_check.setImageResource(R.mipmap.hui);
                    isCheck = false;
                } else {
                    iv_regist_check.setImageResource(R.mipmap.lv);
                    isCheck = true;
                }
                break;

            case R.id.tv_regist_agreement:
                Intent intentUserAgreement = new Intent(this, UserAgreementActivity.class);
                intentUserAgreement.putExtra("userOrCom", "1");
                startActivity(intentUserAgreement);
                break;
        }
    }

    /**
     * 数据验证
     */
    private boolean checkData() {
        // ---验证 邮箱
        String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(emailString);
        boolean isMatched = matcher.matches();
        if (emailString.length() == 0) {
            Toast.makeText(this, getString(R.string.email_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMatched) {
            Toast.makeText(this, getString(R.string.email_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        // ---验证用户名
        if (usernameString.length() == 0) {
            Toast.makeText(this, getString(R.string.username_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (usernameString.length() < 4 || usernameString.length() > 25) {
            Toast.makeText(this, getString(R.string.username_length_error), Toast.LENGTH_LONG).show();
            return false;
        }

        Pattern pattern = Pattern.compile("^[_.@a-zA-Z0-9]+$");
        Matcher matcher1 = pattern.matcher(usernameString);
        if (!matcher1.find()) {
            Toast.makeText(this, getString(R.string.username_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        // ---验证密码
        if (password0String.length() == 0) {
            Toast.makeText(this, getString(R.string.password_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password0String.length() < 6) {
            Toast.makeText(this, getString(R.string.password_length_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password0String.equals(password1String)) {
            Toast.makeText(this,
                    getString(R.string.password_pswconfirm_not_equal),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password0String.equals(usernameString)) {
            Toast.makeText(this, getString(R.string.password_username_equal),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        Pattern pattern2 = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher2 = pattern2.matcher(password1String);
        if (!matcher2.find()) {
            Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isCheck) {
            Toast.makeText(this, "您未同意注册协议", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
