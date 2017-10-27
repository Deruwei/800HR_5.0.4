package com.hr.ui.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 *
 * 关于我们
 */
public class AboutOurActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_aboutour_back;
    private TextView tv_aboutour_version;

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
        setContentView(R.layout.activity_about_our);
        MobclickAgent.onEvent(this, "setting-about-us");
        initView();
    }
    private void initView() {
        iv_aboutour_back = (ImageView) findViewById(R.id.iv_aboutour_back);
        tv_aboutour_version = (TextView) findViewById(R.id.tv_aboutour_version);
        try {
            tv_aboutour_version.setText("软件版本                          " + this.getPackageManager().getPackageInfo("com.hr.ui", 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        iv_aboutour_back.setOnClickListener(this);
        //2 2p2p 4 2增长
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_aboutour_back:
                finish();
                break;
        }

    }
}
