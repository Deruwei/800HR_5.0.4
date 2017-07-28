package com.hr.ui.view.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class ZCPopupmenuBar extends PopupWindow {
	private Context mContext;
	// 弹窗子类项选中时的监听
	private OnMenuItemClickedListener onMenuItemClickedListener;
	// 定义列表对象
	private ListView mListView;
	// 定义弹窗子类项列表
	private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

	// 功能描述：弹窗子类项按钮监听事件
	public static interface OnMenuItemClickedListener {
		public void onItemClicked(ActionItem item, int position);
	}

	// 设置监听事件
	public void setOnMenuItemClickedListener(
			OnMenuItemClickedListener onMenuItemClickedListener) {
		this.onMenuItemClickedListener = onMenuItemClickedListener;
	}

	public ZCPopupmenuBar(Context context, int layoutResId, int listViewResId,
			final int textColor, int width, int height) {
		this.mContext = context;
		// 设置可以获得焦点，默认为false
		setFocusable(true);
		// 设置弹窗内可点击，默认为true
		setTouchable(true);
		// 设置弹窗外可点击，默认为false
		setOutsideTouchable(true);
		// 必须增加背景，否则无法让菜单消失
		setBackgroundDrawable(new BitmapDrawable());

		// 设置弹窗的宽度和高度
		setWidth(width);
		setHeight(height);

		// 设置弹窗的布局界面
		setContentView(LayoutInflater.from(mContext).inflate(layoutResId, null));
		// 初始化菜单上的ListView
		mListView = (ListView) getContentView().findViewById(listViewResId);
		// 设置菜单列表项的适配器
		mListView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView textView = new TextView(mContext);
				textView.setTextSize(14);
				textView.setTextColor(textColor);
				// 设置文本居中
				textView.setGravity(Gravity.CENTER);
				// 设置文本域的范围
				textView.setPadding(0, 10, 0, 10);
				// 设置文本为单行
				textView.setSingleLine(true);

				ActionItem item = mActionItems.get(position);
				// 设置文本文字
				textView.setText(item.mTitle);
				// 设置文字与图标的间隔
				textView.setCompoundDrawablePadding(10);
				// 设置在文字的左边放一个图标
				textView.setCompoundDrawablesWithIntrinsicBounds(
						item.mDrawable, null, null, null);
				return textView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return mActionItems.get(position);
			}

			@Override
			public int getCount() {
				return mActionItems.size();
			}
		});

		// 设置监听器
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// 点击子类项后，弹窗消失
				dismiss();
				if (onMenuItemClickedListener != null)
					onMenuItemClickedListener.onItemClicked(
							mActionItems.get(position), position);
			}
		});
	}

	// 显示弹窗列表界面
	public void show(View view) {
		// 显示弹窗的位置
		if (isShowing()) {
			dismiss();
		} else {
			// 在哪个view的下方显示popupWindow菜单
			showAsDropDown(view, 0, 0);
		}
	}

	// 添加子类项
	public void addAction(ActionItem action) {
		if (action != null) {
			mActionItems.add(action);
		}
	}

	// item中每个条目
	public static class ActionItem {
		// 定义图片对象
		public Drawable mDrawable;
		// 定义文本对象
		public CharSequence mTitle;

		public ActionItem(Context context, int titleId, int drawableId) {
			this.mTitle = context.getResources().getText(titleId);
			this.mDrawable = context.getResources().getDrawable(drawableId);
		}

		public ActionItem(Context context, CharSequence title, int drawableId) {
			this.mTitle = title;
			this.mDrawable = context.getResources().getDrawable(drawableId);
		}
	}

}
