package com.example.zhbj.pager.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.domain.NewsData.NewsTabData;
import com.example.zhbj.pager.BaseMenuDetailPager;
import com.example.zhbj.pager.TabDetailPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
/**
 * 新闻详细页面，12个标签，是一个ViewPager
 * @author Administrator
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

	private ViewPager vpPager;
	private ArrayList<NewsTabData> newsTabData; // 拿到从数据解析出来的数据

	private ArrayList<TabDetailPager> tabPagerList; // 页签栏
	private TabPageIndicator indictor;
	private ImageButton btnNext;

	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> data) {
		super(activity);
		newsTabData = data;
	}
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_detail_pager, null);
		vpPager = (ViewPager) view.findViewById(R.id.vp_pager);
		//tab标签栏
		indictor = (TabPageIndicator) view.findViewById(R.id.indicator);
		
		btnNext = (ImageButton) view.findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 拿到当前页的索引
				int currentItem = vpPager.getCurrentItem();
				vpPager.setCurrentItem(currentItem + 1);
			}
		});

		return view;
	}

	@Override
	public void initData() {
		tabPagerList = new ArrayList<TabDetailPager>();
		// System.out.println("newsTabData是："+newsTabData);
		//添加页签的内容，根据服务器上数据
		for (int i = 0; i < newsTabData.size(); i++) {
			TabDetailPager tab = new TabDetailPager(mActivity,
					newsTabData.get(i));
			tabPagerList.add(tab);
		}
		// 给ViewPager设置标签页
		vpPager.setAdapter(new mPagerAdapter());
		//将ViewPager塞给indictor
		indictor.setViewPager(vpPager);
		// 滑动侦听
		indictor.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * @author Administrator ViewPager的数据适配器
	 */
	class mPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {

			return newsTabData.get(position).title;
		}

		@Override
		public int getCount() {

			return tabPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 这里拿到对象后，因为构造方法里面已经返回了一个跟跟布局，直接可以拿到
			container.addView(tabPagerList.get(position).mRootView);
			// 初始化数据
			tabPagerList.get(position).initData();
			return tabPagerList.get(position).mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator 给第二层的ViewPager设置滑动侦听，防止事件分发出问题
	 */
	class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			HomeActivity home = (HomeActivity) mActivity;
			SlidingMenu slidingMenu = home.getSlidingMenu();
			if (arg0 == 0) {
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else {
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}

	}

}
