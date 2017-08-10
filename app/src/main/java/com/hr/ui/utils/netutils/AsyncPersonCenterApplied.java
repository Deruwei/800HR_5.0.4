package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hr.ui.activity.MyPositionActivity;
import com.hr.ui.adapter.MyPositionAdapter;
import com.hr.ui.model.PositionInfo;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 获取个人中心-我申请的职位
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class AsyncPersonCenterApplied {
	private Context context;
	private ArrayList<PositionInfo> listPositionInfos;
	private String listTagString;// json中数组对象名称
	private MyPositionAdapter adapter;
	public static String user_id;
	Handler handler;
	RecyclerView listview;
	private Handler handlerService = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String json = (String) msg.obj;
				try {

					JSONObject jsonObject = new JSONObject(json);
					// System.out.println("我申请的职位：" + jsonObject);
					int error_code = jsonObject.getInt("error_code");
					switch (error_code) {
					case 0:// 请求成功

						JSONArray jsonArray = jsonObject
								.getJSONArray(listTagString);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) jsonArray
									.get(i);
							PositionInfo positionInfo = new PositionInfo();
							positionInfo.setEnterprise_name(jsonObject2
									.getString("enterprise_name"));
							positionInfo.setApplied_time(jsonObject2
									.getString("applied_time"));
							positionInfo.setApplied_num(jsonObject2
									.getString("applied_num"));
							positionInfo.setJob_id(jsonObject2
									.getString("job_id"));
							positionInfo.setJob_name(jsonObject2
									.getString("job_name"));
							positionInfo.setRecord_id(jsonObject2
									.getString("record_id"));
							positionInfo.setIs_expire(jsonObject2
									.getString("is_expire"));
							positionInfo.setArea(jsonObject2
									.getString("workplace"));
							positionInfo.setJob_name(jsonObject2
									.getString("job_name"));
							positionInfo.setEnterprise_id(jsonObject2
									.getString("enterprise_id"));
							positionInfo.setIs_apply(jsonObject2
									.getString("is_apply"));
							positionInfo.setIs_favourite(jsonObject2
									.getString("is_favourite"));
							positionInfo.setJob_id(jsonObject2
									.getString("job_id"));

							listPositionInfos.add(positionInfo);
						}
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// System.out.println("结束加载.....");
						// 判断是否全部加载
						if (jsonObject.has("navpage_info")) {
							JSONObject navpage_info = jsonObject
									.getJSONObject("navpage_info");
							String current_page = "";
							String total_pages = "";
							if (navpage_info.has("current_page")) {
								current_page = navpage_info
										.getString("current_page");
							}

							if (navpage_info.has("total_pages")) {
								total_pages = navpage_info
										.getString("total_pages");
							}
							if (current_page.length() != 0
									&& total_pages.length() != 0) {
								int current_pageInt = Integer
										.parseInt(current_page);
								int total_pageInt = Integer
										.parseInt(total_pages);
								if (current_pageInt >= total_pageInt) {
									/*MyPositionActivity.isLoadAll = true;*/
								}
							}
						}
						break;
					default:
						Toast.makeText(
								context,
								Rc4Md5Utils
										.getErrorResourceId(error_code),
								Toast.LENGTH_SHORT).show();
						break;
					}
					/*MyPositionActivity.isLoading = false;// 加载完毕*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

	};

	/**
	 * 
	 * @param context
	 * @param listPositionInfos
	 * @param listTagString
	 */
	public AsyncPersonCenterApplied(Context context,
									ArrayList<PositionInfo> listPositionInfos, String listTagString,
									MyPositionAdapter adapter, RecyclerView listview) {
		this.context = context;
		// System.out.println(context);
		this.listTagString = listTagString;
		this.listPositionInfos = listPositionInfos;
		this.adapter = adapter;
		this.listview = listview;

	}

	public void execute(String method, String page, String page_nums) {
		try {
			/*MyPositionActivity.isLoading = true;// 开始加载*/
			HashMap<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("method", method);
			requestParams.put("page", page);
			requestParams.put("page_nums", page_nums);

			NetService service = new NetService(context, handlerService);
			service.execute(requestParams);

		} catch (Exception e) {
			// System.out.println("取消-我申请的职位");
			e.printStackTrace();
		}
	}

}