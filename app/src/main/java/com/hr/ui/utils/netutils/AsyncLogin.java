package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * 发起登录请求
 *
 * @author 800hr:xuebaohua
 */
public class AsyncLogin {
    private Context context;
    private String username;
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
//							JPushInterface.clearAllNotifications(context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            userID = jsonObject.getString("user_id");
//						Toast.makeText(context,context.getString(R.string.login_success)+"UserID:"+userID,
//								Toast.LENGTH_SHORT).show();
                            // 改变全局状态
                            MyUtils.username = username;
                            MyUtils.userID = userID;
                            MyUtils.industryId = industry;
                            Message message = handler.obtainMessage();
                            message.what = 0;
//						message.arg1 = LoginTag;
                            handler.sendMessage(message);
                            // 成功登录后记录数据
                            SharedPreferencesUtils sUtils = new SharedPreferencesUtils(
                                    context);
                            sUtils.setStringValue(Constants.USERNAME, username);
                            sUtils.setStringValue(Constants.PASSWORD, pwd);
                            industry = jsonObject.getInt("industry") + "";
                            sUtils.setIntValue(Constants.INDUSTRY, jsonObject.getInt("industry"));
                            sUtils.setStringValue(Constants.USERID, MyUtils.userID);

//						// 刷新设置中注销按钮状态
//						if (SettingActivityGroup.group != null) {
//							SetingActivity setingActivity = (SetingActivity) SettingActivityGroup.group
//									.getLocalActivityManager().getActivity(
//											"SetingActivity");
//							if (setingActivity != null) {
//								setingActivity.refreshLogoutBg();
//							}
//						}
                            MyUtils.isLogin = true;
                            RecommendJobFragment.recommendJobFragment.initView();
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Message message1 = handler.obtainMessage();
                            message1.what = -1;
//						message1.arg1 = LoginTag;
                            handler.sendMessage(message1);
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == -1) {
                Toast.makeText(context,
                        context.getString(R.string.login_false),
                        Toast.LENGTH_SHORT).show();
                MyUtils.isLogin = false;
            }
        }

        ;

    };

    public AsyncLogin(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
//		this.LoginTag = LoginTag;
    }

    public void execute(String username, String pwd, String industry) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.login");
            requestParams.put("user_name", username);
            requestParams.put("user_pwd", pwd);
            requestParams.put("industry", industry);
            this.username = username;
            this.pwd = pwd;
            this.industry = industry;
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}