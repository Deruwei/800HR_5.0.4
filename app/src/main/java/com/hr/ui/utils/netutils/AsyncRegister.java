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
 * 注册用户
 */
public class AsyncRegister {
    private Context context;
    private String username;
    private Handler handler = null;
    private String pwdString = null;
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
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT)
                                    .show();
                            // 存储用户数据
                            MyUtils.isLogin = true;
                            MyUtils.username = username;
                            MyUtils.userID = jsonObject.getString("user_id");
                            MyUtils.industryId = jsonObject.getString("industry");
                            SharedPreferencesUtils sp = new SharedPreferencesUtils(context);
                            sp.setIntValue(Constants.INDUSTRY, Integer.parseInt(MyUtils.industryId));
                            sp.setStringValue(Constants.USERNAME, MyUtils.username);
                            sp.setStringValue(Constants.USERID, MyUtils.userID);
                            sp.setStringValue(Constants.PASSWORD, pwdString);
//                            Intent intent = new Intent(context, MainActivity.class);
//                            context.startActivity(intent);
                            SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
                            AsyncLogin asyncLogin = new AsyncLogin(context, handler);
                            asyncLogin.execute(username, pwdString, sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
                            Message message = handler.obtainMessage();
                            message.what = 0;
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

    public AsyncRegister(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }


    public void execute(String emailaddress, String user_name, String user_pwd,
                        String industry) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();

            requestParams.put("method", "user.register");
            requestParams.put("emailaddress", emailaddress);
            requestParams.put("user_name", user_name);
            requestParams.put("user_pwd", user_pwd);
            requestParams.put("industry", industry);

            username = user_name;
            pwdString = user_pwd;
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}