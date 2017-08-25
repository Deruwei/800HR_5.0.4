package com.hr.ui.utils.netutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.AndroidSystemInfo;
import com.hr.ui.utils.VersionUpdate;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.view.custom.MyProgressDialog;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 推送
 */
public class Async_GetCity {
	private Context context;
	private Handler handlerFrom;
	private String json;

	public Async_GetCity(Context context, Handler handlerer) {
		this.context = context;
		this.handlerFrom = handlerer;
		new Async_GetCity.AsyncGetVersion( context).execute();
	}


	/**
	 * get版本信息
	 *
	 * @author 800hr:xuebaohua
	 */
	private class AsyncGetVersion extends AsyncTask<Void, Void, Integer> {
		private Context context;

		public AsyncGetVersion( Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			if (dialog != null && !dialog.isShowing()) {
//				dialog.show();
//			}

		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				InputStream inputStream = NetUtils.HttpGet("https://api.800hr.com/client/file/array/job.php");
				json = NetUtils.readAsString(inputStream, Constants.ENCODE);
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch (result){
				case 1:
					Message message=Message.obtain();
					message.what=1;
					message.obj=json;
					handlerFrom.sendMessage(message);
					break;
				case  -1:
					Message message1=Message.obtain();
					message1.what=2;
					handlerFrom.sendMessage(message1);
					break;
			}

		}
	}

}