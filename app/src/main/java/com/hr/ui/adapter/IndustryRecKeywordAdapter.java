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
 * Created by wdr on 2017/8/8.
 */

public class IndustryRecKeywordAdapter extends RecyclerView.Adapter<IndustryRecKeywordAdapter.MyViewhoder> {
    private Context context;
    private ArrayList<Industry> industries;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public IndustryRecKeywordAdapter(Context context, ArrayList<Industry> industries) {
        this.context = context;
        this.industries = industries;
    }

    @Override
    public MyViewhoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keyword_lv, parent,false);
        return new MyViewhoder(view);
    }

    @Override
    public void onBindViewHolder(MyViewhoder holder, final int position) {
        holder.industryRecTitle.setText(industries.get(position).getTitle());
        if (position == industries.size() - 1) {
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
        return industries.size();
    }

    static class MyViewhoder extends RecyclerView.ViewHolder {
        @Bind(R.id.industry_rec_title)
        TextView industryRecTitle;
        @Bind(R.id.view_rec_title)
        View viewRecTitle;

        public MyViewhoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
