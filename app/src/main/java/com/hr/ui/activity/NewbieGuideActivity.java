package com.hr.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ViewPagerAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.GetBaiduLocation;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手指导
 */
public class NewbieGuideActivity extends BaseActivity implements OnClickListener,
        OnPageChangeListener {

    private ViewPager viewPager;
    private RelativeLayout rl_background;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;
    private static final int BAIDU_READ_PHONE_STATE =100;
    private Button myClickBtn;
    private static final int[] pics = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3,R.mipmap.guide4};
    private SharedPreferencesUtils sUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }*/
        //判断版本是否支持沉浸式状态栏
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_newbie_guide);
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }
        sUtils = new SharedPreferencesUtils(NewbieGuideActivity.this);
        views = new ArrayList<View>();
        myClickBtn = (Button) findViewById(R.id.myClickBtn);
        myClickBtn.setOnClickListener(this);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            views.add(iv);
        }
        rl_background= (RelativeLayout) findViewById(R.id.background);
        rl_background.setBackgroundColor(ContextCompat.getColor(this,R.color.guide1));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(NewbieGuideActivity.this,
                MainActivity.class);
        startActivity(intent);
        sUtils.setBooleanValue(Constants.IS_FIRST, false);
        this.finish();

    }
    /**
     * 判断获取的权限的返回码
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*Toast.makeText(this,"权限获取成功",Toast.LENGTH_SHORT).show();*/
                }
                break;
            default:
                break;
        }
    }
    /**
     * 动态获取百度地图的定位权限
     *
     */
    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }
    @Override
    public void onPageScrollStateChanged(int arg0) {


    }

    @Override
    public void onPageScrolled(int index, float arg1, int dis) {
    }

    @Override
    public void onPageSelected(int index) {
        if(index==0){
            rl_background.setBackgroundColor(ContextCompat.getColor(this,R.color.guide1));
        }
        if(index==1){
            rl_background.setBackgroundColor(ContextCompat.getColor(this,R.color.guide2));
        }
        if(index==2){
            rl_background.setBackgroundColor(ContextCompat.getColor(this,R.color.guide3));
        }
        if(index==3){
            rl_background.setBackgroundColor(ContextCompat.getColor(this,R.color.guide4));
        }
        if (index == pics.length - 1) {
            myClickBtn.setVisibility(View.VISIBLE);
        } else {
            myClickBtn.setVisibility(View.GONE);
        }
    }
}
