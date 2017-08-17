package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.BaseActivity;
import com.hr.ui.activity.BaseFragmentActivity;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.adapter.FindAdapter;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.GetDataInfo;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerCompanyFragment extends BaseFragment {

    @Bind(R.id.tv_comNoData)
    TextView tvComNoData;
    @Bind(R.id.lv_pager_company)
    RecyclerView lvPagerCompany;
    private View view;
    private FindAdapter findAdapter;
    private Context mContext;
    private ArrayList<Industry> dataList;
    private int ad_type;
    private String json_result;
    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    /**
     * 品牌招聘对象
     */
    private Industry industry;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;
                //Log.i("json的数据",json_result);
                dataList = new ArrayList<>();
                dataList = GetJssonList.getDazzleJson(ad_type, json_result);
                if (dataList != null) {
                    if (dataList.size() != 0) {
                        findAdapter = new FindAdapter(mContext, dataList, 2);
                        lvPagerCompany.setAdapter(findAdapter);
                        findAdapter.setOnItemClick(new OnItemClick() {
                            @Override
                            public void ItemClick(View view, int position) {
                                industry = dataList.get(position);
                                if (industry.getTopic_type() == 1) {// 专题网址
                                    openBrowser(industry.getTopic_url());
                                } else if (industry.getTopic_type() == 2) {// 企业详情
                                    Intent intent = new Intent(mContext,
                                            CompanyParticularActivity.class);
                                    intent.putExtra("Enterprise_id", industry.getEnterprise_id());
                                    startActivity(intent);
                                }
                                totalAdNum(industry.getA_id());
                            }
                        });
                        lvPagerCompany.setVisibility(View.VISIBLE);
                        tvComNoData.setVisibility(View.GONE);
                    } else {
                        lvPagerCompany.setVisibility(View.GONE);
                        tvComNoData.setVisibility(View.VISIBLE);
                    }
                }
            } else {
//                Message message = Message.obtain();
//                message.what = 1002;
//                handlerUI.sendMessage(message);
            }
        }
    };

    @SuppressLint("ValidFragment")
    public PagerCompanyFragment(Context context, int ad_type) {
        this.mContext = context;
        this.ad_type = ad_type;

    }

    @SuppressLint("ValidFragment")
    public PagerCompanyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_company, container, false);
        ButterKnife.bind(this, view);
        initView();
        isCreateView = true;
        return view;
    }

    /**
     * 向服务器请求数据
     */
    public void loadNetMsg() {
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(GetDataInfo.getData(ad_type, getActivity()));
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvPagerCompany.setLayoutManager(manager);
        lvPagerCompany.addItemDecoration(new SpacesItemDecoration(10));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }
    protected void lazyLoad() {
        //加载数据操作
        loadNetMsg();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
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
