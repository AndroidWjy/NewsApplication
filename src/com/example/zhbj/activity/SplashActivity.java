package com.example.zhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.zhbj.R;
import com.example.zhbj.utils.SpreUtils;

public class SplashActivity extends Activity {

	private RelativeLayout rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		rl = (RelativeLayout) findViewById(R.id.rl_root);
		// 动画进入闪存页
		startAnimation();
	}

	/**
	 * 动画效果
	 */

	public void startAnimation() {
		AnimationSet set = new AnimationSet(false);
		// 旋转
		RotateAnimation rot = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rot.setDuration(1000);
		rot.setFillAfter(true);
		// 缩放
		ScaleAnimation scal = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scal.setDuration(1000);// 设置执行实践
		scal.setFillAfter(true);// 动画执行完毕之后保持状态
		// 渐变
		AlphaAnimation alp = new AlphaAnimation(0, 1);
		alp.setDuration(1000);
		alp.setFillAfter(true);

		set.addAnimation(scal);
		set.addAnimation(rot);
		set.addAnimation(alp);
		// 设置动画侦听
		set.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				boolean bool = SpreUtils.getBoolean(SplashActivity.this,
						"is_guide_use", false);
				if (bool) {
					startActivity(new Intent(SplashActivity.this,
							HomeActivity.class));
					finish();
				} else {
					startActivity(new Intent(SplashActivity.this,
							GuideActivity.class));
					finish();
				}
			}
		});

		rl.startAnimation(set);
	}

}
