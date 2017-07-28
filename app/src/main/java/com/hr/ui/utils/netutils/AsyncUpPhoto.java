package com.hr.ui.utils.netutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.fragment.MeFragment;
import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.jivesoftware.smack.util.Base64;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 上传简历头像
 *
 * @author 800hr:xuebaohua
 */
public class AsyncUpPhoto {
    private Context context;
    private String filepath;
    private Bitmap bitmap = null;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            Toast.makeText(context,
                                    context.getString(R.string.upsuccess),
                                    Toast.LENGTH_SHORT).show();
//                            MeFragment.meFragment.setDrawableToUI();
                            MeFragment.meFragment.execute();
                            MyResumeFragment.myResumeFragment.getData();
                            break;
                        case -1:// 文件不存在
                            Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
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
        }

        ;

    };

    public AsyncUpPhoto(Context context, String filepath) {
        this.context = context;
        this.filepath = filepath;
    }

    public void execute() {
        File file = new File(filepath);
        if (file == null || !file.exists()) {
            Toast.makeText(context, context.getString(R.string.nofile), Toast.LENGTH_SHORT).show();
            return;// 文件不存在
        } else {
            bitmap = BitmapFactory.decodeFile(filepath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] bt = stream.toByteArray();
            String bt1 = Base64.encodeBytes(bt);
            String en = URLEncoder.encode(bt1);
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.uploadphoto");
            requestParams.put("photo_content", en);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        }
    }
}