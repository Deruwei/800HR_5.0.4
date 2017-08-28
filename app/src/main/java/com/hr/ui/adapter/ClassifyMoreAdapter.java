package com.hr.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.bean.FunctionBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClassifyMoreAdapter extends BaseAdapter {

    private Context context;
    private List<FunctionBean> text_list;
    private int position = 0;
   ViewHolder hold;

    public ClassifyMoreAdapter(Context context, List<FunctionBean> text_list) {
        this.context = context;
        this.text_list = text_list;
    }

    public int getCount() {
        return text_list.size();
    }

    public Object getItem(int position) {
        return text_list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int arg0, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_function_second, null);
            hold = new ViewHolder(view);
            view.setTag(hold);
        } else {
            hold = (ViewHolder) view.getTag();
        }
        if(text_list.get(arg0).isSelect()==true){
            hold.functionSecondImage.setVisibility(View.VISIBLE);
            if ("0".equalsIgnoreCase(text_list.get(arg0).getId())
                    || "000".contains(text_list.get(arg0).getId().subSequence(text_list.get(arg0).getId().length() - 3,
                    text_list.get(arg0).getId().length()))) {
                hold.functionSecondText.getPaint().setFakeBoldText(true);
                hold.functionSecondText.setTextColor(ContextCompat.getColor(context,R.color.orange));
                // 加粗
                // TextPaint tpaint = tv.getPaint();
                // tpaint.setFakeBoldText(true);
            }else{
                hold.functionSecondText.setTextColor(ContextCompat.getColor(context,R.color.orange));
            }
        }else{
            hold.functionSecondImage.setVisibility(View.GONE);
            if ("0".equalsIgnoreCase(text_list.get(arg0).getId())
                    || "000".contains(text_list.get(arg0).getId().subSequence(text_list.get(arg0).getId().length() - 3,
                    text_list.get(arg0).getId().length()))) {
                hold.functionSecondText.getPaint().setFakeBoldText(true);
                hold.functionSecondText.setTextColor(ContextCompat.getColor(context,R.color.black));
                // 加粗
                // TextPaint tpaint = tv.getPaint();
                // tpaint.setFakeBoldText(true);
            }else{
                hold.functionSecondText.setTextColor(ContextCompat.getColor(context,R.color.darkgray));
            }

        }
        hold.functionSecondText.setText(text_list.get(arg0).getName());
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
    }


    static class ViewHolder {
        @Bind(R.id.functionSecond_text)
        TextView functionSecondText;
        @Bind(R.id.functionSecond_image)
        ImageView functionSecondImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
