package com.example.roman.echoparkrecorder.Recording.data;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.Recorder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by roman on 6/13/16.
 */
public class DataRecorderHandler extends HandlerThread implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        Recorder {

    // location services stuff
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation = null;
    private final int LOCATION_UPDATE_INTERVAL = 1000  ;

    private JSONDataRecorder mJSONdataRecorder;

    private String mDataFilePath="";

    private Handler mHandler;

    public DataRecorderHandler(Context mContext){
        super("DataRecorderThread");
        mJSONdataRecorder = new JSONDataRecorder();

        // bind the google api client - for user location
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void startRecording(String dataFilePath){
        mDataFilePath = dataFilePath;
        //connect the api
        mGoogleApiClient.connect();
    }

    @Override
    public void stopRecording(){
        // disconnect the api
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        mJSONdataRecorder.recordLocation(mLastLocation,mDataFilePath);
        LogUtils.d("written some data just now");
    }

    @Override
    public void onConnected(Bundle bundle) {

        updateLastLocation();

        if (mLastLocation != null) {
            mJSONdataRecorder.recordLocation(mLastLocation,mDataFilePath);
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void updateLastLocation() {
        try {
            //get the last location of the device. This is mostly for before getting actual GPS position
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //if fine location permission not granted
        } catch (SecurityException e) {
            LogUtils.d("LOCATION PERMISSION NOT GRANTED");
        }
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }


}
