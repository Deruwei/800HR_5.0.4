package com.hr.ui.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wdr on 2017/9/25.
 */

public class MyImageVIew extends ImageView {
    public MyImageVIew(Context context) {
        super(context);
    }

    public MyImageVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        Drawable d = getDrawable();

        if(d!=null){
            // ceil not round - avoid thin vertical gaps along the left/right edges
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
