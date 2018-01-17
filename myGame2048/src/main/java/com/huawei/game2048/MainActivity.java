package com.huawei.game2048;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.cameraframework.C_BaseActivity;
import com.huawei.game2048.constants.Constants;
import com.huawei.game2048.util.MySharedPreferenceManager;
import com.huawei.game2048.util.Util;
import com.huawei.game2048.view.AnimLayer;
import com.huawei.game2048.view.GameView;
import com.huawei.utils.SharedPreferenceManager;

public class MainActivity extends C_BaseActivity implements OnClickListener {


	private int score = 0;
	private TextView tvScore, tvBestScore;
	private LinearLayout root = null;
	private TextView btnNewGame;
	private TextView text_introduce;
	private Button btn_menu;
	private Button btn_refresh;
	private GameView gameView;
	private AnimLayer animLayer = null;
	public static final String SP_KEY_BEST_SCORE = "bestScore";
	private int MARGIN_LEFT = 0;
	private int MARGIN_RIGHT = 0;
	private long lastTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initData() {
		MARGIN_LEFT = Util.dip2px(10);
		MARGIN_RIGHT = MARGIN_LEFT;
	}

	protected void findViewById() {

		root = (LinearLayout) findViewById(R.id.container);
		root.setBackgroundColor(0xfffaf8ef);

		tvScore = (TextView) findViewById(R.id.tvScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);
		text_introduce = (TextView) findViewById(R.id.text_introduce);
		btn_menu = (Button) findViewById(R.id.btn_menu);
		btn_refresh = (Button) findViewById(R.id.btn_refresh);

		gameView = (GameView) findViewById(R.id.gameView);
		gameView.setGameViewContext(this);

		btnNewGame = (TextView) findViewById(R.id.btnNewGame);

		animLayer = (AnimLayer) findViewById(R.id.animLayer);
		FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(
				Constants.width - (MARGIN_LEFT + MARGIN_RIGHT), Constants.width
						- (MARGIN_LEFT + MARGIN_RIGHT));
		layout.leftMargin = MARGIN_LEFT;
		layout.rightMargin = MARGIN_RIGHT;
		gameView.setLayoutParams(layout);
		animLayer.setLayoutParams(layout);

		btnNewGame.setOnClickListener(this);
		btn_menu.setOnClickListener(this);
		btn_refresh.setOnClickListener(this);
	}

	protected int setContentView() {
		return (R.layout.activity_main);
	}

	protected void requestService() {
		score=MySharedPreferenceManager.getScroe();
		showScore();
		int bestscroe=MySharedPreferenceManager.getBestScroe();
		showBestScore(bestscroe);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNewGame:
			gameView.startNewGame();
			break;
		case R.id.btn_menu:
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_refresh:
			take_Photo();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){
			boolean isEnable=SharedPreferenceManager.getCameraEnable();
			if(isEnable){
				setCameraInfo(0);
			}else{
				
			}
		}
	}

	public void clearScore() {
		score = 0;
		MySharedPreferenceManager.saveScroe(score);
		showScore();
	}

	public void showScore() {
		tvScore.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();
		
		MySharedPreferenceManager.saveScroe(score);
		
		if(score>getBestScore()){
			saveBestScore(score);
			showBestScore(score);
		}
	}

	public void saveBestScore(int s) {
		MySharedPreferenceManager.saveBestScroe(s);
	}

	public int getBestScore() {
	return	MySharedPreferenceManager.getBestScroe();
	}

	public void showBestScore(int s) {
		tvBestScore.setText(s + "");
	}

	public AnimLayer getAnimLayer() {
		return animLayer;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - lastTime > 2000) {
				lastTime = System.currentTimeMillis();
				showToast(R.string.toast_exit);
				return true;
			} else {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);

	}
}
