package com.hr.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hr.ui.R;
import com.umeng.analytics.MobclickAgent;

public class AttentionOurActivity extends BaseActivity implements View.OnClickListener {


    private ImageView iv_attention_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_our);
        MobclickAgent.onEvent(this, "setting-focus-us");
        initView();

    }

    private void initView() {
        iv_attention_back = (ImageView) findViewById(R.id.iv_attention_back);
        iv_attention_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_attention_back:
                finish();
                break;
        }
    }
}
