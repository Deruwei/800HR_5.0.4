package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MyCollectionActivity;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Colin
 * 日期：2016/1/25 19:58
 * 邮箱：bestxt@qq.com
 */
public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder> {
    private static final String TAG = "MyCollectionAdapter";
    private  ArrayList<HashMap<String, Object>> dataList;
    private  Context mContext;
    private  HashMap<Integer, Boolean> isSelected=new HashMap<>();
    /**
     * 本地缓存图片名字
     */
    private SharedPreferencesUtils sUtils;
    private LayoutInflater inflater;
    private int error_code;
    private Handler handler;


    public  void setDataList(ArrayList<HashMap<String, Object>> dataList) {
        this.dataList = dataList;
        sUtils = new SharedPreferencesUtils(mContext);
    }

    public MyCollectionAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);


    }

    // 初始化isSelected的数据
    public void initData() {
        isSelected=new HashMap<>();
            for (int i = 0; i < dataList.size(); i++) {
                isSelected.put(i, false);
            }
    }

    /**
     * 简历投递handler
     */
    private Handler handlerSendResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            if (handler != null) {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                            Toast.makeText(mContext, "申请职位成功", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case 304:// 为设置默认简历
                            Toast.makeText(mContext, "请先到简历中心设置默认简历",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext,
                                    Rc4Md5Utils.getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

        ;
    };

    public Handler handlerCollectJob = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            if (handler != null) {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                            Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, MyCollectionActivity.class);
                            mContext.startActivity(intent);
                            MyCollectionActivity.instance.finish();
                            break;
                        case 304:// 为设置默认简历
                            Toast.makeText(mContext, "前先选择APP简历", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "取消收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchjob_result, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvItemSearchJobJobName.setText(dataList.get(position).get("job_name").toString());
        holder.tvItemSearchjobCityname.setText(dataList.get(position).get("workplace").toString());
        holder.tvItemSearchjobCompanyname.setText(dataList.get(position).get("enterprise_name").toString());
        holder.tvItemSearchjoblvReleasetime.setText(dataList.get(position).get("favourite_time").toString());
        holder.rlItemSearchjoblvClick.setTag(dataList.get(position).get("job_id").toString());
        holder.rlItemSearchjoblvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(dataList.get(position).get("is_expire"))) {
                    String job_id = (String) v.getTag();
                    // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PostParticularsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("job_id", job_id);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "该职位已过期", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        if (isSelected.get(position) == null) {
            isSelected.put(position, false);
        }
        holder.cbItemSearchjobSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        Toast.makeText(mContext, "" + position + isChecked, Toast.LENGTH_SHORT).show();
                isSelected.put(position, isChecked);
            }
        });
        holder.cbItemSearchjobSelect.setChecked(isSelected.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataList==null ? 0 : dataList.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cb_item_searchjob_select)
        CheckBox cbItemSearchjobSelect;
        @Bind(R.id.tv_item_searchJob_jobName)
        TextView tvItemSearchJobJobName;
        @Bind(R.id.tv_item_searchjob_companyname)
        TextView tvItemSearchjobCompanyname;
        @Bind(R.id.tv_item_searchjob_cityname)
        TextView tvItemSearchjobCityname;
        @Bind(R.id.tv_item_searchjoblv_releasetime)
        TextView tvItemSearchjoblvReleasetime;
        @Bind(R.id.iv_item_searchjoblv_jobinfo)
        ImageView ivItemSearchjoblvJobinfo;
        @Bind(R.id.tv_item_searchjoblv_applycollection)
        TextView tvItemSearchjoblvApplycollection;
        @Bind(R.id.rl_item_searchjoblv_click)
        RelativeLayout rlItemSearchjoblvClick;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    /**
     * 多选分页
     *
     * @return
     */
    public  HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public  void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 投递职位
     */
    public void deliverJob() {
        if (getJobIds() != null) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "job.apply");
            requestParams.put("job_id", getJobIds());
            if (sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000").equals("000")) {
                Toast.makeText(mContext, Constants.APPLY_NO_RESUME, Toast.LENGTH_SHORT).show();
                return;
            } else {
                requestParams.put("resume_id", sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000"));
            }
            requestParams.put("resume_language", "zh");
            NetService service = new NetService(mContext, handlerSendResume);
            service.execute(requestParams);
        }
    }

    /**
     * 取消收藏
     */
    public void collectJob() {
        if (getCpllectJobIds() != null) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_stow.delefavourite");
            requestParams.put("job_id", getCpllectJobIds());
            requestParams.put("record_id", "");
            NetService service = new NetService(mContext, handlerCollectJob);
            service.execute(requestParams);
        }
    }

    private String getJobIds() {
        StringBuffer sbJobId = new StringBuffer();
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i) == true) {
                if ("0".equals(dataList.get(i).get("is_expire"))) {
                    sbJobId.append(dataList.get(i).get("job_id") + ",");
                } else {
                    Toast.makeText(mContext, "该职位已过期", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }
        if (sbJobId.toString().length() == 0 || sbJobId == null) {
            Toast.makeText(mContext, "请先选择职位", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            String jobIds = sbJobId.substring(0, sbJobId.length() - 1);
            return jobIds;
        }
    }

    private String getCpllectJobIds() {
        StringBuffer sbJobId = new StringBuffer();
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i) == true) {
                sbJobId.append(dataList.get(i).get("job_id") + ",");
            }
        }
        if (sbJobId.toString().length() == 0 || sbJobId == null) {
            Toast.makeText(mContext, "请先选择职位", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            String jobIds = sbJobId.substring(0, sbJobId.length() - 1);
            return jobIds;
        }
    }
}
