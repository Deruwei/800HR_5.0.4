package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.OtherPostParticularsActivity;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：Colin
 * 日期：2016/1/25 19:58
 * 邮箱：bestxt@qq.com
 */
public class CompanyOtherJobAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "CompanyOtherJobAdapter";
    private static ArrayList<HashMap<String, Object>> dataList;
    private static Context mContext;
    /**
     * 本地缓存图片名字
     */
    private ViewHolder viewHolder;
    private SharedPreferencesUtils sUtils;
    private LayoutInflater inflater;


    public CompanyOtherJobAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.mContext = context;
        this.dataList = list;
        inflater = LayoutInflater.from(mContext);
        sUtils = new SharedPreferencesUtils(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_my_position_lv, null);
            viewHolder.tv_item_my_position_jobname = (TextView) convertView.findViewById(R.id.tv_item_my_position_jobname);
            viewHolder.tv_item_my_position_companyname = (TextView) convertView.findViewById(R.id.tv_item_my_position_companyname);
            viewHolder.tv_item_my_position_cityname = (TextView) convertView.findViewById(R.id.tv_item_my_position_cityname);
            viewHolder.tv_item_my_positionlv_releasetime = (TextView) convertView.findViewById(R.id.tv_item_my_positionlv_releasetime);
            viewHolder.iv_item_my_positionlv_jobinfo = (ImageView) convertView.findViewById(R.id.iv_item_my_positionlv_jobinfo);
            viewHolder.rl_item_my_positionlv_click = (RelativeLayout) convertView.findViewById(R.id.rl_item_my_positionlv_click);
            viewHolder.cb_item_my_position_select = (CheckBox) convertView.findViewById(R.id.cb_item_my_position_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_my_position_jobname.setText(dataList.get(position).get("job_name").toString());
        viewHolder.tv_item_my_position_cityname.setText(dataList.get(position).get("workplace").toString());
        viewHolder.tv_item_my_position_companyname.setText(dataList.get(position).get("enterprise_name").toString());
        viewHolder.tv_item_my_positionlv_releasetime.setText(dataList.get(position).get("issue_date").toString());
        viewHolder.rl_item_my_positionlv_click.setTag(dataList.get(position).get("job_id").toString());
        viewHolder.cb_item_my_position_select.setVisibility(View.GONE);
        viewHolder.rl_item_my_positionlv_click.setOnClickListener(this);
//        StringBuffer sb = new StringBuffer();
//        if (dataList.get(position).get("is_apply").toString().equals("1")) {
//            sb.append("   已投递");
//        }
//        if (dataList.get(position).get("is_favourite").toString().equals("1")) {
//            sb.append("   已收藏");
//        }
//        if (sb.toString().trim().length() > 0) {
//            viewHolder.tv_item_my_positionlv_applycollection.setVisibility(View.VISIBLE);
//            viewHolder.tv_item_my_positionlv_applycollection.setText(sb.toString());
//        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_item_my_position_jobname, tv_item_my_positionlv_applycollection, tv_item_my_position_companyname, tv_item_my_position_cityname, tv_item_my_positionlv_releasetime;
        ImageView iv_item_my_positionlv_jobinfo;
        RelativeLayout rl_item_my_positionlv_click;
        CheckBox cb_item_my_position_select;
    }

    /**
     * 跳转到职位详情页
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String job_id = (String) v.getTag();
        Intent intent = new Intent(mContext, OtherPostParticularsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("job_id", job_id);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
