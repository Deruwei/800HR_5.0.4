package com.hr.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.BrowsedInfo;
import com.hr.ui.utils.OnItemClick;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/8/9.
 */

public class MyWhoLookMeAdapter extends RecyclerView.Adapter<MyWhoLookMeAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BrowsedInfo> listBrowsedInfos;
    private OnItemClick onItemClick;

    public void setListBrowsedInfos(ArrayList<BrowsedInfo> listBrowsedInfos) {
        this.listBrowsedInfos = listBrowsedInfos;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MyWhoLookMeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wholookme_listview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.companyName.setText(listBrowsedInfos.get(position)
                .getEnterprise_name());
        holder.companyTime.setText("浏览日期:"
                + listBrowsedInfos.get(position).getBrowsed_time());
        holder.companyScale.setText("公司规模:"
                + listBrowsedInfos.get(position).getStuffmunber());
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
        return listBrowsedInfos==null ? 0 : listBrowsedInfos.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageView1)
        ImageView imageView1;
        @Bind(R.id.company_name)
        TextView companyName;
        @Bind(R.id.company_scale)
        TextView companyScale;
        @Bind(R.id.company_time)
        TextView companyTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
