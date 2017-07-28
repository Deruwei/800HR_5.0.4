package com.hr.ui.view.pulltorefresh;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * 用于嵌套在ListView、gridView中的ViewPager
 * 
 * @version 1.0
 * @author ZZT
 * 
 */
public class NoDispatcherViewPager extends ViewPager {
	private static final String TAG = "NoDispatcherViewPager";

	public NoDispatcherViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 让父类不拦截触摸事件
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

}
