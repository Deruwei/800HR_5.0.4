package com.hr.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.MyCollectionAdapter;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCollectionActivity extends BaseActivity {
    private static final String TAG = "MyCollectionActivity";
    @Bind(R.id.iv_my_collection_back)
    ImageView ivMyCollectionBack;
    @Bind(R.id.tv_my_collection_jobnum)
    TextView tvMyCollectionJobnum;
    @Bind(R.id.rl_my_collection_top)
    RelativeLayout rlMyCollectionTop;
    @Bind(R.id.lv_my_collection_result)
    RecyclerView lvMyCollectionResult;
    @Bind(R.id.sr_collection)
    SwipeRefreshLayout srCollection;
    @Bind(R.id.bt_my_collection_collect)
    Button btMyCollectionCollect;
    @Bind(R.id.bt_my_collection_deliver)
    Button btMyCollectionDeliver;
    @Bind(R.id.rl_my_collection_visible)
    LinearLayout rlMyCollectionVisible;
    @Bind(R.id.tv_noDataCollection)
    TextView tvNoDataCollection;
    @Bind(R.id.rl_hasDataCollection)
    RelativeLayout rlHasDataCollection;
    private MyCollectionAdapter myCollectionAdapter;
    private ArrayList<HashMap<String, Object>> dataList;
    private boolean fisrt;
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    private HashMap<Integer, Boolean> totalIsSelect;
    private LinearLayoutManager manager;
    /**
     * 当前第几页
     */
    private int pageNum = 1;
    /**
     * 搜索到的工作总数
     */

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
    private int lastVisibleItem;
    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                srCollection.setRefreshing(false);
                json_result = (String) msg.obj;
                try {
                    // 1001 成功 1002失败
                    Message msg0 = new Message();
                    msg0.what = 1001;
                    msg0.arg1 = searchResult_json();// 状态码
                   // Log.d("msg0.arg1", msg0.arg1 + "");
                    myhandler.sendMessage(msg0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg1 = new Message();
                    msg1.what = 1002;
                    myhandler.sendMessage(msg1);
                }
            } else {
                srCollection.setRefreshing(false);
                Message msg1 = new Message();
                msg1.what = 1002;
                myhandler.sendMessage(msg1);
            }
        }
    };
    // 更新UI
    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                if (msg.arg1 == 0) {// 成功获取数据
                    // 通知适配器更新数据
                    myCollectionAdapter.setDataList(totalList);
                    if(pageNum==1) {
                        myCollectionAdapter.initData();
                        lvMyCollectionResult.setAdapter(myCollectionAdapter);
                    }else{
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        ButterKnife.bind(this);
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
            rlMyCollectionVisible.setVisibility(View.VISIBLE);
            rlHasDataCollection.setVisibility(View.VISIBLE);
            tvNoDataCollection.setVisibility(View.GONE);
        } else {
            rlMyCollectionVisible.setVisibility(View.GONE);
            rlHasDataCollection.setVisibility(View.GONE);
            tvNoDataCollection.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvMyCollectionResult.setLayoutManager(manager);
        lvMyCollectionResult.addItemDecoration(new SpacesItemDecoration(5));
        myCollectionAdapter = new MyCollectionAdapter(MyCollectionActivity.this);
        srCollection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        totalList.clear();
                        totalIsSelect.clear();
                        pageNum = 1;
                        loadNetData();
                        myCollectionAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        //rvChannalcontent根据上拉到页面的最低端来加载下一页
        lvMyCollectionResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动到页面的最底端的时候最后一个item的下标+1等于adapter数据的个数，加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == myCollectionAdapter
                        .getItemCount()) {
                    pageNum++;
                    srCollection.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srCollection.setRefreshing(true);
                            loadNetData();
                            myCollectionAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
        srCollection.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srCollection.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srCollection.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
                        .getDisplayMetrics()));

        /*lvMyCollectionResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 1 || newState == 2) {
                    rlMyCollectionVisible.setVisibility(View.INVISIBLE);
                } else {
                    rlMyCollectionVisible.setVisibility(View.VISIBLE);
                }
            }
        });*/
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
        if(fisrt==false){
            srCollection.setRefreshing(true);
        }
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

    @OnClick({R.id.iv_my_collection_back, R.id.bt_my_collection_collect, R.id.bt_my_collection_deliver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
