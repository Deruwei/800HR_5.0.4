package com.hr.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hr.ui.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 发布时间
 */
public class SubTimeActivity extends Activity implements OnClickListener, OnItemClickListener {

    private ListView job_list;
    private ArrayList<HashMap<String, String>> array_job;
    private HashMap<String, String> hp;
    private String str[];
    private String strIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }
        setContentView(R.layout.search_job_screen);
        init();

       // private String timeStr[] = {"不限", "近一天","近两天","近三天", "近一周","近两周" ,"近一个月", "近六周","近两月"};
        //private String timeStrIds[] = {"0", "1", "2", "3", "7", "14","30","42","60"};
        str = new String[]{"不限", "近一天","近两天","近三天", "近一周","近两周" ,"近一个月", "近六周","近两月"};
        strIds = new String[]{"0", "1", "2", "3", "7", "14","30","42","60"};
        for (int i = 0; i < str.length; i++) {
            hp = new HashMap<String, String>();
            hp.put("job", str[i]);
            array_job.add(hp);

        }

        SimpleAdapter sim = new SimpleAdapter(SubTimeActivity.this, array_job, R.layout.search_job_listview,
                new String[]{"job"}, new int[]{R.id.mytext});
        job_list.setAdapter(sim);

    }

    public void init() {
        TextView Text_nowPage = (TextView) findViewById(R.id.Text_nowPage);
        Text_nowPage.setText("请选择发布时间");
        array_job = new ArrayList<HashMap<String, String>>();
        job_list = (ListView) findViewById(R.id.job_list);
        job_list.setOnItemClickListener(this);
        findViewById(R.id.iv_subscription_select_back).setOnClickListener(this);
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

        SubscriptionActivity.subscriptionActivity.dataHashMap.put("issuedate", strIds[arg2]);
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
        if (array_job != null) {
            array_job.clear();
            array_job = null;
        }
        if (hp != null) {
            hp.clear();
            hp = null;
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
