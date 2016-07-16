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
	 * ViewPage֮�����л���Ч������Ϊ�ײ�ʵ����onTouchEvent�¼�����������д���ǵ��¼���������
	 */
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
	
	/**
	 * Ƕ��������ViewPager���漰���¼��ַ���Ĭ���ǽ��¼��ڸ��ؼ�����ִ�У�������д�����¼��ķ��������ӿؼ�����
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}
}
