package com.hr.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hr.ui.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 抢工作设置-薪资待遇
 */
public class SubSalaryActivity extends Activity implements
        OnClickListener, OnItemClickListener {

    private ListView job_list;
    private ArrayList<HashMap<String, String>> array_job;
    private HashMap<String, String> hp;
    private String str[];
    private String strIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_job_screen);
        init();
        str = new String[]{"不限", "3千以下/月", "3千～6千/月", "6千～1万/月", "1万～3万/月",
                "3万～5万/月", "5万以上/月"};
        strIds = new String[]{"0", "13", "14", "16", "18", "21", "22"};
        for (int i = 0; i < str.length; i++) {
            hp = new HashMap<String, String>();
            hp.put("job", str[i]);
            array_job.add(hp);

        }

        SimpleAdapter sim = new SimpleAdapter(SubSalaryActivity.this,
                array_job, R.layout.search_job_listview,
                new String[]{"job"}, new int[]{R.id.mytext});
        job_list.setAdapter(sim);

    }

    public void init() {
        TextView Text_nowPage = (TextView) findViewById(R.id.Text_nowPage);
        Text_nowPage.setText("请选择薪资待遇");
        array_job = new ArrayList<HashMap<String, String>>();
        findViewById(R.id.iv_subscription_select_back).setOnClickListener(this);
        job_list = (ListView) findViewById(R.id.job_list);
        job_list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_subscription_select_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        SubscriptionActivity.subscriptionActivity.dataHashMap.put("salary", strIds[arg2]);
        SubscriptionActivity.subscriptionActivity.initData();
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (job_list != null) {
            job_list.setAdapter(null);
        }
        if (hp != null) {
            hp.clear();
            hp = null;
        }
        if (array_job != null) {
            array_job.clear();
            array_job = null;
        }
        str = null;
        strIds = null;

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
