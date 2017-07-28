package com.hr.ui.utils.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 公司介绍
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class AsyncCompanyIntroduce {
	private Context context;
	private Handler handler;
	private HashMap<String, String> resultHashMap;
	ProgressDialog dialog;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					// System.out.println("公司介绍：" + jsonObject);
					int error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 成功
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("enterprise_info");
						Iterator<String> keys = jsonObject2.keys();
						while (keys.hasNext()) {
							String key = keys.next();
							resultHashMap.put(key, jsonObject2.getString(key));
						}
						Message message = handler.obtainMessage();
						message.obj = resultHashMap;
						handler.sendMessage(message);
						break;
					default:
						Toast.makeText(
								context,
								Rc4Md5Utils
										.getErrorResourceId(error_code),
								Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

	};

	public AsyncCompanyIntroduce(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		resultHashMap = new HashMap<String, String>();
	}

	public void execute(String method, String enterprise_id) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("enterprise_id", enterprise_id);

			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			// System.out.println("取消");
			e.printStackTrace();
		}
	}

}