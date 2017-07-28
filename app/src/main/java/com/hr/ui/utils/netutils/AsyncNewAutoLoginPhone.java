package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * 发起登录请求
 *
 * @author 800hr:xuebaohua
 */
public class AsyncNewAutoLoginPhone {
    private Context context;
    private String userphone;
    private String pwd;
    private String userID;
    private String industry;
    private Handler handler;
    //	private int LoginTag = -1;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 登录成功
                            try {// 清楚所有通知
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MobclickAgent.onEvent(context, "user-login");
                            MyUtils.isLogin = true;
                            userID = jsonObject.getString("user_id");
                            // 改变全局状态
                            MyUtils.userphone = userphone;
                            MyUtils.userID = userID;
                            MyUtils.industryId = industry;
                            // 成功登录后记录数据
                            SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
                            sUtils.setStringValue(Constants.USERPHONE, userphone);
                            sUtils.setStringValue(Constants.PASSWORD, pwd);
                            industry = jsonObject.getInt("industry") + "";
                            sUtils.setIntValue(Constants.INDUSTRY, jsonObject.getInt("industry"));
                            sUtils.setStringValue(Constants.USERID, MyUtils.userID);
                            Message message = new Message();
                            message.what = 0;
                            handler.sendMessage(message);
                            RecommendJobFragment.recommendJobFragment.initView();
                            MainActivity.instanceMain.refreshBaseInfo();
                            break;
                        case 301:
                            Toast.makeText(context, "该手机号未验证", Toast.LENGTH_SHORT).show();
                            break;
                        case 311:
                            Toast.makeText(context, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Message message2 = new Message();
                            message2.what = -1;
                            handler.sendMessage(message2);
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == -1) {
                Toast.makeText(context, context.getString(R.string.login_false), Toast.LENGTH_SHORT).show();
                MyUtils.isLogin = false;
            }
        }

        ;
    };

    public AsyncNewAutoLoginPhone(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
//		this.LoginTag = LoginTag;
    }

    public void execute(String userphone, String pwd, String industry) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.phonelogin");
            requestParams.put("user_phone", userphone);
            requestParams.put("user_pwd", pwd);
            requestParams.put("industry", industry);
            this.userphone = userphone;
            this.pwd = pwd;
            this.industry = industry;
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}