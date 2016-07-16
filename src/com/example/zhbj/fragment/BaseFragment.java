package com.example.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public Activity mActivity;// �����������Activity

	// �ոմ���
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
	}

	// ���ƽ��洦�����ļ�
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ������ͼ
		return initView();
	}

	// Activity����Ĵ�������ִ�����
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// ��ʼ������
		initData();
	}

	/**
	 * ������ʵ�֣���ʼ��������
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * ��ʼ�����ݣ���ʵ�ֿɲ�ʵ��
	 */
	public void initData() {

	};

}
