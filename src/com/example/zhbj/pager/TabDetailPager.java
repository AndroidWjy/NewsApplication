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
 * ��Ϊ��ViewPager�»���12��Ҫ��ʾ��ҳ�棬��ʱӦ�ô���12�����󣬺�ǰ�����Ƶ���ӷ�ʽ����NewsMenuDetailPager�£�������ʾ����
 * 
 * @author Administrator
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabData mTabData;
	// ������ViewPager
	private ViewPager mTabPager;
	// �ӵڶ���json�н�������������
	private TabData mData;

	// ͷ������
	private ArrayList<topNewsList> topList;
	// �����б�
	private ArrayList<NewsList> newsList;

	// ͷ�����ű�ǩ
	private TextView tvTop;
	private CirclePageIndicator indicatorTab;
	private RefreshListView lvTabs;
	private String mMoreUrl;
	private MyListAdapter myAdapter;
	private String mURL;
	private Handler mHandler;

	// ��url�������
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
		// ��henderView��ӵ�listView��
		lvTabs.addHeaderView(headerView);
		// ͷ�����ŵ�ͼƬչʾ
		mTabPager = (ViewPager) headerView.findViewById(R.id.tab_pager);
		tvTop = (TextView) headerView.findViewById(R.id.tv_top);

		// Բ��
		indicatorTab = (CirclePageIndicator) headerView
				.findViewById(R.id.indicator_tab);
		// ����ˢ�·����Թ��ص�
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
		// ������Ŀ��ѡ��
		lvTabs.setOnItemClickListener(new OnItemClickListener() {
			// ����������������ʵ�ֺ��˵�
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("��ѡ��Ŀ��" + position);
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
	 * ��ʼ������
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
	 * ��ȡ����
	 */
	private void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, mURL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// System.out.println("ҳǩ���ݣ�" + result);
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
	 * ��ȡ��������
	 */
	private void getMoreDataFromServer() {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// System.out.println("ҳǩ���ݣ�" + result);
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
	 * ��������
	 * 
	 * @param result
	 * 
	 */

	protected void parseData(String result, boolean hasMoreItem) {

		Gson gson = new Gson();
		// �������������б���Ϣ
		mData = gson.fromJson(result, TabData.class);
		//System.out.println("ҳǩ�������ݣ�" + mData);
		// �������Ƿ��ж������
		String more = mData.data.more;
		if (!TextUtils.isEmpty(more)) {
			// ��ȡ���ص�url
			mMoreUrl = GlobalContants.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		// ������
		if (!hasMoreItem) {
			topList = mData.data.topnews;
			newsList = mData.data.news;

			mTabPager.setAdapter(new TopPagerAdapter());

			indicatorTab.setViewPager(mTabPager);
			indicatorTab.setSnap(true);
			// ���øı�����
			indicatorTab.setOnPageChangeListener(new MyPageChangeListener());
			// ѡ���һ��ҳ��
			indicatorTab.onPageSelected(0);
			// tvTop.setText(topList.get(0).title);
			if (newsList != null) {
				myAdapter = new MyListAdapter();
				// ��list����������
				lvTabs.setAdapter(myAdapter);
			}
			/**
			 * �ֲ�ʵ�֣��ݹ鷢��handler��Ϣ
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
							// ��Ϣ�����ٴη�����Ϣ
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
	 * @author Administrator ͷ������ҳ�棬��Ҫ��ͼƬ����viewPager��������
	 */
	class TopPagerAdapter extends PagerAdapter {

		private BitmapUtils utils;

		public TopPagerAdapter() {
			// ��ʡ��Դ
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
			// ����������Ļ
			view.setScaleType(ScaleType.FIT_XY);
			// ��View��ӵ�ViewPage����
			utils.display(view, topList.get(position).topimage);
			//��ÿ��ͼƬ���е������
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
	 * @author Administrator ҳ��״̬�ı䣬����ָ������
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
	 * ��listView��������������
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
				// ��view���󷵸�convertView
				convertView = View.inflate(mActivity, R.layout.item_news_list,
						null);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tvData = (TextView) convertView
						.findViewById(R.id.tv_date);
				// �ɸ��õ�View����holder
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
	 * Ϊÿһ��ͼƬ���ô�������
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyTopImageTouch implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//System.out.println("��ָ���£�");
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_CANCEL:
				//System.out.println("�¼�ȡ����");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			case MotionEvent.ACTION_UP:
				//System.out.println("��ָ̧��");
				mHandler.sendEmptyMessageDelayed(0, 3000);
				break;
			default:

				break;
			}
			return true;
		}
	}

}
