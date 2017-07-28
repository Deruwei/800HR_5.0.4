package com.hr.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.NetStateUtils;

/**
 * 作者：Colin
 * 日期：2016/6/29 10:28
 * 邮箱：bestxt@qq.com
 */


public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetStateUtils.isNetworkConnected(context);
//            System.out.println("网络状态：" + isConnected);
//            System.out.println("wifi状态：" + NetUtils.isWifiConnected(context));
//            System.out.println("移动网络状态：" + NetUtils.isMobileConnected(context));
//            System.out.println("网络连接类型：" + NetUtils.getConnectedType(context));
            if (isConnected) {
//                Toast.makeText(context, "已经连接网络", Toast.LENGTH_LONG).show();
                MyUtils.ableInternet = true;
            } else {
//                Toast.makeText(context, "已经断开网络", Toast.LENGTH_LONG).show();
                MyUtils.ableInternet = false;
            }
        }
    }
}