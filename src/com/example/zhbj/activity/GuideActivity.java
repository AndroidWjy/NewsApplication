package com.example.zhbj.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.example.zhbj.R;
import com.example.zhbj.utils.SpreUtils;

public class GuideActivity extends Activity {

	private ViewPager vpGuide;
	private int[] imageId = new int[] { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };
	private ArrayList<ImageView> ImageList;
	private LinearLayout llPoint;
	private Button btnStart;
	private int pointWidth;
	private View redView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		llPoint = (LinearLayout) findViewById(R.id.ll_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		redView = findViewById(R.id.view_red);
		initViews();
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		// ����������
		vpGuide.setAdapter(new GuidePageAdapter());
		// ���viewPage�Ļ�������
		vpGuide.setOnPageChangeListener(new GuidePageChangeListener());
		// ��ȡ��ͼ����������layoutִ����ɵ�����
		llPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// ��layout������ɷ�������
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						// ִ�гɹ��Ϳ��Խ����Ƴ�
						llPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// ��������
						pointWidth = llPoint.getChildAt(1).getLeft()
								- llPoint.getChildAt(0).getLeft();
						System.out.println("�������룺" + pointWidth);
					}
				});
		// Ϊ��ť��������
		
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SpreUtils.setBoolean(GuideActivity.this, "is_guide_use", true);
				finish();
				startActivity(new Intent(GuideActivity.this, HomeActivity.class));
			}
		});
	}

	/**
	 * ��ʼ������
	 */
	public void initViews() {
		ImageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageId.length; i++) {
			ImageView image = new ImageView(this);
			image.setBackgroundResource(imageId[i]);
			ImageList.add(image);
			// ����һ��view����
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			// ����view���󸸿ؼ��Ĳ���
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					10, 10);
			if (i > 0) {
				params.leftMargin = 10;
			}
			// ����point�Ĳ���
			point.setLayoutParams(params);
			llPoint.addView(point);
		}

	}

	/**
	 * @author Administrator ViewPager����������
	 * 
	 */
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return ImageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// �������������ͼ����
			container.addView(ImageList.get(position));
			return ImageList.get(position);

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// ��������ɾ�������ͼ����
			container.removeView((View) object);
		}
	}

	/**
	 * @author Administrator ��������
	 */
	class GuidePageChangeListener implements OnPageChangeListener {
		// ĳ��ҳ�汻ѡ��
		@Override
		public void onPageSelected(int position) {
			if (position == ImageList.size() - 1) {
				btnStart.setVisibility(View.VISIBLE);
			} else {
				btnStart.setVisibility(View.GONE);
			}
		}

		// �����¼�
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// ��㻬���ľ���Ӧ��Ϊ�ܾ���ٷֱȺ͵��������ڼ�ҳ
			int len = (int) (pointWidth * positionOffset + position
					* pointWidth);
			RelativeLayout.LayoutParams params = (LayoutParams) redView
					.getLayoutParams();
			params.leftMargin = len;
			redView.setLayoutParams(params);
		}

		// ����״̬�ı�
		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}
}
