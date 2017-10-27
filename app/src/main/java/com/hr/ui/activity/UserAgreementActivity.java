package com.hr.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 用户协议
 */
public class UserAgreementActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_userargument;
    private TextView tv_user_agreement, tv_userargument_title;
    private String userOrCom;//1 个人用户 2企业用户

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
        setContentView(R.layout.activity_user_agreement);
        initView();
    }

    private void initView() {
        userOrCom = getIntent().getStringExtra("userOrCom");
        iv_userargument = (ImageView) findViewById(R.id.iv_userargument);
        tv_userargument_title = (TextView) findViewById(R.id.tv_userargument_title);
        tv_user_agreement = (TextView) findViewById(R.id.tv_user_agreement);
        iv_userargument.setOnClickListener(this);
        if (userOrCom.equals("1")) {
            tv_user_agreement.setText(R.string.userargument);
            tv_userargument_title.setText("个人会员注册协议");
        } else {
            tv_user_agreement.setText(R.string.comargument);
            tv_userargument_title.setText("企业会员注册协议");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userargument:
                finish();
                break;
        }
    }
}
