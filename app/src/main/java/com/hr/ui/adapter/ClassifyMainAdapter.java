package com.hr.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.bean.FunctionBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassifyMainAdapter extends BaseAdapter {

    private Context context;
    private List<FunctionBean> list;
    private int position = 0;
    private boolean islodingimg = true;
    ViewHolder hold;

    public ClassifyMainAdapter(Context context, List<FunctionBean> list) {
        this.context = context;
        this.list = list;
    }

    public ClassifyMainAdapter(Context context, List<FunctionBean> list,
                               boolean islodingimg) {
        this.context = context;
        this.list = list;
        this.islodingimg = islodingimg;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_function_first, null);
            hold = new ViewHolder(view);
            view.setTag(hold);
        } else {
            hold = (ViewHolder) view.getTag();
        }
        hold.functionFirstText.setText(list.get(arg0).getName());
        if(list.get(arg0).isSelect()==true&&list.get(arg0).isShowImage()==true){
            hold.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.orange));
            hold.functionFirstImage.setVisibility(View.VISIBLE);
        }else if(list.get(arg0).isSelect()==true&&list.get(arg0).isShowImage()==false){
            hold.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.orange));
            hold.functionFirstImage.setVisibility(View.GONE);
        }else{
            hold.functionFirstText.setTextColor(ContextCompat.getColor(context,R.color.darkgray));
            hold.functionFirstImage.setVisibility(View.GONE);
        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }

    public int getSelectItem() {
        return position;
    }

    static class ViewHolder {
        @Bind(R.id.functionFirst_text)
        TextView functionFirstText;
        @Bind(R.id.functionFirst_image)
        ImageView functionFirstImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
