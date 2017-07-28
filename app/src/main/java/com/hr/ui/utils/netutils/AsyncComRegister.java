package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.activity.CompanyRegisterActivity;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 作者：Colin
 * 日期：2016/4/11 09:44
 * 邮箱：bestxt@qq.com
 * <p/>
 * 企业注册
 */
public class AsyncComRegister {
    private Context context;
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
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                            CompanyRegisterActivity.companyRegisterActivity.finish();
                            break;
                        default:

//                            Toast.makeText(context, error_code + "", Toast.LENGTH_SHORT).show();
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

    public AsyncComRegister(Context context, Handler handler) {
        this.context = context;
    }

    public void execute(String emailaddress, String user_name, String user_pwd,
                        String industry, String password1String, String companyNameString,
                        String comPhoneString, String comContactsString) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "enterprise.register");
            requestParams.put("site_code", industry);
            requestParams.put("user_name", user_name);
            requestParams.put("password", user_pwd);
            requestParams.put("passwordre", password1String);
            requestParams.put("enterprise_name", companyNameString);
            requestParams.put("linkman", comContactsString);
            requestParams.put("phone", comPhoneString);
            requestParams.put("email", emailaddress);
            requestParams.put("is_login", "0");
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}
