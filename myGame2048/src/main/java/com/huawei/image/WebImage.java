package com.huawei.image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.util.Log;


public class WebImage implements SmartImage {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;
	private final static int DATA_BUFFER = 1024;
	public static final int AUTO_ROTATE = 1;
	private static ImageCache webImageCache;
	private SmartImageConfig mSmartImageConfig;
	private String url;

	long downloadSize;
	long downloadPercent;
	private long totalSize = -1;
	long remoteSize = 0;
	long currentSize = 0;
	private DownloadListener mDownloadListener;
	private OnImageCacheListener listener;
	public WebImage(String url,OnImageCacheListener listener) {
		this.url = url;
		this.mSmartImageConfig = new SmartImageConfig();
		this.listener=listener;
	}

	public WebImage(String url, SmartImageConfig mSmartImageConfig) {
		super();
		this.mSmartImageConfig = mSmartImageConfig;
		this.url = url;
	}

	public WebImage(String url, DownloadListener mDownloadListener) {
		this.url = url;
		this.mDownloadListener = mDownloadListener;
		this.mSmartImageConfig = new SmartImageConfig();
	}

	public WebImage(String url, SmartImageConfig mSmartImageConfig, DownloadListener mDownloadListener) {
		this.url = url;
		this.mSmartImageConfig = mSmartImageConfig;
		this.mDownloadListener = mDownloadListener;
	}

	public Bitmap getBitmap(Context context) {
		// Don't leak context
		if (VERSION.SDK_INT >= 11) {
			if (webImageCache == null) {
				webImageCache = ImageCacheLRU.getInstance(context);
			}
		}
		else{
			if (webImageCache == null) {
				webImageCache = ImageCacheSoftReference.getInstance(context);
			}
		}


		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.get(url);
			if (bitmap == null) {
				listener.setImageResouce(true);
				bitmap = getBitmapFromUrl(url);
			} else {
				listener.setImageResouce(false);
				if (bitmap!=null && mSmartImageConfig.isAutoRotate && bitmap.getWidth() > bitmap.getHeight()) {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					if (bitmap!=null) {
						webImageCache.cacheBitmapToMemory(url,bitmap);
					}
					
				}
			}
		}

		if (bitmap!=null && mSmartImageConfig.isRound) {
			bitmap = toGrayscale(bitmap, 10);
			return bitmap;
		} else {
			return bitmap;
		}

	}


	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;

		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);
			HttpClient httpClient = new DefaultHttpClient(params);

			HttpGet request = new HttpGet(String.valueOf(url));
			HttpResponse response = httpClient.execute(request);

			InputStream is = null;
			ByteArrayOutputStream baos = null;
			is = response.getEntity().getContent();
			long remoteSize = response.getEntity().getContentLength();
			totalSize = currentSize + remoteSize;
			byte buffer[] = new byte[DATA_BUFFER];
			int readSize = 0;
			baos = new ByteArrayOutputStream();
			while ((readSize = is.read(buffer)) > 0) {
				baos.write(buffer, 0, readSize);
				currentSize += readSize;
				downloadPercent = (long) (currentSize * 100 / totalSize);
				if (mDownloadListener != null) {
					mDownloadListener.updateProcess(downloadPercent);
				}
				downloadSize = currentSize;
			}
			byte[] data = baos.toByteArray();
//			BitmapFactory.Options option=new BitmapFactory.Options();
//			option.inSampleSize=8;
			
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (mSmartImageConfig.isAutoRotate && bitmap.getWidth() > bitmap.getHeight()) {
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			}

			if (bitmap != null) {
				webImageCache.put(url, bitmap, data);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}


	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}
public static void removeAllFromCache(){
	if (webImageCache != null) {
		webImageCache.clear();
	}
}
public static void clearHoldMoemory(){
	if (webImageCache != null) {
		webImageCache.clearHoldMoemory();
	}
}

	public Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(bmpOriginal, pixels);
	}


	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		if (bitmap != null) {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}