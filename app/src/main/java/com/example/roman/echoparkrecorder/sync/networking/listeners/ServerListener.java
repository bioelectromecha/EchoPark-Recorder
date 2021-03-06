package com.example.roman.echoparkrecorder.sync.networking.listeners;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.roman.echoparkrecorder.sync.networking.NetworkProtocol;
import com.example.roman.echoparkrecorder.sync.networking.SyncServer;

/**
 * Created by roman on 7/27/16.
 */
public class ServerListener extends Listener {
    private SyncServer mSyncServer;
    public ServerListener(SyncServer syncServer) {
        mSyncServer = syncServer;
    }

    @Override
    public void connected(Connection connection) {
        LogUtils.d("connected");
    }

    @Override
    public void received(Connection connection, Object object) {
        LogUtils.d("received");
        if (object instanceof NetworkProtocol.CommandMessage) {
            LogUtils.d("object instanceof NetworkProtocol.CommandMessage");
            NetworkProtocol.CommandMessage commandMessage = (NetworkProtocol.CommandMessage) object;
            if (commandMessage.command == NetworkProtocol.START_COMMAND) {
                LogUtils.d("received START_COMMAND");
                mSyncServer.sendStartMessage();
                mSyncServer.sendStartRecordingMessage();
                return;
            }
            if (commandMessage.command == NetworkProtocol.STOP_COMMAND) {
                LogUtils.d("received1 STOP_COMMAND");
                mSyncServer.sendStopMessage();
                mSyncServer.sendStopRecordingMessage();
                return;
            }
        }else{
            LogUtils.d("received is not instance of anything we know");
        }
    }
    @Override
    public void disconnected(Connection connection) {
        LogUtils.d("disconnected");
        mSyncServer.sendStopRecordingMessage();
    }




}