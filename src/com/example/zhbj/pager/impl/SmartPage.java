package com.example.zhbj.pager.impl;

import com.example.zhbj.pager.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator
 *�ǻ۷���
 */
public class SmartPage extends BasePager {

	public SmartPage(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		super.initData();
		tvTitle.setText("����");
		setMenuDisable(true);
		TextView text = new TextView(mActivity);
		text.setText("�ǻ۷���");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		flPager.addView(text);
	}

}
