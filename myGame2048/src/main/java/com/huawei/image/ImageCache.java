package com.huawei.image;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;
import android.util.Log;

public abstract class ImageCache {
	protected static final String DISK_CACHE_PATH = "/DeRun/image/";
	protected static final String DATA_CACHE_PATH = "/webimage_cache/"; //
	protected String diskCachePath;
	protected boolean diskCacheEnabled = false;
	protected ExecutorService writeThread; //


	public abstract Bitmap getBitmapFromMemory(String url);


	public abstract void cacheBitmapToMemory(final String url,
			final Bitmap bitmap);


	public abstract void clear();


	public abstract void clearHoldMoemory();


	public abstract void remove(String url);

	protected ImageCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			diskCachePath = Environment.getExternalStorageDirectory().getPath()
					+ DISK_CACHE_PATH;
		} else {
			Context appContext = context.getApplicationContext();
			diskCachePath = appContext.getCacheDir().getAbsolutePath()
					+ DATA_CACHE_PATH;
		}

		File outFile = new File(diskCachePath);
		outFile.mkdirs();
		diskCacheEnabled = outFile.exists();
		// Set up threadpool for image fetching tasks
		writeThread = Executors.newSingleThreadExecutor();
	}


	public Bitmap get(final String url) {
		Bitmap bitmap = null;
		// Check for image in memory
		bitmap = getBitmapFromMemory(url);
		// Check for image on disk cache
		if (bitmap == null) {
			bitmap = getBitmapFromDisk(false,url);
			// Write bitmap back into memory cache
			if (bitmap != null) {
				cacheBitmapToMemory(url, bitmap);
			}
		}

		return bitmap;
	}


	public void put(String url, Bitmap bitmap, byte[] data) {
		if (bitmap != null) {
			cacheBitmapToMemory(url, bitmap); // �ڴ�
		}
		if (data != null) {
			cacheBitmapToDisk(url, data); // SDCard
		}

	}


	private void cacheBitmapToDisk(final String url, final Bitmap bitmap) {
		writeThread.execute(new Runnable() {
			@Override
			public void run() {
				if (diskCacheEnabled) {
					BufferedOutputStream ostream = null;
					try {
						ostream = new BufferedOutputStream(
								new FileOutputStream(new File(diskCachePath,
										getCacheKey(url))), 2 * 1024);
						bitmap.compress(CompressFormat.JPEG, 100, ostream);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							if (ostream != null) {
								ostream.flush();
								ostream.close();
							}
						} catch (IOException e) {
						}
					}
				}
			}
		});
	}


	private void cacheBitmapToDisk(final String url, final byte[] data) {
		writeThread.execute(new Runnable() {
			@Override
			public void run() {
				if (diskCacheEnabled) {
					File file = new File(diskCachePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					File picPath = new File(diskCachePath + getCacheKey(url));
					BufferedOutputStream bufferedOutputStream;
					try {
						bufferedOutputStream = new BufferedOutputStream(
								new FileOutputStream(picPath));
						bufferedOutputStream.write(data);
						bufferedOutputStream.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}


	protected String getCacheKey(String url) {
		if (url != null) {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
		return url;
	}


	public Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(bmpOriginal, pixels);
	}


	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		if (bitmap != null) {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		}

		return output;
	}


	public Bitmap getBitmapFromDisk(boolean isLocal,String url) {
		Bitmap bitmap = null;
		if (diskCacheEnabled) {
			String filePath ;
			if(isLocal){
				filePath=url;
			}else{
				filePath= getFilePath(url);
			}
			File file = new File(url);
			if (file.exists()) {
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
       	    		options.inJustDecodeBounds = true;
       	    		bitmap = BitmapFactory.decodeFile(url,options);
       	    		int scale = (int)(options.outHeight / (float)200);
       	    		if (scale <= 1){
       	    			scale = 1;
       	    		}
       	    		options.inSampleSize = scale;
       	    		options.inJustDecodeBounds=false;
					bitmap = BitmapFactory.decodeFile(filePath,options);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}


	private String getFilePath(String url) {
		return diskCachePath + getCacheKey(url);
	}

}
