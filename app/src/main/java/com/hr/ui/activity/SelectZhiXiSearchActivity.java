package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.fragment.PagerPostSearchFragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职系选择（可多选）
 */
public class SelectZhiXiSearchActivity extends Activity implements
        OnClickListener, OnItemClickListener {
    private ListView functionzhixiselect_listview;
    private TextView post_searchzhixi_confirm;
    private ImageView iv_searchzhixi_back;
    private MyBaseAdapterFindJobFunctionZhiXiSelect adapter;
    private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
    private Map<String, String> selectedMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_zhi_xi_search);
        Bundle bundle = getIntent().getExtras();
        final String filter = bundle.getString("filter");
        final String id = bundle.getString("id");
        final String value = bundle.getString("value");
        Map<String, String> tempMap = (Map<String, String>) bundle
                .getSerializable("selectMap");
        if (tempMap != null) {
            if (id == null) {
                this.selectedMap.clear();
            }
            if (tempMap.size() > 0) {
                this.selectedMap.putAll(tempMap);
            }
        }

        functionzhixiselect_listview = (ListView) findViewById(R.id.functionzhixiselect_listview);
        iv_searchzhixi_back = (ImageView) findViewById(R.id.iv_searchzhixi_back);
        post_searchzhixi_confirm = (TextView) findViewById(R.id.post_searchzhixi_confirm);
        post_searchzhixi_confirm.setOnClickListener(this);
        initData();

        adapter = new MyBaseAdapterFindJobFunctionZhiXiSelect(this, dataList, selectedMap);
        functionzhixiselect_listview.setAdapter(adapter);
        functionzhixiselect_listview.setOnItemClickListener(this);
        iv_searchzhixi_back.setOnClickListener(this);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_searchzhixi_back:
                finish();
                break;
            case R.id.post_searchzhixi_confirm:
                // 设置搜索页信息
                PagerPostSearchFragment.setZhiXiSelected(selectedMap);
                selectedMap.clear();
                finish();
                break;
            default:
                break;
        }
    }

    private void initData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "10100");
        map.put("value", "医生");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("key", "10200");
        map2.put("value", "药师");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("key", "10300");
        map3.put("value", "护士");

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("key", "10400");
        map4.put("value", "技师");

        dataList.add(map);
        dataList.add(map2);
        dataList.add(map3);
        dataList.add(map4);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        Map<String, String> item = (Map<String, String>) ((MyBaseAdapterFindJobFunctionZhiXiSelect) arg0
                .getAdapter()).getItem(arg2);

        if (selectedMap.containsKey(item.get("key"))) {
            selectedMap.remove(item.get("key"));
        } else {
            selectedMap.put(item.get("key"), item.get("value"));
        }
        adapter.setSelectedMap(selectedMap);
        adapter.notifyDataSetChanged();
    }

    /**
     * 职系适配器
     *
     */
    class MyBaseAdapterFindJobFunctionZhiXiSelect extends BaseAdapter {
        Context context;
        List<Map<String, String>> data;
        Map<String, String> selectedMap;

        public Map<String, String> getSelectedMap() {
            return selectedMap;
        }

        public void setSelectedMap(Map<String, String> selectedMap) {
            this.selectedMap = selectedMap;
        }

        public MyBaseAdapterFindJobFunctionZhiXiSelect(Context context,
                                                       List<Map<String, String>> data, Map<String, String> selectedMap) {
            this.context = context;
            this.data = data;
            this.selectedMap = selectedMap;
        }

        @Override
        public Map<String, String> getItem(int position) {
            // TODO Auto-generated method stub
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.v1_jobthird_item, null);
                viewHolder = new ViewHolder();
                viewHolder.v1_zhixi_name = (TextView) convertView
                        .findViewById(R.id.v1_zhixi_name);
                viewHolder.v1_item_select = (ImageView) convertView
                        .findViewById(R.id.v1_item_select);
                viewHolder.v1_list_item = (RelativeLayout) convertView
                        .findViewById(R.id.v1_list_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Map<String, String> item = getItem(position);
            viewHolder.v1_zhixi_name.setText(item.get("value"));

            if (selectedMap.containsKey(item.get("key"))) {
                viewHolder.v1_item_select.setVisibility(View.VISIBLE);
            } else {
                viewHolder.v1_item_select.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView v1_zhixi_name;
            ImageView v1_item_select;
            RelativeLayout v1_list_item;

        }
    }
}

