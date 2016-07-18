package com.example.roman.echoparkrecorder.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by roman on 7/16/16.
 */
public class ServiceBroadcastTransmitter {

    private Context mContext;
    private static final String ACTION_STRING_RECORDING_ON = "RECORDING_ON";
    private static final String ACTION_STRING_RECORDING_OFF = "RECORDING_OFF";

    public ServiceBroadcastTransmitter(Context context) {
        mContext = context;
    }

    public void sendRecordingStatusOn(){
        Intent intent = new Intent();
        intent.setAction(ACTION_STRING_RECORDING_ON);
        mContext.sendBroadcast(intent);
    }

    public void sendRecrdingStatusOff(){
        Intent intent = new Intent();
        intent.setAction(ACTION_STRING_RECORDING_OFF);
        mContext.sendBroadcast(intent);
    }

}
