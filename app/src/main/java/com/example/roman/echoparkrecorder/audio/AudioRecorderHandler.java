package com.example.roman.echoparkrecorder.audio;

/**
 * Created by roman on 6/13/16.
 */
public class AudioRecorderHandler {

    //audio recorder
    private WavAudioRecorder myAudioRecorder;

    public AudioRecorderHandler(){
        //initialize media recorder
        myAudioRecorder= WavAudioRecorder.getInstance();
    }
    private  void initializeMediaRecorder(String audioFilePath){
        //append the filename to the internal storage path - this is where the output will go
        myAudioRecorder.setOutputFile(audioFilePath);
        myAudioRecorder.prepare();
    }

    public void startRecording(String audioFilePath){
        initializeMediaRecorder(audioFilePath);
        myAudioRecorder.start();
    }

    /**
     * Used by recordEcho() to stop the recording.
     */
    public void stopRecording(){
        myAudioRecorder.stop();
    }
}
