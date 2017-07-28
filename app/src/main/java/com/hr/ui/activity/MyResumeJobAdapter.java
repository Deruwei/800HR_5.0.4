package com.hr.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.ResumeList;
import com.hr.ui.utils.OnItemClick;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/7/28.
 */

public class MyResumeJobAdapter extends RecyclerView.Adapter<MyResumeJobAdapter.MyViewHolder> {
    private static final String TAG = "ResumeIsAppResumePopupLVAdapter";
    private ArrayList<ResumeList> listResume;
    private Context context;
    private OnItemClick onItemClick;
    private int selectedPosition = -1;

    public MyResumeJobAdapter(Context context, ArrayList<ResumeList> listResume) {
        this.context = context;
        this.listResume = listResume;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popupwindow_listview_resume, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (selectedPosition == position) {
            holder.ivPopupwindowLsitviewCheck.setImageResource(R.mipmap.lv);
        } else {
            holder.ivPopupwindowLsitviewCheck.setImageResource(R.mipmap.hui);
        }
        holder.tvPopupwindowLsitviewMyresumeName.setText(listResume.get(position).getTitle());
        holder.tvPopupwindowLsitviewMyresumeTime.setText(listResume.get(position).getAdd_time());
        int fillScallInt = Integer.parseInt(listResume.get(position).getFill_scale());
        if (fillScallInt < 60) {
            holder.tvPopupwindowLsitviewMyresumeComplete.setTextColor(Color.parseColor("#E60012"));
        }
        holder.tvPopupwindowLsitviewMyresumeComplete.setText("简历完整度" + fillScallInt + "%");
        if(onItemClick!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    onItemClick.ItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listResume.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_popupwindow_lsitview_check)
        ImageView ivPopupwindowLsitviewCheck;
        @Bind(R.id.tv_popupwindow_lsitview_myresume_name)
        TextView tvPopupwindowLsitviewMyresumeName;
        @Bind(R.id.textView54)
        TextView textView54;
        @Bind(R.id.tv_popupwindow_lsitview_myresume_time)
        TextView tvPopupwindowLsitviewMyresumeTime;
        @Bind(R.id.tv_popupwindow_lsitview_myresume_complete)
        TextView tvPopupwindowLsitviewMyresumeComplete;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
