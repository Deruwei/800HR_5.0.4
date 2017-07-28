package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by tx on 2017/5/18.
 */

public class AsyncSetResumeName {
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
                    MyResumeFragment.myResumeFragment.closeStResumeNamePopWinow();
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:
                            Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT);
                            break;
                        default:
                            Message message1 = handler.obtainMessage();
                            message1.what = -1;
                            handler.sendMessage(message1);
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == -1) {
                Toast.makeText(context,
                        "修改失败",
                        Toast.LENGTH_SHORT).show();
                MyUtils.isLogin = false;
            }
        }

        ;

    };

    public AsyncSetResumeName(Context context) {
        this.context = context;
    }

    public void execute(String resume_id, String title) {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.settitle");
            requestParams.put("resume_id", resume_id);
            requestParams.put("title", title);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
