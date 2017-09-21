package com.hr.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.bean.CityBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/9/19.
 */

public class SelectCitySecondAdapter extends BaseAdapter {

    private Context context;
    private List<CityBean> list;
    private int position = 0;
    private boolean islodingimg = true;
    ViewHolder hold;

    public SelectCitySecondAdapter(Context context, List<CityBean> list) {
        this.context = context;
        this.list = list;
    }

    public SelectCitySecondAdapter(Context context, List<CityBean> list,
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
            view = View.inflate(context, R.layout.item_select_city_second, null);
            hold = new ViewHolder(view);
            view.setTag(hold);
        } else {
            hold = (ViewHolder) view.getTag();
        }
        hold.tvSelectCitySecondName.setText(list.get(arg0).getName());
        if(list.get(arg0).getId().endsWith("00")){
            hold.tvSelectCitySecondName.getPaint().setFakeBoldText(true);
        }
        if (list.get(arg0).isCheck() == true) {
            hold.tvSelectCitySecondName.setTextColor(ContextCompat.getColor(context, R.color.orange));
            hold.ivSelectCitySecondName.setVisibility(View.VISIBLE);
        } else {
            hold.tvSelectCitySecondName.setTextColor(ContextCompat.getColor(context, R.color.darkgray));
            hold.ivSelectCitySecondName.setVisibility(View.GONE);
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
        @Bind(R.id.tv_selectCity_secondName)
        TextView tvSelectCitySecondName;
        @Bind(R.id.iv_selectCity_secondName)
        ImageView ivSelectCitySecondName;
        @Bind(R.id.rl_selectCity_secondName)
        RelativeLayout rlSelectCitySecondName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
