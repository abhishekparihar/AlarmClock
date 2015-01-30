package com.abhi.alarmclock.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.abhi.alarmclock.models.AlarmModel;

public class DbAdapter {
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	protected String dbName;
	protected String[] dbColumns = { "_id", "alarm_name", "hours", "minutes", "alarm_days", "is_enabled" };
	protected Context mContext;

	public DbAdapter(Context context) {
		super();
		this.mContext = context;
		dbHelper = new DatabaseHelper(context);
		open();
	}

	private void open() {
		db = dbHelper.getReadableDatabase();
	}

	final public void close() {
		if (!unopened()) {
			dbHelper.close();
		}
	}

	final public Boolean unopened() {
		return db == null || !db.isOpen();
	}

	public long create(AlarmModel alarmModel) {
		return db.insert("alarm", null, setContentValues(alarmModel));
	}

	public boolean update(Long rowId, AlarmModel alarmModel) {
		return db.update("alarm", setContentValues(alarmModel), "_id=" + rowId, null) > 0;
	}

	public final boolean delete(long id) {
		String where = "_id=" + id;
		return db.delete("alarm", where, null) > 0;
	}

	public final void delete() {
		db.delete("alarm", null, null);
	}

	public List<AlarmModel> fetchAll() {
		List<AlarmModel> alarms = new ArrayList<AlarmModel>();

		Cursor c = db.rawQuery("SELECT * FROM alarm", null);

		while (c.moveToNext()) {
			alarms.add(populateDataIntoModel(c));
		}
		if (!alarms.isEmpty()) {
			return alarms;
		}
		return null;
	}

	public AlarmModel fetch(long rowId) throws SQLException {
		Cursor c = db.query(true, "alarm", dbColumns, "_id=" + rowId, null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();
		}

		if (c.moveToNext()) {
			return populateDataIntoModel(c);
		}
		return null;
	}

	final public int getCount() {
		int cnt;

		Cursor c = db.rawQuery("SELECT count(*) AS our_count FROM " + dbName, null);

		if (c.moveToFirst()) {
			cnt = c.getInt(0);
		} else {
			cnt = 0;
		}

		c.close();

		return cnt;
	}

	final public void beginTransaction() {
		db.beginTransaction();
	}

	final public void endTransaction() {
		db.endTransaction();
	}

	final public void succeedTransaction() {
		db.setTransactionSuccessful();
	}

	private ContentValues setContentValues(AlarmModel alarmModel) {
		ContentValues values = new ContentValues();
		values.put("alarm_name", alarmModel.getName());
		values.put("hours", alarmModel.getHours());
		values.put("minutes", alarmModel.getMinutes());
		values.put("alarm_days", String.valueOf(alarmModel.getDays()));
		values.put("is_enabled", alarmModel.isEnabled);

		return values;

	}

	private AlarmModel populateDataIntoModel(Cursor c) {
		AlarmModel alarmModel = new AlarmModel();
		alarmModel.id = c.getLong(c.getColumnIndex("_id"));
		alarmModel.name = c.getString(c.getColumnIndex("alarm_name"));
		alarmModel.hours = c.getInt(c.getColumnIndex("hours"));
		alarmModel.minutes = c.getInt(c.getColumnIndex("minutes"));
		alarmModel.days = c.getLong(c.getColumnIndex("alarm_days"));
		alarmModel.isEnabled = c.getInt(c.getColumnIndex("is_enabled")) == 0 ? false : true;

		return alarmModel;
	}
}