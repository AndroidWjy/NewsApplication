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
		builder.setTitle("ѡ�������С");
		String[] items = { "���������", "�������", "��������", "С������", "��С������" };
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
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView(String url) {

		settings = webView.getSettings();
		// ����js����
		// settings.setJavaScriptEnabled(true);
		// ��ʾ������С
		settings.setBuiltInZoomControls(true);
		/**
		 * �������ؿͻ��ˣ���ʼ���أ��������
		 */
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("�������أ�");
				pb.setVisibility(View.VISIBLE);
				sbProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("������ϣ�");
				pb.setVisibility(View.GONE);
				sbProgress.setVisibility(View.GONE);
			}

			/**
			 * ���е����URL��Ҫ������������������еĵ����url
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("url=" + url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			/**
			 * �õ�������
			 */
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("newProgress=" + newProgress);
				sbProgress.setMax(100);
				sbProgress.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}

			/**
			 * �õ�����ͷ����
			 */
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});

		webView.loadUrl(url); 
	}

	/**
	 * shareSdk����
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();
		oks.setTheme(OnekeyShareTheme.SKYBLUE);

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle(getString(R.string.share));
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("���Ƿ����ı�");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		oks.show(this);
	}
}
