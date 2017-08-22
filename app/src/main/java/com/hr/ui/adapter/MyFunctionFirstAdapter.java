package com.hr.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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

public class MyFunctionFirstAdapter extends RecyclerView.Adapter<MyFunctionFirstAdapter.MyViewHolder> {
    private Context context;
    private List<FunctionBean> functionBeen;
    private OnItemClick onItemClick;

    public void setFunctionBeen(List<FunctionBean> functionBeen) {
        this.functionBeen = functionBeen;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MyFunctionFirstAdapter(Context context){
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_function_first, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.functionFirstText.setText(functionBeen.get(position).getName());
        if(functionBeen.get(position).isSelect()==true&&functionBeen.get(position).isShowImage()==true){
            holder.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.orange));
            holder.functionFirstImage.setVisibility(View.VISIBLE);
        }else if(functionBeen.get(position).isSelect()==true&&functionBeen.get(position).isShowImage()==false){
            holder.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.orange));
            holder.functionFirstImage.setVisibility(View.GONE);
        }else{
            holder.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.darkgray));
            holder.functionFirstImage.setVisibility(View.GONE);
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
        return functionBeen.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.functionFirst_text)
        TextView functionFirstText;
        @Bind(R.id.functionFirst_image)
        ImageView functionFirstImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
