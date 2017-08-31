package com.hr.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.adapter.FindPagerAdapter;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.fragment.PhoneLoginFragment;
import com.hr.ui.fragment.UserNameLoginFragment;
import com.hr.ui.model.ResumeList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncPhoneStates;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewLoginActivity extends BaseFragmentActivity {

    @Bind(R.id.iv_newlogin_back)
    ImageView ivNewloginBack;
    @Bind(R.id.tv_newlogin_phone)
    TextView tvNewloginPhone;
    @Bind(R.id.iv_newlogin_phone)
    ImageView ivNewloginPhone;
    @Bind(R.id.tv_newlogin_username)
    TextView tvNewloginUsername;
    @Bind(R.id.iv_newlogin_username)
    ImageView ivNewloginUsername;
    @Bind(R.id.vp_newlogin)
    ViewPager vpNewlogin;
    /**
     * 关键词搜索Fragment
     */
    private PhoneLoginFragment phoneLoginFragment;
    /**
     * 分类搜索Fragment
     */
    private UserNameLoginFragment usernameLoginFragment;
    /**
     * 存放Fragment的集合
     */
    private List<Fragment> list;
    private TextView[] tvArray;
    private ImageView[] ivArray;
    private FindPagerAdapter findPagerAdapter;
    public static NewLoginActivity newLoginActivity;
    private SharedPreferencesUtils sUtils;
    private String listResumeJsonString;
    private String goType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        ButterKnife.bind(this);
        newLoginActivity = NewLoginActivity.this;
        initViewPager();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void execute() {
        if(MainActivity.instanceMain!=null) {
            MainActivity.instanceMain.refreshBaseInfo();
        }
//        if (goType.equals("1")) {
//            MainActivity.instanceMain.setTabSelect(3);
//        }
        finish();
    }

    private void initData() {
//        goType =  getIntent().getStringExtra("goType");
        sUtils = new SharedPreferencesUtils(this);
        tvArray = new TextView[]{tvNewloginPhone, tvNewloginUsername};
        ivArray = new ImageView[]{ivNewloginPhone, ivNewloginUsername};
        tvArray[0].setTextColor(Color.parseColor("#F39D0D"));
        ivArray[0].setBackgroundColor(Color.parseColor("#F39D0D"));
    }

    @OnClick({R.id.iv_newlogin_back, R.id.tv_newlogin_phone, R.id.tv_newlogin_username})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_newlogin_back:
                finish();
                break;
            case R.id.tv_newlogin_phone:
                vpNewlogin.setCurrentItem(0);
                break;
            case R.id.tv_newlogin_username:
                vpNewlogin.setCurrentItem(1);
                break;
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        list = new ArrayList<>();
        phoneLoginFragment = new PhoneLoginFragment();
        usernameLoginFragment = new UserNameLoginFragment();
        list.add(phoneLoginFragment);
        list.add(usernameLoginFragment);
        findPagerAdapter = new FindPagerAdapter(getSupportFragmentManager(), list);
        vpNewlogin.setAdapter(findPagerAdapter);
        vpNewlogin.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                resetState();
                tvArray[position].setTextColor(Color.parseColor("#F39D0D"));
                ivArray[position].setBackgroundColor(Color.parseColor("#F39D0D"));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 重置按钮
     */
    private void resetState() {
        for (TextView tv : tvArray) {
            tv.setTextColor(Color.parseColor("#333333"));
        }
        for (View iv : ivArray) {
            iv.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }
}
