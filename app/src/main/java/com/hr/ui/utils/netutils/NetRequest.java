package com.hr.ui.utils.netutils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @author 800hr:xuebaohua QQ:840188952
 */
public abstract class NetRequest<T> extends Request<T> {
    private final Listener<T> mListener;
    private byte[] data;

    /**
     * 构造MyRequest
     *
     * @param method
     * @param url
     * @param listener
     * @param errListener
     * @param requestParams
     */
    public NetRequest(int method, String url, Listener<T> listener,
                      ErrorListener errListener, HashMap<String, String> requestParams) {
        super(method, url, errListener);
        mListener = listener;
        setShouldCache(true);
        this.data = mapTobyteArray(requestParams);
    }

    /**
     * 集合转化成 byte类型的数组
     *
     * @param requestParams
     * @return
     */
    private byte[] mapTobyteArray(HashMap<String, String> requestParams) {
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            /*
             * 将字符串 遍历 前后加上 = &符号
			 */
            params.append(entry.getKey());
            params.append("=");
            params.append(entry.getValue());
            params.append("&");
        }

        if (params.length() > 0)
            params.deleteCharAt(params.length() - 1);
        try {

            String parameter = new String(params.toString().getBytes(
                    Constants.ENCODE), "iso8859-1");

            String encodeParams = Rc4Md5Utils.AuthCode(parameter, "",
                    "0");
            String param = (MyUtils.session_key == null ? "" : "s="
                    + MyUtils.session_key + "&")
                    + "d=" + URLEncoder.encode(encodeParams, Constants.ENCODE);
//            System.out.print("加密后" + param.toString());
            return param.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求头
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charset", "utf-8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        // headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("Connection", "Keep-alive");
        return headers;
    }

    /**
     * 请求体
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        // TODO Auto-generated method stub
        return data;
    }

    /**
     * 响应
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String string = new String(response.data, Constants.ENCODE);
            T result = parseNetworkResponseDelegate(string);// 这里用了一个泛型的技巧，T由后面的实现确定
            mListener.onResponse(result);// 成功时的回调方法
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        // System.out.println("params:" + super.getParams());
        return super.getParams();
    }

    // parseNetworkResponseDelegate解析json的接口，由子类实现
    protected abstract T parseNetworkResponseDelegate(String jsonString);

    @Override
    protected void deliverResponse(T arg0) {
        // TODO Auto-generated method stub

    }
    /**
     * 设置超时时间和自动请求
     */
    // @Override
    // public RetryPolicy getRetryPolicy() {
    // RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,
    // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
    // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    // return retryPolicy;
    // }

}
