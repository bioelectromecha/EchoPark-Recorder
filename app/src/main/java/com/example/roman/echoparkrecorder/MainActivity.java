package com.example.roman.echoparkrecorder;

import android.location.LocationManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roman.echoparkrecorder.service.intentcomms.ActivityBroadcastReceiver;
import com.example.roman.echoparkrecorder.service.intentcomms.ActivityBroadcastTransmitter;
import com.example.roman.echoparkrecorder.service.listeners.ServiceStateListener;

public class MainActivity extends MyAppCompatActivity implements ServiceStateListener {

    private Button mButton;
    private LocationManager mLocationManager;
    private ActivityBroadcastTransmitter mBroadcastTransmitter;
    private ActivityBroadcastReceiver mBroadcastReceiver;
    private boolean mIsRecording = false;


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

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mButton = (Button) findViewById(R.id.button_record);
    }

    public void onClick(View view) {

        //ignore input if user is button mashing
        if (!clickTimeoutOverCheck()) {
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
        registerReceiver(mBroadcastReceiver, mBroadcastReceiver.getIntentFilter());
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
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setRecordingState(boolean state) {
        mIsRecording = state;
        resetButtonState();
    }

    private void resetButtonState() {
        if (mIsRecording) {
            mButton.setText(R.string.stop_recording);
        } else {
            mButton.setText(R.string.start_recording);
        }
    }
}