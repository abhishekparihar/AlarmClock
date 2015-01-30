package com.abhi.alarmclock;

import com.abhi.alarmclock.db.DbAdapter;
import com.abhi.alarmclock.models.AlarmModel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Intent mIntent = new Intent(getBaseContext(), AlarmNoticationActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.putExtras(intent);
		getApplication().startActivity(mIntent);
		
		DbAdapter dbAdapter = new DbAdapter(getBaseContext());
		AlarmModel model = dbAdapter.fetch((long)intent.getLongExtra("id", 0));
		//BroadcastRecieverManager.setAlarm(model, getBaseContext());
		return super.onStartCommand(intent, flags, startId);
	}

}
