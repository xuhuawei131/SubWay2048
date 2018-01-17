package com.huawei.image;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCacheLRU extends ImageCache {
	public LruCache<String, Bitmap> mMemoryCache;
	private static ImageCacheLRU instance;

	private ImageCacheLRU(Context context) {
		super(context);
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		memClass = memClass > 32 ? 32 : memClass;
		// 使用可用内存�?/8作为图片缓存
		final int cacheSize = 1024 * 1024 * memClass / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}

			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				if (evicted && oldValue != null && !oldValue.isRecycled()) {
					oldValue.recycle();
					oldValue = null;
				}
			}

		};
	}

	public static ImageCacheLRU getInstance(Context context) {
		if (instance == null) {
			instance = new ImageCacheLRU(context);
		}
		return instance;
	}

	/**
	 * 删除图片
	 * 
	 * @param url
	 *            图片地址
	 */
	public void remove(String url) {
		if (url == null) {
			return;
		}
		// Remove from memory cache
		mMemoryCache.remove(getCacheKey(url));
		// Remove from file cache
		File f = new File(diskCachePath, url);
		if (f.exists() && f.isFile()) {
			f.delete();
		}
	}

	/**
	 * 清除缓存
	 */
	public void clear() {
		// Remove everything from memory cache
		mMemoryCache.evictAll();
		// Remove everything from file cache
		File cachedFileDir = new File(diskCachePath);
		if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
			File[] cachedFiles = cachedFileDir.listFiles();
			for (File f : cachedFiles) {
				if (f.exists() && f.isFile()) {
					f.delete();
				}
			}
		}
	}

	/**
	 * 清楚常驻内存
	 */
	public void clearHoldMoemory() {
		mMemoryCache.evictAll();
	}

	/**
	 * 缓存图片到内�?	 * 
	 * @param url
	 * @param bitmap
	 */
	public void cacheBitmapToMemory(final String url, final Bitmap bitmap) {
		mMemoryCache.put(getCacheKey(url), bitmap);
	}

	/**
	 * 从内存中获取图片
	 * 
	 * @param url
	 *            图片地址
	 * @return bitmap 对象
	 */
	public Bitmap getBitmapFromMemory(String url) {
		return mMemoryCache.get(getCacheKey(url));
	}

}