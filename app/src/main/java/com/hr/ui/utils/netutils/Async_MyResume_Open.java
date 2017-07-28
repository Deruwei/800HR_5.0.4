package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 推送
 */
public class Async_MyResume_Open {
    private Context context;
    private HashMap<String, String> resultHashMap;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    //System.out.println("得到设置的开关：" + jsonObject);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            Toast.makeText(context,"设置成功",Toast.LENGTH_SHORT).show();
                            MyResumeFragment.myResumeFragment.refreshNow();
                            MobclickAgent.onEvent(context, "cv-open");
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

    public Async_MyResume_Open(Context context) {
        this.context = context;
    }

    public void execute(String openstate, String resume_id) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.setopenstate");
            requestParams.put("openstate", openstate);
            requestParams.put("resume_id", resume_id);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}