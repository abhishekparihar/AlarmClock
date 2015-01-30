package com.abhi.alarmclock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHelper";
	private static final String DATABASE_NAME = "alarm_clock";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS alarm (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "alarm_name TEXT, " + "hours INTEGER, " + "minutes INTEGER, "+ "alarm_days TEXT, " + "is_enabled BOOLEAN)";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	private void createTable(SQLiteDatabase db) {
		try {
			Log.v(TAG, "Creating db: ");
			db.execSQL(DATABASE_CREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dropTable(SQLiteDatabase db, String table) {
		db.execSQL("DROP TABLE IF EXISTS " + table);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		this.dropTable(db, "alarm");
		createTable(db);
	}

}
