package com.example.roman.echoparkrecorder.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.roman.echoparkrecorder.MainActivity;
import com.example.roman.echoparkrecorder.R;
import com.example.roman.echoparkrecorder.Recording.RecordingManager;

public class RecordingService extends Service implements RecordingStateListener{

    private ServiceBroadcastReceiver mBroadcastReceiver;
    private ServiceBroadcastTransmitter mServiceTransmitter;
    private static final int NOTIFICATION_ID = 1;
    private RecordingManager mRecordingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcastReceiver = new ServiceBroadcastReceiver(this);
        registerReceiver(mBroadcastReceiver, mBroadcastReceiver.getIntentFilter());
        mRecordingManager = new RecordingManager(this);
        mServiceTransmitter = new ServiceBroadcastTransmitter(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showForegroundNotification("READY");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mRecordingManager.stopRecording();
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void requestStartRecording() {
        showForegroundNotification("RECORDING");
        mRecordingManager.startRecording();
    }

    @Override
    public void requestStopRecording() {
        mRecordingManager.stopRecording();
        showForegroundNotification("READY");
    }

    @Override
    public void requestUpdate() {
        if(mRecordingManager.getRecordingState()){
            mServiceTransmitter.sendRecordingStatusOn();
        }else {
            mServiceTransmitter.sendRecrdingStatusOff();
        }
    }

    private void showForegroundNotification(String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }


}
