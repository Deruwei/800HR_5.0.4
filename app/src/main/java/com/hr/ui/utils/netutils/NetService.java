package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hr.ui.HrApplication;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.AndroidSystemInfo;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.tools.AndroidUtils;
import com.hr.ui.view.custom.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * 提供和服务器连接的借口。
 *
 * @author 800hr:xuebaohua
 */
public class NetService {

    // 当需要重连接时，暂存目标任务。先连接.
    private static Context tempContext;
    private static Handler tempHandler;
    private static HashMap<String, String> tempRequestParams;
    public static ArrayList<RequestQueue> listRequestQueues = new ArrayList<RequestQueue>();

    public RequestQueue mQueue;
    private Context context;
    private Handler handler;
    private Message message;
    private MyProgressDialog dialog;
    private static boolean reconnecting = false;// 是否启动了重连机制
    /**
     * 输出网络访问时间
     */
    public static Handler testHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:// 重连结果
                    String jsonString = (String) msg.obj;
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        int error_code = jsonObject.getInt("error_code");
                        switch (error_code) {
                            case 0:
                                MyUtils.session_key = jsonObject.getString("session_key");
                                Rc4Md5Utils.secret_key = MyUtils.pre_secret_key + jsonObject.getString("secret_key");
                                MyUtils.svr_api_ver = jsonObject.getString("svr_api_ver");
                                // 还原任务
                                new NetService(tempContext, tempHandler).execute(tempRequestParams);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 303:// error_code:303(用户未登录)
                    MyUtils.isLogin = false;
                    MyUtils.isLogouted = true;
                    MyUtils.username = null;
                    MyUtils.userID = null;
//                    // 刷新设置中注销按钮状态
//                    if (SettingActivityGroup.group != null) {
//                        SetingActivity setingActivity = (SetingActivity) SettingActivityGroup.group
//                                .getLocalActivityManager().getActivity(
//                                        "SetingActivity");
//                        if (setingActivity != null) {
//                            setingActivity.refreshLogoutBg();
//                        }
//                    }
//                    // System.out.println("curgroup:" + MyUtils.currentGroup);
//                    if (MyUtils.currentGroup == ResumeCenterActivityGroup.group) {
//                        initLoginView(Constants.RESUMECENTER_LOGIN);
//                    } else if (MyUtils.currentGroup == PersonCenterActivityGroup.group) {
//                        initLoginView(Constants.PERSONCENTER_LOGIN);
//                    }
                    break;
                default:
                    break;
            }

        }

        ;
    };

//    // 加载简历中心页
//    private static void initLoginView(int loginTag) {
//        Intent intent = new Intent(MyUtils.currentGroup, EnterActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("LoingTag", loginTag);
//        MyUtils.currentGroup.clearViews();
//        View view = MyUtils.currentGroup.getLocalActivityManager()
//                .startActivity("EnterActivity", intent).getDecorView();
//        MyUtils.currentGroup.replaceView(view);
//    }

    /**
     * 此方法显示网络进度条
     *
     * @param context
     * @param handler
     */
    public NetService(Context context, final Handler handler) {
        this.context = context;
        this.handler = handler;
        dialog = new MyProgressDialog(context);
    }

    /**
     * 此方法不显示网络进度条
     *
     * @param context
     * @param handler
     * @param ableShowProgress
     */
    public NetService(Context context, final Handler handler,
                      boolean ableShowProgress) {
        this.context = context;
        this.handler = handler;
    }

    /**
     * 启动任务
     */
    public void execute(final HashMap<String, String> requestParams) {
        if(context!=null) {
            try {
                message = new Message();
                if (dialog != null && !dialog.isShowing()&&context!=null) {
                    dialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MyUtils.ableInternet = NetUtils.checkNet(context);
            if (!MyUtils.ableInternet) {
                dialog.dismiss();
                message.obj = "{error_code:11}";
                message.what = 0;
                handler.sendMessage(message);
                return;
            }
            System.out.println("请求字段：" + requestParams.toString());
            mQueue = HrApplication.getInstance().getRequestQueue();
            ;
            /**
             * 发布版本的真实地址 SERVER_URL，把NetService类中的TEST_SERVER_URL改为SERVER_URL
             */
            NetRequest<String> request = new NetRequest<String>(Method.POST,
                    Constants.SERVER_URL, new Listener<String>() {
                @Override
                public void onResponse(String arg0) {
                    // listRequestQueues.remove(mQueue);
                    System.out.println("请求结果：" + arg0);
                    if (dialog != null && dialog.isShowing()||context==null) {
                        dialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(arg0);
                        String error_code = jsonObject.getString("error_code");
                            /*
                             * sessionkey过期处理
							 */
                        if ("203".equals(error_code) || "204".equals(error_code) || ("205".equals(error_code) && reconnecting == false)
                                || ("205.2".equals(error_code) && reconnecting == false)) {
                            tempContext = context;
                            tempHandler = handler;
                            tempRequestParams = requestParams;
                            reconnecting = true;
                            HashMap<String, String> requestParams = initReconnectParams(context);
                            NetService service = new NetService(context, testHandler, false);
                            service.execute(requestParams);
                            return;
                        } else if ("303".equals(error_code)) {// 用户未登录问题
                            SharedPreferencesUtils utils = new SharedPreferencesUtils(context);
                            utils.setBooleanValue(Constants.AUTO_LOGIN, false);
                            testHandler.sendEmptyMessage(303);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        reconnecting = false;
                        message.obj = arg0;
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    // listRequestQueues.remove(mQueue);
                    // ***输出错误信息***//
                    // AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
                    // NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
                    // NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
                    // ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
                    // SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
                    // TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
                    NetworkResponse response = arg0.networkResponse;
//                System.out.println("错误信息：" + arg0.toString());
                    if (response != null) {
                        switch (response.statusCode) {
                            case 404:
                                break;
                            case 422:
                                break;
                            case 401:
                                break;
                            default:
                                break;
                        }
                    }
                    if (dialog != null && dialog.isShowing()||context==null) {
                        dialog.dismiss();
                    }
                    try {
                        message.obj = arg0;
                        message.what = -1;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, requestParams) {

                @Override
                protected String parseNetworkResponseDelegate(String jsonString) {
                    return jsonString;
                }
            };
            // 如果失败，3s后自动重新请求
            request.setRetryPolicy(new DefaultRetryPolicy(3 * 1000, 3, 1.0f));
            mQueue.add(request);
            mQueue.start();
            // System.out.println("超时时间：" + request.getTimeoutMs());
            // listRequestQueues.add(mQueue);
        }else{
            if(dialog!=null) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 停止任务
     */
    public void stop() {
        if (mQueue != null) {
            mQueue.stop();
        }
    }

    /**
     * 会话连接
     */
    public void connect() {
        HashMap<String, String> requestParams = initConnParams(context);
        execute(requestParams);
    }

    /**
     * 初始化连接
     *
     * @param context
     * @return
     */
    private HashMap<String, String> initConnParams(Context context) {
        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", "user.connect");
        requestParams.put("api_ver", Constants.API_VER);
        requestParams.put("client_ver", AndroidSystemInfo.getVerName(context));
        requestParams.put("os_name", "android");
        requestParams.put("os_ver", android.os.Build.VERSION.RELEASE);
        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(context);
        if (sUtils.getStringValue(Constants.IS_GUIDE, "1") == "1") {// 第一次运行
            sUtils.setStringValue(Constants.IS_GUIDE, "0");
            requestParams.put("new_setup", "1");
            MyUtils.isFirst = true;
            sUtils.setStringValue(Constants.DEVICE_USER_ID, newRandomUUID());
        } else {
            requestParams.put("new_setup", "0");
            MyUtils.isFirst = false;
        }
        requestParams.put("appcode", "personal");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        requestParams.put("width", String.valueOf(displayMetrics.widthPixels));
        requestParams.put("height", String.valueOf(displayMetrics.heightPixels));
        String username = sUtils.getStringValue(Constants.DEVICE_USER_ID, "");
        NetParamsConstants.DEVICE_USER_ID = username;
        requestParams.put("phonecode", username);
        requestParams.put("model", AndroidUtils.get_model());
        requestParams.put("dnfrom", Constants.DNFROM);
        requestParams.put("network_type", MyUtils.network_type);
//        System.out.print("第一次连接" + requestParams.toString());
        return requestParams;

    }

    /**
     * 初始化重连接字段
     */
    private static HashMap<String, String> initReconnectParams(Context context) {
        HashMap<String, String> requestParams = new HashMap<String, String>();
        requestParams.put("method", "user.connect");
        requestParams.put("api_ver", Constants.API_VER);
        requestParams.put("client_ver", AndroidSystemInfo.getVerName(context));
        requestParams.put("os_name", "android");
        requestParams.put("os_ver", android.os.Build.VERSION.RELEASE);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        requestParams.put("width", String.valueOf(displayMetrics.widthPixels));
        requestParams.put("width", String.valueOf(displayMetrics.widthPixels));
        requestParams.put("height", String.valueOf(displayMetrics.heightPixels));
        requestParams.put("phonecode", NetParamsConstants.DEVICE_USER_ID);
        requestParams.put("model", AndroidUtils.get_model());
        requestParams.put("dnfrom", Constants.DNFROM);
        requestParams.put("new_setup", "0");
        requestParams.put("appcode", "personal");
        requestParams.put("network_type", MyUtils.network_type);
        MyUtils.session_key = null;
        Rc4Md5Utils.secret_key = MyUtils.init_secret_key;
        // System.out.println("重连");
        return requestParams;
    }

    private String newRandomUUID() {// add by yl
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

    /**
     * 获得 city 数组信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public static ArrayList<HashMap<String, String>> getCityArray(
            JSONArray jsonArray, String id) throws Exception {
        String endString = "00";
        int endIndex = 2;

        return getArrayList(jsonArray, id, endString, endIndex);
    }

    /**
     * 获得 job 数组信息
     *
     * @param id
     * @return
     */
    public static ArrayList<HashMap<String, String>> getJobArray(
            JSONArray jsonArray, String id) throws Exception {
        String endString = "000";
        int endIndex = 3;

        return getArrayList(jsonArray, id, endString, endIndex);
    }

    /**
     * 获取 job 的 JSON 数组对象，由于json 数据不大，就先存在内存中，避免每次读取文件获得
     *
     * @param context
     * @param filename
     * @param industryId
     * @return 对应行业的 JSONArray
     * @throws Exception
     */
    public static JSONArray getJobAsJSONArray(Context context, String filename,
                                              String industryId) throws Exception {
        String json = getFileAsString(context, filename);
        return new JSONObject(json).getJSONArray(industryId);
    }

    /**
     * 获取 city 的 JSON 数组对象，由于json 数据不大，就先存在内存中，避免每次读取文件获得
     *
     * @param context
     * @param filename
     * @return 整个 city 的 JSONArray
     * @throws Exception
     */
    public static JSONArray getCityAsJSONArray(Context context, String filename)
            throws Exception {
        String json = getFileAsString(context, filename);
        return new JSONArray(json);
    }

    private static ArrayList<HashMap<String, String>> getArrayList(
            JSONArray jsonArray, String id, String endString, int endIndex)
            throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        int len = jsonArray.length();
        JSONObject jsonObject = null;

        if ("0".equals(id)) { // 一级分类
            for (int i = 0; i < len; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String key = (String) jsonObject.keys().next();
                if (key.endsWith(endString)) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("key", key);
                    item.put("value", jsonObject.getString(key));
                    list.add(item);
                }
            }
        } else {
            String preId = id.substring(0, endIndex);
            for (int i = 0; i < len; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String key = (String) jsonObject.keys().next();
                if (key.startsWith(preId) && !key.endsWith(endString)) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("key", key);
                    item.put("value", "    " + jsonObject.getString(key));
                    list.add(item);
                }
            }
        }
        return list;
    }

    private static String getFileAsString(Context context, String filename)
            throws FileNotFoundException, UnsupportedEncodingException,
            IOException {
        FileInputStream in = context.openFileInput(filename);
        return NetUtils.readAsString(in, Constants.ENCODE);
    }

}
