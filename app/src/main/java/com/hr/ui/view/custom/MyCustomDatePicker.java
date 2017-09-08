package com.hr.ui.view.custom;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wdr on 2017/9/6.
 */

public class MyCustomDatePicker {
    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String time);
    }

    private ResultHandler handler;
    private Context context;
    private boolean canAccess = false;

    private Dialog datePickerDialog;
    private DatePickerView year_pv, month_pv, day_pv;
    private String textValue;
    private int typeNum;
    private ArrayList<String> year, month, day;
    private int startYear=1960, startMonth=1, startDay=1,endYear, endMonth=12, endDay;
    private int  selectYear,selectMonth,selectDay;
    private int  spanYear, spanMon, spanDay;
    private TextView tv_cancle, tv_select,day_text,month_text,year_text;
    private LinearLayout day_ll,year_text_ll;

    public MyCustomDatePicker(Context context, ResultHandler resultHandler) {
            canAccess = true;
            this.context = context;
            this.handler = resultHandler;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日 HH:mm");//格式为 2013年9月3日 14:44
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String  currentDate = formatter.format(curDate);
            endYear=Integer.valueOf(currentDate.substring(0,currentDate.indexOf("年")))+20;
            initDialog();
            initView();
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.mytime_dialog);
            datePickerDialog.setCancelable(false);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.my_custom_date_picker);
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
    }

    private void initView() {
        year_pv = (DatePickerView) datePickerDialog.findViewById(R.id.year_pv);
        month_pv = (DatePickerView) datePickerDialog.findViewById(R.id.month_pv);
        day_pv = (DatePickerView) datePickerDialog.findViewById(R.id.day_pv);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) datePickerDialog.findViewById(R.id.tv_select);
        day_text= (TextView) datePickerDialog.findViewById(R.id.day_text);
        month_text= (TextView) datePickerDialog.findViewById(R.id.month_text);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });
        day_ll= (LinearLayout) datePickerDialog.findViewById(R.id.day_ll);
        year_text= (TextView) datePickerDialog.findViewById(R.id.year_text);
        year_text_ll= (LinearLayout) datePickerDialog.findViewById(R.id.year_text_ll);
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(textValue)||textValue==null){
                    if(typeNum==3) {
                        handler.handle(selectYear + "-" + selectMonth+"-"+selectDay);
                    }else{
                        handler.handle(selectYear + "-" + selectMonth);
                    }
                }else {
                    handler.handle(textValue);

                }
                datePickerDialog.dismiss();
            }
        });
    }

    private void initTimer(int type) {
        initArrayList();
        for (int i = startYear; i <= endYear; i++) {
            year.add(String.valueOf(i));
        }
        if(type==2){
            year.add("至今");
        }
        if(selectYear!=spanYear){
            endMonth=12;
        }else{
            if(type!=4) {
                endMonth = spanMon;
            }else{
                endMonth=12;
            }
        }
        for (int i = startMonth; i <= endMonth; i++) {
            month.add(String.valueOf(i));
        }
        if(selectYear==spanYear&&selectMonth==spanMon){
            endDay=spanDay;
        }else{
            endDay=getDaysByYearMonth(selectYear,selectMonth);
        }
        for (int i = startDay; i <= endDay; i++) {
                day.add(String.valueOf(i));
        }
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
    }

    private void loadComponent() {
        year_pv.setData(year);
        month_pv.setData(month);
        day_pv.setData(day);
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        day_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(text.equals("至今")){
                    textValue=text;
                    year_text.setVisibility(View.GONE);
                    month_text.setVisibility(View.GONE);
                    month_pv.setVisibility(View.GONE);
                }else {
                    textValue="";
                    year_text.setVisibility(View.VISIBLE);
                    selectYear = Integer.valueOf(text);
                    month_text.setVisibility(View.VISIBLE);
                    month_pv.setVisibility(View.VISIBLE);
                }
                monthChange();
            }
        });

        month_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
              selectMonth=Integer.valueOf(text);
                dayChange();
            }
        });

        day_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectDay=Integer.valueOf(text);
            }
        });
    }

    private void monthChange() {
        month.clear();
        if(selectYear!=spanYear){
                endMonth = 12;
        }else{
            if(typeNum!=4) {
                endMonth = spanMon;
            }else{
                endMonth=12;
            }

        }
        for (int i = startMonth; i <= endMonth; i++) {
            month.add(i+"");
        }
        month_pv.setData(month);
        if(selectMonth>endMonth) {
            month_pv.setSelected(0);
            selectMonth=Integer.valueOf(month.get(0));
        }else{
            month_pv.setSelected(selectMonth+"");
        }

        executeAnimator(month_pv);

        month_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, 100);
    }
    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    private void dayChange() {
        day.clear();
        if(selectYear==spanYear&&selectMonth==spanMon){
            endDay=spanDay;
        }else{
            endDay=getDaysByYearMonth(selectYear,selectMonth);
        }
        for (int i = startDay; i <= endDay; i++) {
            day.add(i+"");
        }
        day_pv.setData(day);
        if(selectDay>endDay) {
            day_pv.setSelected(0);
            selectDay=Integer.valueOf(day.get(0));
        }else {
            day_pv.setSelected(selectDay+"");
        }

        executeAnimator(day_pv);
    }


    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        year_pv.setCanScroll(year.size() > 1);
        month_pv.setCanScroll(month.size() > 1);
        day_pv.setCanScroll(day.size() > 1);
    }


    public void show(String time,int type) {
        if (canAccess) {
            typeNum=type;
            if (isValidDate(time, "yyyy-M-d")) {
                selectYear=Integer.valueOf(time.substring(0,time.indexOf("-")));
                selectMonth=Integer.valueOf(time.substring(time.indexOf("-")+1,time.lastIndexOf("-")));
                selectDay=Integer.valueOf(time.substring(time.lastIndexOf("-")+1));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日 HH:mm");//格式为 2013年9月3日 14:44
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String  currentDate = formatter.format(curDate);
                if(type==4) {
                    endYear = Integer.valueOf(currentDate.substring(0, currentDate.indexOf("年"))) + 6;
                }else{
                    endYear = Integer.valueOf(currentDate.substring(0, currentDate.indexOf("年")));
                }
                endMonth=Integer.valueOf(currentDate.substring(currentDate.indexOf("年")+1,currentDate.indexOf("月")));
                endDay=Integer.valueOf(currentDate.substring(currentDate.indexOf("月")+1,currentDate.indexOf("日")));
                spanDay=endDay;
                spanYear=endYear;
                spanMon=endMonth;
               // Log.i("当前选择的",selectDay+"---"+selectMonth+"---"+selectYear);
                canAccess = true;
                initTimer(type);
                addListener();
                setSelectedTime(time);
                datePickerDialog.show();
            } else {
                if(time.equals("至今")){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日 HH:mm");//格式为 2013年9月3日 14:44
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String  currentDate = formatter.format(curDate);
                    endYear=Integer.valueOf(currentDate.substring(0,currentDate.indexOf("年")));
                    endMonth=Integer.valueOf(currentDate.substring(currentDate.indexOf("年")+1,currentDate.indexOf("月")));
                    endDay=Integer.valueOf(currentDate.substring(currentDate.indexOf("月")+1,currentDate.indexOf("日")));
                    spanDay=endDay;
                    spanYear=endYear;
                    spanMon=endMonth;
                    //Log.i("当前选择的",selectDay+"---"+selectMonth+"---"+selectYear);
                    canAccess = true;
                    initTimer(type);
                    addListener();
                    setSelectedTime(time);
                    datePickerDialog.show();
                }else {
                    canAccess = false;
                }
            }
        }
    }

    /**
     * 设置日期控件是否显示时和分
     */
    public void showSpecificYearAndMonth(boolean show) {
        if (canAccess) {
            if (show) {
            } else {
                day_ll.setVisibility(View.GONE);
            }
        }
    }
    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
            this.month_pv.setIsLoop(isLoop);
            this.day_pv.setIsLoop(isLoop);
        }
    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time) {
        if (canAccess) {
            if(time.equals("至今")){
                year_pv.setSelected(time);
                year_text.setVisibility(View.GONE);
                month_pv.setVisibility(View.GONE);
                month_text.setVisibility(View.GONE);
            }else {
                year_pv.setSelected(selectYear + "");
                month_pv.setSelected(selectMonth + "");
                day_pv.setSelected(selectDay + "");
                //Log.i("当前选择的2",selectDay+"---"+selectMonth+"---"+selectYear);
                executeAnimator(month_pv);
                executeAnimator(day_pv);
                executeScroll();
            }
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
}
