package com.huawei.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;

public class LocalImage implements SmartImage{
	private static ImageCache webImageCache; // ͼƬ����
	private String url; // ���ص�ַ

	public LocalImage(String url) {
		this.url = url;
	}

	public LocalImage(String url, boolean autoRotate) {
		this.url = url;
	}

	/**
	 * ��ȡͼƬ
	 */
	public Bitmap getBitmap(Context context) {
		if (VERSION.SDK_INT >= 11) {//3.0 �汾���� LRU
			if (webImageCache == null) {
				webImageCache = ImageCacheLRU.getInstance(context);
			}
		}else{
			if (webImageCache == null) {
				webImageCache = ImageCacheSoftReference.getInstance(context);
			}
		}

		// ���ȴӻ����л��ͼƬ
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.getBitmapFromMemory(url);
			if (bitmap == null) {
				bitmap = webImageCache.getBitmapFromDisk(true,url);
				if (bitmap != null) {
					webImageCache.put(url, bitmap,null);
				}
			}
		}

		return bitmap;
	}

	/**
	 * �������
	 * @param url ͼƬ��ַ
	 */
	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}

}
