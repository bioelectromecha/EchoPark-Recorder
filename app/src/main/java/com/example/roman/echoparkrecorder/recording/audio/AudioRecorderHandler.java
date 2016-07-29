package com.example.roman.echoparkrecorder.recording.audio;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.Recorder;
import com.example.roman.echoparkrecorder.sync.TimeStamper;

import hugo.weaving.DebugLog;

/**
 * Created by roman on 6/13/16.
 */
public class AudioRecorderHandler extends HandlerThread implements Recorder {

    //audio recorder
    private WavAudioRecorder myAudioRecorder;
    private Handler mHandler;

    private TimeStamper mTimeStamper;
    private String mCurrentFilePath = "";

    //TODO: CHECK WHY THE HELL THE RECORDING IS NOT STOPPING AFTER ALL THE OTHER THREADS HAVE STOPPED
    public AudioRecorderHandler(){
        super("AudioRecorderThread", Process.THREAD_PRIORITY_DISPLAY);
        LogUtils.d("AudioRecorderHandler");
        //initialize media recorder
        myAudioRecorder= WavAudioRecorder.getInstance();
        mTimeStamper = new TimeStamper();

    }
    private  void initializeMediaRecorder(String audioFilePath){
        //append the filename to the internal storage path - this is where the output will go
        myAudioRecorder.setOutputFile(audioFilePath);
        myAudioRecorder.prepare();
    }

    @Override
    @DebugLog
    public void startRecording(String audioFilePath){
        LogUtils.d("startRecording");
        mCurrentFilePath = audioFilePath;
        mTimeStamper.stampCmdMetadata(TimeStamper.StampRecorderType.AUDIO);

        myAudioRecorder = WavAudioRecorder.getInstance();
        initializeMediaRecorder(audioFilePath);
        myAudioRecorder.start();

        mTimeStamper.stampInitMetadata(TimeStamper.StampRecorderType.AUDIO);
    }

    @Override
    @DebugLog
    public void stopRecording(){
        LogUtils.d("stopRecording");
        mTimeStamper.stampStopMetadata(TimeStamper.StampRecorderType.AUDIO);
        myAudioRecorder.stop();
        myAudioRecorder.release();
    }


    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }
}
