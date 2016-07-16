package com.example.zhbj.utils;

import android.content.Context;

/**
 * 缓存处理
 * 
 * @author Administrator
 * 
 */
public class CacheUtils {
	//缓存以它的url为key或者以url的MD5为key，json数据为Value
	public static void setCache(Context ctx, String key, String value) {
		SpreUtils.setString(ctx, key, value);
	}

	public static String getCache(Context ctx, String key, String value) {
		return SpreUtils.getString(ctx, key, value);
	}
}
