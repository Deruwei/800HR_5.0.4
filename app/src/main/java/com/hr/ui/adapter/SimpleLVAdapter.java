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
 * 日期：2016/1/26 16:37
 * 邮箱：bestxt@qq.com
 */
public class SimpleLVAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context context;
    private LayoutInflater mInflater;

    public SimpleLVAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_easy_text, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_item_easy = (TextView) convertView.findViewById(R.id.tv_item_easy);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_item_easy.setText(list.get(position).get("value"));

        return convertView;
    }

    class ViewHolder {
        TextView tv_item_easy;
    }
}
