package com.hr.ui.view.payquery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 网络帮助类，有post和get请求两种方式。
 * @author 123
 *
 */
public class NetUtils {
	public static InputStream HttpPost(String path, byte[] data) throws IOException {
		FakeX509TrustManager.allowAllSSL();
		URL url = new URL(path);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		conn.setRequestProperty("Charset", Constants.ENCODE);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			outStream.close();
			return conn.getInputStream();
		}
		return HttpPost(path, data);
	}
	
	public static InputStream HttpGet(String path) throws IOException {
		FakeX509TrustManager.allowAllSSL();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(5000);
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] readAsByteArray(InputStream inputStream) throws IOException
			 {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			return outSteam.toByteArray();
		} finally {
			outSteam.close();
			inputStream.close();
		}
	}

	public static String readAsString(InputStream inputStream, String encode) throws UnsupportedEncodingException, IOException
			 {
		return new String(readAsByteArray(inputStream), encode);
	}

	/**
	 * 检测网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
			if (networkInfos == null) {
				return false;
			}
			for (int i = networkInfos.length - 1; i >= 0; i--) {
				if (networkInfos[i].isConnected()) {
					return true;
				}
			}
		}
		return false;
	}
}
