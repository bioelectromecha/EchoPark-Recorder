package com.example.roman.echoparkrecorder.sync.networking;

import android.os.Looper;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.service.listeners.RecordingStateListener;
import com.example.roman.echoparkrecorder.sync.threadcomms.MessageThreadHandler;
import com.example.roman.echoparkrecorder.sync.threadcomms.NetworkMessenger;

/**
 * Created by roman on 7/26/16.
 */
public class NetworkManager implements NetworkMessenger {
    private boolean mIsServer;
    private RecordingStateListener mRecordingStateListener;
    private MessageThreadHandler mHandler;
    private SyncClient mSyncClient;
    private SyncServer mSyncServer;

    public NetworkManager(boolean isServer, RecordingStateListener recordingStateListener) {
        mIsServer = isServer;
        mRecordingStateListener = recordingStateListener;
        mHandler = new MessageThreadHandler(Looper.getMainLooper(),this);
        mSyncClient = new SyncClient(mHandler);
        mSyncServer = new SyncServer(mHandler);
    }

    public void sendNetworkStartRecording(){
        LogUtils.d("sendNetworkStartRecording");
        if (mIsServer){
            MessageThreadHandler handler = new MessageThreadHandler(mSyncServer.getLooper(), mSyncServer);
            Message message = new Message();
            message.what = MessageThreadHandler.START_MSG;
            handler.sendMessage(message);
        }else{
            MessageThreadHandler handler = new MessageThreadHandler(mSyncClient.getLooper(), mSyncClient);
            Message message = new Message();
            message.what = MessageThreadHandler.STOP_MSG;
            handler.sendMessage(message);
        }
    }
    public void sendNetworkStopRecording(){
        LogUtils.d("sendNetworkStopRecording");
        if (mIsServer){
            MessageThreadHandler handler = new MessageThreadHandler(mSyncServer.getLooper(), mSyncServer);
            Message message = new Message();
            message.what = MessageThreadHandler.STOP_MSG;
            handler.sendMessage(message);
        }else{
            MessageThreadHandler handler = new MessageThreadHandler(mSyncClient.getLooper(), mSyncClient);
            Message message = new Message();
            message.what = MessageThreadHandler.STOP_MSG;
            handler.sendMessage(message);
        }
    }

    public void start() {
        LogUtils.d("start");
        if (mIsServer){
            mSyncServer.start();
            return;
        }
        if (!mIsServer) {
            mSyncClient.start();
            return;
        }
    }

    @Override
    public void sendStartMessage() {
        mRecordingStateListener.requestStartRecording();
    }

    @Override
    public void sendStopMessage() {
        mRecordingStateListener.requestStopRecording();
    }

    public void stop(){
        if (mIsServer){
            mSyncServer.quit();
            return;
        }
        if (!mIsServer) {
            mSyncClient.quit();
            return;
        }
    }
}

