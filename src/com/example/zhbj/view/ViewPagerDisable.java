package com.example.zhbj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerDisable extends ViewPager {

	public ViewPagerDisable(Context context) {
		super(context);
	}

	public ViewPagerDisable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ViewPage之所以有滑动效果是因为底层实现了onTouchEvent事件，而我们重写他们的事件方法即可
	 */
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
	
	/**
	 * 嵌套了两个ViewPager，涉及到事件分发，默认是将事件在父控件里面执行，所以重写拦截事件的方法，让子控件处理
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}
}
