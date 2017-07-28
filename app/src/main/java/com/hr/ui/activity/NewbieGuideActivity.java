package com.hr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hr.ui.R;
import com.hr.ui.adapter.ViewPagerAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 新手指导
 */
public class NewbieGuideActivity extends BaseActivity implements OnClickListener,
        OnPageChangeListener {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private List<View> views;

    private Button myClickBtn;
    private static final int[] pics = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3,R.mipmap.guide4};
    private SharedPreferencesUtils sUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbie_guide);
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

    @Override
    public void onPageScrollStateChanged(int arg0) {


    }

    @Override
    public void onPageScrolled(int index, float arg1, int dis) {
    }

    @Override
    public void onPageSelected(int index) {
        if (index == pics.length - 1) {
            myClickBtn.setVisibility(View.VISIBLE);
        } else {
            myClickBtn.setVisibility(View.GONE);
        }
    }
}
