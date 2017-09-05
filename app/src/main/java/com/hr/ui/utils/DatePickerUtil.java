package com.hr.ui.utils;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.renderscript.Type;
import android.widget.TextView;

import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.MyDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wdr on 2017/9/1.
 */

public class DatePickerUtil {
    private static String currentDate;
    private static MyDatePicker myDatePicker;
    //显示年月日
    public  static void initMyDatePicker(Context context, final TextView tv) {
        //2013年9月3日 14:44
        if(!tv.getText().toString().trim().equals("请选择")&&!tv.getText().toString().trim().equals("")&&tv.getText().toString().trim()!=null) {
            currentDate = tv.getText().toString().trim().replace("-", "年");
            currentDate = currentDate + "月1日 00:00";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm");//格式为 2013年9月3日 14:44
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            currentDate = formatter.format(curDate);

        }
        myDatePicker = new MyDatePicker(context, new MyDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time );
            }
        },currentDate,2);
    }
    //显示年月
    public  static void initMyDatePicker2(Context context, final TextView tv) {
        //2013年9月3日 14:44
        if(!tv.getText().toString().trim().equals("请选择")&&!tv.getText().toString().trim().equals("")&&tv.getText().toString().trim()!=null) {
            currentDate = tv.getText().toString().trim().replace("-", "年");
            String a = currentDate.substring(0, currentDate.lastIndexOf("年"));
            String b = currentDate.substring(currentDate.lastIndexOf("年") + 1);
            currentDate = a + "月" + b;
            currentDate = currentDate + "日 00:00";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm");//格式为 2013年9月3日 14:44
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            currentDate = formatter.format(curDate);
        }
        myDatePicker = new MyDatePicker(context, new MyDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time );
            }
        },currentDate,1);
    }
}
