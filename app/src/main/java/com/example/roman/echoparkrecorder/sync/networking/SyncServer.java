package com.example.roman.echoparkrecorder.sync.networking;

import android.os.HandlerThread;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Server;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;
import com.example.roman.echoparkrecorder.sync.threadcomms.MessageThreadHandler;
import com.example.roman.echoparkrecorder.sync.threadcomms.NetworkMessenger;


public class SyncServer extends HandlerThread implements NetworkMessenger {
        private MessageThreadHandler mUiThreadHandler;
        private Server mServer;
        private TimeKeeper mTimeKeeper;

        public SyncServer(MessageThreadHandler uiThreadHandler) {
        super("ServerThread", HandlerThread.MAX_PRIORITY);
        mUiThreadHandler = uiThreadHandler;
        mServer = new Server();
        mTimeKeeper = TimeKeeper.getInstance();
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
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage(NetworkProtocol.START_COMMAND,mTimeKeeper.getTime());
        mServer.sendToAllTCP(commandMessage);
    }

    @Override
    public void sendStopMessage() {
        LogUtils.d("sendStopMessage");
        NetworkProtocol.CommandMessage commandMessage = new NetworkProtocol.CommandMessage(NetworkProtocol.STOP_COMMAND,mTimeKeeper.getTime());
        mServer.sendToAllTCP(commandMessage);
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
