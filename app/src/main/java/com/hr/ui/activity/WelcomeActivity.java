package com.hr.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import com.hr.ui.utils.RefleshDialogUtils;
import com.hr.ui.utils.netutils.NetService;
import com.networkbench.agent.impl.NBSAppAgent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.GetBaiduLocation;
import com.hr.ui.utils.MyLocationListenner;
import com.hr.ui.utils.PermissionCheck;
import com.hr.ui.utils.VersionUpdate;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncArrayUpdate;
import com.hr.ui.utils.tools.PushAliasString;
import com.networkbench.agent.impl.NBSAppAgent;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.security.acl.Permission;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.R.attr.start;

/**
 * 欢迎页面
 */
public class WelcomeActivity extends BaseActivity {
    private SharedPreferencesUtils sUtils;
    private static  final int REQUESTCODE=101;
    public static WelcomeActivity welcomeActivity;
    private RefleshDialogUtils dialogUtils;
    static final String[] permissionStrings=new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1000) {
                // 会话连接，更新职位，省份数组
                new AsyncArrayUpdate(WelcomeActivity.this, handler).execute();
            } else if (msg.what == 1001) {// 无可用网络，启动程序
                if (sUtils.getBooleanValue(Constants.IS_FIRST, true)) {
                    sUtils.setStringValue(Constants.IS_GUIDE, "1");
                    Intent intent = new Intent(WelcomeActivity.this, NewbieGuideActivity.class);// 新手指导
                    startActivity(intent);
                    goout();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);// 行业找工作
                    startActivity(intent);
                    goout();
                }
            } else if (msg.what == 1002) {// 有网络，但服务器连接失败或数组更新完毕（不论是否更新成功），启动程序
                // 行业选择
                if (sUtils.getBooleanValue(Constants.IS_FIRST, true)) {
                    Intent intent = new Intent(WelcomeActivity.this, NewbieGuideActivity.class);// 新手指导
                    startActivity(intent);
                    goout();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    goout();
                }
            } else if (msg.what == 1003) {// exit sys
                Toast.makeText(WelcomeActivity.this,"无网络状态,请连接网络！",Toast.LENGTH_SHORT).show();
                goout();
            }
            dialogUtils.dismissDialog();
        }


    };

    public void goout() {
        finish();
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
            tintManager.setStatusBarTintColor(Color.parseColor("#ffffff"));// 通知栏所需颜色
        }
        setContentView(R.layout.activity_welcome);
        dialogUtils=new RefleshDialogUtils(this);
        NBSAppAgent.setLicenseKey("8a97e06a76944ee3886dafe60f20a809").withLocationServiceEnabled(true).start(
                this.getApplicationContext());
        welcomeActivity = WelcomeActivity.this;
//        NBSAppAgent.setLicenseKey("018b83f2a7c7413abc8d6225c7ea3573").withLocationServiceEnabled(true).start(this);
        sUtils = new SharedPreferencesUtils(WelcomeActivity.this);
        sUtils.setIntValue(Constants.OPPEN_NUM, sUtils.getIntValue(Constants.OPPEN_NUM, 0) + 1);
        initData();

        /*goToPermissionActivity();
        if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }else{
          baiduLocation.loadLocation();
        }*/
    }

   /* private void goToPermissionActivity(){
        if(permissionCheck.lackPermission(permissionStrings)){
            startPermissionsActivity();
        }
    }
    private void startPermissionsActivity() {
        PermissionActivity.startActivityForResult(this, REQUESTCODE,permissionStrings);
    }*/
    /**
     * 初始化行业选择
     */
    private void initData() {
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(getApplicationContext());
        sUtils.setBooleanValue(Constants.IS_CHOOSEINDUSTRY, true);
        JPushInterface.setDebugMode(true);// JPush-debug模式
        JPushInterface.init(this);
        JPushInterface.setAlias(this, PushAliasString.getDeviceId(this),
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0, String arg1, Set<String> arg2) {
                        System.out.println("别名设置结果" + arg0 + " " + arg1);
                    }
                });
        System.out.println("设备别名：" + PushAliasString.getDeviceId(this));
        dialogUtils.showDialog();
        new VersionUpdate(WelcomeActivity.this, handler);
    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUESTCODE && resultCode == PermissionActivity.PERMISSIONS_DENIED) {
            Toast.makeText(this,"请手动获取手机定位权限",Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogUtils.dismissDialog();
    }
}
