package com.example.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public Activity mActivity;// 零件所依附的Activity

	// 刚刚创建
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
	}

	// 绘制界面处理布局文件
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 传递视图
		return initView();
	}

	// Activity里面的创建方法执行完成
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 初始化数据
		initData();
	}

	/**
	 * 由子类实现，初始化自身布局
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * 初始化数据，可实现可不实现
	 */
	public void initData() {

	};

}
