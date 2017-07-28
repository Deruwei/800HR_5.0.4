package com.hr.ui.utils.netutils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.model.BrowsedInfo;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

/**
 * 获取个人中心-谁看过我的简历
 *
 * @author 800hr:xuebaohua
 */
public class AsyncPersonCenterBrowsed {
    private Context context;
    private ArrayList<BrowsedInfo> listBrowsedInfos;
    private String listTagString;// json中数组对象名称
    private BaseAdapter adapter;
    ListView listview;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    // System.out.println("AsyncPersonCenterBrowsed" + json +
                    // "");
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 请求成功
                            JSONArray jsonArray = jsonObject
                                    .getJSONArray(listTagString);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray
                                            .get(i);
                                    BrowsedInfo browsedInfo = new BrowsedInfo();
                                    browsedInfo.setEnterprise_name(jsonObject2
                                            .getString("enterprise_name"));
                                    browsedInfo.setEnterprise_id(jsonObject2
                                            .getString("enterprise_id"));
                                    browsedInfo.setBrowsed_time(jsonObject2
                                            .getString("browsed_time"));
                                    browsedInfo.setStuffmunber(jsonObject2
                                            .getString("stuffmunber"));
                                    listBrowsedInfos.add(browsedInfo);
                                }
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        default:
                            Toast.makeText(
                                    context,
                                    Rc4Md5Utils
                                            .getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }

        ;

    };

    /**
     *
     */
    public AsyncPersonCenterBrowsed(Context context,
                                    ArrayList<BrowsedInfo> listBrowsedInfos, String listTagString,
                                    BaseAdapter adapter, ListView listview) {
        this.context = context;
        this.listTagString = listTagString;
        this.listBrowsedInfos = listBrowsedInfos;
        this.adapter = adapter;
        this.listview = listview;
    }

    public void execute(String method, String page, String page_nums) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", method);
            requestParams.put("page", page);
            requestParams.put("page_nums", page_nums);

            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            // System.out.println("取消-谁看过我的简历");
            e.printStackTrace();
        }
    }

}