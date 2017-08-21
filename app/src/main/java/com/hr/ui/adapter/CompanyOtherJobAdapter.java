package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.OtherPostParticularsActivity;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Colin
 * 日期：2016/1/25 19:58
 * 邮箱：bestxt@qq.com
 */
public class CompanyOtherJobAdapter extends RecyclerView.Adapter<CompanyOtherJobAdapter.MyViewHolder> implements View.OnClickListener{
    private static final String TAG = "CompanyOtherJobAdapter";
    private static ArrayList<HashMap<String, Object>> dataList;
    private static Context mContext;
    /**
     * 本地缓存图片名字
     */
    private SharedPreferencesUtils sUtils;

    public  void setDataList(ArrayList<HashMap<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public CompanyOtherJobAdapter(Context context) {
        this.mContext = context;
        sUtils = new SharedPreferencesUtils(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_position_lv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvItemMyPositionJobname.setText(dataList.get(position).get("job_name").toString());
        holder.tvItemMyPositionCityname.setText(dataList.get(position).get("workplace").toString());
        holder.tvItemMyPositionCompanyname.setText(dataList.get(position).get("enterprise_name").toString());
        holder.tvItemMyPositionlvReleasetime.setText(dataList.get(position).get("issue_date").toString());
        holder.rlItemMyPositionlvClick.setTag(dataList.get(position).get("job_id").toString());
        holder.cbItemMyPositionSelect.setVisibility(View.GONE);
        holder.rlItemMyPositionlvClick.setOnClickListener(this);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return  dataList==null ? 0 : dataList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_item_my_position_select)
        CheckBox cbItemMyPositionSelect;
        @Bind(R.id.tv_item_my_position_jobname)
        TextView tvItemMyPositionJobname;
        @Bind(R.id.tv_item_my_position_companyname)
        TextView tvItemMyPositionCompanyname;
        @Bind(R.id.tv_item_my_position_cityname)
        TextView tvItemMyPositionCityname;
        @Bind(R.id.textView28)
        TextView textView28;
        @Bind(R.id.tv_item_my_positionlv_releasetime)
        TextView tvItemMyPositionlvReleasetime;
        @Bind(R.id.iv_item_my_positionlv_jobinfo)
        ImageView ivItemMyPositionlvJobinfo;
        @Bind(R.id.tv_item_my_positionlv_applycollection)
        TextView tvItemMyPositionlvApplycollection;
        @Bind(R.id.rl_item_my_positionlv_click)
        RelativeLayout rlItemMyPositionlvClick;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    /**
     * 跳转到职位详情页
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String job_id = (String) v.getTag();
        Intent intent = new Intent(mContext, PostParticularsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("job_id", job_id);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
