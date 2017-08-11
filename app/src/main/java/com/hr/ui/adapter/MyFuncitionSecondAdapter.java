package com.hr.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.utils.OnItemClick;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/8/11.
 */

public class MyFuncitionSecondAdapter extends RecyclerView.Adapter<MyFuncitionSecondAdapter.MyViewHolder> {
    private Context context;
    private List<FunctionBean> functionBean;
    private OnItemClick onItemClick;

    public MyFuncitionSecondAdapter(Context context, List<FunctionBean> functionBean) {
        this.context = context;
        this.functionBean=functionBean;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_function_second, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(functionBean.get(position).isSelect()==true){
            holder.functionSecondImage.setVisibility(View.VISIBLE);
        }else{
            holder.functionSecondImage.setVisibility(View.GONE);
        }
        holder.functionSecondText.setText(functionBean.get(position).getName());
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
        return functionBean.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.functionSecond_text)
        TextView functionSecondText;
        @Bind(R.id.functionSecond_image)
        ImageView functionSecondImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
