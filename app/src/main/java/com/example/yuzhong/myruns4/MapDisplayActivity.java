package com.example.yuzhong.myruns4;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapDisplayActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection {

    private static final String TAG = "Test";
    private GoogleMap mMap;
    private String activityType;
    private String inputType;
    private Marker whereAmI;
    private TrackingService mTrackingService;
    private HistoryEntry mHistoryEntry;

    private boolean mIsBound;
    private TextView myLocationText;
    private Marker mBegin;
    private Marker mEnd;
    private Polyline route;
    private long mStartTime;

    private double mAverageSpeed;
    private String unit1 = "";
    private String unit2 = "";
    private double mCurrentSpeed;
    private double mClimb;
    private double mDistance;

    private final double MILESCONVERTTOKILOMETERS = 1.609344;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        Intent i = new Intent(this, TrackingService.class);
        startService(i);
        automaticBind();

        //set up broadcast filter
        IntentFilter filter = new IntentFilter("Update");
        registerReceiver(receiver, filter);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        setUpMapIfNeeded();

        Intent intent = getIntent();
        activityType = intent.getStringExtra("activity_type");
        inputType = intent.getStringExtra("input_type");
        myLocationText = (TextView) findViewById(R.id.myLocationText);

        route = mMap.addPolyline(new PolylineOptions()
//                    .width(_strokeWidth)
//                    .color(_pathColor)
//                    .geodesic(true)
//                    .zIndex(z)
        );

        mStartTime = System.currentTimeMillis();

        mIsBound = false;
    }

    /**
     * Check if the service is running. If the service is running when the
     * activity starts, we want to automatically bind to it.
     */
    private void automaticBind() {
        doBindService();
    }

    /**
     * Bind this Activity to TimerService
     */
    private void doBindService() {
        Log.d(TAG, "C:doBindService()");
        bindService(new Intent(this, TrackingService.class), this,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected");
        // get the service reference
        TrackingService.TrackingBinder binder = (TrackingService.TrackingBinder) service;
        mIsBound = true;
        mTrackingService = binder.getService();
        mHistoryEntry = mTrackingService.getHistoryEntry();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public static LatLng fromLocationToLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }



    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                // Configure the map display options

            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BroadcastReceiver receiver= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(	mIsBound)
                drawTraceOnMap();
        }
    };

    private void drawTraceOnMap(){
        ArrayList<LatLng> locationCoordinatesList = mHistoryEntry.getmLocationList();
        String unit = PreferenceManager.getDefaultSharedPreferences(
                this).getString("list_preference", "Miles");

        if(locationCoordinatesList.size() != 0){
            LatLng beginLatlng = locationCoordinatesList.get(0);
            LatLng endLatlng = locationCoordinatesList.get(locationCoordinatesList.size() - 1);

            route.setPoints(locationCoordinatesList);
            if(mBegin == null){
                mBegin = mMap.addMarker(new MarkerOptions().position(beginLatlng).icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(beginLatlng, 17));
            }
            if (mEnd != mBegin) {
                if (mEnd == null)
                    mEnd = mMap.addMarker(new MarkerOptions().position(endLatlng).icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED)));
                else mEnd.setPosition(endLatlng);
            }
        }

        mAverageSpeed = mHistoryEntry.getmDistance() / (((double) (System.currentTimeMillis() - mStartTime)) / 3600000);
        mHistoryEntry.setmAvgSpeed(mAverageSpeed);
        mCurrentSpeed = mTrackingService.getCurSpeed();
        mClimb = mHistoryEntry.getmClimb();
        mDistance = mHistoryEntry.getmDistance();

        if(unit.equals("Metric(Kilometers)")){
            unit1 = " km/h";
            unit2 = " Kilometers";
        } else {
            unit1 = " m/h";
            unit2 = " Miles";
            mAverageSpeed /= MILESCONVERTTOKILOMETERS;
            mCurrentSpeed /= MILESCONVERTTOKILOMETERS;
            mClimb /= MILESCONVERTTOKILOMETERS;
            mDistance /= MILESCONVERTTOKILOMETERS;
        }

        String formatSpeed = new DecimalFormat("###.##").format(mAverageSpeed);
        String formatCurSpeed = new DecimalFormat("#.##").format(mCurrentSpeed);
        String formatClimb = new DecimalFormat("#.##").format(mClimb);
        String calories = String.valueOf(mHistoryEntry.getmCalorie());
        String formatDistance = new DecimalFormat("#.##").format(mDistance);

        StringBuilder location = new StringBuilder();

        location.append("Type:"); location.append(activityType); location.append("\n");
        location.append("Avg speed: "); location.append(formatSpeed);  location.append(unit1); location.append("\n");
        location.append("Cur speed: "); location.append(formatCurSpeed); location.append(unit1); location.append("\n");
        location.append("Climb: "); location.append(formatClimb); location.append(unit2); location.append("\n");
        location.append("Calorie: "); location.append(calories); location.append("\n");
        location.append("Distance: "); location.append(formatDistance); location.append(unit2); location.append("\n");

        myLocationText.setText(location.toString());
    }


    public void onMapSaveClicked(View view) {
        String time = new SimpleDateFormat("HH:mm:ss MMM dd yyyy").format(new Date());
        mHistoryEntry = mTrackingService.getHistoryEntry();
        mHistoryEntry.setmDateTime(time);

        Log.d("InputType", inputType);

        mHistoryEntry.setmInputType(inputType);
        if(!inputType.equals("Automatic")) mHistoryEntry.setmActivityType(activityType);
        double duration = ((double)(System.currentTimeMillis() - mStartTime)) / (1000 * 60);
        mHistoryEntry.setmDistance(duration);

        SaveToDatabase saveToDatabase = new SaveToDatabase(this, mHistoryEntry);
        saveToDatabase.execute();

        unregisterReceiver(receiver);
        unbindService(this);
        stopService(new Intent(this, TrackingService.class));

        this.finish();
    }

    public void onMapCancelClicked(View view) {
        unregisterReceiver(receiver);
        unbindService(this);
        stopService(new Intent(this, TrackingService.class));

        this.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        LatLng l = mHistoryEntry.getmLocationList().get(0);
//        // Add a marker in Sydney and move the camera
//        mBegin = mMap.addMarker(new MarkerOptions().position(l).icon(BitmapDescriptorFactory.defaultMarker(
//                BitmapDescriptorFactory.HUE_GREEN)));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 17));
    }
}
