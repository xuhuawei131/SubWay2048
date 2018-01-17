package com.huawei.game2048;

import com.huawei.cameraframework.BaseActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class NotificationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected View setContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.activity_notification, null);
			
	}

	@Override
	protected void findViewById() {
		setTitleName("֪ͨ");
	}

	@Override
	protected void requestService() {
	}


}
