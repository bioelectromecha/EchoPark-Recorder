package com.example.roman.echoparkrecorder.Recording.audio;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.provider.Settings;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.Recorder;

/**
 * Created by roman on 6/13/16.
 */
public class AudioRecorderHandler extends HandlerThread implements Recorder {

    //audio recorder
    private WavAudioRecorder myAudioRecorder;
    private Handler mHandler;

    //TODO: CHECK WHY THE HELL THE RECORDING IS NOT STOPPING AFTER ALL THE OTHER THREADS HAVE STOPPED
    public AudioRecorderHandler(){
        super("AudioRecorderThread", Process.THREAD_PRIORITY_DISPLAY);
        LogUtils.d("AudioRecorderHandler");
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
        LogUtils.d("startRecording "+ System.currentTimeMillis());
        if(myAudioRecorder.getState() == WavAudioRecorder.State.STOPPED){
            myAudioRecorder.reset();
        }else if(myAudioRecorder.getState() == WavAudioRecorder.State.ERROR){
            myAudioRecorder = WavAudioRecorder.getInstance();
        }
        initializeMediaRecorder(audioFilePath);
        myAudioRecorder.start();
    }

    @Override
    public void stopRecording(){
        LogUtils.d("stopRecording "+ System.currentTimeMillis());
        myAudioRecorder.stop();
    }


    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }
}
