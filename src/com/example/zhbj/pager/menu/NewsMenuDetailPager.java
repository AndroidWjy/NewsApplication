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
 * ������ϸҳ�棬12����ǩ����һ��ViewPager
 * @author Administrator
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

	private ViewPager vpPager;
	private ArrayList<NewsTabData> newsTabData; // �õ������ݽ�������������

	private ArrayList<TabDetailPager> tabPagerList; // ҳǩ��
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
		//tab��ǩ��
		indictor = (TabPageIndicator) view.findViewById(R.id.indicator);
		
		btnNext = (ImageButton) view.findViewById(R.id.btn_next);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �õ���ǰҳ������
				int currentItem = vpPager.getCurrentItem();
				vpPager.setCurrentItem(currentItem + 1);
			}
		});

		return view;
	}

	@Override
	public void initData() {
		tabPagerList = new ArrayList<TabDetailPager>();
		// System.out.println("newsTabData�ǣ�"+newsTabData);
		//���ҳǩ�����ݣ����ݷ�����������
		for (int i = 0; i < newsTabData.size(); i++) {
			TabDetailPager tab = new TabDetailPager(mActivity,
					newsTabData.get(i));
			tabPagerList.add(tab);
		}
		// ��ViewPager���ñ�ǩҳ
		vpPager.setAdapter(new mPagerAdapter());
		//��ViewPager����indictor
		indictor.setViewPager(vpPager);
		// ��������
		indictor.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * @author Administrator ViewPager������������
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
			// �����õ��������Ϊ���췽�������Ѿ�������һ���������֣�ֱ�ӿ����õ�
			container.addView(tabPagerList.get(position).mRootView);
			// ��ʼ������
			tabPagerList.get(position).initData();
			return tabPagerList.get(position).mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator ���ڶ����ViewPager���û�����������ֹ�¼��ַ�������
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
