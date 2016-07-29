package com.example.roman.echoparkrecorder.sync.networking;

import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Server;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;
import com.example.roman.echoparkrecorder.sync.TimeStamper;
import com.example.roman.echoparkrecorder.sync.networking.listeners.ServerListener;
import com.example.roman.echoparkrecorder.sync.threadcomms.MessageThreadHandler;
import com.example.roman.echoparkrecorder.sync.threadcomms.NetworkMessenger;


public class SyncServer extends HandlerThread implements NetworkMessenger {
        private MessageThreadHandler mUiThreadHandler;
        private Server mServer;
        private TimeKeeper mTimeKeeper;
        private TimeStamper mTimeStamper;

        public SyncServer(MessageThreadHandler uiThreadHandler) {
        super("ServerThread");
        mUiThreadHandler = uiThreadHandler;
        mServer = new Server();
        mTimeKeeper = TimeKeeper.getInstance();
        mTimeStamper = new TimeStamper();
    }

    @Override
    public boolean quit() {
        LogUtils.d("quit");
        sendStopMessage();
        mServer.stop();
        return super.quit();
    }

    @Override
    public void sendStartMessage() {
        LogUtils.d("sendStartMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage();
        commandMessage.command = NetworkProtocol.START_COMMAND;
        mTimeKeeper.setNewOffset(SystemClock.elapsedRealtime());
        commandMessage.serverTime = mTimeKeeper.getServerTime();
        mServer.sendToAllTCP(commandMessage);
        mTimeStamper.stampServerTimeOffsetLog();
        sendStartRecordingMessage();
    }

    @Override
    public void sendStopMessage() {
        LogUtils.d("sendStopMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage();
        commandMessage.command = NetworkProtocol.STOP_COMMAND;
        commandMessage.serverTime = mTimeKeeper.getServerTime();
        mServer.sendToAllTCP(commandMessage);
        sendStopRecordingMessage();
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

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        LogUtils.d("onLooperPrepared");
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and mServer.
        NetworkProtocol.register(mServer);
        mServer.addListener(new ServerListener(this));
        try {
            mServer.bind(NetworkProtocol.PORT_NUMBER);
            mServer.start();
        }catch (Exception e){
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }
    }
}
