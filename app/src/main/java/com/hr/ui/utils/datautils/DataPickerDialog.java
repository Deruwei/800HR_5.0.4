package com.hr.ui.utils.datautils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;

import java.util.Calendar;

import static java.util.Calendar.*;

/**
 * 日期选择类
 */
public class DataPickerDialog {
    /**
     * @param context
     * @param textView
     * @param cells    ：年月日为3部分，cells代表需要返回部分的数量
     */
    public static void showDialog(final Context context,
                                  final TextView textView, final int cells) {
        Dialog dialog = null;
        final Calendar c = getInstance();
        dialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month,
                                          int dayOfMonth) {
                        //                         未来时间判断
                        if (year > c.get(YEAR)) {

                            Toast.makeText(context,
                                    context.getString(R.string.date1),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (year == c.get(YEAR)
                                && month > c.get(MONDAY)) {
                            Toast.makeText(context,
                                    context.getString(R.string.date1),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (year == c.get(YEAR) && month == c.get(MONDAY) && dayOfMonth > c.get(DAY_OF_MONTH)) {
                            Toast.makeText(context, context.getString(R.string.date1), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (cells == 2) {// yyyy-MM
                            textView.setText(year + "-" + (month + 1));
                        } else if (cells == 3) {
                            textView.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }
                    }
                }, c.get(YEAR), // 传入年份
                c.get(MONTH), // 传入月份
                c.get(DAY_OF_MONTH) // 传入天数
        );
        dialog.show();
    }
}