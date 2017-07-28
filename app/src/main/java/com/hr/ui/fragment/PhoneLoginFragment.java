package com.hr.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.CompanyRegisterActivity;
import com.hr.ui.activity.NewFindPwdActivity;
import com.hr.ui.activity.NewPhoneRegisterActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncNewLoginPhone;
import com.hr.ui.utils.tools.IsBind;
import com.mob.commons.SHARESDK;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.ShareSDKR;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneLoginFragment extends Fragment {


    @Bind(R.id.et_phonelogin_username)
    EditText etPhoneloginUsername;
    @Bind(R.id.et_phonelogin_password)
    EditText etPhoneloginPassword;
    @Bind(R.id.frame_phonelogin_phonelogin)
    FrameLayout framePhoneloginPhonelogin;
    @Bind(R.id.iv_phonelogin_auto)
    ImageView ivPhoneloginAuto;
    @Bind(R.id.tv_phonelogin_findpwd)
    TextView tvPhoneloginFindpwd;
    @Bind(R.id.relative_phonelogin_regist)
    RelativeLayout relativePhoneloginRegist;
    @Bind(R.id.iv_phonelogin_qq)
    ImageView ivPhoneloginQq;
    @Bind(R.id.iv_phonelogin_weibo)
    ImageView ivPhoneloginWeibo;
    @Bind(R.id.linear_phonelogin_bottom2)
    LinearLayout linearPhoneloginBottom2;
    @Bind(R.id.rl_phonelogin_comregister)
    RelativeLayout rlPhoneloginComregister;
    private boolean isAutoLogin;
    private View view;
    private int loginFromTag = -1;// 标记从哪个模块登录
    private boolean isFromPush = false;// 推送
    private SharedPreferencesUtils sUtils;
    private AsyncNewLoginPhone loginAsync;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
//                    MyUtils.isLogin = true;
//                    FindjobFragment.upDataUI();
//                    NewLoginActivity.newLoginActivity.execute();
                    break;
                case 1:
                    //QQ登录
//                    Toast.makeText(getActivity(), "有数据么", Toast.LENGTH_SHORT).show();
                    Bundle bundleQzone = msg.getData();
                    String nameString3 = bundleQzone.getString("nameString");
                    String uidString3 = bundleQzone.getString("uidString");
                    String birthdayString3 = bundleQzone
                            .getString("birthdayString");
                    String tinyurlString3 = bundleQzone.getString("tinyurlString");
                    String genderString3 = bundleQzone.getString("genderString");
                    IsBind isBindQzone = new IsBind("qq", loginFromTag,
                            uidString3, nameString3, birthdayString3, nameString3, tinyurlString3, genderString3,
                            getActivity(), MyUtils.industryId, "", isFromPush);
                    isBindQzone.execute();
                    break;
                case 2:
                    //微博登录0
                    Bundle bundleSina = msg.getData();
                    String nameString2 = bundleSina.getString("nameString");
                    String uidString2 = bundleSina.getString("uidString");
                    String birthdayString2 = bundleSina.getString("birthdayString");
                    String tinyurlString2 = bundleSina.getString("tinyurlString");
                    String genderString2 = bundleSina.getString("genderString");
//                    if (isFromPush) {
                    IsBind isBindSina = new IsBind("sina", loginFromTag, uidString2, nameString2, birthdayString2,
                            nameString2, tinyurlString2, genderString2, getActivity(), MyUtils.industryId, "", isFromPush);
                    isBindSina.execute();
                    break;
                case 3:
                    //微信登录
                    Bundle bundleWechat = msg.getData();
                    String nameString4 = bundleWechat.getString("nameString");
                    String uidString4 = bundleWechat.getString("uidString");
                    String birthdayString4 = bundleWechat.getString("birthdayString");
                    String tinyurlString4 = bundleWechat.getString("tinyurlString");
                    String genderString4 = bundleWechat.getString("genderString");
//                    if (isFromPush) {
                    IsBind isBindWechat = new IsBind("weixin", loginFromTag, uidString4, nameString4, birthdayString4,
                            nameString4, tinyurlString4, genderString4, getActivity(), MyUtils.industryId, "", isFromPush);
                    isBindWechat.execute();
                    break;
                default:
                    break;
            }
        }
    };

    public PhoneLoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_phone_login, container, false);
        ButterKnife.bind(this, view);

        initData();
        return view;
    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(getActivity());
        isAutoLogin = sUtils.getBooleanValue(Constants.AUTO_LOGIN, false);
        if (isAutoLogin) {
            ivPhoneloginAuto.setImageResource(R.mipmap.lv);
        } else {
            ivPhoneloginAuto.setImageResource(R.mipmap.hui);
        }
        /**
         * 尝试自动登录
         */
        if (MyUtils.isLogin && getActivity().getIntent().getIntExtra("LoingTag", -1) == 10010) {// 未登录
            // 自动登录
            boolean autologin = sUtils.getBooleanValue(Constants.AUTO_LOGIN, true);
            String usernameString = sUtils.getStringValue(Constants.USERPHONE, null);
            String pwdString = sUtils.getStringValue(Constants.PASSWORD, null);
            //是否自动登录
            isAutoLogin = sUtils.getBooleanValue(Constants.AUTO_LOGIN, false);
            //之前为自动登录还是自动登录
            if (isAutoLogin) {
                ivPhoneloginAuto.setImageResource(R.mipmap.lv);
            } else {
                ivPhoneloginAuto.setImageResource(R.mipmap.hui);
            }
            etPhoneloginUsername.setText(usernameString);
            etPhoneloginPassword.setText(pwdString);
            if (autologin && usernameString != null && pwdString != null) {
                loginAsync = new AsyncNewLoginPhone(getActivity(), handler);
                loginAsync.execute(usernameString, pwdString, MyUtils.industryId + "");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.frame_phonelogin_phonelogin, R.id.iv_phonelogin_weixin, R.id.iv_phonelogin_auto, R.id.tv_phonelogin_findpwd, R.id.relative_phonelogin_regist, R.id.iv_phonelogin_qq, R.id.iv_phonelogin_weibo, R.id.linear_phonelogin_bottom2, R.id.rl_phonelogin_comregister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_phonelogin_phonelogin:
                saveInfo();
                break;
            case R.id.iv_phonelogin_auto:
                //是否自动登录 取反
                sUtils.setBooleanValue(Constants.AUTO_LOGIN, !isAutoLogin);
                sUtils.setStringValue("autoLoginThired", "2");//设置为正常登录状态
                isAutoLogin = sUtils.getBooleanValue(Constants.AUTO_LOGIN, false);
                //获取已经修改好的 判断背景图片
                if (isAutoLogin) {
                    ivPhoneloginAuto.setImageResource(R.mipmap.lv);
                } else {
                    ivPhoneloginAuto.setImageResource(R.mipmap.hui);
                }
                break;
            case R.id.tv_phonelogin_findpwd:
                Intent intentNewFindPwd = new Intent(getActivity(), NewFindPwdActivity.class);
                startActivity(intentNewFindPwd);
                break;
            case R.id.relative_phonelogin_regist:
                Intent intentRegister = new Intent(getActivity(), NewPhoneRegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.iv_phonelogin_qq:
                loginQQ();
                break;
            case R.id.iv_phonelogin_weibo:
                loginSina();
            case R.id.iv_phonelogin_weixin:
                loginWeixin();
                break;
            case R.id.linear_phonelogin_bottom2:
                break;
            case R.id.rl_phonelogin_comregister:
                Intent intentComRegister = new Intent(getActivity(), CompanyRegisterActivity.class);
                startActivity(intentComRegister);
                break;
        }
    }

    /**
     * 微博登录
     */

    private void loginSina() {
//        ShareSDK.initSDK(getActivity());
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
//        if (sina.isAuthValid()) {
        sina.removeAccount(true);
//        }
        sina.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                System.out.println("sina-complete");
                String nameString = arg2.get("name") + "";
                String uidString = arg2.get("idstr") + "";
                String birthdayString = arg2.get("birthday") + "";
                String tinyurlString = arg2.get("avatar_hd") + "";
                String genderString = arg2.get("gender") + "";
                if ("m".equals(genderString)) {
                    genderString = "1";
                } else {
                    genderString = "2";
                }
                Bundle bundleSina = new Bundle();
                bundleSina.putString("nameString", nameString);
                bundleSina.putString("uidString", uidString);
                bundleSina.putString("birthdayString", birthdayString);
                bundleSina.putString("tinyurlString", tinyurlString);
                bundleSina.putString("genderString", genderString);
                Message message = handler.obtainMessage();
                message.setData(bundleSina);
                message.what = 2;
                handler.sendMessage(message);
            }
            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        sina.showUser(null);
    }

    /**
     * 微信登录
     */
    private void loginWeixin() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//        if (wechat.isAuthValid()) {
        wechat.removeAccount(true);
//        }
        if (wechat.isClientValid()) {
            wechat.removeAccount(true);
        } else {
            Toast.makeText(getActivity(), "您需要先安装微信客户端", Toast.LENGTH_SHORT).show();
        }
        wechat.SSOSetting(false);
        wechat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                System.out.println("wechat-complete");
                String nameString = arg2.get("name") + "";
                String nicknameString = arg2.get("nickname") + "";
                String uidString = arg2.get("unionid") + "";
                String birthdayString = arg2.get("birthday") + "";
                String tinyurlString = arg2.get("avatar_hd") + "";
                String genderString = arg2.get("gender") + "";
                Bundle bundleWechat = new Bundle();
                bundleWechat.putString("nameString", nameString);
                bundleWechat.putString("uidString", uidString);
                bundleWechat.putString("birthdayString", birthdayString);
                bundleWechat.putString("tinyurlString", tinyurlString);
                bundleWechat.putString("genderString", genderString);
                bundleWechat.putString("nickname", nicknameString);
                Message message = handler.obtainMessage();
                message.setData(bundleWechat);
                message.what = 3;
                handler.sendMessage(message);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        wechat.showUser(null);
    }

    /**
     * QQ登录
     */
    private void loginQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        if (qq.isAuthValid()) {
            qq.removeAccount(true);
        }
        // 设置网页版的登陆
        qq.SSOSetting(false);
        qq.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),"响应失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                // TODO Auto-generated method stub
                System.out.println("qq-complete");
                String nickNameString = arg2.get("nickname") + "";
                String uidString = arg0.getDb().getUserId() + "";
                String birthdayString = arg2.get("birthday") + "";
                String tinyurlString = arg2.get("figureurl_qq_2") + "";
                String genderString = arg2.get("gender") + "";

                Bundle bundleQzone = new Bundle();
                bundleQzone.putString("nameString", nickNameString);
                bundleQzone.putString("uidString", uidString);
                bundleQzone.putString("birthdayString", birthdayString);
                bundleQzone.putString("tinyurlString", tinyurlString);
                bundleQzone.putString("genderString", genderString);
                Message message = handler.obtainMessage();
                message.setData(bundleQzone);
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        qq.showUser(null);
    }

    /**
     * 确认登录
     */
    private void saveInfo() {
        String username = etPhoneloginUsername.getText().toString().trim();
        String password = etPhoneloginPassword.getText().toString().trim();
        String regExp = "^\\+?(86|086)?(-)?(1[3|4|5|8|7]\\d{9})$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(username.trim());
        if (username == null || username.equals("") || !m.find()) {
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        } else if (password == null || password.equals("")) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            AsyncNewLoginPhone asyncLogin = new AsyncNewLoginPhone(getActivity(), handler);
            if (isAutoLogin) {
                sUtils.setStringValue(Constants.USERPHONE, username);
                sUtils.setStringValue(Constants.PASSWORD, password);
            }
            asyncLogin.execute(username, password, sUtils.getIntValue(Constants.INDUSTRY, 11) + "");
        }
    }
}
