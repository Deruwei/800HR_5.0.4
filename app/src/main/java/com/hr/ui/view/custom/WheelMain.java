package com.hr.ui.view.custom;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.hr.ui.R;
import com.hr.ui.adapter.ArrayWheelAdapter;
import com.hr.ui.adapter.NumericWheelAdapter;

import java.util.Calendar;


public class WheelMain {
	String countries[] = new String[] { "1:00", "2:00", "3:00", "4:00", "5:00",
			"6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:30",
			"14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00","21:00","22:00","23:00" };
	String countries2[] = new String[] { "1:00", "2:00", "3:00", "4:00",
			"5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
			"13:00","14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00","21:00","22:00","23:00" };
	private PopupWindow popupWindow;
	private View view;

	private WheelView wv_hours;
	private WheelView wv_mins;

	public WheelMain() {

	}

	public PopupWindow getPopupWinddow() {
		return popupWindow;
	}

	public void setPopupWinddow(PopupWindow popupWinddow) {
		this.popupWindow = popupWinddow;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelMain(PopupWindow popupWinddow, View view) {
		super();
		this.popupWindow = popupWinddow;
		this.view = view;
	}

	public void showDateTimePicker(final View v) {
		Calendar calendar = Calendar.getInstance();

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter<String>(countries2));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		wv_mins = (WheelView) view.findViewById(R.id.min);
		wv_mins.setAdapter(new ArrayWheelAdapter<String>(countries));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		int textSize = 0;
        textSize = 30;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

		popupWindow.setContentView(view);
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public int[] getTime() {
		StringBuffer sb = new StringBuffer();
		//String from_time = null, to_time=null;
		int from_time = 0, to_time=0;
        from_time=wv_hours.getCurrentItem()+1;
		to_time=wv_mins.getCurrentItem()+1;
		int[] num={from_time,to_time};
		
		// int a=wv_hours.getCurrentItem();

		// sb.append(wv_hours.getCurrentItem()).append("~")
		//
		// .append(wv_mins.getCurrentItem()== 0?"00":"1：30");
		// //System.out.println(wv_hours+"!!!!!!!!!!!!!!!!");
		// //System.out.println(wv_mins+"?????????????????????");
		// //System.out.println(wv_hours.getCurrentItem()+"wwwwwooooooooooooooo");
		// //System.out.println(wv_mins.getCurrentItem()+"Qqqqqqqqqqqqqqq");
		//
	//	return sb.toString();
		return num;
	}
}
