package com.abhi.alarmclock;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abhi.alarmclock.db.DbAdapter;
import com.abhi.alarmclock.models.AlarmModel;

@SuppressLint("NewApi")
public class BroadcastRecieverManager extends BroadcastReceiver {

	AlarmModel mAlarmModel;
	//static Context mContext;
	Intent mIntent;


	@Override
	public void onReceive(Context context, Intent intent) {
		//mContext = context;
		mIntent = intent;

	}

	static void setAlarm(AlarmModel model, Context context) {
		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra("id", model.id);
		intent.putExtra("name", model.name);
		intent.putExtra("hours", model.hours);
		intent.putExtra("minutes", model.minutes);
		intent.putExtra("date", model.days);

		PendingIntent pendingIntent = PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(model.days);
		c.set(Calendar.HOUR_OF_DAY,model.hours);
		c.set(Calendar.MINUTE,model.minutes);
		c.set(Calendar.SECOND,0);

		Log.v("after setting time", ""+ c.getTimeInMillis());

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
		}
	}

	public static void cancelAlarm(Context context, long id) {
		DbAdapter db = new DbAdapter(context);

		AlarmModel alarmModel =  db.fetch(id);

		Intent intent = new Intent(context, AlarmService.class);
		intent.putExtra("id", alarmModel.id);
		intent.putExtra("name", alarmModel.name);
		intent.putExtra("hours", alarmModel.hours);
		intent.putExtra("minutes", alarmModel.minutes);
		intent.putExtra("date", alarmModel.days);

		PendingIntent pendingIntent = PendingIntent.getService(context, (int) alarmModel.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);


		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}




}
