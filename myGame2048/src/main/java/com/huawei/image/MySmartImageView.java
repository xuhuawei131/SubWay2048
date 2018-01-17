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
/**
 * 继承至PhotoView 因此 它是可以更新的在放大缩小控件中的
 * @author Administrator
 *
 */
public class MySmartImageView extends PhotoView implements OnImageCacheListener {
	private static final int LOADING_THREADS = 3; // 线程池下载线程的数量
	private static ExecutorService threadPool = Executors
			.newFixedThreadPool(LOADING_THREADS);
	private SmartImageTask currentTask; // 图片下载任务实例对象
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

	/**
	 * 设置图片下载地址
	 * 
	 * @param url
	 *            图片下载地址
	 */
	public void setImageUrl(String url, DownloadListener mDownloadListener) {
		setImage(new WebImage(url, mDownloadListener));
	}

	/**
	 * 设置图片下载地址
	 * 
	 * @param url
	 *            图片下载地址
	 */
	public void setImageUrl(String url, SmartImageConfig mSmartImageConfig) {
		setImage(new WebImage(url, mSmartImageConfig));
	}

	/**
	 * 设置图片下载地址
	 * 
	 * @param url
	 *            图片下载地址
	 */
	public void setImageUrl(String url) {
		setImage(new WebImage(url, this));
	}

	/**
	 * 功能描述： 加载本地图片
	 * 
	 * @param url
	 *            图片路径 创 建 人: DavikChen 日 期： 2012-11-28 上午9:11:21 修 改 人: 日 期:
	 */
	public void setImageLocalUrl(String url) {
		setImage(new LocalImage(url));
	}

	
	
	/**
	 * 设置图片下载地址 和图片下载失败后的错误图
	 * 
	 * @param url
	 *            图片下载地址
	 * @param fallbackResource
	 *            下载失败后的错误图片
	 */
	public void setImageUrl(String url, final Integer fallbackResource) {
		setImage(new WebImage(url, this), fallbackResource);
	}

	/**
	 * 功能描述：加载本地图片
	 * 
	 * @param urlString
	 *            路径
	 * @param fallbackResource
	 *            加载失败后的默认图片 创 建 人: DavikChen 日 期： 2012-11-28 上午9:13:24 修 改 人: 日
	 *            期:
	 */
	public void setImageLocalUrl(String urlString,
			final Integer fallbackResource) {
		setImage(new LocalImage(urlString),fallbackResource);
	}

	/**
	 * 设置图片下载地址
	 * 
	 * @param url
	 *            图片下载地址
	 * @param fallbackResource
	 *            下载失败后的 错误图片
	 * @param loadingResource
	 *            下载中的laoding 图片
	 */
	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource) {
		setImage(new WebImage(url, this), fallbackResource, loadingResource);
	}

	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource, SmartImageConfig mSmartImageConfig) {
		setImage(new WebImage(url, mSmartImageConfig), fallbackResource,
				loadingResource);
	}

	/**
	 * 设置图片下载地址
	 * 
	 * @param url
	 *            图片下载地址
	 * @param fallbackResource
	 *            下载失败后的 错误图片
	 * @param loadingResource
	 *            下载中的laoding 图片
	 * @param isReflact
	 *            是否进行倒影处理
	 */
	public void setImageUrl(String url, final Integer fallbackResource,
			final Integer loadingResource, boolean isReflact) {
		this.isReflect = isReflact;
		setImage(new WebImage(url, this), fallbackResource, loadingResource);
	}

	/**
	 * 功能描述： 加载本地图片
	 * 
	 * @param url
	 *            图片路径
	 * @param fallbackResource
	 *            加载失败后的默认图片
	 * @param loadingResource
	 *            加载中的图片 创 建 人: DavikChen 日 期： 2012-11-28 上午9:14:08 修 改 人: 日 期:
	 */
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

		// 初始化新任务
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

		// 执行任务
		threadPool.execute(currentTask);
	}


	/**
	 * 取消所有任务
	 */
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