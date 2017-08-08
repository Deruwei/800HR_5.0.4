package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.hr.ui.utils.netutils.NetService;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerCompanyFragment extends Fragment {

    @Bind(R.id.tv_comNoData)
    TextView tvComNoData;
    private View view;
    private ListView lv_pager_recruitment;
    private FindAdapter findAdapter;
    private Context mContext;
    private ArrayList<Industry> dataList;
    private int  ad_type;
    private String json_result;
    /**
     * 品牌招聘对象
     */
    private Industry industry;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                json_result = (String) msg.obj;

                    dataList=new ArrayList<>();
                    dataList=GetJssonList.getDazzleJson(ad_type,json_result);
                    if(dataList.size()!=0) {
                        findAdapter = new FindAdapter(mContext, dataList, 2);
                        lv_pager_recruitment.setAdapter(findAdapter);
                        lv_pager_recruitment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                        lv_pager_recruitment.setVisibility(View.VISIBLE);
                        tvComNoData.setVisibility(View.GONE);
                    }else{
                        lv_pager_recruitment.setVisibility(View.GONE);
                        tvComNoData.setVisibility(View.VISIBLE);
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
       this.ad_type=ad_type;

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
        lv_pager_recruitment = (ListView) view.findViewById(R.id.lv_pager_company);
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
