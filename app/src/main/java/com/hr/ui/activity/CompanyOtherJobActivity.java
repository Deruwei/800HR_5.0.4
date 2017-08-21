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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CompanyOtherJobAdapter;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.pulltorefresh.PullToRefreshBase;
import com.hr.ui.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyOtherJobActivity extends BaseActivity {
    private static final String TAG = "CompanyOtherJobAdapter";
    @Bind(R.id.iv_company_otherjob_back)
    ImageView ivCompanyOtherjobBack;
    @Bind(R.id.tv_company_otherjob_jobnum)
    TextView tvCompanyOtherjobJobnum;
    @Bind(R.id.rl_company_otherjob_top)
    RelativeLayout rlCompanyOtherjobTop;
    @Bind(R.id.lv_company_otherjob_listview)
    RecyclerView lvCompanyOtherjobListview;
    @Bind(R.id.sr_companyother)
    SwipeRefreshLayout srCompanyother;
    private CompanyOtherJobAdapter comAdapter;
    private ArrayList<HashMap<String, Object>> dataList;
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
     * 企业id(加密)
     */
    private String enterprise_id;
    /**
     * ResultListView
     */
    private boolean fisrt;
    /**
     * 网络获取的json数据集合
     */
    private String json_result;
    /**
     * 异常返回值
     */
    private int error_code;
    private int lastVisibleItem;
    private LinearLayoutManager manager;
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
                    srCompanyother.setRefreshing(false);
                    if (dataList.size() != 0&&dataList!=null) {
                        totalList.addAll(dataList);
                        comAdapter.setDataList(totalList);
                        if(pageNum==1) {
                            lvCompanyOtherjobListview.setAdapter(comAdapter);
                        }else{
                            comAdapter.notifyDataSetChanged();
                        }
                    } else{
                        Toast.makeText(CompanyOtherJobActivity.this, "没有更多的数据了！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.arg1 == 206) {//
                    srCompanyother.setRefreshing(false);
                    Toast.makeText(CompanyOtherJobActivity.this, "执行失败",
                            Toast.LENGTH_SHORT).show();
                    try {
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (msg.arg1 == 11) {
                    srCompanyother.setRefreshing(false);
                    Toast.makeText(CompanyOtherJobActivity.this,
                            getString(R.string.error_notnet),
                            Toast.LENGTH_SHORT).show();
                } else {// 获取数据失败
                    srCompanyother.setRefreshing(false);
                }
            } else if (msg.what == 1002) {// 无响应或抛异常
                srCompanyother.setRefreshing(false);
            } else if (msg.what == 1003) {
                srCompanyother.setRefreshing(false);
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
        ButterKnife.bind(this);
        fisrt=false;
        getCompanyData();
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvCompanyOtherjobListview.setLayoutManager(manager);
        lvCompanyOtherjobListview.addItemDecoration(new SpacesItemDecoration(5));
        srCompanyother.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        totalList.clear();
                        pageNum = 1;
                        loadNetData();
                        comAdapter.notifyDataSetChanged();
                    }
                },1000);
            }
        });
        //rvChannalcontent根据上拉到页面的最低端来加载下一页
        lvCompanyOtherjobListview.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动到页面的最底端的时候最后一个item的下标+1等于adapter数据的个数，加载下一页
                if(newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) ==comAdapter
                        .getItemCount()){
                    srCompanyother.setRefreshing(true);
                    pageNum++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNetData();
                            comAdapter.notifyDataSetChanged();
                        }
                    },1000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
        srCompanyother.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srCompanyother.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srCompanyother.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
                        .getDisplayMetrics()));


    }

    /**
     * 初始化数据
     */
    private void initData() {
        comAdapter = new CompanyOtherJobAdapter(CompanyOtherJobActivity.this);
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
        params.put("page_nums", "20");
        params.put("page", pageNum + "");
        params.put("enterprise_id", enterprise_id);
        return params;
    }

    /**
     * 加载数据
     */
    public void loadNetData() {
        if(fisrt==false) {
            fisrt=true;
            srCompanyother.setRefreshing(true);
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


    @OnClick(R.id.iv_company_otherjob_back)
    public void onViewClicked() {
        finish();
    }
}
