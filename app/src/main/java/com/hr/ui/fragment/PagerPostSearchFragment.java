package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.activity.MySelectFuncitonActivity;
import com.hr.ui.activity.SearchJobResultActivity;
import com.hr.ui.activity.SelectFunctionSearchActivity;
import com.hr.ui.activity.SelectZhiXiSearchActivity;
import com.hr.ui.adapter.IndustryRecAdapter2;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.HistoyInfo;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.OptimizeLVSV;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 职位搜索
 * A simple {@link Fragment} subclass.
 */
public class PagerPostSearchFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "PagerPostSearchFragment";
    private View view;
    private RelativeLayout rl_post_function;
    private static TextView tv_post_function;
    private static RelativeLayout rl_post_medical;
    private static TextView tv_post_medical;
    private Button bt_post_search;
    private LinearLayout rl_post_clear;
    private ImageView iv_post_net;
    private SharedPreferencesUtils sUtils;
    private String functionId = "";// 职能Id

    private RecyclerView lv_postsearch_advertistment;
    /**
     * 数据库
     */
    private DAO_DBOperator db;
    HistoyInfo histoyInfo = new HistoyInfo();
    HistoyInfo[] histoyInfo2;// 查询結果集合
    /**
     * 历史记录相关控件
     */
    TextView tv_postsearch_text1, tv_postsearch_text2, tv_postsearch_text3, tv_postsearch_text4;
    RelativeLayout rl_postsearch_text1, rl_postsearch_text2, rl_postsearch_text3, rl_search_post_clear;
    /**
     * 城市名
     */
    private static String cityName;

    private int industryId;
    /**
     * 城市ID
     */
    private static String placeId;
    /**
     * 职能选择集合
     */
    private static List<FunctionBean> functionBeanList=new ArrayList<>();

    /**
     * 职系选择集合
     */
    private static HashMap<String, String> zhixiSelectMap = new HashMap<>();
    /**
     * 显示企业推荐的列表
     */
    private Industry industry;
    private ArrayList<Industry> rec_data;// 企业推荐数据
    private String json_result;// 网络获取的json数据集合
    private int error_code;// 异常返回值
    private IndustryRecAdapter2 industryRecAdapter2;
    private boolean request_type = true;
    int DB_strid = -3;
    private Context context;
    private int industry_id;
    private RefleshDialogUtils dialogUtils;


    // 显示广告的图片
    private ArrayList<Industry> ad_data;
    /**
     * 更新UI
     */
    private Handler handlerUI = new Handler() {
        public void handleMessage(Message msg) {
            if (request_type) {
                if (msg.what == 1001) {// 获取数据成功
                    if (msg.arg1 == 0) {// 数据获取成功，并解析没有错误
                        if (rec_data != null && rec_data.size() > 0) {
                            industryRecAdapter2 = new IndustryRecAdapter2(getActivity(), rec_data);
                            lv_postsearch_advertistment.setAdapter(industryRecAdapter2);
                            /*OptimizeLVSV.setListViewHeightBasedOnChildren(lv_postsearch_advertistment);*/
                            industryRecAdapter2.setOnItemClick(new OnItemClick() {
                                @Override
                                public void ItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), CompanyParticularActivity.class);
                                    intent.putExtra("Enterprise_id", rec_data.get(position).getEnterprise_id());
                                    startActivity(intent);
                                }
                            });
                            lv_postsearch_advertistment.setFocusable(false);
                        }
                    } else if (msg.arg1 == 206) {
                        Log.i("SearchJobActivity", "handlerUI 执行失败");
                    } else if (msg.arg1 == 11) {
                        Toast.makeText(getActivity(), getString(R.string.error_notnet), Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == 1002) {// 获取数据失败
                    Log.i("SearchJobActivity", "handlerUI 获取企业推荐数据失败");
                }
            }
        }

        ;
    };
    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                dialogUtils.dismissDialog();
                json_result = (String) msg.obj;
                Log.i("SearchJobActivity", "======== json_result" + json_result.toString());
                // 1001 成功 1002失败
                Message message = Message.obtain();
                try {
                    message.what = 1001;
                    message.arg1 = json();
                    Log.i("SearchJobActivity", "======== message.arg1" + message.arg1);
                    handlerUI.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = 1002;
                    handlerUI.sendMessage(message);
                }
            } else {
                dialogUtils.dismissDialog();
                Message message = Message.obtain();
                message.what = 1002;
                handlerUI.sendMessage(message);
            }
        }

        ;
    };
    @SuppressLint("ValidFragment")
    public PagerPostSearchFragment(Context context, String cityName, String placeId) {
        this.context = context;
        this.cityName=cityName;
        this.placeId=placeId;

    }

    @SuppressLint("ValidFragment")
    public PagerPostSearchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_post_search, container, false);
        dialogUtils=new RefleshDialogUtils(getActivity());
        ad_data = new ArrayList<Industry>();
        rec_data = new ArrayList<Industry>();
        db = new DAO_DBOperator(getActivity());
        initView();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void initView() {
        sUtils = new SharedPreferencesUtils(getActivity());
        industryId = sUtils.getIntValue(Constants.INDUSTRY, Constants.INDUSTRY_BUILD_ID);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_postsearch_advertistment = (RecyclerView) view.findViewById(R.id.lv_postsearch_advertistment);
        lv_postsearch_advertistment.setLayoutManager(manager);
        lv_postsearch_advertistment.addItemDecoration(new SpacesItemDecoration(1));
        /*
        历史记录相关
         */
        tv_postsearch_text1 = (TextView) view.findViewById(R.id.tv_postsearch_text1);
        tv_postsearch_text2 = (TextView) view.findViewById(R.id.tv_postsearch_text2);
        tv_postsearch_text3 = (TextView) view.findViewById(R.id.tv_postsearch_text3);

        rl_postsearch_text1 = (RelativeLayout) view.findViewById(R.id.rl_postsearch_text1);
        rl_postsearch_text2 = (RelativeLayout) view.findViewById(R.id.rl_postsearch_text2);
        rl_postsearch_text3 = (RelativeLayout) view.findViewById(R.id.rl_postsearch_text3);
//        rl_postsearch_text4 = (RelativeLayout) view.findViewById(R.id.rl_postsearch_text4);
        rl_search_post_clear = (RelativeLayout) view.findViewById(R.id.rl_search_post_clear);

        rl_postsearch_text1.setOnClickListener(this);
        rl_postsearch_text2.setOnClickListener(this);
        rl_postsearch_text3.setOnClickListener(this);
//        rl_postsearch_text4.setOnClickListener(this);
        rl_search_post_clear.setOnClickListener(this);

        iv_post_net = (ImageView) view.findViewById(R.id.iv_post_net);
        rl_post_function = (RelativeLayout) view.findViewById(R.id.rl_post_function);
        tv_post_function = (TextView) view.findViewById(R.id.tv_post_function);
        rl_post_medical = (RelativeLayout) view.findViewById(R.id.rl_post_medical);
        tv_post_medical = (TextView) view.findViewById(R.id.tv_post_medical);
        bt_post_search = (Button) view.findViewById(R.id.bt_post_search);
        rl_post_clear = (LinearLayout) view.findViewById(R.id.rl_post_clear);

        rl_post_medical.setOnClickListener(this);
        rl_post_function.setOnClickListener(this);
        bt_post_search.setOnClickListener(this);

        switch (industryId) {
            case 11:// 建筑
                iv_post_net.setImageResource(R.mipmap.net11);
                break;
            case 12:// 金融
                iv_post_net.setImageResource(R.mipmap.net12);
                break;
            case 14:// 医药
//                rl_post_medical.setVisibility(View.VISIBLE);
                iv_post_net.setImageResource(R.mipmap.net14);
                break;
            case 26:// 服装
                iv_post_net.setImageResource(R.mipmap.net26);
                break;
            case 29:// 化工
                iv_post_net.setImageResource(R.mipmap.net29);
                break;
            case 15:// 教培
                iv_post_net.setImageResource(R.mipmap.net15);
                break;
            case 22:// 机械
                iv_post_net.setImageResource(R.mipmap.net22);
                break;
            case 19:// 电子
                iv_post_net.setImageResource(R.mipmap.net19);
                break;
            case 13:// 传媒
                iv_post_net.setImageResource(R.mipmap.net13);
                break;
            case 30:// 旅游
                iv_post_net.setImageResource(R.mipmap.net30);
                break;
            case 40:// 酒店餐饮
                iv_post_net.setImageResource(R.mipmap.net40);
                break;
            case 20:// 电力
                iv_post_net.setImageResource(R.mipmap.net20);
                break;
            case 23:// IT
                iv_post_net.setImageResource(R.mipmap.net23);
                break;
            case 16:// 物流
                iv_post_net.setImageResource(R.mipmap.net16);
                break;
            case 21:// 通信
                iv_post_net.setImageResource(R.mipmap.net21);
                break;
        }
        functionBeanList.clear();
    }

    private int json() {
        rec_data.clear();
        ad_data.clear();
        try {
            JSONObject jsonObject = new JSONObject(json_result);
            error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("list");
            int count = jsonArray.length();
            for (int i = 0; i < count; i++) {
                JSONObject obj = (JSONObject) jsonArray.opt(i);
                industry = new Industry();
                industry.setA_id(obj.getInt("a_id"));
                industry.setC_id(obj.getInt("c_id"));
                industry.setTitle(obj.getString("title"));
                industry.setTopic_type(obj.getInt("topic_type"));
                industry.setTopic_url(obj.getString("topic_url"));
                industry.setEnterprise_id(obj.getString("enterprise_id"));
                industry.setDescribe(obj.getString("ad_txt"));
                industry.setPic_path(obj.getString("pic_path"));
                industry.setPic_s_path(obj.getString("pic_s_path"));
                industry.setClick_num(obj.getInt("click_num"));
                industry.setEdit_time(obj.getInt("edit_time"));
                industry.setCompany_type(obj.getString("company_type"));
                industry.setStuff_munber(obj.getString("stuff_munber"));
                if (request_type) {
                    rec_data.add(industry);
                } else {
                    ad_data.add(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error_code;
    }

    /**
     * 初始化数据
     */
    private void initData() {

        industry_id = sUtils.getIntValue(Constants.INDUSTRY, 11);
        // 首先加载企业推荐数据
        dialogUtils.showDialog();
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(getData(3));
    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    private HashMap<String, String> getData(int type) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "mobilead.list");
        params.put("a_id", "");
        params.put("c_id", "");
        params.put("c_type", "" + type);

        params.put("industry", "" + industry_id);
        params.put("page", "");
        params.put("page_nums", "10");
        return params;
    }


    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_post_function:
                // 加载职能选择页
                Intent function = new Intent(getActivity(), MySelectFuncitonActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("filter", "post");
                bundle.putSerializable("selectMap", (Serializable) functionBeanList);
                bundle.putString("value", "职能");
                function.putExtras(bundle);
                startActivity(function);
                break;
            case R.id.bt_post_search:
                placeId = CityNameConvertCityID.convertCityID(getActivity(), cityName);
                if (tv_post_function.getText().toString().trim().equals("全部职能") || tv_post_function.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "请选择职能", Toast.LENGTH_SHORT).show();
                } else {
                    saveHistory();
                    initactivity();
                }
                break;
            case R.id.rl_post_medical:
                // 加载职系选择页
                Intent zhixi = new Intent(getActivity(), SelectZhiXiSearchActivity.class);
                zhixi.putExtra("TAG", "ZhiXiSelectActivityFindJob");
                zhixi.putExtra("selectMap", (Serializable) zhixiSelectMap);
                startActivity(zhixi);
                break;
            case R.id.rl_postsearch_text1:
                Histoy_initactivity(tv_postsearch_text1, 1);
                break;
            case R.id.rl_postsearch_text2:
                Histoy_initactivity(tv_postsearch_text2, 2);
                break;
            case R.id.rl_postsearch_text3:
                Histoy_initactivity(tv_postsearch_text3, 3);
                break;
            case R.id.rl_search_post_clear:
                boolean bool = db.Delete_SearchHistory(MyUtils.industryId + "");
                if (bool == true) {
                    Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(getActivity(), "清除失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 加载职系选择列表
     */
    public static void setFunctionSelectMap(List<FunctionBean> list) {
        functionBeanList.clear();
        zhixiSelectMap.clear();
        functionBeanList.addAll(list);
        showZhixiLayoutOrNot();
        showText();
        showZhiXi();
    }

    /**
     *
     */
    private static void showZhiXi() {// 职系搜索
        Set<String> keySet = zhixiSelectMap.keySet();
        if (zhixiSelectMap.size() == 0) {
            tv_post_medical.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String keyString = (String) iterator.next();
            buffer.append(zhixiSelectMap.get(keyString).trim() + "、");
        }
        tv_post_medical.setText(buffer.toString()
                .subSequence(0, buffer.length() - 1).toString().trim());
    }

    /**
     * 显示文本信息
     */
    private static void showText() {
        if (functionBeanList.size() == 0) {
            tv_post_function.setText("全部职能");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<functionBeanList.size();i++ ) {
            String keyString = functionBeanList.get(i).getName();
            buffer.append(keyString + "、");
        }
        tv_post_function.setText(buffer.toString()
                .subSequence(0, buffer.length() - 1).toString().trim());
    }

    /**
     * 设置地点
     *
     * @param value
     */
    public static void setPlaceText(String value) {
        // TODO Auto-generated method stub
        cityName = value;
    }

    /**
     * 设置城市ID
     *
     * @param string
     */
    public static void setPlaceId(String string) {
        // TODO Auto-generated method stub
        placeId = string;
    }

    /**
     * 加载找工作页面
     */
    private void initactivity() {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
//        bundle.putString("searchword", activity_searchjob_searchtext.getText()
//                .toString().trim());
        // 获取职能id，有职系就拼接 funcID|职系ID
        if (functionBeanList != null) {
            StringBuffer funcidBuffer = new StringBuffer();
            for (int i=0;i<functionBeanList.size();i++) {
                String string = functionBeanList.get(i).getId();
                if (zhixiSelectMap != null && zhixiSelectMap.size() > 0) {
                    if ((string.contains("263") || string.contains("264")
                            || string.contains("265") || string.contains("266")
                            || string.contains("267") || string.contains("268"))) {
                        for (Map.Entry<String, String> entry : zhixiSelectMap
                                .entrySet()) {
                            funcidBuffer.append(string + "|" + entry.getKey()
                                    + ",");
                        }
                    } else {
                        funcidBuffer.append(string + ",");
                    }
                } else {
                    funcidBuffer.append(string + ",");
                }
            }
            if (funcidBuffer.length() >= 1) {
                functionId = funcidBuffer.substring(0,
                        funcidBuffer.length() - 1);
            } else {
                functionId = "";
            }
            bundle.putString("funcid", functionId);
        }
        bundle.putString("areaid", MyUtils.selectCityId);
        bundle.putString(
                "industry", sUtils.getIntValue(Constants.INDUSTRY, 11) + "");// 默认建筑
        bundle.putString("funcName", tv_post_function.getText().toString());
        bundle.putString("areaName", MyUtils.selectCityZh);
        Intent intent2 = new Intent(getActivity(), SearchJobResultActivity.class);// 找工作界面
        intent2.putExtras(bundle);
        startActivity(intent2);
    }

    /**
     * 保存历史记录
     */
    private void saveHistory() {
        //Log.i("城市的名称和id",MyUtils.selectCityId+MyUtils.selectCityZh);
        String str_function = tv_post_function.getText().toString().trim();
        if ("全部职能".equals(str_function)) {
            str_function = "";
        }
        String str_place = cityName;
        if ("全国".equals(MyUtils.selectCityZh)) {
            str_place = "";
        }
        histoyInfo.setFunction_value(tv_post_function.getText().toString());
        histoyInfo.setIndustry_id(industryId + "");
        histoyInfo.setFunction_id("0".equals(functionId) ? "" : functionId);
        histoyInfo.setPlace_id(MyUtils.selectCityId);
        histoyInfo.setPlace_value(MyUtils.selectCityZh);
        int db_num = db.query_SearchHistory(industryId + "", str_function, str_place);
        if (db_num == 0) {
            DB_strid = db.Insert_SearchHistory(histoyInfo);
        } else {
            db.Delete_SearchHistory(industryId + "", str_function, str_place);
            DB_strid = db.Insert_SearchHistory(histoyInfo);
        }
    }

    protected void Histoy_initactivity(TextView textview, int i) {
        Bundle bundle = new Bundle();
        // 查询历史记录
        histoyInfo2 = db.query_SearchHistory(industryId + "");
        String strOut_Place_id = histoyInfo2[histoyInfo2.length - i].getPlace_id();
        String strOut_Function_id = histoyInfo2[histoyInfo2.length - i].getFunction_id();
        String strPlace_value = histoyInfo2[histoyInfo2.length - i].getPlace_value();
        String strFunction_value = histoyInfo2[histoyInfo2.length - i].getFunction_value();
//        String Out_searchword = histoyInfo2[histoyInfo2.length - i]
//                .getSearch_value();
        bundle.putString("industry", industryId + "");// 默认建筑
        bundle.putString("funcid", strOut_Function_id);
        bundle.putString("areaid", strOut_Place_id);
        bundle.putString("areaName", strPlace_value);
        bundle.putString("funcName", strFunction_value);
        Intent intent2 = new Intent(getActivity(), SearchJobResultActivity.class);
        intent2.putExtra("TAG", "SearchResultActivity");
        intent2.putExtras(bundle);
        startActivity(intent2);
    }

    /**
     * 更新Ui页面
     */
    public void updateUI() {
        histoyInfo2 = db.query_SearchHistory(sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
        Log.i(TAG, "====histoyInfo2" + histoyInfo2.length);
        if (histoyInfo2.length == 0) {
            rl_search_post_clear.setVisibility(View.GONE);
//            tv_postsearch_text1.setVisibility(View.GONE);
//            tv_postsearch_text2.setVisibility(View.GONE);
//            tv_postsearch_text4.setVisibility(View.GONE);
            rl_postsearch_text1.setVisibility(View.GONE);
            rl_postsearch_text2.setVisibility(View.GONE);
            rl_postsearch_text3.setVisibility(View.GONE);
//            rl_postsearch_text4.setVisibility(View.GONE);
        } else {
            rl_search_post_clear.setVisibility(View.VISIBLE);

            rl_postsearch_text1.setVisibility(View.GONE);
            rl_postsearch_text2.setVisibility(View.GONE);
            rl_postsearch_text3.setVisibility(View.GONE);
            if (histoyInfo2.length >= 1) {
                // 设置第一个搜索历史记录的值
                SetTextValue(tv_postsearch_text1, 1);
//                tv_postsearch_text1.setVisibility(View.VISIBLE);
                rl_postsearch_text1.setVisibility(View.VISIBLE);
            }
            if (histoyInfo2.length >= 2) {
                // 设置第二个搜索历史记录的值
                SetTextValue(tv_postsearch_text2, 2);
//                tv_postsearch_text2.setVisibility(View.VISIBLE);
                rl_postsearch_text2.setVisibility(View.VISIBLE);
            }
            if (histoyInfo2.length >= 3) {
                // 设置第三个搜索历史记录的值
                SetTextValue(tv_postsearch_text3, 3);
//                tv_postsearch_text3.setVisibility(View.VISIBLE);
                rl_postsearch_text3.setVisibility(View.VISIBLE);
            }
            if (histoyInfo2.length > 8) {
                for (int i = 8; i < histoyInfo2.length; i++) {
                    db.Delete_SearchHistory(i, MyUtils.industryId + "");
                }
            }
        }
    }

    /**
     * 判断textview显示的值
     */
    private void SetTextValue(TextView textView, int i) {
        // TODO Auto-generated method stub
        String Function_value = histoyInfo2[histoyInfo2.length - i].getFunction_value();
        String Place_value = histoyInfo2[histoyInfo2.length - i].getPlace_value();
        if (!"".equals(Function_value)) {
            Function_value = Function_value;
        }
        if (!"".equals(Place_value)) {
            Place_value = "+" + Place_value;
        }
        textView.setText(Function_value + Place_value);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialogUtils.dismissDialog();
    }

    /**
     * 是否有职系
     *
     * @param list
     * @return
     */
    private static boolean isHaveZhiXi(List<FunctionBean> list) {
        boolean flag = false;
        for (int i=0;i<list.size();i++) {
            String item = list.get(i).getId();
            flag = (item.contains("263") || item.contains("264")
                    || item.contains("265") || item.contains("266")
                    || item.contains("267") || item.contains("268"));
            if (flag)
                return flag;
        }
        return flag;
    }

    private static void showZhixiLayoutOrNot() {
        if (isHaveZhiXi(functionBeanList)) {
            rl_post_medical.setVisibility(View.VISIBLE);
        } else {
            rl_post_medical.setVisibility(View.GONE);
        }
    }

    public static void setZhiXiSelected(Map<String, String> selectedMap) {
        zhixiSelectMap.clear();
        zhixiSelectMap.putAll(selectedMap);
        showZhiXi();
    }
}
