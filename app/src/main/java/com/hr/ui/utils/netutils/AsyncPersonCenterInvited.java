package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hr.ui.model.Invitedinfo;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 获取个人中心-人事经理来信
 *
 * @author 800hr:xuebaohua
 */
public class AsyncPersonCenterInvited {
    private Context context;
    private ArrayList<Invitedinfo> listInvitedinfoInfos;
    private String listTagString = "invited_list";// json中数组对象名称
    private BaseAdapter adapter;
    ListView listview;
    private Handler handlerService = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    //System.out.println("AsyncPersonCenterInvited" + json);
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 请求成功
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            JSONArray jsonArray = jsonObject
                                    .getJSONArray(listTagString);

                            // if(jsonArray.length()>0){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = (JSONObject) jsonArray
                                        .get(i);
                                Invitedinfo invitedinfo = new Invitedinfo();
                                invitedinfo.setEnterprise_name(jsonObject2
                                        .getString("enterprise_name"));
                                invitedinfo.setEnterprise_id(jsonObject2
                                        .getString("enterprise_id"));
                                invitedinfo.setJob_name(jsonObject2
                                        .getString("job_name"));
                                invitedinfo.setJob_id(jsonObject2
                                        .getString("job_id"));
                                invitedinfo.setIs_new(jsonObject2
                                        .getString("is_new"));
                                invitedinfo.setIs_email(jsonObject2
                                        .getString("is_email"));
                                invitedinfo.setEmail_content(jsonObject2
                                        .getString("email_content"));
                                invitedinfo.setInvited_time(jsonObject2
                                        .getString("invited_time"));

                                invitedinfo.setSms_content(jsonObject2
                                        .getString("sms_content"));
                                invitedinfo.setInvited_title(jsonObject2
                                        .getString("invited_title"));
                                listInvitedinfoInfos.add(invitedinfo);
                            }
                            // 获取记录数量
                            // JSONObject jsonObject2 =
                            // jsonObject.getJSONObject("navpage_info");
                            // record_nums = jsonObject2.getString("record_nums");
                            adapter.notifyDataSetChanged();
                            break;
                        default:
                            Toast.makeText(context,
                                    Rc4Md5Utils.getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;
    };

    /**
     *
     */
    public AsyncPersonCenterInvited(Context context, ArrayList<Invitedinfo> listInvitedinfoInfos,
                                    BaseAdapter baseAdapter, ListView listview) {
        this.context = context;
        this.listInvitedinfoInfos = listInvitedinfoInfos;
        this.adapter = baseAdapter;
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
            //System.out.println("取消-人事经理来信");
            e.printStackTrace();
        }
    }
}