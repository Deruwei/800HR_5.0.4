package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hr.ui.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class RegistPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_regist_goemail;
    private Context mContext = this;

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
        setContentView(R.layout.activity_phoneregist);
        initView();
    }

    private void initView() {
        tv_regist_goemail = (TextView) findViewById(R.id.tv_regist_goemail);
        tv_regist_goemail.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_regist, menu);
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_regist_goemail:
                Intent intent = new Intent(mContext, RegistEmailActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
