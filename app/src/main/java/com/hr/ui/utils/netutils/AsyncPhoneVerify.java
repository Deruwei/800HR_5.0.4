package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 修改验证手机号
 */
public class AsyncPhoneVerify {
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
                        case 0:// 注册成功
                            Toast.makeText(context, "验证成功", Toast.LENGTH_SHORT).show();
                            Message message = handler.obtainMessage();
                            message.what = 0;
                            handler.sendMessage(message);
                            break;
                        default:
                            Toast.makeText(context,"请输入正确的验证码", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;

    };

    public AsyncPhoneVerify(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void execute(String user_phone, String token) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.chgphone");
            requestParams.put("user_phone", user_phone);
            requestParams.put("token", token);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}