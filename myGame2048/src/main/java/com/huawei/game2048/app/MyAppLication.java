

		package com.huawei.game2048.app;

import java.io.File;







import com.baidu.frontia.FrontiaApplication;
import com.huawei.application.BaseApplication;
import com.huawei.constants.C_Constants;
import com.huawei.game2048.constants.Constants;
import com.huawei.utils.SharedPreferenceManager;

import android.app.Application;
import android.os.Environment;

public class MyAppLication extends BaseApplication {
	public static MyAppLication instance;
	@Override
	public void onCreate() {
		FrontiaApplication.initFrontiaApplication(this);
		super.onCreate();
		instance=this;
		initFile();
	}
	private void initFile() {
		String file=SharedPreferenceManager.getFilePath();
		if(file==null){
			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				C_Constants.diskCachePath = Environment.getExternalStorageDirectory()
						.getPath() + Constants.DISK_CACHE_PATH;
			} else {
				C_Constants.diskCachePath = this.getCacheDir().getAbsolutePath()
						+ Constants.DATA_CACHE_PATH;
			}
			
			File outFile = new File(C_Constants.diskCachePath);
			if(!outFile.exists()){
				outFile.mkdirs();
			}
			
			SharedPreferenceManager.saveFilePath(C_Constants.diskCachePath);
		}else{
			C_Constants.diskCachePath=file;
		}
		
		
	}
}
