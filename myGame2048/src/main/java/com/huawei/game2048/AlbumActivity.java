package com.huawei.game2048;

import java.io.File;

import com.huawei.adapter.AlbumAdapter;
import com.huawei.cameraframework.BaseActivity;
import com.huawei.constants.C_Constants;
import com.huawei.interfaces.PhotoFileDirFilter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AlbumActivity extends BaseActivity implements OnItemClickListener{
	private GridView gridview;
	private File[] files;
	private AlbumAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestService();
	}
	protected void initData(){
		File file=new File(C_Constants.diskCachePath);
		files=file.listFiles(new PhotoFileDirFilter());
		
	}
	protected View setContentView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.activity_album,null);
	}

	protected void findViewById() {
		gridview=(GridView)findViewById(R.id.gridview);
		gridview.setOnItemClickListener(this);
		adapter=new AlbumAdapter(this,files);
		gridview.setAdapter(adapter);
		
		setTitleName(R.string.album_title);
	}

	protected void requestService() {

	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent=new Intent(this,PhotoActivity.class);
		intent.putExtra("file", files[position]);
		startActivity(intent);
	}


}
