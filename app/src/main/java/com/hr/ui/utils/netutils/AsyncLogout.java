package com.hr.ui.utils.netutils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.HrApplication;
import com.hr.ui.R;
import com.hr.ui.activity.AppSettingActivity;
import com.hr.ui.activity.ChooseIndustriesActivity;
import com.hr.ui.activity.MainActivity;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 注销
 *
 * @author 800hr:xuebaohua
 */
public class AsyncLogout {
    private Context context;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            // 改变全局变量
                            MyUtils.isLogin = false;
                            MyUtils.isLogouted = true;
                            MyUtils.username = null;
                            MyUtils.userID = null;
                            MeFragment.isLoad = true;
                            MyUtils.isThiredLogin = false;
                            HrApplication.isPhoneState = true;
                            Toast.makeText(context,
                                    context.getString(R.string.logout_success),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(context, ChooseIndustriesActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                            context.startActivity(intent);
                            AppSettingActivity.appSettingActivity.finish();
                            MainActivity.instanceMain.finish();
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

    public AsyncLogout(Context context) {
        this.context = context;
    }

    public void execute() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user.logout");

            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            //System.out.println("取消");
            e.printStackTrace();
        }
    }

}