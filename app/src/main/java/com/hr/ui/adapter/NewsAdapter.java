package com.hr.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hr.ui.R;

public class NewsAdapter extends BaseAdapter {

	private Context mContext;

	public NewsAdapter(Context c) {

		mContext = c;
	}

	public int getCount() {

		return mThumbIds.length;

	}

	public Object getItem(int position) {

		return null;

	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);

			imageView.setLayoutParams(new GridView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(10, 10, 10, 10);

		} else {

			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;

	}

	private Integer[] mThumbIds = {
            R.mipmap.qiuzhibaodian, R.mipmap.jianlizhinan,
            R.mipmap.mianshimiji22, R.mipmap.xinjinhangqing,
            R.mipmap.zhiyezhenduan, R.mipmap.zhichangbagua,
            R.mipmap.renjiguanxi, R.mipmap.chuanyezhinan,
            R.mipmap.laodongfahuan
	};
}
