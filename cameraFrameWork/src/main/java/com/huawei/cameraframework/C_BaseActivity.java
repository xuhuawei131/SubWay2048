package com.huawei.cameraframework;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baidu.mobstat.StatService;
import com.huawei.bean.MySize;
import com.huawei.constants.C_Constants;
import com.huawei.utils.C_Utils;
import com.huawei.utils.SharedPreferenceManager;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.ViewGroup.LayoutParams;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.Toast;

public abstract  class  C_BaseActivity extends FragmentActivity implements Callback{
	protected Camera mCamera;
	protected CameraInfo mCameraInfo;
    private Camera.Parameters params;
    private LinearLayout.LayoutParams lp;
    public SurfaceView mPreview;
    private LinearLayout container;
    private SurfaceHolder mSurfaceHolder;
    
    protected ExecutorService writeThread;
    
    protected abstract void initData();
    protected abstract int setContentView();
    protected abstract void findViewById();
    protected abstract void requestService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		initData();
		super.onCreate(savedInstanceState);
		
		writeThread = Executors.newFixedThreadPool(C_Constants.LOADING_THREADS);
		lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		setContentView(R.layout.activity_main_base);
		
		mPreview=(SurfaceView)findViewById(R.id.surface);
		mSurfaceHolder = mPreview.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		 
		container=(LinearLayout)findViewById(R.id.container);
		
		int layoutID=setContentView();
		View view=LayoutInflater.from(this).inflate(layoutID, null);
		container.addView(view,lp);
		
		findViewById();
		requestService();
	}
	
	protected void showToast(int res){
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}
//   @Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		
//		if(resultCode==1943){
//			mSurfaceHolder.addCallback(this);
//		}else if(resultCode==1942){
//			mCamera.stopPreview();
//			mCamera.release();
//            mCamera = null;
//		}
//		
//			
//	}
@Override
	protected void onPause() {
		super.onPause();
		boolean isEnable=SharedPreferenceManager.getCameraEnable();
		if(isEnable){
			if(mCamera!=null){
				mCamera.stopPreview();
				mCamera.release();
	            mCamera = null;
			}
		}
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		boolean isEnable=SharedPreferenceManager.getCameraEnable();
		if(isEnable){
			mSurfaceHolder.addCallback(this);
		}
		StatService.onResume(this);
	}


	
	/**
	 * 
			* 功能描述：
			* 拍照
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年6月10日 上午11:00:15</p>
			*
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
  protected void take_Photo() {
	  if (mCamera != null) {
		  mCamera.takePicture(null, rawCallback, mPictureCallback);
//		  mCamera.autoFocus(mAutoFocusCallback);
     }
}
	
  
//  private ShutterCallback shutterCallback = new ShutterCallback()  
//  {  
//    public void onShutter()  
//    {  
//      /* 按兀快门瞬间会呼?这里的程序 */
//    }  
//  };
  
  private PictureCallback rawCallback = new PictureCallback()  
  {  
    public void onPictureTaken(byte[] _data, Camera _camera)  
    {  
      /* 要处理raw data?写?否 */
    }  
  };
  
	 /**
     * 拍照图片返回值
     */
    private PictureCallback mPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera arg1) {
        	writeThread.execute(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//		            C_Utils.writeData2File(data);
						C_Utils.saveBitmap(bitmap);
				}
			});
        		showToast(R.string.toast_finish);
				mCamera.stopPreview();
				setCameraInfo(0);
        }
    };
	
	
	
		/**
		* 功能描述：
		* 对焦
		* @author 许华维(WAH-WAY)
		* <p>创建日期 ：2014年5月30日 下午5:53:30</p>
		* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
		*/
	public void takeFocuse() {
		
		mCamera.autoFocus(mAutoFocusCallback);
		
	}
	/**
	 * 
			* 功能描述：
			* 焦点放大
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年5月31日 下午2:05:48</p>
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	protected void setZoomBig() {
		if (isSupportZoom()) {
			try {
				Parameters params = mCamera.getParameters();
				final int MAX = params.getMaxZoom();
				int zoomValue = params.getZoom();
				if (zoomValue <= MAX) {
					zoomValue += 1;
					params.setZoom(zoomValue);
					mCamera.setParameters(params);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
			* 功能描述：
			* 是否支持变焦
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年5月31日 下午2:06:33</p>
			* @return
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	private boolean isSupportZoom() {
		boolean isSuppport = false;
		if (mCamera.getParameters().isZoomSupported())// myCamera.getParameters().isSmoothZoomSupported())
		{
			isSuppport = true;
		}
		return isSuppport;
	}
	
	/**
	 * 
			* 功能描述：
			* 焦点放小
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年5月31日 下午2:06:08</p>
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	protected void setZoomSmall() {
		if (isSupportZoom()) {
			try {
				Parameters params = mCamera.getParameters();
				int zoomValue = params.getZoom();
				if (zoomValue >1) {
					zoomValue -= 1;
					params.setZoom(zoomValue);
					mCamera.setParameters(params);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 对焦的回调函数
	 */
	private Camera.AutoFocusCallback mAutoFocusCallback=new Camera.AutoFocusCallback(){
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if(success){
            	 mCamera.setOneShotPreviewCallback(null);
            	 mCamera.takePicture(null, rawCallback, mPictureCallback);
               }
        }
        
    };
   
	
  //*********************************SurfaceView**************************
	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }
	@Override
    public void surfaceCreated(SurfaceHolder holder) {
		setCameraInfo(0);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    
    
    
    /**
     * 
    		* 功能描述：
    		* 初始化摄像头的参数
    		* @author 许华维(xuhuawei)
    		* <p>创建日期 ：2014-4-29 下午4:09:48</p>
    		* @param i 0为前置摄像头 1为后置摄像头
    		* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
	@SuppressLint("NewApi")
	protected void setCameraInfo(int i){
		if (mCamera != null) {
            mCamera.release();
        }
        CameraInfo cameraInfo = new CameraInfo();
        if (Build.VERSION.SDK_INT >= 9) {
            mCamera = Camera.open(i);
            Camera.getCameraInfo(i, cameraInfo);
        } else {
            mCamera = Camera.open();
            Camera.getCameraInfo(0, cameraInfo);
        }
		
		 params= mCamera.getParameters();
	     params.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭闪光灯
	     params.setJpegQuality(100);
	     params.setPictureFormat(PixelFormat.JPEG);
	     params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//设置聚焦方式为
	     
	     setDisplayOrientation(cameraInfo);
	     initPictureAndPreview();
	        try {
	            mCamera.setParameters(params);
	            mCamera.setPreviewDisplay(mSurfaceHolder);
		        mCamera.startPreview();
	        } catch (Exception e) {
	            e.printStackTrace();
	            mCamera.release();
	            mCamera = null;
	        }
	     
	}

	private void initPictureAndPreview() {
		MySize picture = SharedPreferenceManager.getSelectedPictureSize();
		MySize preview = SharedPreferenceManager.getSelectedPreViewSize();

		// ------------------------------------------------
		if (picture == null) {
			List<Size> list_View = params.getSupportedPictureSizes();
			Size Viewsize = list_View.get(0);
			SharedPreferenceManager.saveSelectedPictureSize(Viewsize);
			params.setPictureSize(Viewsize.width, Viewsize.height);
		} else {
			params.setPictureSize(picture.width, picture.height);
		}

		// ------------------------------------------------
		if (preview == null) {
			List<Size> list_View = params.getSupportedPreviewSizes();
			Size Viewsize = list_View.get(0);
			SharedPreferenceManager.saveSelectedPreViewSize(Viewsize);
			params.setPreviewSize(Viewsize.width, Viewsize.height);
		} else {
			params.setPreviewSize(preview.width, preview.height);
		}
	}
	
	
	/**
     * 设置相机的展示方向
     * @param info
     * @Description:
     */
    @SuppressLint("NewApi")
    private void setDisplayOrientation(CameraInfo info) {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
        case Surface.ROTATION_0:
            degree = 0;
            break;
        case Surface.ROTATION_90:
            degree = 90;
            break;
        case Surface.ROTATION_180:
            degree = 180;
            break;
        case Surface.ROTATION_270:
            degree = 270;
            break;
        }
        int result;
        mCameraInfo = info;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {// 前置摄像头
            result = (info.orientation + degree) % 360;
            result = (360 - result) % 360;
        } else {// 背面摄像头
            result = (info.orientation - degree + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }
	
	
	/**
	 * 
			* 功能描述：
			* 获取支持的 图片文件输出分辨率列表
			* @author 许华维(WAH-WAY)
			* <p>创建日期 ：2014年6月8日 下午11:51:03</p>
			*
			* @return
			*
			* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
@SuppressLint("NewApi")
	protected List<Size> getSupportedPictureSizes() {
		// 照片拍出来的时候的尺寸支持的大小
		if (mCamera == null || params == null) {
			CameraInfo cameraInfo = new CameraInfo();
			if (Build.VERSION.SDK_INT >= 9) {
				mCamera = Camera.open(0);
				Camera.getCameraInfo(0, cameraInfo);
			} else {
				mCamera = Camera.open();
				Camera.getCameraInfo(0, cameraInfo);
			}
			params = mCamera.getParameters();
		}
		List<Size> list_File = params.getSupportedPictureSizes();
		return list_File;
	}

/**
 * 
		* 功能描述：
		* 预览在屏幕上的大小
		* @author 许华维(WAH-WAY)
		* <p>创建日期 ：2014年6月8日 下午11:59:00</p>
		*
		* @return
		*
		* <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
 */
@SuppressLint("NewApi")
	protected List<Size> getSupportedPreviewSizes() {
		if (mCamera == null || params == null) {
			CameraInfo cameraInfo = new CameraInfo();
			if (Build.VERSION.SDK_INT >= 9) {
				mCamera = Camera.open(0);
				Camera.getCameraInfo(0, cameraInfo);
			} else {
				mCamera = Camera.open();
				Camera.getCameraInfo(0, cameraInfo);
			}
			params = mCamera.getParameters();
		}
		List<Size> list_view = params.getSupportedPreviewSizes();
		mCamera.release();
		mCamera = null;
		return list_view;
	}

	protected void setSupportedPictureSizes(Size size) {
		params.setPictureSize(size.width, size.height);
	}

	protected void setSupportedPreviewSizes(Size size) {
		params.setPreviewSize(size.width, size.height);
	}




}
