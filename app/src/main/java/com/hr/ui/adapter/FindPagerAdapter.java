package com.hr.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.List;

/**
 * 作者：Colin
 * 日期：2015/12/8 17:59
 * 邮箱：bestxt@qq.com
 */
public class FindPagerAdapter extends FragmentPagerAdapter {
    private final String TAG = "FindPagerAdapter";
    private List<Fragment> list = null;

    public FindPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }




    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
