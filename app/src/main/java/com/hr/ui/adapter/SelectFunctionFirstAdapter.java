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
 * 日期：2015/12/17 16:57
 * 邮箱：bestxt@qq.com
 */
public class SelectFunctionFirstAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> list;
    private Context mContext;
    private LayoutInflater mInflater;

    public SelectFunctionFirstAdapter(ArrayList<HashMap<String, String>> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.item_selectfunction_first, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_selectfunction_first = (TextView) convertView.findViewById(R.id.tv_selectfunction_first);
            viewHolder.tv_selectfunction_firstmark = (TextView) convertView.findViewById(R.id.tv_selectfunction_firstmark);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_selectfunction_first.setText(list.get(position).get("value"));
        viewHolder.tv_selectfunction_firstmark.setText(list.get(position).get("valueMark"));
        return convertView;
    }

    class ViewHolder {
        TextView tv_selectfunction_first, tv_selectfunction_firstmark;
    }
}
