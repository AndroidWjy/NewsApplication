package com.example.zhbj.pager.impl;

import com.example.zhbj.pager.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator 主页面
 */
public class HomePage extends BasePager {

	public HomePage(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tvTitle.setText("智慧北京");
		setMenuDisable(false);
		TextView text = new TextView(mActivity);
		text.setText("首页");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		flPager.addView(text);
	}

}
