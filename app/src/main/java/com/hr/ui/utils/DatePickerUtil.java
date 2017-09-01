package com.hr.ui.utils;

import android.content.Context;
import android.renderscript.Type;
import android.widget.TextView;

import com.hr.ui.view.custom.MyDatePicker;

/**
 * Created by wdr on 2017/9/1.
 */

public class DatePickerUtil {
    private static MyDatePicker myDatePicker;
    public  static void initMyDatePicker(Context context, final TextView tv) {
        //2013年9月3日 14:44
        String currentDate = tv.getText().toString().trim().replace("-","年");
        currentDate=currentDate+"月1日 00:00";
        myDatePicker = new MyDatePicker(context, new MyDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time );
            }
        },currentDate,2);
    }
    public  static void initMyDatePicker2(Context context, final TextView tv) {
        //2013年9月3日 14:44
        String currentDate = tv.getText().toString().trim().replace("-","年");
        String a=currentDate.substring(0,currentDate.lastIndexOf("年"));
        String b=currentDate.substring(currentDate.lastIndexOf("年")+1);
        currentDate=a+"月"+b;
        currentDate=currentDate+"日 00:00";
        myDatePicker = new MyDatePicker(context, new MyDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                tv.setText(time );
            }
        },currentDate,1);
    }
}
