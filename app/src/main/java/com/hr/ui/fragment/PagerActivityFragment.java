package com.hr.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.BaseActivity;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.adapter.FindAdapter;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.GetDataInfo;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.OnItemClick;
import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.CustomDialog;


import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 品牌招聘
 */
public class PagerActivityFragment extends BaseFragment {

    @Bind(R.id.tv_actNoData)
    TextView tvActNoData;
    @Bind(R.id.lv_pager_activity)
    RecyclerView lvPagerActivity;
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

    private RefleshDialogUtils dialogUtils;
    /**
     * 品牌招聘对象
     */
    private Industry industry;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if(lvPagerActivity!=null) {
                    json_result = (String) msg.obj;
                    dataList = new ArrayList<>();
                    dialogUtils.dismissDialog();
                    dataList = GetJssonList.getEnterpriseJson(ad_type, json_result);
                    if (dataList != null) {
                        if (dataList.size() != 0) {
                            findAdapter.setData(dataList);
                            lvPagerActivity.setAdapter(findAdapter);
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
                            lvPagerActivity.setVisibility(View.VISIBLE);
                            if (tvActNoData != null) {
                                tvActNoData.setVisibility(View.GONE);
                            }
                        } else {
                            lvPagerActivity.setVisibility(View.GONE);
                            if (tvActNoData != null) {
                                tvActNoData.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }else{
                dialogUtils.dismissDialog();
            }
        }
    };

    /**
     * 向服务器请求数据
     */
    public void loadNetMsg() {
        dialogUtils.showDialog();
        NetService service = new NetService(getActivity(), handlerService);
        service.execute(GetDataInfo.getData(ad_type, getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_activity, container, false);
        ButterKnife.bind(this, view);
        dialogUtils=new RefleshDialogUtils(getActivity());
        initView();
        isCreateView = true;
        loadNetMsg();
        return view;
    }

    @SuppressLint("ValidFragment")
    public PagerActivityFragment() {

    }

    @SuppressLint("ValidFragment")
    public PagerActivityFragment(Context context, int ad_type) {
        this.mContext = context;
        this.ad_type = ad_type;
    }


    private void initView() {
        // Log.i("this", dataList.toString());
        findAdapter = new FindAdapter(mContext, 2);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lvPagerActivity.setLayoutManager(manager);
        lvPagerActivity.addItemDecoration(new SpacesItemDecoration(10));
    }
  /*  @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }
    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
            loadNetMsg();
    }*/
 /*   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint())
            lazyLoad();
    }*/
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
        dialogUtils.dismissDialog();
    }

}
