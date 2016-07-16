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
	 * ���������޸��¼��ַ�
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// �ж��¼�����
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// �ؼ������µ�ʱ���Ҫ���󲻱�����
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX = (int) ev.getRawX();
			int endY = (int) ev.getRawY();
			// �����ҵķ��ȴ������·���ʱ�������һ���
			if (Math.abs(endX - startX) > Math.abs(endY - endY)) {
				// ��ʾ�һ�
				if (endX > startX) {
					// ������һҳ����
					if (getCurrentItem() == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// �������һҳ����
					if (getCurrentItem() == getAdapter().getCount() - 1) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {
				// ���»����ɸ��ؼ�����
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		
		return super.dispatchTouchEvent(ev);
	}

}
