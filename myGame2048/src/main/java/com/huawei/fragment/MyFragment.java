

package com.huawei.fragment;

import java.io.File;
import java.util.Arrays;

import com.huawei.adapter.FileAdapter;
import com.huawei.game2048.R;
import com.huawei.interfaces.FileDirFilter;
import com.huawei.interfaces.Filecomparator;
import com.huawei.interfaces.OnFileSelectedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MyFragment extends Fragment implements OnItemClickListener,
		OnClickListener {

	private TextView text_path;
	private TextView text_finish;
	private TextView text_create;
	private ListView listview;
	protected View mContentView;
	private File[] files;
	private FileAdapter adapter;
	private File path;
	private OnFileSelectedListener listener;
	private AlertDialog dialog;
	private EditText edit_name;
	private LayoutInflater inflater;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		mContentView = inflater.inflate(R.layout.fragment, null);

		listview = (ListView) mContentView.findViewById(R.id.listview);
		listview.setOnItemClickListener(this);

		text_finish = (TextView) mContentView.findViewById(R.id.text_finish);
		text_create = (TextView) mContentView.findViewById(R.id.text_create);
		text_path = (TextView) mContentView.findViewById(R.id.text_path);

		text_finish.setOnClickListener(this);
		text_create.setOnClickListener(this);

		text_path.setText("path:" + path.getAbsolutePath());
		
		notificationListView();
		
		return mContentView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public void setOnFileSelectedListener(OnFileSelectedListener listener) {
		this.listener = listener;
	}

	public void load(File path) {
		this.path = path;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		listener.onFileClicked(files[position]);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_finish:
			listener.onFileSelected(path);
			break;
		case R.id.text_create:
			showDialog();
			break;
		case R.id.btn_sure:
			setFileCreate();
			break;
		case R.id.btn_cancel:
			dialog.dismiss();
			break;
		}
	}

	private void setFileCreate() {
		String name = edit_name.getText().toString();
		if (name.length() > 0) {
			String file_name = path.getAbsolutePath() + "/" + name+"/";
			File file = new File(file_name);
			if (!file.exists()) {
				file.mkdirs();
			}
			notificationListView();
			dialog.dismiss();
		}
	}

	private void notificationListView() {
		files = path.listFiles(new FileDirFilter());
		if (files != null && files.length > 0) {
			Arrays.sort(files, new Filecomparator());
		}
		adapter = new FileAdapter(inflater, files);
		listview.setAdapter(adapter);
	}

	private void showDialog() {
		if (dialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					this.getActivity());
			View view = inflater.inflate(R.layout.dialog_file, null);
			builder.setView(view);
			edit_name = (EditText) view.findViewById(R.id.edit_name);
			Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			btn_sure.setOnClickListener(this);
			btn_cancel.setOnClickListener(this);
			dialog = builder.create();
		}
		dialog.show();
	}

}
