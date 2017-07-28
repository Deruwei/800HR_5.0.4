package com.hr.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.RefuseComAdapter;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefuseCompanyActivity extends Activity {
    private Context mContext = RefuseCompanyActivity.this;

    @Bind(R.id.iv_refusecompany_back)
    ImageView ivRefusecompanyBack;
    @Bind(R.id.et_refusecom_keyword)
    EditText etRefusecomKeyword;
    @Bind(R.id.bt_refusecom_add)
    Button btRefusecomAdd;
    @Bind(R.id.lv_refusecom)
    ListView lvRefusecom;
    @Bind(R.id.tv_refusecom_add1)
    TextView tvRefusecomAdd1;
    @Bind(R.id.tv_refusecom_add2)
    TextView tvRefusecomAdd2;
    private ArrayList<HashMap<String, String>> comRefuseList;
    private JSONArray jsonComListArray;
    private RefuseComAdapter adapter;
    /**
     * 访问网络
     */
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
//                   Toast.makeText(mContext,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("error_code") == 0) {
                        comRefuseList = new ArrayList<>();
                        jsonComListArray = jsonObject.getJSONArray("eliminate_list");
                        for (int i = 0; i < jsonComListArray.length(); i++) {
                            HashMap<String, String> refuseMap = new HashMap<>();
                            JSONObject jsonObjComRefuse = (JSONObject) jsonComListArray.get(i);
                            refuseMap.put("eliminate_txt", jsonObjComRefuse.get("eliminate_txt").toString());
                            refuseMap.put("id", jsonObjComRefuse.get("id").toString());
                            comRefuseList.add(refuseMap);
                        }
                    }
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };
    /**
     * 访问网络
     */
    private Handler handlerService2 = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
//                   Toast.makeText(mContext,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt("error_code") == 0) {
                        comRefuseList = new ArrayList<>();
                        jsonComListArray = jsonObject.getJSONArray("eliminate_list");
                        for (int i = 0; i < jsonComListArray.length(); i++) {
                            HashMap<String, String> refuseMap = new HashMap<>();
                            JSONObject jsonObjComRefuse = (JSONObject) jsonComListArray.get(i);
                            refuseMap.put("eliminate_txt", jsonObjComRefuse.get("eliminate_txt").toString());
                            refuseMap.put("id", jsonObjComRefuse.get("id").toString());
                            comRefuseList.add(refuseMap);
                        }
                    }
                    if (etRefusecomKeyword.getText().toString().trim().length() > 0) {
                        if (comRefuseList.size() < 20) {
                            Intent intent = new Intent(mContext, RefuseComResultActivity.class);
                            intent.putExtra("keywords", etRefusecomKeyword.getText().toString().trim());
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "您最多可以添加20个屏蔽关键词", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "请输入关键词", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };
    /**
     * 访问网络
     */
    private Handler handlerDelete = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("error_code") == 0) {
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        loadNetComListData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };

    private void initView() {
        adapter = new RefuseComAdapter(comRefuseList, mContext);
        lvRefusecom.setAdapter(adapter);
        tvRefusecomAdd1.setText(comRefuseList.size() + "");
        tvRefusecomAdd2.setText((20 - comRefuseList.size()) + "");
//        ListViewUtil.setListViewHeightBasedOnChildren(lvRefusecom);
        lvRefusecom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isUploadDialog(position);

            }
        });
    }

    private void isUploadDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("是否删除屏蔽关键词" + "\"" + comRefuseList.get(position).get("eliminate_txt") + "\"？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadDeleteComListData(comRefuseList.get(position).get("id"));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse_company);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNetComListData();
    }

    @OnClick({R.id.iv_refusecompany_back, R.id.bt_refusecom_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refusecompany_back:
                finish();
                break;
            case R.id.bt_refusecom_add:
                loadNetDataAgain();
                break;
        }
    }

    /**
     * 加载网络数据
     */
    public void loadNetComListData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_eliminate.list");
        NetService service = new NetService(this, handlerService);
        service.execute(params);
    }

    /**
     * 加载网络数据
     */
    public void loadNetDataAgain() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_eliminate.list");
        NetService service = new NetService(this, handlerService2);
        service.execute(params);
    }

    /**
     * 删除
     */
    public void loadDeleteComListData(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "user_eliminate.del");
        params.put("id", id);
        NetService service = new NetService(this, handlerDelete);
        service.execute(params);
    }
}
