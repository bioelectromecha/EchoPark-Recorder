package com.example.roman.echoparkrecorder.sync.networking;


import android.os.HandlerThread;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Client;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;
import com.example.roman.echoparkrecorder.sync.TimeStamper;
import com.example.roman.echoparkrecorder.sync.networking.listeners.ClientListener;
import com.example.roman.echoparkrecorder.sync.threadcomms.MessageThreadHandler;
import com.example.roman.echoparkrecorder.sync.threadcomms.NetworkMessenger;


public class SyncClient extends HandlerThread implements NetworkMessenger {
    private MessageThreadHandler mUiThreadHandler;
    private Client mClient;
    private TimeKeeper mTimeKeeper;
    private TimeStamper mTimeStamper;

    public SyncClient(MessageThreadHandler UiThreadHandler) {
        super("ClientThread", HandlerThread.MAX_PRIORITY);
        mUiThreadHandler = UiThreadHandler;
        mClient = new Client();
        mTimeKeeper = TimeKeeper.getInstance();
        mTimeStamper = new TimeStamper();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        init();
    }

    private void init(){
        LogUtils.d("init");
        mClient.start();
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the mClient and server.
        NetworkProtocol.register(mClient);
        mClient.addListener(new ClientListener(this));
        try {
            //connect to server
            mClient.connect(NetworkProtocol.TIMEOUT_TIME, NetworkProtocol.HOTSPOT_IP, NetworkProtocol.PORT_NUMBER);
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean quit() {
        LogUtils.d("quit");
//        sendStopMessage();
//        mClient.stop();
        return super.quitSafely();
    }

    @Override
    public void sendStartMessage() {
        LogUtils.d("sendStartMessage");
        LogUtils.d(mClient.isConnected());
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage();
        commandMessage.command = NetworkProtocol.START_COMMAND;
        commandMessage.serverTime = mTimeKeeper.getTime();
        mClient.sendTCP(commandMessage);
    }

    @Override
    public void sendStopMessage() {
        LogUtils.d("sendStopMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage();
        commandMessage.command = NetworkProtocol.STOP_COMMAND;
        commandMessage.serverTime = mTimeKeeper.getTime();
        mClient.sendTCP(commandMessage);
    }

    public void setStartTimeOffset(NetworkProtocol.CommandMessage commandMessage) {
        mTimeKeeper.setNewOffset(commandMessage.serverTime);
        mTimeStamper.stampServerTimeOffsetLog();
    }

    public void sendStartRecordingMessage() {
        LogUtils.d("sendStartRecordingMessage");
        Message message = new Message();
        message.what= MessageThreadHandler.START_MSG;
        mUiThreadHandler.sendMessage(message);
    }

    public void sendStopRecordingMessage(){
        LogUtils.d("sendStopRecordingMessage");
        Message message = new Message();
        message.what= MessageThreadHandler.STOP_MSG;
        mUiThreadHandler.sendMessage(message);
    }


}
