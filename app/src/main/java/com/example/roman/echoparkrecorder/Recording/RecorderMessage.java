package com.example.roman.echoparkrecorder.Recording;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by roman on 7/7/16.
 */
public class RecorderMessage implements Runnable {
    private Recorder mRecorder;

    private String mUrl;


    private RecorderMessage(Recorder recorder, String url){
        mRecorder = recorder;
        mUrl = url;
    }

    public static RecorderMessage getStartMessage(Recorder recorder, String url) {
        return new RecorderMessage( recorder,url);
    }

    public static RecorderMessage getStopMessage(Recorder recorder) {
        return new RecorderMessage( recorder,null);
    }


    @Override
    public void run() {
        if(mUrl != null){
            mRecorder.startRecording(mUrl);
        }else {
            mRecorder.stopRecording();
        }

    }
}
