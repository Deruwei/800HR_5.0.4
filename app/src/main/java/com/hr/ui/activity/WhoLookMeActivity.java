package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.MyWhoLookMeAdapter;
import com.hr.ui.model.BrowsedInfo;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.netutils.AsyncPersonCenterBrowsed;
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
 * 谁看过我的简历
 *
 * @author 800hr:xuebaohua
 */
public class WhoLookMeActivity extends Activity {

    @Bind(R.id.iv_lookme_back)
    ImageView ivLookmeBack;
    @Bind(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @Bind(R.id.listview_search)
    RecyclerView listviewSearch;
    @Bind(R.id.sr_whoLookMe)
    SwipeRefreshLayout srWhoLookMe;
    @Bind(R.id.rl_hasDataWhoLookMe)
    RelativeLayout rlHasDataWhoLookMe;
    @Bind(R.id.tv_nodDataWhoLookMe)
    TextView tvNodDataWhoLookMe;
    private ArrayList<BrowsedInfo> listBrowsedInfos=new ArrayList<>();// 谁看过我的简历
    private MyWhoLookMeAdapter myBaseAdpter;
    private ArrayList<BrowsedInfo> totalListBrowsedInfos = new ArrayList<>();
    private ImageView iv_lookme_back;
    private boolean isLoadAll = false;// 是否全部加载
    private int index = 1;// 页码索引
    private String page_nums = "";// 总记录数
    private Context mContext = WhoLookMeActivity.this;
    private LinearLayoutManager manager;
    private int lastVisibleItem;
    private Handler handlerService=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    // System.out.println("AsyncPersonCenterBrowsed" + json +
                    // "");
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 请求成功
                            JSONArray jsonArray = jsonObject
                                    .getJSONArray("browsed_list");
                            Log.i("我申请的职位2",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 =jsonArray.getJSONObject(i);
                                    BrowsedInfo browsedInfo = new BrowsedInfo();
                                    browsedInfo.setEnterprise_name(jsonObject2
                                            .getString("enterprise_name"));
                                    browsedInfo.setEnterprise_id(jsonObject2
                                            .getString("enterprise_id"));
                                    browsedInfo.setBrowsed_time(jsonObject2
                                            .getString("browsed_time"));
                                    browsedInfo.setStuffmunber(jsonObject2
                                            .getString("stuffmunber"));
                                    listBrowsedInfos.add(browsedInfo);

                                }


                            initData();
                            break;
                        default:
                            Toast.makeText(
                                    WhoLookMeActivity.this,
                                    Rc4Md5Utils
                                            .getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_look_me);
        ButterKnife.bind(this);
        MobclickAgent.onEvent(this, "cv-who-see-me");
        initView();
        loadNetData();
    }

    private void initData() {

        if(listBrowsedInfos!=null) {
            myBaseAdpter.setListBrowsedInfos(listBrowsedInfos);
            if (index == 1) {
                listviewSearch.setAdapter(myBaseAdpter);
            } else {
                myBaseAdpter.notifyDataSetChanged();
            }
            myBaseAdpter.setOnItemClick(new OnItemClick() {
                @Override
                public void ItemClick(View view, int position) {
                    if (!NetUtils.checkNet(WhoLookMeActivity.this)) {
                        return;
                    }
                    if (listBrowsedInfos.size() != 0) {
                        open_detaile(listBrowsedInfos, position);
                    }
                }
            });

        }
        isVISIBLE();
    }

    /**
     * 是否有数据
     */
    private void isVISIBLE() {
        if (listBrowsedInfos!=null&&!"".equals(listBrowsedInfos)&&listBrowsedInfos.size()!=0) {
            rlHasDataWhoLookMe.setVisibility(View.VISIBLE);
            tvNodDataWhoLookMe.setVisibility(View.GONE);
        } else {
            rlHasDataWhoLookMe.setVisibility(View.GONE);
            tvNodDataWhoLookMe.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        myBaseAdpter=new MyWhoLookMeAdapter(WhoLookMeActivity.this);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listviewSearch.setLayoutManager(manager);
        listviewSearch.addItemDecoration(new SpacesItemDecoration(5));
        srWhoLookMe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        totalListBrowsedInfos = new ArrayList<BrowsedInfo>();
                        listBrowsedInfos.clear();
                        index = 1;
                        loadNetData();
                        myBaseAdpter.notifyDataSetChanged();
                        srWhoLookMe.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        //rvChannalcontent根据上拉到页面的最低端来加载下一页
        listviewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动到页面的最底端的时候最后一个item的下标+1等于adapter数据的个数，加载下一页
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == myBaseAdpter
                        .getItemCount()) {
                    index++;
                    srWhoLookMe.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            srWhoLookMe.setRefreshing(true);
                            loadNetData();
                            myBaseAdpter.notifyDataSetChanged();
                            srWhoLookMe.setRefreshing(false);
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
        srWhoLookMe.setProgressBackgroundColorSchemeResource(android.R.color.white);
        srWhoLookMe.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srWhoLookMe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
                        .getDisplayMetrics()));

    }

    /**
     * 获取数据
     */
    public void loadNetData() {
        NetService service = new NetService(this, handlerService);
        service.execute(getData("user_stow.browsed", index + "", "20"));
    }
   /* private void getData() {
        page_nums = "20";
        new AsyncPersonCenterBrowsed(mContext, listBrowsedInfos, "browsed_list", myBaseAdpter, listviewSearch).execute("user_stow.browsed", index + "", "20");
    }*/
    public HashMap<String, String> getData(String method, String page, String page_nums) {

        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", method);
        requestParams.put("page", page);
        requestParams.put("page_nums", page_nums);
        return requestParams;
    }
/*    private class MyBaseAdpter extends BaseAdapter {
        ArrayList<BrowsedInfo> listBrowsedInfos;
        Context context;

        public MyBaseAdpter(Context context,
                            ArrayList<BrowsedInfo> listBrowsedInfos) {
            this.context = context;
            this.listBrowsedInfos = listBrowsedInfos;
        }

        @Override
        public int getCount() {
            return listBrowsedInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return listBrowsedInfos.get(position);
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
                        R.layout.wholookme_listview, null);

                convertView.setTag(myHolder);
                myHolder.company_name = (TextView) convertView
                        .findViewById(R.id.company_name);
                myHolder.browsed_time = (TextView) convertView
                        .findViewById(R.id.company_time);
                myHolder.job_place = (TextView) convertView
                        .findViewById(R.id.company_scale);

            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.company_name.setText(listBrowsedInfos.get(position)
                    .getEnterprise_name());
            myHolder.browsed_time.setText("浏览日期:"
                    + listBrowsedInfos.get(position).getBrowsed_time());
            myHolder.job_place.setText("公司规模:"
                    + listBrowsedInfos.get(position).getStuffmunber());
            return convertView;
        }

        private final class MyHolder {
            TextView company_name, browsed_time, job_place;
        }
    }*/


   /* @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (!NetUtils.checkNet(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            return;
        }
        if (listBrowsedInfos.size() != 0) {
            open_detaile(listBrowsedInfos, arg2);
        }
    }*/

    /**
     * 初始化视图
     */
    private void open_detaile(ArrayList<BrowsedInfo> listBrowsedInfos, int item_Index) {
        Intent intent = new Intent(this, CompanyParticularActivity.class);
        intent.putExtra("Enterprise_id", listBrowsedInfos.get(item_Index).getEnterprise_id());
        startActivity(intent);
    }

    @OnClick(R.id.iv_lookme_back)
    public void onViewClicked() {
        finish();
    }
}
