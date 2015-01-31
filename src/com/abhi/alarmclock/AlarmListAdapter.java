package com.abhi.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.abhi.alarmclock.db.DbAdapter;
import com.abhi.alarmclock.models.AlarmModel;

@SuppressLint("SimpleDateFormat")
public class AlarmListAdapter extends BaseAdapter {

	private Context mContext;
	private List<AlarmModel> alarmModels;
	private DbAdapter dbAdapter;
	private static final String WEEKDAYS[] = {"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	public AlarmListAdapter(Context context, List<AlarmModel> alarms) {
		mContext = context;
		alarmModels = alarms;
		dbAdapter = new DbAdapter(mContext);
	}

	@Override
	public int getCount() {
		if(alarmModels != null)
			return alarmModels.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(alarmModels != null)
			return alarmModels.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		if(alarmModels != null)
			return alarmModels.get(position).id;
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);
		}

		AlarmModel model = alarmModels.get(position);
		TextView textViewAlarmTime = (TextView)convertView.findViewById(R.id.textViewAlarmTime);
		TextView textViewAlarmName = (TextView)convertView.findViewById(R.id.textViewAlarmName);
		TextView textViewDate = (TextView)convertView.findViewById(R.id.textViewDate);
		TextView textViewAlarmDay = (TextView)convertView.findViewById(R.id.textViewAlarmDay);
		ImageButton buttonDelete = (ImageButton)convertView.findViewById(R.id.imgButtonDelete);

		Switch switchOnOff = (Switch)convertView.findViewById(R.id.switchOnOff);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(model.days);

		SimpleDateFormat df = new SimpleDateFormat("dd- MMM");
		String formatDate = df.format(model.days);
		textViewAlarmTime.setText(model.hours + " : " + model.minutes);
		textViewAlarmName.setText(model.name);
		textViewDate.setText(formatDate);
		textViewAlarmDay.setText(WEEKDAYS[c.get(Calendar.DAY_OF_WEEK)-1]);

		switchOnOff.setChecked(model.isEnabled);

		buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("QQQQQQQQQ", ""+ alarmModels.get(position).id);
				dbAdapter.delete(alarmModels.get(position).id);
				alarmModels.remove(position);
				notifyDataSetChanged();

			}
		});

		switchOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dbAdapter.updateSwitchState(alarmModels.get(position).id, isChecked);
				changeAlarmState(alarmModels.get(position), isChecked);

			}


		});

		return convertView;
	}

	private void changeAlarmState(AlarmModel alarmModel, boolean isChecked) {
		Calendar cal = Calendar.getInstance();
		long timeInMillis = alarmModel.days;
		dbAdapter.updateSwitchState(alarmModel.id, isChecked);
		if(isChecked && timeInMillis > cal.getTimeInMillis())
			BroadcastRecieverManager.setAlarm(alarmModel, mContext);
		else if (!isChecked) {
			BroadcastRecieverManager.cancelAlarm(mContext, alarmModel.id);
		}

	}

}
