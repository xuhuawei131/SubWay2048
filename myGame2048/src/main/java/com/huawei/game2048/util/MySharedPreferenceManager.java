package com.huawei.game2048.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.huawei.application.BaseApplication;

public class MySharedPreferenceManager {
	private static final String SHARE_NAME = "2048";

	public static void saveCardNum(String card) {

		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("cards", card);
		editor.commit();
	}

	public static String getCatdNum() {
		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return sp.getString("cards", null);
	}

	public static void saveScroe(int num) {

		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("scroe", num);
		editor.commit();
	}

	public static int getScroe() {
		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return sp.getInt("scroe", 0);
	}

	public static void saveBestScroe(int num) {

		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("bestscroe", num);
		editor.commit();
	}

	public static int getBestScroe() {
		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return sp.getInt("bestscroe", 0);
	}
	
}
