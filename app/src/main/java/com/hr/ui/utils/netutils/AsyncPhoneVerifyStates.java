package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 修改验证手机号
 */
public class AsyncPhoneVerifyStates {
    private Context context;
    private String user_phone;
    private Handler handler = null;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    // System.out.println("register-result:" + json);
                    JSONObject jsonObject = new JSONObject(json);

                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:
                            int state =jsonObject.getInt("flag_exist");
                            Message message = handler.obtainMessage();
                            if (state==0){
                                message.what = 1;
                            }else if (state==1){
                                message.what = 2;
                            }else {
                                message.what = 3;
                            }
                            handler.sendMessage(message);
                            break;
                        default:
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;

    };

    public AsyncPhoneVerifyStates(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void execute(String phone) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_check.phoneexists");
            requestParams.put("phone",phone);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}