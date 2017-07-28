package com.hr.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.Industry;

import java.util.ArrayList;

/**
 * 找工作页面的企业推荐适配器
 */
public class IndustryRecAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Industry> data = null;
    private LayoutInflater mInflater;

    public IndustryRecAdapter(Context context, ArrayList<Industry> data) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
//        Log.i("IndustryRecAdapter", "======data" + data.toString());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_keyword_lv, null);
            holder.title = (TextView) convertView.findViewById(R.id.industry_rec_title);
            holder.line = (View) convertView.findViewById(R.id.view_rec_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(data.get(position).getTitle());
        if (position == data.size() - 1) {
            holder.line.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        View line;
    }
}
