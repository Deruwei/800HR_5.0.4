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
 * 推送设置
 * 
 */
public class Async_Set_Rob_Switch {
	private Context context;
	private Handler handler;
	private HashMap<String, String> resultHashMap;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {

					JSONObject jsonObject = new JSONObject(json);
					int error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 成功
//						Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT)
//								.show();

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

	public Async_Set_Rob_Switch(Context context,
			HashMap<String, String> resultHashMap) {
		this.context = context;
		this.resultHashMap = resultHashMap;
	}

	public void execute() {
		try {
			NetService service = new NetService(context, handlerService);
			service.execute(resultHashMap);
		} catch (Exception e) {
			//System.out.println("取消");
			e.printStackTrace();
		}
	}

}