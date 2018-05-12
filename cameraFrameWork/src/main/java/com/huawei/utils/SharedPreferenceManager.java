/**
		* SharedPreferenceManager.java V1.0 2014��6��12�� ����10:23:07
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

		package com.huawei.utils;


import com.huawei.application.BaseApplication;
import com.huawei.bean.MySize;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Camera.Size;

	public class SharedPreferenceManager {
		private static final String SHARE_NAME="camera_shared";
		
		public static void saveSelectedPictureSize(Size size){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putInt("picture_width", size.width);
			editor.putInt("picture_height", size.height);
			editor.commit();
		}
		public static MySize getSelectedPictureSize(){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			int width=sp.getInt("picture_width", 0);
			int heigth=sp.getInt("picture_height", 0);
			if(width==0){
				return null;
			}else{
				MySize size=new MySize(width, heigth);
				return size;
			}
		}
		
		public static void saveSelectedPreViewSize(Size size){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putInt("preview_width", size.width);
			editor.putInt("preview_height", size.height);
			editor.commit();
		}
		public static MySize getSelectedPreViewSize(){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			int width=sp.getInt("preview_width", 0);
			int heigth=sp.getInt("preview_height", 0);
			if(width==0){
				return null;
			}else{
				MySize size=new MySize(width, heigth);
				return size;
			}
		}

		public static void saveFirstRun(boolean isFirst){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putBoolean("isFirst", isFirst);
			editor.commit();
		}
		
		public static boolean getFirstRun(){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			return sp.getBoolean("isFirst", true);
		}
		public static String getPassword(){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			return sp.getString("password", null);
		}
		public static void savePassword(String password){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("password", password);
			editor.commit();
		}
		
	public static void saveCameraEnable(boolean enable) {
		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("camera_enable", enable);
		editor.commit();
	}

	public static boolean getCameraEnable() {
		SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
				SHARE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("camera_enable", false);
	}
	
		public static void saveSuperEnable(boolean enable) {
			SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
					SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putBoolean("super_enable", enable);
			editor.commit();
		}

		public static boolean getSuperEnable() {
			SharedPreferences sp = BaseApplication.instance.getSharedPreferences(
					SHARE_NAME, Context.MODE_PRIVATE);
			return sp.getBoolean("super_enable", false);
		}
		
		public static String getFilePath(){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			return sp.getString("filepath", null);
		}
		public static void saveFilePath(String filepath){
			SharedPreferences sp =BaseApplication.instance.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("filepath", filepath);
			editor.commit();
		}
		
		
		
		
}
