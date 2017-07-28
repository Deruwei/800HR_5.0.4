package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.model.BrowsedInfo;
import com.hr.ui.utils.netutils.AsyncPersonCenterBrowsed;
import com.hr.ui.utils.netutils.NetUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 谁看过我的简历
 *
 * @author 800hr:xuebaohua
 */
public class WhoLookMeActivity extends Activity implements OnClickListener, OnItemClickListener {

    private ListView listview;
    private ArrayList<BrowsedInfo> listBrowsedInfos;// 谁看过我的简历
    private MyBaseAdpter myBaseAdpter;
    private ImageView iv_lookme_back;
    private boolean isLoadAll = false;// 是否全部加载
    private int index = 1;// 页码索引
    private String page_nums = "";// 总记录数
    private Context mContext = WhoLookMeActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_look_me);
        MobclickAgent.onEvent(this,"cv-who-see-me");
        listview = (ListView) findViewById(R.id.listview_search);
        listBrowsedInfos = new ArrayList<BrowsedInfo>();
        myBaseAdpter = new MyBaseAdpter(mContext, listBrowsedInfos);
        listview.setOnItemClickListener(this);
        listview.setAdapter(myBaseAdpter);
        iv_lookme_back = (ImageView) findViewById(R.id.iv_lookme_back);
        iv_lookme_back.setOnClickListener(this);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        page_nums = "20";
        new AsyncPersonCenterBrowsed(mContext, listBrowsedInfos, "browsed_list", myBaseAdpter, listview).execute("user_stow.browsed", index++ + "", "20");
    }

    private class MyBaseAdpter extends BaseAdapter {
        ArrayList<BrowsedInfo> listBrowsedInfos;
        Context context;

        public MyBaseAdpter(Context context,
                            ArrayList<BrowsedInfo> listBrowsedInfos) {
            this.context = context;
            this.listBrowsedInfos = listBrowsedInfos;
        }

        @Override
        public int getCount() {
            return listBrowsedInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return listBrowsedInfos.get(position);
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
                        R.layout.wholookme_listview, null);

                convertView.setTag(myHolder);
                myHolder.company_name = (TextView) convertView
                        .findViewById(R.id.company_name);
                myHolder.browsed_time = (TextView) convertView
                        .findViewById(R.id.company_time);
                myHolder.job_place = (TextView) convertView
                        .findViewById(R.id.company_scale);

            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.company_name.setText(listBrowsedInfos.get(position)
                    .getEnterprise_name());
            myHolder.browsed_time.setText("浏览日期:"
                    + listBrowsedInfos.get(position).getBrowsed_time());
            myHolder.job_place.setText("公司规模:"
                    + listBrowsedInfos.get(position).getStuffmunber());
            return convertView;
        }

        private final class MyHolder {
            TextView company_name, browsed_time, job_place;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_lookme_back:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (!NetUtils.checkNet(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            return;
        }
        if (listBrowsedInfos.size() != 0) {
            open_detaile(listBrowsedInfos, arg2);
        }
    }

    /**
     * 初始化视图
     */
    private void open_detaile(ArrayList<BrowsedInfo> listBrowsedInfos, int item_Index) {
        Intent intent = new Intent(this, CompanyParticularActivity.class);
        intent.putExtra("Enterprise_id", listBrowsedInfos.get(item_Index).getEnterprise_id());
        startActivity(intent);
    }
}
