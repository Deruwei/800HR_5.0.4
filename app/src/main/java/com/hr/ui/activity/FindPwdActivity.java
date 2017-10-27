package com.hr.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.netutils.AsyncForgetpwd;
import com.hr.ui.utils.tools.InvisibleKeyboard;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 找回密码
 *
 * @author 800hr:xuebaohua
 */
public class FindPwdActivity extends BaseActivity implements
        OnClickListener {
    private EditText forget_password_input;
    private String username;
    private Button forget_password_surebtn;
    // 推送
    private boolean isFromPush = false;
    private ImageView iv_findpwd_back;

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
        setContentView(R.layout.activity_find_pwd);
        isFromPush = getIntent().getBooleanExtra("isFromPush", false);
        username = getIntent().getStringExtra("username");
        initView();
    }
    private void initView() {
        forget_password_input = (EditText) findViewById(R.id.forget_password_input);
        iv_findpwd_back = (ImageView) findViewById(R.id.iv_findpwd_back);
        forget_password_surebtn = (Button) findViewById(R.id.forget_password_surebtn);
        iv_findpwd_back.setOnClickListener(this);
        forget_password_surebtn.setOnClickListener(this);
    }
    /**
     * 数据验证
     * @param emailString
     * @return
     */
    private boolean checkEmail(String emailString) {
        // 邮箱验证
        //String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        String check = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
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
        return true;
    }

    @Override
    public void onClick(View v) {
        InvisibleKeyboard.hideSoftKeyboard(this);
        switch (v.getId()) {
            case R.id.iv_findpwd_back:
                finish();
                break;
            case R.id.forget_password_surebtn:
                // 确定
                if (checkEmail(forget_password_input.getText().toString())) {
                    if (isFromPush) {
                        AsyncForgetpwd asyncForgetpwd = new AsyncForgetpwd(FindPwdActivity.this,
                                isFromPush);
                        asyncForgetpwd.execute(forget_password_input.getText()
                                .toString().trim(), username);
                    } else {
                        AsyncForgetpwd asyncForgetpwd = new AsyncForgetpwd(
                                FindPwdActivity.this, isFromPush);
                        asyncForgetpwd.execute(forget_password_input.getText()
                                .toString().trim(), username);
                    }

                }
                break;

            default:
                break;
        }
    }

}
