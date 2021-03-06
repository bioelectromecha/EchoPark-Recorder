package com.example.roman.echoparkrecorder.sync.networking.listeners;

import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.roman.echoparkrecorder.sync.networking.NetworkProtocol;
import com.example.roman.echoparkrecorder.sync.networking.SyncClient;

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
        LogUtils.d("connected");
    }

    @Override
    public void received(Connection connection, Object object) {
        LogUtils.d("received");
        if (object instanceof NetworkProtocol.CommandMessage) {
            NetworkProtocol.CommandMessage commandMessage = (NetworkProtocol.CommandMessage) object;
            if (commandMessage.command == NetworkProtocol.START_COMMAND) {
                LogUtils.d("received START_COMMAND");
                mSyncClient.setStartTimeOffset(commandMessage);
                mSyncClient.sendStartRecordingMessage();
                return;
            }
            if (commandMessage.command == NetworkProtocol.STOP_COMMAND) {
                LogUtils.d("received STOP_COMMAND");
                mSyncClient.sendStopRecordingMessage();
                return;
            }
        }
    }

    @Override
    public void disconnected(Connection connection) {
        LogUtils.d("disconnected");
        mSyncClient.sendStopRecordingMessage();
    }




}
