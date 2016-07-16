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
 * @author Administrator �̳в໬����Activity ��ҳ��
 * 
 */
public class HomeActivity extends SlidingFragmentActivity {

	private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	private static final String FRAGMENT_CONTENT = "fragment_content";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		// ���ò����
		setBehindContentView(R.layout.activity_left_menu);

		SlidingMenu slidingMenu = getSlidingMenu();
		// ȫ����������
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// ���û���Ԥ���Ĵ�С
		slidingMenu.setBehindOffset(280);
		// ���ò໬�ķ���
		slidingMenu.setMode(SlidingMenu.LEFT);

		initFragment();
	}

	/**
	 * ��ʼ��Fragment
	 */
	public void initFragment() {
		// �õ�Fragment��������V4�µ�
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// �������񣬴���Fragment����

		transaction.replace(R.id.fl_content, new ContentFragment(),
				FRAGMENT_CONTENT);// ��fragment�滻framelayout
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				FRAGMENT_LEFT_MENU);

		transaction.commit();// �ύ����
	}

	/**
	 * ͨ��TagѰ�Ҳ��������
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
	 * ͨ��TagѰ����ҳ�����
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
