package com.hr.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

/**
 * 作者：Colin
 * 日期：2016/8/1 15:29
 * 邮箱：bestxt@qq.com
 * <p/>
 * 跳转到应用市场评价
 */
public class AndroidMarketEvaluate {
    public static void goMarket(Context context) {
        if (hasAnyMarketInstalled(context)) {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intentMark = new Intent(Intent.ACTION_VIEW, uri);
            intentMark.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentMark);
        } else {
            Toast.makeText(context, "请先安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean hasAnyMarketInstalled(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("market://details?id=android.browser"));
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return 0 != list.size();
    }
}
