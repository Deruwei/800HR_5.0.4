package com.hr.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hr.ui.model.ResumeEducation;

/**
 * Created by tx on 2017/7/5.
 */

public class LVResumeEduAdapter extends BaseAdapter {
    private ResumeEducation[] resumeEducations;
    private Context context;

    public LVResumeEduAdapter(ResumeEducation[] resumeEducations, Context context) {
        this.resumeEducations = resumeEducations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return resumeEducations.length;
    }

    @Override
    public Object getItem(int position) {
        return resumeEducations[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
