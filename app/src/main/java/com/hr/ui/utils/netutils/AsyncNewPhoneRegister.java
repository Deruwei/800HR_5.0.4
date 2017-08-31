package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.NewBindActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 手机注册用户
 */
public class AsyncNewPhoneRegister {
    private Context context;
    private String user_phone;
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
                            MobclickAgent.onEvent(context, "user-register");
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                            // 存储用户数据
                            MyUtils.isLogin = true;
                            MyUtils.userphone = user_phone;
//                            MyUtils.userID = jsonObject.getString("user_id");
                            MyUtils.industryId = jsonObject.getString("industry");
                            SharedPreferencesUtils sp = new SharedPreferencesUtils(context);
                            sp.setIntValue(Constants.INDUSTRY, Integer.parseInt(MyUtils.industryId));
                            sp.setStringValue(Constants.USERPHONE, MyUtils.userphone);
                            sp.setStringValue(Constants.USERID, MyUtils.userID);
                            sp.setStringValue(Constants.PASSWORD, pwdString);
//                          Intent intent = new Intent(context, MainActivity.class);
//                          context.startActivity(intent);
                            SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
                            AsyncNewLoginPhone asyncLogin = new AsyncNewLoginPhone(context, handler);
                            asyncLogin.execute(user_phone, pwdString, sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);
                            RecommendJobFragment.recommendJobFragment.initView();
                            MainActivity.instanceMain.refreshBaseInfo();
                            break;
                        case 201:
                            Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
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

    public AsyncNewPhoneRegister(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void execute(String user_phone, String user_pwd, String industry, String token) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.phoneregister");
            requestParams.put("user_phone", user_phone);
            requestParams.put("user_pwd", user_pwd);
            requestParams.put("industry", industry);
            requestParams.put("token", token);
            this.user_phone = user_phone;
            this.pwdString = user_pwd;
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}