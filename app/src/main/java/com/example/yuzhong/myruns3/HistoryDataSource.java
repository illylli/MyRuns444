package com.example.yuzhong.myruns3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataSource {

	// Database fields
	private SQLiteDatabase database;
	private ExerciseEntryHelper dbHelper;
	private String[] allColumns = { ExerciseEntryHelper.COLUMN_ID, ExerciseEntryHelper.COLUMN_INPUTTYPE,
			ExerciseEntryHelper.COLUMN_ACTIVITY, ExerciseEntryHelper.COLUMN_DATETIME, ExerciseEntryHelper.COLUMN_DURATION,
			ExerciseEntryHelper.COLUMN_DISTANCE, ExerciseEntryHelper.COLUMN_AVGPACE, ExerciseEntryHelper.COLUMN_AVGSPEED,
			ExerciseEntryHelper.COLUMN_CALORIES, ExerciseEntryHelper.COLUMN_CLIMB, ExerciseEntryHelper.COLUMN_HEARTRATE,
			ExerciseEntryHelper.COLUMN_COMMENT};

	private static final String TAG = "DBDEMO";

	public HistoryDataSource(Context context) {
		dbHelper = new ExerciseEntryHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

    //add one history to database
	public HistoryEntry createHistory(HistoryEntry historyEntry) {
		ContentValues values = new ContentValues();

        //get one historyEntry values
        values.put(ExerciseEntryHelper.COLUMN_INPUTTYPE, historyEntry.getmInputType());
        values.put(ExerciseEntryHelper.COLUMN_ACTIVITY, historyEntry.getmActivityType());
        values.put(ExerciseEntryHelper.COLUMN_DATETIME, historyEntry.getmDateTime());
        values.put(ExerciseEntryHelper.COLUMN_DURATION, historyEntry.getmDuration());
        values.put(ExerciseEntryHelper.COLUMN_DISTANCE, historyEntry.getmDistance());
        values.put(ExerciseEntryHelper.COLUMN_AVGPACE, historyEntry.getmAvgPace());
        values.put(ExerciseEntryHelper.COLUMN_AVGSPEED, historyEntry.getmAvgSpeed());
        values.put(ExerciseEntryHelper.COLUMN_CALORIES, historyEntry.getmCalorie());
        values.put(ExerciseEntryHelper.COLUMN_CLIMB, historyEntry.getmClimb());
        values.put(ExerciseEntryHelper.COLUMN_HEARTRATE, historyEntry.getmHeartRate());
        values.put(ExerciseEntryHelper.COLUMN_COMMENT, historyEntry.getmComment());

		long insertId = database.insert(ExerciseEntryHelper.TABLE_COMMENTS, null,
				values);
		Cursor cursor = database.query(ExerciseEntryHelper.TABLE_COMMENTS,
				allColumns, ExerciseEntryHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
        HistoryEntry newHistoryEntry = cursorToHistroyEntry(cursor);

		// Log the comment stored
		Log.d(TAG, "HistoryEntry = " + cursorToHistroyEntry(cursor).toString()
				+ " insert ID = " + insertId);

		cursor.close();
		return newHistoryEntry;
	}

//	public void deleteHistory(HistoryEntry histroyEntry) {
//		long id = histroyEntry.getId();
//		Log.d(TAG, "delete histroyEntry = " + id);
//		System.out.println("HistoryEntry deleted with id: " + id);
//		database.delete(ExerciseEntryHelper.TABLE_COMMENTS, ExerciseEntryHelper.COLUMN_ID
//				+ " = " + id, null);
//	}
	public void deleteHistory(long id){
		database.delete(ExerciseEntryHelper.TABLE_COMMENTS, ExerciseEntryHelper.COLUMN_ID
				+ " = " + id, null);
	}
	
	public void deleteAllHistories() {
		System.out.println("HistroyEntries deleted all");
		Log.d(TAG, "delete all = ");
		database.delete(ExerciseEntryHelper.TABLE_COMMENTS, null, null);
	}
	
	public List<HistoryEntry> getAllHistories() {
		List<HistoryEntry> historyEntries = new ArrayList<HistoryEntry>();

		Cursor cursor = database.query(ExerciseEntryHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
            HistoryEntry historyEntry = cursorToHistroyEntry(cursor);
			Log.d(TAG, "get comment = " + cursorToHistroyEntry(cursor).toString());
			historyEntries.add(historyEntry);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return historyEntries;
	}

	private HistoryEntry cursorToHistroyEntry(Cursor cursor) {
        HistoryEntry historyEntry = new HistoryEntry();
        historyEntry.setId(cursor.getLong(0));
        historyEntry.setmInputType(cursor.getString(1));
        historyEntry.setmActivityType(cursor.getString(2));
        historyEntry.setmDateTime(cursor.getString(3));
        historyEntry.setmDuration(cursor.getInt(4));
        historyEntry.setmDistance(cursor.getFloat(5));
        historyEntry.setmAvgPace(cursor.getFloat(6));
        historyEntry.setmAvgSpeed(cursor.getFloat(7));
        historyEntry.setmCalorie(cursor.getInt(8));
        historyEntry.setmClimb(cursor.getFloat(9));
        historyEntry.setmHeartRate(cursor.getInt(10));
        historyEntry.setmComment(cursor.getString(11));
		return historyEntry;
	}
}