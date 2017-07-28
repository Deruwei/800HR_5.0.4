package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.model.PositionInfo;
import com.hr.ui.utils.netutils.AsyncPersonCenterApplied;
import com.hr.ui.utils.netutils.NetUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 我申请的职位---列表页
 */

public class MyPositionActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {
    private Context mContext = this;
    private ListView listview;
    private ArrayList<PositionInfo> listapplied;// 申请的职位
    private int index = 0;// 页码索引
    private PositionBaseAdpter positionBaseAdpter;
    public static boolean isLoadAll;// 是否全部加载
    public static boolean isLoading;
    private ImageView iv_my_position_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_position);
        MobclickAgent.onEvent(this, "cv-show-applied");
        listview = (ListView) findViewById(R.id.lv_my_position_listview);
        iv_my_position_back = (ImageView) findViewById(R.id.iv_my_position_back);
        iv_my_position_back.setOnClickListener(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        isLoading = false;
        isLoadAll = false;
        listapplied = new ArrayList<PositionInfo>();
        positionBaseAdpter = new PositionBaseAdpter(this, listapplied);
        listview.setAdapter(positionBaseAdpter);
        listview.setOnScrollListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NetUtils.checkNet(mContext)) {
                    Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listapplied.size() != 0) {
                    if (listapplied.get(position).getIs_expire().equals("0")) {
                        goCompanyParticular(listapplied, position);
                    } else {
                        Toast.makeText(mContext, "该职位已过期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }


    /**
     * 适配器
     *
     * @author 800hr:xuebaohua
     */
    private class PositionBaseAdpter extends BaseAdapter {
        ArrayList<PositionInfo> listapplied;
        Context context;

        public PositionBaseAdpter(Context context,
                                  ArrayList<PositionInfo> listapplied) {
            this.context = context;
            this.listapplied = listapplied;
        }

        @Override
        public int getCount() {
            return listapplied == null ? 0 : listapplied.size();
        }

        @Override
        public Object getItem(int position) {
            return listapplied.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder = null;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_my_position_lv, null);

                convertView.setTag(myHolder);
                myHolder.job_name = (TextView) convertView.findViewById(R.id.tv_item_my_position_jobname);
                myHolder.company_name = (TextView) convertView.findViewById(R.id.tv_item_my_position_companyname);
                myHolder.applied_count = (TextView) convertView.findViewById(R.id.tv_item_my_position_cityname);
                myHolder.applied_time = (TextView) convertView.findViewById(R.id.tv_item_my_positionlv_releasetime);
                myHolder.cb_item_my_position_select = (CheckBox) convertView.findViewById(R.id.cb_item_my_position_select);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            if (listapplied.size() != 0) {
                myHolder.cb_item_my_position_select.setVisibility(View.GONE);
                myHolder.company_name.setText(listapplied.get(position).getEnterprise_name());
                myHolder.job_name.setText(listapplied.get(position).getJob_name());
                myHolder.applied_count.setText("已有" + listapplied.get(position).getApplied_num() + "人申请");
                myHolder.applied_time.setText(listapplied.get(position).getApplied_time());

            }
            return convertView;
        }

        private final class MyHolder {
            TextView company_name, job_name, applied_count, applied_time;
            CheckBox cb_item_my_position_select;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_my_position_back:
                finish();
                break;
            default:
                break;
        }

    }


    /**
     * 跳转到职位详情页
     */
    private void goCompanyParticular(ArrayList<PositionInfo> positionInfo, int item_Index) {

        Intent intent = new Intent(mContext, PostParticularsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("job_id", positionInfo.get(item_Index).getJob_id());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int totalItem = firstVisibleItem + visibleItemCount;
        if (totalItem == listapplied.size() && !isLoadAll) {
            if (isLoading) {
                return;
            }
            new AsyncPersonCenterApplied(mContext, listapplied,
                    "applied_list", positionBaseAdpter, listview)
                    .execute("user_stow.applied", index++ + "", "20");
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}

