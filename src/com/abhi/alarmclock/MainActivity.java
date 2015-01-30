package com.abhi.alarmclock;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.abhi.alarmclock.db.DbAdapter;
import com.abhi.alarmclock.models.AlarmModel;

public class MainActivity extends Activity {
	private DbAdapter dbAdapter;
	private AlarmListAdapter alarmListAdapter;
	private ListView list;
	private TextView textViewEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.main);
		dbAdapter = new DbAdapter(this);
		list = (ListView) findViewById(R.id.list);
		textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
		list.setEmptyView(textViewEmpty);
		// initializeView();
	}

	private void initializeView() {}
	
	@Override
	protected void onResume() {
		List<AlarmModel> alarmModels = dbAdapter.fetchAll();
		alarmListAdapter = new AlarmListAdapter(this, alarmModels);

		
		list.setAdapter(alarmListAdapter);
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add) {
			goTOAddAlarmActivity();
		}
		return super.onOptionsItemSelected(item);
	}

	private void goTOAddAlarmActivity() {
		Intent intent = new Intent(this, AddAlarmActivity.class);
		startActivity(intent);
		
	}
	
	public void deleteAlarmFromList(long id){
		
	}
}
