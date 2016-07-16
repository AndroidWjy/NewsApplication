package com.example.zhbj.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.zhbj.R;
import com.example.zhbj.fragment.ContentFragment;
import com.example.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @author Administrator 继承侧滑栏的Activity 主页面
 * 
 */
public class HomeActivity extends SlidingFragmentActivity {

	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		// 设置侧边栏
		setBehindContentView(R.layout.activity_left_menu);

		SlidingMenu slidingMenu = getSlidingMenu();
		// 全屏触摸滑动
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置滑动预留的大小
		slidingMenu.setBehindOffset(280);
		// 设置侧滑的方向
		slidingMenu.setMode(SlidingMenu.LEFT);

		initFragment();
	}

	/**
	 * 初始化Fragment
	 */
	public void initFragment() {
		// 拿到Fragment管理器，V4下的
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开启事务，处理Fragment属性

		transaction.replace(R.id.fl_content, new ContentFragment(),
				FRAGMENT_CONTENT);// 用fragment替换framelayout
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);

		transaction.commit();// 提交事务
	}

	/**
	 * 通过Tag寻找侧边栏对象
	 * 
	 * @return
	 */
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm
				.findFragmentByTag(FRAGMENT_LEFT_MENU);
		return leftMenuFragment;
	}

	/**
	 * 通过Tag寻找主页面对象
	 * 
	 * @return
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(FRAGMENT_CONTENT);
		return fragment;
	}
}
