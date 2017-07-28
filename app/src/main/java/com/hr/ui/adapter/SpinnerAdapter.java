package com.hr.ui.adapter;


import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者：Colin
 * 日期：2016/1/18 15:11
 * 邮箱：bestxt@qq.com
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items = new String[]{};

    public SpinnerAdapter(final Context context, final int textViewResourceId,
                          final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setTextColor(Color.parseColor("#949390"));
        tv.setTextSize(15);
//		tv .setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setSingleLine(false);
//		tv .setMarqueeRepeatLimit(6);
        return convertView;
    }

    /**
     * UI最终显示
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        // android.R.id.text1 is default text view in resource of the android.
        // android.R.layout.simple_spinner_item is default layout in resources
        // of android.

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(items[position]);
        tv.setTextColor(Color.parseColor("#F7931E"));
        tv.setTextSize(15);
        tv.setGravity(Gravity.RIGHT);
//		tv .setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setSingleLine(false);
//		tv .setMarqueeRepeatLimit(6);
        return convertView;
    }
}

