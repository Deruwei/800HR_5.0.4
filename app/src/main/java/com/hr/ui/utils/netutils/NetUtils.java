package com.hr.ui.utils.netutils;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.hr.ui.utils.MyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络帮助类，有post和get请求两种方式。
 *
 * @author 123
 */
public class NetUtils extends Application {
    /**
     * get 请求
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static InputStream HttpGet(String path) throws IOException {
        // FakeX509TrustManager.allowAllSSL();
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(3000);
        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conn.getInputStream();
            return inputStream;
        }
        return null;
    }

    /**
     * inputstream to byte[]
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] readAsByteArray(InputStream inputStream)
            throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            return outSteam.toByteArray();
        } finally {
            outSteam.close();
            inputStream.close();
        }
    }

    /**
     * @param inputStream
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String readAsString(InputStream inputStream, String encode)
            throws UnsupportedEncodingException, IOException {
        return new String(readAsByteArray(inputStream), encode);
    }

    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        // System.out.println("检测网络状态");
        // // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）

        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        if (networkInfo != null && networkInfo.isAvailable()) {
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                MyUtils.network_type = "mobile";
                if (networkInfo.getExtraInfo().toLowerCase()!=null){
                    if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                        if (networkInfo.isConnected()) {
                            return true;
                        }
                    } else {
                        if (networkInfo.isConnected()) {
                            return true;
                        }
                    }
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                MyUtils.network_type = "wifi";
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Service.WIFI_SERVICE);

                if (networkInfo.isConnected()) {
                    return true;
                }
            } else {
                MyUtils.network_type = "Other";
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Service.WIFI_SERVICE);

                if (networkInfo.isConnected()) {
                    return true;
                }
            }
        }

        // WIFi网卡有五个状态，实际就是一些整形常量：
        //
        // WIFI_STATE_DISABLED:WIFI不能使用，其值是：1.
        //
        // WIFI_STATE_DISABLING:WIFI正在关闭中，由于WIFI关闭是需要一个过程，其值是：0
        //
        // WIFI_STATE_ENABLED:WIFI可以使用，其值是：3.
        //
        // WIFI_STATE_ENABLING:WIFI正在开启中， 其值是：2.
        //
        // WIFI_STATE_UNKNOWN:WIFI未知网卡状态，当手机或程序出现错误引起WIFi不可用，其值是：4.
        return false;
    }
}
