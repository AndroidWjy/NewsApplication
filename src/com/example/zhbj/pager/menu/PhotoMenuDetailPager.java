package com.example.zhbj.pager.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.R;
import com.example.zhbj.domain.PhotosData;
import com.example.zhbj.domain.PhotosData.NewsPhoto;
import com.example.zhbj.global.GlobalContants;
import com.example.zhbj.pager.BaseMenuDetailPager;
import com.example.zhbj.utils.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ListView lvPhoto;
	private GridView gvPhoto;
	private String photoUrl = GlobalContants.PHOTO_URL;
	// 装载数据
	private ArrayList<NewsPhoto> photoList;
	private PhotosData mPhotoData;
	private ImageButton mBtnPhoto;
	private boolean isSelectedList = true;

	public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
		super(activity);
		mBtnPhoto = btnPhoto;
	}

	@Override
	public View initView() {
		View mView = View.inflate(mActivity, R.layout.menu_photo_pager, null);
		lvPhoto = (ListView) mView.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) mView.findViewById(R.id.gv_photo);
		// lvPhoto.setAdapter()
		return mView;
	}

	@Override
	public void initData() {
		getDataFromServer();
	}

	/**
	 * 访问服务器
	 */
	private void getDataFromServer() {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, photoUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				// 解析数据
				parseData(result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "网络失败！", Toast.LENGTH_SHORT).show();
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
		Gson gson = new Gson();
		mPhotoData = gson.fromJson(result, PhotosData.class);
		// System.out.println("解析到照片数据：" + mPhotoData);
		photoList = mPhotoData.data.news;
		MyAdapter adapter = new MyAdapter();

		lvPhoto.setAdapter(adapter);
		gvPhoto.setAdapter(adapter);

		mBtnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSelectedList) {
					isSelectedList = false;
					lvPhoto.setVisibility(View.VISIBLE);
					gvPhoto.setVisibility(View.GONE);
				} else {
					isSelectedList = true;
					gvPhoto.setVisibility(View.VISIBLE);
					lvPhoto.setVisibility(View.GONE);
				}
			}
		});

	}

	public class MyAdapter extends BaseAdapter {
		private MyBitmapUtils utils;

		public MyAdapter() {
			utils = new MyBitmapUtils(mActivity);
		}

		@Override
		public int getCount() {
			return photoList.size();
		}

		@Override
		public NewsPhoto getItem(int position) {
			return photoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_photo_item,
						null);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title_photo);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic_photo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(getItem(position).title);
			utils.display(holder.ivPic, getItem(position).listimage);

			return convertView;
		}
	}

	private class ViewHolder {
		private ImageView ivPic;
		private TextView tvTitle;
	}

}
