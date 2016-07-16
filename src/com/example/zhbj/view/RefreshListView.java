package com.example.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbj.R;

/**
 * 下来刷新的界面
 * 
 * @author Administrator
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {

	private int startY = -1;
	private View view;
	private int mViewHeight;
	private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
	private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
	private static final int STATE_REFRESHING = 2;// 正在刷新
	private ImageView ivArr;
	private ProgressBar pb;
	private TextView tvHeader;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private int mStatus = STATE_PULL_REFRESH;
	private TextView tvPubData;
	onRefreshListener mRefreshListener;
	private View footerView;
	private int mFooterHeight;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		initFooter();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initFooter();
	}

	public RefreshListView(Context context) {
		super(context);
		initView();
		initFooter();
	}

	/**
	 * 加载头布局
	 */
	public void initView() {
		// 下拉刷新的对象
		view = View.inflate(getContext(), R.layout.refresh_view, null);
		this.addHeaderView(view);
		ivArr = (ImageView) view.findViewById(R.id.iv_arr);
		pb = (ProgressBar) view.findViewById(R.id.pb_progress);
		tvHeader = (TextView) view.findViewById(R.id.tv_header);
		tvPubData = (TextView) view.findViewById(R.id.pubdata);
		// 要拿到高度先要测量
		view.measure(0, 0);
		mViewHeight = view.getMeasuredHeight();
		view.setPadding(0, -mViewHeight, 0, 0);

		initAnimation();
		tvPubData.setText("最后刷新时间：" + getCurrentTime());
	}

	/**
	 * 设置加载脚布局
	 */
	public void initFooter() {
		footerView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		this.addFooterView(footerView);
		footerView.measure(0, 0);
		mFooterHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -mFooterHeight, 0, 0);// 隐藏
		// 设置滑动侦听
		this.setOnScrollListener(this);
	}

	/**
	 * 接口供调用
	 * 
	 * @author Administrator
	 * 
	 */
	public interface onRefreshListener {
		// 下拉更新数据
		public void onRefresh();

		// 加载更多数据
		public void onMoreLoad();
	}

	public void setOnRefreshListener(onRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	/**
	 * 重写触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {
				startY = (int) ev.getRawY();
			}
			// 若已经是刷新页面直接跳出
			if (mStatus == STATE_REFRESHING) {
				break;
			}
			int endY = (int) ev.getRawY();
			// 滑动距离
			int dy = endY - startY;
			if (dy > 0 && this.getFirstVisiblePosition() == 0) {
				// 从遮住位置到完全拉出的位移
				int height = dy - mViewHeight;
				view.setPadding(0, height, 0, 0);
				// 已经完全拉出来
				if (height > 0 && mStatus != STATE_RELEASE_REFRESH) {
					mStatus = STATE_RELEASE_REFRESH;
					refreshState(mStatus);
					// 未完全拉出来
				} else if (height <= 0 && mStatus != STATE_PULL_REFRESH) {
					mStatus = STATE_PULL_REFRESH;
					refreshState(mStatus);
				}

			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mStatus == STATE_RELEASE_REFRESH) {
				mStatus = STATE_REFRESHING;
				// 在顶部进行刷新
				view.setPadding(0, 0, 0, 0);
				refreshState(mStatus);
				if (mRefreshListener != null) {
					mRefreshListener.onRefresh();
				}

			} else if (mStatus == STATE_PULL_REFRESH) {
				view.setPadding(0, -mViewHeight, 0, 0);
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 获取当前时间
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * 更新刷新状态
	 * 
	 * @param status
	 */
	public void refreshState(int status) {
		switch (status) {
		case STATE_PULL_REFRESH: // 下拉刷新
			tvHeader.setText("下拉刷新");
			pb.setVisibility(View.INVISIBLE);
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animDown);
			break;
		case STATE_RELEASE_REFRESH:// 松开刷新
			tvHeader.setText("松开刷新");
			pb.setVisibility(View.INVISIBLE);
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animUp);
			break;
		case STATE_REFRESHING:// 正在刷新
			ivArr.clearAnimation();
			tvHeader.setText("正在刷新...");
			pb.setVisibility(View.VISIBLE);
			ivArr.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化动画
	 */
	public void initAnimation() {
		// 旋转动画
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);
	}

	/**
	 * 刷新完成
	 * 
	 * @param complete
	 */
	public void onRefreshComplete(boolean complete) {
		if (isMoreLoad) {
			footerView.setPadding(0, -mFooterHeight, 0, 0);
			// 关闭之后需要将其置位，不然无法触发接口
			isMoreLoad = false;
		} else {
			// 更新状态
			mStatus = STATE_PULL_REFRESH;
			refreshState(mStatus);
			if (complete) {
				tvPubData.setText("最后刷新时间：" + getCurrentTime());
			}
			view.setPadding(0, -mViewHeight, 0, 0);// 隐藏
		}
	}

	// 判断是否到底了
	private boolean isMoreLoad = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			// 若是最后一个
			if (this.getLastVisiblePosition() == getCount() - 1 && !isMoreLoad) {

				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);

				isMoreLoad = true;
				// 通过接口传递标识
				if (mRefreshListener != null) {
					mRefreshListener.onMoreLoad();
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private OnItemClickListener mItemClickListener;

	/**
	 * 自带的点击事件
	 */
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// super.setOnItemClickListener(listener);
		mItemClickListener = listener;
		// 将本类的侦听对象传入
		super.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			//回调这个方法将position的值减去头部分
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}

}
