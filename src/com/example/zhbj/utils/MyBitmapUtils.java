package com.example.zhbj.utils;

import com.example.zhbj.R;
import com.example.zhbj.utils.bitmap.DiskMemCache;
import com.example.zhbj.utils.bitmap.MemoryCache;
import com.example.zhbj.utils.bitmap.NetCacheUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * ͼƬѹ������
 * 
 * @author Administrator
 * 
 */
public class MyBitmapUtils {

	private NetCacheUtils mNetCache;
	private MemoryCache mMemCache;
	private DiskMemCache mDiskCache;

	public MyBitmapUtils(Context ctx) {
		mNetCache = new NetCacheUtils();
		mMemCache = new MemoryCache();
		mDiskCache = new DiskMemCache(ctx, mMemCache);
	}

	/**
	 * ��ʾͼƬ����
	 * 
	 * @param imageView
	 * @param url
	 */
	public void display(ImageView imageView, String url) {
		imageView.setImageResource(R.drawable.news_pic_default);

		bindBitmap(url, imageView, 0, 0);
	}

	/**
	 * �첽�ӿ�ʹ��
	 * 
	 * @param url
	 * @param reqWidth
	 * @param reqHeight
	 */
	public void bindBitmap(final String url, final ImageView imageView,
			int reqWidth, int reqHeight) {
		Bitmap bitmap = mMemCache.getBitmapFromMemCache(MD5Utils.encoding(url));
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("ֱ�Ӵ����ڴ��ͼƬ�л�ȡ------");
			return;
		}
		new MyBitmapTask().execute(imageView, url, reqWidth, reqHeight);
	}

	/**
	 * ͬ���ӿ�
	 * 
	 * @param url
	 * @param reqWidth
	 * @param height
	 * @return
	 */
	public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
		// ���ڴ��л�ȡͼƬ
		Bitmap bitmap = mMemCache.getBitmapFromMemCache(MD5Utils.encoding(url));
		//System.out.println("url�����" + MD5Utils.encoding(url));
		if (bitmap != null) {
			System.out.println("���ڴ��л�ȡͼƬ----");
			return bitmap;
		}
		// �Ӵ�����ֱ�ӻ�ȡ
		bitmap = mDiskCache.getBitmapFromDiskCache(url, reqWidth, reqHeight);
		if (bitmap != null) {
			System.out.println("�Ӵ����л�ȡͼƬ----");
			return bitmap;
		}
		// ������û�У�����������ȡȻ��Ӵ����ж���
		bitmap = mDiskCache.setBitmapFromHttpToDisk(url, reqWidth, reqHeight);

		if (bitmap != null) {
			System.out.println("�ȴ����������ص����̣��ٴӴ��̶�----");
			return bitmap;
		}

		if (bitmap == null) {
			bitmap = mNetCache.downloadFromUrl(url);
			System.out.println("ֱ��ͨ������ȡ------");
		}

		return bitmap;
	}

	/**
	 * �첽������������
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyBitmapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView imageView;
		private String url;
		/**
		 * ���̳߳���ִ��
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			imageView = (ImageView) params[0];
			url = (String) params[1];
			int reqWidth = (Integer) params[2];
			int reqHeight = (Integer) params[3];
			imageView.setTag(url);

			return loadBitmap(url, reqWidth, reqHeight);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				String bindUrl = (String) imageView.getTag();
				if (bindUrl.equals(url)) {
					imageView.setImageBitmap(result);
				}
			}
		}
	}
}
