package com.example.roman.echoparkrecorder.service.intentcomms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.roman.echoparkrecorder.service.listeners.ServiceStateListener;

/**
 * Created by roman on 7/16/16.
 */
public class ActivityBroadcastReceiver extends BroadcastReceiver {
    private IntentFilter mIntentFilter;
    private ServiceStateListener mListener;

    private static final String ACTION_STRING_RECORDING_ON = "RECORDING_ON";
    private static final String ACTION_STRING_RECORDING_OFF = "RECORDING_OFF";

    public ActivityBroadcastReceiver(ServiceStateListener listener) {
        mListener = listener;
        mIntentFilter = new IntentFilter(ACTION_STRING_RECORDING_ON);
        mIntentFilter.addAction(ACTION_STRING_RECORDING_OFF);
    }

    public IntentFilter getIntentFilter() {
        return mIntentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_STRING_RECORDING_ON)){
            mListener.setRecordingState(true);
            return;
        }
        if(intent.getAction().equals(ACTION_STRING_RECORDING_OFF)) {
            mListener.setRecordingState(false);
            return;
        }
    }
}
