/**
		* BaseActivity.java V1.0 2014年6月12日 上午9:18:01
		*
		* Copyright JIAYUAN Co. ,Ltd. All rights reserved.
		*
		* Modification history(By WAH-WAY):
		*
		* Description:
		*/

		package com.huawei.cameraframework;


import com.baidu.mobstat.StatService;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener{
	private LayoutInflater inflater;
	private LinearLayout container;
	private RelativeLayout layout_top;
	private TextView text_title;
	private Button btn_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		initData();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		inflater=LayoutInflater.from(this);
		
		layout_top=(RelativeLayout)findViewById(R.id.layout_top);
		container=(LinearLayout)findViewById(R.id.container);
		btn_back=(Button)findViewById(R.id.btn_back);
		text_title=(TextView)findViewById(R.id.text_title);
		
		btn_back.setOnClickListener(this);
		
		View childView=setContentView(inflater);
		container.addView(childView,new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		findViewById();
		requestService();
	}
	protected void setScreenFull(){
		layout_top.setVisibility(View.GONE);
	}
	protected abstract void initData();
	protected abstract View setContentView(LayoutInflater inflater);
	protected abstract void findViewById();
	protected abstract void requestService();
	
	protected void setTitleName(String title){
		text_title.setText(title);
	}
	
	protected void setTitleName(int resId){
		text_title.setText(resId);
	}
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_back){
			finish();
		}
	}
	
	
	
//	@Override
//	protected void onPause() {
//		super.onPause();
//		StatService.onPause(this);
//	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		StatService.onResume(this);
//	}
	protected void showTaost(String resID){
		Toast.makeText(this, resID, Toast.LENGTH_SHORT).show();
	}
	protected void showTaost(int resID){
		Toast.makeText(this, resID, Toast.LENGTH_SHORT).show();
	}
}
