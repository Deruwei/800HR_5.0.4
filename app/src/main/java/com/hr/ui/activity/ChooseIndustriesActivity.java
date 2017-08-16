package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.AndroidMarketEvaluate;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.view.custom.BeautifulDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 作者：Colin
 * 日期：2015/12/2 10:44
 * 功能：选择行业界面
 */
public class ChooseIndustriesActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChooseIndustriesActivity";
    private Context mContext = this;
    /**
     * 第一次点击时间
     */
    private long firstClickTime;
    private BeautifulDialog.Builder builderRef;
    /**
     * 找控件
     */
    private LinearLayout industry11;
    private LinearLayout industry29;
    private LinearLayout industry14;
    private LinearLayout industry12;
    private LinearLayout industry22;
    private LinearLayout industry13;
    private LinearLayout industry21;
    private LinearLayout industry26;
    private LinearLayout industry40;
    private LinearLayout industry20;
    private LinearLayout industry15;
    private LinearLayout industry16;
    private LinearLayout industry30;
    private LinearLayout industry19;
    private LinearLayout industry23;
    /**
     * SharedPreferences工具类
     */
    private SharedPreferencesUtils sUtils;

    private void initView() {
        industry11 = (LinearLayout) findViewById(R.id.industry_11);
        industry29 = (LinearLayout) findViewById(R.id.industry_29);
        industry14 = (LinearLayout) findViewById(R.id.industry_14);
        industry12 = (LinearLayout) findViewById(R.id.industry_12);
        industry22 = (LinearLayout) findViewById(R.id.industry_22);
        industry13 = (LinearLayout) findViewById(R.id.industry_13);
        industry21 = (LinearLayout) findViewById(R.id.industry_21);
        industry26 = (LinearLayout) findViewById(R.id.industry_26);
        industry40 = (LinearLayout) findViewById(R.id.industry_40);
        industry20 = (LinearLayout) findViewById(R.id.industry_20);
        industry15 = (LinearLayout) findViewById(R.id.industry_15);
        industry16 = (LinearLayout) findViewById(R.id.industry_16);
        industry30 = (LinearLayout) findViewById(R.id.industry_30);
        industry19 = (LinearLayout) findViewById(R.id.industry_19);
        industry23 = (LinearLayout) findViewById(R.id.industry_23);

        industry11.setOnClickListener(this);
        industry29.setOnClickListener(this);
        industry14.setOnClickListener(this);
        industry12.setOnClickListener(this);
        industry22.setOnClickListener(this);
        industry13.setOnClickListener(this);
        industry21.setOnClickListener(this);
        industry26.setOnClickListener(this);
        industry40.setOnClickListener(this);
        industry20.setOnClickListener(this);
        industry15.setOnClickListener(this);
        industry16.setOnClickListener(this);
        industry30.setOnClickListener(this);
        industry19.setOnClickListener(this);
        industry23.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }
        setContentView(R.layout.activity_choose_industries);
        initView();
        initData();
        MyUtils.clear();
        /**
         * 不再进行行业选择
         */
        sUtils.setBooleanValue(Constants.IS_CHOOSEINDUSTRY, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyUtils.canReflesh=true;
    }

    private void initData() {

        sUtils = new SharedPreferencesUtils(mContext);
    }

    /**
     * 记住所选行业
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.industry_11:
                sUtils.setIntValue(Constants.INDUSTRY, 11);
                MyUtils.industryId = "11";
                break;
            case R.id.industry_12:
                sUtils.setIntValue(Constants.INDUSTRY, 12);
                MyUtils.industryId = "12";
                break;
            case R.id.industry_13:
                sUtils.setIntValue(Constants.INDUSTRY, 13);
                MyUtils.industryId = "13";
                break;
            case R.id.industry_14:
                sUtils.setIntValue(Constants.INDUSTRY, 14);
                MyUtils.industryId = "14";
                break;
            case R.id.industry_15:
                sUtils.setIntValue(Constants.INDUSTRY, 15);
                MyUtils.industryId = "15";
                break;
            case R.id.industry_16:
                sUtils.setIntValue(Constants.INDUSTRY, 16);
                MyUtils.industryId = "16";
                break;
            case R.id.industry_19:
                sUtils.setIntValue(Constants.INDUSTRY, 19);
                MyUtils.industryId = "19";
                break;
            case R.id.industry_20:
                sUtils.setIntValue(Constants.INDUSTRY, 20);
                MyUtils.industryId = "20";
                break;
            case R.id.industry_21:
                sUtils.setIntValue(Constants.INDUSTRY, 21);
                MyUtils.industryId = "21";
                break;
            case R.id.industry_22:
                sUtils.setIntValue(Constants.INDUSTRY, 22);
                MyUtils.industryId = "22";
                break;
            case R.id.industry_23:
                sUtils.setIntValue(Constants.INDUSTRY, 23);
                MyUtils.industryId = "23";
                break;
            case R.id.industry_26:
                sUtils.setIntValue(Constants.INDUSTRY, 26);
                MyUtils.industryId = "26";
                break;
            case R.id.industry_29:
                sUtils.setIntValue(Constants.INDUSTRY, 29);
                MyUtils.industryId = "29";
                break;
            case R.id.industry_30:
                sUtils.setIntValue(Constants.INDUSTRY, 30);
                MyUtils.industryId = "30";
                break;
            case R.id.industry_40:
                sUtils.setIntValue(Constants.INDUSTRY, 40);
                MyUtils.industryId = "40";
                break;
        }
        MobclickAgent.onEvent(this, "choice-industry");
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondClick = System.currentTimeMillis();
            if (secondClick - firstClickTime > 1000) {
                firstClickTime = secondClick;
                Toast.makeText(mContext, "再次点击确认退出应用", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                if (sUtils.getIntValue(Constants.OPPEN_NUM, 0) <= 3) {
                    evaluateDialog();
                } else {
                    finish();
                    System.exit(0);//正常退出App
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 五星好评
     */
    private void evaluateDialog() {
        if (AndroidMarketEvaluate.hasAnyMarketInstalled(mContext)) {
            builderRef = new BeautifulDialog.Builder(mContext);
            builderRef.setMessage("给个五星好评吧，我们会更努力!");
            builderRef.setTitle("提示");
            builderRef.setPositiveButton("马上评论", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intentMark = new Intent(Intent.ACTION_VIEW, uri);
                    intentMark.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentMark);
                    sUtils.setIntValue(Constants.OPPEN_NUM, 4);
                    dialog.dismiss();
                }
            });
            builderRef.setNegativeButton("以后评论", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);//正常退出App
                    dialog.dismiss();
                }
            });
            builderRef.create().show();
        } else {
            finish();
            System.exit(0);//正常退出App
        }
    }
}
