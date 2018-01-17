/**
 * BaseApplication.java V1.0 2014年6月13日 上午9:27:02
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

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
