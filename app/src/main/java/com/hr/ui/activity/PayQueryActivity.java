package com.hr.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.utils.tools.LogTools;
import com.hr.ui.view.payquery.Constants;
import com.hr.ui.view.payquery.NetUtils;
import com.hr.ui.view.payquery.OperatorSharedPreference;
import com.hr.ui.view.payquery.TestSD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 薪酬查询
 */
public class PayQueryActivity extends BaseActivity {
    public static PayQueryActivity group;

    private ImageView iv_payquery_back;
    private WebView webView;
    private ProgressDialog progressdialog;
    private ProgressDialog pd = null;// 下载时的progress

    // webview获取到的行业id和工作id
    private int industry_id = 0;
    private int job_id = 0;
    private int enterprise_id = 0;

    private static int index_click = -1;// -1:默认值 0:比较按钮 1：职位分类或工作地点

    // cookie
    OperatorSharedPreference sharedPreference;

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = this;
//		MyUtils.currentGroup = PayQueryActivity.group;
        setContentView(R.layout.activity_pay_query);
        sharedPreference = new OperatorSharedPreference(this);

        iv_payquery_back = (ImageView) findViewById(R.id.iv_payquery_back);
        iv_payquery_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreference.clear();
        webView = (WebView) findViewById(R.id.webview);
        webView.requestFocus();
        // 屏蔽长按选择文本
        webView.setOnLongClickListener(new WebView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("请稍后...");
        progressdialog.show();
        // progress.setVisibility(View.VISIBLE);
        getWindow().setBackgroundDrawable(null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				Toast.makeText(PayQueryActivity.this, url+".........", 0).show();
                view.loadUrl(url.toString());
                return true;
            }
        });
        /*
         * 调用js脚本
		 */
        webView.addJavascriptInterface(new Object() {
            /*
             * 跳转到行业找工作
             */
            public void onclick(int hyid, int jobid, int enterpriseid) {
                industry_id = hyid;
                job_id = jobid;
                enterprise_id = enterpriseid;
                new AsyncWebview().execute();
            }

            /**
             * 获取网络状态
             */
            public String getnetstate() {
                if (!NetUtils.checkNet(PayQueryActivity.this)) {
                    return "0";
                }
                return "1";
            }

            /**
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             *
             * 写入cookie
             */
            public void setcookie(final String key, final String value) {
                sharedPreference.writeToShared(key, value);
            }

            /**
             * 读取cookie
             */
            public String getcookie(String key) {
                return sharedPreference.readFromShared(key);
            }

            /*
             * 清除所有cookie
             */
            public boolean clearcookie() {
                return sharedPreference.clear();
            }

            public boolean clearone(String key) {
                return sharedPreference.clearone(key);
            }

            /*
             * 记录点击事件的位置索引，以便控制webview返回的位置
             */
            public void index_click(int index) {
                PayQueryActivity.index_click = index;
            }
        }, "android_payquery");
		/*
		 * webview配置
		 */
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                LogTools.i("life", "ProgressChanged:view : " + view);
                LogTools.i("life", "ProgressChanged:newProgress : " + newProgress);
                if (newProgress == 100) {
                    if (progressdialog.isShowing()) {
                        progressdialog.dismiss();
                    }
                } else {

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                System.out.println(url);
                new AlertDialog.Builder(PayQueryActivity.this)
                        .setTitle("温馨提示")
                        .setMessage(message)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                    }
                                }).show();
                result.confirm();
                return true;
                // return super.onJsAlert(view, url, message, result);打印出浏览信息的网址
            }

            @Override
            public boolean onJsConfirm(final WebView view, final String url,
                                       String message, JsResult result) {
                new AlertDialog.Builder(PayQueryActivity.this)
                        .setTitle("温馨提示")
                        .setMessage(message)
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                        view.loadUrl(Constants.URL);
                                    }
                                }).show();
                result.confirm();
                return true;
                // return super.onJsConfirm(view, url, message, result);
            }

        });

//		Toast.makeText(PayQueryActivity.this, Constants.URL, 0).show();
        webView.loadUrl(Constants.URL);//获得您的查询条件页面
    }

    private class AsyncWebview extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                openOtherProgect();
            }
            super.onPostExecute(result);
        }
    }

    /**
     * 打开“行业找工作”
     */
    private void openOtherProgect() {
        // TODO Auto-generated method stub
        PackageInfo packageInfo;

        try {
            packageInfo = PayQueryActivity.this.getPackageManager().getPackageInfo(
                    "com.hr.ui", PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            new AlertDialog.Builder(PayQueryActivity.this)
                    .setTitle(R.string.install_confirm)
                    .setCancelable(false)
                    .setMessage(R.string.no_exit_800hrapp)
                    .setPositiveButton(R.string.alert_dialog_ok_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    boolean[] testSD = new TestSD().getState();
                                    if (testSD[0] && testSD[1]) {
                                        // 正常下载
                                        pd = new ProgressDialog(PayQueryActivity.this);
                                        pd.setTitle("正在下载");
                                        pd.setMessage("请稍后...0%");
                                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        downFile(Constants.HR800_release,
                                                PayQueryActivity.this);

                                    } else {
                                        // SD卡不可用
                                        Toast.makeText(PayQueryActivity.this,
                                                R.string.unuser_sdcard,
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                    .setNegativeButton(R.string.alert_dialog_cancel_btn,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }

                            }).show();
        }
//		else {
        // 跳转到“行业找工作”的职位推荐页
			/*
			 * Intent i = new Intent(); ComponentName cn = new
			 * ComponentName("com.hr.ui", "com.hr.ui.HRTabActivity");
			 * i.setComponent(cn); i.putExtra("industry_id",
			 * String.valueOf(industry_id)); i.putExtra("job_id",
			 * String.valueOf(job_id)); i.putExtra("enterprise_id",
			 * String.valueOf(enterprise_id)); i.putExtra("fromOtherPro", true);
			 * startActivity(i);
			 */

//		}
    }

    /**
     * 下载apk
     */
    private void downFile(final String url, final Context context) {
        pd.show();

        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    int downloadCount = 0;
                    int updateTotalSize = (int) entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(Environment
                                .getExternalStorageDirectory()
                                .getAbsolutePath(), "800hr.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        int charb = -1;
                        int count = 0;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                            if ((downloadCount == 0)
                                    || (count * 100 / updateTotalSize) - 10 > downloadCount) {
                                downloadCount += 10;
                                Message message = handler.obtainMessage();
                                String upcount = count * 100 / updateTotalSize
                                        + "%";
                                message.obj = upcount;
                                message.arg1 = 1;
                                handler.sendMessage(message);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.arg1) {
                case 0:
                    pd.cancel();
                    update();
                    break;
                case 1:
                    pd.setMessage("请稍后..." + msg.obj);
                    break;
            }
        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    private void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                message.arg1 = 0;
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath(), "800hr.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        System.out.println(Constants.VERSION);
//        webView.requestFocus();
//
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            String urlString = webView.getUrl();
//            if ("http://api.800hr.com/user/emolument/index_solo.html"
//                    //	file:///android_asset/emolument/index_solo.html
////					file:///android_asset/emolument/result.html
//                    .equalsIgnoreCase(urlString)) {
//                exit();
//                return true;
//            }
//
//            /**
//             * 兼容4.0返回处理（首页返回键控制）
//             */
//            if ("http://api.800hr.com/user/emolument/index_solo.html#/api.800hr.com/user/emolument/index_solo.html"
//                    .equalsIgnoreCase(urlString)) {
//                exit();
//                return true;
//            }
//
//            /**
//             * 控制比较按钮和职位分类、工作地点的返回
//             */
//            // System.out.println("index:" + Webview.index_click);
//            if (PayQueryActivity.index_click == 0) {
//                // 点击的是比较按钮
//                PayQueryActivity.index_click = -1;
//                webView.clearHistory();
//                webView.loadUrl(Constants.URL);
//                return true;
//            } else if (PayQueryActivity.index_click == 1) {
//                // 单击了职位分类或工作地点
//                /**
//                 * 兼容4.0（点击结果页后返回的处理）
//                 */
//                if ("4.0".equalsIgnoreCase(Constants.VERSION)
//                        || "4.0.1".equalsIgnoreCase(Constants.VERSION)
//                        || "4.0.2".equalsIgnoreCase(Constants.VERSION)
//                        || "4.0.3".equalsIgnoreCase(Constants.VERSION)) {
//                    if (urlString
//                            .contentEquals("http://api.800hr.com/user/emolument/result.html")) {
//                        PayQueryActivity.index_click = -1;
//                        webView.clearHistory();
//                        webView.loadUrl(Constants.URL);
//                        return true;
//                    }
//                }
//            } else if (PayQueryActivity.index_click == -1) {
//                /**
//                 * 处理个别机型返回主页时，找不到主页面问题
//                 */
//                if (urlString
//                        .contentEquals("http://api.800hr.com/user/emolument/result.html")) {
//                    PayQueryActivity.index_click = -1;
//                    webView.clearHistory();
//                    webView.loadUrl(Constants.URL);
//                    return true;
//                }
//            }
//
//            webView.goBack();
//            return true;
//        }
//        // Pops up a dialog before exit
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && (!webView.canGoBack())) {
//            exit();
//            return true;
//        }
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出
     */
    private void exit() {
        new AlertDialog.Builder(this).setTitle("是否退出").setMessage("是否退出薪酬查询")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        sharedPreference.clear();
                        android.os.Process.killProcess(android.os.Process
                                .myPid());
                    }
                }).show();
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(R.string.more);
//		return super.onCreateOptionsMenu(menu);
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		more();
//		return true;
//	}
//	private void more() {
//		startActivity(new Intent(PayQueryActivity.this, MoreActivityGroup.class));
//		MobclickAgent.onEvent(PayQueryActivity.this, Constants.UMENG_more);
//	}

}
