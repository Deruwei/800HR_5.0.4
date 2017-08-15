package com.hr.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.netutils.AsyncFindPhonePWD;
import com.hr.ui.utils.netutils.AsyncFindUsernamePWD;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindUserNamePWDFragment extends BaseFragment {


    @Bind(R.id.et_findusernamepwd_username)
    EditText etFindusernamepwdUsername;
    @Bind(R.id.et_findusernamepwd_authcode)
    EditText etFindusernamepwdAuthcode;
    @Bind(R.id.frame_findusernamepwd_login)
    FrameLayout frameFindusernamepwdLogin;
    private String username, email;

    public FindUserNamePWDFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_user_name_pwd, container, false);
        ButterKnife.bind(this, view);
        MobclickAgent.onEvent(getActivity(), "cv-reset-password");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.frame_findusernamepwd_login)
    public void onClick() {
        username = etFindusernamepwdUsername.getText().toString().trim();
        email = etFindusernamepwdAuthcode.getText().toString().trim();
        String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        if (username.equals("")) {
            Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
        } else if (email.equals("")||!isMatched) {
            Toast.makeText(getActivity(), "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
        } else {
            AsyncFindUsernamePWD asyncNewRegister = new AsyncFindUsernamePWD(getActivity());
            asyncNewRegister.execute(email, username);
        }
    }
}
