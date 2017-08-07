package com.hr.ui.view.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hr.ui.HrApplication;
import com.hr.ui.R;

/**
 * @author wmm    email：930562017@qq.com
 * @Statement 验证码倒计时控件
 */
public class CodeButton extends Button implements OnClickListener {
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "s后再次获取";
    private String textbefore = "获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;
    Map<String, Long> map = new HashMap<String, Long>();

    public CodeButton(Context context) {
        super(context);
        setOnClickListener(this);

    }

    public CodeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            CodeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                CodeButton.this.setEnabled(true);
                CodeButton.this.setBackgroundResource(R.drawable.linear_yuanhu_button);
                CodeButton.this.setText(textbefore);
                clearTimer();
            }
        }

        ;
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                Log.e("wmm", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof CodeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    public void onStart() {
        initTimer();
        CodeButton.this.setBackgroundResource(R.drawable.linear_yuanhu_bg_hui);
        this.setText(time / 1000 + textafter);
        this.setEnabled(false);
        t.schedule(tt, 0, 1000);
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        // t.scheduleAtFixedRate(task, delay, period);
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (HrApplication.map == null)
            HrApplication.map = new HashMap<String, Long>();
        HrApplication.map.put(TIME, time);
        HrApplication.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
        Log.e("wmm", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        Log.e("wmm", HrApplication.map + "");
        if (HrApplication.map == null)
            return;
        if (HrApplication.map.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - HrApplication.map.get(CTIME)
                - HrApplication.map.get(TIME);
        HrApplication.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
            this.setBackgroundResource(R.drawable.linear_yuanhu_bg_hui);
        }
    }

    /**
     * 设置计时时候显示的文本
     */
    public CodeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /**
     * 设置点击之前的文本
     */
    public CodeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght 时间 默认毫秒
     * @return
     */
    public CodeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }
    /*

*
*/
}