package com.hr.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.PagerPostSearchFragment;
import com.hr.ui.utils.MyUtils;
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
 */
public class SelectCityActivity extends BaseActivity {

    private int index = 0; // 0, 代表一级分类。1, 代表二级分类
    MyBaseAdapterFindJobPlaceSelect.TopViewHolder topViewHolder;
    private static ArrayList<String> zhixiashiORhotcity; // 直辖市或热门城市
    private String locationCityID;// 定位到的城市对应的id
    private ListView lv_selectcity_first;
    private ListView lv_selectcity_second;
    private String filter = "place";
    ArrayList<HashMap<String, String>> dataArrayList;
    ArrayList<HashMap<String, String>> dataArrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initView();
    }

    private void initView() {

        try {

            lv_selectcity_first = (ListView) findViewById(R.id.lv_selectcity_first);//一级
            lv_selectcity_second = (ListView) findViewById(R.id.lv_selectcity_second);//二级

            dataArrayList = getData("place", null, "选择地点");

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
            MyBaseAdapterFindJobPlaceSelect adapter = new MyBaseAdapterFindJobPlaceSelect(this, dataArrayList);
            lv_selectcity_first.setAdapter(adapter);
            lv_selectcity_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                /**
                 * 省市选择
                 */
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {

                    if (position == 0) {
                        PagerPostSearchFragment
                                .setPlaceId(locationCityID);
                        PagerPostSearchFragment
                                .setPlaceText(MyUtils.currentCityZh);
                        System.out.println("id:" + locationCityID + " value:" + MyUtils.currentCityZh);
                        return;
                    }
                    if (zhixiashiORhotcity.contains(dataArrayList.get(position - 1).get("value"))) {
                        PagerPostSearchFragment.setPlaceId(dataArrayList.get(position - 1).get("key"));
                        PagerPostSearchFragment.setPlaceText(dataArrayList.get(position - 1).get("value").trim());
                    } else {
                        if (dataArrayList.get(position - 1)
                                .get("value").contains("#")) {
                            return;
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据
     *
     * @return
     * @throws Exception
     */
    private ArrayList<HashMap<String, String>> getData(String filter, String id, String value) throws Exception {
        ArrayList<HashMap<String, String>> data = null;
        if (id == null) { // 一级分类
            if ("place".equals(filter)) {
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
                    data = NetService.getCityArray(cityJSONArray, "0");
                } else {// 使用程序内置数据
                    InputStream in = getAssets().open("city_zh.json");
                    JSONArray cityJSONArray = new JSONArray(NetUtils.readAsString(in, Constants.ENCODE));
                    data = NetService.getCityArray(cityJSONArray, "0");
                    Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            index = 1;
            if ("place".equals(filter)) {
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供数据
                    JSONArray cityJSONArray = NetService.getCityAsJSONArray(this, "city.json");
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

    /**
     * 城市选择适配器
     */
    private final class MyBaseAdapterFindJobPlaceSelect extends BaseAdapter {
        ArrayList<HashMap<String, String>> dataArrayList = null;
        private LayoutInflater inflater;
        final int VIEW_TYPE = 2;

        public MyBaseAdapterFindJobPlaceSelect(Context context, ArrayList<HashMap<String, String>> dataArrayList) {
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
            if (index == 0) {// 一级目录

                int viewType = getItemViewType(position);
                if (viewType == 0) {// 获取定位城市
                    if (convertView == null) {
                        topViewHolder = new TopViewHolder();
                        convertView = inflater.inflate(
                                R.layout.item_selectcity_first, null);

                        topViewHolder.name = (TextView) convertView
                                .findViewById(R.id.tv_selectcity_first);
                        topViewHolder.name
                                .setCompoundDrawablesWithIntrinsicBounds(null,
                                        null, null, null);
                        convertView.setTag(topViewHolder);
                    } else {
                        topViewHolder = (TopViewHolder) convertView.getTag();
                    }

                    if (MyUtils.currentCityZh != null && MyUtils.currentCityZh.length() > 0) {
                        topViewHolder.name.setText(MyUtils.currentCityZh);
                        JSONArray cityJSONArray;
                        try {
                            cityJSONArray = NetService.getCityAsJSONArray(SelectCityActivity.this, "city.json");
                            for (int i = 0; i < cityJSONArray.length(); i++) {
                                JSONObject object = cityJSONArray
                                        .getJSONObject(i);
                                Iterator<String> iterator = object.keys();
                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    String value = object.getString(key);
                                    if (value.contains(MyUtils.currentCityZh)) {
                                        locationCityID = key;
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
                } else if (viewType == 1) {// 非定位
                    if (convertView == null) {
                        convertView = inflater.inflate(
                                R.layout.item_selectcity_first, null);
                        holder = new ViewHolder();
                        holder.name = (TextView) convertView
                                .findViewById(R.id.tv_selectcity_first);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }

                    // 职系设置显示数据
                    if (position >= 1) {
                        if (dataArrayList.get(position - 1).get("value")
                                .contains("#")) {
                        } else {
                            holder.name.setText(dataArrayList.get(position - 1)
                                    .get("value"));
                            if (zhixiashiORhotcity.contains(dataArrayList.get(
                                    position - 1).get("value"))) {
                                holder.name
                                        .setCompoundDrawablesWithIntrinsicBounds(
                                                null, null, null, null);
                            }
                        }
                    }
                }
            } else {// 二级目录
                if (convertView == null) {
                    convertView = inflater.inflate(
                            R.layout.item_selectcity_second, null);
                    holder = new ViewHolder();
                    holder.name = (TextView) convertView
                            .findViewById(R.id.tv_selectcity_first);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        null, null);
                holder.name.setText(dataArrayList.get(position).get("value")
                        .trim());
            }
            return convertView;
        }

        private class ViewHolder {
            TextView name; // 城市名字
        }

        private class TopViewHolder {
            TextView name; // 城市名字
        }

    }
}
