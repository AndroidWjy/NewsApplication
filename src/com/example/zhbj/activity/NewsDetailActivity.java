package com.example.zhbj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.example.zhbj.R;

public class NewsDetailActivity extends Activity {

	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;
	private WebView webView;
	private ProgressBar pb;
	private SeekBar sbProgress;
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		btnBack.setOnClickListener(mClickListener);
		btnShare.setOnClickListener(mClickListener);
		btnSize.setOnClickListener(mClickListener);

		webView = (WebView) findViewById(R.id.wv_web);
		pb = (ProgressBar) findViewById(R.id.pb_progress);
		sbProgress = (SeekBar) findViewById(R.id.sb_progress);
		String mUrl = getIntent().getStringExtra("NewsUrl");

		initWebView(mUrl);
	}

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				break;
			case R.id.btn_size:
				showDialog();
				break;
			case R.id.btn_share:
				showShare();
				break;
			default:
				break;
			}
		}
	};
	private int mCurrentItem = 2;

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				NewsDetailActivity.this);
		builder.setTitle("选择字体大小");
		String[] items = { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
		builder.setSingleChoiceItems(items, mCurrentItem,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("which=" + which);
						switch (which) {
						case 0:
							settings.setTextZoom(200);
							break;
						case 1:
							settings.setTextZoom(150);
							break;
						case 2:
							settings.setTextZoom(100);
							break;
						case 3:
							settings.setTextZoom(75);
							break;
						case 4:
							settings.setTextZoom(50);
							break;
						default:
							break;
						}
						mCurrentItem = which;
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView(String url) {

		settings = webView.getSettings();
		// 设置js可用
		// settings.setJavaScriptEnabled(true);
		// 显示方法缩小
		settings.setBuiltInZoomControls(true);
		/**
		 * 侦听下载客户端，开始下载，下载完毕
		 */
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("开启下载！");
				pb.setVisibility(View.VISIBLE);
				sbProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("下载完毕！");
				pb.setVisibility(View.GONE);
				sbProgress.setVisibility(View.GONE);
			}

			/**
			 * 所有点击的URL都要经过这里，可以侦听所有的点击的url
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("url=" + url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			/**
			 * 拿到进度条
			 */
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("newProgress=" + newProgress);
				sbProgress.setMax(100);
				sbProgress.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}

			/**
			 * 拿到访问头内容
			 */
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});

		webView.loadUrl(url); 
	}

	/**
	 * shareSdk集成
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setTheme(OnekeyShareTheme.SKYBLUE);

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
