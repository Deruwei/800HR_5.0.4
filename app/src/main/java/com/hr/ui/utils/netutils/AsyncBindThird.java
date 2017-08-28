package com.hr.ui.utils.netutils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hr.ui.activity.NewBindActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.view.custom.BeautifulDialog;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 本站帐号绑定第三方帐号
 */
public class AsyncBindThird {
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
//                            // 存储用户数据
                            Log.i("===========", jsonObject.toString());
                            MyUtils.isLogin = true;
                            MyUtils.username= user_name;
                            MyUtils.industryId = industry;
                            MyUtils.userphone=phoneNum;
                            // 成功登录后记录数据
                            SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
                            sUtils.setStringValue(Constants.USERNAME, user_name);
                            sUtils.setStringValue(Constants.PASSWORD, user_pwd);
                            sUtils.setStringValue(Constants.USERPHONE,phoneNum);
                            sUtils.setStringValue("autoLoginThird_code", third_code);
                            sUtils.setStringValue("autoLoginThird_uid", third_uid);
                            sUtils.setStringValue("autoLoginIndustry", industry);
                            sUtils.setStringValue("autoLoginThired", "1");
//                            MyUtils.userphone = user_phone;
//                            MyUtils.userID = jsonObject.getString("user_id");
//                            MyUtils.industryId = jsonObject.getString("industry");
                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);
//                            NewBindActivity.newBindActivity.finish();
                            RecommendJobFragment.recommendJobFragment.initView();
                            NewLoginActivity.newLoginActivity.execute();
//                            MyUtils.isLogin = true;
//                    FindjobFragment.upDataUI();
                            break;
                        default:
//                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            confirm();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;

    };
    private BeautifulDialog.Builder builderConfirm;

    private void confirm() {
        builderConfirm = new BeautifulDialog.Builder(context);
        builderConfirm.setMessage("本站帐号已绑定过此类型第三方帐号，\n继续绑定将会覆盖原有绑定！");
        builderConfirm.setTitle("提示");
        builderConfirm.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding = "1";
                if (type.equals("1")){
                    execute();
                }else {
                    execute2();
                }
//                execute();
                dialog.dismiss();

            }
        });
        builderConfirm.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderConfirm.create().show();
    }

    public AsyncBindThird(Context context,String phoneNum,String psw, Handler handler,String third_code, String third_uid, String industry,String third_userinfo,String binding) {
        this.context = context;
        this.handler = handler;
        this.third_code=third_code;
        this.third_uid=third_uid;
        this.industry=industry;
        this.phoneNum=phoneNum;
        this.user_pwd=psw;
        this.binding=binding;
        this.type="1";
    }
    public AsyncBindThird(Context context, Handler handler,String third_code, String third_uid, String industry, String user_name, String user_pwd,String third_userinfo, String binding) {
        this.context = context;
        this.handler = handler;
        this.third_code=third_code;
        this.third_uid=third_uid;
        this.industry=industry;
        this.user_name=user_name;
        this.third_code=third_code;
        this.user_pwd=user_pwd;
        this.binding=binding;
        this.type="2";
    }
    private String phoneNum;
    private String psw;
    private String third_code;
    private String third_uid;
    private String industry;
    private String binding;
    private String user_pwd;
    private String type;
    private String user_name;
    public void execute() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.thirdbinding");
            requestParams.put("third_code", third_code);
            requestParams.put("third_uid", third_uid);
            requestParams.put("industry", industry);
            requestParams.put("third_userinfo", "");
            requestParams.put("binding", binding);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
    public void execute2() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.thirdbinding");
            requestParams.put("third_code", third_code);
            requestParams.put("third_uid", third_uid);
            requestParams.put("industry", industry);
            requestParams.put("user_name", user_name);
            requestParams.put("user_pwd", user_pwd);
            requestParams.put("third_userinfo", "");
            requestParams.put("binding", binding);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            // System.out.println("取消");
            e.printStackTrace();
        }
    }
}