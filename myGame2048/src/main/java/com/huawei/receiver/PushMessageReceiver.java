/**
 * PushMessageReceiver.java V1.0 2014年6月17日 下午3:06:47
 *
 * Copyright JIAYUAN Co. ,Ltd. All rights reserved.
 *
 * Modification history(By WAH-WAY):
 *
 * Description:
 */

package com.huawei.receiver;

import com.baidu.android.pushservice.PushConstants;
import com.huawei.game2048.NotificationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(PushConstants.ACTION_RECEIVE)) {

		} else if (action.equals(PushConstants.ACTION_MESSAGE)) {

		} else if (action.equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
//			Intent myintent = new Intent();
			intent.setClass(context.getApplicationContext(),
					NotificationActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startActivity(intent);
		}
	}

}
