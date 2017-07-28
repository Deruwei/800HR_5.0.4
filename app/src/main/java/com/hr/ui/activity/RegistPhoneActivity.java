package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.hr.ui.R;

public class RegistPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_regist_goemail;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
