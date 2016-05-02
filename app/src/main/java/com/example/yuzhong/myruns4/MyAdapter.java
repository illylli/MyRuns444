package com.example.yuzhong.myruns4;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Yuzhong on 2016/4/20.
 */
public class MyAdapter extends ArrayAdapter<HistoryEntry> {

    private Context context;
    private ArrayList<HistoryEntry> mHistoryEntries;
    private final double MILESCONVERTTOKILOMETERS = 1.609344;

    public MyAdapter(Context context, ArrayList<HistoryEntry> mHistoryEntries) {
        super(context, android.R.layout.two_line_list_item, mHistoryEntries);
        this.context = context;
        this.mHistoryEntries = mHistoryEntries;
    }

    @Override
    public int getCount() {
        return mHistoryEntries.size();
    }

    @Override
    public HistoryEntry getItem(int position) {
        return mHistoryEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View twoLineListItem;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = inflater.inflate(
                    android.R.layout.two_line_list_item, null);
        } else {
            twoLineListItem = convertView;
        }

        TextView text1 = (TextView) twoLineListItem.findViewById(android.R.id.text1);
        TextView text2 = (TextView) twoLineListItem.findViewById(android.R.id.text2);

        text1.setTextColor(Color.GRAY);
        text2.setTextColor(Color.GRAY);
        text1.setText(getFirstRow(mHistoryEntries.get(position)));
        text2.setText("" + getSecondRow(mHistoryEntries.get(position)));

        return twoLineListItem;
    }

    public String getFirstRow(HistoryEntry historyEntry){
        return "Manual Entry: " + historyEntry.getmActivityType() + ", " + historyEntry.getmDateTime();
    }

    public String getSecondRow(HistoryEntry historyEntry){
        int dur = (int)historyEntry.getmDuration();
        int min = dur / 60;
        int sec = dur % 60;
        StringBuilder finalString = new StringBuilder();
        double distance = historyEntry.getmDistance();
        String unit = PreferenceManager.getDefaultSharedPreferences(context).getString("list_preference", "Miles");
        if(unit.equals("Imperial(Miles)")) {
            distance /= MILESCONVERTTOKILOMETERS;
            unit = "Miles";
        }else{
            unit = "Kilometers";
        }
        finalString.append(String.valueOf(new DecimalFormat("####.##").format(distance)));
        finalString.append(" ");
        finalString.append(unit);
        finalString.append(", ");
        finalString.append(min);
        finalString.append("mins ");
        finalString.append(sec);
        finalString.append("secs");

        return finalString.toString();
    }
}
