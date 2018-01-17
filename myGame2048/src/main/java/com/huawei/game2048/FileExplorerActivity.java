package com.huawei.game2048;

import java.io.File;

import com.huawei.cameraframework.BaseActivity;
import com.huawei.constants.C_Constants;
import com.huawei.fragment.MyFragment;
import com.huawei.interfaces.OnFileSelectedListener;
import com.huawei.utils.SharedPreferenceManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FileExplorerActivity extends FragmentActivity implements
		OnClickListener,OnFileSelectedListener {
	private MyFragment fragment1;
	private MyFragment fragment2;
	private int num = 0;
	private File file_path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_file_explorer);
		requestService();
	}

	protected void initData() {
		String path;
		fragment1 = new MyFragment();
		fragment1.setOnFileSelectedListener(this);
		fragment2 = new MyFragment();
		fragment2.setOnFileSelectedListener(this);
		
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getPath() ;
		} else {
			path = this.getCacheDir().getAbsolutePath();
		}
		file_path = new File(path);
		if(!file_path.exists()){
			file_path.mkdirs();
		}
	}

	protected View setContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.activity_file_explorer,null);
	}

	protected void findViewById() {
		
	}

	protected void requestService() {
		showNextNum();
	}

	@Override
	public void onClick(View v) {
		showNextNum();
	}

	private void showNextNum() {
		if (num % 2 == 0) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.setCustomAnimations(R.anim.view_visible, R.anim.view_invisible);
			ft.replace(R.id.container, fragment1);
			ft.commit();
			fragment1.load(file_path);
		} else {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			 ft.setCustomAnimations(R.anim.view_visible, R.anim.view_invisible);
			ft.replace(R.id.container, fragment2);
			ft.commit();
			fragment2.load(file_path);
		}
		num++;
	}

	@Override
	public void onFileSelected(File file) {
		move2NewDir(C_Constants.diskCachePath,file.getAbsolutePath());
		C_Constants.diskCachePath=file.getAbsolutePath();
		SharedPreferenceManager.saveFilePath(C_Constants.diskCachePath);
		setResult(1);
		finish();
	}

	private void move2NewDir(String oldFile,String newFile){
	}
	
	
	
	@Override
	public void onFileClicked(File file) {
		file_path=file;
		showNextNum();
	}
}
