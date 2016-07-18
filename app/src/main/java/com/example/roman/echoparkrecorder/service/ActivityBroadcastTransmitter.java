package com.example.roman.echoparkrecorder.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by roman on 7/16/16.
 */
public class ActivityBroadcastTransmitter {
    private ActivityBroadcastReceiver mBroadcastReceiver;

    private Context mContext;

    private static final String ACTION_STRING_REQUEST_UPDATE = "REQUEST_UPDATE";
    private static final String ACTION_STRING_START_RECORDING = "START_RECORDING";
    private static final String ACTION_STRING_STOP_RECORDING = "STOP_RECORDING";



    public ActivityBroadcastTransmitter(Context context){
        mContext = context;


    }

    public boolean isServiceRunning(){
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ((RecordingService.class).getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void launchService() {
        mContext.startService(new Intent(mContext, RecordingService.class));
    }

    public void stopService() {
        mContext.stopService(new Intent(mContext, RecordingService.class));
    }

    public void startRecording() {
        Intent intent = new Intent();
        intent.setAction(ACTION_STRING_START_RECORDING);
        mContext.sendBroadcast(intent);
        LogUtils.d("startRecording");
    }

    public void stopRecording() {
        Intent intent = new Intent();
        intent.setAction(ACTION_STRING_STOP_RECORDING);
        mContext.sendBroadcast(intent);
        LogUtils.d("stopRecording");
    }

    public void requestStatusUpdate() {
        Intent intent = new Intent();
        intent.setAction(ACTION_STRING_REQUEST_UPDATE);
        mContext.sendBroadcast(intent);
        LogUtils.d("requestStatusUpdate");
    }
}