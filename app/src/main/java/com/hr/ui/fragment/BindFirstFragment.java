package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.hr.ui.activity.BaseActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.NewPhoneRegisterActivity;
import com.hr.ui.activity.VerifyPhoneNumStateActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.model.Industry;
import com.hr.ui.server.RegisterCodeTimerService;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncBindThird;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegister;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegisterAutoCode;
import com.hr.ui.utils.netutils.AsyncPhoneVerifyStates;
import com.hr.ui.utils.tools.CodeUtils;
import com.hr.ui.utils.tools.RegisterCodeTimer;
import com.hr.ui.view.custom.CodeButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.WINDOW_SERVICE;

/**
 */
@SuppressLint("ValidFragment")
public class BindFirstFragment extends BaseFragment {


    @Bind(R.id.et_firstbind_username)
    EditText etFirstbindUsername;
    @Bind(R.id.et_firstbind_authcode)
    EditText etFirstbindAuthcode;
    @Bind(R.id.bt_firstbind_authcode)
    CodeButton btFirstbindAuthcode;
    @Bind(R.id.tv_firstbind_password)
    TextView tvFirstbindPassword;
    @Bind(R.id.et_firstbind_password)
    EditText etFirstbindPassword;
    @Bind(R.id.frame_firstbind_login)
    FrameLayout frameFirstbindLogin;
    private String phoneNum, token, pwdStr,third_userinfo;
    private Intent mIntent;
    private int industryID;
    private boolean isCheck = true;
    private String third_code, third_uid, industry;

    public BindFirstFragment(String third_code, String third_uid, String industry,String third_userinfo) {
        this.third_code = third_code;
        this.third_uid = third_uid;
        this.industry = industry;
        this.third_userinfo = third_userinfo;
    }

    private Handler handlerLogin = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                btFirstbindAuthcode.setClickable(true);
                MyUtils.canResumeReflesh=true;
                getActivity().finish();
            }
        }

        ;
    };
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                AsyncBindThird asyncBindThird = new AsyncBindThird(getActivity(),phoneNum,pwdStr, handlerLogin,third_code, third_uid, industry,third_userinfo,"0");
                asyncBindThird.execute();
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
                        AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(getActivity());
                        asyncRegister.execute(phoneNum, "1");
                        btFirstbindAuthcode.onStart();
                    }
                    break;
                case 2:
                    Toast.makeText(getActivity(), "手机号已经被注册", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bind_first, container, false);
        ButterKnife.bind(this, view);
        sharedPreferencedUtils = new SharedPreferencesUtils(getActivity());
        industryID = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        btFirstbindAuthcode.onCreate(savedInstanceState);
        btFirstbindAuthcode.setTextAfter("s后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btFirstbindAuthcode.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bt_firstbind_authcode, R.id.frame_firstbind_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_firstbind_authcode:
                phoneNum = etFirstbindUsername.getText().toString().trim();
                if (phoneNum.length() != 0) {
                    String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
                    Pattern p = Pattern.compile(regExp);
                    if (phoneNum != null) {
                        Matcher m = p.matcher(phoneNum.trim());
                        if (!m.find()) {
                            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            AsyncPhoneVerifyStates asyncPhoneVerifyStates = new AsyncPhoneVerifyStates(getActivity(), handlerPhone);
                            asyncPhoneVerifyStates.execute(phoneNum);
                            btFirstbindAuthcode.setClickable(false);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.frame_firstbind_login:
                token = etFirstbindAuthcode.getText().toString().trim();
                pwdStr = etFirstbindPassword.getText().toString().trim();
                phoneNum = etFirstbindUsername.getText().toString().trim();
                Pattern pattern = Pattern.compile("^[_.@a-zA-Z0-9]+$");
                Matcher matcher1 = pattern.matcher(pwdStr);
                String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
                Pattern p = Pattern.compile(regExp);
                Matcher m = p.matcher(phoneNum.trim());
                if (phoneNum.equals("") || !m.find()) {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else if (token.equals("")) {
                    Toast.makeText(getActivity(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else if (pwdStr.equals("")) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (pwdStr.trim().length() > 25 || pwdStr.trim().length() < 6) {
                    Toast.makeText(getActivity(), "请输入6-25位密码", Toast.LENGTH_SHORT).show();
                } else if (!matcher1.find()) {
                    Toast.makeText(getActivity(), "密码只能输入数字和字母", Toast.LENGTH_SHORT).show();
                } else {
                    AsyncNewPhoneRegister asyncNewRegister = new AsyncNewPhoneRegister(getActivity(), handler);
                    asyncNewRegister.execute(phoneNum, pwdStr, industryID + "", token);
                }
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
//        popwindowIsAPPResume.setFocusable(true);
//        popwindowIsAPPResume.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_popup_isresumeapp));
//        popwindowIsAPPResume.setAnimationStyle(R.style.popwindow);
//        popwindowIsAPPResume.showAtLocation(viewPopIsApp, Gravity.CENTER, 0, 0);
        viewPopIsApp = LayoutInflater.from(getActivity()).inflate(R.layout.item_autocode, null);
        popwindowIsAPPResume = new PopupWindow(getActivity());
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
                    Toast.makeText(getActivity(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else if (!v_code.equalsIgnoreCase(getCode)) {
                    Toast.makeText(getActivity(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), "验证成功", Toast.LENGTH_SHORT).show();
                    AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(getActivity());
                    asyncRegister.execute(phoneNum, "1");
                    btFirstbindAuthcode.onStart();
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
