package com.example.yuzhong.myruns4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity {
    private Thread deleteThread = null;
    private GoogleMap mMap;
    private long pos;
    private TextView myLocationText;
    private Marker mBegin;
    private Marker mEnd;
    private Polyline route;
    private ArrayList<LatLng> locationList = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent i = getIntent();
        pos = i.getLongExtra("Id", pos);
        StringBuilder location = new StringBuilder();
        String unit1 = i.getStringExtra("unit1");
        String unit2 = i.getStringExtra("unit2");
        location.append("Type: "); location.append(i.getStringExtra("activity_type")); location.append("\n");
        location.append("Avg speed: "); location.append(i.getStringExtra("avgSpeed"));  location.append("\n");
        location.append("Cur speed: "); location.append("n/a");  location.append("\n");
        location.append("Climb: "); location.append(i.getStringExtra("climb")); location.append("\n");
        location.append("Calorie: "); location.append(i.getStringExtra("calories")); location.append("\n");
        location.append("Distance: "); location.append(i.getStringExtra("distance")); location.append("\n");

        myLocationText = (TextView) findViewById(R.id.myLocationText2);
        myLocationText.setText(location.toString());

//        byte[] location_lists = i.getByteArrayExtra("location_list");
//        convertToLatLng(location_lists);

        Bundle bundle = i.getExtras();
        locationList = (ArrayList<LatLng>) bundle.get("location_list2");

        route = mMap.addPolyline(new PolylineOptions()
//                    .width(_strokeWidth)
//                    .color(_pathColor)
//                    .geodesic(true)
//                    .zIndex(z)
        );

        drawTraceOnMap();
    }

    private void drawTraceOnMap() {
        if (locationList.size() != 0) {
            LatLng beginLatlng = locationList.get(0);
            LatLng endLatlng = locationList.get(locationList.size() - 1);

            route.setPoints(locationList);
            if (mBegin == null) {
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
    }

    public void convertToLatLng(byte[] location_lists){
        if(location_lists.length == 0) return;

        ByteBuffer byteBuffer = ByteBuffer.wrap(location_lists);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        double[] locationCoordinates = new double[location_lists.length / Double.SIZE];
        doubleBuffer.get(locationCoordinates);

        int CoordinatesListSize = locationCoordinates.length / 2;
        for (int i = 0; i < CoordinatesListSize; i++) {
            LatLng latLng = new LatLng(locationCoordinates[i * 2], locationCoordinates[i * 2 + 1]);
            locationList.add(latLng);
        }
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
                HistoryDataSource historydata = new HistoryDataSource(MapActivity.this);
                deleteThread = new DeleteFromDatabase(pos, historydata);
                deleteThread.start();
                this.finish();
                break;
        }
        return true;
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
}
