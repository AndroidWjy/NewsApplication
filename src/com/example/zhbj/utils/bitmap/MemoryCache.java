package com.example.zhbj.utils.bitmap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache {
	private LruCache<String, Bitmap> mLruCache;

	public MemoryCache() {
		init();
	}

	private void init() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 8;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			/**
			 * 覆写此方法
			 */
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//高度乘以像素点
				return value.getRowBytes() * value.getHeight() / 1024;
			}
		};
	}

	/**
	 * 将图片设置到内存中
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void setBitmapToMemCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 将图片从内存中取出
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return mLruCache.get(key);
	}
}
