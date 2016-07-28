package com.example.roman.echoparkrecorder.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.MainActivity;
import com.example.roman.echoparkrecorder.R;
import com.example.roman.echoparkrecorder.recording.RecordingManager;
import com.example.roman.echoparkrecorder.service.intentcomms.ServiceBroadcastReceiver;
import com.example.roman.echoparkrecorder.service.intentcomms.ServiceBroadcastTransmitter;
import com.example.roman.echoparkrecorder.service.listeners.RecordingStateListener;
import com.example.roman.echoparkrecorder.sync.networking.NetworkManager;

import java.lang.reflect.Method;

public class RecordingService extends Service implements RecordingStateListener {

    private ServiceBroadcastReceiver mBroadcastReceiver;
    private ServiceBroadcastTransmitter mServiceTransmitter;
    private static final int NOTIFICATION_ID = 1337;
    private RecordingManager mRecordingManager;
    private NetworkManager mNetworkManager;

    @Override
    public void onCreate() {
        LogUtils.d("");
        super.onCreate();
        mBroadcastReceiver = new ServiceBroadcastReceiver(this);
        registerReceiver(mBroadcastReceiver, mBroadcastReceiver.getIntentFilter());
        mRecordingManager = new RecordingManager(this);
        mServiceTransmitter = new ServiceBroadcastTransmitter(this);
        LogUtils.d("isWifiHotspotEnabled: " + isWifiHotspotEnabled());
        mNetworkManager = new NetworkManager(isWifiHotspotEnabled(),this);
        mNetworkManager.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("");
        showForegroundNotification("READY");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LogUtils.d("");
        mRecordingManager.stopRecording();
        unregisterReceiver(mBroadcastReceiver);
        mNetworkManager.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void requestNetworkStartRecording() {
        LogUtils.d("requestNetworkStartRecording");
        mNetworkManager.sendNetworkStartRecording();
    }

    @Override
    public void requestNetworkStopRecording() {
        LogUtils.d("requestNetworkStopRecording");
        mNetworkManager.sendNetworkStopRecording();
    }

    @Override
    public void requestStartRecording() {
        LogUtils.d("requestStartRecording");
        showForegroundNotification("RECORDING");
        mRecordingManager.startRecording();
    }

    @Override
    public void requestStopRecording() {
        LogUtils.d("requestStopRecording");
        mRecordingManager.stopRecording();
        showForegroundNotification("READY");
    }

    @Override
    public void requestUpdate() {
        LogUtils.d("requestUpdate");
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

    private boolean isWifiHotspotEnabled(){
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        final Method method;
        try {
            method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true); //in the case of visibility change in future APIs
            return (Boolean) method.invoke(wifiManager);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
