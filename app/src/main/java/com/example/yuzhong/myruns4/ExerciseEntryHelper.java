package com.example.yuzhong.myruns4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Yuzhong on 2016/4/19.
 */

public class ExerciseEntryHelper extends SQLiteOpenHelper {

	public static final String TABLE_COMMENTS = "history";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_INPUTTYPE = "input_type";
	public static final String COLUMN_ACTIVITY = "activity_type";
	public static final String COLUMN_DATETIME = "date_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_AVGPACE = "avg_pace";
	public static final String COLUMN_AVGSPEED = "avg_speed";
	public static final String COLUMN_CALORIES = "calories";
	public static final String COLUMN_CLIMB = "climb";
	public static final String COLUMN_HEARTRATE = "heartrate";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_GPS = "gps_data";

	private static final String DATABASE_NAME = "smartPhone.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_COMMENTS + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_INPUTTYPE + " TEXT NOT NULL, "
			+ COLUMN_ACTIVITY + " TEXT NOT NULL, "
			+ COLUMN_DATETIME + " DATETIME NOT NULL, "
			+ COLUMN_DURATION + " INTEGER NOT NULL, "
			+ COLUMN_DISTANCE + " FLOAT, "
			+ COLUMN_AVGPACE + " FLOAT, "
			+ COLUMN_AVGSPEED + " FLOAT, "
			+ COLUMN_CALORIES + " INTEGER, "
			+ COLUMN_CLIMB + " FLOAT, "
			+ COLUMN_HEARTRATE + " INTEGER, "
			+ COLUMN_COMMENT + " TEXT, "
			+ COLUMN_GPS + " BLOB );";

	public ExerciseEntryHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ExerciseEntryHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		onCreate(db);
	}

}