package com.hr.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hr.ui.activity.CompanyEmailActivity;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.utils.netutils.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private String industry, job_id, ente_id, alert_type;
    private String id, uid;// 人事经理来信id

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
                + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收Registration Id : " + regId);
            // send the Registration Id to your server...
        } else if (JPushInterface.ACTION_RESTOREPUSH.equals(intent.getAction())) {
            //ACTION_UNREGISTER
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "接收UnRegistration Id : " + regId);
            // send the UnRegistration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 打开自定义的Activity
            NetUtils.checkNet(context);// 初始化MyUtils.network_type
            String extraData = intent.getExtras().getString(
                    JPushInterface.EXTRA_EXTRA);
            //System.out.println("收到推送消息:" + extraData);
            json(extraData);
            Intent aIntent = new Intent();
            if (alert_type.equals("1")) {
                aIntent.setClass(context, PostParticularsActivity.class);// 职位详细页
                aIntent.putExtra("industry", industry);
                aIntent.putExtra("isFromPush", true);
                aIntent.putExtra("jobIdFromPush", job_id);
                aIntent.putExtra("ente_id", ente_id);

            } else if (alert_type.equals("2")) {
                aIntent.putExtra("id", id);
                aIntent.putExtra("uid", uid);
                aIntent.putExtra("isFromPush", true);
                aIntent.setClass(context, CompanyEmailActivity.class);// 来信详细页
            }
            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(aIntent);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        /*
         * if (MainActivity.isForeground) { String message =
		 * bundle.getString(JPushInterface.EXTRA_MESSAGE); String extras =
		 * bundle.getString(JPushInterface.EXTRA_EXTRA); Intent msgIntent = new
		 * Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
		 * msgIntent.putExtra(MainActivity.KEY_MESSAGE, message); if
		 * (!ExampleUtil.isEmpty(extras)) { try { JSONObject extraJson = new
		 * JSONObject(extras); if (null != extraJson && extraJson.length() > 0)
		 * { msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras); } } catch
		 * (JSONException e) {
		 * 
		 * }
		 * 
		 * } context.sendBroadcast(msgIntent); }
		 */
    }

    private String json(String str) {
        try {
            JSONObject js = new JSONObject(str);
            if (js != null && js.has("alert_type")) {
                alert_type = js.getString("alert_type");
            }
            if (js.has("job_id")) {
                job_id = js.getString("job_id");
            }
            if (js.has("industry")) {
                industry = js.getString("industry");
            }
            if (js.has("ente_id")) {
                ente_id = js.getString("ente_id");
            }
            if (js.has("id")) {
                id = js.getString("id");
            }
            if (js.has("uid")) {
                uid = js.getString("uid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;

    }
}
