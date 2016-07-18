package com.example.roman.echoparkrecorder.Recording.audio;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.Recorder;

/**
 * Created by roman on 6/13/16.
 */
public class AudioRecorderHandler extends HandlerThread implements Recorder {

    //audio recorder
    private WavAudioRecorder myAudioRecorder;

    private Handler mHandler;

    public AudioRecorderHandler(){
        super("AudioRecorderThread", Process.THREAD_PRIORITY_AUDIO);
        //initialize media recorder
        myAudioRecorder= WavAudioRecorder.getInstance();
    }
    private  void initializeMediaRecorder(String audioFilePath){
        //append the filename to the internal storage path - this is where the output will go
        myAudioRecorder.setOutputFile(audioFilePath);
        myAudioRecorder.prepare();
    }

    @Override
    public void startRecording(String audioFilePath){
        if(myAudioRecorder.getState() == WavAudioRecorder.State.STOPPED){
            myAudioRecorder.reset();
        }
        if(myAudioRecorder.getState() == WavAudioRecorder.State.ERROR){
            myAudioRecorder = WavAudioRecorder.getInstance();
        }
        initializeMediaRecorder(audioFilePath);
        LogUtils.d("before start" +myAudioRecorder.getState());
        myAudioRecorder.start();
        LogUtils.d("after start" +myAudioRecorder.getState());
    }

    @Override
    public void stopRecording(){
        LogUtils.d("before stop" +myAudioRecorder.getState());
        myAudioRecorder.stop();
        LogUtils.d("after stop"+myAudioRecorder.getState());
    }


    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }
}
