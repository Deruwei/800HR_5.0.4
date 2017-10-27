package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.RefuseComResultAdapter;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.tools.ListViewUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 屏蔽企业列表展示页
 */
public class RefuseComResultActivity extends Activity {
    @Bind(R.id.iv_refusecomresult_back)
    ImageView ivRefusecomresultBack;
    @Bind(R.id.tv_refuseresult_keyword)
    TextView tvRefuseresultKeyword;
    @Bind(R.id.tv_refuseresult_num)
    TextView tvRefuseresultNum;
    @Bind(R.id.lv_refuseresult)
    ListView lvRefuseresult;
    @Bind(R.id.bt_refuseresult_confirm)
    Button btRefuseresultConfirm;
    @Bind(R.id.bt_refuseresult_cancel)
    Button btRefuseresultCancel;
    private String keywords;
    private ArrayList<HashMap<String, String>> comRefuseList = new ArrayList<>();
    private JSONArray jsonComListArray;
    private RefuseComResultAdapter adapter;
    private Context mContext = RefuseComResultActivity.this;
    private int totals;
    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
//                    Toast.makeText(mContext,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("error_code") == 0) {
                        totals = jsonObject.getInt("totals");
                        jsonComListArray = jsonObject.getJSONArray("ente_list");
                        for (int i = 0; i < jsonComListArray.length(); i++) {
                            HashMap<String, String> refuseMap = new HashMap<>();
                            JSONObject jsonObjComRefuse = (JSONObject) jsonComListArray.get(i);
                            refuseMap.put("enterprise_name", jsonObjComRefuse.get("enterprise_name").toString());
//                            Toast.makeText(mContext, jsonObjComRefuse.get("enterprise_name").toString(), Toast.LENGTH_SHORT).show();
                            comRefuseList.add(refuseMap);
                        }
                        initView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };
    public Handler handlerAddRefuse = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject2 = new JSONObject(json);
                    if (jsonObject2.getInt("error_code") == 0) {
                        Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void initView() {
        tvRefuseresultKeyword.setText(keywords);
        tvRefuseresultNum.setText(totals + "");
        adapter = new RefuseComResultAdapter(comRefuseList, mContext);
//        ListViewUtil.setListViewHeightBasedOnChildren(lvRefuseresult);
        lvRefuseresult.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }
        setContentView(R.layout.activity_refuse_com_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        keywords = intent.getStringExtra("keywords");
//        Toast.makeText(mContext, keywords, Toast.LENGTH_SHORT).show();
        loadNetComListData();
    }

    /**
     * 加载网络数据
     */
    public void loadNetComListData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_eliminate.entelist");
        params.put("eliminate_txt", keywords);
//        params.put("enterprise_id", "");
        params.put("nums", "");
        NetService service = new NetService(this, handlerService);
        service.execute(params);
    }

    /**
     * 添加屏蔽
     */
    public void addRefuseCom() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_eliminate.set");
        params.put("eliminate_txt", keywords);
        NetService service = new NetService(this, handlerAddRefuse);
        service.execute(params);
    }

    @OnClick({R.id.iv_refusecomresult_back, R.id.bt_refuseresult_confirm, R.id.bt_refuseresult_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refusecomresult_back:
                finish();
                break;
            case R.id.bt_refuseresult_confirm:
                if (totals <= 50 && totals > 0) {
                    addRefuseCom();
                } else if (totals == 0) {
                    Toast.makeText(mContext, "没有搜索结果，无法提交", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "搜索结果超过50个，请使用更精确关键词", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_refuseresult_cancel:
                finish();
                break;
        }
    }
}
