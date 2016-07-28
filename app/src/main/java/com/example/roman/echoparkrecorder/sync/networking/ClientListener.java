package com.example.roman.echoparkrecorder.sync.networking;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Created by roman on 7/27/16.
 */
public class ClientListener extends Listener {
    private SyncClient mSyncClient;

    public ClientListener(SyncClient syncClient) {
        mSyncClient = syncClient;
    }

    @Override
    public void connected(Connection connection) {
        LogUtils.d("DISCONNECT");
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof NetworkProtocol.CommandMessage) {
            NetworkProtocol.CommandMessage commandMessage = (NetworkProtocol.CommandMessage) object;
            if (commandMessage.getCommand() == NetworkProtocol.START_COMMAND) {
                mSyncClient.setStartTimeOffset(commandMessage);
                mSyncClient.sendStartRecordingMessage();
                return;
            }
            if (commandMessage.getCommand() == NetworkProtocol.STOP_COMMAND) {
                mSyncClient.sendStopRecordingMessage();
                return;
            }
        }
    }

    @Override
    public void disconnected(Connection connection) {
        LogUtils.d("DISCONNECT");
        mSyncClient.sendStopRecordingMessage();
    }




}
