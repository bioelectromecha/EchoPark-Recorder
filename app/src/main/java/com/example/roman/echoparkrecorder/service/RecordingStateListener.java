package com.example.roman.echoparkrecorder.service;

/**
 * Created by roman on 7/16/16.
 */
public interface RecordingStateListener {

    public void requestStartRecording();

    public void requestStopRecording();

    public void requestUpdate();
}
