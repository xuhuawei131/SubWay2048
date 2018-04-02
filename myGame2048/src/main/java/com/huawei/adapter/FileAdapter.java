

package com.huawei.adapter;

import java.io.File;

import com.huawei.game2048.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {
	private File[] fileArray;
	private LayoutInflater inflater;
	public FileAdapter(LayoutInflater inflater,File[] arrayList){
		this.fileArray=arrayList;
		this.inflater=inflater;	
	}
	@Override
	public int getCount() {
		return fileArray==null?0:fileArray.length;

	}

	@Override
	public Object getItem(int position) {
		return fileArray[position];

	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=inflater.inflate(R.layout.item_file, null);
		}
		TextView text_file=(TextView)convertView.findViewById(R.id.text_file);
		File file=fileArray[position];
		text_file.setText(file.getName());
		return convertView;

	}

}
