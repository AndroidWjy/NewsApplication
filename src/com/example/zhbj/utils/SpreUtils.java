package com.example.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpreUtils {

	private static final String name = "config";

	/**
	 * 拿到标记位
	 */
	public static boolean getBoolean(Context ctx, String key, boolean defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(name,
				Context.MODE_PRIVATE);

		return sp.getBoolean(key, defValue);
	}

	/**
	 * 设置标记位
	 */
	public static void setBoolean(Context ctx, String key, boolean defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, defValue).commit();
	}

	public static String getString(Context ctx, String key, String defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(name,
				Context.MODE_PRIVATE);

		return sp.getString(key, defValue);
	}

	public static void setString(Context ctx, String key, String defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, defValue).commit();
	}
	
}
