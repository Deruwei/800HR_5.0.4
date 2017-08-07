package com.hr.ui.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by wdr on 2017/8/1.
 */

public class PermissionCheck {
    private final Context context;
    public PermissionCheck(Context context){
        this.context=context.getApplicationContext();
    }
    // 判断权限集合
    public boolean lackPermission(String[] permisions){
        for(String permission:permisions){
            if(lacksPermission(permission)){
                return true;
            }

        }
        return false;
    }
    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
