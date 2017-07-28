package com.hr.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
 * 日期：2015/12/28 15:21
 * 邮箱：bestxt@qq.com
 */
public class PopupFiltrateListViewAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context mContext;
    private ViewHolder viewHolder;
    private int selectedPosition = -1;

    public PopupFiltrateListViewAdapter(ArrayList<HashMap<String, String>> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    public void setselectedPosition(int i) {
        selectedPosition = i;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popupfiltrate_second, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_popupfiltrate_second = (TextView) convertView.findViewById(R.id.tv_popupfiltrate_second);
            viewHolder.iv_item_popupfiltre_select = (TextView) convertView.findViewById(R.id.iv_item_popupfiltre_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_popupfiltrate_second.setText(list.get(position).get("value"));
        if (selectedPosition == position) {
            viewHolder.tv_popupfiltrate_second.setTextColor(Color.parseColor("#F39701"));
            viewHolder.iv_item_popupfiltre_select.setBackgroundResource(R.mipmap.duihao);
        } else {
            viewHolder.tv_popupfiltrate_second.setTextColor(Color.parseColor("#666666"));
            viewHolder.iv_item_popupfiltre_select.setBackgroundColor(Color.parseColor("#00000000"));
        }
        return convertView;
    }

    class ViewHolder {

        TextView tv_popupfiltrate_second;
        TextView iv_item_popupfiltre_select;
    }
}
