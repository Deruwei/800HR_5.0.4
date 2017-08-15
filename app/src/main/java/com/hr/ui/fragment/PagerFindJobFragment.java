package com.hr.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hr.ui.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFindJobFragment extends BaseFragment {


    public PagerFindJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_find_job, container, false);
    }
}
