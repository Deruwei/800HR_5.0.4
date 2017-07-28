package com.hr.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.ResumeList;

import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/22 14:01
 * 邮箱：bestxt@qq.com
 * <p/>
 * 设置app简历 listview适配器
 */
public class ResumeIsAppResumePopupLVAdapter extends BaseAdapter {
    private static final String TAG = "ResumeIsAppResumePopupLVAdapter";
    private ArrayList<ResumeList> listResume;
    private Context context;
    private int selectedPosition = -1;

    public ResumeIsAppResumePopupLVAdapter(Context context, ArrayList<ResumeList> listResume) {
        this.context = context;
        this.listResume = listResume;
    }

    public void setselectedPosition(int i) {
        selectedPosition = i;
    }

    @Override
    public int getCount() {
        return listResume.size();
    }

    @Override
    public Object getItem(int position) {
        return listResume.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_popupwindow_listview_resume, null);
            viewHolder.tv_popupwindow_lsitview_myresume_name = (TextView) convertView.findViewById(R.id.tv_popupwindow_lsitview_myresume_name);
            viewHolder.tv_popupwindow_lsitview_myresume_time = (TextView) convertView.findViewById(R.id.tv_popupwindow_lsitview_myresume_time);
            viewHolder.tv_popupwindow_lsitview_myresume_complete = (TextView) convertView.findViewById(R.id.tv_popupwindow_lsitview_myresume_complete);
            viewHolder.iv_popupwindow_lsitview_check = (ImageView) convertView.findViewById(R.id.iv_popupwindow_lsitview_check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (selectedPosition == position) {
            viewHolder.iv_popupwindow_lsitview_check.setImageResource(R.mipmap.lv);
        } else {
            viewHolder.iv_popupwindow_lsitview_check.setImageResource(R.mipmap.hui);
        }
        viewHolder.tv_popupwindow_lsitview_myresume_name.setText(listResume.get(position).getTitle());
        viewHolder.tv_popupwindow_lsitview_myresume_time.setText(listResume.get(position).getAdd_time());
        int fillScallInt = Integer.parseInt(listResume.get(position).getFill_scale());
        if (fillScallInt < 60) {
            viewHolder.tv_popupwindow_lsitview_myresume_complete.setTextColor(Color.parseColor("#E60012"));
        }
        viewHolder.tv_popupwindow_lsitview_myresume_complete.setText("简历完整度" + fillScallInt + "%");

        return convertView;
    }
    class ViewHolder {
        TextView tv_popupwindow_lsitview_myresume_name, tv_popupwindow_lsitview_myresume_complete, tv_popupwindow_lsitview_myresume_time;
        ImageView iv_popupwindow_lsitview_check;
    }
}
