package com.example.yuzhong.myruns4;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Yuzhong on 2016/4/19.
 */
public class ReadFromDatabase extends AsyncTaskLoader<List<HistoryEntry>> {

    private HistoryDataSource mDataSource;
    private Context context;

    private static final String TAG = "AsyncTask";

    public ReadFromDatabase(Context context){
        super(context);
        this.context = context;
        mDataSource = new HistoryDataSource(context);
    }

    @Override
    public List<HistoryEntry> loadInBackground() {
        mDataSource.open();
        // read data from database
        List<HistoryEntry> list = mDataSource.getAllHistories();

        Log.d("CS65", "Read All:" + list.size());

        mDataSource.close();
        return list;
    }

}
