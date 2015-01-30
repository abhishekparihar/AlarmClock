package com.abhi.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlarmNoticationActivity extends Activity {

	private static String TAG = "AlarmNoticationActivity";
	private TextView textViewName;
	private TextView textViewTime;
	private Button buttonSnooz;
	private Button buttonDismiss;
	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_screen);

		initializeView();

		String time = getIntent().getIntExtra("hours", 0) + " : " + getIntent().getIntExtra("minutes", 0);
		String name = getIntent().getStringExtra("name");

		textViewName.setText(name);
		textViewTime.setText(time);

		playAlarm();

		//Ensure wakelock release
		Runnable releaseWakelock = new Runnable() {

			@Override
			public void run() {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

				if (mWakeLock != null && mWakeLock.isHeld()) {
					mWakeLock.release();
				}
			}
		};

		new Handler().postDelayed(releaseWakelock, 60000);

	}

	private void initializeView() {
		textViewName = (TextView)findViewById(R.id.textViewName);
		textViewTime = (TextView)findViewById(R.id.textViewTime);
		buttonSnooz = (Button) findViewById(R.id.buttonSnooz);
		buttonDismiss = (Button) findViewById(R.id.buttonDismiss);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
		}

		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
			Log.i("", "Wakelock aquired!!");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		mPlayer.stop();
		finish();
	}

	private void playAlarm() {
		mPlayer = new MediaPlayer();
		try {

			mPlayer.setDataSource(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
			mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mPlayer.setLooping(true);
			mPlayer.prepare();
			mPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void onDismissAlarmClick(View v){
		mPlayer.stop();
		finish();
	}

}
