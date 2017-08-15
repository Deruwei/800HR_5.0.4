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
import com.hr.ui.activity.CreateResumePersonInfoActivity;
import com.hr.ui.activity.NewPhoneRegisterActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.server.RegisterCodeTimerService;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncFindPhonePWD;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegister;
import com.hr.ui.utils.netutils.AsyncNewPhoneRegisterAutoCode;
import com.hr.ui.utils.netutils.AsyncPhoneVerifyStates;
import com.hr.ui.utils.tools.CodeUtils;
import com.hr.ui.utils.tools.RegisterCodeTimer;
import com.hr.ui.view.custom.CodeButton;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindPhonePWDFragment extends BaseFragment {


    @Bind(R.id.et_findphonepwd_username)
    EditText etFindphonepwdUsername;
    @Bind(R.id.et_findphonepwd_authcode)
    EditText etFindphonepwdAuthcode;
    @Bind(R.id.bt_findphonepwd_authcode)
    CodeButton btFindphonepwdAuthcode;
    @Bind(R.id.et_findphonepwd_password)
    EditText etFindphonepwdPassword;
    @Bind(R.id.frame_findphonepwd_login)
    FrameLayout frameFindphonepwdLogin;
    private String phoneNum, token, pwdStr;
    private Intent mIntent;
    private int industryID;
    private boolean isCheck = true;

    public FindPhonePWDFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                getActivity().finish();
            }
        }

        ;
    };
    private Handler handlerPhone = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Toast.makeText(getActivity(), "手机号尚未注册", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 1) > 1) {
                        chooseAutoCode();
                    } else {
                        sharedPreferencedUtils.setIntValue(Constants.AUTOCODENUM, sharedPreferencedUtils.getIntValue(Constants.AUTOCODENUM, 0) + 1);
                        AsyncNewPhoneRegisterAutoCode asyncRegister = new AsyncNewPhoneRegisterAutoCode(getActivity());
                        asyncRegister.execute(phoneNum, "3");
//                        getActivity().startService(mIntent);
                        btFindphonepwdAuthcode.onStart();
                    }
                    break;
                default:
                    Toast.makeText(getActivity(), "验证手机号失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_phone_pwd, container, false);
        ButterKnife.bind(this, view);
        MobclickAgent.onEvent(getActivity(), "cv-reset-password");
//        RegisterCodeTimerService.setHandler(mCodeHandler);
//        mIntent = new Intent(getActivity(), RegisterCodeTimerService.class);
        sharedPreferencedUtils = new SharedPreferencesUtils(getActivity());
        industryID = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        btFindphonepwdAuthcode.onCreate(savedInstanceState);
        btFindphonepwdAuthcode.setTextAfter("s后重新获取").setTextBefore("获取验证码").setLenght(60 * 1000);
        return view;
    }

    @Override
    public void onDestroyView() {
        btFindphonepwdAuthcode.onDestroy();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.bt_findphonepwd_authcode, R.id.frame_findphonepwd_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_findphonepwd_authcode:
                phoneNum = etFindphonepwdUsername.getText().toString().trim();
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
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (phoneNum.equals("")) {
//                    Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
//                } else {
//
//                }
                break;
            case R.id.frame_findphonepwd_login:
                token = etFindphonepwdAuthcode.getText().toString().trim();
                pwdStr = etFindphonepwdPassword.getText().toString().trim();
                phoneNum = etFindphonepwdUsername.getText().toString().trim();
                Pattern pattern = Pattern.compile("^[_.@a-zA-Z0-9]+$");
                Matcher matcher1 = pattern.matcher(pwdStr);
                if (phoneNum.equals("")) {
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
                    AsyncFindPhonePWD asyncNewRegister = new AsyncFindPhonePWD(getActivity(), handler);
                    asyncNewRegister.execute(phoneNum, token, pwdStr);
                }
                break;
        }
    }

    //    /**
//     * 倒计时Handler
//     */
//    @SuppressLint("HandlerLeak")
//    Handler mCodeHandler = new Handler() {
//        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//        public void handleMessage(Message msg) {
//            if (msg.what == RegisterCodeTimer.IN_RUNNING) {// 正在倒计时
//                //设置按钮为灰色，这时是不能点击的
//                btFindphonepwdAuthcode.setClickable(false);//设置不能点击
//                btFindphonepwdAuthcode.setBackground(getResources().getDrawable(R.drawable.linear_yuanhu_bg_hui));
//                btFindphonepwdAuthcode.setText(msg.obj.toString());
//                Spannable span = new SpannableString(btFindphonepwdAuthcode.getText().toString());//获取按钮的文字
//                span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
//            } else if (msg.what == RegisterCodeTimer.END_RUNNING) {// 完成倒计时
//                btFindphonepwdAuthcode.setEnabled(true);
//                btFindphonepwdAuthcode.setClickable(true);
//                btFindphonepwdAuthcode.setText(msg.obj.toString());
//                btFindphonepwdAuthcode.setBackground(getResources().getDrawable(R.drawable.linear_yuanhu_button));//还原背景色
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
        viewPopIsApp = LayoutInflater.from(getActivity()).inflate(R.layout.item_autocode, null);
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
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
                    asyncRegister.execute(phoneNum, "3");
//                    getActivity().startService(mIntent);
                    btFindphonepwdAuthcode.onStart();
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
