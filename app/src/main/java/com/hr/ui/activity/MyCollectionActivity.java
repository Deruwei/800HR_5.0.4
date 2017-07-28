package com.hr.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.MyCollectionAdapter;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;
import com.hr.ui.view.pulltorefresh.PullToRefreshBase;
import com.hr.ui.view.pulltorefresh.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyCollectionActivity";
    private PullToRefreshListView lv_my_collection_result;
    private MyCollectionAdapter myCollectionAdapter;
    private ArrayList<HashMap<String, Object>> dataList;
    private int firstNumm = 0;
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    private HashMap<Integer, Boolean> totalIsSelect;
    /**
     * 当前第几页
     */
    private int pageNum = 1;
    /**
     * 搜索到的工作总数
     */
    private LinearLayout rl_my_collection_visible;
    private ImageView iv_my_collection_back;
    private Button bt_my_collection_collect, bt_my_collection_deliver;
    public static MyCollectionActivity instance = null;
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
                        myCollectionAdapter = new MyCollectionAdapter(MyCollectionActivity.this, totalList);
                        resultListView.setAdapter(myCollectionAdapter);
                        firstNumm = 2;
                    } else if (dataList != null && firstNumm == 2 && dataList.size() > 0) {
                        myCollectionAdapter.notifyDataSetChanged();
                    }
                } else if (msg.arg1 == 206) {//
                    Toast.makeText(MyCollectionActivity.this, "执行失败",
                            Toast.LENGTH_SHORT).show();
                    try {
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (msg.arg1 == 11) {
                    Toast.makeText(MyCollectionActivity.this,
                            getString(R.string.error_notnet),
                            Toast.LENGTH_SHORT).show();
                } else {// 获取数据失败
                }
            } else if (msg.what == 1002) {// 无响应或抛异常
            } else if (msg.what == 1003) {
                Toast.makeText(MyCollectionActivity.this, "连接服务器超时", Toast.LENGTH_SHORT)
                        .show();
            }
            isVISIBLE();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        MobclickAgent.onEvent(this, "cv-show-favorites");
        initView();
        initData();
        instance = MyCollectionActivity.this;
    }

    /**
     * 是否隐藏收藏投递
     */
    private void isVISIBLE() {
        if (totalList.size() > 0) {
            rl_my_collection_visible.setVisibility(View.VISIBLE);
        } else {
            rl_my_collection_visible.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_my_collection_result = (PullToRefreshListView) findViewById(R.id.lv_my_collection_result);
        rl_my_collection_visible = (LinearLayout) findViewById(R.id.rl_my_collection_visible);
        iv_my_collection_back = (ImageView) findViewById(R.id.iv_my_collection_back);
        iv_my_collection_back.setOnClickListener(this);
        bt_my_collection_collect = (Button) findViewById(R.id.bt_my_collection_collect);
        bt_my_collection_deliver = (Button) findViewById(R.id.bt_my_collection_deliver);

        bt_my_collection_collect.setOnClickListener(this);
        bt_my_collection_deliver.setOnClickListener(this);

        resultListView = lv_my_collection_result.getRefreshableView();
        // 设置PullToRefreshListView的事件监听
        // 刷新监听器，目的是实现下拉刷新
        lv_my_collection_result
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        totalList.clear();
                        totalIsSelect.clear();
                        pageNum = 1;
                        loadNetData();
                        // 让下拉刷新的动画Ui消失
                        lv_my_collection_result.onRefreshComplete();
                    }
                });
        // 监听当前页面的最后一条，目的是触发上拉加载下一页
        lv_my_collection_result
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        pageNum += 1;
                        loadNetData();
                    }
                });
        lv_my_collection_result.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 1 || scrollState == 2) {
                    rl_my_collection_visible.setVisibility(View.INVISIBLE);
                } else {
                    rl_my_collection_visible.setVisibility(View.VISIBLE);
                }
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
        totalIsSelect = new HashMap<>();
        loadNetData();
    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    private HashMap<String, String> getData(int pageIndex) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_stow.favourite");
        params.put("page", pageIndex + "");
        params.put("page_nums", "20");
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
            LogTools.i(TAG, "====jsonObjectCollection" + jsonObject.toString());
            error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("favourite_list");
            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOb = (JSONObject) jsonArray.get(i);
                hs = new HashMap<String, Object>();
                hs.put("job_id", jsonOb.getString("job_id"));
                hs.put("job_name", jsonOb.getString("job_name"));
                hs.put("enterprise_name", jsonOb.getString("enterprise_name"));
                hs.put("applied_num", jsonOb.getString("applied_num"));
                hs.put("favourite_time", jsonOb.getString("favourite_time"));
                hs.put("workplace", jsonOb.getString("workplace"));
                hs.put("is_expire", jsonOb.getString("is_expire"));
                hs.put("is_apply", jsonOb.getString("is_apply"));
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
            case R.id.iv_my_collection_back:
                finish();
                break;
            case R.id.bt_my_collection_collect:
                myCollectionAdapter.collectJob();
                break;
            case R.id.bt_my_collection_deliver:
                myCollectionAdapter.deliverJob();
                break;
        }
    }
}
