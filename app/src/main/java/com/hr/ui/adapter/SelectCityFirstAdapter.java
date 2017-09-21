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

public class SelectCityFirstAdapter extends BaseAdapter {

    private Context context;
    private List<CityBean> list;
    private int position = 0;
    private boolean islodingimg = true;
    ViewHolder hold;
    private int num;

    public SelectCityFirstAdapter(Context context, List<CityBean> list) {
        this.context = context;
        this.list = list;
    }

    public SelectCityFirstAdapter(Context context, List<CityBean> list,
                                  int num) {
        this.context = context;
        this.list = list;
        this.num=num;
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
            view = View.inflate(context, R.layout.item_select_city_first, null);
            hold = new ViewHolder(view);
            view.setTag(hold);
        } else {
            hold = (ViewHolder) view.getTag();
        }
        if(num!=0) {
            if (arg0 == 0) {
                hold.tvSelectCityTitle.setVisibility(View.VISIBLE);
                hold.tvSelectCityTitle.setText("定位城市");
            } else if (arg0 == 1) {
                hold.tvSelectCityTitle.setVisibility(View.VISIBLE);
                hold.tvSelectCityTitle.setText("热门城市");
            } else if (arg0 == num - 4) {
                hold.tvSelectCityTitle.setVisibility(View.VISIBLE);
                hold.tvSelectCityTitle.setText("按省市选择城市");
            } else {
                hold.tvSelectCityTitle.setVisibility(View.GONE);
                hold.viewSelectCity.setVisibility(View.GONE);
            }
        }else{
            if(arg0<4) {
                hold.ivSelectCityName.setImageResource(R.mipmap.duihao);
            }else{
                hold.ivSelectCityName.setImageResource(R.mipmap.jiantou_right);
            }
            hold.tvSelectCityTitle.setVisibility(View.GONE);
            hold.viewSelectCity.setVisibility(View.GONE);
        }
        hold.tvSelectCityName.setText(list.get(arg0).getName());
        if (list.get(arg0).isCheck() == true&&list.get(arg0).isSign()==true) {
            hold.tvSelectCityName.setTextColor(ContextCompat.getColor(context, R.color.orange));
            hold.ivSelectCityName.setVisibility(View.VISIBLE);
        } else if(list.get(arg0).isCheck()==false&&list.get(arg0).isSign()==true){
            hold.tvSelectCityName.setTextColor(ContextCompat.getColor(context, R.color.orange));
            hold.ivSelectCityName.setVisibility(View.GONE);
        }else{
            hold.tvSelectCityName.setTextColor(ContextCompat.getColor(context, R.color.darkgray));
            hold.ivSelectCityName.setVisibility(View.GONE);
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
        @Bind(R.id.tv_selectCity_title)
        TextView tvSelectCityTitle;
        @Bind(R.id.view_selectCity)
        View viewSelectCity;
        @Bind(R.id.tv_selectCity_name)
        TextView tvSelectCityName;
        @Bind(R.id.iv_selectCity_name)
        ImageView ivSelectCityName;
        @Bind(R.id.rl_selectCity_name)
        RelativeLayout rlSelectCityName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
