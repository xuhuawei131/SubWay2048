package com.huawei.image;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCacheSoftReference extends ImageCache {
	public ConcurrentHashMap<String, SoftReference<Bitmap>> mMemoryCache;
	private static ImageCacheSoftReference instance;

	private ImageCacheSoftReference(Context context) {
		super(context);
		mMemoryCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	}

	public static ImageCacheSoftReference getInstance(Context context) {
		if (instance == null) {
			instance = new ImageCacheSoftReference(context);
		}
		return instance;
	}


	public Bitmap getBitmapFromMemory(String url) {
		Bitmap bitmap = null;
		SoftReference<Bitmap> softRef = mMemoryCache.get(getCacheKey(url));
		if (softRef != null) {
			bitmap = softRef.get();
		}
		return bitmap;
	}

	@Override
	public void clear() {
		mMemoryCache.clear();
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

	@Override
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
	@Override
	public void cacheBitmapToMemory(String url, Bitmap bitmap) {
		mMemoryCache.put(getCacheKey(url), new SoftReference<Bitmap>(bitmap));
	}
	
	@Override
	public void clearHoldMoemory() {
		mMemoryCache.clear();
		System.gc();
	}

}