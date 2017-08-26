package com.hr.ui.utils.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.NewBindActivity;
import com.hr.ui.activity.NewLoginActivity;
import com.hr.ui.activity.OpenBindActivity;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.BeautifulDialog;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 判断用户的第三方平台是否已经使用
 *
 * @author 123
 */
public class IsBind {
    private String platformString;
    private int loginFromTag;
    private String uid;
    private String name;
    private String birthday;
    private String nickname;
    private String tinyurl;
    private String gender;
    private Context context;
    private String industry;// 登录时选择行业
    private String suid;// 要保留绑定的用户ID
    private boolean isFromPush = false;
    private SharedPreferencesUtils sUtils;
    private HashMap<String, String> requestParams;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
//            Toast.makeText(context, "返回的" + msg.what, Toast.LENGTH_SHORT).show();
            if (msg.what == 0) {
//                Toast.makeText(context, "返回了", Toast.LENGTH_SHORT).show();
                String json = (String) msg.obj;
//                System.out.println("第三方登录结果：" + json);
//                Toast.makeText(context, "返回的" + json.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:
                            // 改变全局状态
                            MyUtils.isLogin = true;
                            MyUtils.isLogouted = false;
                            MyUtils.isThiredLogin = true;
                            MyUtils.userID = jsonObject.getString("user_id");
                            MyUtils.industryId = jsonObject.getString("industry");
                            MyUtils.canResumeReflesh=true;
                            if (nickname != null && nickname.length() > 0) {
                                MyUtils.username = nickname;
                            } else {
                                MyUtils.username = name;
                            }
                            sUtils.setStringValue("autoLoginThird_code", platformString);
                            sUtils.setStringValue("autoLoginThird_uid", uid);
                            sUtils.setStringValue("autoLoginIndustry", industry);
                            sUtils.setStringValue("autoLoginThired", "1");//1为自动登录
                            Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show();
                            NewLoginActivity.newLoginActivity.finish();
                            break;
                        case 305:// 第一次绑定账号
                            Intent intent = new Intent(context, NewBindActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("uid", uid);
                            intent.putExtra("platform", platformString);
                            intent.putExtra("name", name);
                            intent.putExtra("birthday", birthday);
                            intent.putExtra("nickname", nickname);
                            intent.putExtra("tinyurl", tinyurl);
                            intent.putExtra("gender", gender);
                            intent.putExtra("isFromPush", isFromPush);
                            context.startActivity(intent);
                            break;
                        default:
//                           Toast.makeText(context, "此第三方账号曾绑定过多行业", Toast.LENGTH_LONG).show();
                            Toast.makeText(context, context.getResources().getString(R.string.login_false), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                execute();
            }
        }

        ;

    };

    /**
     * @param platformString
     * @param loginFromTag
     * @param uid
     * @param name
     * @param birthday
     * @param nickname
     * @param tinyurl
     * @param gender
     * @param context
     * @param industry
     * @param suid
     * @param isFromPush
     */
    public IsBind(String platformString, int loginFromTag, String uid,
                  String name, String birthday, String nickname, String tinyurl,
                  String gender, Context context, String industry, String suid,
                  boolean isFromPush) {
        super();
        this.isFromPush = isFromPush;
        this.platformString = platformString;
        this.loginFromTag = loginFromTag;
        this.uid = uid;
        this.name = name;
        this.birthday = birthday;
        this.nickname = nickname;
        this.tinyurl = tinyurl;
        this.gender = gender;
        this.context = context;
        this.industry = industry;
        this.suid = suid;
        sUtils = new SharedPreferencesUtils(context);
    }

    public IsBind(String platformString, String uid,
                  Context context, String industry, String suid
    ) {
        super();
        this.platformString = platformString;
        this.uid = uid;
        this.industry = industry;
        this.suid = suid;
        this.context = context;
        sUtils = new SharedPreferencesUtils(context);
    }
    public void execute() {
        requestParams = new HashMap<String, String>();
        requestParams.put("method", "user.thirdlogin");
        requestParams.put("third_code", platformString);
        requestParams.put("third_uid", uid);
        requestParams.put("industry", industry);
        requestParams.put("suid", suid);
        try {
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
//            Toast.makeText(context, "请求了", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}