package com.example.zhbj.pager.impl;

import com.example.zhbj.pager.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator 政务页面
 */
public class GovPage extends BasePager {

	public GovPage(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		tvTitle.setText("人口管理");
		setMenuDisable(true);
		
		TextView text = new TextView(mActivity);
		text.setText("政务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		flPager.addView(text);
	}

}
