package com.example.zhbj.utils;

import android.content.Context;

/**
 * ���洦��
 * 
 * @author Administrator
 * 
 */
public class CacheUtils {
	//����������urlΪkey������url��MD5Ϊkey��json����ΪValue
	public static void setCache(Context ctx, String key, String value) {
		SpreUtils.setString(ctx, key, value);
	}

	public static String getCache(Context ctx, String key, String value) {
		return SpreUtils.getString(ctx, key, value);
	}
}
