package com.hr.ui.utils.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 升级简历
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class AsyncResumeUpgrade {
	private Context context;
	private Handler handler;
	private ProgressDialog dialog;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					int error_code = jsonObject.getInt("error_code");
					handler.sendEmptyMessage(3);
					switch (error_code) {
					case 0:// 成功
						Toast.makeText(context,
								context.getString(R.string.upgrade_success),
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
				}
			}
		};

	};

	public AsyncResumeUpgrade(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		dialog = new ProgressDialog(context);
		dialog.setCancelable(true);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	public void execute(String resume_id, String resume_language) {
		try {
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", "user_resume.upgradetype");
			requestParams.put("resume_id", resume_id);
			requestParams.put("resume_language", resume_language);

			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			//System.out.println("取消");
			e.printStackTrace();
		}
	}
}