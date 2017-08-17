package com.hr.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SearchJobResultAdapter;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.Async_SetRobJob;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;
import com.hr.ui.utils.tools.PushAliasString;
import com.hr.ui.view.custom.MyProgressDialog;
import com.hr.ui.view.custom.PopupmenuBar;
import com.hr.ui.view.pulltorefresh.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchJobResultActivity extends Activity  {
    private static final String TAG = "SearchJobResultActivity";
    @Bind(R.id.iv_selectcity_back)
    ImageView ivSelectcityBack;
    @Bind(R.id.tv_searchjobresult_jobnum)
    TextView tvSearchjobresultJobnum;
    @Bind(R.id.tv_searchjobresult_filtration)
    TextView tvSearchjobresultFiltration;
    @Bind(R.id.rl_searchjobresult_top)
    RelativeLayout rlSearchjobresultTop;
    @Bind(R.id.tv_searchjobresult_condition)
    TextView tvSearchjobresultCondition;
    @Bind(R.id.tv_searchjob_result_subscribe)
    TextView tvSearchjobResultSubscribe;
    @Bind(R.id.rl_searchjobresult_condition)
    RelativeLayout rlSearchjobresultCondition;
    @Bind(R.id.lv_searchjobresult_result)
    RecyclerView lvSearchjobresultResult;
    @Bind(R.id.sr_searchJobResult)
    SwipeRefreshLayout srSearchJobResult;
    @Bind(R.id.bt_searchjobresult_collect)
    Button btSearchjobresultCollect;
    @Bind(R.id.bt_searchjobresult_deliver)
    Button btSearchjobresultDeliver;
    @Bind(R.id.rl_searchjobresult_visible)
    LinearLayout rlSearchjobresultVisible;
    private String searchword;
    private String funcid;
    private String areaid;
    private String industry;
    private String areaName;
    private String funcName;
    private String wordtype;
    private SearchJobResultAdapter sjrAdapter;
    private ArrayList<HashMap<String, Object>> dataList;
    private int firstNumm = 0;

   /* private MyProgressDialog dialog;*/
    /**
     * popupwondow
     */
    private PopupmenuBar popupmenuBar;
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    private HashMap<Integer, Boolean> totalIsSelect;
    private HashMap<String, String> setVoicehashMap;
    /**
     * 当前第几页
     */
    private int pageNum = 1;
    /**
     * 搜索到的工作总数
     */
    private String jobNum;
    /**
     * ResultListView
     */
    /**
     * 网络获取的json数据集合
     */
    private String json_result;
    /**
     * 异常返回值
     */
    private int error_code;

    /**
     * 记录筛选条件
     */
    private HashMap<String, String> filterMap = new HashMap<>();
    /**
     * 筛选条件
     */
    private static HashMap<String, String> titledataHashMap = new HashMap<String, String>();
    public static HashMap<String, String> dataHashMap;
    private LinearLayoutManager manager;
    private int lastVisibleItem;
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
                    msg0.arg1 = searchResult_json();
                    // 状态码
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
                    if(dataList!=null&&dataList.size()!=0){
                    totalList.addAll(dataList);
                       /* Log.i("页码的总数",totalList.size()+"");*/
                    if (totalIsSelect.size() == 0) {
                        for (int i = 0; i < totalList.size(); i++) {
                            totalIsSelect.put(i, false);
                        }
                    } else {
                        for (int i = totalIsSelect.size() - 1; i < dataList.size(); i++) {
                            totalIsSelect.put(i, false);
                        }
                    }
                        sjrAdapter.setDataList(totalList);
                        // 通知适配器更新数据
                        if (pageNum == 1) {
                            lvSearchjobresultResult.setAdapter(sjrAdapter);
                        } else {
                            sjrAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(SearchJobResultActivity.this, "没有更多的数据！",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.arg1 == 206) {//
                    Toast.makeText(SearchJobResultActivity.this, "执行失败",
                            Toast.LENGTH_SHORT).show();
                    try {
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (msg.arg1 == 11) {
                    Toast.makeText(SearchJobResultActivity.this,
                            getString(R.string.error_notnet),
                            Toast.LENGTH_SHORT).show();
                } else {// 获取数据失败
                }
            } else if (msg.what == 1002) {// 无响应或抛异常
                Toast.makeText(SearchJobResultActivity.this, "服务器无响应", Toast.LENGTH_SHORT)
                        .show();
            } else if (msg.what == 1003) {
                Toast.makeText(SearchJobResultActivity.this, "连接服务器超时", Toast.LENGTH_SHORT)
                        .show();
            }
           /* if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }*/
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job_result);
        ButterKnife.bind(this);
        MobclickAgent.onEvent(this, "job-search");
        titledataHashMap.clear();
        initView();
        initData();
        setSearchInfo();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvSearchjobresultResult.setLayoutManager(manager);
        lvSearchjobresultResult.addItemDecoration(new SpacesItemDecoration(5));
        srSearchJobResult.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        totalList = new ArrayList<HashMap<String, Object>>();
                        pageNum = 1;
                        loadNetData();
                        srSearchJobResult.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //rvChannalcontent根据上拉到页面的最低端来加载下一页
        lvSearchjobresultResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动到页面的最底端的时候最后一个item的下标+1等于adapter数据的个数，加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == sjrAdapter
                        .getItemCount()) {
                    pageNum++;
                    srSearchJobResult.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNetData();
                            srSearchJobResult.setRefreshing(false);
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });
        srSearchJobResult.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srSearchJobResult.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srSearchJobResult.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
                        .getDisplayMetrics()));

       /* View view = LayoutInflater.from(SearchJobResultActivity.this).inflate(R.layout.foot_listview_white, null);
        resultListView.addFooterView(view);
        // 设置PullToRefreshListView的事件监听
        // 刷新监听器，目的是实现下拉刷新
        lv_searchjobresult_result.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                totalList.clear();
                totalIsSelect.clear();
                pageNum = 1;
                firstNumm = 0;
                loadNetData();
                // 让下拉刷新的动画Ui消失
                lv_searchjobresult_result.onRefreshComplete();
            }
        });
        // 监听当前页面的最后一条，目的是触发上拉加载下一页
        lv_searchjobresult_result
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        pageNum += 1;
                        loadNetData();
                    }
                });*/
//        lv_searchjobresult_result.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == 1 || scrollState == 2) {
////                    rl_searchjobresult_visible.setVisibility(View.INVISIBLE);
//                } else {
////                    rl_searchjobresult_visible.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sjrAdapter=new SearchJobResultAdapter(this);
        totalList = new ArrayList<>();
        totalIsSelect = new HashMap<>();
        getFindJobData();
        loadNetData();
        initPopupmenuBar();
    }

    /**
     * 获得从找工作页面传过来的值
     */
    private void getFindJobData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            searchword = bundle.getString("searchword");
            funcid = bundle.getString("funcid");
            areaid = bundle.getString("areaid");
            industry = bundle.getString("industry");
            funcName = bundle.getString("funcName");
            areaName = bundle.getString("areaName");
            wordtype = bundle.getString("wordtype");
        }
    }

    public void getFiltrate(HashMap<String, String> map, HashMap<String, String> map2) {
      /*  dialog = new MyProgressDialog(this);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }*/
        titledataHashMap.clear();
        totalList.clear();
        totalIsSelect.clear();
        filterMap.clear();
        if (sjrAdapter != null) {
            sjrAdapter.notifyDataSetChanged();
        }
        titledataHashMap.putAll(map);
        filterMap.putAll(map2);
        loadNetData();
        setSearchInfo();

    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    private HashMap<String, String> getData(int pageIndex) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "job.search");
        params.put("get_poster", "1");
        params.put("searchword", searchword == null ? "" : searchword);
        params.put("func", funcid == null ? "" : funcid);
        params.put("area", areaid == null ? "" : areaid);
        params.put("industry", industry == null ? "11" : industry);
        params.put("wordtype", wordtype);
        params.put("lingyu", "");
        params.put("filter_workyear",
                titledataHashMap.get("filter_workyear") == null ? "0"
                        : titledataHashMap.get("filter_workyear"));
        params.put("filter_worktype",
                titledataHashMap.get("filter_worktype") == null ? "0"
                        : titledataHashMap.get("filter_worktype"));
        params.put("filter_issuedate",
                titledataHashMap.get("filter_issuedate") == null ? "0"
                        : titledataHashMap.get("filter_issuedate"));
        params.put("filter_study",
                titledataHashMap.get("filter_study") == null ? "0"
                        : titledataHashMap.get("filter_study"));
        params.put("filter_stuffmunber",
                titledataHashMap.get("filter_stuffmunber") == null ? "0"
                        : titledataHashMap.get("filter_stuffmunber"));
        params.put("filter_salary", titledataHashMap.get("filter_salary") == null ? "0" : titledataHashMap.get("filter_salary"));
        params.put("page", pageIndex + "");
        params.put("page_nums", "20");
        params.put("nautica", "");
        params.put("range", "");
        return params;
    }

    private NetService service;

    /**
     * 加载数据
     */
    public void loadNetData() {
        service = new NetService(this, handlerService);
       // Log.i("页码",pageNum+"");
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
            LogTools.i(TAG, "====jsonObject" + jsonObject.toString());
            error_code = jsonObject.getInt("error_code");
            jobNum = jsonObject.getString("totals");
            refreshInfo();
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("jobs_list");
//            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOb = (JSONObject) jsonArray.get(i);
                hs = new HashMap<String, Object>();
                hs.put("job_name", jsonOb.getString("job_name"));
                hs.put("enterprise_name", jsonOb.getString("enterprise_name"));
                hs.put("issue_date", jsonOb.getString("issue_date"));
                hs.put("workplace", jsonOb.getString("workplace"));
                hs.put("posterimg", jsonOb.getString("posterimg"));
                hs.put("job_id", jsonOb.getString("job_id"));
                hs.put("enterprise_id", jsonOb.getString("enterprise_id"));
                hs.put("nautica", jsonOb.getString("nautica"));
                hs.put("study", jsonOb.getString("study"));
                hs.put("is_expire", jsonOb.getString("is_expire"));

                hs.put("is_apply", jsonOb.getString("is_apply"));//是否投递过，0没有，1有
                hs.put("is_favourite", jsonOb.getString("is_favourite"));//是否投递过，0没有，1有

                dataList.add(hs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error_code;
    }

    /**
     * 刷新信息
     */
    private void refreshInfo() {
        /**
         * 判断获取工作数量的最大值
         */
        if (jobNum != null) {
            int jobNumInt = Integer.parseInt(jobNum);
            if (jobNumInt > 999) {
                jobNum = "999";
                tvSearchjobresultJobnum.setText("为您找到" + jobNum + "+个职位");
            } else {
                tvSearchjobresultJobnum.setText("为您找到" + jobNum + "个职位");
            }
            Log.i("工作的数量",jobNum+"");
            if (jobNum.equals("0")) {
                srSearchJobResult.setVisibility(View.GONE);
                rlSearchjobresultVisible.setVisibility(View.GONE);
            } else {
                srSearchJobResult.setVisibility(View.VISIBLE);
                rlSearchjobresultVisible.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置找工作的值
     */
    private void setSearchInfo() {
        String word = "全部";
        StringBuffer showBuffer = new StringBuffer();
        // 关键字
        if (searchword != null && searchword.trim().length() > 0) {
            showBuffer.append(searchword);
        }
        if (funcid != null) {
            showBuffer.append("+" + funcName);
        }
        // 把地点ID转换为文字
        if (areaid != null && !areaid.equals("")) {
            showBuffer.append("+" + areaName);
        }
        // 找工作条件上设置筛选条件
        try {
            if (titledataHashMap.get("filter_workyear_value") != null
                    && !titledataHashMap.get("filter_workyear_value").equals(
                    "不限")) {
                showBuffer.append("+"
                        + titledataHashMap.get("filter_workyear_value"));
            }
            if (titledataHashMap.get("filter_worktype_value") != null
                    && !titledataHashMap.get("filter_worktype_value").equals(
                    "不限")) {
                showBuffer.append("+"
                        + titledataHashMap.get("filter_worktype_value"));
            }
            if (titledataHashMap.get("filter_issuedate_value") != null
                    && !titledataHashMap.get("filter_issuedate_value").equals(
                    "不限")) {
                showBuffer.append("+"
                        + titledataHashMap.get("filter_issuedate_value"));
            }
            if (titledataHashMap.get("filter_study_value") != null
                    && !titledataHashMap.get("filter_study_value").equals("不限")) {
                showBuffer.append("+"
                        + titledataHashMap.get("filter_study_value"));
            }
            if (titledataHashMap.get("filter_stuffmunber_value") != null
                    && !titledataHashMap.get("filter_stuffmunber_value").equals("不限")) {
                showBuffer.append("+" + titledataHashMap.get("filter_stuffmunber_value"));
            }
            if (titledataHashMap.get("filter_salary_value") != null
                    && !titledataHashMap.get("filter_salary_value").equals("不限")) {
                showBuffer.append("+" + titledataHashMap.get("filter_salary_value"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (showBuffer.toString().startsWith("+")) {
            showBuffer.delete(0, 1);
        }
        while (showBuffer.toString().endsWith("+")) {
            showBuffer.delete(showBuffer.length() - 1, showBuffer.length());
        }
        if (showBuffer.toString().equals("") || showBuffer == null || showBuffer.toString().equals("null")) {
            tvSearchjobresultCondition.setText(word);
            return;
        } else {
            // 设置找工作的值
            tvSearchjobresultCondition.setText(showBuffer.toString().replace("++", "+"));
            return;
        }
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopupmenuBar() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 实例化标题栏弹窗
        popupmenuBar = new PopupmenuBar(this, R.layout.item_popupwindow_filtrate,
                R.id.lv_popupwindow_first, R.id.lv_popupwindow_second, Color.BLACK, ViewGroup.LayoutParams.MATCH_PARENT,
//                wm.getDefaultDisplay().getHeight() / 2, this, filterMap);
                ViewGroup.LayoutParams.WRAP_CONTENT, this, filterMap);
    }


    private void goLoginActivity() {
        Intent intentLogin = new Intent(SearchJobResultActivity.this, NewLoginActivity.class);
        startActivity(intentLogin);
    }

    /**
     * 是否定订阅
     */
    private void isSubscription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchJobResultActivity.this);
        builder.setMessage("您确定要重设订阅条件");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

//    /***
//     * 配置请求参数
//     */
//    private HashMap<String, String> setRequestArgument() {
//        setVoicehashMap = new HashMap<String, String>();
//        setVoicehashMap.put("method", "job.setnotice");
//        setVoicehashMap.put("notice_bgntime", "06:00");
//        setVoicehashMap.put("notice_endtime", "23:00");
//        setVoicehashMap.put("phonecode", MyUtils.ALIAS);
//        setVoicehashMap.put("baidu_user_id", MyUtils.baidu_push_userId);
//        setVoicehashMap.put("baidu_channel_id", MyUtils.baidu_channel_id);
//        setVoicehashMap.put("push_way", "2");// （0原生，1百度，2极光）
//        return setVoicehashMap;
//    }

    private void saveData() {
        MyUtils.ALIAS=PushAliasString.getDeviceId(this);
        // 验证信息
        if (MyUtils.ALIAS == null || MyUtils.ALIAS.length() == 0) {
            Toast.makeText(SearchJobResultActivity.this, "手机IMEI获取失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (industry == null || industry.length() == 0) {
            Toast.makeText(SearchJobResultActivity.this, "行业获取失败", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
//        new Async_Set_Rob_Switch(SearchJobResultActivity.this, setRequestArgument()).execute();

        new Async_SetRobJob(SearchJobResultActivity.this, setRob_handler).execute(
                "job.setrushjob", funcid == null ? "" : funcid, areaid, searchword, industry, MyUtils.ALIAS,
                titledataHashMap.get("filter_workyear"), titledataHashMap.get("filter_worktype"), titledataHashMap.get("filter_issuedate"),
                titledataHashMap.get("filter_study"), titledataHashMap.get("filter_stuffmunber"), titledataHashMap.get("filter_salary"));
    }

    private Handler setRob_handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {// 抢工作设置成功
                Toast.makeText(SearchJobResultActivity.this, R.string.setRobJob_succes, Toast.LENGTH_SHORT).show();
            }
        }


    };


    @OnClick({R.id.iv_selectcity_back, R.id.tv_searchjobresult_filtration, R.id.tv_searchjob_result_subscribe, R.id.bt_searchjobresult_collect, R.id.bt_searchjobresult_deliver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_selectcity_back:
                finish();
                break;
            case R.id.tv_searchjobresult_filtration:
                popupmenuBar.show(view);
                break;
            case R.id.tv_searchjob_result_subscribe:
                isSubscription();
                break;
            case R.id.bt_searchjobresult_collect:
                if (MyUtils.isLogin) {
                    if (sjrAdapter != null) {
                        sjrAdapter.collectJob();
                    }
                } else {
                    Toast.makeText(SearchJobResultActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
                break;
            case R.id.bt_searchjobresult_deliver:
                if (MyUtils.isLogin) {
                    if (sjrAdapter != null) {
                        sjrAdapter.deliverJob();
                    }
                } else {
                    Toast.makeText(SearchJobResultActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
                break;
        }
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }*/
}
