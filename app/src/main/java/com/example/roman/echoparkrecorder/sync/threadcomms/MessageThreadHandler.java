package com.example.roman.echoparkrecorder.sync.threadcomms;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by roman on 7/26/16.
 */
public class MessageThreadHandler extends Handler {
    public static final int STOP_MSG = 1001;
    public static final int START_MSG = 1002;

    private NetworkMessenger mNetworkMessenger;

    public MessageThreadHandler(Looper looper, NetworkMessenger networkMessenger) {
        super(looper);
        mNetworkMessenger = networkMessenger;
    }

    @Override
    public void handleMessage(Message msg) {
        LogUtils.d("handleMessage");
        if(msg.what == START_MSG){
            LogUtils.d("START_MSG");
            mNetworkMessenger.sendStartMessage();
            return;
        }
        if (msg.what == STOP_MSG) {
            LogUtils.d("STOP_MSG");
            mNetworkMessenger.sendStopMessage();
        }
    }
}
