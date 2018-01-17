package com.huawei.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;

public class LocalImage implements SmartImage{
	private static ImageCache webImageCache; // 图片缓存
	private String url; // 下载地址

	public LocalImage(String url) {
		this.url = url;
	}

	public LocalImage(String url, boolean autoRotate) {
		this.url = url;
	}

	/**
	 * 获取图片
	 */
	public Bitmap getBitmap(Context context) {
		if (VERSION.SDK_INT >= 11) {//3.0 版本引入 LRU
			if (webImageCache == null) {
				webImageCache = ImageCacheLRU.getInstance(context);
			}
		}else{
			if (webImageCache == null) {
				webImageCache = ImageCacheSoftReference.getInstance(context);
			}
		}

		// 首先从缓存中获得图片
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
	 * 清除缓存
	 * @param url 图片地址
	 */
	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}

}
