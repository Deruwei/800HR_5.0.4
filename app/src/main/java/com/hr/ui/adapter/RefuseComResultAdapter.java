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
 * 日期：2016/4/25 09:57
 * 邮箱：bestxt@qq.com
 */
public class RefuseComResultAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> arrayList;
    private Context mContext;


    public RefuseComResultAdapter(ArrayList<HashMap<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_refusecom_result_adapter, null);
            viewHolder.tv_refusecomitem = (TextView) convertView.findViewById(R.id.tv_refusecomitem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_refusecomitem.setText(arrayList.get(position).get("enterprise_name").toString());
        return convertView;
    }

    class ViewHolder {
        TextView tv_refusecomitem;
    }
}
