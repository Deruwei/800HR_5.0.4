package com.hr.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.netutils.Async_user_feedback;
import com.umeng.analytics.MobclickAgent;

public class UserFeedBackActivity extends BaseActivity implements View.OnClickListener {

    EditText et_setting_userback_email, et_setting_userback;
    ImageView iv_userfeedback_back;
    RelativeLayout rl_userback_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_back);
        MobclickAgent.onEvent(this, "setting-feedback");
        et_setting_userback_email = (EditText) findViewById(R.id.et_setting_userback_email);
        et_setting_userback = (EditText) findViewById(R.id.et_setting_userback);
        iv_userfeedback_back = (ImageView) findViewById(R.id.iv_userfeedback_back);
        rl_userback_save = (RelativeLayout) findViewById(R.id.rl_userback_save);
        iv_userfeedback_back.setOnClickListener(this);
        rl_userback_save.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 0) {
                Toast.makeText(UserFeedBackActivity.this, R.string.set_feed_words_toast, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        ;
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_userfeedback_back:
                finish();
                break;
            case R.id.rl_userback_save:
                if (et_setting_userback.getText().toString().trim().length() == 0
                        || et_setting_userback_email.getText().toString().trim().length() == 0
                        || !et_setting_userback_email.getText().toString().trim().contains("@")
                        || !et_setting_userback_email.getText().toString().trim().contains("com")) {
                    if (et_setting_userback.getText().toString().trim().length() == 0) {
                        Toast.makeText(UserFeedBackActivity.this,
                                R.string.edit_feed_words_toast, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_setting_userback_email.getText().toString().trim().length() == 0) {
                        Toast.makeText(UserFeedBackActivity.this, "请输入您的邮箱",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!et_setting_userback_email.getText().toString().trim().contains("@")
                            || !et_setting_userback_email.getText().toString().trim()
                            .contains("com")) {
                        Toast.makeText(UserFeedBackActivity.this, "请正确输入您的邮箱",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    new Async_user_feedback(UserFeedBackActivity.this, handler).execute(
                            "user.feedback",
                            et_setting_userback_email.getText().toString().trim(), et_setting_userback
                                    .getText().toString().trim());
                }

                break;
            default:
                break;
        }
    }
}
