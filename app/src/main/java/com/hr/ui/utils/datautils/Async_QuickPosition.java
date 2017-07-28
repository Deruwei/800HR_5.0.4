package com.hr.ui.utils.datautils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.utils.netutils.NetService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 
 * 快速投递
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class Async_QuickPosition {
	private Context context;
	private Handler handler;
	int error_code;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 成功
						if (handler != null) {
							Message message = handler.obtainMessage();
							message.what = 0;
							handler.sendMessage(message);
						}
						Toast.makeText(context, "申请职位成功", Toast.LENGTH_SHORT)
								.show();
						break;
					case 304:// 为设置默认简历
						Toast.makeText(context, "请先到简历中心设置默认简历",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(context,
								Rc4Md5Utils.getErrorResourceId(error_code),
								Toast.LENGTH_SHORT).show();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "申请失败", Toast.LENGTH_SHORT).show();
				}
			}
		};

	};

	public Async_QuickPosition(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	public void execute(String method, String job_id, String resume_id,
			String resume_language) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("job_id", job_id);
			requestParams.put("resume_id", resume_id);
			requestParams.put("resume_language", resume_language);
			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}