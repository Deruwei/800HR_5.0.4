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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeIsAppResumePopupLVAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.server.RegisterCodeTimerService;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegister;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegisterAutoCode;
import com.hr.ui.utils.netutils.AsyncPhoneVerify;
import com.hr.ui.utils.netutils.AsyncPhoneVerifyStates;
import com.hr.ui.utils.tools.CodeUtils;
import com.hr.ui.utils.tools.RegisterCodeTimer;
import com.hr.ui.view.custom.CodeButton;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyPhoneNumActivity extends Activity {

    @Bind(R.id.iv_phoneverify_back)
    ImageView ivPhoneverifyBack;
    @Bind(R.id.et_phoneverify_username)
    EditText etPhoneverifyUsername;
    @Bind(R.id.et_phoneverify_authcode)
    EditText etPhoneverifyAuthcode;
    @Bind(R.id.bt_phoneverify_authcode)
    CodeButton btPhoneverifyAuthcode;
    @Bind(R.id.frame_phoneverify_login)
    FrameLayout framePhoneverifyLogin;
    private String phoneNum, token, pwdStr;
    private Intent mIntent;
    private int industryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_num);
        ButterKnife.bind(this);
        sharedPreferencedUtils = new SharedPreferencesUtils(this);
        industryID = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        btPhoneverifyAuthcode.onCreate(savedInstanceState);
        btPhoneverifyAuthcode.setTextAfter("s后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                ResumePersonInfoActivity.setPhone(etPhoneverifyUsername.getText().toString().trim());
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
                    if (sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 1) > 1) {
                        chooseAutoCode();
                    } else {
                        sharedPreferencedUtils.setIntValue(Constants.AUTOCODENUM, sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 0) + 1);
                        AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(VerifyPhoneNumActivity.this);
                        asyncRegister.execute(phoneNum, "4");
                        btPhoneverifyAuthcode.onStart();
                    }
                    break;
                case 2:
                    Toast.makeText(VerifyPhoneNumActivity.this, "该手机号已验证", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(VerifyPhoneNumActivity.this, "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        btPhoneverifyAuthcode.onDestroy();
        super.onDestroy();
    }

    @OnClick({R.id.iv_phoneverify_back, R.id.et_phoneverify_username, R.id.et_phoneverify_authcode, R.id.bt_phoneverify_authcode, R.id.frame_phoneverify_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_phoneverify_back:
                finish();
                break;
            case R.id.et_phoneverify_username:
                break;
            case R.id.et_phoneverify_authcode:
                break;
            case R.id.bt_phoneverify_authcode:
                phoneNum = etPhoneverifyUsername.getText().toString().trim();
                if (phoneNum.length() != 0) {
                    String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
                    Pattern p = Pattern.compile(regExp);
                    if (phoneNum != null) {
                        Matcher m = p.matcher(phoneNum.trim());
                        if (!m.find()) {
                            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            AsyncPhoneVerifyStates asyncPhoneVerifyStates = new AsyncPhoneVerifyStates(VerifyPhoneNumActivity.this, handlerPhone);
                            asyncPhoneVerifyStates.execute(phoneNum);
                        }
                    }
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.frame_phoneverify_login:
                token = etPhoneverifyAuthcode.getText().toString().trim();
                phoneNum = etPhoneverifyUsername.getText().toString().trim();
                if (phoneNum.equals("")) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else if (token.equals("")) {
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
                    AsyncPhoneVerify asyncPhoneVerify = new AsyncPhoneVerify(this, handler);
                    asyncPhoneVerify.execute(phoneNum, token);
                }
                break;
        }
    }
//
//    /**
//     * 倒计时Handler
//     */
//    @SuppressLint("HandlerLeak")
//    Handler mCodeHandler = new Handler() {
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//        public void handleMessage(Message msg) {
//            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
//                //设置按钮为灰色，这时是不能点击的
//                btPhoneverifyAuthcode.setClickable(false);//设置不能点击
//                btPhoneverifyAuthcode.setBackground(getResources().getDrawable(R.drawable.linear_yuanhu_bg_hui));
//                btPhoneverifyAuthcode.setText(msg.obj.toString());
//                Spannable span = new SpannableString(btPhoneverifyAuthcode.getText().toString());//获取按钮的文字
//                span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
//            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
//                btPhoneverifyAuthcode.setEnabled(true);
//                btPhoneverifyAuthcode.setClickable(true);//设置不能点击
//                btPhoneverifyAuthcode.setText(msg.obj.toString());
//                btPhoneverifyAuthcode.setBackground(getResources().getDrawable(R.drawable.linear_yuanhu_button));//还原背景色
//            }
//        }
//
//        ;
//    };
    private PopupWindow popwindowIsAPPResume;
    private View viewPopIsApp;
    private String getCode = null;
    private SharedPreferencesUtils sharedPreferencedUtils;

    private void chooseAutoCode() {

//        popwindowIsAPPResume.setWidth(width / 6 * 5);
//        popwindowIsAPPResume.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popwindowIsAPPResume.setFocusable(true);
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
                    Toast.makeText(VerifyPhoneNumActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else if (!v_code.equalsIgnoreCase(getCode)) {
                    Toast.makeText(VerifyPhoneNumActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(VerifyPhoneNumActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(VerifyPhoneNumActivity.this);
                    asyncRegister.execute(phoneNum, "4");
                    btPhoneverifyAuthcode.onStart();
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
