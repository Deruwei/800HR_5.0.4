package com.hr.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.adapter.FindPagerAdapter;
import com.hr.ui.fragment.FindPhonePWDFragment;
import com.hr.ui.fragment.FindUserNamePWDFragment;
import com.hr.ui.fragment.PhoneLoginFragment;
import com.hr.ui.fragment.UserNameLoginFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewFindPwdActivity extends BaseFragmentActivity {

    @Bind(R.id.iv_newfindpwd_back)
    ImageView ivNewfindpwdBack;
    @Bind(R.id.tv_newfindpwd_phone)
    TextView tvNewfindpwdPhone;
    @Bind(R.id.iv_newfindpwd_phone)
    ImageView ivNewfindpwdPhone;
    @Bind(R.id.tv_newfindpwd_username)
    TextView tvNewfindpwdUsername;
    @Bind(R.id.iv_newfindpwd_username)
    ImageView ivNewfindpwdUsername;
    @Bind(R.id.vp_newfindpwd)
    ViewPager vpNewfindpwd;
    /**
     * 手机找回密码Fragment
     */
    private FindPhonePWDFragment findPhonePWDFragment;
    /**
     * 邮箱找回密码Fragment
     */
    private FindUserNamePWDFragment findUserNamePWDFragment;
    /**
     * 存放Fragment的集合
     */
    private List<Fragment> list;
    private TextView[] tvArray;
    private ImageView[] ivArray;
    private FindPagerAdapter findPagerAdapter;
    public static NewFindPwdActivity newFindPwdActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_find_pwd);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        newFindPwdActivity = NewFindPwdActivity.this;
        initViewPager();
        initData();

    }

    @OnClick({R.id.iv_newfindpwd_back, R.id.tv_newfindpwd_phone, R.id.iv_newfindpwd_phone, R.id.tv_newfindpwd_username, R.id.iv_newfindpwd_username})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_newfindpwd_back:
                finish();
                break;
            case R.id.tv_newfindpwd_phone:
                vpNewfindpwd.setCurrentItem(0);
                break;
            case R.id.iv_newfindpwd_phone:
                break;
            case R.id.tv_newfindpwd_username:
                vpNewfindpwd.setCurrentItem(1);
                break;
            case R.id.iv_newfindpwd_username:
                break;
        }
    }

    private void initData() {
        tvArray = new TextView[]{tvNewfindpwdPhone, tvNewfindpwdUsername};
        ivArray = new ImageView[]{ivNewfindpwdPhone, ivNewfindpwdUsername};
        tvArray[0].setTextColor(Color.parseColor("#F39D0D"));
        ivArray[0].setBackgroundColor(Color.parseColor("#F39D0D"));
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        list = new ArrayList<>();
        findPhonePWDFragment = new FindPhonePWDFragment();
        findUserNamePWDFragment = new FindUserNamePWDFragment();
        list.add(findPhonePWDFragment);
        list.add(findUserNamePWDFragment);
        findPagerAdapter = new FindPagerAdapter(getSupportFragmentManager(), list);
        vpNewfindpwd.setAdapter(findPagerAdapter);
        vpNewfindpwd.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
