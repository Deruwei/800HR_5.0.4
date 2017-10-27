package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.netutils.NetService;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 就业指导
 */
public class VocationalCounselActivity extends Activity implements OnClickListener,
        OnItemClickListener {
    ListView lv_vocationalcounse;
    TextView tv_vocationalcounse_title;
    View footer;

    private ImageView iv_vocationalcounse_back;
    private MyBaseAdpter myBaseAdpter;
    private ArrayList<HashMap<String, String>> listdata;
    private String id;// 栏目id
    public static int index;// 记录最后一次所看的条目索引
    public static int pageIndex = 1;// 请求页码
    public static ArrayList<String> idList;// 存放id集合传递给NewsInfoActivity
    public static MyOnScrollListener myOnScrollListener;// 滑动监听
    public static String title = "";// 栏目内容
    // 是否加载完成
    private Boolean finish = true;

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
        setContentView(R.layout.activity_vocational_counsel);
        int indexSelected = getIntent().getIntExtra("indexSelected",0);
        id = getIntent().getStringExtra("id");

        initView();
        initData(indexSelected);
    }

    /**
     * 初始化请求参数
     *
     * @param id
     * @param page
     */
    private HashMap<String, String> initRequestparams(String id, int page) {
        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", "news.gettitle");
        requestParams.put("column_id", id);
        requestParams.put("page", page + "");
        requestParams.put("page_nums", "20");
        return requestParams;
    }

    /**
     * 实例化对象，适配器，title初始化
     *
     * @param indexSelected
     */
    private void initData(int indexSelected) {
        footer = LayoutInflater.from(VocationalCounselActivity.this).inflate(
                R.layout.listview_footerview, null);
        footer.setOnClickListener(this);
        set_Titlt_Text(indexSelected);
        listdata = new ArrayList<HashMap<String, String>>();
        idList = new ArrayList<String>();
        lv_vocationalcounse.addFooterView(footer);
        myBaseAdpter = new MyBaseAdpter(this, listdata);
        lv_vocationalcounse.setAdapter(myBaseAdpter);
        lv_vocationalcounse.removeFooterView(footer);
        myOnScrollListener = new MyOnScrollListener();
        lv_vocationalcounse.setOnScrollListener(myOnScrollListener);

    }

    private void initView() {
        tv_vocationalcounse_title = (TextView) findViewById(R.id.tv_vocationalcounse_title);
        iv_vocationalcounse_back = (ImageView) findViewById(R.id.iv_vocationalcounse_back);
        lv_vocationalcounse = (ListView) findViewById(R.id.lv_vocationalcounse);
        lv_vocationalcounse.setOnItemClickListener(this);
        iv_vocationalcounse_back.setOnClickListener(this);

    }

    /**
     * 新闻标题滚动监听
     *
     * @author 800hr:xuebaohua
     */
    public final class MyOnScrollListener implements OnScrollListener {
        protected static final int GET_DATA_SUCCESS = 0;
        protected static final int GET_DATA_FALSE = 1;
        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == GET_DATA_SUCCESS) {
                    if (msg.arg1 == 0) {// 成功获取数据
                        // 通知适配器更新数据
                        myBaseAdpter.notifyDataSetChanged();
                        ++pageIndex;
                        finish = true;
                        lv_vocationalcounse.removeFooterView(footer);
                    } else if (msg.arg1 == 206) {// 全部加载
                        ((TextView) (footer
                                .findViewById(R.id.listview_footer_textview)))
                                .setText(getString(R.string.listview_load_over));
                        ((View) (footer
                                .findViewById(R.id.listview_footer_progressBar1)))
                                .setVisibility(View.GONE);
                    } else {// 获取数据失败
                        Toast.makeText(
                                VocationalCounselActivity.this,
                                getString(Rc4Md5Utils.getErrorResourceId(msg.arg1)),
                                Toast.LENGTH_SHORT).show();
                        ((TextView) (footer
                                .findViewById(R.id.listview_footer_textview)))
                                .setText(getString(R.string.listview_load_false));
                        ((View) (footer
                                .findViewById(R.id.listview_footer_progressBar1)))
                                .setVisibility(View.GONE);
                    }
                } else if (msg.what == GET_DATA_FALSE) {// 无响应或抛异常
                    ((TextView) (footer
                            .findViewById(R.id.listview_footer_textview)))
                            .setText(getString(R.string.listview_load_false));
                    ((View) (footer
                            .findViewById(R.id.listview_footer_progressBar1)))
                            .setVisibility(View.GONE);
                }

            }

            ;
        };

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // 计算当前加载上来的最后item数目
            int totalItem = firstVisibleItem + visibleItemCount;
            // 当前加载过的item总数目等于总显示过的Item数目的时候可以去加载
            if (totalItem == lv_vocationalcounse.getCount() && finish) {
                startRunningGetData();
            }

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }

        private Handler handlerService = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    try {

                        String jsonString = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int error_code = jsonObject.getInt("error_code");
                        if (error_code != 0) {
                            Message msg0 = new Message();
                            msg0.what = GET_DATA_SUCCESS;
                            msg0.arg1 = error_code;// 状态码
                            mHandler.sendMessage(msg0);
                            return;
                        }
                        JSONArray jsonArray = jsonObject
                                .getJSONArray("title_list");
                        Log.d("jsonArray数据", jsonArray.toString());
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonItem = jsonArray.getJSONObject(i);
                            idList.add(jsonItem.get("id").toString());
                            Iterator<String> keys = jsonItem.keys();
                            HashMap<String, String> item = new HashMap<String, String>();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                item.put(key, jsonItem.getString(key));
                            }
                            listdata.add(item);
                        }
                        Message msg2 = new Message();
                        msg2.what = GET_DATA_SUCCESS;
                        msg2.arg1 = 0;// 状态码
                        mHandler.sendMessage(msg2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Message msg1 = new Message();
                    msg1.what = GET_DATA_FALSE;
                    mHandler.sendMessage(msg1);
                }
            }

            ;
        };

        /**
         * 启动网络访问线程
         */
        public void startRunningGetData() {
            finish = false;
            if (lv_vocationalcounse.getFooterViewsCount() == 0) {
                lv_vocationalcounse.addFooterView(footer);
            }
            ((View) (footer.findViewById(R.id.listview_footer_progressBar1)))
                    .setVisibility(View.VISIBLE);
            // 开始加载数据
            NetService service = new NetService(VocationalCounselActivity.this, handlerService);
            service.execute(initRequestparams(id, pageIndex));
        }

    }

    class MyBaseAdpter extends BaseAdapter {
        ArrayList<HashMap<String, String>> adpterData;
        Context context;

        public MyBaseAdpter(Context context,
                            ArrayList<HashMap<String, String>> adpterData) {
            this.context = context;
            this.adpterData = adpterData;
        }

        @Override
        public int getCount() {
            return adpterData.size();
        }

        @Override
        public Object getItem(int position) {
            return adpterData.get(position);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_keyword_lv, null);
                myHolder.job_name = (TextView) convertView.findViewById(R.id.industry_rec_title);
                myHolder.line = (View) convertView.findViewById(R.id.view_rec_title);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.job_name.setText(adpterData.get(position).get("title"));
            myHolder.line.setVisibility(View.INVISIBLE);
            return convertView;
        }

        private final class MyHolder {
            TextView job_name;
            View line;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listview_footerview:
                if (finish) {
                    myOnScrollListener.startRunningGetData();
                }
                break;
            case R.id.iv_vocationalcounse_back:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 设置标题显示
     *
     * @param index
     */
    private void set_Titlt_Text(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                tv_vocationalcounse_title.setText("求职宝典");
                title = "求职宝典";
                break;
            case 1:
                tv_vocationalcounse_title.setText("简历指南");
                title = "简历指南";
                break;
            case 2:
                tv_vocationalcounse_title.setText("面试秘籍");
                title = "面试秘籍";
                break;
            case 3:
                tv_vocationalcounse_title.setText("薪酬行情");
                title = "薪酬行情";
                break;
            case 4:
                tv_vocationalcounse_title.setText("职业诊断");
                title = "职业诊断";
                break;
            case 5:
                tv_vocationalcounse_title.setText("职场八卦");
                title = "职场八卦";
                break;
            case 6:
                tv_vocationalcounse_title.setText("人际关系");
                title = "人际关系";
                break;
            case 7:
                tv_vocationalcounse_title.setText("创业指南");
                title = "创业指南";
                break;
            case 8:
                tv_vocationalcounse_title.setText("劳动法范");
                title = "劳动法苑";
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        index = arg2;
        Intent intent = new Intent(VocationalCounselActivity.this, ApplyValuableBookActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        if (idList != null) {
            idList.clear();
            idList = null;
        }
        if (listdata != null) {
            listdata.clear();
            listdata = null;
        }
        super.onDestroy();
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
