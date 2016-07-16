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
 * ������ҳ����4���������������������
 * 
 * @author Administrator ��������ҳ��
 */
public class NewsPage extends BasePager {
	// �ӷ�����õ�������Ϣ
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
		// �����治Ϊ��
		if (!TextUtils.isEmpty(cache)) {
			// ֱ��ȥ��������
			parseData(cache);
		}

		getDataFromServer();

	}

	/**
	 * �ӷ������˻�ȡ���ݣ��������Ҳ�ͻ���ʷ�����
	 */
	public void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		// ����XUtils��������
		http.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = (String) responseInfo.result;
						// System.out.println("���ؽ����"+result);
						// ��������
						parseData(result);
						// �����ݻ�������
						CacheUtils.setCache(mActivity,
								GlobalContants.CATEGORIES_URL, result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, "����ʧ�ܣ�", Toast.LENGTH_SHORT)
								.show();
						System.out.println(msg);
						error.printStackTrace();
					}

				});
	}

	/**
	 * ��������
	 * 
	 * @param result
	 */
	private void parseData(String result) {
		// ͨ��Gson��������
		Gson mGson = new Gson();
		mNewsData = mGson.fromJson(result, NewsData.class);
		//System.out.println("����֮������ݣ�" + mNewsData);

		HomeActivity home = (HomeActivity) mActivity;
		LeftMenuFragment leftMenuFragment = home.getLeftMenuFragment();// �õ����������
		if (leftMenuFragment != null) {
			// ���ö�������ķ�������������
			leftMenuFragment.setMenuData(mNewsData);
		}

		mDetailPagerList = new ArrayList<BaseMenuDetailPager>();
		// ��������ӽ������ĸ��������ҳ�棬��������ϸҳ����11��TabDetailPager
		mDetailPagerList.add(new NewsMenuDetailPager(mActivity, mNewsData.data
				.get(0).children));
		mDetailPagerList.add(new TopicMenuDetailPager(mActivity));
		mDetailPagerList.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mDetailPagerList.add(new InteractMenuDetailPager(mActivity));

		setMenuDetailPager(0); // Ĭ�ϼ��ص�һҳ
	}

	/**
	 * ����ÿ�������ҳ������飬�ɲ��������ָ����ҳ��
	 * 
	 * @param position
	 */
	public void setMenuDetailPager(int position) {
		// ���������õ�ѡ��Ķ���
		BaseMenuDetailPager pager = mDetailPagerList.get(position);
		// ���֮ǰ��ӵ�View
		flPager.removeAllViews();
		flPager.addView(pager.mRootView);

		// ���������ñ���ͷ
		NewsMenuData newsMenuData = mNewsData.data.get(position);
		tvTitle.setText(newsMenuData.title);
		//�ж��Ƿ�Ϊ��ͼ����ҳ��
		if (pager instanceof PhotoMenuDetailPager) {
			btnPhoto.setVisibility(View.VISIBLE);
		} else {
			btnPhoto.setVisibility(View.GONE);
		}
		// �����ļ�������Ϻ󣬳�ʼ������
		pager.initData();
	}

}
