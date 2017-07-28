package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 推送
 */
public class Async_GetNotice {
	private Context context;
	private Handler handler;
	private HashMap<String, String> resultHashMap;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {

					JSONObject jsonObject = new JSONObject(json);
					//System.out.println("得到设置的开关：" + jsonObject);
					int error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 成功
						JSONObject jsonObject2 = jsonObject
								.getJSONObject("notice_info");
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
						Toast.makeText(context,
								Rc4Md5Utils.getErrorResourceId(error_code),
								Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

	};

	public Async_GetNotice(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		resultHashMap = new HashMap<String, String>();
	}

	public void execute(String method, String alias) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("phonecode", alias);
			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			//System.out.println("取消");
			e.printStackTrace();
		}
	}

}