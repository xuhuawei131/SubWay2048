

package com.huawei.application;

import android.app.Application;

public class BaseApplication extends Application {
	public static BaseApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
