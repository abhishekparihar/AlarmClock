package com.abhi.alarmclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.abhi.alarmclock.db.DbAdapter;
import com.abhi.alarmclock.models.AlarmModel;

@SuppressLint("SimpleDateFormat")
public class AddAlarmActivity extends Activity {
	TextView textViewDate;
	EditText editTextName;
	TimePicker timePickerAlarm;
	RadioGroup radioGroupDay;
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	private  AlarmModel alarmModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_alarm);
		alarmModel = new AlarmModel();
		initilaizeView();
	}

	private void initilaizeView() {
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);




		textViewDate = (TextView) findViewById(R.id.textViewDate);
		editTextName = (EditText) findViewById(R.id.editTextName);
		timePickerAlarm = (TimePicker) findViewById(R.id.timePickerAlarm);
		radioGroupDay = (RadioGroup) findViewById(R.id.radioGroupDay);

		textViewDate.setText(getFormattedDate(cal));

		radioGroupDay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.v("", "++---------++  " + checkedId);

				getDate(checkedId);


			}
		});

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void  OnSaveButtonClick(View v) {
		populateDataIntoModel();
		DbAdapter dbAdapter = new DbAdapter(this);
		dbAdapter.create(alarmModel);
		BroadcastRecieverManager.setAlarm(alarmModel, this);
		Log.v("", "++++++++++");
		
		finish();
	}



	@SuppressWarnings("deprecation")
	private void getDate(int checkedId) {
		cal = Calendar.getInstance();

		RadioButton radioButton = (RadioButton) findViewById(checkedId);

		Toast.makeText(AddAlarmActivity.this,
				radioButton.getText(), Toast.LENGTH_SHORT).show();

		switch (radioButton.getText().toString()) {
		case "Today":
			textViewDate.setText(getFormattedDate(cal));
			Log.v("", "Today++++++++++");
			break;

		case "Tomorrow":
			Log.v("", "Tomorrow++++++++++");
			day = cal.get(Calendar.DAY_OF_MONTH) + 1;
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			textViewDate.setText(getFormattedDate(cal));
			Log.v("", "Tomorrow++++++++++");
			break;
		case "Select Date":
			showDialog(0);
			Log.v("", "Select Date++++++++++");
			break;
		default:
			break;
		}

	}

	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			day = selectedDay;
			month = selectedMonth;
			year = selectedYear;
			cal = Calendar.getInstance();
			cal.set(selectedYear, selectedMonth, selectedDay);

			textViewDate.setText(getFormattedDate(cal));
		}
	};


	private String getFormattedDate(Calendar cal) {
		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd-MMM-yyyy",Locale.getDefault());
		String formatDate = df.format(cal.getTime());

		return formatDate;

	}

	private void populateDataIntoModel() {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, 00, 00, 00);
		//cal.set(Calendar.MINUTE, timePickerAlarm.getCurrentMinute());
		alarmModel.name = editTextName.getText().toString().trim();
		alarmModel.hours = timePickerAlarm.getCurrentHour().intValue();
		alarmModel.minutes = timePickerAlarm.getCurrentMinute().intValue();
		alarmModel.days = cal.getTimeInMillis();
		alarmModel.isEnabled = true;
		
	}
}
