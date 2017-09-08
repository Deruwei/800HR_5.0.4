package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.MyPositionAdapter;
import com.hr.ui.model.BrowsedInfo;
import com.hr.ui.model.PositionInfo;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.netutils.AsyncPersonCenterApplied;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我申请的职位---列表页
 */

public class MyPositionActivity extends BaseActivity {
    @Bind(R.id.iv_my_position_back)
    ImageView ivMyPositionBack;
    @Bind(R.id.tv_my_position_jobnum)
    TextView tvMyPositionJobnum;
    @Bind(R.id.rl_my_position_top)
    RelativeLayout rlMyPositionTop;
    @Bind(R.id.rl_noDataApplyJob)
    RelativeLayout rlNoDataApplyJob;
    @Bind(R.id.lv_my_position_listview)
    RecyclerView lvMyPositionListview;
    @Bind(R.id.sr_position)
    SwipeRefreshLayout srPosition;
    @Bind(R.id.rl_hasDataApplyJob)
    RelativeLayout rlHasDataApplyJob;
    private Context mContext = this;
    private ArrayList<PositionInfo> listapplied=new ArrayList<>();// 申请的职位
    private int index = 1;// 页码索引
    private MyPositionAdapter positionAdpter;
/*    public static boolean isLoadAll;// 是否全部加载
    public static boolean isLoading;*/
    private int lastVisibleItem;
    private LinearLayoutManager manager;
    //是否是第一次加载数据
    private boolean fisrt;
    private Handler handlerService=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    // System.out.println("我申请的职位：" + jsonObject);
                   // Log.i("我申请的职位",jsonObject.toString());
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 请求成功

                            JSONArray jsonArray = jsonObject
                                    .getJSONArray("applied_list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject) jsonArray
                                        .get(i);
                                PositionInfo positionInfo = new PositionInfo();
                                positionInfo.setEnterprise_name(jsonObject2
                                        .getString("enterprise_name"));
                                positionInfo.setApplied_time(jsonObject2
                                        .getString("applied_time"));
                                positionInfo.setApplied_num(jsonObject2
                                        .getString("applied_num"));
                                positionInfo.setJob_id(jsonObject2
                                        .getString("job_id"));
                                positionInfo.setJob_name(jsonObject2
                                        .getString("job_name"));
                                positionInfo.setRecord_id(jsonObject2
                                        .getString("record_id"));
                                positionInfo.setIs_expire(jsonObject2
                                        .getString("is_expire"));
                                positionInfo.setArea(jsonObject2
                                        .getString("workplace"));
                                positionInfo.setJob_name(jsonObject2
                                        .getString("job_name"));
                                positionInfo.setEnterprise_id(jsonObject2
                                        .getString("enterprise_id"));
                                positionInfo.setIs_apply(jsonObject2
                                        .getString("is_apply"));
                                positionInfo.setIs_favourite(jsonObject2
                                        .getString("is_favourite"));
                                positionInfo.setJob_id(jsonObject2
                                        .getString("job_id"));

                                listapplied.add(positionInfo);
                            }
                            // System.out.println("结束加载.....");
                            // 判断是否全部加载
                            if (jsonObject.has("navpage_info")) {
                                JSONObject navpage_info = jsonObject
                                        .getJSONObject("navpage_info");
                                String current_page = "";
                                String total_pages = "";
                                if (navpage_info.has("current_page")) {
                                    current_page = navpage_info
                                            .getString("current_page");
                                }

                                if (navpage_info.has("total_pages")) {
                                    total_pages = navpage_info
                                            .getString("total_pages");
                                }
                                if (current_page.length() != 0
                                        && total_pages.length() != 0) {
                                    int current_pageInt = Integer
                                            .parseInt(current_page);
                                    int total_pageInt = Integer
                                            .parseInt(total_pages);
                                    if (current_pageInt >= total_pageInt) {
									/*MyPositionActivity.isLoadAll = true;*/
                                    }
                                }
                            }
                            initData();
                            break;
                        default:
                            Toast.makeText(MyPositionActivity.this,
                                    Rc4Md5Utils
                                            .getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
					/*MyPositionActivity.isLoading = false;// 加载完毕*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(fisrt==false) {
                fisrt=true;
                srPosition.setRefreshing(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_position);
        ButterKnife.bind(this);
        MobclickAgent.onEvent(this, "cv-show-applied");
        initView();
        loadNetData();

    }

    private void initView() {
        manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvMyPositionListview.setLayoutManager(manager);
        lvMyPositionListview.addItemDecoration(new SpacesItemDecoration(5));
        positionAdpter = new MyPositionAdapter(this);
        srPosition.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fisrt=false;
                        listapplied = new ArrayList<PositionInfo>();
                        index = 1;
                       loadNetData();
                        positionAdpter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        //rvChannalcontent根据上拉到页面的最低端来加载下一页
        lvMyPositionListview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动到页面的最底端的时候最后一个item的下标+1等于adapter数据的个数，加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == positionAdpter
                        .getItemCount()) {
                    index++;
                    fisrt=false;
                    srPosition.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNetData();
                            positionAdpter.notifyDataSetChanged();
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
        srPosition.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srPosition.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srPosition.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
                        .getDisplayMetrics()));
    }
    /**
     * 是否有数据
     */
    private void isVISIBLE() {
        if (listapplied!=null&&!"".equals(listapplied)&&listapplied.size()!=0) {
            rlHasDataApplyJob.setVisibility(View.VISIBLE);
            rlNoDataApplyJob.setVisibility(View.GONE);
        } else {
            rlHasDataApplyJob.setVisibility(View.GONE);
            rlNoDataApplyJob.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
 /*   private void getData(){
        new AsyncPersonCenterApplied(mContext, listapplied,
                "applied_list", positionAdpter, lvMyPositionListview)
                .execute("user_stow.applied", index+"", "20");
    }*/
    public void loadNetData() {
        if(fisrt==false){
            srPosition.setRefreshing(true);
        }
        NetService service = new NetService(this, handlerService);
        service.execute(getData("user_stow.applied", index+"", "20"));
    }

    public HashMap<String, String> getData(String method, String page, String page_nums) {
			/*MyPositionActivity.isLoading = true;// 开始加载*/
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", method);
            requestParams.put("page", page);
            requestParams.put("page_nums", page_nums);

           return requestParams;

    }
    /**
     * 初始化数据
     */
    private void initData() {
/*        isLoading = false;
        isLoadAll = false;*/
        //Log.i("申请的数据",listapplied.toString());
        if(listapplied!=null&&!"".equals(listapplied)) {
            positionAdpter.setListapplied(listapplied);
            if(index==1) {
               // Log.i("申请的数据1",listapplied.toString());
                lvMyPositionListview.setAdapter(positionAdpter);
            }else{
                //Log.i("申请的数据2",listapplied.toString());
                positionAdpter.notifyDataSetChanged();
            }
        }

       positionAdpter.setOnItemClick(new OnItemClick() {
           @Override
           public void ItemClick(View view, int position) {
               if (!NetUtils.checkNet(mContext)) {
                   Toast.makeText(mContext, R.string.no_network, Toast.LENGTH_SHORT).show();
                   return;
               }
               if (listapplied.size() != 0) {
                   if (listapplied.get(position).getIs_expire().equals("0")) {
                       goCompanyParticular(listapplied, position);
                   } else {
                       Toast.makeText(mContext, "该职位已过期", Toast.LENGTH_SHORT).show();
                       return;
                   }
               }
           }
       });
        isVISIBLE();
    }

/*
    */

    /**
     * 适配器
     *
     * @author 800hr:xuebaohua
     *//*
    private class PositionBaseAdpter extends BaseAdapter {
        ArrayList<PositionInfo> listapplied;
        Context context;

        public PositionBaseAdpter(Context context,
                                  ArrayList<PositionInfo> listapplied) {
            this.context = context;
            this.listapplied = listapplied;
        }

        @Override
        public int getCount() {
            return listapplied == null ? 0 : listapplied.size();
        }

        @Override
        public Object getItem(int position) {
            return listapplied.get(position);
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
                        R.layout.item_my_position_lv, null);

                convertView.setTag(myHolder);
                myHolder.job_name = (TextView) convertView.findViewById(R.id.tv_item_my_position_jobname);
                myHolder.company_name = (TextView) convertView.findViewById(R.id.tv_item_my_position_companyname);
                myHolder.applied_count = (TextView) convertView.findViewById(R.id.tv_item_my_position_cityname);
                myHolder.applied_time = (TextView) convertView.findViewById(R.id.tv_item_my_positionlv_releasetime);
                myHolder.cb_item_my_position_select = (CheckBox) convertView.findViewById(R.id.cb_item_my_position_select);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            if (listapplied.size() != 0) {
                myHolder.cb_item_my_position_select.setVisibility(View.GONE);
                myHolder.company_name.setText(listapplied.get(position).getEnterprise_name());
                myHolder.job_name.setText(listapplied.get(position).getJob_name());
                myHolder.applied_count.setText("已有" + listapplied.get(position).getApplied_num() + "人申请");
                myHolder.applied_time.setText(listapplied.get(position).getApplied_time());

            }
            return convertView;
        }

        private final class MyHolder {
            TextView company_name, job_name, applied_count, applied_time;
            CheckBox cb_item_my_position_select;
        }
    }*/
  /*  @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_my_position_back:
                finish();
                break;
            default:
                break;
        }

    }*/


    /**
     * 跳转到职位详情页
     */
    private void goCompanyParticular(ArrayList<PositionInfo> positionInfo, int item_Index) {

        Intent intent = new Intent(mContext, PostParticularsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("job_id", positionInfo.get(item_Index).getJob_id());
        intent.putExtras(bundle);
        startActivity(intent);
    }

   /* @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        int totalItem = firstVisibleItem + visibleItemCount;
        if (totalItem == listapplied.size() && !isLoadAll) {
            if (isLoading) {
                return;
            }
            new AsyncPersonCenterApplied(mContext, listapplied,
                    "applied_list", positionBaseAdpter, listview)
                    .execute("user_stow.applied", index++ + "", "20");
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }*/

    @OnClick(R.id.iv_my_position_back)
    public void onViewClicked() {
        finish();
    }
}

