package com.example.yuzhong.myruns4;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Yuzhong on 2016/4/19.
 */
public class HistoryEntry {
    private Long id;

    private String mInputType;        // Manual, GPS or automatic
    private String mActivityType;     // Running, cycling etc.
    private String mDateTime;    // When does this entry happen
    private int mDuration;         // Exercise duration in seconds
    private double mDistance;      // Distance traveled. Either in meters or feet.
    private double mAvgPace;       // Average pace
    private double mAvgSpeed;      // Average speed
    private int mCalorie;          // Calories burnt
    private double mClimb;         // Climb. Either in meters or feet.
    private int mHeartRate;        // Heart rate
    private String mComment;       // Comments
    private ArrayList<LatLng> mLocationList; // Location list

    //generate all getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getmInputType() {
        return mInputType;
    }

    public void setmInputType(String mInputType) {
        this.mInputType = mInputType;
    }

    public String getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(String mActivityType) {
        this.mActivityType = mActivityType;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public int getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(int mCalorie) {
        this.mCalorie = mCalorie;
    }

    public ArrayList<LatLng> getmLocationList() {
        return mLocationList;
    }

    public void setmLocationList(LatLng mLocation) {
        this.mLocationList.add(mLocation);
    }

    public double getmClimb() {

        return mClimb;
    }

    public void setmClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }

    public byte[] getmLocationByteArray() {
        double[] locationCoordinates = new double[mLocationList.size() * 2];

        int count = 0;
        for( LatLng location: mLocationList) {
            locationCoordinates[count] = location.latitude;
            count++;
            locationCoordinates[count] = location.longitude;
            count++;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(locationCoordinates.length
                * Double.SIZE);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        doubleBuffer.put(locationCoordinates);

        return byteBuffer.array();
    }

    public void setmLocationListFromByteArray(byte[] bytesLocationCoordinates) {

        if(bytesLocationCoordinates.length == 0) return;

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytesLocationCoordinates);
        DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        double[] locationCoordinates = new double[bytesLocationCoordinates.length / Double.SIZE];
        doubleBuffer.get(locationCoordinates);

        int CoordinatesListSize = locationCoordinates.length / 2;
        for (int i = 0; i < CoordinatesListSize; i++) {
            LatLng latLng = new LatLng(locationCoordinates[i * 2], locationCoordinates[i * 2 + 1]);
            mLocationList.add(latLng);
        }
    }

    public HistoryEntry() {
        this.mInputType = "Manual Input";
        this.mActivityType = "Running";
        this.mDateTime = new SimpleDateFormat("HH:mm:ss MMM dd yyyy").format(new Date());
        this.mDuration = 0;
        this.mDistance = 0.0;
        this.mAvgPace = 0.0;
        this.mAvgSpeed = 0.0;
        this.mCalorie = 0;
        this.mClimb = 0.0;
        this.mHeartRate = 0;
        this.mComment = "";
        this.mLocationList = new ArrayList<LatLng>();
    }


}
