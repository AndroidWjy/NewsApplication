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
 * @author Administrator 5个子页面ViewPager的共性基类，需要传入一个Activity
 */
public class BasePager {

	public Activity mActivity;
	public View mRootView;// 基类布局
	public TextView tvTitle;// 标题
	public ImageButton btnMenu;// 标题按钮
	public FrameLayout flPager;// 动态塞入的类
	public ImageButton btnPhoto; //切换的button

	/**
	 * @param activity
	 *            构造方法
	 */
	public BasePager(Activity activity) {
		mActivity = activity;
		initView();
	}

	/**
	 * 初始化布局文件 函数一执行构造方法，该方法被调用，则返回一个以填充好的对象
	 */
	public void initView() {
		//5个布局都填充一样的内容
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
	 * 初始化数据 子类都有自己的实现，父类可以调用指定的内容
	 */
	public void initData() {

	}

	/**
	 * @param bool
	 * 选择性让标题按钮显示，以及侧边栏滑出
	 */
	public void setMenuDisable(boolean bool) {
		//所要得到的Activity就是一直引入的Activity
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
	 * 点击关闭侧滑栏，toggle方法
	 */
	protected void toggleSlidingMenu(){
		HomeActivity home = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = home.getSlidingMenu();
		slidingMenu.toggle();
	}
}
