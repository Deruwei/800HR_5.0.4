package com.hr.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hr.ui.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：Colin
 * 日期：2015/12/18 16:10
 * 邮箱：bestxt@qq.com
 */
public class SelectFunctionSecondAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context mContext;
    private LayoutInflater mInflater;

    public SelectFunctionSecondAdapter(ArrayList<HashMap<String, String>> list, Context context) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.from(mContext).inflate(R.layout.item_selectfunction_second, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_selectfunction_second = (TextView) convertView.findViewById(R.id.tv_selectfunction_second);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_selectfunction_second.setText(list.get(position).get("value"));
        return convertView;
    }

    class ViewHolder {
        TextView tv_selectfunction_second;
    }
}
