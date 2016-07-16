package com.example.zhbj.pager.impl;

import com.example.zhbj.pager.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator
 *智慧服务
 */
public class SmartPage extends BasePager {

	public SmartPage(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		super.initData();
		tvTitle.setText("生活");
		setMenuDisable(true);
		TextView text = new TextView(mActivity);
		text.setText("智慧服务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		
		flPager.addView(text);
	}

}
