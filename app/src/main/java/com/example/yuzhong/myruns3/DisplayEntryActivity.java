package com.example.yuzhong.myruns3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayEntryActivity extends Activity {


    private TextView InputType;
    private TextView ActivityType;
    private TextView DateAndTime;
    private TextView Duration;
    private TextView Distance;
    private TextView Calories;
    private TextView HeartRate;
    private Thread deleteThread = null;
    private long pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        InputType = (TextView) findViewById(R.id.entry_input);
        ActivityType = (TextView) findViewById(R.id.entry_activity_type);
        DateAndTime = (TextView) findViewById(R.id.entry_date_and_time);
        Duration = (TextView) findViewById(R.id.entry_duration);
        Distance = (TextView) findViewById(R.id.entry_distance);
        Calories = (TextView) findViewById(R.id.entry_calories);
        HeartRate = (TextView) findViewById(R.id.entry_heart);

        Intent i = getIntent();


        InputType.setText(i.getStringExtra("input_type"));
        ActivityType.setText(i.getStringExtra("activity_type"));
        DateAndTime.setText(i.getStringExtra("date_time"));
        Duration.setText(i.getStringExtra("duration"));
        Distance.setText(i.getStringExtra("distance"));
        Calories.setText(i.getStringExtra("calories"));
        HeartRate.setText(i.getStringExtra("heart_rate"));
        pos = i.getLongExtra("Id", pos);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                // this.setResult(1);
                HistoryDataSource historydata = new HistoryDataSource(DisplayEntryActivity.this);
                deleteThread = new DeleteFromDatabase(pos, historydata);
                deleteThread.start();
                this.finish();
                break;
        }
        return true;
    }

//    private void DeleteNote() {
//        final HistoryDataSource historydata = new HistoryDataSource(DisplayEntryActivity.this);
//        new AsyncTask<Long, Void, Void>() {
//            @Override
//            protected Void doInBackground(Long... params) {
//                Log.d("illy", "before doing background");
//                historydata.open();
//                historydata.deleteHistory(params[0]);
//                historydata.close();
//                Log.d("illy", "done in background");
//                return null;
//            }
//        }.execute(new Long[]{pos});
//    }
}
