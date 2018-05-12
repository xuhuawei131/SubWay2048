package com.huawei.game2048;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.bean.MySize;
import com.huawei.cameraframework.C_BaseActivity;
import com.huawei.constants.C_Constants;
import com.huawei.game2048.constants.Constants;
import com.huawei.game2048.custom.MySlipSwitch;
import com.huawei.game2048.custom.MySlipSwitch.OnSwitchListener;
import com.huawei.game2048.custom.ResolutionPopwindow;
import com.huawei.game2048.custom.onPopwindowSelectorListener;
import com.huawei.utils.SharedPreferenceManager;

public class SettingActivity extends C_BaseActivity implements OnClickListener,
		onPopwindowSelectorListener, OnSwitchListener {

	private static final String TITLE_PICTURE = "图片分辨率";
	private static final String TITLE_PREVIEW = "预览分辨率";

	private ArrayList<Size> picture_list;
	private ArrayList<Size> preview_list;

	private ResolutionPopwindow pop_picture;
	private ResolutionPopwindow pop_preview;

	private View layout_file_size;
	private View layout_preview_size;
	private View layout_file_path;
	private View layout_photo;
	private View btn_back;

	private TextView text_picture_size;
	private TextView text_preview_size;

	private MySlipSwitch slip_camera;
	private MySlipSwitch slip_super;

	private TextView text_camera_enable;
	private TextView text_super_enable;
	private TextView text_path;

	private AlertDialog dialog;
	private EditText edit_password;
	private EditText edit_repassword;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void initData() {
		
	}

	protected int setContentView() {
		return (R.layout.activity_setting);
	}

	protected void findViewById() {
		btn_back = findViewById(R.id.btn_back);
		layout_file_size = findViewById(R.id.layout_file_size);
		layout_preview_size = findViewById(R.id.layout_preview_size);
		layout_file_path = findViewById(R.id.layout_file_path);
		layout_photo = findViewById(R.id.layout_photo);

		text_picture_size = (TextView) findViewById(R.id.text_picture_size);
		text_preview_size = (TextView) findViewById(R.id.text_preview_size);

		slip_camera = (MySlipSwitch) findViewById(R.id.slip_camera);
		slip_super = (MySlipSwitch) findViewById(R.id.slip_super);

		text_camera_enable = (TextView) findViewById(R.id.text_camera_enable);
		text_super_enable = (TextView) findViewById(R.id.text_super_enable);

		text_path = (TextView) findViewById(R.id.text_path);
		text_path.setText(C_Constants.diskCachePath);

		layout_file_size.setOnClickListener(this);
		layout_preview_size.setOnClickListener(this);
		layout_file_path.setOnClickListener(this);
		layout_photo.setOnClickListener(this);
		btn_back.setOnClickListener(this);

	}

	protected void requestService() {

		MySize picture = SharedPreferenceManager.getSelectedPictureSize();
		if (picture == null) {
			List<Size> list_View = getSupportedPictureSizes();
			Size Viewsize = list_View.get(0);
			SharedPreferenceManager.saveSelectedPictureSize(Viewsize);
			text_picture_size.setText(Viewsize.width + "X" + Viewsize.height);
		} else {
			text_picture_size.setText(picture.width + "X" + picture.height);
		}

		MySize preview = SharedPreferenceManager.getSelectedPreViewSize();
		if (preview == null) {
			List<Size> list_View = getSupportedPreviewSizes();
			Size Viewsize = list_View.get(0);
			SharedPreferenceManager.saveSelectedPreViewSize(Viewsize);
			text_preview_size.setText(Viewsize.width + "X" + Viewsize.height);
		} else {
			text_preview_size.setText(preview.width + "X" + preview.height);
		}

		boolean cameraEnable = SharedPreferenceManager.getCameraEnable();
		if (cameraEnable) {
			text_camera_enable.setText(R.string.setting_enable_content);
		} else {
			text_camera_enable.setText(R.string.setting_disenable_content);
		}
		slip_camera.setSwitchState(cameraEnable);
		slip_camera.setOnSwitchListener(this);

		boolean superEnable = SharedPreferenceManager.getSuperEnable();
		if (superEnable) {
			text_super_enable.setText(R.string.setting_enable_content);
		} else {
			text_super_enable.setText(R.string.setting_disenable_content);
		}
		slip_super.setSwitchState(superEnable);
		slip_super.setOnSwitchListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.layout_file_size:
			setSaveFileSize();
			break;
		case R.id.layout_preview_size:
			setPreViewSize();
			break;
		case R.id.layout_file_path:
			setSavePath();
			break;
		case R.id.layout_photo:
			lookPhotos();
			break;
		case R.id.btn_sure:
			checkEditText();
			dialog.dismiss();
			break;
		case R.id.btn_cancel:
			dialog.dismiss();
			break;
		}
	}

	private void checkEditText() {
		String password = edit_password.getText().toString();

		if (edit_repassword.getVisibility() == View.VISIBLE) {
			String repassword = edit_repassword.getText().toString();
			if (password.length() == 0) {
				showToast(R.string.toast_password_not_null);
				return;
			} else if (repassword.length() == 0) {
				showToast(R.string.toast_repassword_not_null);
				return;
			} else if (!password.equals(repassword)) {
				showToast(R.string.toast_passwords_not_equal);
				return;
			} else {
				SharedPreferenceManager.savePassword(password);
				Intent intent = new Intent(this, AlbumActivity.class);
				startActivity(intent);
			}
		} else {
			if (password.length() == 0) {
				showToast(R.string.toast_password_not_null);
				return;
			} else if (!password.equals(SharedPreferenceManager.getPassword())) {
				showToast(R.string.toast_password_error);
				return;
			} else {
				Intent intent = new Intent(this, AlbumActivity.class);
				startActivity(intent);
			}
		}

	}

	private void setSaveFileSize() {
		if (picture_list == null) {
			picture_list = (ArrayList<Size>) getSupportedPictureSizes();
		}
		if (pop_picture == null) {
			pop_picture = new ResolutionPopwindow(this, TITLE_PICTURE, this,
					picture_list);
		}
		pop_picture.showAtLocation(findViewById(R.id.container),
				Gravity.BOTTOM, 0, 0);
	}

	private void setPreViewSize() {
		if (preview_list == null) {
			preview_list = (ArrayList<Size>) getSupportedPreviewSizes();
		}
		if (pop_preview == null) {
			pop_preview = new ResolutionPopwindow(this, TITLE_PREVIEW, this,
					preview_list);
		}
		pop_preview.showAtLocation(findViewById(R.id.container),
				Gravity.BOTTOM, 0, 0);
	}
	
	private void setSavePath() {
		Intent intent = new Intent(this, FileExplorerActivity.class);
		startActivityForResult(intent, 1);
	}

	private void lookPhotos() {
		if (dialog == null) {
			View view = View.inflate(this, R.layout.dialog_password, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(view);
			
			text = (TextView) view.findViewById(R.id.text_title);
			edit_password = (EditText) view.findViewById(R.id.edit_password);
			edit_repassword = (EditText) view
					.findViewById(R.id.edit_repassword);
			Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			btn_sure.setOnClickListener(this);
			btn_cancel.setOnClickListener(this);
			dialog = builder.create();
			
		} else {
			edit_password.setText("");
			edit_repassword.setText("");
		}

		String password = SharedPreferenceManager.getPassword();
		if (password == null) {
			edit_repassword.setVisibility(View.VISIBLE);
			text.setText(R.string.dialog_title_create);
		} else {
			edit_repassword.setVisibility(View.GONE);
			text.setText(R.string.dialog_title_check);
		}
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1) {
			text_path.setText(C_Constants.diskCachePath);
		}
	}

	@Override
	public void onPopwindowSelector(String ID, int position) {
		if (ID.equals(TITLE_PICTURE)) {
			Size size = picture_list.get(position);
			SharedPreferenceManager.saveSelectedPictureSize(size);
			text_picture_size.setText(size.width + "X" + size.height);

		} else if (ID.equals(TITLE_PREVIEW)) {
			Size size = preview_list.get(position);
			SharedPreferenceManager.saveSelectedPreViewSize(size);
			text_preview_size.setText(size.width + "X" + size.height);
		}
	}

	@Override
	public void onSwitched(View view, boolean isSwitchOn) {
		switch (view.getId()) {
		case R.id.slip_camera:
			if (isSwitchOn) {
				text_camera_enable.setText(R.string.setting_enable_content);
			} else {
				text_camera_enable.setText(R.string.setting_disenable_content);
			}
			SharedPreferenceManager.saveCameraEnable(isSwitchOn);
			break;
		case R.id.slip_super:
			if (isSwitchOn) {
				text_super_enable.setText(R.string.setting_enable_content);
			} else {
				text_super_enable.setText(R.string.setting_disenable_content);
			}
			SharedPreferenceManager.saveSuperEnable(isSwitchOn);
			break;
		}
	}




}
