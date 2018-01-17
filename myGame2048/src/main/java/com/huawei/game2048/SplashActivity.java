package com.huawei.game2048;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.huawei.cameraframework.BaseActivity;
import com.huawei.game2048.constants.Constants;
import com.huawei.utils.SharedPreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends BaseActivity implements AnimationListener {
	private TextView text_splash;
	private Animation anim;// 透明度变化的动画

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatService.setAppChannel(this, "360", true);
		
		
		
		PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                "pYOCh1wQzKhA8uEGNOGfLB6Q");
	}

	protected void initData() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		Constants.width = mDisplayMetrics.widthPixels;
		Constants.height = mDisplayMetrics.heightPixels;
		Constants.density=mDisplayMetrics.density;
	}
	
	protected View setContentView(LayoutInflater inflater){
		return inflater.inflate(R.layout.activity_splash, null);
	} 
	
	protected void findViewById() {
		text_splash = (TextView) findViewById(R.id.text_splash);
		setScreenFull();
	}

	protected void requestService() {
		
		boolean is = SharedPreferenceManager.getFirstRun();
		if (is) {
			SharedPreferenceManager.saveFirstRun(false);
		}
		
		anim = AnimationUtils.loadAnimation(this, R.anim.alphanim);
		anim.setAnimationListener(this);
		text_splash.startAnimation(anim);
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}
