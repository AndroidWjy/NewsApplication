package com.example.zhbj.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @author Administrator 5����ҳ��ViewPager�Ĺ��Ի��࣬��Ҫ����һ��Activity
 */
public class BasePager {

	public Activity mActivity;
	public View mRootView;// ���಼��
	public TextView tvTitle;// ����
	public ImageButton btnMenu;// ���ⰴť
	public FrameLayout flPager;// ��̬�������
	public ImageButton btnPhoto; //�л���button

	/**
	 * @param activity
	 *            ���췽��
	 */
	public BasePager(Activity activity) {
		mActivity = activity;
		initView();
	}

	/**
	 * ��ʼ�������ļ� ����һִ�й��췽�����÷��������ã��򷵻�һ�������õĶ���
	 */
	public void initView() {
		//5�����ֶ����һ��������
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_tltle);
		btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		flPager = (FrameLayout) mRootView.findViewById(R.id.fl_pager);
		btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);
		
		
		btnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
	}

	/**
	 * ��ʼ������ ���඼���Լ���ʵ�֣�������Ե���ָ��������
	 */
	public void initData() {

	}

	/**
	 * @param bool
	 * ѡ�����ñ��ⰴť��ʾ���Լ����������
	 */
	public void setMenuDisable(boolean bool) {
		//��Ҫ�õ���Activity����һֱ�����Activity
		HomeActivity home = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = home.getSlidingMenu();
		if (bool) {
			btnMenu.setVisibility(View.VISIBLE);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			
		} else {
			btnMenu.setVisibility(View.INVISIBLE);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	/**
	 * ����رղ໬����toggle����
	 */
	protected void toggleSlidingMenu(){
		HomeActivity home = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = home.getSlidingMenu();
		slidingMenu.toggle();
	}
}
