package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 发送验证码
 */
public class AsyncNewGetBase64 {
    private Context context;
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
                            String base64=jsonObject.getString("captcha");
                            Message message=Message.obtain();
                            message.what=1;
                            message.obj=base64;
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

    public AsyncNewGetBase64(Context context,Handler handler) {
        this.context = context;
        this.handler=handler;
    }


    public void execute() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();

            requestParams.put("method", "user.captcha");
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}