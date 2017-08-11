package com.hr.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.FindjobFragment;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 城市选择
 *
 * @author 800hr:xuebaohua
 */
public class MainSelectCityToKeywordActivity extends BaseActivity implements View.OnClickListener {

    int screenWidth;// 屏幕宽度
    private int index; // 0, 代表一级分类。1, 代表二级分类
    MyBaseAdapterFindJobPlaceSelect.TopViewHolder topViewHolder;
    private static ArrayList<String> zhixiashiORhotcity; // 直辖市或热门城市
    private String locationCityID;// 定位到的城市对应的id
    private Context mContext = MainSelectCityToKeywordActivity.this;
    private String id, value, filter;
    private String idSecond, valueSecond, filterSecond;
    private ListView mListView;// 一级
    private ListView add_city;// 二级
    private ArrayList<HashMap<String, String>> dataArrayList;
    private ArrayList<HashMap<String, String>> secondArrList;
    private MyBaseAdapterFindJobPlaceSelect adapter;
    private SelectCitySecondAdapter adapterSecond;
    private ImageView iv_placeselect_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity_tosearch);
        iv_placeselect_back = (ImageView) findViewById(R.id.iv_placeselect_back);
        iv_placeselect_back.setOnClickListener(this);
        init();
    }

    private void init() {
        try {
            // 隐藏动态布局
            findViewById(R.id.placeselect_showmessage).setVisibility(View.GONE);
            findViewById(R.id.placeselect_showinforlayout).setVisibility(View.GONE);
            findViewById(R.id.post_placeselect_confirm).setVisibility(View.GONE);
            mListView = (ListView) findViewById(R.id.placeselect_listview);// 一级
            add_city = (ListView) findViewById(R.id.add_city);// 二级
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                filter = bundle.getString("filter");
                id = bundle.getString("id");
                value = bundle.getString("value");
            }
//            filter = "place";
//            value = "选择地点";
            dataArrayList = getData(filter, id, value);
            if (index == 0 && filter.equals("place")) { // 如果是地点一级分类，初始化直辖市数组
                zhixiashiORhotcity = new ArrayList<String>();
                zhixiashiORhotcity.add("北京");
                zhixiashiORhotcity.add("上海");
                zhixiashiORhotcity.add("重庆");
                zhixiashiORhotcity.add("天津");
                // 热门城市
                zhixiashiORhotcity.add("全国");
                zhixiashiORhotcity.add("广州");
                zhixiashiORhotcity.add("深圳");
            }
            adapter = new MyBaseAdapterFindJobPlaceSelect(this, dataArrayList);
            mListView.setAdapter(adapter);


            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                /**
                 * 省市选择
                 */
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long itemId) {
                    if (position == 0) {
                        FindjobFragment
                                .setPlaceId(locationCityID);
                        String city="";
                        if(MyUtils.currentCityZh.length()>0){
                            city=MyUtils.currentCityZh.substring(0,MyUtils.currentCityZh.length()-1);
                            MyUtils.selectCityId= ResumeInfoIDToString.getCityID(mContext,city,true);
                            MyUtils.selectCityZh=city;
                            FindjobFragment.setPlaceText(city);
                        }else{
                            FindjobFragment.setPlaceText("定位失败");
                        }
                        //System.out.println("id:" + locationCityID + " value:" + MyUtils.currentCityZh);
                        toSearchJob();
                        return;
                    }
                    if (zhixiashiORhotcity.contains(dataArrayList
                            .get(position - 1).get("value"))) {
                        FindjobFragment
                                .setPlaceId(dataArrayList.get(
                                        position - 1).get("key"));
                        FindjobFragment
                                .setPlaceText(dataArrayList
                                        .get(position - 1)
                                        .get("value").trim());
                        MyUtils.selectCityId=dataArrayList.get(position - 1).get("key");
                        MyUtils.selectCityZh=dataArrayList.get(position - 1).get("value").trim();

                        // System.out.println("id:"
                        // + dataArrayList.get(position - 1)
                        // .get("key")
                        // + " value:"
                        // + dataArrayList.get(position - 1)
                        // .get("value").trim());
                        // 点击的是直辖市或热门城市
                        toSearchJob();


                    } else {
                        if (dataArrayList.get(position - 1)
                                .get("value").contains("#")) {
                            return;
                        }
                        idSecond = dataArrayList.get(position - 1).get("key");
                        valueSecond = dataArrayList.get(position - 1).get("value");
                        filterSecond = "place";

                        try {
                            secondArrList = getData(filterSecond, idSecond, valueSecond);
                            adapterSecond = new SelectCitySecondAdapter(MainSelectCityToKeywordActivity.this, secondArrList);
                            add_city.setAdapter(adapterSecond);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            add_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
                    FindjobFragment
                            .setPlaceId(secondArrList.get(position)
                                    .get("key"));
                    FindjobFragment
                            .setPlaceText(secondArrList
                                    .get(position).get("value")
                                    .trim());
                    MyUtils.selectCityId=secondArrList.get(position)
                            .get("key");
                    MyUtils.selectCityZh=secondArrList
                            .get(position).get("value")
                            .trim();
                    toSearchJob();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回到找工作页面
     */
    private void toSearchJob() {
        finish();
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
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(
                            this, "city.json");
                    data = NetService.getCityArray(cityJSONArray, "0");
                } else {// 使用程序内置数据
                    InputStream in = getAssets().open("city_zh.json");
                    JSONArray cityJSONArray = new JSONArray(
                            NetUtils.readAsString(in, Constants.ENCODE));
                    data = NetService.getCityArray(cityJSONArray, "0");
                }
            }
        } else {
            index = 1;
            if ("place".equals(filter)) {
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(
                            this, "city.json");
                    data = NetService.getCityArray(cityJSONArray, id);
                } else {// 使用程序内置数据
                    InputStream in = getAssets().open("city_zh.json");
                    JSONArray cityJSONArray = new JSONArray(
                            NetUtils.readAsString(in, Constants.ENCODE));
                    data = NetService.getCityArray(cityJSONArray, id);
                }
            }
        }
        HashMap<String, String> item;
        if (index == 0) {

            item = new HashMap<String, String>();
            item.put("value", "#热门城市");
            data.add(0, item);

            item = new HashMap<String, String>();
            item.put("key", "");
            item.put("value", "全国");
            data.add(1, item);

            item = new HashMap<String, String>();
            item.put("key", "1100");
            item.put("value", "北京");
            data.add(2, item);

            item = new HashMap<String, String>();
            item.put("key", "1200");
            item.put("value", "上海");
            data.add(3, item);

            item = new HashMap<String, String>();
            item.put("key", "1801");
            item.put("value", "广州");
            data.add(4, item);

            item = new HashMap<String, String>();
            item.put("key", "1802");
            item.put("value", "深圳");
            data.add(5, item);

            item = new HashMap<String, String>();
            item.put("key", "1300");
            item.put("value", "天津");
            data.add(6, item);

            item = new HashMap<String, String>();
            item.put("key", "1400");
            item.put("value", "重庆");
            data.add(7, item);

            item = new HashMap<String, String>();
            item.put("value", "#按省份选择城市");
            data.add(8, item);
        } else {
            item = new HashMap<String, String>();
            item.put("key", id);
            item.put("value", value);
            data.add(0, item);
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_placeselect_back:
                finish();
                break;
        }
    }

    /**
     * 城市选择适配器
     *
     * @author 800hr:xuebaohua
     */
    private final class MyBaseAdapterFindJobPlaceSelect extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataAdapterArrayList = null;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 2;

        public MyBaseAdapterFindJobPlaceSelect(Context context, ArrayList<HashMap<String, String>> dataAdapterArrayList) {
            this.inflater = LayoutInflater.from(context);
            this.dataAdapterArrayList = dataAdapterArrayList;
        }

        @Override
        public int getCount() {
            if (dataAdapterArrayList != null) {
                return dataAdapterArrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataAdapterArrayList.get(position);
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
            int viewType = getItemViewType(position);
            if (viewType == 0) {// 获取定位城市
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
                            .setCompoundDrawablesWithIntrinsicBounds(null,
                                    null, null, null);
                    convertView.setTag(topViewHolder);
                } else {
                    topViewHolder = (TopViewHolder) convertView.getTag();
                }

                if (MyUtils.currentCityZh != null
                        && MyUtils.currentCityZh.length() > 0) {
                    topViewHolder.name.setText(MyUtils.currentCityZh.substring(0,MyUtils.currentCityZh.length()-1));
                    JSONArray cityJSONArray;
                    try {
                        cityJSONArray = NetService.getCityAsJSONArray(
                                MainSelectCityToKeywordActivity.this,
                                "city.json");
                        // InputStream in =
                        // getAssets().open("city_zh.json");
                        // cityJSONArray = new JSONArray(
                        // NetUtils.readAsString(in, Constants.ENCODE));
                        for (int i = 0; i < cityJSONArray.length(); i++) {
                            JSONObject object = cityJSONArray
                                    .getJSONObject(i);
                            Iterator<String> iterator = object.keys();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                String value = object.getString(key);
                                if (value.contains(MyUtils.currentCityZh)) {
                                    locationCityID = key;
                                    // System.out.println("locationCityID:"
                                    // + locationCityID);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    topViewHolder.name.setText("定位失败");
                }
                topViewHolder.title.setText("定位城市");
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
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(
                            null, null,
                            getResources().getDrawable(R.mipmap.jiantou_right),
                            null);
                }

                // 职系设置显示数据
                if (position >= 1) {
                    if (dataAdapterArrayList.get(position - 1).get("value")
                            .contains("#")) {
                        holder.title.setVisibility(View.VISIBLE);
                        holder.title.setText(dataAdapterArrayList
                                .get(position - 1).get("value")
                                .replace("#", ""));
                        holder.name.setVisibility(View.GONE);
                    } else {
                        holder.title.setVisibility(View.GONE);
                        holder.name.setText(dataAdapterArrayList.get(position - 1)
                                .get("value"));
                        if (zhixiashiORhotcity.contains(dataAdapterArrayList.get(
                                position - 1).get("value"))) {
                            holder.name
                                    .setCompoundDrawablesWithIntrinsicBounds(
                                            null, null, null, null);
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

    /**
     * 城市选择适配器2
     *
     * @author 800hr:xuebaohua
     */
    private final class SelectCitySecondAdapter extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataAdapterArrayList = null;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 2;

        public SelectCitySecondAdapter(Context context, ArrayList<HashMap<String, String>> dataAdapterArrayList) {
            this.inflater = LayoutInflater.from(context);
            this.dataAdapterArrayList = dataAdapterArrayList;
        }

        @Override
        public int getCount() {
            if (dataAdapterArrayList != null) {
                return dataAdapterArrayList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataAdapterArrayList.get(position);
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
            ViewHolder2 holder2;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.placeselect_listview_item, null);
                holder2 = new ViewHolder2();
                holder2.title = (TextView) convertView
                        .findViewById(R.id.placeselect_listview_item_title);
                holder2.name = (TextView) convertView
                        .findViewById(R.id.placeselect_listview_item_text);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
                holder2.name.setVisibility(View.VISIBLE);
            }
            holder2.title.setVisibility(View.GONE);
            holder2.name.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    null, null);
            holder2.name.setText(dataAdapterArrayList.get(position).get("value")
                    .trim());

            return convertView;
        }

        private class ViewHolder2 {
            TextView title; // 标题
            TextView name; // 城市名字
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
