package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.adapter.FindPagerAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.BindFirstFragment;
import com.hr.ui.fragment.BindSecondFragment;
import com.hr.ui.fragment.PhoneLoginFragment;
import com.hr.ui.fragment.UserNameLoginFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 第三方账号绑定
 */
public class NewBindActivity extends BaseFragmentActivity {

    @Bind(R.id.iv_newbind_back)
    ImageView ivNewbindBack;
    @Bind(R.id.tv_newbind_phone)
    TextView tvNewbindPhone;
    @Bind(R.id.iv_newbind_phone)
    ImageView ivNewbindPhone;
    @Bind(R.id.tv_newbind_username)
    TextView tvNewbindUsername;
    @Bind(R.id.iv_newbind_username)
    ImageView ivNewbindUsername;
    @Bind(R.id.vp_newbind)
    ViewPager vpNewbind;
    /**
     * 第一次访问Fragment
     */
    private BindFirstFragment bindFirstFragment;
    /**
     * 已有账号绑定Fragment
     */
    private BindSecondFragment bindSecondFragment;
    /**
     * 存放Fragment的集合
     */
    private List<Fragment> list;
    private TextView[] tvArray;
    private ImageView[] ivArray;
    private FindPagerAdapter findPagerAdapter;
    private String uid;
    private String opf;
    private String name;
    private String birthday;
    private String nickname;
    private String tinyurl;
    private String gender;
    private String third_userinfo;
    private int industry;
    private boolean isFromPush = false;
    public static NewBindActivity newBindActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bind);
        ButterKnife.bind(this);
        newBindActivity = NewBindActivity.this;
        getData();
        initViewPager();
        initData();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
////        if (MyUtils.isLogin) {
////            finish();
////        }
//    }

    private void initData() {
        tvArray = new TextView[]{tvNewbindPhone, tvNewbindUsername};
        ivArray = new ImageView[]{ivNewbindPhone, ivNewbindUsername};
        tvArray[0].setTextColor(Color.parseColor("#F39D0D"));
        ivArray[0].setBackgroundColor(Color.parseColor("#F39D0D"));

    }

    /**
     * 获取数据
     */
    private void getData() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        opf = intent.getStringExtra("platform");
        name = intent.getStringExtra("name");
        birthday = intent.getStringExtra("birthday");
        nickname = intent.getStringExtra("nickname");
        tinyurl = intent.getStringExtra("tinyurl");
        gender = intent.getStringExtra("gender");
        isFromPush = intent.getBooleanExtra("isFromPush", false);
        third_userinfo ="{"+"nickname:"+nickname+",birthday:"+birthday+"}";
        SharedPreferences sp = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(this);
        industry = sUtils.getIntValue(Constants.INDUSTRY, 11);
    }

    @OnClick({R.id.iv_newbind_back, R.id.tv_newbind_phone, R.id.tv_newbind_username})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_newbind_back:
                finish();
                break;
            case R.id.tv_newbind_phone:
                vpNewbind.setCurrentItem(0);
                break;
            case R.id.tv_newbind_username:
                vpNewbind.setCurrentItem(1);
                break;
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        list = new ArrayList<>();
        bindFirstFragment = new BindFirstFragment(opf, uid, industry + "",third_userinfo);
        bindSecondFragment = new BindSecondFragment(opf, uid, industry + "",third_userinfo);
        list.add(bindFirstFragment);
        list.add(bindSecondFragment);
        findPagerAdapter = new FindPagerAdapter(getSupportFragmentManager(), list);
        vpNewbind.setAdapter(findPagerAdapter);
        vpNewbind.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
