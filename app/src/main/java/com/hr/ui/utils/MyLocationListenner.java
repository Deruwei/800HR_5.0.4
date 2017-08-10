package com.hr.ui.utils;

import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.bumptech.glide.util.Util;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by wdr on 2017/8/1.
 */

public class MyLocationListenner implements BDLocationListener {
    private String city;
    @Override
    public void onReceiveLocation(BDLocation location) {
        //获取定位结果
        StringBuffer sb = new StringBuffer(256);

        if (location.getLocType() == BDLocation.TypeGpsLocation){
            Log.i("定位的地址1",location.getAddress().city);
            city=location.getAddress().city;
            MyUtils.currentCityZh = city;
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            city=location.getAddress().city;
            MyUtils.currentCityZh = city;

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            city=location.getAddress().city;
            MyUtils.currentCityZh = city;
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        }else{
            city="定位失败";
            MyUtils.currentCityZh = city;
        }
        Log.i("定位的地址7",city);
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
