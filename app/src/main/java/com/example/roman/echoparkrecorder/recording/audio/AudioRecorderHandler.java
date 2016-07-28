package com.example.roman.echoparkrecorder.recording.audio;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.Recorder;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hugo.weaving.DebugLog;

/**
 * Created by roman on 6/13/16.
 */
public class AudioRecorderHandler extends HandlerThread implements Recorder {

    //audio recorder
    private WavAudioRecorder myAudioRecorder;
    private Handler mHandler;


    private static final String METADATA_SUFFIX = ".metadata";

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
    @DebugLog
    public void startRecording(String audioFilePath){
        stampCmdMetadata(audioFilePath);
        if(myAudioRecorder.getState() == WavAudioRecorder.State.STOPPED){
            myAudioRecorder.reset();
        }else if(myAudioRecorder.getState() == WavAudioRecorder.State.ERROR){
            myAudioRecorder = WavAudioRecorder.getInstance();
        }
        initializeMediaRecorder(audioFilePath);
        myAudioRecorder.start();
        stampInitMetadata(audioFilePath);
    }

    private void stampCmdMetadata(String dataFilePath) {
        // the file we're going to write to
        dataFilePath.concat(METADATA_SUFFIX);
        File file = new File(dataFilePath);
        //write json to file
        try {
            String timeStamp = "cmdTime:" + String.valueOf(TimeKeeper.getInstance().getTime()) + "\r\n";
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(timeStamp.getBytes());
            outputStream.close();
        } catch (IOException e) {
            LogUtils.d("stampCmdMetadata failed");
            e.printStackTrace();
        }
    }
    private void stampInitMetadata(String dataFilePath) {
        // the file we're going to write to
        dataFilePath.concat(METADATA_SUFFIX);
        File file = new File(dataFilePath);
        //write json to file
        try {
            String timeStamp = "initTime:" + String.valueOf(TimeKeeper.getInstance().getTime()) + "\r\n";
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(timeStamp.getBytes());
            outputStream.close();
        } catch (IOException e) {
            LogUtils.d("stampInitMetadata failed");
            e.printStackTrace();
        }
    }

    @Override
    @DebugLog
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
