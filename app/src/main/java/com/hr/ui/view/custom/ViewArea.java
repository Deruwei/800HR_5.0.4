package com.hr.ui.view.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.FrameLayout;

/**
 * ViewArea是一个布局， 所以这里当然要继承一个布局了,LinearLayout也可以
 */
public class ViewArea extends FrameLayout
{
    private int imgDisplayW;
    private int imgDisplayH;
    private int imgW;
    private int imgH;
    private TouchView touchView;


    // private DisplayMetrics dm;

    /**
     * 构造函数
     * @param context
     * @param resId 图片资源id,当然也可以用别的方式获取图片
     */
    public ViewArea(Context context, Bitmap resId)
    {
        super(context);

        // 这里的宽高要和xml中的LinearLayout大小一致，如果要指定大小。
        // xml中LinearLayout的宽高一定要用px像素单位，因为这里的宽高是像素，用dp会有误差！
        imgDisplayW = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        imgDisplayH = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();

        // 这句就是我们的自定义ImageView
        touchView = new TouchView(context, imgDisplayW, imgDisplayH);

        // 给我们的自定义imageView设置要显示的图片
        // touchView.setImageResource(resId);
        touchView.setImageBitmap(resId);

        // Bitmap img =
        // BitmapFactory.decodeResource(context.getResources(),
        // resId);
        imgW = resId.getWidth();
        imgH = resId.getHeight();

        // 图片第一次加载进来，判断图片大小从而确定第一次图片的显示方式。
        int layout_w = imgW > imgDisplayW ? imgDisplayW : imgDisplayW;
        int layout_h = imgH > imgDisplayH ? imgDisplayH : imgDisplayH;

        // 下面的代码是判断图片初始显示样式的，当然可以根据你的想法随意显示，
        // 我这里是将宽大于高的图片按照宽缩小的比例把高压缩，
        // 前提必须是宽度超出了屏幕大小，相反，如果高大于宽，我将图片按照高缩小的比例把宽压缩，
        // 前提必须是高度超出了屏幕大小
        if (imgW >= imgH)
        {
            if (layout_w == imgDisplayW)
            {
                layout_h = (int) (imgH * ((float) imgDisplayW / imgW));
            }
        }
        else
        {
            if (layout_h == imgDisplayH)
            {
                layout_w = (int) (imgW * ((float) imgDisplayH / imgH));
            }
        }

        // 这里需要注意的是，采用FreamLayout或者LinearLayout的好处是：
        // 如果压缩后的图片仍有一个边大于屏幕， 那么只显示在屏幕内的部分，可以通过移动后看见外部（不会裁剪掉图片），
        // 如果采用RelativeLayout布局，图片会始终完整显示在屏幕内部，不会有超出屏幕的现象；
        // 如果图片不是完全占满屏幕，那么在屏幕上没有图片的地方拖动，图片也会移动，这样的体验不太好，
        // 建议用FreamLayout或者LinearLayout。

        // 这是自定义imageView的大小，也就是触摸范围

        LayoutParams params = new LayoutParams(
            layout_w, layout_h);
        params.gravity = Gravity.CENTER;
        touchView.setLayoutParams(params);
        this.addView(touchView);
    }

}
