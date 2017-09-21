package com.hr.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.SelectCityFirstAdapter;
import com.hr.ui.adapter.SelectCitySecondAdapter;
import com.hr.ui.bean.CityBean;
import com.hr.ui.fragment.FindjobFragment;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.MyFlowLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wdr on 2017/9/19.
 */

public class SelectCityActicity extends BaseActivity {
    @Bind(R.id.iv_placeselect_back)
    ImageView ivPlaceselectBack;
    @Bind(R.id.tv_placeselect_jobnum)
    TextView tvPlaceselectJobnum;
    @Bind(R.id.post_placeselect_confirm)
    TextView postPlaceselectConfirm;
    @Bind(R.id.rl_placeselect_top)
    RelativeLayout rlPlaceselectTop;
    @Bind(R.id.placeselect_selectedinfo)
    TextView placeselectSelectedinfo;
    @Bind(R.id.placeselect_showmessage)
    LinearLayout placeselectShowmessage;
    @Bind(R.id.placeselect_showinforlayout)
    MyFlowLayout placeselectShowinforlayout;
    @Bind(R.id.placeselect_listview)
    ListView placeselectListview;
    @Bind(R.id.add_city)
    ListView addCity;
    private List<CityBean> cityList = new ArrayList<>();
    private List<CityBean> cityList2 = new ArrayList<>();
    private List<CityBean> cityFisrtList = new ArrayList<>();
    private List<CityBean> citySecondList = new ArrayList<>();
    private List<CityBean> selectCityList = new ArrayList<>();
    private String type;//类型
    private String from;//来自哪个页面
    private int num;
    private int sum;
    private int position;//左边listview现在点击的位置
    private SelectCityFirstAdapter selectFirstAdapter;
    private SelectCitySecondAdapter selectCitySecondAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    selectFirstAdapter = new SelectCityFirstAdapter(SelectCityActicity.this, cityFisrtList, num);
                    placeselectListview.setAdapter(selectFirstAdapter);
                    placeselectListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            position=i;
                            if (i < num) {
                                if (i == 0 && "定位失败".equals(cityFisrtList.get(i).getName())) {
                                    return;
                                }
                                if ("createPersonInfo".equals(from)) {
                                    CreateResumePersonInfoActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    CreateResumePersonInfoActivity.setPlaceText(cityFisrtList.get(i).getName());
                                } else if ("recommendJob".equals(from)) {
                                    RecommendJobFragment.setPlaceId(cityFisrtList.get(i).getId());
                                    RecommendJobFragment.setPlaceText(cityFisrtList.get(i).getName());
                                } else if ("findJob".equals(from)) {
                                    FindjobFragment.setPlaceId(cityFisrtList.get(i).getId());
                                    FindjobFragment.setPlaceText(cityFisrtList.get(i).getName());
                                }else if("createJob".equals(from)){
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceText(cityFisrtList.get(i).getName());
                                }else if("resumePersonInfo".equals(from)){
                                    ResumePersonInfoActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    ResumePersonInfoActivity.setPlaceText(cityFisrtList.get(i).getName());
                                }else if("resumePlant".equals(from)){
                                    ModifyPlantActivity.modifyPlantActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    ModifyPlantActivity.modifyPlantActivity.setPlaceText(cityFisrtList.get(i).getName());
                                }else if("subscription".equals(from)) {
                                    SubscriptionActivity.subscriptionActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    SubscriptionActivity.subscriptionActivity.setPlaceText(cityFisrtList.get(i).getName());
                                }else {
                                    ModifyExpActivity.modifyExpActivity.setPlaceId(cityFisrtList.get(i).getId());
                                    ModifyExpActivity.modifyExpActivity.setPlaceText(cityFisrtList.get(i).getName());
                                }
                                MyUtils.selectCityId = cityFisrtList.get(i).getId();
                                MyUtils.selectCityZh = cityFisrtList.get(i).getName();
                                submit();
                            } else {
                                if (num == 0) {
                                    if (i < 4) {
                                        if (cityFisrtList.get(i).isCheck() == true) {
                                            cityFisrtList.get(i).setCheck(false);
                                            cityFisrtList.get(i).setSign(false);
                                            for (int j = 0; j < selectCityList.size(); j++) {
                                                if (selectCityList.get(j).getId().equals(cityFisrtList.get(i).getId())) {
                                                    selectCityList.remove(selectCityList.get(j));
                                                }
                                            }
                                            sum=selectCityList.size();
                                            removeView(cityFisrtList.get(i));
                                        } else {

                                            if (sum >= 5) {
                                                Toast.makeText(SelectCityActicity.this, "选择城市已达最大值", Toast.LENGTH_SHORT).show();
                                            } else {
                                                selectCityList.add(cityFisrtList.get(i));
                                                cityFisrtList.get(i).setCheck(true);
                                                addView(cityFisrtList.get(i));
                                                sum=selectCityList.size();
                                            }
                                        }
                                        for (int j = 4; j < cityFisrtList.size(); j++) {
                                            cityFisrtList.get(j).setCheck(false);
                                            cityFisrtList.get(j).setSign(false);
                                        }
                                        addCity.setVisibility(View.GONE);
                                    } else {
                                        addCity.setVisibility(View.VISIBLE);
                                        for (int j = 4; j < cityFisrtList.size(); j++) {
                                            cityFisrtList.get(j).setCheck(false);
                                            cityFisrtList.get(j).setSign(false);
                                        }
                                        cityFisrtList.get(i).setCheck(true);
                                        cityFisrtList.get(i).setSign(true);
                                        getSecondList(cityFisrtList.get(i));
                                    }
                                } else {
                                    for (int j = num; j < cityFisrtList.size(); j++) {
                                        cityFisrtList.get(j).setCheck(false);
                                        cityFisrtList.get(j).setSign(false);
                                    }
                                    cityFisrtList.get(i).setCheck(true);
                                    cityFisrtList.get(i).setSign(true);
                                    getSecondList(cityFisrtList.get(i));
                                }
                                for (int j = 0; j < cityFisrtList.size(); j++) {
                                    for (int k = 0; k < selectCityList.size(); k++) {
                                        if (selectCityList.get(k).getId().substring(0, 2).equals(cityFisrtList.get(j).getId().substring(0, 2))) {
                                            cityFisrtList.get(j).setSign(true);
                                        }
                                    }
                                }
                                //Log.i("第二个adapter的数据",citySecondList.toString());
                            }
                            setNum();
                            selectFirstAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2:
                    selectCitySecondAdapter = new SelectCitySecondAdapter(SelectCityActicity.this, citySecondList);
                    addCity.setAdapter(selectCitySecondAdapter);
                    if(num==0) {
                        if(selectCityList!=null&&selectCityList.size()!=0) {
                            for (int i = 0; i < citySecondList.size(); i++) {
                                for (int j = 0; j < selectCityList.size(); j++) {
                                    if (citySecondList.get(i).getId().equals(selectCityList.get(j).getId())) {
                                        citySecondList.get(i).setCheck(true);
                                    }
                                }
                            }
                        }

                    }
                    addCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (num == 0) {
                                if (citySecondList.get(i).isCheck() == true) {
                                    citySecondList.get(i).setCheck(false);
                                    for (int j = 0; j < selectCityList.size(); j++) {
                                        if (selectCityList.get(j).getId().equals(citySecondList.get(i).getId())) {
                                            selectCityList.remove(selectCityList.get(j));

                                        }
                                    }
                                    sum=selectCityList.size();
                                    int mon = 0;
                                    for (int j = 0; j < citySecondList.size(); j++) {
                                        if (citySecondList.get(i).isCheck() == true) {
                                            mon++;
                                        }
                                    }
                                    if (mon == 0) {
                                        for (int j = 0; j < cityFisrtList.size(); j++) {
                                            if (cityFisrtList.get(j).getId().equals(citySecondList.get(0).getId())) {
                                                cityFisrtList.get(j).setSign(false);
                                            }
                                        }
                                    }
                                    cityFisrtList.get(position).setSign(true);
                                    cityFisrtList.get(position).setCheck(true);
                                    removeView(citySecondList.get(i));
                                } else {
                                    if (i == 0) {
                                        List<CityBean> removeList = new ArrayList<CityBean>();
                                        for (int j = 0; j < selectCityList.size(); j++) {
                                            if (selectCityList.get(j).getId().substring(0, 2).equals(citySecondList.get(0).getId().substring(0, 2))) {
                                                removeList.add(selectCityList.get(j));
                                                removeView(selectCityList.get(j));
                                            }
                                        }
                                        selectCityList.removeAll(removeList);
                                        getSecondCityListBean();
                                        sum = selectCityList.size();
                                    } else {
                                        for (int j = 0; j < selectCityList.size(); j++) {
                                            if (selectCityList.get(j).getId().equals(citySecondList.get(0).getId())) {
                                                selectCityList.remove(selectCityList.get(j));
                                                citySecondList.get(0).setCheck(false);
                                                removeView(citySecondList.get(0));
                                            }
                                        }
                                        sum = selectCityList.size();
                                    }

                                    if (sum >= 5) {
                                        Toast.makeText(SelectCityActicity.this, "选择城市已达最大值", Toast.LENGTH_SHORT).show();
                                    } else {
                                        selectCityList.add(citySecondList.get(i));
                                        citySecondList.get(i).setCheck(true);
                                        addView(citySecondList.get(i));
                                        sum=selectCityList.size();
                                    }

                                }
                                setNum();
                                selectCitySecondAdapter.notifyDataSetChanged();
                            } else {
                                if ("createPersonInfo".equals(from)) {
                                    CreateResumePersonInfoActivity.setPlaceId(citySecondList.get(i).getId());
                                    CreateResumePersonInfoActivity.setPlaceText(citySecondList.get(i).getName());
                                } else if ("recommendJob".equals(from)) {
                                    RecommendJobFragment.setPlaceId(citySecondList.get(i).getId());
                                    RecommendJobFragment.setPlaceText(citySecondList.get(i).getName());
                                } else if ("findJob".equals(from)) {
                                    FindjobFragment.setPlaceId(citySecondList.get(i).getId());
                                    FindjobFragment.setPlaceText(citySecondList.get(i).getName());
                                }else if("createJob".equals(from)){
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceId(citySecondList.get(i).getId());
                                    CreateResumeJobActivity.createResumeJobActivity.setPlaceText(citySecondList.get(i).getName());
                                } else if("resumePersonInfo".equals(from)){
                                    ResumePersonInfoActivity.setPlaceId(citySecondList.get(i).getId());
                                    ResumePersonInfoActivity.setPlaceText(citySecondList.get(i).getName());
                                }else if("resumePlant".equals(from)){
                                    ModifyPlantActivity.modifyPlantActivity.setPlaceId(citySecondList.get(i).getId());
                                    ModifyPlantActivity.modifyPlantActivity.setPlaceText(citySecondList.get(i).getName());
                                }else if("subscription".equals(from)) {
                                    SubscriptionActivity.subscriptionActivity.setPlaceId(citySecondList.get(i).getId());
                                    SubscriptionActivity.subscriptionActivity.setPlaceText(citySecondList.get(i).getName());
                                }   else {
                                    ModifyExpActivity.modifyExpActivity.setPlaceId(citySecondList.get(i).getId());
                                    ModifyExpActivity.modifyExpActivity.setPlaceText(citySecondList.get(i).getName());
                                }
                                MyUtils.selectCityId = citySecondList.get(i).getId();
                                MyUtils.selectCityZh = citySecondList.get(i).getName();
                                submit();
                            }
                        }
                    });
                    break;
            }
        }
    };

    private void submit() {
        finish();
    }

    /**
     * 获得右边adapter的数据
     *
     * @param cityBean
     */
    private void getSecondList(CityBean cityBean) {
        citySecondList.clear();
        for (int i = 0; i < cityList2.size(); i++) {
            if (cityBean.getId().substring(0, 2).equals(cityList2.get(i).getId().substring(0, 2))) {

                citySecondList.add(cityList2.get(i));
            }
        }
        Message message = Message.obtain();
        message.what = 2;
        handler.sendMessage(message);

    }

    private void setNum() {
        placeselectSelectedinfo.setText(sum + "/5");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity_tosearch);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        from = getIntent().getStringExtra("from");
        if("2".equals(type)){
            selectCityList= (List<CityBean>) getIntent().getSerializableExtra("selectCity");
            if(selectCityList!=null&&selectCityList.size()!=0){
                sum=selectCityList.size();
            }else{
                sum=0;
            }
            setNum();
            for (int i = 0; i < selectCityList.size(); i++) {
                addView(selectCityList.get(i));
            }
        }
        try {
            JSONArray cityJSONArray = NetService.getCityAsJSONArray(
                    this, "city.json");
            cityList = GetJssonList.getCityBeanList(cityJSONArray);
            cityList2 = GetJssonList.getCityBeanList(cityJSONArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("2".equals(type)) {
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getId().endsWith("00")) {
                    cityFisrtList.add(cityList.get(i));
                }
            }
            num = 0;
            for (int i = 0; i < cityFisrtList.size(); i++) {
                for (int j = 0; j < selectCityList.size(); j++) {
                    if (selectCityList.get(j).getId().length() > 2) {
                        if (selectCityList.get(j).getId().substring(0,2).equals(cityFisrtList.get(i).getId().substring(0, 2))) {
                           if(i<4){
                               cityFisrtList.get(i).setCheck(true);
                               cityFisrtList.get(i).setSign(true);
                           }else{
                               cityFisrtList.get(i).setCheck(false);
                               cityFisrtList.get(i).setSign(true);
                           }
                        }
                    }
                }
            }
        } else {
            placeselectShowinforlayout.setVisibility(View.GONE);
            placeselectShowmessage.setVisibility(View.GONE);
            postPlaceselectConfirm.setVisibility(View.GONE);
            if (MyUtils.currentCityZh != null && !"".equals(MyUtils.currentCityZh)) {
                CityBean cityBean = new CityBean();
                cityBean.setId(ResumeInfoIDToString.getCityID(this, MyUtils.currentCityZh, true));
                cityBean.setName(MyUtils.currentCityZh);
                cityBean.setCheck(false);
                cityBean.setSign(false);
                cityFisrtList.add(cityBean);
            } else {
                CityBean cityBean = new CityBean();
                cityBean.setId("");
                cityBean.setName("定位失败");
                cityBean.setCheck(false);
                cityFisrtList.add(cityBean);
            }
            if ("recommendJob".equals(from) || "findJob".equals(from)) {
                num = 12;
                CityBean cityBean = new CityBean();
                cityBean.setId("");
                cityBean.setName("全国");
                cityBean.setCheck(false);
                cityBean.setSign(false);
                cityFisrtList.add(cityBean);
            } else {
                num = 11;
            }
            CityBean cityBean1 = new CityBean("1100", "北京", false, false);
            CityBean cityBean2 = new CityBean("1200", "上海", false, false);
            CityBean cityBean3 = new CityBean("1801", "广州", false, false);
            CityBean cityBean4 = new CityBean("1802", "深圳", false, false);
            CityBean cityBean5 = new CityBean("1300", "天津", false, false);
            CityBean cityBean6 = new CityBean("1400", "重庆", false, false);
            cityFisrtList.add(cityBean1);
            cityFisrtList.add(cityBean2);
            cityFisrtList.add(cityBean3);
            cityFisrtList.add(cityBean4);
            cityFisrtList.add(cityBean5);
            cityFisrtList.add(cityBean6);
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getId().endsWith("00")) {

                    cityFisrtList.add(cityList.get(i));
                }
            }
        }
        Message mes = Message.obtain();
        mes.what = 1;
        handler.sendMessage(mes);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 添加已选城市视图
     *
     * @param cityBean
     */
    private void addView(final CityBean cityBean) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 15;
        params.topMargin = 12;
        params.bottomMargin = 12;
        params.rightMargin = 15;
        final TextView tv = (TextView) LayoutInflater.from(this).inflate(
                R.layout.textview_selected, null, false);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(6, 4, 6, 4);
        tv.setText(cityBean.getName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityList.remove(cityBean);

                placeselectShowinforlayout
                        .removeView(placeselectShowinforlayout
                                .findViewWithTag(cityBean.getId()));
                getFirstCityList(cityBean);
                getSecondCityListBean();
                sum = selectCityList.size();
                setNum();
                if (selectFirstAdapter != null) {
                    selectFirstAdapter.notifyDataSetChanged();
                }
                if (selectCitySecondAdapter != null) {
                    selectCitySecondAdapter.notifyDataSetChanged();
                }
             /*   checkStateHashMap.remove(keyString);
                refreshShowInfo(true);
                adapter.notifyDataSetChanged();
                CreateSelectPlaceToResumeActivity placeSelectedActivity = CreateSelectPlaceToResumeActivity.this;
                if (placeSelectedActivity != null) {
                    placeSelectedActivity.refreshShowInfo(true);
                    placeSelectedActivity.adapter.notifyDataSetChanged();
                }*/
            }
        });
        tv.setTag(cityBean.getId());
        placeselectShowinforlayout.addView(tv);
    }

    private void removeView(CityBean cityBean) {
        placeselectShowinforlayout
                .removeView(placeselectShowinforlayout
                        .findViewWithTag(cityBean.getId()));
        /*Log.i("选择2",selectFunctionBeenList.toString());*/


    }

    private void getFirstCityList(CityBean cityBean) {
        for (int i = 0; i < cityFisrtList.size(); i++) {
            if (cityBean.getId().equals(cityFisrtList.get(i).getId())) {
                cityFisrtList.get(i).setCheck(false);
                cityFisrtList.get(i).setSign(false);
            }
        }
        int count=0;
        for(int i=0;i<selectCityList.size();i++){
            if(selectCityList.get(i).getId().substring(0,2).equals(cityBean.getId().substring(0,2))){
                count++;
            }
        }
        if(count==0){
            for (int i = 0; i < cityFisrtList.size(); i++) {
                if (cityBean.getId().subSequence(0,2).equals(cityFisrtList.get(i).getId().substring(0,2))) {
                    cityFisrtList.get(i).setCheck(false);
                    cityFisrtList.get(i).setSign(false);
                }
            }
        }
    }

    private void getSecondCityListBean() {
        for (int i = 0; i < citySecondList.size(); i++) {
            citySecondList.get(i).setCheck(false);
            for (int j = 0; j < selectCityList.size(); j++) {
                if (citySecondList.get(i).equals(selectCityList.get(j))) {
                    citySecondList.get(i).setCheck(true);
                }
            }
        }

        /*Log.i("选择3",functionBeenList3.toString());*/
    }

    @OnClick({R.id.iv_placeselect_back, R.id.post_placeselect_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_placeselect_back:
                finish();
                break;
            case R.id.post_placeselect_confirm:
                if(selectCityList!=null&&!"".equals(selectCityList)&&selectCityList.size()!=0) {
                    if("resumeJobOrder".equals(from)) {
                        ResumeJobOrderActivity.getInstance().setPlaceId(selectCityList);
                    }else {
                        CreateResumeJobOrderActivity.getInstance().setPlaceId(selectCityList);
                    }
                    finish();
                }else{
                    Toast.makeText(SelectCityActicity.this,"请选择城市",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
