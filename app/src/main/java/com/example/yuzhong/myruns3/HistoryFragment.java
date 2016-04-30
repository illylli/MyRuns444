package com.example.yuzhong.myruns3;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuzhong on 2016/4/7.
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<HistoryEntry>> {
    private ListView mHistoryEntryList;
    private MyAdapter myAdapter;
    private List<HistoryEntry> entryList;
    private final int DELETE_REQEST_CODE = 1;
    private final double MILESCONVERTTOKILOMETERS = 1.609344;


    // newInstance constructor for creating fragment with arguments
    public static HistoryFragment newInstance(int page, String title) {
        HistoryFragment fragmentHistory = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", 0);
        args.putString("someTitle", "HistoryFragment");
        fragmentHistory.setArguments(args);
        return fragmentHistory;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View root = inflater.inflate(R.layout.history_fragment, container, false);

        mHistoryEntryList = (ListView) root.findViewById(R.id.history_entries);
        myAdapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<HistoryEntry>());
        mHistoryEntryList.setAdapter(myAdapter);
        mHistoryEntryList.setOnItemClickListener(new ListEntry());
//        MyReceiver receiver = new MyReceiver();
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.intent.action.BOOT_COMPLETED");
//        registerReceiver(receiver, filter);
        Log.d("Create", "Load");
        getLoaderManager().initLoader(0, null, this).forceLoad();

        return root;
    }

    public class ListEntry implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView,View v, int pos, long arg3){
            Intent i;
            if(entryList.get(pos).getmInputType().equals("Manual Entry")) {
                i = new Intent(getActivity(), DisplayEntryActivity.class);
                i.putExtra("Id",entryList.get(pos).getId());
                i.putExtra("input_type", entryList.get(pos).getmInputType());
                i.putExtra("activity_type", entryList.get(pos).getmActivityType());
                i.putExtra("date_time", entryList.get(pos).getmDateTime().toString());
                int dur = (int) entryList.get(pos).getmDuration();
                int min = dur / 60;
                int sec = dur % 60;
                i.putExtra("duration", min  + "mins" + " " + sec + "secs");
                double distance = entryList.get(pos).getmDistance();
                String unit = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("list_preference", "Miles");
                if(unit.equals("Imperial(Miles)")) {
                    distance /= MILESCONVERTTOKILOMETERS;
                    unit = "Miles";
                }else{
                    unit = "Kilometers";
                }
                i.putExtra("distance", String.valueOf(new DecimalFormat("####.##").format(distance)) + " " + unit);
                i.putExtra("calories", entryList.get(pos).getmCalorie()+" cals");
                i.putExtra("heart_rate", entryList.get(pos).getmHeartRate()+" bpm");
                startActivityForResult(i, DELETE_REQEST_CODE);
            }
        }
    }

    @Override
    public Loader<List<HistoryEntry>> onCreateLoader(int id, Bundle args) {
        return new ReadFromDatabase(getActivity().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<HistoryEntry>> loader, List<HistoryEntry> data) {
        Log.d("CS65", data.size() + " size");
        myAdapter.clear();
        entryList = data;
        myAdapter.addAll(data);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<HistoryEntry>> loader) {
        Log.d("CS65", "RESET");
        myAdapter = new MyAdapter(getActivity().getApplicationContext(), new ArrayList<HistoryEntry>());
        mHistoryEntryList.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CS65", "On resume");
        refresh();
    }

    public void refresh(){
        this.getLoaderManager().restartLoader(0, null, this).forceLoad();
    }
}
