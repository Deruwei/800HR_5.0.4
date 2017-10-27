package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.InvisibleKeyboard;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 简历外发
 */
public class ResumeSendActivity extends Activity implements OnClickListener {
    private EditText activity_resumesend_email, activity_resumesend_content;
    private String resumeIdString, resumeLanguageString;
    private ImageView iv_selectcity_back;

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
        setContentView(R.layout.activity_resumesend);
        findViewById(R.id.resumesend_sendbtn).setOnClickListener(this);
        activity_resumesend_email = (EditText) findViewById(R.id.activity_resumesend_email);
        activity_resumesend_content = (EditText) findViewById(R.id.activity_resumesend_content);
        iv_selectcity_back = (ImageView) findViewById(R.id.iv_selectcity_back);
        iv_selectcity_back.setOnClickListener(this);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
    }

    @Override
    public void onClick(View v) {
        InvisibleKeyboard.hideSoftKeyboard(ResumeSendActivity.this);
        switch (v.getId()) {
            case R.id.iv_selectcity_back://返回
                finish();
                break;
            case R.id.resumesend_sendbtn:// 发送
                String emailString = activity_resumesend_email.getText().toString();
                String contentString = activity_resumesend_content.getText()
                        .toString();

                if (emailString.length() == 0) {
                    Toast.makeText(this, "请输入接收邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!emailString.trim().contains("@")
                        || !emailString.trim().contains(".com")) {
                    Toast.makeText(this, "请输入正确的接收邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contentString.length() == 0) {
                    Toast.makeText(this, "请输入邮件内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                AsyncResumeSend asyncResumeSend = new AsyncResumeSend(ResumeSendActivity.this);
                asyncResumeSend.execute(resumeIdString, resumeLanguageString,
                        emailString, contentString);
                break;
            default:
                break;
        }

    }

    /**
     * 简历外发任务
     *
     * @author 800hr:xuebaohua
     */
    class AsyncResumeSend {
        private Context context;
        private Handler handlerService = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    try {
                        String jsonString = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int error_code = jsonObject.getInt("error_code");
                        if (error_code != 0) {
                            Toast.makeText(
                                    context,
                                    Rc4Md5Utils
                                            .getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(context,
                                context.getString(R.string.send_success),
                                Toast.LENGTH_SHORT).show();
                        MobclickAgent.onEvent(ResumeSendActivity.this, "cv-send-outside");
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        };

        public AsyncResumeSend(Context context) {
            this.context = context;
        }

        protected void execute(String resumeIdString, String resumeLanguageString,
                               String emailString, String contentString) {
            try {
                HashMap<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("method", "user_resume.sendmail");
                requestParams.put("resume_id", resumeIdString);
                requestParams.put("resume_language", resumeLanguageString);
                requestParams.put("tomail", emailString);
                requestParams.put("mailtxt", contentString);

                NetService service = new NetService(context, handlerService);
                service.execute(requestParams);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}


