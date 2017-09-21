package com.hr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.OnItemClick;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Colin
 * 日期：2016/5/13 09:07
 * 邮箱：bestxt@qq.com
 */
public class IndustryRecAdapter2 extends RecyclerView.Adapter<IndustryRecAdapter2.MyViewHolder> {
    private Context context;
    private ArrayList<Industry> data = null;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setData(ArrayList<Industry> data) {
        this.data = data;
    }

    public IndustryRecAdapter2(Context context) {
        this.context = context;
//        Log.i("IndustryRecAdapter", "======data" + data.toString());
    }


    @Override
    public IndustryRecAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keyword_lv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IndustryRecAdapter2.MyViewHolder holder, final int position) {
        holder.industryRecTitle.setText(data.get(position).getTitle());
        if (position == data.size() - 1) {
            holder.viewRecTitle.setVisibility(View.GONE);
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
        return data.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.industry_rec_title)
        TextView industryRecTitle;
        @Bind(R.id.view_rec_title)
        View viewRecTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
