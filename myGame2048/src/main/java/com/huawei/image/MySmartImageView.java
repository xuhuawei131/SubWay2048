package com.huawei.image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.huawei.photoview.PhotoView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class MySmartImageView extends PhotoView implements OnImageCacheListener {
	private static final int LOADING_THREADS = 3;
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);
	private SmartImageTask currentTask;
	private boolean isReflect = false;
	private Integer loadingResource = null;


	public MySmartImageView(Context context) {
		super(context);
	}

	public MySmartImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySmartImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public void setImageUrl(String url, DownloadListener mDownloadListener) {
		setImage(new WebImage(url, mDownloadListener));
	}

	public void setImageUrl(String url, SmartImageConfig mSmartImageConfig) {
		setImage(new WebImage(url, mSmartImageConfig));
	}


	public void setImageUrl(String url) {
		setImage(new WebImage(url, this));
	}


	public void setImageLocalUrl(String url) {
		setImage(new LocalImage(url));
	}

	
	

	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url, this), fallbackResource);
	}


	public void setImageLocalUrl(String urlString,
			final Integer fallbackResource) {
		setImage(new LocalImage(urlString),fallbackResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url, this), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource, SmartImageConfig mSmartImageConfig) {
		setImage(new WebImage(url, mSmartImageConfig), fallbackResource,
				loadingResource);
	}


	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource, boolean isReflact) {
		this.isReflect = isReflact;
		setImage(new WebImage(url, this), fallbackResource, loadingResource);
	}


	public void setImageLocalUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new LocalImage(url),fallbackResource,loadingResource);
	}

	public void setImage(final SmartImage image) {
		setImage(image, null, null);
	}

	public void setImage(final SmartImage image, final Integer fallbackResource) {
		setImage(image, fallbackResource, null);
	}

	public void setImage(final SmartImage image,
			final Integer fallbackResource, final Integer loadingResource) {

		if (currentTask != null) {
			currentTask.cancel();
			currentTask = null;
		}


		currentTask = new SmartImageTask(getContext(), image);
		currentTask
				.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
					@Override
					public void onComplete(Bitmap bitmap) {
						if (bitmap != null) {
							// if(isReflect){//
							// setImageBitmap(getReflectBitmap(bitmap));
							// }else{
							setImageDrawable(new BitmapDrawable(bitmap));
							// }

						} else {
							if (fallbackResource != null) {
								setImageResource(fallbackResource);
							}
						}

					}

				});

		// ִ������
		threadPool.execute(currentTask);
	}



	public static void cancelAllTasks() {
		threadPool.shutdownNow();
		threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
	}

	@Override
	public void setImageResouce(boolean isNet) {
		if (loadingResource != null) {
			if(isNet){
				setImageResource(loadingResource);
			}
		} 
	}
}