package com.example.zhbj.pager.impl;

import com.example.zhbj.pager.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator ��ҳ��
 */
public class HomePage extends BasePager {

	public HomePage(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tvTitle.setText("�ǻ۱���");
		setMenuDisable(false);
		TextView text = new TextView(mActivity);
		text.setText("��ҳ");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		flPager.addView(text);
	}

}
