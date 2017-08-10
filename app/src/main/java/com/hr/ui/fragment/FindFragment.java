package com.hr.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.adapter.FindPagerAdapter;
import com.hr.ui.config.Constants;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.GetDataInfo;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 发现Fragment
 */
public class FindFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FindFragment";
    private View view;
    private TextView tvFindFindjob;
    private View vFindFindjob;
    private TextView tvFindRecruitment;
    private View vFindRecruitment;
    private TextView tvFindCompany;
    private View vFindCompany;
    private TextView tvFindActivity;
    private TextView tvFindBack;
    private TextView tv_gome;
    private View vFindActivity;
    private ViewPager vpFind;
    private LinearLayout linearFindFindjob;
 /*   *//**
     * 异常返回值
     *//*
    private int error_code;
    *//**
     * 1.品牌招聘 2.炫公司 3.专题活动
     *//*
    private int ad_type = 1;
    *//**
     * 网络获取的json数据集合
     *//*
    private String json_result;
    *//**
     * 存放Fragment的list集合
     */
    private List<Fragment> list;
    /**
     * ViewPager适配器
     */
    private FindPagerAdapter findPagerAdapter;
    /**
     * 存放textView的集合
     */
    private List<TextView> listText;

    /**
     * 存放View的集合
     */
    private List<View> listView;

    /**
     * 判断有无职位
     */
    private int isJob = 1;
    private PagerFindJobFragment pagerFindJobFragment;
    private PagerActivityFragment pagerActivityFragment;
    private PagerCompanyFragment pagerCompanyFragment;
    private PagerRecruitmentFragment pagerRecruitmentFragment;
/*    *//**
     * 品牌招聘的数据
     *//*
    private ArrayList<Industry> enterprise_data;
    *//**
     * 炫公司的数据
     *//*
    private ArrayList<Industry> dazzle_data;
    *//**
     * 专题活动的数据
     *//*
    private ArrayList<Industry> special_data;

    private Industry industry;*/
    /**
     * 访问网络
     */
   /* private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;
                // 1001 成功 1002失败
                Message message = Message.obtain();
                try {
                    message.what = 1001;
                    message.arg1 = json();
    //                Log.d(TAG, "=====message.arg1" + message.arg1);
                    pagerRecruitmentFragment.upData();
                    pagerCompanyFragment.upData();
                    pagerActivityFragment.upData();
                } catch (Exception e) {
                    e.printStackTrace();
//                    message.what = 1002;
                }
            } else {
//                Message message = Message.obtain();
//                message.what = 1002;
//                handlerUI.sendMessage(message);
            }
        }

        ;
    };
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);
        initView();
        /*loadNetMsg();*/
        initViewPager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyUtils.isLogin) {
            tvFindBack.setVisibility(View.GONE);
        }
    }

    private void initView() {
        tvFindFindjob = (TextView) view.findViewById(R.id.tv_find_findjob);
        tvFindBack = (TextView) view.findViewById(R.id.tv_find__back);
        vFindFindjob = view.findViewById(R.id.v_find_findjob);
        tvFindRecruitment = (TextView) view.findViewById(R.id.tv_find_recruitment);
        vFindRecruitment = view.findViewById(R.id.v_find_recruitment);
        tvFindCompany = (TextView) view.findViewById(R.id.tv_find_company);
        tv_gome = (TextView) view.findViewById(R.id.tv_gome);
        vFindCompany = view.findViewById(R.id.v_find_company);
        tvFindActivity = (TextView) view.findViewById(R.id.tv_find_activity);
        vFindActivity = view.findViewById(R.id.v_find_activity);
        vpFind = (ViewPager) view.findViewById(R.id.vp_find);
        linearFindFindjob = (LinearLayout) view.findViewById(R.id.linear_find_findjob);
        listText = new ArrayList<>();
        listView = new ArrayList<>();
        tvFindFindjob.setOnClickListener(this);
        tvFindCompany.setOnClickListener(this);
        tvFindBack.setOnClickListener(this);
        tv_gome.setOnClickListener(this);
        tvFindRecruitment.setOnClickListener(this);
        tvFindActivity.setOnClickListener(this);

        if (isJob == 1) {
            linearFindFindjob.setVisibility(View.GONE);
//            listText.clear();
//            listView.clear();
            //按照顺序 存入集合
            listText.add(tvFindRecruitment);
            listText.add(tvFindCompany);
            listText.add(tvFindActivity);
            listView.add(vFindRecruitment);
            listView.add(vFindCompany);
            listView.add(vFindActivity);

        } else if (isJob == 2) {
            linearFindFindjob.setVisibility(View.VISIBLE);
            listText.clear();
            listView.clear();
            //按照顺序 存入集合
            listText.add(tvFindFindjob);
            listText.add(tvFindRecruitment);
            listText.add(tvFindCompany);
            listText.add(tvFindActivity);
            listView.add(vFindFindjob);
            listView.add(vFindRecruitment);
            listView.add(vFindCompany);
            listView.add(vFindActivity);
        }

       /* enterprise_data = new ArrayList<Industry>();
        dazzle_data = new ArrayList<Industry>();
        special_data = new ArrayList<Industry>();
        enterprise_data = new ArrayList<Industry>();*/
    }

    private void initViewPager() {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (pagerFindJobFragment == null) {
            pagerFindJobFragment = new PagerFindJobFragment();
        }
        if (pagerActivityFragment == null) {
            pagerActivityFragment = new PagerActivityFragment(getActivity(), 3);
        }
        if (pagerCompanyFragment == null) {
            pagerCompanyFragment = new PagerCompanyFragment(getActivity(), 2);
        }
        if (pagerRecruitmentFragment == null) {
            pagerRecruitmentFragment = new PagerRecruitmentFragment(getActivity(), 1);
        }
        /*
        将Fragment加入集合
         */
        if (isJob == 1) {
            list.clear();
            list.add(pagerRecruitmentFragment);
            list.add(pagerCompanyFragment);
            list.add(pagerActivityFragment);
        } else if (isJob == 2) {
            list.clear();
            list.add(pagerFindJobFragment);
            list.add(pagerRecruitmentFragment);
            list.add(pagerCompanyFragment);
            list.add(pagerActivityFragment);
        }
        /*
        默认第一个为红色
         */
        listText.get(0).setTextColor(Color.parseColor("#F39D0D"));
        listView.get(0).setBackgroundColor(Color.parseColor("#F39D0D"));
        findPagerAdapter = new FindPagerAdapter(getActivity().getSupportFragmentManager(), list);
        vpFind.setAdapter(findPagerAdapter);
        vpFind.setOffscreenPageLimit(1);
        vpFind.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetState();
                listText.get(position).setTextColor(Color.parseColor("#F39D0D"));
                listView.get(position).setBackgroundColor(Color.parseColor("#F39D0D"));
                vpFind.setCurrentItem(position);
             /*   ad_type = position + 1;
               if (ad_type == 1) {
                    enterprise_data.clear();
                } else if (ad_type == 2) {
                    dazzle_data.clear();
                } else if (ad_type == 3) {
                    special_data.clear();
                }
                loadNetMsg();*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 重置按钮
     */
    private void resetState() {
        for (TextView lt : listText) {
            lt.setTextColor(Color.parseColor("#333333"));
        }
        for (View lv : listView) {
            lv.setBackgroundColor(Color.parseColor("#00333333"));
        }
    }
/*
    *//**
     * 向服务器请求数据
     *//*
    public void loadNetMsg() {
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(GetDataInfo.getData(ad_type,getActivity()));
    }*/


   /* *//**
     * 初始化请求参数
     *
     * @param
     * @return
     *//*
    private HashMap<String, String> getData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "mobilead.list");
        params.put("a_id", "");
        params.put("c_id", "");
        if (ad_type == 1) {// 名企推广
            params.put("c_type", "4");
        } else if (ad_type == 2) {// 炫公司
            params.put("c_type", "7");
        } else if (ad_type == 3) {// 专题活动
            params.put("c_type", "5");
        }
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(getActivity());
        int industry_id = sUtils.getIntValue(Constants.INDUSTRY, 11);
        params.put("industry", "" + industry_id);
        params.put("page", "");
        params.put("page_nums", "20");
        return params;
    }*/

    /**
     * 解析
     *
     * @return
     */
   /* private int json() {
        Log.i(TAG, "======json" + json_result.toString());
        try {
            JSONObject jsonObject = new JSONObject(json_result);
            error_code = jsonObject.getInt("error_code");
            if (error_code != 0) {
                return error_code;
            }
            JSONArray jsonArray = (JSONArray) jsonObject.get("list");
            for (int i = 0; i < jsonArray.length(); i++) {
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
                if (ad_type == 1) {
                    enterprise_data.add(industry);
                } else if (ad_type == 2) {
                    dazzle_data.add(industry);
                } else if (ad_type == 3) {
                    special_data.add(industry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error_code;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_find_recruitment:
                if (isJob == 1) {
                    vpFind.setCurrentItem(0);
                } else {
                    vpFind.setCurrentItem(1);
                }
                break;
            case R.id.tv_find_company:
                if (isJob == 1) {
                    vpFind.setCurrentItem(1);
                } else {
                    vpFind.setCurrentItem(2);
                }
                break;
            case R.id.tv_find_activity:
                if (isJob == 1) {
                    vpFind.setCurrentItem(2);
                } else {
                    vpFind.setCurrentItem(3);
                }
                break;
            case R.id.tv_find_findjob:
                if (isJob == 1) {
                    vpFind.setCurrentItem(3);
                } else {
                    vpFind.setCurrentItem(4);
                }
                break;
            case R.id.tv_find__back:
                Intent intent = new Intent(getActivity(), ChooseIndustriesActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tv_gome:
                MainActivity.instanceMain.openD();
                break;
        }
    }
}
