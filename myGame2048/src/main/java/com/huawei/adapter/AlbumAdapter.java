
package com.huawei.adapter;

import java.io.File;
import java.util.ArrayList;

import com.huawei.constants.C_Constants;
import com.huawei.game2048.R;
import com.huawei.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AlbumAdapter extends BaseAdapter {
	private File[] files;
	private LayoutInflater inlater;

	public AlbumAdapter(Context context, File[] files) {
		this.files = files;
		this.inlater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return files.length;

	}

	@Override
	public Object getItem(int position) {
		return files[position];

	}

	@Override
	public long getItemId(int position) {
		return position;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inlater.inflate(R.layout.item_album, null);
		}
		SmartImageView image = (SmartImageView) convertView
				.findViewById(R.id.image);
		image.setImageLocalUrl(files[position].getAbsolutePath(),R.drawable.ic_launcher,R.drawable.ic_launcher);
		return convertView;

	}

}
