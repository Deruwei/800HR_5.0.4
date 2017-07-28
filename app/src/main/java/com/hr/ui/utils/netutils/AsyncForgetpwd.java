package com.hr.ui.utils.netutils;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

/**
 * 重置密码
 *
 * @author 800hr:xuebaohua
 */
public class AsyncForgetpwd {
    private Activity context;
    private boolean isFromPush;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 密码找回成功
                            if (isFromPush) {
                                context.finish();
                            } else {
                                context.finish();
                            }
                            Toast.makeText(context,
                                    context.getString(R.string.enter_findpwd),
                                    Toast.LENGTH_SHORT).show();
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

    public AsyncForgetpwd(Activity context, boolean isFromPush) {
        this.context = context;
        this.isFromPush = isFromPush;
    }

    public void execute(String email, String user_name) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.resetpwd");
            requestParams.put("email", email);
            requestParams.put("user_name", user_name);

            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}