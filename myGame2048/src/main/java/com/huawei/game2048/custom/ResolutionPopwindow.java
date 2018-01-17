package com.huawei.game2048.custom;

import java.util.ArrayList;

import com.huawei.game2048.R;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.Size;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ResolutionPopwindow extends PopupWindow implements
OnKeyListener, OnClickListener 
{
	private Activity context;
	private onPopwindowSelectorListener listener;
	private WheelView industry;
	private String id;
	private ArrayList<Size> arrayList;
	public ResolutionPopwindow(Activity context, String id,
			onPopwindowSelectorListener listener,ArrayList<Size> arrayList) {
		super(context);
		this.context = context;
		this.id = id;
		this.listener = listener;
		this.arrayList=arrayList;
		findViewByID();
	}

	private void findViewByID() {
		View view = context.getLayoutInflater().inflate(
				R.layout.dialog_revolution, null);

		setContentView(view);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setAnimationStyle(R.style.PopupAnimation);

		TextView text_sure = (TextView)view.findViewById(R.id.btn_sure);
		TextView btn_cancel = (TextView)view.findViewById(R.id.btn_cancel);
		TextView text_title=(TextView)view.findViewById(R.id.text_title);
		
		text_title.setText(id);
		text_sure.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		view.setOnKeyListener(this);
		industry = (WheelView) view.findViewById(R.id.industry);
		industry.setVisibleItems(3);
		ArrayWheelAdapter<Size> adapter = new ArrayWheelAdapter<Size>(
				context, arrayList);
		adapter.setTextSize(20);
		industry.setViewAdapter(adapter);
		industry.setCurrentItem(0);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK && isShowing()) {
			dismiss();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_sure:
			int first = industry.getCurrentItem();
			listener.onPopwindowSelector(id, first);
			dismiss();
			break;
		}
	}
}
