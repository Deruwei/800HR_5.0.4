package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.NoDoubleClickListener;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncBindThird;
import com.hr.ui.utils.netutils.AsyncLogin;
import com.hr.ui.utils.netutils.AsyncNewLoginPhone;
import com.hr.ui.utils.tools.IsBind;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class BindSecondFragment extends Fragment {

    @Bind(R.id.et_bindlogin_phonenum)
    EditText etBindloginPhonenum;
    @Bind(R.id.et_bindlogin_phonepwd)
    EditText etBindloginPhonepwd;
    @Bind(R.id.frame_bindlogin_phonelogin)
    FrameLayout frameBindloginPhonelogin;
    @Bind(R.id.et_bindlogin_username)
    EditText etBindloginUsername;
    @Bind(R.id.et_bindlogin_userpwd)
    EditText etBindloginUserpwd;
    @Bind(R.id.frame_bindlogin_userlogin)
    FrameLayout frameBindloginUserlogin;
    private String third_code, third_uid, user_name, industry,third_userinfo,phoneNum,psw;
    private SharedPreferencesUtils sUtils;
    private Handler handlerLogin = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT);
            }
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    MyUtils.isLogin = true;
                    AsyncBindThird asyncBindThird = new AsyncBindThird(getActivity(),phoneNum,psw, handlerLogin,third_code, third_uid, industry,third_userinfo,"0");
                    asyncBindThird.execute();
                    break;
                default:
                    break;
            }
        }
    };
    public BindSecondFragment() {
    }
    public BindSecondFragment(String third_code, String third_uid, String industry,String third_userinfo) {
        this.third_code = third_code;
        this.third_uid = third_uid;
        this.industry = industry;
        this.third_userinfo = third_userinfo;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bind_second, container, false);
        ButterKnife.bind(this, view);
        sUtils = new SharedPreferencesUtils(getActivity());
        initListener();
        return view;
    }

    private void initListener() {
        frameBindloginPhonelogin.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    saveInfo();
                }
            });
        frameBindloginUserlogin.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                saveInfo2();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 确认登录
     */
    private void saveInfo() {
        phoneNum = etBindloginPhonenum.getText().toString().trim();
        psw= etBindloginPhonepwd.getText().toString().trim();
        String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phoneNum.trim());
        if (phoneNum == null || phoneNum.equals("") || !m.find()) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        } else if (psw == null || psw.equals("")) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            AsyncNewLoginPhone asyncLogin = new AsyncNewLoginPhone(getActivity(), handler);
            asyncLogin.execute(phoneNum, psw, sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
        }
    }
    /**
     * 确认登录
     */
    private void  saveInfo2() {
        String username = etBindloginUsername.getText().toString().trim();
        String userpassword = etBindloginUserpwd.getText().toString().trim();
        if (username == null || username.equals("")) {
            Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (userpassword == null || userpassword.equals("")) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
        } else {
            AsyncBindThird asyncBindThird = new AsyncBindThird(getActivity(), handlerLogin,third_code, third_uid, industry, username, userpassword,third_userinfo,"0");
            asyncBindThird.execute2();
        }
    }
}
