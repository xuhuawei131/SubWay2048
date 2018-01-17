package com.huawei.game2048;

import java.io.File;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.huawei.cameraframework.BaseActivity;
import com.huawei.image.MySmartImageView;

public class PhotoActivity extends BaseActivity {
	private File file;
	private MySmartImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initData() {
		file=(File)this.getIntent().getSerializableExtra("file");
	}

	protected View setContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.activity_photo,null);
	}

	protected void findViewById() {
		image=(MySmartImageView)findViewById(R.id.image);
		setTitleName(R.string.big_photo_title);
		
	}
	protected void requestService(){
		image.setImageLocalUrl(file.getAbsolutePath(), R.drawable.ic_launcher, R.drawable.ic_launcher);
	}

}
