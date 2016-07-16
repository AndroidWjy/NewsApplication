package com.example.zhbj.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.R;
import com.example.zhbj.activity.NewsDetailActivity;
import com.example.zhbj.domain.NewsData.NewsTabData;
import com.example.zhbj.domain.PhotosData.NewsPhoto;
import com.example.zhbj.domain.TabData;
import com.example.zhbj.domain.TabData.NewsList;
import com.example.zhbj.domain.TabData.topNewsList;
import com.example.zhbj.global.GlobalContants;
import com.example.zhbj.utils.CacheUtils;
import com.example.zhbj.utils.SpreUtils;
import com.example.zhbj.view.RefreshListView;
import com.example.zhbj.view.RefreshListView.onRefreshListener;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 因为在ViewPager下还有12个要显示的页面，此时应该创建12个对象，和前面类似的添加方式，在NewsMenuDetailPager下，具体显示内容
 * 
 * @author Administrator
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabData mTabData;
	// 第三个ViewPager
	private ViewPager mTabPager;
	// 从第二个json中解析出来的数据
	private TabData mData;

	// 头条新闻
	private ArrayList<topNewsList> topList;
	// 新闻列表
	private ArrayList<NewsList> newsList;

	// 头条新闻标签
	private TextView tvTop;
	private CirclePageIndicator indicatorTab;
	private RefreshListView lvTabs;
	private String mMoreUrl;
	private MyListAdapter myAdapter;
	private String mURL;
	private Handler mHandler;

	// 将url传入进来
	public TabDetailPager(Activity activity, NewsTabData tabData) {
		super(activity);
		mTabData = tabData;
		mURL = GlobalContants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tabs_news, null);
		View headerView = View.inflate(mActivity, R.layout.item_header_view,
				null);
		lvTabs = (RefreshListView) view.findViewById(R.id.lv_tabs);
		// 将henderView添加到listView中
		lvTabs.addHeaderView(headerView);
		// 头条新闻的图片展示
		mTabPager = (ViewPager) headerView.findViewById(R.id.tab_pager);
		tvTop = (TextView) headerView.findViewById(R.id.tv_top);

		// 圆点
		indicatorTab = (CirclePageIndicator) headerView
				.findViewById(R.id.indicator_tab);
		// 设置刷新方法以供回调
		lvTabs.setOnRefreshListener(new onRefreshListener() {
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onMoreLoad() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					lvTabs.onRefreshComplete(true);
				}
			}
		});
		// 设置条目的选择
		lvTabs.setOnItemClickListener(new OnItemClickListener() {
			// 参数都是在类里面实现好了的
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("所选条目：" + position);
				String ids = SpreUtils.getString(mActivity, "readNews", "");
				if (!ids.contains(newsList.get(position).id)) {
					ids = ids + newsList.get(position).id + ",";
					SpreUtils.setString(mActivity, "readNews", ids);
				}
				setViewChange(view);
				Intent intent = new Intent(mActivity, NewsDetailActivity.class);
				intent.putExtra("NewsUrl", newsList.get(position).url);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	private void setViewChange(View v) {
		TextView tv = (TextView) v.findViewById(R.id.tv_content);
		tv.setTextColor(Color.GRAY);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void initData() {
		String cache = CacheUtils.getCache(mActivity, mURL, "");
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
		getDataFromServer();
	}

	/**
	 * 获取数据
	 */
	private void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, mURL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// System.out.println("页签数据：" + result);
				parseData(result, false);
				lvTabs.onRefreshComplete(true);

				CacheUtils.setCache(mActivity, mURL, result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				System.out.println(msg);
				error.printStackTrace();
			}
		});
	}

	/**
	 * 获取更多数据
	 */
	private void getMoreDataFromServer() {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// System.out.println("页签数据：" + result);
				parseData(result, true);
				lvTabs.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				System.out.println(msg);
				error.printStackTrace();
				lvTabs.onRefreshComplete(false);
			}
		});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 * 
	 */

	protected void parseData(String result, boolean hasMoreItem) {

		Gson gson = new Gson();
		// 解析出的新闻列表信息
		mData = gson.fromJson(result, TabData.class);
		//System.out.println("页签解析数据：" + mData);
		// 解析出是否还有多的数据
		String more = mData.data.more;
		if (!TextUtils.isEmpty(more)) {
			// 获取加载的url
			mMoreUrl = GlobalContants.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		// 不加载
		if (!hasMoreItem) {
			topList = mData.data.topnews;
			newsList = mData.data.news;

			mTabPager.setAdapter(new TopPagerAdapter());

			indicatorTab.setViewPager(mTabPager);
			indicatorTab.setSnap(true);
			// 设置改变侦听
			indicatorTab.setOnPageChangeListener(new MyPageChangeListener());
			// 选择第一个页面
			indicatorTab.onPageSelected(0);
			// tvTop.setText(topList.get(0).title);
			if (newsList != null) {
				myAdapter = new MyListAdapter();
				// 给list设置适配器
				lvTabs.setAdapter(myAdapter);
			}
			/**
			 * 轮播实现，递归发送handler消息
			 */
			if (mHandler == null) {
				mHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 0) {
							int currentItem = mTabPager.getCurrentItem();
							if (currentItem < topList.size() - 1) {
								currentItem++;
							} else {
								currentItem = 0;
							}
							mTabPager.setCurrentItem(currentItem);
							// 消息里面再次发送消息
							mHandler.sendEmptyMessageDelayed(0, 3000);
						}
					}
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);
			}

		} else {
			ArrayList<NewsList> news = mData.data.news;
			newsList.addAll(news);
			myAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * @author Administrator 头条新闻页面，主要是图片，给viewPager设置数据
	 */
	class TopPagerAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopPagerAdapter() {
			// 节省资源
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return topList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			// 填满整个屏幕
			view.setScaleType(ScaleType.FIT_XY);
			// 将View添加到ViewPage里面
			utils.display(view, topList.get(position).topimage);
			//对每张图片进行点击侦听
			view.setOnTouchListener(new MyTopImageTouch());

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator 页面状态改变，设置指定标题
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
			tvTop.setText(topList.get(arg0).title);
		}

	}

	/**
	 * 对listView设置数据适配器
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyListAdapter extends BaseAdapter {
		private BitmapUtils utils;

		public MyListAdapter() {
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public NewsList getItem(int position) {
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				// 将view对象返给convertView
				convertView = View.inflate(mActivity, R.layout.item_news_list,
						null);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tvData = (TextView) convertView
						.findViewById(R.id.tv_date);
				// 由复用的View设置holder
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsList item = getItem(position);
			String ids = SpreUtils.getString(mActivity, "readNews", "");
			holder.tvContent.setText(item.title);
			holder.tvData.setText(item.pubdate);
			if (ids.contains(item.id)) {
				holder.tvContent.setTextColor(Color.GRAY);
			} else {
				holder.tvContent.setTextColor(Color.BLACK);
			}

			utils.display(holder.ivPic, item.listimage);

			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvContent;
		public TextView tvData;
	}

	/**
	 * 为每一张图片设置触摸侦听
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyTopImageTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//System.out.println("手指放下！");
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_CANCEL:
				//System.out.println("事件取消！");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				//System.out.println("手指抬起！");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:

				break;
			}
			return true;
		}
	}

}
