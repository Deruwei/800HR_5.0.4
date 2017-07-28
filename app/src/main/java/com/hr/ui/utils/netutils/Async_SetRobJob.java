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
 *订阅条件
 */
public class Async_SetRobJob {
	private Context context;
	private Handler handler;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					//System.out.println("抢工作设置：" + jsonObject);
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

	public Async_SetRobJob(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	public void execute(String method, String func, String area,
			String searchword, String industry, String phonecode,
			String workyear, String worktype, String issuedate, String study,
			String stuffmunber, String salary) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("func", func == null ? "0" : func);
			requestParams.put("area", area == null ? "0" : area);
			requestParams.put("searchword", searchword == null ? "0"
					: searchword);
			requestParams.put("industry", industry == null ? "0" : industry);
			requestParams.put("phonecode", phonecode);

			requestParams.put("workyear", workyear == null ? "0" : workyear);
			requestParams.put("worktype", worktype == null ? "0" : worktype);
			requestParams.put("issuedate", issuedate == null ? "0" : issuedate);
			requestParams.put("study", study == null ? "0" : study);
			requestParams.put("stuffmunber", stuffmunber == null ? "0"
					: stuffmunber);
			requestParams.put("salary", salary == null ? "0" : salary);
			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			//System.out.println("取消");
			e.printStackTrace();
		}
	}
}