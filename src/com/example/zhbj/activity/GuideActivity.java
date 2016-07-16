package com.example.zhbj.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.zhbj.R;
import com.example.zhbj.utils.SpreUtils;

public class GuideActivity extends Activity {

	private ViewPager vpGuide;
	private int[] imageId = new int[] { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };
	private ArrayList<ImageView> ImageList;
	private LinearLayout llPoint;
	private Button btnStart;
	private int pointWidth;
	private View redView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		llPoint = (LinearLayout) findViewById(R.id.ll_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		redView = findViewById(R.id.view_red);
		initViews();
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		// 数据适配器
		vpGuide.setAdapter(new GuidePageAdapter());
		// 添加viewPage的滑动侦听
		vpGuide.setOnPageChangeListener(new GuidePageChangeListener());
		// 获取视图树，并设置layout执行完成的侦听
		llPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// 当layout绘制完成方法调用
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						// 执行成功就可以将其移除
						llPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// 滑动距离
						pointWidth = llPoint.getChildAt(1).getLeft()
								- llPoint.getChildAt(0).getLeft();
						System.out.println("滑动距离：" + pointWidth);
					}
				});
		// 为按钮设置侦听
		
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpreUtils.setBoolean(GuideActivity.this, "is_guide_use", true);
				finish();
				startActivity(new Intent(GuideActivity.this, HomeActivity.class));
			}
		});
	}

	/**
	 * 初始化界面
	 */
	public void initViews() {
		ImageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageId.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(imageId[i]);
			ImageList.add(image);
			// 创建一个view对象
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			// 设置view对象父控件的参数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);
			if (i > 0) {
				params.leftMargin = 10;
			}
			// 设置point的参数
			point.setLayoutParams(params);
			llPoint.addView(point);
		}

	}

	/**
	 * @author Administrator ViewPager数据适配器
	 * 
	 */
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 向容器内添加视图对象
			container.addView(ImageList.get(position));
			return ImageList.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 当滑动完成就销毁视图对象
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator 滑动侦听
	 */
	class GuidePageChangeListener implements OnPageChangeListener {
		// 某个页面被选中
		@Override
		public void onPageSelected(int position) {
			if (position == ImageList.size() - 1) {
				btnStart.setVisibility(View.VISIBLE);
			} else {
				btnStart.setVisibility(View.GONE);
			}
		}

		// 滑动事件
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// 红点滑动的距离应该为总距离百分比和到滑动到第几页
			int len = (int) (pointWidth * positionOffset + position
					* pointWidth);
			RelativeLayout.LayoutParams params = (LayoutParams) redView
					.getLayoutParams();
			params.leftMargin = len;
			redView.setLayoutParams(params);
		}

		// 滑动状态改变
		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
}
