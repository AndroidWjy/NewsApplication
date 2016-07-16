package com.example.zhbj.pager;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;//根布局

	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}
	
	/**
	 * 初始化布局
	 * 子类实现
	 */
	public abstract View initView();
	
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
}
