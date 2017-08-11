package com.hr.ui.view.custom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.SearchJobResultActivity;
import com.hr.ui.adapter.PopupFiltrateListViewAdapter;
import com.hr.ui.adapter.SelectFunctionFirstAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PopupmenuBar extends PopupWindow implements View.OnClickListener {


    private static final String TAG = "PopupmenuBar";
    private Context mContext;
    // 弹窗子类项选中时的监听
    // 定义列表对象
    private ListView mListViewFirst, mListViewSecond;
    private View myView;
    private TextView textView, tv_popupwindow_cancel;
    private ImageView imageView_main_me_order_arraw_down;
    private HashMap<String, String> mapFirst;
    private ArrayList<HashMap<String, String>> listFirst;
    private HashMap<String, String> filterMap;
    private TextView tv_popupwindow_confirm;
    /**
     * 存放请求参数的HashMap
     */
    private HashMap<String, String> postParam = new HashMap<>();
    private SearchJobResultActivity searchJobResultActivity;
    private SelectFunctionFirstAdapter firstAdapter;

    private HashMap<String, String> map1;
    private HashMap<String, String> map2;
    private HashMap<String, String> map3;
    private HashMap<String, String> map4;
    private HashMap<String, String> map5;
    private HashMap<String, String> map6;

    private String timeStr[] = {"不限", "近一天","近两天","近三天", "近一周","近两周" ,"近一个月", "近六周","近两月"};
    private String timeStrIds[] = {"0", "1", "2", "3", "7", "14","30","42","60"};
    private String comStr[] = {"不限", "1-49人", "50-99人", "100-499人", "500-999人", "1000人以上"};
    private String comStrIds[] = {"0", "12", "13", "14", "15", "16"};
    private String expStr[] = {"不限", "无要求", "应届生", "一年以上", "三年以上", "五年以上", "八年以上"};
    private String expStrIds[] = {"0", "10", "11", "13", "15", "17", "20"};
    private String eduStr[] = {"不限", "无要求", "大专以下", "大专及以上", "本科及以上", "硕士及以上", "博士及以上"};
    private String eduStrIds[] = {"0", "10", "11", "15", "16", "17", "18"};
    private String natureStr[] = {"不限", "无要求", "实习", "兼职", "全职"};
    private String natureStrIds[] = {"0", "14", "11", "12", "13"};
    private String salStr[] = {"不限", "2000以下", "2000-4000", "4000-6000", "6000-8000","8000-10000", "10000-15000", "15000-20000","20000-30000","30000-50000","50000以上"};
    private String salStrIds[] = {"0","10","11", "12", "13", "14","15", "16", "17", "18","19"};
    private PopupFiltrateListViewAdapter expAdapter, comAdapter, timeAdapter, eduAdapter, natureAdapter, salAdapter;
    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public PopupmenuBar(Context context, int layoutResId, int listViewResIdFirst, int listViewResIdSecond,
                        final int textColor, int width, int height, SearchJobResultActivity searchJobResultActivity, HashMap<String, String> filterMap) {
        this.mContext = context;
        this.searchJobResultActivity = searchJobResultActivity;
        this.filterMap = filterMap;

        // 设置可以获得焦点，默认为false
        setFocusable(true);
        // 设置弹窗内可点击，默认为true
        setTouchable(true);
        // 设置弹窗外可点击，默认为false
        setOutsideTouchable(true);
        // 必须增加背景，否则无法让菜单消失
        setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);
        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(layoutResId, null));
        mListViewFirst = (ListView) getContentView().findViewById(listViewResIdFirst);
        mListViewSecond = (ListView) getContentView().findViewById(listViewResIdSecond);
        initView();
        // 设置菜单列表项的适配器0
        initData();
        refreshPop();
    }

    private void initView() {
        tv_popupwindow_confirm = (TextView) getContentView().findViewById(R.id.tv_popupwindow_confirm);
        tv_popupwindow_confirm.setOnClickListener(this);
        tv_popupwindow_cancel = (TextView) getContentView().findViewById(R.id.tv_popupwindow_cancel);
        tv_popupwindow_cancel.setOnClickListener(this);
    }

    private void initData() {

        map1 = new HashMap<String, String>();
        map1.put("value", "发布时间");
        map1.put("valueMark", "");
        map2 = new HashMap<String, String>();
        map2.put("value", "工作经验");
        map2.put("valueMark", "");
        map3 = new HashMap<String, String>();
        map3.put("value", "学历要求");
        map3.put("valueMark", "");
        map4 = new HashMap<String, String>();
        map4.put("value", "公司规模");
        map4.put("valueMark", "");
        map5 = new HashMap<String, String>();
        map5.put("value", "薪资待遇");
        map5.put("valueMark", "");
        map6 = new HashMap<String, String>();
        map6.put("value", "工作性质");
        map6.put("valueMark", "");

        listFirst = new ArrayList<>();
        listFirst.add(map1);
        listFirst.add(map2);
        listFirst.add(map3);
        listFirst.add(map4);
        listFirst.add(map5);
        listFirst.add(map6);
        firstAdapter = new SelectFunctionFirstAdapter(listFirst, mContext);


        eduAdapter = new PopupFiltrateListViewAdapter(arrToList(eduStr, eduStrIds), mContext);
        expAdapter = new PopupFiltrateListViewAdapter(arrToList(expStr, expStrIds), mContext);
        timeAdapter = new PopupFiltrateListViewAdapter(arrToList(timeStr, timeStrIds), mContext);
        comAdapter = new PopupFiltrateListViewAdapter(arrToList(comStr, comStrIds), mContext);
        natureAdapter = new PopupFiltrateListViewAdapter(arrToList(natureStr, natureStrIds), mContext);
        salAdapter = new PopupFiltrateListViewAdapter(arrToList(salStr, salStrIds), mContext);

    }


    /**
     * 初始化ListViewFirst1的数据
     */
    private void refreshPop() {
        mListViewFirst.setAdapter(firstAdapter);
        Log.i(TAG, "=====popupList" + listFirst.toString());
        mListViewFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mListViewSecond.setAdapter(timeAdapter);
                        if (filterMap.get("filter_issuedate") != null && !filterMap.get("filter_issuedate").equals("")) {
                            timeAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_issuedate")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                timeAdapter.setselectedPosition(position);
                                postParam.put("filter_issuedate", arrToList(timeStr, timeStrIds).get(position).get("id"));
                                postParam.put("filter_issuedate_value", arrToList(timeStr, timeStrIds).get(position).get("value"));
                                filterMap.put("filter_issuedate", position + "");

                                listFirst.get(0).put("valueMark", arrToList(timeStr, timeStrIds).get(position).get("value"));

                                firstAdapter.notifyDataSetChanged();
                                timeAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 1:
                        mListViewSecond.setAdapter(expAdapter);
                        if (filterMap.get("filter_workyear") != null && !filterMap.get("filter_workyear").equals("")) {
                            expAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_workyear")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                expAdapter.setselectedPosition(position);
                                postParam.put("filter_workyear", arrToList(expStr, expStrIds).get(position).get("id"));
                                postParam.put("filter_workyear_value", arrToList(expStr, expStrIds).get(position).get("value"));
                                filterMap.put("filter_workyear", position + "");

                                listFirst.get(1).put("valueMark", arrToList(expStr, expStrIds).get(position).get("value"));
                                firstAdapter.notifyDataSetChanged();
                                expAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 2:
                        mListViewSecond.setAdapter(eduAdapter);
                        if (filterMap.get("filter_study") != null && !filterMap.get("filter_study").equals("")) {
                            eduAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_study")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                eduAdapter.setselectedPosition(position);
                                postParam.put("filter_study", arrToList(eduStr, eduStrIds).get(position).get("id"));
                                postParam.put("filter_study_value", arrToList(eduStr, eduStrIds).get(position).get("value"));
                                filterMap.put("filter_study", position + "");
                                listFirst.get(2).put("valueMark", arrToList(eduStr, eduStrIds).get(position).get("value"));
                                firstAdapter.notifyDataSetChanged();
                                eduAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 3:
                        mListViewSecond.setAdapter(comAdapter);
                        if (filterMap.get("filter_stuffmunber") != null && !filterMap.get("filter_stuffmunber").equals("")) {
                            comAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_stuffmunber")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                comAdapter.setselectedPosition(position);
                                postParam.put("filter_stuffmunber", arrToList(comStr, comStrIds).get(position).get("id"));
                                postParam.put("filter_stuffmunber_value", arrToList(comStr, comStrIds).get(position).get("value"));
                                filterMap.put("filter_stuffmunber", position + "");
                                listFirst.get(3).put("valueMark", arrToList(comStr, comStrIds).get(position).get("value"));
                                firstAdapter.notifyDataSetChanged();
                                comAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 4:
                        mListViewSecond.setAdapter(salAdapter);
                        if (filterMap.get("filter_salary") != null && !filterMap.get("filter_salary").equals("")) {
                            salAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_salary")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                salAdapter.setselectedPosition(position);
                                postParam.put("filter_salary", arrToList(salStr, salStrIds).get(position).get("id"));
                                postParam.put("filter_salary_value", arrToList(salStr, salStrIds).get(position).get("value"));
                                filterMap.put("filter_salary", position + "");

                                listFirst.get(4).put("valueMark", arrToList(salStr, salStrIds).get(position).get("value"));
                                firstAdapter.notifyDataSetChanged();
                                salAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 5:
                        mListViewSecond.setAdapter(natureAdapter);
                        if (filterMap.get("filter_worktype") != null && !filterMap.get("filter_worktype").equals("")) {
                            natureAdapter.setselectedPosition(Integer.parseInt(filterMap.get("filter_worktype")));
                        }
                        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                natureAdapter.setselectedPosition(position);
                                postParam.put("filter_worktype", arrToList(natureStr, natureStrIds).get(position).get("id"));
                                postParam.put("filter_worktype_value", arrToList(natureStr, natureStrIds).get(position).get("value"));
                                filterMap.put("filter_worktype", position + "");

                                listFirst.get(5).put("valueMark", arrToList(natureStr, natureStrIds).get(position).get("value"));
                                firstAdapter.notifyDataSetChanged();
                                natureAdapter.notifyDataSetChanged();
                            }
                        });
                        break;
                }
            }
        });
    }

    // 显示弹窗列表界面
    public void show(View view) {
        // 显示弹窗的位置
        if (isShowing()) {
            dismiss();
//			imageView_main_me_order_arraw_down.setImageResource(R.drawable.arraw_up);
        } else {
            // 在哪个view的下方显示popupWindow菜单
            showAsDropDown(view, 0, 0);
        }
    }

    /**
     * 给listviewSecond初始化用
     */
    private ArrayList<HashMap<String, String>> arrToList(String[] str, String[] strId) {
        ArrayList<HashMap<String, String>> popupList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < str.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("value", str[i]);
            map.put("id", strId[i]);
            popupList.add(map);
        }
        return popupList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_popupwindow_confirm:
                /**
                 * 待解决
                 */
                searchJobResultActivity.getFiltrate(postParam, filterMap);
                dismiss();
                break;
            case R.id.tv_popupwindow_cancel:
                //取消选择条件
                postParam.clear();
                filterMap.clear();
                searchJobResultActivity.getFiltrate(postParam, filterMap);
                Toast.makeText(mContext, "已清空筛选条件", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }
}
