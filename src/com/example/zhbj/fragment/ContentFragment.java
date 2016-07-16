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
 * ��ҳ�棬����5����ҳ��
 * @author Administrator
 * 
 */
public class ContentFragment extends BaseFragment {

	private RadioGroup rgRoot;// ����ѡ��ť
	private ViewPager vpContent;// ��ʾҳ��ViewPager
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
		// ȥ������ִ�з���
		super.initData();
		rgRoot.check(R.id.btn_home);
		// ����һ������
		pageList = new ArrayList<BasePager>();
		// ��ʵ�����ĸ�����ҳ����ӵ������У� �����Ѿ���mActivity����ֱ��ʹ��
		pageList.add(new HomePage(mActivity));
		pageList.add(new NewsPage(mActivity));
		pageList.add(new SmartPage(mActivity));
		pageList.add(new GovPage(mActivity));
		pageList.add(new SettingPage(mActivity));
		//��������
		vpContent.setAdapter(new ContentPagerAdapter());
		//��ťѡ������
		rgRoot.setOnCheckedChangeListener(new MyCheckedChangeListener());
		//ҳ��ѡ������
		vpContent.setOnPageChangeListener(new ContentPageChangeListener());
		//�ֶ������ҳ�ĳ�ʼ������
		pageList.get(0).initData();
	}

	/**
	 * @author Administrator ViewPager����������
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
			//pager.initData(); ��������ı䲻������ͬʱ���س�ʼ������
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator RadioGroupѡ��ť������
	 * 
	 */
	class MyCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.btn_home:
				//��ViewPager����ѡ�е�ǰҳ������
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
	 * ҳ��ѡ������
	 */
	class ContentPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		//ѡ��ҳ��
		@Override
		public void onPageSelected(int arg0) {
			//ѡ���Ǹ�ҳ�棬�ɸ�ҳ���Զ�����
			pageList.get(arg0).initData();
		}
		
	}
}
