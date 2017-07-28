package com.hr.ui.utils.tools;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.UUID;

/**
 * 获取设备唯一值
 *
 */
public class PushAliasString {
    public static String getDeviceId(Context context) {
        StringBuilder deviceAlias = new StringBuilder();
        // 检测缓存设备标识
        SharedPreferencesUtils sp = new SharedPreferencesUtils(context);
        if (sp != null) {
            deviceAlias = deviceAlias.append(sp.getStringValue("alias", ""));
            if (deviceAlias.length() > 0) {
                String aliasString = deviceAlias.toString();
                aliasString.replace(":", "_").trim();
                aliasString.replace("-", "_").trim();
                MyUtils.ALIAS = aliasString;
                return aliasString;
            }

        }
        // 重新生成设备标识
        try {
            // wifi mac地址
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!isEmpty(wifiMac)) {
                deviceAlias.append("wifi");
                deviceAlias.append(wifiMac);
                MyUtils.ALIAS = deviceAlias.toString().replace(":", "_").trim();
                saveAlias(sp);
                return deviceAlias.toString().replace(":", "_").trim();
            }

            // IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!isEmpty(imei)) {
                deviceAlias.append("imei");
                deviceAlias.append(imei);
                MyUtils.ALIAS = deviceAlias.toString().replace(":", "_").trim();
                saveAlias(sp);
                return deviceAlias.toString().replace(":", "_").trim();
            }

            // 序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!isEmpty(sn)) {
                deviceAlias.append("sn");
                deviceAlias.append(sn);
                MyUtils.ALIAS = deviceAlias.toString().replace("-", "_").trim();
                saveAlias(sp);
                return deviceAlias.toString().replace("-", "_").trim();
            }

            // 如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!isEmpty(uuid)) {
                deviceAlias.append("id");
                deviceAlias.append(uuid);
                MyUtils.ALIAS = deviceAlias.toString().replace("-", "_").trim();
                saveAlias(sp);
                return deviceAlias.toString().replace("-", "_").trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceAlias.append("id").append(getUUID(context));
        }
        String aliasString = deviceAlias.toString();
        aliasString.replace(":", "_").trim();
        aliasString.replace("-", "_").trim();
        MyUtils.ALIAS = aliasString;
        return aliasString;

    }

    /**
     * 保存别名
     *
     * @param sp
     */
    private static void saveAlias(SharedPreferencesUtils sp) {
        if (sp != null) {
            sp.setStringValue("alias", MyUtils.ALIAS);
        }
    }

    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        String uuid = null;
        SharedPreferencesUtils mShare = new SharedPreferencesUtils(context);
        if (mShare != null) {
            uuid = mShare.getStringValue("uuid", "");
        }
        if (isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.setStringValue("uuid", uuid);
        }

        return uuid;
    }

    private static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }
}
