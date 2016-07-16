package com.example.zhbj.pager.impl;

import java.util.ArrayList;

import com.example.zhbj.activity.HomeActivity;
import com.example.zhbj.domain.NewsData;
import com.example.zhbj.domain.NewsData.NewsMenuData;
import com.example.zhbj.fragment.LeftMenuFragment;
import com.example.zhbj.global.GlobalContants;
import com.example.zhbj.pager.BaseMenuDetailPager;
import com.example.zhbj.pager.BasePager;
import com.example.zhbj.pager.menu.InteractMenuDetailPager;
import com.example.zhbj.pager.menu.NewsMenuDetailPager;
import com.example.zhbj.pager.menu.PhotoMenuDetailPager;
import com.example.zhbj.pager.menu.TopicMenuDetailPager;
import com.example.zhbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * 在新闻页面有4个侧边栏，依附在这上面
 * 
 * @author Administrator 新闻中心页面
 */
public class NewsPage extends BasePager {
	// 从服务端拿到新闻信息
	private NewsData mNewsData;
	private ArrayList<BaseMenuDetailPager> mDetailPagerList;

	public NewsPage(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		super.initData();
		setMenuDisable(true);
		String cache = CacheUtils.getCache(mActivity,
				GlobalContants.CATEGORIES_URL, "");
		// 若缓存不为空
		if (!TextUtils.isEmpty(cache)) {
			// 直接去解析数据
			parseData(cache);
		}

		getDataFromServer();

	}

	/**
	 * 从服务器端获取数据，点击新闻也就会访问服务器
	 */
	public void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		// 利用XUtils发送请求
		http.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = (String) responseInfo.result;
						// System.out.println("返回结果："+result);
						// 解析数据
						parseData(result);
						// 将数据缓存起来
						CacheUtils.setCache(mActivity,
								GlobalContants.CATEGORIES_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, "网络失败！", Toast.LENGTH_SHORT)
								.show();
						System.out.println(msg);
						error.printStackTrace();
					}

				});
	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	private void parseData(String result) {
		// 通过Gson解析数据
		Gson mGson = new Gson();
		mNewsData = mGson.fromJson(result, NewsData.class);
		//System.out.println("解析之后的数据：" + mNewsData);

		HomeActivity home = (HomeActivity) mActivity;
		LeftMenuFragment leftMenuFragment = home.getLeftMenuFragment();// 拿到侧边栏对象
		if (leftMenuFragment != null) {
			// 调用对象里面的方法，设置数据
			leftMenuFragment.setMenuData(mNewsData);
		}

		mDetailPagerList = new ArrayList<BaseMenuDetailPager>();
		// 将对象添加进来，四个侧边栏子页面，而新闻详细页又有11个TabDetailPager
		mDetailPagerList.add(new NewsMenuDetailPager(mActivity, mNewsData.data
				.get(0).children));
		mDetailPagerList.add(new TopicMenuDetailPager(mActivity));
		mDetailPagerList.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mDetailPagerList.add(new InteractMenuDetailPager(mActivity));

		setMenuDetailPager(0); // 默认加载第一页
	}

	/**
	 * 设置每个侧边栏页面的详情，由侧边栏设置指定的页面
	 * 
	 * @param position
	 */
	public void setMenuDetailPager(int position) {
		// 从数组中拿到选择的对象
		BaseMenuDetailPager pager = mDetailPagerList.get(position);
		// 清空之前添加的View
		flPager.removeAllViews();
		flPager.addView(pager.mRootView);

		// 给布局设置标题头
		NewsMenuData newsMenuData = mNewsData.data.get(position);
		tvTitle.setText(newsMenuData.title);
		//判断是否为组图详情页面
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		} else {
			btnPhoto.setVisibility(View.GONE);
		}
		// 布局文件加载完毕后，初始化数据
		pager.initData();
	}

}
