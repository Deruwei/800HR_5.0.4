package com.hr.ui.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.DownAPK;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.MyProgressDialog;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * 新版本检测与更新
 *
 * @author 800hr:xuebaohua
 */
public class VersionUpdate {

    private Context context;
    private Handler handlerFrom;

    public VersionUpdate(Context context, Handler handlerer) {
        this.context = context;
        this.handlerFrom = handlerer;
        String clientVerString = AndroidSystemInfo.getVerName(context);
        new AsyncGetVersion(clientVerString, context).execute();
    }

    /**
     * 检查新版本
     */
    private void checkVersion(boolean updata, String content,
                              final String urlDownLoad) {
        if (updata) {
            // 有新版本
            new AlertDialog.Builder(context)
                    .setTitle(R.string.update_new)
                    .setCancelable(false)
                    .setMessage(content)
                    .setPositiveButton(R.string.alert_dialog_ok_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    // 清楚原2.0SP数据
                                    try {
                                        SharedPreferencesUtils sUtils = new SharedPreferencesUtils(
                                                context, "800HR");
                                        sUtils.deleteAll();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//									// 下载
                                    new DownAPK(context, "800hr.apk").downFile("http://www.800hr.com/app/android/800hr.apk");
                                }
                            })
                    .setNegativeButton(R.string.alert_dialog_cancel_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // 不更新
                                    dialog.dismiss();
                                    handlerFrom.sendEmptyMessage(1000);
                                }
                            }).show();
        } else {
            handlerFrom.sendEmptyMessage(1000);
            // handlerFrom.sendEmptyMessage(1001);
        }
    }

    /**
     * get版本信息
     *
     * @author 800hr:xuebaohua
     */
    private class AsyncGetVersion extends AsyncTask<Void, Void, Integer> {
        private String clientVerString;
        private String content;
        private String url;
        private Context context;
        private MyProgressDialog dialog;

        public AsyncGetVersion(String clientVerString, Context context) {
            this.clientVerString = clientVerString;
            this.context = context;
            dialog = new MyProgressDialog(context);
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
                if (!NetUtils.checkNet(context)) {
                    return 1;
                }
                InputStream inputStream = NetUtils.HttpGet("https://api.800hr.com/client/getclientver.php?cur_ver=" + clientVerString);
                String json = NetUtils.readAsString(inputStream, Constants.ENCODE);
                JSONObject jsonObject = new JSONObject(json)
                        .getJSONObject("android");
                // System.out.println("版本检测：" + json);
                if (clientVerString.compareTo(jsonObject.getString("ver")) == -1) {
                    content = jsonObject.getString("text");
                    // content = getResources().getString(R.string.update_text);
                    url = jsonObject.getString("url");
                    return 0;
                }
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            switch (result) {
                case 0:// 有新版本
                    // System.out.println("有新版本");
                    checkVersion(true, content, url);
                    break;
                case 1:// 无新版本
                    // System.out.println("无新版本");
                    checkVersion(false, null, null);
                    break;
                case -1:// 异常
                    // System.out.println("版本检测异常");
                    checkVersion(false, null, null);
                    break;
                default:
                    break;
            }

        }
    }
}
