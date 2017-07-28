package com.hr.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hr.ui.R;
import com.hr.ui.activity.CompanyParticularActivity;
import com.hr.ui.adapter.FindAdapter;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.netutils.NetService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerRecruitmentFragment extends Fragment {
    private View view;
    private ListView lv_pager_recruitment;
    private FindAdapter findAdapter;
    private Context mContext;
    private ArrayList<Industry> dataList;
    /**
     * 品牌招聘对象
     */
    private Industry industry;

    @SuppressLint("ValidFragment")
    public PagerRecruitmentFragment(Context context, ArrayList<Industry> data) {
        this.mContext = context;
        this.dataList = data;
    }

    @SuppressLint("ValidFragment")
    public PagerRecruitmentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pager_recruitment, container, false);
        initView();
        return view;
    }

    private void initView() {
        lv_pager_recruitment = (ListView) view.findViewById(R.id.lv_pager_recruitment);
        if (dataList != null) {
            findAdapter = new FindAdapter(mContext, dataList, 1);
            lv_pager_recruitment.setAdapter(findAdapter);
            lv_pager_recruitment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        }
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
}
