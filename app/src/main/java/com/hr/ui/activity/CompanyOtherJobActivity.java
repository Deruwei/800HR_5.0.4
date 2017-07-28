package com.hr.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CompanyOtherJobAdapter;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.pulltorefresh.PullToRefreshBase;
import com.hr.ui.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyOtherJobActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CompanyOtherJobAdapter";
    private PullToRefreshListView lv_company_otherjob_listview;
    private CompanyOtherJobAdapter comAdapter;
    private ArrayList<HashMap<String, Object>> dataList;
    private int firstNumm = 0;
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    /**
     * 当前第几页
     */
    private int pageNum = 1;
    /**
     * 返回按钮
     */
    private ImageView iv_company_otherjob_back;
    /**
     * 企业id(加密)
     */
    private String enterprise_id;
    /**
     * ResultListView
     */
    private ListView resultListView;
    /**
     * 网络获取的json数据集合
     */
    private String json_result;
    /**
     * 异常返回值
     */
    private int error_code;
    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;
                try {
                    // 1001 成功 1002失败
                    Message msg0 = new Message();
                    msg0.what = 1001;
                    msg0.arg1 = searchResult_json();// 状态码
                    Log.d("msg0.arg1", msg0.arg1 + "");
                    myhandler.sendMessage(msg0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg1 = new Message();
                    msg1.what = 1002;
                    myhandler.sendMessage(msg1);
                }
            } else {
                Message msg1 = new Message();
                msg1.what = 1002;
                myhandler.sendMessage(msg1);
            }
        }

        ;
    };
    // 更新UI
    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                if (msg.arg1 == 0) {// 成功获取数据
                    // 通知适配器更新数据
                    if (firstNumm == 0) {
                        comAdapter = new CompanyOtherJobAdapter(CompanyOtherJobActivity.this, totalList);
                        resultListView.setAdapter(comAdapter);
                        firstNumm = 2;
                    } else if (dataList != null && firstNumm == 2 && dataList.size() > 0) {
                        comAdapter.notifyDataSetChanged();
                    }
                } else if (msg.arg1 == 206) {//
                    Toast.makeText(CompanyOtherJobActivity.this, "执行失败",
                            Toast.LENGTH_SHORT).show();
                    try {
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (msg.arg1 == 11) {
                    Toast.makeText(CompanyOtherJobActivity.this,
                            getString(R.string.error_notnet),
                            Toast.LENGTH_SHORT).show();
                } else {// 获取数据失败
                }
            } else if (msg.what == 1002) {// 无响应或抛异常
            } else if (msg.what == 1003) {
                Toast.makeText(CompanyOtherJobActivity.this, "连接服务器超时", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_other_job);
        getCompanyData();
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_company_otherjob_listview = (PullToRefreshListView) findViewById(R.id.lv_company_otherjob_listview);
        iv_company_otherjob_back = (ImageView) findViewById(R.id.iv_company_otherjob_back);
        iv_company_otherjob_back.setOnClickListener(this);

        resultListView = lv_company_otherjob_listview.getRefreshableView();
        // 设置PullToRefreshListView的事件监听
        // 刷新监听器，目的是实现下拉刷新
        lv_company_otherjob_listview
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        totalList.clear();
                        pageNum = 1;
                        loadNetData();
                        // 让下拉刷新的动画Ui消失
                        lv_company_otherjob_listview.onRefreshComplete();
                    }
                });
        // 监听当前页面的最后一条，目的是触发上拉加载下一页
        lv_company_otherjob_listview
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        pageNum += 1;
                        loadNetData();
                    }
                });
        lv_company_otherjob_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        totalList = new ArrayList<>();
        loadNetData();
    }


    private void getCompanyData() {
        enterprise_id = getIntent().getStringExtra("enterprise_id");
    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    private HashMap<String, String> getData(int pageIndex) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "job.alljobs");
        params.put("page_nums", "100");
        params.put("page", pageNum + "");
        params.put("enterprise_id", enterprise_id);
        return params;
    }

    /**
     * 加载数据
     */
    public void loadNetData() {
        NetService service = new NetService(this, handlerService);
        service.execute(getData(pageNum));
    }

    /**
     * 数据解析
     *
     * @return
     */
    public int searchResult_json() {
        // TODO Auto-generated method stub
        try {
            dataList = new ArrayList<HashMap<String, Object>>();
            JSONObject jsonObject = new JSONObject(json_result);
            error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("jobs_list");
//            Log.i("==============is_apply",jsonArray.toString());
            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOb = (JSONObject) jsonArray.get(i);

                hs = new HashMap<String, Object>();
                hs.put("job_name", jsonOb.getString("job_name"));
                hs.put("issue_date", jsonOb.getString("issue_date"));
                hs.put("workplace", jsonOb.getString("workplace"));
                hs.put("number", jsonOb.getString("number"));
                hs.put("poster_state", jsonOb.getString("poster_state"));
                hs.put("recruitment_date", jsonOb.getString("release_date"));
                hs.put("job_id", jsonOb.getString("job_id"));
                hs.put("enterprise_id", jsonOb.getString("enterprise_id"));
                hs.put("enterprise_name", jsonOb.getString("enterprise_name"));
//                hs.put("is_apply", jsonOb.getString("is_apply"));
//                hs.put("is_favourite", jsonOb.getString("is_favourite"));
                dataList.add(hs);
            }
            totalList.addAll(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error_code;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_company_otherjob_back:
                finish();
                break;
        }
    }
}
