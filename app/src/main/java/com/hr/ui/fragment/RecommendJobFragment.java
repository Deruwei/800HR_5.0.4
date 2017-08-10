package com.hr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.activity.SelectCityRecommendJobActivity;
import com.hr.ui.activity.SelectFunctionRecommendJobActivity;
import com.hr.ui.adapter.SearchJobResultRecommendAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.MyProgressDialog;
import com.mob.tools.utils.LocationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendJobFragment extends Fragment {

    public static TextView tvRecfragmentPlace, tvRecfragmentFunction, tvRecfragmentText;

    private RelativeLayout rlRecfragmentEmpty;
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
    @Bind(R.id.lr_recfragment_job)
    LinearLayout lrRecfragmentJob;
    @Bind(R.id.lv_recommendfragment)
    RecyclerView lvRecommendfragment;
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
    private SearchJobResultRecommendAdapter sjrAdapter;
    private MyProgressDialog dialog;
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
    /**
     * 总数据
     */
    private static ArrayList<HashMap<String, Object>> totalList;
    private HashMap<String, Object> hs;
    private ArrayList<HashMap<String, Object>> dataList;
    /**
     * 职能选择集合
     */
    private static HashMap<String, String> functionSelectMap = new HashMap<>();
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        initView();
    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(getActivity());
        industry = sUtils.getIntValue(Constants.INDUSTRY, 11) + "";
        totalList = new ArrayList<>();
        dialog = new MyProgressDialog(getActivity());
        functionSelectMap.clear();
    }
    public void initView() {
        tvRecfragmentFunction = (TextView) view.findViewById(R.id.tv_recfragment_function);
        tvRecfragmentText = (TextView) view.findViewById(R.id.tv_recfragment_text);
        tvRecfragmentPlace = (TextView) view.findViewById(R.id.tv_recfragment_place);
        rlRecfragmentEmpty = (RelativeLayout) view.findViewById(R.id.rl_recfragment_empty);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvRecommendfragment.setLayoutManager(manager);
        lvRecommendfragment.addItemDecoration(new SpacesItemDecoration(5));
//        tvRecommendfragmentBack = (ImageView) view.findViewById(R.id.iv_recommendfragment_back);
//        rlRecfragmentFunction = (RelativeLayout) view.findViewById(R.id.rl_recfragment_function);
//        rlRecfragmentLogin = (RelativeLayout) view.findViewById(R.id.rl_recfragment_login);
//        rlRecfragmentPlace = (RelativeLayout) view.findViewById(R.id.rl_recfragment_place);
        //先判断是否存在搜索要求
        if (MyUtils.isLogin) {
            tvRecfragmentLogin.setText("完善简历");
            tvRecfragmentText.setText("我还不了解你的意向，先告诉我吧！");
            loadJobIntension();
            tvRecommendfragmentBack.setVisibility(View.GONE);
        } else {
            if (sUtils.getBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, false)) {
                    funcid = sUtils.getStringValue(Constants.RECOMMEND_FUNCID + industry, funcid);
                    areaid = sUtils.getStringValue(Constants.RECOMMEND_AREAID + industry, areaid);
                    loadNetData();
            } else {
                lvRecommendfragment.setVisibility(View.GONE);
                rlRecfragmentEmpty.setVisibility(View.GONE);
                lrRecfragmentJob.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                Intent function = new Intent(getActivity(), SelectFunctionRecommendJobActivity.class);
                function.putExtra("filter", "post");
                function.putExtra("selectMap", (Serializable) functionSelectMap);
                function.putExtra("value", "职能");
                startActivity(function);

                break;
            case R.id.rl_recfragment_place:
                Intent intent1 = new Intent(getActivity(), SelectCityRecommendJobActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("value", "选择地点");
                intent1.putExtra("filter", "place");
                startActivity(intent1);
                break;
            case R.id.bt_recfragment_submit:
                if (functionSelectMap != null) {
                    StringBuffer funcidBuffer = new StringBuffer();
                    Set<String> keySet = functionSelectMap.keySet();
                    for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
                        String string = (String) iterator.next();
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
                    Intent intentLogin = new Intent(getActivity(), NewLoginActivity.class);
                    startActivity(intentLogin);
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
    public static void setFunctionSelectMap(HashMap<String, String> map) {
        functionSelectMap.clear();
        functionSelectMap.putAll(map);
        showText();
    }

    /**
     * 显示文本信息
     */
    private static void showText() {
        Set<String> keySet = functionSelectMap.keySet();
        if (functionSelectMap.size() == 0) {
            tvRecfragmentPlace.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext(); ) {
            String keyString = (String) iterator.next();
            buffer.append(functionSelectMap.get(keyString).trim() + "、");
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
        return params;
    }

    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;
               /* try {*/
                    // 1001 成功 1002失败
                   /* Message msg0 = new Message();
                    msg0.what = 1001;*/
                    dataList = GetJssonList.searchResult_json(json_result);// 状态码
                    if(dataList!=null&&dataList.size()!=0) {
                        totalList.addAll(dataList);
                        sjrAdapter = new SearchJobResultRecommendAdapter(getActivity(), totalList);
                        lvRecommendfragment.setAdapter(sjrAdapter);
                        lrRecfragmentJob.setVisibility(View.GONE);
                        lvRecommendfragment.setVisibility(View.VISIBLE);
                        rlRecfragmentEmpty.setVisibility(View.GONE);
                    }else{
                        lrRecfragmentJob.setVisibility(View.GONE);
                        lvRecommendfragment.setVisibility(View.GONE);
                        rlRecfragmentEmpty.setVisibility(View.VISIBLE);
                    }
                  /*  Log.d("msg0.arg1", msg0.arg1 + "");
                    myhandler.sendMessage(msg0);
                } catch (Exception e) {
                    e.printStackTrace();
                  *//*  Message msg1 = new Message();
                    msg1.what = 1002;
                    myhandler.sendMessage(msg1);*//*
                }*/
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }/* else {
                Message msg1 = new Message();
                msg1.what = 1002;
                myhandler.sendMessage(msg1);
            }*/
        }


    };
  /*  // 更新UI
    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                if (msg.arg1 == 0) {// 成功获取数据
                    // 通知适配器更新数据
                    updateUI(totalList);
                } else if (msg.arg1 == 206) {//
                    Toast.makeText(getActivity(), "执行失败", Toast.LENGTH_SHORT).show();
                 *//*   try {
//                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*//*

                } else if (msg.arg1 == 11) {
                    Toast.makeText(getActivity(), getString(R.string.error_notnet), Toast.LENGTH_SHORT).show();
                } else {// 获取数据失败
                }
            } else if (msg.what == 1002) {// 无响应或抛异常
//                Toast.makeText(getActivity(), "网络不稳定", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1003) {
//                Toast.makeText(getActivity(), "网络不稳定", Toast.LENGTH_SHORT).show();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        ;
    };*/

    /*private void updateUI(ArrayList<HashMap<String, Object>> totalList1) {
        Message message=new Message();
       if(totalList1!=null&&totalList1.size()!=0){
           message.what=0;
           message.obj=totalList1;
       }else{
           message.what=1;
       }
       refleshUIHandler.sendMessage(message);

    }
    private Handler refleshUIHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    totalList= (ArrayList<HashMap<String, Object>>) msg.obj;
                    sjrAdapter = new SearchJobResultRecommendAdapter(getActivity(), totalList);
                    lvRecommendfragment.setAdapter(sjrAdapter);
                    lrRecfragmentJob.setVisibility(View.GONE);
                    lvRecommendfragment.setVisibility(View.VISIBLE);
                    rlRecfragmentEmpty.setVisibility(View.GONE);
                    break;
                case 1:
                    lrRecfragmentJob.setVisibility(View.GONE);
                    lvRecommendfragment.setVisibility(View.GONE);
                    rlRecfragmentEmpty.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };*/
    /**
     * 加载数据
     */
    public void loadNetData() {
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(getData(1));
    }

    private Handler handlerJobIntension = new Handler() {
        public void handleMessage(Message msg) {
//            Toast.makeText(getActivity(), "发送数据完毕" + msg.what, Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
                json_resultJobIntension = (String) msg.obj;
                try {
                    JSONObject jsonObjectJobIntension = new JSONObject(json_resultJobIntension);
//                    Toast.makeText(getActivity(), "完毕" + jsonObjectJobIntension.toString(), Toast.LENGTH_SHORT).show();
                    if (jsonObjectJobIntension.getString("error_code").trim().equals("0")) {
                        JSONObject jsonorder_info = jsonObjectJobIntension.getJSONObject("order_info");
                        if(!jsonorder_info.toString().contains("func")||!jsonorder_info.toString().contains("workarea")){
                            lrRecfragmentJob.setVisibility(View.VISIBLE);
                        }else{
                            lrRecfragmentJob.setVisibility(View.GONE);
                            funcid = jsonorder_info.getString("func");
                            areaid = jsonorder_info.getString("workarea");
                            loadNetData();
                        }

                    } else {
                        if (sUtils.getBooleanValue(Constants.IS_HAVE_RECOMMEND + industry, false)) {

                        } else {
                            lrRecfragmentJob.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }/* else {
                Message msg1 = new Message();
                msg1.what = 1002;
                myhandler.sendMessage(msg1);
            }*/
        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 加载数据
     */
    public void loadJobIntension() {
        HashMap<String, String> paramsJobIntension = new HashMap<String, String>();
        paramsJobIntension.put("method", "user_resume.orderget");
        NetService service = new NetService(getActivity(), handlerJobIntension);
        service.execute(paramsJobIntension);
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
