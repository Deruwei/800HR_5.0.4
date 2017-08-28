package com.hr.ui.utils.netutils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.widget.Toast;

import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 职位，省份数组更新
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class AsyncArrayUpdate {
	private Context context;
	private Handler handler;
	private Handler handlerService = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {// 成功
				System.out.println("会话已连接");
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int error_code = Integer.parseInt(jsonObject
							.getString("error_code"));
					// 强制升级106
					if (error_code == 106) {
						final String urlString = jsonObject.getString("url");
						Builder builder = new Builder(context);
						builder.setTitle("版本提示");
						builder.setMessage("当前版本过低，请升级后使用。");
						builder.setPositiveButton("升级", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 清楚原2.0SP数据
								try {
									SharedPreferencesUtils sUtils = new SharedPreferencesUtils(
											context, "800HR");
									sUtils.deleteAll();
								} catch (Exception e) {
									e.printStackTrace();
								}

//								// 下载
//								new DownAPK(context, "800hr.apk")
//										.downFile("http://www.800hr.com/app/android/800hr.apk");
							}
						});
						builder.setNegativeButton("取消", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								handler.sendEmptyMessage(1003);// exit sys
							}
						});
						builder.show();
						return;
					}
					if(error_code==11){
						handler.sendEmptyMessage(1003);
					}
					//
					if (error_code != 0) {
						handler.sendEmptyMessage(1001);
						Toast.makeText(context,
								Rc4Md5Utils.getErrorResourceId(error_code),
								Toast.LENGTH_SHORT).show();
						return;
					}
					MyUtils.session_key = jsonObject.getString("session_key");
					Rc4Md5Utils.secret_key = MyUtils.pre_secret_key
							+ jsonObject.getString("secret_key");
					MyUtils.svr_api_ver = jsonObject.getString("svr_api_ver");
					if (MyUtils.USE_ONLINE_ARRAY) {
						System.out.println("检测数据更新");
						// 检测 job 和 city 文件，是否需要跟新
						updateArray(context);
					} else {
						handler.sendEmptyMessage(1002);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {// 有网络，但连接失败
				handler.sendEmptyMessage(1002);
			}
		};
	};

	public AsyncArrayUpdate(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	public void execute() {
		// 建立会话连接
		NetService service = new NetService(context, handlerService);
		service.connect();
	}

	/**
	 * 更新数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public void updateArray(final Context context) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					InputStream in = NetUtils.HttpGet("https://api.800hr.com/client/getarrayver.php");
					if (in != null) {
						String result = NetUtils.readAsString(in,
								Constants.ENCODE);
						// ① 获得 json 数据中的 city 和 job ver 和 lasttime
						// 值，与SharedPreferences
						// 文件中进行比较
						JSONObject json = new JSONObject(result);
						JSONObject cityJson = json.getJSONObject("city");
						JSONObject jobJson = json.getJSONObject("job");

						String cityVer = cityJson.getString("ver");
						String cityDate = cityJson.getString("lasttime");
						String jobVer = jobJson.getString("ver");
						String jobDate = jobJson.getString("lasttime");

						SharedPreferences sp = context.getSharedPreferences(
								Constants.PREFS_NAME, Context.MODE_PRIVATE);

						// ② 进行比较判断
						if (!(sp.getString(Constants.CITY_VER, "0.0").equals(
								cityVer) && sp.getString(Constants.CITY_DATE,
								"2010-01-01 10:10:00").equals(cityDate))) {
							updateCJ(context, "https://api.800hr.com/client/file/array/city.php", "city.json");
							// 更新 SharedPreferences 中的数据
							Editor editor = sp.edit();
							editor.putString(Constants.CITY_VER, cityVer);
							editor.putString(Constants.CITY_DATE, cityDate);
							editor.commit();
						}

						if (!(sp.getString(Constants.JOB_VER, "0.0").equals(
								jobVer) && sp.getString(Constants.JOB_DATE,
								"2010-01-01 10:10:00").equals(jobDate))) {
							updateCJ(
									context,
									"https://api.800hr.com/client/file/array/job.php",
									"job.json");
							// 更新 SharedPreferences 中的数据
							Editor editor = sp.edit();
							editor.putString(Constants.JOB_VER, jobVer);
							editor.putString(Constants.JOB_DATE, jobDate);
							editor.commit();
						}
						// 更新成功
						handler.sendEmptyMessage(1002);
						System.out.println("数组更新成功");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 更新失败
				handler.sendEmptyMessage(1002);
				System.out.println("数组更新失败");
			}
		}).start();
	}

	/**
	 * 更新 city 和 job 文件信息
	 * 
	 * @param context
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void updateCJ(Context context, String path, String filename)
			throws IOException, FileNotFoundException {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = NetUtils.HttpGet(path);
			out = context.openFileOutput(filename, Context.MODE_PRIVATE);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
	}

}
