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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SearchJobResultAdapter;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.Async_SetRobJob;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.LogTools;
import com.hr.ui.view.custom.MyProgressDialog;
import com.hr.ui.view.custom.PopupmenuBar;
import com.hr.ui.view.pulltorefresh.PullToRefreshBase;
import com.hr.ui.view.pulltorefresh.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchJobResultActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SearchJobResultActivity";
    private PullToRefreshListView lv_searchjobresult_result;
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

    private MyProgressDialog dialog;
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
    private TextView tv_searchjobresult_jobnum;
    private LinearLayout rl_searchjobresult_visible;
    private TextView tv_searchjobresult_condition;
    private TextView tv_searchjobresult_filtration, tv_searchjob_result_subscribe;
    private ImageView iv_selectcity_back;
    private Button bt_searchjobresult_collect, bt_searchjobresult_deliver;

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
     * 记录筛选条件
     */
    private HashMap<String, String> filterMap = new HashMap<>();
    /**
     * 筛选条件
     */
    private static HashMap<String, String> titledataHashMap = new HashMap<String, String>();
    public static HashMap<String, String> dataHashMap;
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
                        sjrAdapter = new SearchJobResultAdapter(SearchJobResultActivity.this, totalList);
                        resultListView.setAdapter(sjrAdapter);
                        firstNumm = 2;
                    } else if (dataList != null && firstNumm == 2 && dataList.size() > 0) {
                        sjrAdapter.notifyDataSetChanged();
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_job_result);
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
        tv_searchjobresult_condition = (TextView) findViewById(R.id.tv_searchjobresult_condition);
        tv_searchjob_result_subscribe = (TextView) findViewById(R.id.tv_searchjob_result_subscribe);
        lv_searchjobresult_result = (PullToRefreshListView) findViewById(R.id.lv_searchjobresult_result);
        tv_searchjobresult_jobnum = (TextView) findViewById(R.id.tv_searchjobresult_jobnum);
        rl_searchjobresult_visible = (LinearLayout) findViewById(R.id.rl_searchjobresult_visible);
        tv_searchjobresult_filtration = (TextView) findViewById(R.id.tv_searchjobresult_filtration);
        iv_selectcity_back = (ImageView) findViewById(R.id.iv_selectcity_back);
        iv_selectcity_back.setOnClickListener(this);
        tv_searchjobresult_filtration.setOnClickListener(this);
        bt_searchjobresult_collect = (Button) findViewById(R.id.bt_searchjobresult_collect);
        bt_searchjobresult_deliver = (Button) findViewById(R.id.bt_searchjobresult_deliver);

        bt_searchjobresult_collect.setOnClickListener(this);
        bt_searchjobresult_deliver.setOnClickListener(this);
        tv_searchjob_result_subscribe.setOnClickListener(this);

        resultListView = lv_searchjobresult_result.getRefreshableView();
        View view = LayoutInflater.from(SearchJobResultActivity.this).inflate(R.layout.foot_listview_white, null);
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
                });
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
        dialog = new MyProgressDialog(this);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
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
            totalList.addAll(dataList);
            if (totalIsSelect.size() == 0) {
                for (int i = 0; i < totalList.size(); i++) {
                    totalIsSelect.put(i, false);
                }
            } else {
                for (int i = totalIsSelect.size() - 1; i < dataList.size(); i++) {
                    totalIsSelect.put(i, false);
                }
            }
            refreshInfo();
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
                tv_searchjobresult_jobnum.setText("为您找到" + jobNum + "+个职位");
            } else {
                tv_searchjobresult_jobnum.setText("为您找到" + jobNum + "个职位");
            }
            if (jobNum.equals("0")) {
                lv_searchjobresult_result.setVisibility(View.GONE);
                rl_searchjobresult_visible.setVisibility(View.GONE);
            } else {
                lv_searchjobresult_result.setVisibility(View.VISIBLE);
                rl_searchjobresult_visible.setVisibility(View.VISIBLE);
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
            tv_searchjobresult_condition.setText(word);
            return;
        } else {
            // 设置找工作的值
            tv_searchjobresult_condition.setText(showBuffer.toString().replace("++", "+"));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_searchjobresult_filtration:
                popupmenuBar.show(v);
                break;
            case R.id.iv_selectcity_back:
                finish();
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
            case R.id.tv_searchjob_result_subscribe:
                isSubscription();
                break;
        }
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

        ;
    };
}
