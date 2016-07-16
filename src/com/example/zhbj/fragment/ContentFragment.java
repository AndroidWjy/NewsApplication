package com.example.zhbj.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zhbj.R;
import com.example.zhbj.pager.BasePager;
import com.example.zhbj.pager.impl.GovPage;
import com.example.zhbj.pager.impl.HomePage;
import com.example.zhbj.pager.impl.NewsPage;
import com.example.zhbj.pager.impl.SettingPage;
import com.example.zhbj.pager.impl.SmartPage;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页面，包含5个子页面
 * @author Administrator
 * 
 */
public class ContentFragment extends BaseFragment {

	private RadioGroup rgRoot;// 底栏选择按钮
	private ViewPager vpContent;// 显示页面ViewPager
	public List<BasePager> pageList;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		rgRoot = (RadioGroup) view.findViewById(R.id.rg_root);
		vpContent = (ViewPager) view.findViewById(R.id.vp_content);
		return view;
	}

	@Override
	public void initData() {
		// 去父类中执行方法
		super.initData();
		rgRoot.check(R.id.btn_home);
		// 创建一个集合
		pageList = new ArrayList<BasePager>();
		// 将实例化的各个子页面添加到集合中， 父类已经有mActivity可以直接使用
		pageList.add(new HomePage(mActivity));
		pageList.add(new NewsPage(mActivity));
		pageList.add(new SmartPage(mActivity));
		pageList.add(new GovPage(mActivity));
		pageList.add(new SettingPage(mActivity));
		//数据适配
		vpContent.setAdapter(new ContentPagerAdapter());
		//按钮选择侦听
		rgRoot.setOnCheckedChangeListener(new MyCheckedChangeListener());
		//页面选择侦听
		vpContent.setOnPageChangeListener(new ContentPageChangeListener());
		//手动添加首页的初始化数据
		pageList.get(0).initData();
	}

	/**
	 * @author Administrator ViewPager数据适配器
	 */
	class ContentPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pageList.get(position);
			container.addView(pager.mRootView);
			//pager.initData(); 由于需求改变不能让它同时加载初始化数据
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator RadioGroup选择按钮的侦听
	 * 
	 */
	class MyCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.btn_home:
				//对ViewPager设置选中当前页的内容
				vpContent.setCurrentItem(0, false);
				break;
			case R.id.btn_news:
				vpContent.setCurrentItem(1, false);
				break;
			case R.id.btn_smart:
				vpContent.setCurrentItem(2, false);
				break;
			case R.id.btn_gov:
				vpContent.setCurrentItem(3, false);
				break;
			case R.id.btn_set:
				vpContent.setCurrentItem(4, false);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * @author Administrator
	 * 页面选择侦听
	 */
	class ContentPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		//选中页面
		@Override
		public void onPageSelected(int arg0) {
			//选中那个页面，由该页面自动加载
			pageList.get(arg0).initData();
		}
		
	}
}
