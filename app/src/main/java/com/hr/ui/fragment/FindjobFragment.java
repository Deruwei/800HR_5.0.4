package com.hr.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.MainSelectCityToKeywordActivity;
import com.hr.ui.adapter.FindPagerAdapter;
import com.hr.ui.bean.ResumeInfo;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.GetBaiduLocation;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class FindjobFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "FindjobFragment";
    private View view;
    /**
     * ViewPager适配器
     */
    private FindPagerAdapter findPagerAdapter;
    //    private TextView tv_findjob_industory;
    private TextView tv_findjob_keyword;
    private ImageView iv_findjob_keyword;
    private TextView tv_findjob_classify, tv_gome, tv_findjob_back;
    private ImageView iv_findjob_classify, iv_dingwei;
    private static TextView tv_findjob_city;

    //    private static ImageView iv_againindustory_back;
    private ViewPager vp_findjob;
    /**
     * 关键词搜索Fragment
     */
    private PagerKeywordSearchFragment pagerKeywordSearchFragment;
    /**
     * 分类搜索Fragment
     */
    private PagerPostSearchFragment pagerPostSearchFragment;
    /**
     * 城市ID
     */
    private static String placeId;
    /**
     * 城市名
     */
    private static String cityName;
    private GetBaiduLocation baiduLocation;
    /**
     * 存放Fragment的集合
     */
    private List<Fragment> list;
    private TextView[] tvArray;
    private ImageView[] ivArray;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String city = MyUtils.currentCityZh;
                    if (city != null && !"".equals(city)) {
                        if (city.length() != 0) {
                            cityName = city.substring(0, city.length() - 1);
                            MyUtils.selectCityZh = cityName;
                            MyUtils.selectCityId = ResumeInfoIDToString.getCityID(getActivity(), city, true);

                        }
                    } else {
                        cityName = "定位失败";
                    }
                    setPlaceText(cityName);
                    break;

            }
        }
    };

    public FindjobFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.firstIn = true;
    }

    private void initView() {
//        tv_findjob_industory = (TextView) view.findViewById(R.id.tv_findjob_industory);
        tv_findjob_city = (TextView) view.findViewById(R.id.tv_findjob_city);
        tv_findjob_keyword = (TextView) view.findViewById(R.id.tv_findjob_keyword);
        iv_findjob_keyword = (ImageView) view.findViewById(R.id.iv_findjob_keyword);
        tv_findjob_classify = (TextView) view.findViewById(R.id.tv_findjob_classify);
        tv_findjob_back = (TextView) view.findViewById(R.id.tv_findjob_back);
        tv_gome = (TextView) view.findViewById(R.id.tv_gome);
        iv_dingwei = (ImageView) view.findViewById(R.id.iv_dingwei);
        iv_findjob_classify = (ImageView) view.findViewById(R.id.iv_findjob_classify);
        tv_findjob_classify.setOnClickListener(this);
        tv_findjob_keyword.setOnClickListener(this);
        tv_findjob_city.setOnClickListener(this);
        tv_findjob_back.setOnClickListener(this);
        tv_gome.setOnClickListener(this);
        iv_dingwei.setOnClickListener(this);

//        iv_againindustory_back = (ImageView) view.findViewById(R.id.iv_againindustory_back);
//        upDataUI();
//        iv_againindustory_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ChooseIndustriesActivity.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
        vp_findjob = (ViewPager) view.findViewById(R.id.vp_findjob);
        tvArray = new TextView[]{tv_findjob_keyword, tv_findjob_classify};
        ivArray = new ImageView[]{iv_findjob_keyword, iv_findjob_classify};
    }

//    public static void upDataUI() {
//        if (MyUtils.isLogin) {
//            iv_againindustory_back.setVisibility(View.GONE);
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_findjob, container, false);
        baiduLocation = new GetBaiduLocation(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        if (MyUtils.firstIn == true) {
            initData();
            initViewPager();
        }
        if (MyUtils.isLogin) {
            tv_findjob_back.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyUtils.firstIn = false;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (MyUtils.firstIn == true) {
            tvArray[0].setTextColor(Color.parseColor("#F39D0D"));
            ivArray[0].setBackgroundColor(Color.parseColor("#F39D0D"));
        }
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(getActivity());
        int industry = sUtils.getIntValue(Constants.INDUSTRY, 11);
        /*
        选择公司
         */
//        switch (industry) {
//            case 11:// 建筑
//                tv_findjob_industory.setText("建筑英才网");
//                break;
//            case 12:// 金融
//                tv_findjob_industory.setText("金融英才网");
//                break;
//            case 14:// 医药
//                tv_findjob_industory.setText("医药英才网");
//                break;
//            case 26:// 服装
//                tv_findjob_industory.setText("服装英才网");
//                break;
//            case 29:// 化工
//                tv_findjob_industory.setText("化工英才网");
//                break;
//            case 15:// 教培
//                tv_findjob_industory.setText("教培英才网");
//                break;
//            case 22:// 机械
//                tv_findjob_industory.setText("机械英才网");
//                break;
//            case 19:// 电子
//                tv_findjob_industory.setText("电子英才网");
//                break;
//            case 13:// 传媒
//                tv_findjob_industory.setText("传媒英才网");
//                break;
//            case 30:// 旅游
//                tv_findjob_industory.setText("旅游英才网");
//                break;
//            case 40:// 酒店餐饮
//                tv_findjob_industory.setText("酒店餐饮英才网");
//                break;
//            case 20:// 电力
//                tv_findjob_industory.setText("电力英才网");
//                break;
//            case 23:// IT
//                tv_findjob_industory.setText("IT英才网");
//                break;
//            case 16:// 物流
//                tv_findjob_industory.setText("物流英才网");
//                break;
//            case 21:// 通信
//                tv_findjob_industory.setText("通信英才网");
//                break;
//        }
        getAddress();
    }

    private void getAddress() {
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        list = new ArrayList<>();
        pagerPostSearchFragment = new PagerPostSearchFragment();
        pagerKeywordSearchFragment = new PagerKeywordSearchFragment();
        list.add(pagerKeywordSearchFragment);
        list.add(pagerPostSearchFragment);
        findPagerAdapter = new FindPagerAdapter(getActivity().getSupportFragmentManager(), list);
        vp_findjob.setAdapter(findPagerAdapter);
        vp_findjob.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetState();
                tvArray[position].setTextColor(Color.parseColor("#F39D0D"));
                ivArray[position].setBackgroundColor(Color.parseColor("#F39D0D"));
                vp_findjob.setCurrentItem(position);
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
        for (TextView tv : tvArray) {
            tv.setTextColor(Color.parseColor("#333333"));
        }
        for (View iv : ivArray) {
            iv.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_findjob_classify:
                vp_findjob.setCurrentItem(1);
                break;
            case R.id.tv_findjob_keyword:
                vp_findjob.setCurrentItem(0);
                break;
            case R.id.tv_findjob_city:
                if (tv_findjob_city.getText().toString().equals("定位失败")) {
                    baiduLocation.loadLocation();
                    getAddress();
                }
                Intent intent1 = new Intent(getActivity(), MainSelectCityToKeywordActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("value", "选择地点");
                intent1.putExtra("filter", "place");
                startActivity(intent1);
                break;
            case R.id.tv_findjob_back:
                Intent intent = new Intent(getActivity(), ChooseIndustriesActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.tv_gome:
                MainActivity.instanceMain.openD();
                break;
            case R.id.iv_dingwei:

                break;
        }
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
     * 设置地点
     *
     * @param value
     */
    public static void setPlaceText(String value) {
        // TODO Auto-generated method stub
        tv_findjob_city.setText(value);
        cityName = value;
    }
}
