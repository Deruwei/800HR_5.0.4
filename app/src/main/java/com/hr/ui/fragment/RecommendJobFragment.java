package com.hr.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.MySelectFuncitonActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.activity.SelectCityActicity;
import com.hr.ui.activity.SelectCityRecommendJobActivity;
import com.hr.ui.adapter.IndustryRecKeywordAdapter;
import com.hr.ui.adapter.SearchJobResultRecommendAdapter;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.GetBaiduLocation;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.MyImageVIew;
import com.hr.ui.utils.MyItemAnimator;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hr.ui.R.id.iv_keyword_net;
import static com.hr.ui.R.id.iv_recommendAdNo;

public class RecommendJobFragment extends BaseFragment {

    public static TextView tvRecfragmentPlace, tvRecfragmentFunction, tvRecfragmentText;
    @Bind(R.id.btn_back)
    Button btnBack;
    private RelativeLayout rlRecfragmentEmpty;
    private ScrollView rl_recycleViewData;
    @Bind(R.id.tv_recommendfragment_back)
    TextView tvRecommendfragmentBack;
    @Bind(R.id.rl_recfragment_function)
    RelativeLayout rlRecfragmentFunction;
    @Bind(R.id.rl_recfragment_place)
    RelativeLayout rlRecfragmentPlace;
    @Bind(R.id.bt_recfragment_submit)
    Button btRecfragmentSubmit;
    @Bind(R.id.rl_recfragment_login)
    RelativeLayout rlRecfragmentLogin;
    private LinearLayout lrRecfragmentJob;
    private RecyclerView lvRecommendfragment;
    @Bind(R.id.tv_recfragment_login)
    TextView tvRecfragmentLogin;
    @Bind(R.id.tv_gome)
    TextView tvGome;
    //    private ImageView tvRecommendfragmentBack;
//    private RelativeLayout rlRecfragmentFunction, rlRecfragmentPlace, rlRecfragmentLogin;
//    private Button btRecfragmentSubmit;
//    private LinearLayout lrRecfragmentJob, lvRecommendfragment;
    private View view;
    private SharedPreferencesUtils sUtils;
    private String industry;
    private int industryId;
    private SearchJobResultRecommendAdapter sjrAdapter;
    /*private MyProgressDialog dialog;*/
    private NetService service;
    private MyImageVIew ivRecommendAd,ivRecommdnAdNo;
    /**
     * 网络获取的json数据集合
     */
    private String json_result;
    private String json_resultJobIntension;

    private String funcid;
    /**
     * 异常返回值
     */
    private int error_code;
    /**
     * 城市名
     */
    private static String cityName;
    /**
     * 城市ID
     */
    private static String areaid;
    private RefleshDialogUtils dialogUtils;
    private int type = 1;
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    private ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
    /**
     * 职能选择集合
     */
    private static List<FunctionBean> functionSelectMap = new ArrayList<>();
    public static RecommendJobFragment recommendJobFragment;

    public RecommendJobFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendJobFragment = RecommendJobFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recommend_job, null);
        ButterKnife.bind(this, view);
        if(getActivity()!=null) {
            dialogUtils = new RefleshDialogUtils(getActivity());
        }
        initView();
        return view;
    }

    public void toLogin() {
        //先判断是否存在搜索要求
        if (MyUtils.isLogin) {
            tvRecfragmentLogin.setText("完善简历");
            tvRecfragmentText.setText("我还不了解你的意向，先告诉我吧！");
            loadJobIntension();
            btnBack.setVisibility(View.GONE);
            tvRecommendfragmentBack.setVisibility(View.GONE);
        } else {
            if (sUtils.getBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, false)) {
                funcid = sUtils.getStringValue(Constants.RECOMMEND_FUNCID + industry, funcid);
                areaid = sUtils.getStringValue(Constants.RECOMMEND_AREAID + industry, areaid);
                loadNetData();
            } else {
                rl_recycleViewData.setVisibility(View.GONE);
                rlRecfragmentEmpty.setVisibility(View.GONE);
                lrRecfragmentJob.setVisibility(View.VISIBLE);
            }
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyUtils.canReflesh == true) {
            MyUtils.canReflesh = false;
            initData();
            toLogin();
        }
    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(getActivity());
        industry = sUtils.getIntValue(Constants.INDUSTRY, 11) + "";
        totalList = new ArrayList<>();
        /*dialog = new MyProgressDialog(getActivity());*/
        functionSelectMap.clear();
    }

    public void initView() {
        sUtils = new SharedPreferencesUtils(getActivity());
        industryId = sUtils.getIntValue(Constants.INDUSTRY, Constants.INDUSTRY_BUILD_ID);
        sjrAdapter = new SearchJobResultRecommendAdapter(getActivity());
        tvRecfragmentFunction = (TextView) view.findViewById(R.id.tv_recfragment_function);
        tvRecfragmentText = (TextView) view.findViewById(R.id.tv_recfragment_text);
        tvRecfragmentPlace = (TextView) view.findViewById(R.id.tv_recfragment_place);
        rlRecfragmentEmpty= (RelativeLayout) view.findViewById(R.id.rl_recfragment_empty);
        rl_recycleViewData= (ScrollView) view.findViewById(R.id.sv_recommend);
        lrRecfragmentJob= (LinearLayout) view.findViewById(R.id.lr_recfragment_job);
        lvRecommendfragment= (RecyclerView) view.findViewById(R.id.lv_recommendfragment);
        ivRecommendAd= (MyImageVIew) view.findViewById(R.id.iv_recommendAd);
        ivRecommdnAdNo= (MyImageVIew) view.findViewById(R.id.iv_recommendAdNo);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvRecommendfragment.setLayoutManager(manager);
        lvRecommendfragment.addItemDecoration(new SpacesItemDecoration(5));
        switch (industryId) {
            case 11:// 建筑
                ivRecommendAd.setImageResource(R.mipmap.build);
                ivRecommdnAdNo.setImageResource(R.mipmap.build);
                break;
            case 12:// 金融
                ivRecommendAd.setImageResource(R.mipmap.finance);
                ivRecommdnAdNo.setImageResource(R.mipmap.finance);
                break;
            case 14:// 医药
                ivRecommendAd.setImageResource(R.mipmap.medicine);
                ivRecommdnAdNo.setImageResource(R.mipmap.medicine);
                break;
            case 29:// 化工
                ivRecommendAd.setImageResource(R.mipmap.chemical);
                ivRecommendAd.setImageResource(R.mipmap.chemical);
                ivRecommdnAdNo.setImageResource(R.mipmap.chemical);
                break;
            case 22:// 机械
                ivRecommendAd.setImageResource(R.mipmap.manufacture);
                ivRecommdnAdNo.setImageResource(R.mipmap.manufacture);
                break;
            default:
                ivRecommendAd.setImageResource(R.mipmap.hr);
                ivRecommdnAdNo.setImageResource(R.mipmap.hr);
                break;
        }
        ivRecommendAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (industryId) {
                    case 11:// 建筑
                        openBrowser(Constants.BUILD_URL);
                        break;
                    case 12:// 金融
                        openBrowser(Constants.FINANCE_URL);
                        break;
                    case 14:// 医药
                        openBrowser(Constants.MEDICINE_URL);
                        break;
                    case 29:// 化工
                        openBrowser(Constants.CHEMICAL_URL);
                        break;
                    case 22:// 机械
                        openBrowser(Constants.MANUFACTURE_URL);
                        break;
                    default:
                        openBrowser(Constants.HR_URL);
                        break;
                }
            }
        });
        ivRecommdnAdNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (industryId) {
                    case 11:// 建筑
                        openBrowser(Constants.BUILD_URL);
                        break;
                    case 12:// 金融
                        openBrowser(Constants.FINANCE_URL);
                        break;
                    case 14:// 医药
                        openBrowser(Constants.MEDICINE_URL);
                        break;
                    case 29:// 化工
                        openBrowser(Constants.CHEMICAL_URL);
                        break;
                    case 22:// 机械
                        openBrowser(Constants.MANUFACTURE_URL);
                        break;
                    default:
                        openBrowser(Constants.HR_URL);
                        break;
                }
            }
        });
//        tvRecommendfragmentBack = (ImageView) view.findViewById(R.id.iv_recommendfragment_back);
//        rlRecfragmentFunction = (RelativeLayout) view.findViewById(R.id.rl_recfragment_function);
//        rlRecfragmentLogin = (RelativeLayout) view.findViewById(R.id.rl_recfragment_login);
//        rlRecfragmentPlace = (RelativeLayout) view.findViewById(R.id.rl_recfragment_place);
    }

    /**
     * 打开浏览器
     * @param url
     */
    private void openBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("url无效");
        }
    }
    @OnClick({R.id.tv_gome, R.id.tv_recommendfragment_back, R.id.rl_recfragment_function, R.id.rl_recfragment_place, R.id.bt_recfragment_submit, R.id.rl_recfragment_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_recommendfragment_back:
                Intent intent = new Intent(getActivity(), ChooseIndustriesActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.rl_recfragment_function:
                // 加载职能选择页
                Intent function = new Intent(getActivity(), MySelectFuncitonActivity.class);
                function.putExtra("filter", "recommend");
                function.putExtra("selectMap", (Serializable) functionSelectMap);
                function.putExtra("value", "职能");
                startActivity(function);

                break;
            case R.id.rl_recfragment_place:
                Intent intent1 = new Intent(getActivity(), SelectCityActicity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("type", "1");
                intent1.putExtra("from", "recommendJob");
                startActivity(intent1);
                break;
            case R.id.bt_recfragment_submit:
                if (functionSelectMap != null) {
                    StringBuffer funcidBuffer = new StringBuffer();
                    for (int i = 0; i < functionSelectMap.size(); i++) {
                        String string = functionSelectMap.get(i).getId();
                        funcidBuffer.append(string + ",");
                    }
                    if (funcidBuffer.length() >= 1) {
                        funcid = funcidBuffer.substring(0, funcidBuffer.length() - 1);
                    } else {
                        funcid = "";
                    }
                }
                if ((!tvRecfragmentFunction.getText().toString().equals("请选择")) && (!tvRecfragmentFunction.getText().toString().equals(""))) {
                    if ((!tvRecfragmentPlace.getText().toString().equals("请选择")) && (!tvRecfragmentPlace.getText().toString().equals(""))) {
                        loadNetData();
                        sUtils.setBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, true);
                        sUtils.setStringValue(Constants.RECOMMEND_FUNCID + industry, funcid);
                        sUtils.setStringValue(Constants.RECOMMEND_AREAID + industry, areaid);
                    } else {
                        Toast.makeText(getActivity(), "请选择期望地区", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请选择期望职位", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_recfragment_login:
//                if (MyUtils.ableInternet) {
//                    if (isHaveResume) {
//                        Intent intentResume = new Intent(getActivity(), MyResumeActivity.class);
//                        intentResume.putExtra("listResumeJsonObj", listResumeJsonString);
//                        intentResume.putExtra("user_id", MyUtils.userID);
//                        startActivity(intentResume);
//                    } else {
//                        Intent intentResume2 = new Intent(getActivity(), CreateResumePersonInfoActivity.class);
//                        intentResume2.putExtra("resumeId", "-1");
//                        intentResume2.putExtra("resumeLanguage", "zh");
//                        intentResume2.putExtra("isCHS", true);
//                        startActivity(intentResume2);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "无网络,请稍候重试", Toast.LENGTH_SHORT).show();
//                }
                if (tvRecfragmentLogin.getText().toString().equals("注册登录")) {
                    if(getActivity()!=null) {
                        Intent intentLogin = new Intent(getActivity(), NewLoginActivity.class);
                        startActivity(intentLogin);
                    }
                } else if (tvRecfragmentLogin.getText().toString().equals("完善简历")) {
                    MainActivity.instanceMain.setTabSelect(3);
//                    MainActivity.instanceMain.meFragment.isGoResume = true;
                }
                break;
            case R.id.tv_gome:
                MainActivity.instanceMain.openD();
                break;
        }
    }

    /**
     * 加载职系选择列表
     */
    public static void setFunctionSelectMap(List<FunctionBean> map) {
        functionSelectMap.clear();
        functionSelectMap.addAll(map);
        showText();
    }

    /**
     * 显示文本信息
     */
    private static void showText() {
        if (functionSelectMap.size() == 0) {
            tvRecfragmentPlace.setText("请选择");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < functionSelectMap.size(); i++) {
            String keyString = functionSelectMap.get(i).getName();
            buffer.append(keyString + "、");
        }
        tvRecfragmentFunction.setText(buffer.toString().subSequence(0, buffer.length() - 1).toString().trim());
    }

    /**
     * 设置地点
     *
     * @param value
     */
    public static void setPlaceText(String value) {
        // TODO Auto-generated method stub
        cityName = value;
        tvRecfragmentPlace.setText(value);
    }

    /**
     * 设置城市ID
     *
     * @param string
     */
    public static void setPlaceId(String string) {
        // TODO Auto-generated method stub
        areaid = string;
    }

    /**
     * 初始化请求参数
     *
     * @return
     */
    private HashMap<String, String> getData(int pageIndex) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "job.search");
        params.put("get_poster", "1");
        params.put("func", funcid == null ? "" : funcid);
        params.put("area", areaid == null ? "" : areaid);
        params.put("industry", industry == null ? "11" : industry);
        params.put("lingyu", "");
        params.put("page", pageIndex + "");
        params.put("page_nums", "20");
        params.put("nautica", "");
        params.put("range", "");
        Log.i("params",params.toString());
        return params;
    }

    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                json_result = (String) msg.obj;
                dataList = new ArrayList<>();
                dataList = GetJssonList.searchResult_json(json_result);// 状态码
               // Log.i("params获得的数据",json_result.toString());
                if (dataList != null && dataList.size() != 0) {
                    totalList.addAll(dataList);
                    sjrAdapter.setDataList(dataList);
                    lvRecommendfragment.setAdapter(sjrAdapter);
                 /*   MyItemAnimator animator = new MyItemAnimator(getActivity());
                    animator.setAddDuration(500);
                    animator.setRemoveDuration(1000);
                    lvRecommendfragment.setItemAnimator(animator);*/
                    lrRecfragmentJob.setVisibility(View.GONE);
                    rl_recycleViewData.setVisibility(View.VISIBLE);
                    rlRecfragmentEmpty.setVisibility(View.GONE);
                    //Log.i("你好","-----------nihao");
                } else {
                   // Log.i("你好2","-----------nihao");
                    lrRecfragmentJob.setVisibility(View.GONE);
                    rl_recycleViewData.setVisibility(View.GONE);
                    rlRecfragmentEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                lrRecfragmentJob.setVisibility(View.VISIBLE);
                rl_recycleViewData.setVisibility(View.GONE);
                rlRecfragmentEmpty.setVisibility(View.GONE);
            }
            if(dialogUtils!=null) {
                dialogUtils.dismissDialog();
            }
        }


    };

    /**
     * 加载数据
     */
    public void loadNetData() {
        if (type == 1) {
            type = 2;
            if(dialogUtils!=null) {
                dialogUtils.showDialog();
            }
        }
        service = new NetService(getActivity(), handlerService);
        service.execute(getData(1));
    }

    private Handler handlerJobIntension = new Handler() {
        public void handleMessage(Message msg) {
//            Toast.makeText(getActivity(), "发送数据完毕" + msg.what, Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
                if(dialogUtils!=null) {
                    dialogUtils.dismissDialog();
                }
                json_resultJobIntension = (String) msg.obj;
                try {
                    JSONObject jsonObjectJobIntension = new JSONObject(json_resultJobIntension);
//                    Toast.makeText(getActivity(), "完毕" + jsonObjectJobIntension.toString(), Toast.LENGTH_SHORT).show();
                    if ("0".equals(jsonObjectJobIntension.getString("error_code"))) {
                        JSONObject jsonorder_info = jsonObjectJobIntension.getJSONObject("order_info");

                        if (!jsonorder_info.toString().contains("func") || !jsonorder_info.toString().contains("workarea")) {
                            if (sUtils.getBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, false)) {
                                funcid = sUtils.getStringValue(Constants.RECOMMEND_FUNCID + industry, funcid);
                                areaid = sUtils.getStringValue(Constants.RECOMMEND_AREAID + industry, areaid);
                                loadNetData();
                            } else {
                                rl_recycleViewData.setVisibility(View.GONE);
                                rlRecfragmentEmpty.setVisibility(View.GONE);
                                lrRecfragmentJob.setVisibility(View.VISIBLE);
                            }
                        } else {
                            lrRecfragmentJob.setVisibility(View.GONE);
                            funcid = jsonorder_info.getString("func");
                            areaid = jsonorder_info.getString("workarea");
                            loadNetData();
                        }

                    } else {
                        if (sUtils.getBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, false)) {
                            funcid = sUtils.getStringValue(Constants.RECOMMEND_FUNCID + industry, funcid);
                            areaid = sUtils.getStringValue(Constants.RECOMMEND_AREAID + industry, areaid);
                            loadNetData();
                        } else {
                            rl_recycleViewData.setVisibility(View.GONE);
                            rlRecfragmentEmpty.setVisibility(View.GONE);
                            lrRecfragmentJob.setVisibility(View.VISIBLE);
                        }
                        rl_recycleViewData.setVisibility(View.GONE);
                        rlRecfragmentEmpty.setVisibility(View.GONE);
                        lrRecfragmentJob.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if(dialogUtils!=null) {
                    dialogUtils.dismissDialog();
                }
            }
        }


    };

    private void getData() {

    }
   /* @Override
    public void onPause() {
        super.onPause();
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(dialogUtils!=null) {
            dialogUtils.dismissDialog();
        }
    }

    /**
     * 加载数据
     */
    public void loadJobIntension() {
        HashMap<String, String> paramsJobIntension = new HashMap<String, String>();
        paramsJobIntension.put("method", "user_resume.orderget");
        if(dialogUtils!=null) {
            dialogUtils.showDialog();
        }
        NetService service = new NetService(getActivity(), handlerJobIntension);
        service.execute(paramsJobIntension);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        rlRecfragmentEmpty.setVisibility(View.GONE);
        rl_recycleViewData.setVisibility(View.GONE);
        lrRecfragmentJob.setVisibility(View.VISIBLE);
    }

    /**
     * 数据解析
     *
     * @return
     */
  /*  public int searchResult_json() {
        // TODO Auto-generated method stub
        try {
            dataList = new ArrayList<HashMap<String, Object>>();
            JSONObject jsonObject = new JSONObject(json_result);
//            LogTools.i(TAG, "====jsonObject" + jsonObject.toString());
            error_code = jsonObject.getInt("error_code");
//            jobNum = jsonObject.getString("totals");
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("jobs_list");
//            dataList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonOb = (JSONObject) jsonArray.get(i);
                hs = new HashMap<String, Object>();
                hs.put("job_name", jsonOb.getString("job_name"));
                hs.put("enterprise_name", jsonOb.getString("enterprise_name"));
                hs.put("issue_date", jsonOb.getString("issue_date"));
                hs.put("workplace", jsonOb.getString("workplace"));
                hs.put("posterimg", jsonOb.getString("posterimg"));
                hs.put("job_id", jsonOb.getString("job_id"));
                hs.put("enterprise_id", jsonOb.getString("enterprise_id"));
                hs.put("nautica", jsonOb.getString("nautica"));
                hs.put("study", jsonOb.getString("study"));
                hs.put("is_expire", jsonOb.getString("is_expire"));
                hs.put("is_apply", jsonOb.getString("is_apply"));//是否投递过，0没有，1有
                hs.put("is_favourite", jsonOb.getString("is_favourite"));//是否投递过，0没有，1有
                dataList.add(hs);
            }
            totalList.addAll(dataList);
            Log.i("数据",dataList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error_code;
    }*/

}
