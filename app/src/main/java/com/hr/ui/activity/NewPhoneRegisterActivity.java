package com.hr.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.server.RegisterCodeTimerService;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.NoDoubleClickListener;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegister;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegisterAutoCode;
import com.hr.ui.utils.netutils.AsyncPhoneVerifyStates;
import com.hr.ui.utils.tools.CodeUtils;
import com.hr.ui.utils.tools.RegisterCodeTimer;
import com.hr.ui.utils.tools.TimeCountUtil;
import com.hr.ui.view.custom.CodeButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPhoneRegisterActivity extends Activity {

    @Bind(R.id.iv_phoneregister_back)
    ImageView ivPhoneregisterBack;
    @Bind(R.id.et_phoneregister_username)
    EditText etPhoneregisterUsername;
    @Bind(R.id.et_phoneregister_authcode)
    EditText etPhoneregisterAuthcode;
    @Bind(R.id.bt_phoneregister_authcode)
    CodeButton btPhoneregisterAuthcode;
    @Bind(R.id.et_phoneregister_password)
    EditText etPhoneregisterPassword;
    @Bind(R.id.iv_regist_check)
    ImageView ivRegistCheck;
    @Bind(R.id.tv_regist_agreement)
    TextView tvRegistAgreement;
    @Bind(R.id.frame_phoneregister_login)
    FrameLayout framePhoneregisterLogin;
    private String phoneNum, token, pwdStr;
    private Intent mIntent;
    private int industryID;
    private boolean isCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phone_register);
        ButterKnife.bind(this);
        initView();
        sharedPreferencedUtils = new SharedPreferencesUtils(this);
        industryID = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        btPhoneregisterAuthcode.onCreate(savedInstanceState);
        btPhoneregisterAuthcode.setTextAfter("s后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
    }

    private void initView() {
        framePhoneregisterLogin.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                token = etPhoneregisterAuthcode.getText().toString().trim();
                pwdStr = etPhoneregisterPassword.getText().toString().trim();
                phoneNum = etPhoneregisterUsername.getText().toString().trim();
                Pattern pattern = Pattern.compile("^[_.@a-zA-Z0-9]+$");
                Matcher matcher1 = pattern.matcher(pwdStr);
                String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
                Pattern p = Pattern.compile(regExp);
                Matcher m = p.matcher(phoneNum.trim());
                if (phoneNum.equals("") || !m.find()) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else if (token.equals("")) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else if (pwdStr.equals("")) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (pwdStr.trim().length() > 25 || pwdStr.trim().length() < 6) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入6-25位密码", Toast.LENGTH_SHORT).show();
                } else if (!matcher1.find()) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "密码只能输入数字和字母", Toast.LENGTH_SHORT).show();
                } else if (!isCheck) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "您未同意注册协议", Toast.LENGTH_LONG).show();
                } else {
                    AsyncNewPhoneRegister asyncNewRegister = new AsyncNewPhoneRegister(NewPhoneRegisterActivity.this, handler);
                    asyncNewRegister.execute(phoneNum, pwdStr, industryID + "", token);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        btPhoneregisterAuthcode.onDestroy();
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
        }

    };
    private Handler handlerPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
//                    Toast.makeText(NewPhoneRegisterActivity.this, "运行2", Toast.LENGTH_SHORT).show();

                    if (sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 1) > 1) {
                        chooseAutoCode();
                    } else {
//                        Toast.makeText(NewPhoneRegisterActivity.this, "运行3", Toast.LENGTH_SHORT).show();
                        sharedPreferencedUtils.setIntValue(Constants.AUTOCODENUM, sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 0) + 1);
                        AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(NewPhoneRegisterActivity.this);
                        asyncRegister.execute(phoneNum, "1");
                        btPhoneregisterAuthcode.onStart();
                    }
                    break;
                case 2:
                    Toast.makeText(NewPhoneRegisterActivity.this, "手机号已经被注册", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(NewPhoneRegisterActivity.this, "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @OnClick({R.id.iv_phoneregister_back, R.id.bt_phoneregister_authcode, R.id.iv_regist_check, R.id.tv_regist_agreement, R.id.frame_phoneregister_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phoneregister_back:
                finish();
                break;
            case R.id.bt_phoneregister_authcode:
                phoneNum = etPhoneregisterUsername.getText().toString().trim();
                if (phoneNum.length() != 0) {
                    String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
                    Pattern p = Pattern.compile(regExp);
                    if (phoneNum != null) {
                        Matcher m = p.matcher(phoneNum.trim());
                        if (!m.find()) {
                            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
//                            Toast.makeText(this, "运行1", Toast.LENGTH_SHORT).show();
                            AsyncPhoneVerifyStates asyncPhoneVerifyStates = new AsyncPhoneVerifyStates(NewPhoneRegisterActivity.this, handlerPhone);
                            asyncPhoneVerifyStates.execute(phoneNum);
                        }
                    }
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.iv_regist_check:
                if (isCheck) {
                    ivRegistCheck.setImageResource(R.mipmap.hui);
                    isCheck = false;
                } else {
                    ivRegistCheck.setImageResource(R.mipmap.lv);
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

    private PopupWindow popwindowIsAPPResume;
    private View viewPopIsApp;
    private String getCode = null;
    private SharedPreferencesUtils sharedPreferencedUtils;

    private void chooseAutoCode() {

//        popwindowIsAPPResume.setWidth(width / 6 * 5);
//        popwindowIsAPPResume.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

//        popwindowIsAPPResume.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_popup_isresumeapp));
//        popwindowIsAPPResume.setAnimationStyle(R.style.popwindow);
//        popwindowIsAPPResume.showAtLocation(viewPopIsApp, Gravity.CENTER, 0, 0);
        viewPopIsApp = LayoutInflater.from(this).inflate(R.layout.item_autocode, null);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        popwindowIsAPPResume = new PopupWindow(this);
        popwindowIsAPPResume.setContentView(viewPopIsApp);
        popwindowIsAPPResume.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindowIsAPPResume.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popwindowIsAPPResume.setTouchable(true);
        popwindowIsAPPResume.setFocusable(true);
        popwindowIsAPPResume.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popwindowIsAPPResume.setOutsideTouchable(true);
        //设置弹出窗体需要软键盘，
        popwindowIsAPPResume.showAtLocation(viewPopIsApp, Gravity.CENTER, 0, 0);
        popwindowIsAPPResume.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 设置弹窗外可点击，默认为false
        popwindowIsAPPResume.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        RelativeLayout rl__item_autocode_confirm = (RelativeLayout) viewPopIsApp.findViewById(R.id.rl__item_autocode_confirm);
        TextView vc_refresh = (TextView) viewPopIsApp.findViewById(R.id.vc_refresh);
        final EditText vc_code = (EditText) viewPopIsApp.findViewById(R.id.vc_code);
        final ImageView vc_image = (ImageView) viewPopIsApp.findViewById(R.id.vc_image);
        vc_image.setImageBitmap(CodeUtils.getInstance().getBitmap());
        getCode = CodeUtils.getInstance().getCode(); // 获取显示的验证码
        rl__item_autocode_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String v_code = vc_code.getText().toString().trim();
                if (v_code == null || v_code.equals("")) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else if (!v_code.equalsIgnoreCase(getCode)) {
                    Toast.makeText(NewPhoneRegisterActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(NewPhoneRegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(NewPhoneRegisterActivity.this);
                    asyncRegister.execute(phoneNum, "1");
                    btPhoneregisterAuthcode.onStart();
                    popwindowIsAPPResume.dismiss();
                }
            }
        });
        vc_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vc_image.setImageBitmap(CodeUtils.getInstance().getBitmap());
                getCode = CodeUtils.getInstance().getCode();
            }
        });
    }
}
