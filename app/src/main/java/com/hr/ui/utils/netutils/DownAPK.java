package com.hr.ui.utils.netutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 下载apk
 */
public class DownAPK {

    private ProgressDialog pd = null;
    private Context context;
    String fileName;//文件名字

    public DownAPK(Context context, String fileName) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.fileName = fileName;
        pd = new ProgressDialog(context);
        pd.setTitle("正在下载");
        pd.setMessage("请稍后...0%");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                //System.out.println("下载地址："+url);
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
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                fileName);
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
                                String upcount = count * 100 / updateTotalSize + "%";
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
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), fileName)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.arg1) {
                case 0:
                    if (pd != null && pd.isShowing()) {
                        pd.cancel();
                    }
                    update();
                    break;
                case 1:
                    if (pd != null && pd.isShowing()) {
                        pd.setMessage("请稍后..." + msg.obj);
                    }
                    break;
            }

        }
    };
}
