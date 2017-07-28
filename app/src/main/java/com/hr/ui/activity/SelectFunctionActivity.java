package com.hr.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SelectFunctionFirstAdapter;
import com.hr.ui.adapter.SelectFunctionSecondAdapter;
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

public class SelectFunctionActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_selectfunction_second;
    private ListView lv_selectfunction_first;
    private TextView tv_selectfunction_save;
    private TextView tv_selectfunction_functioninfo;
    private TextView tv_selectfunction_num;
    private SelectFunctionFirstAdapter sfAdapter;
    private SelectFunctionSecondAdapter ssAdapter;
    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> data2;
    private HashMap<String, String> dataItem;
    private HashMap<String, String> functionData;
    private Context mContext = this;
    private ArrayList<HashMap<String, String>> totalList;
    /**
     * 存储职位信息
     */
    private StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_function);
        initView();
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() throws Exception {
//        data = new ArrayList<HashMap<String, String>>();
//        HashMap<String,String> map = new HashMap<>();
//        map.put("value","1");
//        data.add(map);
        data = setData("post", null, "职能");
        System.out.println("====data" + data.toString());
        sfAdapter = new SelectFunctionFirstAdapter(data, mContext);
        lv_selectfunction_first.setAdapter(sfAdapter);
        totalList = new ArrayList<>();
        sb = new StringBuffer();
        functionData = new HashMap<>();
        lv_selectfunction_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataItem = data.get(position);
                String value = dataItem.get("value");
                String id2 = dataItem.get("key");
                try {
                    data2 = setData("post", id2, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*
                更改显示
                 */
                ssAdapter = new SelectFunctionSecondAdapter(data2, mContext);
                lv_selectfunction_second.setAdapter(ssAdapter);
                lv_selectfunction_second.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (totalList.size() < 3) {
                            totalList.add(data2.get(position));
                            sb.append(data2.get(position).get("value") + " ");
                            tv_selectfunction_functioninfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            tv_selectfunction_functioninfo.setTextColor(Color.parseColor("#F39D0D"));
                            tv_selectfunction_functioninfo.setText(sb.toString());
                            tv_selectfunction_num.setText(totalList.size() + "");
                            Toast.makeText(mContext, data2.get(position).get("value"), Toast.LENGTH_SHORT).show();
                            functionData.put(data2.get(position).get("key"), data2.get(position).get("value"));
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        lv_selectfunction_first = (ListView) findViewById(R.id.lv_selectfunction_first);
        lv_selectfunction_second = (ListView) findViewById(R.id.lv_selectfunction_second);
        tv_selectfunction_save = (TextView) findViewById(R.id.tv_selectfunction_save);
        tv_selectfunction_functioninfo = (TextView) findViewById(R.id.tv_selectfunction_functioninfo);
        tv_selectfunction_num = (TextView) findViewById(R.id.tv_selectfunction_num);
        tv_selectfunction_save.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_function, menu);
        return true;
    }

    private ArrayList<HashMap<String, String>> setData(String filter, String id, String value) throws Exception {
        ArrayList<HashMap<String, String>> data = null;
        if (id == null) { // 一级分类
            if ("post".equals(filter)) {
                SharedPreferences sp = getSharedPreferences(
                        Constants.PREFS_NAME, Context.MODE_PRIVATE);
                int industryId = sp.getInt(Constants.INDUSTRY,
                        Constants.INDUSTRY_BUILD_ID);// 默认为建筑
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供的数据
                    JSONArray jobJSONArray = NetService.getJobAsJSONArray(mContext,
                            "job.json", String.valueOf(industryId));
                    data = NetService.getJobArray(jobJSONArray, "0");
                } else {// 使用程序内置数据
                    InputStream in = getAssets().open("job.json");
                    JSONObject jsonObject = new JSONObject(
                            NetUtils.readAsString(in, Constants.ENCODE));
                    JSONArray jonArray = jsonObject.getJSONArray(industryId
                            + "");
                    data = NetService.getJobArray(jonArray, "0");
                }
            }
        } else {
            if ("post".equals(filter)) {
                SharedPreferences sp = getSharedPreferences(
                        Constants.PREFS_NAME, Context.MODE_PRIVATE);
                int industryId = sp.getInt(Constants.INDUSTRY,
                        Constants.INDUSTRY_BUILD_ID);// 默认为建筑
                if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供的数据
                    JSONArray jobJSONArray = NetService.getJobAsJSONArray(this,
                            "job.json", String.valueOf(industryId));
                    data = NetService.getJobArray(jobJSONArray, id);
                } else {// 使用程序内置数据
                    InputStream in = getAssets().open("job.json");
                    JSONObject jsonObject = new JSONObject(
                            NetUtils.readAsString(in, Constants.ENCODE));
                    JSONArray jonArray = jsonObject.getJSONArray(industryId
                            + "");
                    data = NetService.getJobArray(jonArray, id);
                }
            }
        }
        // 增加”所有职能“选项
        HashMap<String, String> item = new HashMap<String, String>();
        if (id == null) {
            item.put("key", "0");
            item.put("value", "全部职能");
            data.add(0, item);
        } else {
            item.put("key", id);
            item.put("value", value);
            data.add(0, item);
        }
        return data;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selectfunction_save:
                PagerPostSearchFragment.setFunctionSelectMap(functionData);
                finish();
                break;
        }
    }
}
