package com.hr.ui.utils.netutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hr.ui.utils.datautils.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownImg {

	HttpURLConnection httpconn = null;

	public Bitmap GetImg(String path) {

		InputStream inputS = null;
		try {
			URL url = new URL(path); // 下载图片路径ַ
			httpconn = (HttpURLConnection) url.openConnection(); // 打开HttpURLConnection链接
			inputS = httpconn.getInputStream();
			BufferedInputStream buff = new BufferedInputStream(inputS);
			Bitmap bt = BitmapFactory.decodeStream(buff);
			return bt;
		} catch (IOException e) {
			// System.out.println("img-------error----" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String DownLoadFile(String fileurl, String path) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File file = null;
		String fileName = "";
		String imgPath = "";
		try {

			fileName = fileurl.substring(fileurl.lastIndexOf("/") + 1);
			imgPath = FileUtil.getRootDir() + path + "/" + fileName;
			if (FileUtil.isFileExist(imgPath)) {
				return imgPath;
			}
			URL url = new URL(fileurl);
			HttpURLConnection httpconnCon = (HttpURLConnection) url
					.openConnection();
			inputStream = httpconnCon.getInputStream();
			FileUtil.createDirectory(FileUtil.getRootDir() + path);
			file = FileUtil.createFile(imgPath);
			outputStream = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int length = 0;
			int tol = 0;
			int totalSize = httpconnCon.getContentLength();
			while ((length = inputStream.read(buffer)) != -1) {
				tol += length;
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return imgPath;
	}
}
