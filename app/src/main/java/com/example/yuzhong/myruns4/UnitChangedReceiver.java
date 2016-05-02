package com.example.yuzhong.myruns4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by varun on 1/20/15.
 */

public class UnitChangedReceiver extends BroadcastReceiver {
    //Receive broadcas
    private final int UNIT_CHANGED_CODE = 1;
    @Override
    public void onReceive(final Context context, Intent intent) {
        startPSM(context);
    }

    // start the stress meter
    private void startPSM(Context context) {
        Intent emaIntent = new Intent(context, HistoryFragment.class); //The activity you  want to start.
        emaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivityForResult(emaIntent, UNIT_CHANGED_CODE);
    }
}