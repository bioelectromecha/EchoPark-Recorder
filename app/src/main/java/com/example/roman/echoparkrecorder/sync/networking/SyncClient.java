package com.example.roman.echoparkrecorder.sync.networking;


import android.os.HandlerThread;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Client;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;
import com.example.roman.echoparkrecorder.sync.threadcomms.MessageThreadHandler;
import com.example.roman.echoparkrecorder.sync.threadcomms.NetworkMessenger;


public class SyncClient extends HandlerThread implements NetworkMessenger {
    private MessageThreadHandler mUiThreadHandler;
    private Client mClient;
    private TimeKeeper mTimeKeeper;

    public SyncClient(MessageThreadHandler UiThreadHandler) {
        super("ClientThread", HandlerThread.MAX_PRIORITY);
        mUiThreadHandler = UiThreadHandler;
        mClient = new Client();
        mTimeKeeper = TimeKeeper.getInstance();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        init();
    }

    private void init(){
        LogUtils.d("init");
        //remeber that thread is already running, this is to start the client thread
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
        sendStopMessage();
        mClient.stop();
        return super.quit();
    }

    @Override
    public void sendStartMessage() {
        LogUtils.d("sendStartMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage(NetworkProtocol.START_COMMAND,mTimeKeeper.getTime());
        mClient.sendTCP(commandMessage);
    }

    @Override
    public void sendStopMessage() {
        LogUtils.d("sendStopMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage(NetworkProtocol.STOP_COMMAND,mTimeKeeper.getTime());
        mClient.sendTCP(commandMessage);
    }

    public void setStartTimeOffset(NetworkProtocol.CommandMessage commandMessage) {
        mTimeKeeper.setNewOffset(commandMessage.getServerTime());
    }

    public void sendStartRecordingMessage() {
        LogUtils.d("START COMMAND RECEIVED");
        Message message = new Message();
        message.what= MessageThreadHandler.START_MSG;
        mUiThreadHandler.sendMessage(message);
    }

    public void sendStopRecordingMessage(){
        LogUtils.d("STOP COMMAND RECEIVED");
        Message message = new Message();
        message.what= MessageThreadHandler.STOP_MSG;
        mUiThreadHandler.sendMessage(message);
    }


}
