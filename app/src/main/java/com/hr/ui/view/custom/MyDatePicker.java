package com.hr.ui.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.DatePickerUtil;
import com.hr.ui.utils.tools.AndroidUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.YEAR;

/**
 * Created by wdr on 2017/9/1.
 */

public class MyDatePicker implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String time);
    }

    private DatePicker datePicker;
    private TextView tv_ok;
    private TextView tv_cancle;

    private ResultHandler handler;
    private String dateTime;
    private Context context;
    private String initDateTime;
    private int y,m,d;
    private Dialog datePickerDialog;
    private int type;

    public MyDatePicker(Context context, ResultHandler resultHandler, String initDateTime,int type) {
        this.context = context;
        this.handler = resultHandler;
        this.initDateTime = initDateTime;
        this.type=type;
        initDialog();
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.mytime_dialog);
//            datePickerDialog = new Dialog(context);
            datePickerDialog.setCancelable(false);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.dialog_date);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
        initView();
    }

    private void initView() {
        datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datepicker);
        tv_ok = (TextView) datePickerDialog.findViewById(R.id.tv_ok);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });
        datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        //不显示天
        if(type==2){
            if (datePicker != null) {
                int SDKVersion = android.os.Build.VERSION.SDK_INT;
                if (SDKVersion < 11) {
                    ((ViewGroup) datePicker.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                } else if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                if (y> calendar.get(YEAR)) {

                    Toast.makeText(context,
                            context.getString(R.string.date1),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (y == calendar.get(YEAR)&& m> calendar.get(Calendar.MONTH)) {
                    Toast.makeText(context,
                            context.getString(R.string.date1),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if ( m==calendar.get(Calendar.MONTH)&& d > calendar.get(DAY_OF_MONTH)) {
                    Toast.makeText(context, context.getString(R.string.date1), Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(dateTime)||dateTime==null){
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(datePicker.getYear(), datePicker.getMonth(),
                            datePicker.getDayOfMonth());
                    SimpleDateFormat sdf;
                    if(type==2) {
                        sdf = new SimpleDateFormat("yyyy-M");
                    }else{
                        sdf = new SimpleDateFormat("yyyy-M-d");
                    }
                    dateTime = sdf.format(calendar1.getTime());
                }
                handler.handle( dateTime );
                datePickerDialog.dismiss();
            }
        });

        datePickerDialog.show();
        initDate(datePicker);
    }

    public void initDate(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年"
                    + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime
     *            初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期
        String time = spliteString(initDateTime, "日", "index", "back"); // 时间

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = spliteString(time, ":", "index", "front"); // 时
        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();
        int currentHour = Integer.valueOf(hourStr.trim()).intValue();
        int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay, currentHour,
                currentMinute);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr
     *            源串
     * @param pattern
     *            匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        // 获得日历实例
        y=i;
        m=i1;
        d=i2;
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        SimpleDateFormat sdf;
        if(type==2) {
            sdf = new SimpleDateFormat("yyyy-M");
        }else{
            sdf = new SimpleDateFormat("yyyy-M-d");
        }
        dateTime = sdf.format(calendar.getTime());

    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        onDateChanged(null, 0, 0, 0);
    }
}
