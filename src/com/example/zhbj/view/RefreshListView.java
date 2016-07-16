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
 * ����ˢ�µĽ���
 * 
 * @author Administrator
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener,
		android.widget.AdapterView.OnItemClickListener {

	private int startY = -1;
	private View view;
	private int mViewHeight;
	private static final int STATE_PULL_REFRESH = 0;// ����ˢ��
	private static final int STATE_RELEASE_REFRESH = 1;// �ɿ�ˢ��
	private static final int STATE_REFRESHING = 2;// ����ˢ��
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
	 * ����ͷ����
	 */
	public void initView() {
		// ����ˢ�µĶ���
		view = View.inflate(getContext(), R.layout.refresh_view, null);
		this.addHeaderView(view);
		ivArr = (ImageView) view.findViewById(R.id.iv_arr);
		pb = (ProgressBar) view.findViewById(R.id.pb_progress);
		tvHeader = (TextView) view.findViewById(R.id.tv_header);
		tvPubData = (TextView) view.findViewById(R.id.pubdata);
		// Ҫ�õ��߶���Ҫ����
		view.measure(0, 0);
		mViewHeight = view.getMeasuredHeight();
		view.setPadding(0, -mViewHeight, 0, 0);

		initAnimation();
		tvPubData.setText("���ˢ��ʱ�䣺" + getCurrentTime());
	}

	/**
	 * ���ü��ؽŲ���
	 */
	public void initFooter() {
		footerView = View.inflate(getContext(),
				R.layout.refresh_listview_footer, null);
		this.addFooterView(footerView);
		footerView.measure(0, 0);
		mFooterHeight = footerView.getMeasuredHeight();
		footerView.setPadding(0, -mFooterHeight, 0, 0);// ����
		// ���û�������
		this.setOnScrollListener(this);
	}

	/**
	 * �ӿڹ�����
	 * 
	 * @author Administrator
	 * 
	 */
	public interface onRefreshListener {
		// ������������
		public void onRefresh();

		// ���ظ�������
		public void onMoreLoad();
	}

	public void setOnRefreshListener(onRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	/**
	 * ��д�����¼�
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
			// ���Ѿ���ˢ��ҳ��ֱ������
			if (mStatus == STATE_REFRESHING) {
				break;
			}
			int endY = (int) ev.getRawY();
			// ��������
			int dy = endY - startY;
			if (dy > 0 && this.getFirstVisiblePosition() == 0) {
				// ����סλ�õ���ȫ������λ��
				int height = dy - mViewHeight;
				view.setPadding(0, height, 0, 0);
				// �Ѿ���ȫ������
				if (height > 0 && mStatus != STATE_RELEASE_REFRESH) {
					mStatus = STATE_RELEASE_REFRESH;
					refreshState(mStatus);
					// δ��ȫ������
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
				// �ڶ�������ˢ��
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
	 * ��ȡ��ǰʱ��
	 */
	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * ����ˢ��״̬
	 * 
	 * @param status
	 */
	public void refreshState(int status) {
		switch (status) {
		case STATE_PULL_REFRESH: // ����ˢ��
			tvHeader.setText("����ˢ��");
			pb.setVisibility(View.INVISIBLE);
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animDown);
			break;
		case STATE_RELEASE_REFRESH:// �ɿ�ˢ��
			tvHeader.setText("�ɿ�ˢ��");
			pb.setVisibility(View.INVISIBLE);
			ivArr.setVisibility(View.VISIBLE);
			ivArr.startAnimation(animUp);
			break;
		case STATE_REFRESHING:// ����ˢ��
			ivArr.clearAnimation();
			tvHeader.setText("����ˢ��...");
			pb.setVisibility(View.VISIBLE);
			ivArr.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʼ������
	 */
	public void initAnimation() {
		// ��ת����
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
	 * ˢ�����
	 * 
	 * @param complete
	 */
	public void onRefreshComplete(boolean complete) {
		if (isMoreLoad) {
			footerView.setPadding(0, -mFooterHeight, 0, 0);
			// �ر�֮����Ҫ������λ����Ȼ�޷������ӿ�
			isMoreLoad = false;
		} else {
			// ����״̬
			mStatus = STATE_PULL_REFRESH;
			refreshState(mStatus);
			if (complete) {
				tvPubData.setText("���ˢ��ʱ�䣺" + getCurrentTime());
			}
			view.setPadding(0, -mViewHeight, 0, 0);// ����
		}
	}

	// �ж��Ƿ񵽵���
	private boolean isMoreLoad = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			// �������һ��
			if (this.getLastVisiblePosition() == getCount() - 1 && !isMoreLoad) {

				footerView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);

				isMoreLoad = true;
				// ͨ���ӿڴ��ݱ�ʶ
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
	 * �Դ��ĵ���¼�
	 */
	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		// super.setOnItemClickListener(listener);
		mItemClickListener = listener;
		// �����������������
		super.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mItemClickListener != null) {
			//�ص����������position��ֵ��ȥͷ����
			mItemClickListener.onItemClick(parent, view, position
					- getHeaderViewsCount(), id);
		}
	}

}
