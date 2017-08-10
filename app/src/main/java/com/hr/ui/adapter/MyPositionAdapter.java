package com.hr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.PositionInfo;
import com.hr.ui.utils.OnItemClick;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/8/10.
 */

public class MyPositionAdapter extends RecyclerView.Adapter<MyPositionAdapter.MyViewHolder> {

    private Context context;
    ArrayList<PositionInfo> listapplied;
    private OnItemClick onItemClick;
    public MyPositionAdapter(Context context){
        this.context=context;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setListapplied(ArrayList<PositionInfo> listapplied) {
        this.listapplied = listapplied;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_position_lv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (listapplied.size() != 0) {
            holder.cbItemMyPositionSelect.setVisibility(View.GONE);
           holder.tvItemMyPositionCompanyname.setText(listapplied.get(position).getEnterprise_name());
            holder.tvItemMyPositionJobname.setText(listapplied.get(position).getJob_name());
            holder.tvItemMyPositionCityname.setText("已有" + listapplied.get(position).getApplied_num() + "人申请");
            holder.tvItemMyPositionlvReleasetime.setText(listapplied.get(position).getApplied_time());

        }
        if(onItemClick!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.ItemClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listapplied.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_item_my_position_select)
        CheckBox cbItemMyPositionSelect;
        @Bind(R.id.tv_item_my_position_jobname)
        TextView tvItemMyPositionJobname;
        @Bind(R.id.tv_item_my_position_companyname)
        TextView tvItemMyPositionCompanyname;
        @Bind(R.id.tv_item_my_position_cityname)
        TextView tvItemMyPositionCityname;
        @Bind(R.id.textView28)
        TextView textView28;
        @Bind(R.id.tv_item_my_positionlv_releasetime)
        TextView tvItemMyPositionlvReleasetime;
        @Bind(R.id.iv_item_my_positionlv_jobinfo)
        ImageView ivItemMyPositionlvJobinfo;
        @Bind(R.id.tv_item_my_positionlv_applycollection)
        TextView tvItemMyPositionlvApplycollection;
        @Bind(R.id.rl_item_my_positionlv_click)
        RelativeLayout rlItemMyPositionlvClick;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
