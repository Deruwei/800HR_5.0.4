package com.hr.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.utils.AndroidMarketEvaluate;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.FileUtil;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncLogout;
import com.hr.ui.view.custom.BeautifulDialog;
import com.umeng.analytics.MobclickAgent;

public class AppSettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_settingapp_mark, rl_settingapp_clear, rl_settingapp_attention, rl_settingapp_comregister, rl_settingapp_note, rl_settingapp_feedback, rl_settingapp_recommend, rl_settingapp_regard, rl_settingapp_out;
    private ImageView iv_settingapp_back, iv_settingapp_autologin1, iv_settingapp_mark;
    private TextView tv_settingapp_out;

    private Context mContext = AppSettingActivity.this;
    private SharedPreferencesUtils sUtils;
    public static AppSettingActivity appSettingActivity;
    private boolean isAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        appSettingActivity = AppSettingActivity.this;
        initView();
    }
    private void initView() {
        iv_settingapp_back = (ImageView) findViewById(R.id.iv_settingapp_back);
        iv_settingapp_autologin1 = (ImageView) findViewById(R.id.iv_settingapp_autologin1);
        rl_settingapp_note = (RelativeLayout) findViewById(R.id.rl_settingapp_note);
        rl_settingapp_feedback = (RelativeLayout) findViewById(R.id.rl_settingapp_feedback);
        rl_settingapp_recommend = (RelativeLayout) findViewById(R.id.rl_settingapp_recommend);
        rl_settingapp_regard = (RelativeLayout) findViewById(R.id.rl_settingapp_regard);
        rl_settingapp_clear = (RelativeLayout) findViewById(R.id.rl_settingapp_clear);
        tv_settingapp_out = (TextView) findViewById(R.id.tv_settingapp_out);
        rl_settingapp_out = (RelativeLayout) findViewById(R.id.rl_settingapp_out);
        rl_settingapp_comregister = (RelativeLayout) findViewById(R.id.rl_settingapp_comregister);
        rl_settingapp_attention = (RelativeLayout) findViewById(R.id.rl_settingapp_attention);
        iv_settingapp_mark = (ImageView) findViewById(R.id.iv_settingapp_mark);
        rl_settingapp_mark = (RelativeLayout) findViewById(R.id.rl_settingapp_mark);
        iv_settingapp_back.setOnClickListener(this);
        iv_settingapp_autologin1.setOnClickListener(this);
        rl_settingapp_note.setOnClickListener(this);
        rl_settingapp_feedback.setOnClickListener(this);
        rl_settingapp_recommend.setOnClickListener(this);
        rl_settingapp_regard.setOnClickListener(this);
        rl_settingapp_clear.setOnClickListener(this);
        tv_settingapp_out.setOnClickListener(this);
        rl_settingapp_comregister.setOnClickListener(this);
        rl_settingapp_attention.setOnClickListener(this);
        rl_settingapp_mark.setOnClickListener(this);
        sUtils = new SharedPreferencesUtils(mContext);
        isAutoLogin = sUtils.getBooleanValue(Constants.AUTO_LOGIN, false);
        if (isAutoLogin) {
            iv_settingapp_autologin1.setImageResource(R.mipmap.kaiguan_kai);
            iv_settingapp_autologin1.setTag(R.mipmap.kaiguan_kai);
        } else {
            iv_settingapp_autologin1.setImageResource(R.mipmap.kaiguan_guan);
            iv_settingapp_autologin1.setTag(R.mipmap.kaiguan_guan);
        }
//        if (MyUtils.isLogin && !MyUtils.isThiredLogin) {
//            findViewById(R.id.rl_settingapp_autologin1).setVisibility(View.VISIBLE);
//        }
        if (MyUtils.isLogin) {
            rl_settingapp_out.setVisibility(View.VISIBLE);
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settingapp_back:
                finish();
                break;
            case R.id.rl_settingapp_note:
                break;
            case R.id.iv_settingapp_autologin1:
                if (MyUtils.isLogin) {
                    if (iv_settingapp_autologin1.getTag().equals(R.mipmap.kaiguan_kai)) {
                        iv_settingapp_autologin1.setImageResource(R.mipmap.kaiguan_guan);
                        iv_settingapp_autologin1.setTag(R.mipmap.kaiguan_guan);
                        sUtils.setBooleanValue(Constants.AUTO_LOGIN, false);
                    } else {
                        iv_settingapp_autologin1.setImageResource(R.mipmap.kaiguan_kai);
                        iv_settingapp_autologin1.setTag(R.mipmap.kaiguan_kai);
                        sUtils.setBooleanValue(Constants.AUTO_LOGIN, true);
                    }
                } else {
                    Toast.makeText(mContext, "请登录后进行操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_settingapp_feedback:
                Intent intent = new Intent(mContext, UserFeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_settingapp_recommend:
                break;
            case R.id.rl_settingapp_regard:
                Intent intentAbout = new Intent(mContext, AboutOurActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.rl_settingapp_clear:
                clearBuffer();
                break;
            case R.id.rl_settingapp_comregister:
                Intent intentComReg = new Intent(mContext, CompanyRegisterActivity.class);
                startActivity(intentComReg);
                break;
            case R.id.tv_settingapp_out:
                logOut();
                break;
            case R.id.rl_settingapp_attention:
                Intent intentAttention = new Intent(mContext, AttentionOurActivity.class);
                startActivity(intentAttention);
                break;
            case R.id.rl_settingapp_mark:
                AndroidMarketEvaluate.goMarket(mContext);
                break;
        }
    }

    /**
     * 清理缓存
     */
    private void clearBuffer() {
        BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(mContext);
//        dialog.setTitle("温馨提示");
        dialog.setMessage("缓存能节省你的流量，是否清除？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imagePath = FileUtil.getRootDir() + "/800HR";
                FileUtil.deleteDirectory(imagePath);

                sUtils.deleteAll();
                try {
                    DAO_DBOperator db = new DAO_DBOperator(mContext);
                    db.Delete_SearchHistory(MyUtils.industryId + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(mContext, "清除缓存成功", Toast.LENGTH_SHORT).show();
                MobclickAgent.onEvent(AppSettingActivity.this, "setting-clear-cache");
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }


    /**
     * 退出登录
     */
    private void logOut() {
        if (MyUtils.isLogin) {
            try {
                BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(mContext);
                dialog.setTitle("温馨提示");
                dialog.setMessage("您确定要注销吗？");
                dialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 异步注销
                                AsyncLogout asyncLogout = new AsyncLogout(mContext);
                                asyncLogout.execute();
                                // 注销成功后，取消自动登录
                                sUtils.deleteUserInfo();
                                // 发送消息，关闭所有界面，回到行业选择
                                dialog.dismiss();
//                                Intent intent = new Intent(mContext, MainActivity.class);
//                                startActivity(intent);
                            }
                        });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "尚未登录，无需注销", Toast.LENGTH_LONG).show();
        }
    }
}
