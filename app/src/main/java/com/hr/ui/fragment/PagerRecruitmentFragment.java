package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.adapter.FindAdapter;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.GetDataInfo;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerRecruitmentFragment extends Fragment {
    @Bind(R.id.tv_comNoData)
    TextView tvComNoData;
    private View view;
    private RecyclerView lv_pager_recruitment;
    private FindAdapter findAdapter;
    private Context mContext;
    private ArrayList<Industry> dataList;
    private int ad_type;
    private String json_result;

    /**
     * 品牌招聘对象
     */
    private Industry industry;

    @SuppressLint("ValidFragment")
    public PagerRecruitmentFragment(Context context, int ad_type) {
        this.mContext = context;
       this.ad_type=ad_type;
    }
    @Subscribe
    public void onEvent(ArrayList<Industry> dataList){
        this.dataList=dataList;
    }
    @SuppressLint("ValidFragment")
    public PagerRecruitmentFragment() {

    }
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;
                // 1001 成功 1002失败
                dataList=new ArrayList<>();
                dataList= GetJssonList.getSpecialJson(ad_type,json_result);
                if(dataList.size()!=0) {
                    lv_pager_recruitment.setVisibility(View.VISIBLE);
                    tvComNoData.setVisibility(View.GONE);
                    findAdapter = new FindAdapter(mContext, dataList, 1);
                    lv_pager_recruitment.setAdapter(findAdapter);
                    findAdapter.setOnItemClick(new OnItemClick() {
                        @Override
                        public void ItemClick(View view, int position) {
                            industry = dataList.get(position);
                            if (industry.getTopic_type() == 1) {// 专题网址
                                openBrowser(industry.getTopic_url());

                            } else if (industry.getTopic_type() == 2) {// 企业详情
                                Intent intent = new Intent(mContext, CompanyParticularActivity.class);
                                intent.putExtra("Enterprise_id", industry.getEnterprise_id());
                                startActivity(intent);
                            }
                            totalAdNum(industry.getA_id());
                        }
                    });
                    tvComNoData.setVisibility(View.GONE);
                    lv_pager_recruitment.setVisibility(View.VISIBLE);
                }else{
                    tvComNoData.setVisibility(View.VISIBLE);
                    lv_pager_recruitment.setVisibility(View.GONE);
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_recruitment, container, false);
        ButterKnife.bind(this, view);
        initView();
        loadNetMsg();
        return view;
    }

    /**
     * 向服务器请求数据
     */
    public void loadNetMsg() {
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(GetDataInfo.getData(ad_type,getActivity()));
    }

    private void initView() {
       // Log.i("this", dataList.toString());
        lv_pager_recruitment = (RecyclerView) view.findViewById(R.id.lv_pager_recruitment);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_pager_recruitment.setLayoutManager(manager);
        lv_pager_recruitment.addItemDecoration(new SpacesItemDecoration(10));
    }

    public void upData() {
        findAdapter.notifyDataSetChanged();
    }

    /**
     * 统计广告点击次数
     *
     * @param a_id
     */
    private void totalAdNum(int a_id) {
        NetService service = new NetService(mContext, new Handler(), false);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "mobilead.tally");
        params.put("a_id", "" + a_id);
        service.execute(params);
    }

    private void openBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("url无效");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
