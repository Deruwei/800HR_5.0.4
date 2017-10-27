package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CreateResumeJobExpLVAdapter;
import com.hr.ui.adapter.ResumeTrainExpLVAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 城市选择|| 2013/12/24 add:多项选择
 *
 * @author 800hr:xuebaohua
 */
public class CreateSelectPlaceToResumeActivity extends Activity implements
        OnClickListener {

    private int index; // 0, 代表一级分类。1, 代表二级分类
    MyBaseAdapterFindJobPlaceSelect.TopViewHolder topViewHolder;
    private static ArrayList<String> zhixiashiORhotcity; // 直辖市或热门城市
    private int fromTag;
    private final int FROMTAG_VALUE0 = 100;// 100：个人信息
    private final int FROMTAG_VALUE1 = 101;// 101：求职意向
    private final int FROMTAG_VALUE2 = 102;// 102：工作经验
    private final int FROMTAG_VALUE3 = 103;// 103：培训地点
    private String locationCityID = "";// 定位到的城市对应的id
    private boolean isCHS = true;
    private ArrayList<HashMap<String, String>> dataArrayList = null;
    private ArrayList<HashMap<String, String>> dataArrayList2 = null;

    private static HashMap<String, Boolean> checkStateHashMap;// 记录选择状态
    private static HashMap<String, Boolean> checkStateHashMap2;// 记录选择状态
    private Object fromActivity = null;
    private String id;
    private LinearLayout placeselect_showinforlayout;// 动态布局
    private RelativeLayout placeselect_showmessage;
    private TextView placeselect_selectedinfo;
    public MyBaseAdapterFindJobPlaceSelect adapter;
    private int ableselected = 0;// 可以选择的个数
    private ImageView iv_placeselect_back;
    private MyBaseAdapterFindJobPlaceSelect2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_selectcity_tosearch);
        init();
    }

    private void init() {
        try {
            final TextView placeselect_title = (TextView) findViewById(R.id.tv_placeselect_jobnum);
            findViewById(R.id.post_placeselect_confirm).setOnClickListener(this);
            placeselect_showmessage = (RelativeLayout) findViewById(R.id.placeselect_showmessage);
            placeselect_showinforlayout = (LinearLayout) findViewById(R.id.placeselect_showinforlayout);
            placeselect_selectedinfo = (TextView) findViewById(R.id.placeselect_selectedinfo);
            iv_placeselect_back = (ImageView) findViewById(R.id.iv_placeselect_back);

            iv_placeselect_back.setOnClickListener(this);
            final ListView mListView = (ListView) findViewById(R.id.placeselect_listview);
            final ListView add_city_listView = (ListView) findViewById(R.id.add_city);
            Bundle bundle = getIntent().getExtras();
            isCHS = bundle.getBoolean("isCHS");
            fromTag = bundle.getInt("fromtag", -1);
            ableselected = bundle.getInt("ableselected");
            final String filter = bundle.getString("filter");
            id = bundle.getString("id");
            final String value = bundle.getString("value");
            if (id == null || id.length() == 0) {// 初始化已选数据
                checkStateHashMap = new HashMap<String, Boolean>();// 记录选择状态
                String placeID = getIntent().getStringExtra("placeId");
                if (placeID != null && placeID.length() > 0) {
                    String[] placeStrings = placeID.split(",");
                    for (String string : placeStrings) {
                        checkStateHashMap.put(string, true);
                    }

                }
            }
            // 初始化动态布局
            if (ableselected > 0) {
                refreshShowInfo(false);
                placeselect_showmessage.setOnClickListener(this);
                placeselect_showinforlayout.setVisibility(View.GONE);// 默认收起
            } else {// 隐藏布局
                placeselect_showmessage.setVisibility(View.GONE);
                placeselect_showinforlayout.setVisibility(View.GONE);
            }
            switch (fromTag) {// 初始化上一个页面对象
                case FROMTAG_VALUE1:// 求职意向
                    fromActivity = CreateResumeJobOrderActivity.getInstance();
                    break;
                default:
                    break;
            }
            if (isCHS) {// 初始化title字段
                placeselect_title.setText("选择城市");
            } else {
                placeselect_title.setText("Locate");
            }

            dataArrayList = getData(filter, id, value);// 初始化列表数据

            if (index == 0 && filter.equals("place")) { // 如果是地点一级分类，初始化直辖市数组
                zhixiashiORhotcity = new ArrayList<String>();
                if (isCHS) {
                    zhixiashiORhotcity.add("北京");
                    zhixiashiORhotcity.add("上海");
                    zhixiashiORhotcity.add("重庆");
                    zhixiashiORhotcity.add("天津");

                    // 热门城市
                    if (fromTag != FROMTAG_VALUE1) {
                        zhixiashiORhotcity.add("广州");
                        zhixiashiORhotcity.add("深圳");
                    }
                } else {
                    zhixiashiORhotcity.add("Beijing");
                    zhixiashiORhotcity.add("Shanghai");
                    zhixiashiORhotcity.add("Chongqing");
                    zhixiashiORhotcity.add("Tianjin");
                    // 热门城市
                    if (fromTag != FROMTAG_VALUE1) {
                        zhixiashiORhotcity.add("Guangzhou");
                        zhixiashiORhotcity.add("Shenzhen");
                    }
                }
            }

            adapter = new MyBaseAdapterFindJobPlaceSelect(this, dataArrayList);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                /**
                 * 省市选择
                 */
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
                    if (fromTag == FROMTAG_VALUE1) {// --------------------------求职意向-工作地址
                        if(dataArrayList.get(position).get("value")!=null&&!"".equals(dataArrayList.get(position).get("value"))) {
                            if (zhixiashiORhotcity.contains(dataArrayList.get(position).get("value"))) {
                                if (checkStateHashMap.containsKey(dataArrayList.get(position).get("key"))) {
                                    // 取消选中状态
                                    checkStateHashMap.remove(dataArrayList.get(position).get("key"));
                                } else {
                                    // 检测数量
                                    if (checkStateHashMap.size() == 5) {
                                        Toast.makeText(CreateSelectPlaceToResumeActivity.this, "数量已达最大值", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    // 设置选中状态
                                    checkStateHashMap.put(dataArrayList.get(position).get("key"), true);
                                }
                                adapter.notifyDataSetChanged();
                                refreshShowInfo(true);
                                CreateSelectPlaceToResumeActivity placeSelectedActivity = CreateSelectPlaceToResumeActivity.this;
                                if (placeSelectedActivity != null) {
                                    placeSelectedActivity.refreshShowInfo(true);
                                }
                            } else {
                                try {
                                    if (!dataArrayList.get(position).get("value").equals("#热门城市") && !dataArrayList.get(position).get("value").equals("#按省份选择城市")) {
                                        dataArrayList2 = getData("place", dataArrayList.get(position).get("key"), dataArrayList.get(position).get("value"));
                                        adapter2 = new MyBaseAdapterFindJobPlaceSelect2(CreateSelectPlaceToResumeActivity.this, dataArrayList2);
                                        add_city_listView.setAdapter(adapter2);
                                        adapter.notifyDataSetChanged();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {// ---------------------------------非求职意向-工作地址
                        if (position == 0) {
                            switch (fromTag) {
                                case FROMTAG_VALUE2:// 工作经验
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceId(locationCityID);
                                    if (isCHS) {
                                        if(!"".equals(MyUtils.currentCityZh)&&MyUtils.currentCityZh!=null) {
                                            CreateResumeJobActivity.createResumeJobActivity.setPlaceText(MyUtils.currentCityZh);
                                            backPage(fromActivity);
                                        }
                                    } else {

                                        if(!"".equals(MyUtils.currentCityEn)&&MyUtils.currentCityEn!=null) {
                                            ((CreateResumeJobExpLVAdapter) fromActivity)
                                                    .setPlaceText(MyUtils.currentCityEn);
                                            backPage(fromActivity);
                                        }
                                    }
                                    break;
                                case FROMTAG_VALUE3:// 培训经历
                                    ResumeTrainExpLVAdapter
                                            .setPlaceId(locationCityID);
                                    if (isCHS) {
                                        ResumeTrainExpLVAdapter
                                                .setPlaceText(MyUtils.currentCityZh);
                                    } else {
                                        ResumeTrainExpLVAdapter
                                                .setPlaceText(MyUtils.currentCityEn);
                                    }
                                    backPage(fromActivity);
                                    break;
                                case FROMTAG_VALUE0:// 个人信息
                                    CreateResumePersonInfoActivity
                                            .setPlaceId(locationCityID);
                                    if (isCHS) {
                                        if(!"".equals(MyUtils.currentCityZh)&&MyUtils.currentCityZh!=null) {
                                            CreateResumePersonInfoActivity
                                                    .setPlaceText(MyUtils.currentCityZh);
                                            backPage(fromActivity);
                                        }else{
                                            return;
                                        }
                                    } else {
                                        if(!"".equals(MyUtils.currentCityZh)&&MyUtils.currentCityZh!=null) {
                                            CreateResumePersonInfoActivity
                                                    .setPlaceText(MyUtils.currentCityEn);
                                            backPage(fromActivity);
                                        }else{
                                            return;
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return;
                        }
                        if (zhixiashiORhotcity.contains(dataArrayList.get(position - 1).get("value"))) {
                            switch (fromTag) {
                                case FROMTAG_VALUE2:// 工作经验
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceId(dataArrayList.get(position - 1).get("key"));
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceText(dataArrayList.get(position - 1).get("value").trim());
                                    break;
                                case FROMTAG_VALUE3:// 培训经历
                                    ResumeTrainExpLVAdapter.setPlaceId(dataArrayList.get(position - 1).get("key"));
                                    ResumeTrainExpLVAdapter.setPlaceText(dataArrayList.get(position - 1).get("value").trim());
                                    break;
                                case FROMTAG_VALUE0:// 培训经历
                                    CreateResumePersonInfoActivity.setPlaceId(dataArrayList.get(position - 1).get("key"));
                                    CreateResumePersonInfoActivity.setPlaceText(dataArrayList.get(position - 1).get("value").trim());
                                    break;

                                default:
                                    break;
                            }
                            // 点击的是直辖市或热门城市
                            backPage(fromActivity);

                        } else {
                            try {
                                if (!dataArrayList.get(position - 1).get("value").equals("#热门城市") && !dataArrayList.get(position - 1).get("value").equals("#按省份选择城市")) {
                                    dataArrayList2 = getData("place", dataArrayList.get(position - 1).get("key"), dataArrayList.get(position - 1).get("value"));
                                    adapter2 = new MyBaseAdapterFindJobPlaceSelect2(CreateSelectPlaceToResumeActivity.this, dataArrayList2);
                                    add_city_listView.setAdapter(adapter2);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // 回到“找工作”页面
                private void backPage(Object object) {
                    finish();
                }

            });

            add_city_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (fromTag) {
                        case FROMTAG_VALUE1:// 求职意向
                            if (checkStateHashMap
                                    .containsKey(dataArrayList2.get(
                                            position).get("key"))) {
                                // 取消选中状态
                                checkStateHashMap.remove(dataArrayList2
                                        .get(position).get("key"));
                            } else {
                                // 判断是否为title
                                if (dataArrayList2.get(position)
                                        .get("key").endsWith("00")) {
                                    Iterator keys = checkStateHashMap
                                            .keySet().iterator();
                                    while (keys.hasNext()) {
                                        String key = (String) keys.next();
                                        if (key.contains(dataArrayList2.get(position).get("key").subSequence(0, 2))) {
                                            // 移除本项中左右子项
                                            keys.remove();
                                        }
                                    }
                                    if (checkStateHashMap.size() == 5) {
                                        Toast.makeText(
                                                CreateSelectPlaceToResumeActivity.this,
                                                "数量已达最大值",
                                                Toast.LENGTH_SHORT)
                                                .show();


                                        return;
                                    }
                                    checkStateHashMap.put(dataArrayList2
                                                    .get(position).get("key"),
                                            true);
                                } else {
                                    // 如果已选title，则移除本title
                                    checkStateHashMap
                                            .remove(dataArrayList2
                                                    .get(position)
                                                    .get("key")
                                                    .subSequence(0, 2)
                                                    + "00");

                                    if (checkStateHashMap.size() == 5) {
                                        Toast.makeText(
                                                CreateSelectPlaceToResumeActivity.this,
                                                "数量已达最大值",
                                                Toast.LENGTH_SHORT)
                                                .show();
                                        return;
                                    }
                                    // 设置选中状态
                                    checkStateHashMap.put(dataArrayList2
                                                    .get(position).get("key"),
                                            true);
                                }
                                adapter2.notifyDataSetChanged();
                                refreshShowInfo(true);
                                CreateSelectPlaceToResumeActivity placeSelectedActivity = CreateSelectPlaceToResumeActivity.this;
                                if (placeSelectedActivity != null) {
                                    placeSelectedActivity
                                            .refreshShowInfo(true);
                                }
                            }
                            adapter2.notifyDataSetChanged();
                            refreshShowInfo(true);
                            return;
                        case FROMTAG_VALUE2:// 工作经验
                            CreateResumeJobActivity.createResumeJobActivity.setPlaceId(dataArrayList2.get(position).get("key"));
                            CreateResumeJobActivity.createResumeJobActivity.setPlaceText(dataArrayList2.get(position).get("value").trim());
                            Toast.makeText(CreateSelectPlaceToResumeActivity.this, dataArrayList2.get(position).get("value").trim(), Toast.LENGTH_SHORT).show();
                            adapter2.notifyDataSetChanged();
                            break;
                        case FROMTAG_VALUE3:// 培训经历
                            ResumeTrainExpLVAdapter.setPlaceId(dataArrayList2.get(position).get("key"));
                            ResumeTrainExpLVAdapter.setPlaceText(dataArrayList2.get(position).get("value").trim());
                            Toast.makeText(CreateSelectPlaceToResumeActivity.this, dataArrayList2.get(position).get("value").trim(), Toast.LENGTH_SHORT).show();
                            adapter2.notifyDataSetChanged();
                            break;
                        case FROMTAG_VALUE0:// 个人信息
                            CreateResumePersonInfoActivity.setPlaceId(dataArrayList2.get(position).get("key"));
                            CreateResumePersonInfoActivity.setPlaceText(dataArrayList2.get(position).get("value").trim());
                            Toast.makeText(CreateSelectPlaceToResumeActivity.this, dataArrayList2.get(position).get("value").trim(), Toast.LENGTH_SHORT).show();
                            adapter2.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }

                    backPage(fromActivity);
                    backPage(fromActivity);
                }   // 回到“找工作”页面

                private void backPage(Object object) {
                    finish();

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏或显示已选信息
     * keepState保持原显隐状态
     */
    private void refreshShowInfo(boolean keepState) {
        if (ableselected == 0) {// 城市单选时，不提供动态布局显示
            return;
        }
        //System.out.println("***********************refresh");
        // 数量显示状态
        placeselect_selectedinfo.setText(checkStateHashMap.size() + "/"
                + ableselected);
        // 获取数据
        JSONArray cityJSONArray = null;
        try {
            if (MyUtils.USE_ONLINE_ARRAY && isCHS) {
                cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
            } else {
                if (isCHS) {
                    InputStream inputStream = getAssets().open("city_zh.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                } else {
                    InputStream inputStream = getAssets().open("city_en.json");
                    cityJSONArray = new JSONArray(NetUtils.readAsString(
                            inputStream, Constants.ENCODE));
                }
            }

            // 语句虽重复，但顺序不可调换
            if (keepState) {
                if (placeselect_showinforlayout.getVisibility() == View.VISIBLE) {
                    placeselect_showinforlayout.removeAllViews();
                    Set<String> keySet = checkStateHashMap.keySet();
                    for (String string : keySet) {
                        iteratorPlace(cityJSONArray, string);
                    }
                }
            } else {

                if (placeselect_showinforlayout.getVisibility() == View.VISIBLE) {
                    placeselect_showinforlayout.setVisibility(View.GONE);
                    placeselect_showinforlayout.removeAllViews();
                    placeselect_selectedinfo
                            .setCompoundDrawablesWithIntrinsicBounds(null,
                                    null,
                                    getResources()
                                            .getDrawable(R.mipmap.jiantou_right),
                                    null);
                } else {
                    placeselect_showinforlayout.removeAllViews();
                    placeselect_showinforlayout.setVisibility(View.VISIBLE);
                    Set<String> keySet = checkStateHashMap.keySet();
                    for (String string : keySet) {
                        iteratorPlace(cityJSONArray, string);
                    }
                    placeselect_selectedinfo
                            .setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    getResources().getDrawable(
                                            R.mipmap.jiantou_right), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cityJSONArray = null;
    }

    /**
     * 添加已选城市视图
     *
     * @param keyString
     */
    private void addView(final String keyString, final String valueString) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 5;
        params.topMargin = 3;
        params.bottomMargin = 3;
        params.rightMargin = 0;
        final TextView tv = (TextView) LayoutInflater.from(this).inflate(
                R.layout.textview_selected, null, false);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(6, 4, 6, 3);
        if ("00".contains(keyString.subSequence(keyString.length() - 2,
                keyString.length()))) {
            tv.setTextSize(20f);
        }
        tv.setText(valueString.trim());
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStateHashMap.remove(keyString);
                refreshShowInfo(true);
                adapter.notifyDataSetChanged();
                CreateSelectPlaceToResumeActivity placeSelectedActivity = CreateSelectPlaceToResumeActivity.this;
                if (placeSelectedActivity != null) {
                    placeSelectedActivity.refreshShowInfo(true);
                    placeSelectedActivity.adapter.notifyDataSetChanged();
                }
            }
        });
        tv.setTag(keyString);
        placeselect_showinforlayout.addView(tv);
    }

    /**
     * 根据id遍历出text
     *
     * @param placeIdString
     */
    private void iteratorPlace(JSONArray cityJSONArray, String placeIdString) {
        try {

            if (placeIdString != null && placeIdString.length() > 0) {
                for (int i = 0; i < cityJSONArray.length(); i++) {
                    JSONObject object = cityJSONArray.getJSONObject(i);
                    if (object.has(placeIdString)) {
                        addView(placeIdString, object.getString(placeIdString));// 添加view
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取数据
     *
     * @param filter
     * @param id
     * @param value
     * @return
     * @throws Exception
     */
    private ArrayList<HashMap<String, String>> getData(String filter, String id, String value) throws Exception {
        ArrayList<HashMap<String, String>> data = null;
        if (id == null) { // 一级分类
            if ("place".equals(filter)) {
                if (MyUtils.USE_ONLINE_ARRAY && isCHS) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(
                            this, "city.json");
                    data = NetService.getCityArray(cityJSONArray, "0");
                } else {// 使用程序内置数据
                    if (isCHS) {// ------------------------------------zh
                        InputStream in = getAssets().open("city_zh.json");
                        JSONArray cityJSONArray = new JSONArray(
                                NetUtils.readAsString(in, Constants.ENCODE));
                        data = NetService.getCityArray(cityJSONArray, "0");
                    } else {// ------------------------------------en
                        InputStream in = getAssets().open("city_en.json");
                        JSONArray cityJSONArray = new JSONArray(
                                NetUtils.readAsString(in, Constants.ENCODE));
                        data = NetService.getCityArray(cityJSONArray, "0");
                    }
                }
                if (fromTag == FROMTAG_VALUE1
                        && data.get(data.size() - 1).containsValue("aa00")) {// 移除aa00
                    data.remove(data.size() - 1);
                }
            }
        } else {// 二级分类
            index = 1;
            if ("place".equals(filter)) {
                if (MyUtils.USE_ONLINE_ARRAY && isCHS) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
                    data = NetService.getCityArray(cityJSONArray, id);
                } else {// 使用程序内置数据
                    if (isCHS) {// ------------------------------------zh
                        InputStream in = getAssets().open("city_zh.json");
                        JSONArray cityJSONArray = new JSONArray(
                                NetUtils.readAsString(in, Constants.ENCODE));
                        data = NetService.getCityArray(cityJSONArray, id);
                    } else {// ------------------------------------en
                        InputStream in = getAssets().open("city_en.json");
                        JSONArray cityJSONArray = new JSONArray(
                                NetUtils.readAsString(in, Constants.ENCODE));
                        data = NetService.getCityArray(cityJSONArray, id);
                    }
                }
            }
        }
        HashMap<String, String> item;
        if (index == 0) {

            if (isCHS) {
                if (fromTag != FROMTAG_VALUE1) {

                    item = new HashMap<String, String>();
                    item.put("value", "#热门城市");
                    data.add(0, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1100");
                    item.put("value", "北京");
                    data.add(1, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1200");
                    item.put("value", "上海");
                    data.add(2, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1801");
                    item.put("value", "广州");
                    data.add(3, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1802");
                    item.put("value", "深圳");
                    data.add(4, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1300");
                    item.put("value", "天津");
                    data.add(5, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1400");
                    item.put("value", "重庆");
                    data.add(6, item);

                    item = new HashMap<String, String>();
                    item.put("value", "#按省份选择城市");
                    data.add(7, item);
                }
            } else {
                if (fromTag != FROMTAG_VALUE1) {

                    item = new HashMap<String, String>();
                    item.put("value", "#Hot city");
                    data.add(0, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1100");
                    item.put("value", "Beijing");
                    data.add(1, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1200");
                    item.put("value", "Shanghai");
                    data.add(2, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1801");
                    item.put("value", "Guangzhou");
                    data.add(3, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1802");
                    item.put("value", "Shenzhen");
                    data.add(4, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1300");
                    item.put("value", "Tianjin");
                    data.add(5, item);

                    item = new HashMap<String, String>();
                    item.put("key", "1400");
                    item.put("value", "Chongqing");
                    data.add(6, item);

                    item = new HashMap<String, String>();
                    item.put("value", "#Select city");
                    data.add(7, item);

                }
            }

        } else {
            item = new HashMap<String, String>();
            item.put("key", id);
            item.put("value", value);
            data.add(0, item);
        }
        return data;
    }

    /**
     * 城市选择适配器
     *
     * @author 800hr:xuebaohua
     */
    private final class MyBaseAdapterFindJobPlaceSelect extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataArrayList = null;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 2;

        public MyBaseAdapterFindJobPlaceSelect(Context context,
                                               ArrayList<HashMap<String, String>> dataArrayList) {
            this.inflater = LayoutInflater.from(context);
            this.dataArrayList = dataArrayList;
        }

        @Override
        public int getCount() {
            if (dataArrayList != null) {
                return dataArrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;// 定位城市
            } else {
                return 1;// 热门城市+普通item
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (fromTag == FROMTAG_VALUE1) {// 求职意向-工作地点
                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.placeselect_listview_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_title);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                    holder.name.setVisibility(View.VISIBLE);
                    holder.title.setVisibility(View.VISIBLE);
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
                // 设置显示数据
                holder.title.setVisibility(View.GONE);
                holder.name.setText(dataArrayList.get(position)
                        .get("value"));
                if (zhixiashiORhotcity.contains(dataArrayList.get(position)
                        .get("value"))) {
                    if (checkStateHashMap.containsKey(dataArrayList.get(
                            position).get("key"))) {// 勾选状态
                        holder.name
                                .setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.duihao), null);
                    } else {// 未勾选状态
                        holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }
            } else {
                int viewType = getItemViewType(position);
                if (viewType == 0) {// 定位
                    if (convertView == null) {
                        topViewHolder = new TopViewHolder();
                        convertView = inflater.inflate(
                                R.layout.placeselect_listview_item, null);
                        topViewHolder.title = (TextView) convertView
                                .findViewById(R.id.placeselect_listview_item_title);
                        topViewHolder.title.setVisibility(View.VISIBLE);
                        topViewHolder.name = (TextView) convertView
                                .findViewById(R.id.placeselect_listview_item_text);
                        topViewHolder.name
                                .setCompoundDrawablesWithIntrinsicBounds(
                                        null, null, null, null);
                        convertView.setTag(topViewHolder);
                    } else {
                        topViewHolder = (TopViewHolder) convertView
                                .getTag();
                    }

                    if (MyUtils.currentCityZh != null
                            && MyUtils.currentCityZh.length() > 0) {// 定位城市
                        try {

                            if (isCHS) {
                                locationCityID = ResumeInfoIDToString.getCityID(CreateSelectPlaceToResumeActivity.this,MyUtils.currentCityZh,true);
                                topViewHolder.name.setText(MyUtils.currentCityZh);
                            } else {
                                locationCityID = ResumeInfoIDToString.getCityID(CreateSelectPlaceToResumeActivity.this,MyUtils.currentCityZh,true);
                                topViewHolder.name.setText(MyUtils.currentCityEn);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        topViewHolder.name.setText(isCHS == true ? "定位失败"
                                : "Position failed");
                    }
                    topViewHolder.title.setText(isCHS == true ? "定位城市"
                            : "Position city");
                } else if (viewType == 1) {// 非定位
                    if (convertView == null) {
                        convertView = inflater.inflate(
                                R.layout.placeselect_listview_item, null);
                        holder = new ViewHolder();
                        holder.title = (TextView) convertView
                                .findViewById(R.id.placeselect_listview_item_title);
                        holder.name = (TextView) convertView
                                .findViewById(R.id.placeselect_listview_item_text);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                        holder.name.setVisibility(View.VISIBLE);
                        holder.title.setVisibility(View.VISIBLE);
                        holder.name
                                .setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                    // 设置显示数据
                    if (position >= 1) {
                        if (dataArrayList.get(position - 1).get("value")
                                .contains("#")) {
                            holder.title.setVisibility(View.VISIBLE);
                            holder.title.setText(dataArrayList
                                    .get(position - 1).get("value")
                                    .replace("#", ""));
                            holder.name.setVisibility(View.GONE);
                        } else {
                            holder.title.setVisibility(View.GONE);
                            holder.name.setText(dataArrayList.get(
                                    position - 1).get("value"));
                            if (zhixiashiORhotcity.contains(dataArrayList
                                    .get(position - 1).get("value"))) {
                                if (checkStateHashMap
                                        .containsKey(dataArrayList.get(
                                                position - 1).get("key"))) {// 勾选状态
                                    holder.name
                                            .setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.duihao), null);
                                } else {// 未勾选状态
                                    holder.name
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    null, null, null, null);
                                }
                            }
                        }
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView title; // 标题
            TextView name; // 城市名字
        }

        private class TopViewHolder {
            TextView title; // 标题
            TextView name; // 城市名字
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_placeselect_back:
                //MainActivity.openMenu();
                finish();
                break;
            case R.id.post_placeselect_confirm:// 确定
                // 设置显示信息
                switch (fromTag) {
                    case FROMTAG_VALUE1:// 求职意向
                       /* ((CreateResumeJobOrderActivity) fromActivity)
                                .setPlaceId(checkStateHashMap);
                        checkStateHashMap.clear();*/
                        break;
                    default:
                        break;
                }
                // 回到原始页面
                if (id != null) {
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.placeselect_showmessage:// 刷新动态布局
                refreshShowInfo(false);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        if (index == 0) {
            if (dataArrayList != null) {
                dataArrayList.clear();
                dataArrayList = null;
            }
            if (checkStateHashMap != null) {
                checkStateHashMap.clear();
                checkStateHashMap = null;
            }
            if (zhixiashiORhotcity != null) {
                zhixiashiORhotcity.clear();
                zhixiashiORhotcity = null;
            }
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 城市选择适配器
     *
     * @author 800hr:xuebaohua
     */
    private final class MyBaseAdapterFindJobPlaceSelect2 extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataArrayList2 = null;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 2;

        public MyBaseAdapterFindJobPlaceSelect2(Context context,
                                                ArrayList<HashMap<String, String>> dataArrayList) {
            this.inflater = LayoutInflater.from(context);
            this.dataArrayList2 = dataArrayList;
        }

        @Override
        public int getCount() {
            if (dataArrayList2 != null) {
                return dataArrayList2.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArrayList2.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;// 定位城市
            } else {
                return 1;// 热门城市+普通item
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (fromTag == FROMTAG_VALUE1) {// 求职意向-工作地点
                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.placeselect_listview_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_title);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                    holder.name.setVisibility(View.VISIBLE);
                }
                holder.title.setVisibility(View.GONE);
                if (checkStateHashMap.containsKey(dataArrayList2.get(
                        position).get("key"))) {// 勾选状态
                    holder.name
                            .setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    getResources().getDrawable(
                                            R.mipmap.duihao), null);
                } else {// 未勾选状态
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, null, null);
                }
                holder.name.setText(dataArrayList2.get(position)
                        .get("value").trim());

            } else {

                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.placeselect_listview_item, null);
                    holder = new ViewHolder();
                    holder.title = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_title);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.placeselect_listview_item_text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                    holder.name.setVisibility(View.VISIBLE);
                }
                holder.title.setVisibility(View.GONE);
                if (checkStateHashMap.containsKey(dataArrayList2.get(
                        position).get("key"))) {// 勾选状态
                    holder.name
                            .setCompoundDrawablesWithIntrinsicBounds(null, null,
                                    getResources().getDrawable(R.mipmap.duihao), null);
                } else {// 未勾选状态
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(
                            null, null, null, null);
                }
                holder.name.setText(dataArrayList2.get(position).get("value").trim());

            }
            return convertView;
        }

        private class ViewHolder {
            TextView title; // 标题
            TextView name; // 城市名字
        }
    }

}
