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
 * 图片压缩方法
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
	 * 显示图片方法
	 * 
	 * @param imageView
	 * @param url
	 */
	public void display(ImageView imageView, String url) {
		imageView.setImageResource(R.drawable.news_pic_default);

		bindBitmap(url, imageView, 0, 0);
	}

	/**
	 * 异步接口使用
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
			System.out.println("直接从有内存的图片中获取------");
			return;
		}
		new MyBitmapTask().execute(imageView, url, reqWidth, reqHeight);
	}

	/**
	 * 同步接口
	 * 
	 * @param url
	 * @param reqWidth
	 * @param height
	 * @return
	 */
	public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
		// 从内存中获取图片
		Bitmap bitmap = mMemCache.getBitmapFromMemCache(MD5Utils.encoding(url));
		//System.out.println("url编码后" + MD5Utils.encoding(url));
		if (bitmap != null) {
			System.out.println("从内存中获取图片----");
			return bitmap;
		}
		// 从磁盘中直接获取
		bitmap = mDiskCache.getBitmapFromDiskCache(url, reqWidth, reqHeight);
		if (bitmap != null) {
			System.out.println("从磁盘中获取图片----");
			return bitmap;
		}
		// 磁盘中没有，从网络中拖取然后从磁盘中读出
		bitmap = mDiskCache.setBitmapFromHttpToDisk(url, reqWidth, reqHeight);

		if (bitmap != null) {
			System.out.println("先从网络中下载到磁盘，再从磁盘读----");
			return bitmap;
		}

		if (bitmap == null) {
			bitmap = mNetCache.downloadFromUrl(url);
			System.out.println("直接通过流获取------");
		}

		return bitmap;
	}

	/**
	 * 异步开启任务下载
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyBitmapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView imageView;
		private String url;
		/**
		 * 在线程池中执行
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
