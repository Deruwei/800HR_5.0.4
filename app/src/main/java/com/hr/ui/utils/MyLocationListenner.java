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

        } else if (location.getLocType() == BDLocation.TypeServerError) {
            city=location.getAddress().city;
            MyUtils.currentCityZh = city;
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            city=location.getAddress().city;
            MyUtils.currentCityZh = city;
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            city=location.getAddress().city;
            MyUtils.currentCityZh = city;
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }
        Log.i("定位的地址7",city);
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        Log.i("BaiduLocationApiDem", sb.toString());
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
