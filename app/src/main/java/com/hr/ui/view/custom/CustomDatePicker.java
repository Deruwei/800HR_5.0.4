package com.hr.ui.view.custom;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.hr.ui.R;
import com.hr.ui.utils.DatePickerUtil;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by liuwan on 2016/9/28.
 */
public class CustomDatePicker {

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
    private DatePickerView year_pv;

    private List<String> year;
    private String selectText;
    private String[] date;
    private TextView tv_cancle, tv_select;

    public CustomDatePicker(Context context, ResultHandler resultHandler,String[] data) {
            canAccess = true;
            this.context = context;
            this.handler = resultHandler;
            this.date=data;
            initDialog();
            initView();
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.time_dialog);
            datePickerDialog.setCancelable(false);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.custom_date_picker);
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
        tv_cancle= (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_select= (TextView) datePickerDialog.findViewById(R.id.tv_select);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("".equals(selectText)||selectText==null){
                    selectText=date[0];
                }
                handler.handle(selectText);
                datePickerDialog.dismiss();
            }
        });
    }

 /*   private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        endYear = endCalendar.get(Calendar.YEAR);
        spanYear = startYear != endYear;
        selectedCalender.setTime(startCalendar.getTime());
    }*/

    private void initTimer() {
        initArrayList();
        for(int i = 0; i< date.length; i++){
            year.add(date[i]);
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
        year.clear();
    }

    private void loadComponent() {
        year_pv.setData(year);
        year_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
               selectText=text;
            }
        });


    }


    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        year_pv.setCanScroll(year.size() > 1);
    }

    public void show(String time) {
                    /*initParameter();*/
                    initTimer();
                    addListener();
                    setSelectedTime(time);
                   executeAnimator(year_pv);
                    datePickerDialog.show();
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
        }
    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time){
        year_pv.setSelected(time);
        executeScroll();

    }


}
