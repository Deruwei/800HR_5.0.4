package com.hr.ui.utils.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.model.FavouriteJobInfo;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 收藏职位
 *
 * @author 800hr:xuebaohua
 */
public class Async_DeleteCollectPostion {
    private Context context;
    private Handler handler;
    Button button;
    ProgressDialog progressDialog;
    ArrayList<FavouriteJobInfo> listFavouriteJobInfos;
    int position;
    BaseAdapter baseAdapter;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    //System.out.println("取消收藏职位：" + jsonObject);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            if (button != null) {
                                button.setText("收藏该职位");
                                PostParticularsActivity.is_favourite = "0";
                            }
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            if (listFavouriteJobInfos != null && position >= 0) {

                                listFavouriteJobInfos.remove(position);
                                baseAdapter.notifyDataSetChanged();
                            }
                            Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT)
                                    .show();
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

    public Async_DeleteCollectPostion(Context context, Button button,
                                      ProgressDialog progressDialog,
                                      ArrayList<FavouriteJobInfo> listFavouriteJobInfos, int position,
                                      BaseAdapter baseAdapter) {
        this.context = context;
        this.button = button;
        this.progressDialog = progressDialog;
        this.listFavouriteJobInfos = listFavouriteJobInfos;
        this.position = position;
        this.baseAdapter = baseAdapter;
    }

    public void execute(String method, String job_id, String record_id) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", method);
            requestParams.put("job_id", job_id);
            requestParams.put("record_id", record_id);

            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            //System.out.println("取消");
            e.printStackTrace();
        }
    }
}