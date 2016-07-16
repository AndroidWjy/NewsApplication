package com.example.zhbj.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbj.R;
import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.domain.NewsData;
import com.example.zhbj.domain.NewsData.NewsMenuData;
import com.example.zhbj.pager.impl.NewsPage;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_left_menu)
	private ListView lvList;
	private ArrayList<NewsMenuData> leftMenuList;
	private leftMenuDataAdapter mAdapter;
	private int currentPos = 0;// 当前所选择的页面

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		// 利用框架自带的注解
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		// 为ListView设置侦听
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 拿到当前所选择的Item
				currentPos = position;
				// 由适配器自身调用
				mAdapter.notifyDataSetChanged();
				//设置点击侧边栏显示的页面
				setMenuPager(position);
				//关闭侧滑栏
				toggleSlidingMenu();
			}
		});
	}

	/**
	 * 拿到newsPager对象
	 * 
	 * @param position
	 */
	protected void setMenuPager(int position) {
		HomeActivity home = (HomeActivity) mActivity;
		ContentFragment fragment = home.getContentFragment();
		NewsPage pager = (NewsPage) fragment.pageList.get(1);
		pager.setMenuDetailPager(position);
	}

	/**
	 * 为侧边栏设置网络数据
	 * 
	 * @param data
	 */
	public void setMenuData(NewsData data) {
		//System.out.println("侧边栏拿到数据：" + data);
		// 拿到侧边栏数据
		leftMenuList = data.data;
		mAdapter = new leftMenuDataAdapter();
		lvList.setAdapter(mAdapter);
	}

	/**
	 * 点击关闭侧滑栏，toggle方法
	 */
	protected void toggleSlidingMenu() {
		HomeActivity home = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = home.getSlidingMenu();
		slidingMenu.toggle();
	}

	/**
	 * listView的数据匹配器
	 * 
	 * @author Administrator
	 * 
	 */
	public class leftMenuDataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return leftMenuList.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return leftMenuList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.item_left_menu, null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
			NewsMenuData data = getItem(position);
			tvMenu.setText(data.title);

			if (currentPos == position) {
				//利用enable给标签页设置颜色
				tvMenu.setEnabled(true);
			} else {
				tvMenu.setEnabled(false);
			}
			return view;
		}

	}

}
