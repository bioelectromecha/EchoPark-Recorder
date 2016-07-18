package com.example.roman.echoparkrecorder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by roman on 7/16/16.
 */
public class ServiceBroadcastReceiver extends BroadcastReceiver {


    private static final String ACTION_STRING_START_RECORDING = "START_RECORDING";
    private static final String ACTION_STRING_STOP_RECORDING = "STOP_RECORDING";
    private static final String ACTION_STRING_REQUEST_UPDATE = "REQUEST_UPDATE";

    private RecordingStateListener mListener;
    private IntentFilter mIntentFilter;

    public ServiceBroadcastReceiver(RecordingStateListener listener) {
        mListener = listener;
        mIntentFilter = new IntentFilter(ACTION_STRING_START_RECORDING);
        mIntentFilter.addAction(ACTION_STRING_STOP_RECORDING);
        mIntentFilter.addAction(ACTION_STRING_REQUEST_UPDATE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_STRING_START_RECORDING)){
            mListener.requestStartRecording();
            return;
        }
        if(intent.getAction().equals(ACTION_STRING_STOP_RECORDING)) {
            mListener.requestStopRecording();
            return;
        }
        if (intent.getAction().equals(ACTION_STRING_REQUEST_UPDATE)) {
            mListener.requestUpdate();
            return;
        }
    }

    public IntentFilter getIntentFilter() {
        return mIntentFilter;
    }
}
