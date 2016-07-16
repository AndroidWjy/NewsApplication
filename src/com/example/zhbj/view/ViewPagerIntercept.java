package com.example.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerIntercept extends ViewPager {

	private int startX;
	private int startY;

	public ViewPagerIntercept(Context context) {
		super(context);
	}

	public ViewPagerIntercept(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 根据条件修改事件分发
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 判断事件类型
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 关键，放下的时候就要请求不被拦截
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();
			// 当左右的幅度大于上下幅度时，表左右滑动
			if (Math.abs(endX - startX) > Math.abs(endY - endY)) {
				// 表示右滑
				if (endX > startX) {
					// 滑到第一页拦截
					if (getCurrentItem() == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 滑倒最后一页拦截
					if (getCurrentItem() == getAdapter().getCount() - 1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {
				// 上下滑动由父控件处理
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}

}
