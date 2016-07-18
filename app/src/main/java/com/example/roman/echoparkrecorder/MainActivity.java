package com.example.roman.echoparkrecorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.service.ActivityBroadcastReceiver;
import com.example.roman.echoparkrecorder.service.ActivityBroadcastTransmitter;
import com.example.roman.echoparkrecorder.service.ServiceStateListener;

public class MainActivity extends AppCompatActivity implements ServiceStateListener{

    private Button mButton;
    private LocationManager mLocationManager;
    private ActivityBroadcastTransmitter mBroadcastTransmitter;
    private ActivityBroadcastReceiver mBroadcastReceiver;
    private boolean mIsRecording = false;


    // stuff to protect against button mashing
    private static final long CLICK_TIMEOUT = 500; //milliseconds
    private static long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBroadcastTransmitter = new ActivityBroadcastTransmitter(this);
        if (!mBroadcastTransmitter.isServiceRunning()) {
            mBroadcastTransmitter.launchService();
        }

        mBroadcastReceiver = new ActivityBroadcastReceiver(this);
        registerReceiver(mBroadcastReceiver, mBroadcastReceiver.getIntentFilter());
        mBroadcastTransmitter.requestStatusUpdate();

        mLocationManager =  (LocationManager) getSystemService(LOCATION_SERVICE);
        mButton = (Button) findViewById(R.id.button_record);
    }

    public void onClick(View view){

        //ignore input if user is button mashing
        if(!clickTimeoutOverCheck()){
            return;
        }

        if (mIsRecording) {
            mBroadcastTransmitter.stopRecording();
            mButton.setText(R.string.start_recording);
            mIsRecording = false;
            Toast.makeText(MainActivity.this, R.string.recording_stopped, Toast.LENGTH_SHORT).show();
            return;
        }
        //if not recording
        if (!isGpsAvailable()) {
            buildAlertMessageNoGps();
            return;
        }
        mBroadcastTransmitter.startRecording();
        mButton.setText(R.string.stop_recording);
        mIsRecording = true;
        Toast.makeText(MainActivity.this, R.string.recording_started, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        registerReceiver(mBroadcastReceiver,mBroadcastReceiver.getIntentFilter());
        mBroadcastTransmitter.requestStatusUpdate();

        if (!mBroadcastTransmitter.isServiceRunning()) {
            mBroadcastTransmitter.launchService();
        }

        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mIsRecording) {
            mBroadcastTransmitter.stopService();
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    private boolean isGpsAvailable() {
//        TODO: NO GPS NO RECORDING!
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }else{
            return false;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void setRecordingState(boolean state) {
        mIsRecording = state;
        resetButtonState();
    }

    private void resetButtonState() {
        if (mIsRecording) {
            mButton.setText(R.string.stop_recording);
        }else{
            mButton.setText(R.string.start_recording);
        }
    }

    /**
     * key mashing protection helper method
     * @return true if click timeout has passed, false otherwise
     */
    private boolean clickTimeoutOverCheck() {
        if( System.currentTimeMillis() < mLastClickTime + CLICK_TIMEOUT ){
            return false;
        }else{
            mLastClickTime = System.currentTimeMillis();
            return true;
        }
    }
}