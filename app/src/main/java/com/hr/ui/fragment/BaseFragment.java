package com.hr.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by wdr on 2017/8/14.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onResume(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}
