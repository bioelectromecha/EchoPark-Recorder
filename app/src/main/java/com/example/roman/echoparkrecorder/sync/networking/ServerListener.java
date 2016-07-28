package com.example.roman.echoparkrecorder.sync.networking;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
        LogUtils.d("SERVER CONNECTED");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof NetworkProtocol.CommandMessage) {
            NetworkProtocol.CommandMessage commandMessage = (NetworkProtocol.CommandMessage) object;
            if (commandMessage.getCommand() == NetworkProtocol.START_COMMAND) {
                mSyncServer.sendStartMessage();
                mSyncServer.sendStartRecordingMessage();
                return;
            }
            if (commandMessage.getCommand() == NetworkProtocol.STOP_COMMAND) {
                mSyncServer.sendStopMessage();
                mSyncServer.sendStopRecordingMessage();
                return;
            }
        }
    }
    @Override
    public void disconnected(Connection connection) {
        LogUtils.d("DISCONNECT");
        mSyncServer.sendStopRecordingMessage();
    }




}