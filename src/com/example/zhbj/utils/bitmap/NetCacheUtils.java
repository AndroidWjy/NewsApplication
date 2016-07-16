package com.example.zhbj.utils.bitmap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * ֱ��ͨ����������ȡ
 * 
 * @author Administrator
 * 
 */
public class NetCacheUtils {
	/**
	 * ֱ��ͨ��url��ȡ��
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap downloadFromUrl(String url) {
		HttpURLConnection conn = null;
		try {
			final URL mUrl = new URL(url);
			conn = (HttpURLConnection) mUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
}
