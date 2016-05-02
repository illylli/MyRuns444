package com.example.yuzhong.myruns4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

public class TrackingService extends Service {

    private static final String TAG = "Test";
    private NotificationManager mNotificationManager;
    private static boolean isRunning = false;

    // Binder given to clients
    private final IBinder mBinder = new TrackingBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    private HistoryEntry mHistoryEntry;

    private float mSpeed;
    private float mAverageSpeed;
    private float mClimb;
    private float mDistance;
    private int mCalories;
    private Location mLastLocation = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    public class TrackingBinder extends Binder {
        TrackingService getService() {
            // Return this instance of DownloadBinder so clients can call public methods
            return TrackingService.this;
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d("Hello", "lLLL " +  location.getLatitude());
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };

    public TrackingService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "S:onCreate(): Service Started.");

        LocationManager locationManager;
        String svcName= Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(true);
        criteria.setCostAllowed(true);

        String provider = locationManager.getBestProvider(criteria, true);

//        Location l = locationManager.getLastKnownLocation(provider);

        mHistoryEntry = new HistoryEntry();

//        updateWithNewLocation(l);

        locationManager.requestLocationUpdates(provider, 500, 0,
                locationListener);

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "S:onStartCommand(): Received start id " + startId + ": "
                + intent);
        showNotification();
        return START_STICKY; // Run until explicitly stopped.
    }

    /**
     * Display a notification in the notification bar.
     */
    private void showNotification() {
        //when touch it, you can get the data back
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MapDisplayActivity.class).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.service_label))
                .setContentText(
                        getResources().getString(R.string.service_started))
                .setSmallIcon(R.drawable.images)
                .setContentIntent(contentIntent).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(0, notification);

    }

    public HistoryEntry getHistoryEntry(){
        return  mHistoryEntry;
    }

    public static LatLng fromLocationToLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void updateWithNewLocation(Location location) {
        Log.d("Hello", "2LLL " +  location.getLatitude());
        if (location != null) {

            LatLng latlng = fromLocationToLatLng(location);

            mSpeed = location.getSpeed();

            if (mLastLocation != null) {
                mDistance += Math.abs(location.distanceTo(mLastLocation));
                mClimb += location.getAltitude() - mLastLocation.getAltitude();
            }

            mCalories = (int) (mDistance / 15.0);

            mLastLocation = location;

            mHistoryEntry.setmLocationList(latlng);
            mHistoryEntry.setmCalorie(mCalories);
            mHistoryEntry.setmClimb(mClimb / 1000);
            mHistoryEntry.setmDistance(mDistance / 1000);

            Intent intent = new Intent("Update");
            Log.d("Hello", "Send broadCast");
            sendBroadcast(intent);
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "S:onDestroy():Service Stopped");
        super.onDestroy();

        mNotificationManager.cancelAll(); // Cancel the persistent notification.

        isRunning = false;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public double getCurSpeed(){
        return mSpeed;
    }
}
