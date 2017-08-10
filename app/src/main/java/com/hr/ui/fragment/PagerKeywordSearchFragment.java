package com.hr.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageRequest;
import com.hr.ui.R;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.activity.SearchJobResultActivity;
import com.hr.ui.adapter.IndustryRecAdapter;
import com.hr.ui.adapter.IndustryRecKeywordAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.Industry;
import com.hr.ui.model.KeyWorldHistory;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.OptimizeLVSV;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.speech.UtilityConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 关键词搜索页面
 * A simple {@link Fragment} subclass.
 */
public class PagerKeywordSearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PagerKeywordSearchFragment";
    private PopupWindow pop;
    private View view;
    private ImageView iv_keyword_net, iv_keyword_voice;
    //    private static TextView tv_keyword_city;
    private TextView tv_keyword_condition;
    private EditText et_keyword_keyword;
    private TextView iv_keyword_search;
    private RecyclerView lv_keyword_advertistment;
    private SharedPreferencesUtils sUtils;

    private ArrayList<HashMap<String, String>> listSearch;
    private TextView tv_keywordsearch_text1, tv_keywordsearch_text2, tv_keywordsearch_text3;
    private RelativeLayout rl_search_keyword_clear, rl_pager_keyword_search, rl_keywordsearch_text1, rl_keywordsearch_text2, rl_keywordsearch_text3, rl_keywordsearch_text4;
    private int industryId;

    private String wordtype = "1";
    private DAO_DBOperator db;
    private KeyWorldHistory keyWorldHistory = new KeyWorldHistory();
    private int DB_strid = -3;
    private KeyWorldHistory[] keyWorldHistory2;// 查询結果集合
    private boolean request_type = true;
    // 显示广告的图片
    private ArrayList<Industry> ad_data;
    private ImageRequest mRequest = null;
    private Context context;

    // 显示企业推荐的列表
    private Industry industry;
    private ArrayList<Industry> rec_data;// 企业推荐数据
    private String json_result;// 网络获取的json数据集合
    private int error_code;// 异常返回值
    private IndustryRecKeywordAdapter industryRecAdapter;
    /**
     * 城市名
     */
    private  String cityName;
    /**
     * 城市ID
     */
    private  String placeId;
    private int industry_id;


    /**
     * 更新UI
     */
    private Handler handlerUI = new Handler() {
        public void handleMessage(Message msg) {
            if (request_type) {
                if (msg.what == 1001) {// 获取数据成功
                    if (msg.arg1 == 0) {// 数据获取成功，并解析没有错误
                        if (rec_data != null && rec_data.size() > 0) {
                            Log.i("广告的数据",rec_data.toString());
                            industryRecAdapter = new IndustryRecKeywordAdapter(getActivity(), rec_data);
                            lv_keyword_advertistment.setAdapter(industryRecAdapter);
                            industryRecAdapter.setOnItemClick(new OnItemClick() {
                                @Override
                                public void ItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(),
                                            CompanyParticularActivity.class);
                                    intent.putExtra("Enterprise_id", rec_data.get(position).getEnterprise_id());
                                    startActivity(intent);
                                }
                            });
                           /* OptimizeLVSV.setListViewHeightBasedOnChildren(lv_keyword_advertistment);*/
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
                Message message = Message.obtain();
                message.what = 1002;
                handlerUI.sendMessage(message);
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_keyword_search, container, false);
        ad_data = new ArrayList<Industry>();
        rec_data = new ArrayList<Industry>();
        db = new DAO_DBOperator(getActivity());
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        updateUI();
    }




    private void initView() {
        sUtils = new SharedPreferencesUtils(getActivity());
        industryId = sUtils.getIntValue(Constants.INDUSTRY, Constants.INDUSTRY_BUILD_ID);
        iv_keyword_net = (ImageView) view.findViewById(R.id.iv_keyword_net);
//        tv_keyword_city = (TextView) view.findViewById(R.id.tv_keyword_city);
        tv_keyword_condition = (TextView) view.findViewById(R.id.tv_keyword_condition);
        et_keyword_keyword = (EditText) view.findViewById(R.id.et_keyword_keyword);
        iv_keyword_search = (TextView) view.findViewById(R.id.iv_keyword_search);
        iv_keyword_voice = (ImageView) view.findViewById(R.id.iv_keyword_voice);
//        lv_keyword_history = (ListView) view.findViewById(R.id.lv_keyword_history);
        lv_keyword_advertistment = (RecyclerView) view.findViewById(R.id.lv_keyword_advertistment);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_keyword_advertistment.setLayoutManager(linearLayoutManager);
        lv_keyword_advertistment.addItemDecoration(new SpacesItemDecoration(1));
        tv_keywordsearch_text1 = (TextView) view.findViewById(R.id.tv_keywordsearch_text1);
        tv_keywordsearch_text2 = (TextView) view.findViewById(R.id.tv_keywordsearch_text2);
        tv_keywordsearch_text3 = (TextView) view.findViewById(R.id.tv_keywordsearch_text3);
//        tv_keywordsearch_text4 = (TextView) view.findViewById(R.id.tv_keywordsearch_text4);

        rl_keywordsearch_text1 = (RelativeLayout) view.findViewById(R.id.rl_keywordsearch_text1);
        rl_keywordsearch_text2 = (RelativeLayout) view.findViewById(R.id.rl_keywordsearch_text2);
        rl_keywordsearch_text3 = (RelativeLayout) view.findViewById(R.id.rl_keywordsearch_text3);
//        rl_keywordsearch_text4 = (RelativeLayout) view.findViewById(R.id.rl_keywordsearch_text4);

        rl_pager_keyword_search = (RelativeLayout) view.findViewById(R.id.rl_pager_keyword_search);
        rl_search_keyword_clear = (RelativeLayout) view.findViewById(R.id.rl_search_keyword_clear);

        iv_keyword_search.setOnClickListener(this);
        rl_keywordsearch_text1.setOnClickListener(this);
        rl_keywordsearch_text2.setOnClickListener(this);
        rl_keywordsearch_text3.setOnClickListener(this);
        iv_keyword_voice.setOnClickListener(this);
//        rl_keywordsearch_text4.setOnClickListener(this);
        tv_keyword_condition.setOnClickListener(this);
        rl_search_keyword_clear.setOnClickListener(this);
//        tv_keyword_city.setOnClickListener(this);
        switch (industryId) {
            case 11:// 建筑
                iv_keyword_net.setImageResource(R.mipmap.net11);
                break;
            case 12:// 金融
                iv_keyword_net.setImageResource(R.mipmap.net12);
                break;
            case 14:// 医药
                iv_keyword_net.setImageResource(R.mipmap.net14);
                break;
            case 26:// 服装
                iv_keyword_net.setImageResource(R.mipmap.net26);
                break;
            case 29:// 化工
                iv_keyword_net.setImageResource(R.mipmap.net29);
                break;
            case 15:// 教培
                iv_keyword_net.setImageResource(R.mipmap.net15);
                break;
            case 22:// 机械
                iv_keyword_net.setImageResource(R.mipmap.net22);
                break;
            case 19:// 电子
                iv_keyword_net.setImageResource(R.mipmap.net19);
                break;
            case 13:// 传媒
                iv_keyword_net.setImageResource(R.mipmap.net13);
                break;
            case 30:// 旅游
                iv_keyword_net.setImageResource(R.mipmap.net30);
                break;
            case 40:// 酒店餐饮
                iv_keyword_net.setImageResource(R.mipmap.net40);
                break;
            case 20:// 电力
                iv_keyword_net.setImageResource(R.mipmap.net20);
                break;
            case 23:// IT
                iv_keyword_net.setImageResource(R.mipmap.net23);
                break;
            case 16:// 物流
                iv_keyword_net.setImageResource(R.mipmap.net16);
                break;
            case 21:// 通信
                iv_keyword_net.setImageResource(R.mipmap.net21);
                break;
        }

    }

    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(getActivity(), null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "zh_cn");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        dialog.setParameter(SpeechConstant.ASR_PTT,"0");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        dialog.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        dialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(getActivity(), "请开始说话", Toast.LENGTH_SHORT).show();
    }

    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        et_keyword_keyword.append(text);
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 首先加载企业推荐数据
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(getData(3));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_keyword_search:
                if (et_keyword_keyword.getText().length() == 0 || et_keyword_keyword.equals("")) {
                    Toast.makeText(getActivity(), "请输入关键词", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveHistory();
                    initactivity();
                }
                break;
            case R.id.rl_keywordsearch_text1:
                Histoy_initactivity(tv_keywordsearch_text1, 1);
                break;
            case R.id.rl_keywordsearch_text2:
                Histoy_initactivity(tv_keywordsearch_text2, 2);
                break;
            case R.id.rl_keywordsearch_text3:
                Histoy_initactivity(tv_keywordsearch_text3, 3);
                break;
            case R.id.iv_keyword_voice:
                btnVoice();
                break;
            case R.id.tv_keyword_condition:
                pupup();
                break;
            case R.id.rl_search_keyword_clear:
                boolean bool = db.Delete_KeyWorldHistory(MyUtils.industryId + "");
                if (bool == true) {
                    Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(getActivity(), "清除失败", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.tv_keyword_city:
//                Intent intent1 = new Intent(getActivity(), MainSelectCityToKeywordActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent1.putExtra("value", "选择地点");
//                intent1.putExtra("filter", "place");
//                startActivity(intent1);
//                break;
        }
    }

    /**
     * 加载找工作页面
     */
    private void initactivity() {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        bundle.putString("searchword", et_keyword_keyword.getText().toString().trim());
        bundle.putString("wordtype", wordtype);
        bundle.putString("areaid", placeId);
//        Toast.makeText(getActivity(), placeId + cityName, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "wordtype" + wordtype, Toast.LENGTH_SHORT).show();
        bundle.putString("areaName", cityName);
        bundle.putString("industry", sUtils.getIntValue(Constants.INDUSTRY, 11) + "");// 默认建筑
        Intent intent2 = new Intent(getActivity(), SearchJobResultActivity.class);// 找工作界面
        intent2.putExtras(bundle);
        startActivity(intent2);
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
        industry_id = sUtils.getIntValue(Constants.INDUSTRY, 11);
        params.put("industry", "" + industry_id);
        params.put("page", "");
        params.put("page_nums", "10");
        return params;
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

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * 保存历史记录
     */
    private void saveHistory() {
        Log.i("城市的名称和id",MyUtils.selectCityId+ MyUtils.selectCityZh);
        if ("全国".equals(MyUtils.selectCityZh)) {
            cityName="";
        }
        keyWorldHistory.setIndustry_id(industryId + "");
        keyWorldHistory.setPlace_id(MyUtils.selectCityId);
        keyWorldHistory.setPlace_value(MyUtils.selectCityZh);
        keyWorldHistory.setSearch_value(et_keyword_keyword.getText().toString().trim());
        keyWorldHistory.setWordtype(wordtype);
        int db_num = db.query_KeyWorldHistory(industryId + "", et_keyword_keyword.getText().toString(), cityName, wordtype);
        if (db_num == 0) {
            DB_strid = db.Insert_KeyWorldHistory(keyWorldHistory);
        } else {
            db.Delete_KeyWorldHistory(industryId + "", et_keyword_keyword.getText().toString(), wordtype, cityName);
            DB_strid = db.Insert_KeyWorldHistory(keyWorldHistory);
        }
    }

    protected void Histoy_initactivity(TextView textview, int i) {
        Bundle bundle2 = new Bundle();
        // 查询历史记录
        keyWorldHistory2 = db.query_KeyWorldHistory(industryId + "");
        String strOut_Place_id = keyWorldHistory2[keyWorldHistory2.length - i].getPlace_id();
        String strOut_searchword = keyWorldHistory2[keyWorldHistory2.length - i].getSearch_value();
        String strPlaceValue = keyWorldHistory2[keyWorldHistory2.length - i].getPlace_value();
        String strWordtype = keyWorldHistory2[keyWorldHistory2.length - i].getWordtype();

        bundle2.putString("industry", industryId + "");// 默认建筑
        bundle2.putString("areaid", strOut_Place_id);
        bundle2.putString("searchword", strOut_searchword);
        bundle2.putString("areaName", strPlaceValue);
        bundle2.putString("wordtype", strWordtype);

        Intent intent2 = new Intent(getActivity(), SearchJobResultActivity.class);
        intent2.putExtras(bundle2);
        startActivity(intent2);
    }

    /**
     * 更新Ui页面
     */
    public void updateUI() {
        keyWorldHistory2 = db.query_KeyWorldHistory(sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
        if (keyWorldHistory2.length == 0) {
            rl_search_keyword_clear.setVisibility(View.GONE);
//            tv_keywordsearch_text1.setVisibility(View.GONE);
//            tv_keywordsearch_text2.setVisibility(View.GONE);
//            tv_keywordsearch_text3.setVisibility(View.GONE);
//            tv_keywordsearch_text4.setVisibility(View.GONE);
            rl_keywordsearch_text1.setVisibility(View.GONE);
            rl_keywordsearch_text2.setVisibility(View.GONE);
            rl_keywordsearch_text3.setVisibility(View.GONE);
//            rl_keywordsearch_text4.setVisibility(View.GONE);
        } else {
            rl_search_keyword_clear.setVisibility(View.VISIBLE);
//            tv_keywordsearch_text1.setVisibility(View.GONE);
//            tv_keywordsearch_text2.setVisibility(View.GONE);
//            tv_keywordsearch_text3.setVisibility(View.GONE);
//            tv_keywordsearch_text4.setVisibility(View.GONE);

            rl_keywordsearch_text1.setVisibility(View.GONE);
            rl_keywordsearch_text2.setVisibility(View.GONE);
            rl_keywordsearch_text3.setVisibility(View.GONE);
//            rl_keywordsearch_text4.setVisibility(View.GONE);
            if (keyWorldHistory2.length >= 1) {
                // 设置第一个搜索历史记录的值
                SetTextValue(tv_keywordsearch_text1, 1);
//                tv_keywordsearch_text1.setVisibility(View.VISIBLE);
                rl_keywordsearch_text1.setVisibility(View.VISIBLE);
            }
            if (keyWorldHistory2.length >= 2) {
                // 设置第二个搜索历史记录的值
                SetTextValue(tv_keywordsearch_text2, 2);
//                tv_keywordsearch_text2.setVisibility(View.VISIBLE);
                rl_keywordsearch_text2.setVisibility(View.VISIBLE);
            }
            if (keyWorldHistory2.length >= 3) {
                // 设置第三个搜索历史记录的值
                SetTextValue(tv_keywordsearch_text3, 3);
//                tv_keywordsearch_text3.setVisibility(View.VISIBLE);
                rl_keywordsearch_text3.setVisibility(View.VISIBLE);
            }
//            if (keyWorldHistory2.length >= 4) {
//                // 设置第三个搜索历史记录的值
//                SetTextValue(tv_keywordsearch_text4, 4);
////                tv_keywordsearch_text4.setVisibility(View.VISIBLE);
//                rl_keywordsearch_text4.setVisibility(View.VISIBLE);
//            }
            if (keyWorldHistory2.length > 8) {
                for (int i = 8; i < keyWorldHistory2.length; i++) {
                    db.Delete_KeyWorldHistory(i, MyUtils.industryId + "");
                }
            }
        }
    }

    /**
     * 判断textview显示的值
     */
    private void SetTextValue(TextView textView, int i) {
        // TODO Auto-generated method stub
        String Search_value = keyWorldHistory2[keyWorldHistory2.length - i].getSearch_value();
        String Place_value = keyWorldHistory2[keyWorldHistory2.length - i].getPlace_value();
        if ("".equals(Search_value)) {
            Search_value = "全部";
        }
        if (!"".equals(Place_value)) {
            Place_value = "+" + Place_value;
        }
        textView.setText(Search_value + Place_value);
    }

    /**
     * 搜全文，搜职位，搜公司
     */
    public PopupWindow pupup() {
        View viewpop = LayoutInflater.from(getActivity()).inflate(R.layout.item_easy_listview, null);
        ListView lv_item_easy_lv = (ListView) viewpop.findViewById(R.id.lv_item_easy_lv);
        pop = new PopupWindow(getActivity());
        pop.setContentView(viewpop);
        // 设置可以获得焦点，默认为false
        pop.setFocusable(true);
        // 设置弹窗内可点击，默认为true
        pop.setTouchable(true);
        // 设置弹窗外可点击，默认为false
        pop.setOutsideTouchable(true);
        // 必须增加背景，否则无法让菜单消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗的宽度和高度
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹窗的布局界面
//        pop.showAtLocation(tv_keyword_condition);
//        pop.showAsDropDown(tv_keyword_condition);
//        backgroundAlpha(70);
        pop.showAsDropDown(rl_pager_keyword_search);


        listSearch = new ArrayList<>();
        //默认 1全文2职位名称；3企业名称
        HashMap<String, String> map1 = new HashMap();
        map1.put("key", "1");
        map1.put("value", "搜全文");
        HashMap<String, String> map2 = new HashMap();
        map2.put("key", "2");
        map2.put("value", "搜职位");
        HashMap<String, String> map3 = new HashMap();
        map3.put("key", "3");
        map3.put("value", "搜公司");

        listSearch.add(map1);
        listSearch.add(map2);
        listSearch.add(map3);
        SimplePopAdapter easyAdapter = new SimplePopAdapter();
        lv_item_easy_lv.setAdapter(easyAdapter);
        lv_item_easy_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_keyword_condition.setText(listSearch.get(position).get("value"));
                wordtype = listSearch.get(position).get("key");
                pop.dismiss();
            }
        });
        return pop;
    }

    //    /**
//     * 设置添加屏幕的背景透明度
//     * @param bgAlpha
//     */
//    public void backgroundAlpha(float bgAlpha)
//    {
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = bgAlpha; //0.0-1.0
//        getActivity().getWindow().setAttributes(lp);
//    }
    class SimplePopAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listSearch.size();
        }

        @Override
        public Object getItem(int position) {
            return listSearch.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_pop_search_simple, null);
                viewHolder = new ViewHolder();

                viewHolder.tv_item_pop_search_simple = (TextView) convertView.findViewById(R.id.tv_item_pop_search_simple);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_item_pop_search_simple.setText(listSearch.get(position).get("value"));
            return convertView;
        }

        class ViewHolder {
            TextView tv_item_pop_search_simple;
        }
    }

}
