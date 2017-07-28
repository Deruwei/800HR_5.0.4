package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 
 * 用户反馈
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class Async_user_feedback {
	private Context context;
	private Handler handler;
	private HashMap<String, String> resultHashMap;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					//System.out.println("用户反馈：" + jsonObject);
					int error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 成功
						Message message = handler.obtainMessage();
						message.what = 0;
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

	public Async_user_feedback(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		resultHashMap = new HashMap<String, String>();
	}

	public void execute(String method, String email, String content) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("email", email);
			requestParams.put("content", content);
			requestParams.put("cate_id", "20");
			// requestParams.put("apptype", params[3]);

			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			//System.out.println("取消");
			e.printStackTrace();
		}
	}

}