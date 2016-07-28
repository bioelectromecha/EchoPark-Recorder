package com.example.roman.echoparkrecorder.service.listeners;

/**
 * Created by roman on 7/16/16.
 */
public interface RecordingStateListener {

    public void requestNetworkStartRecording();

    public void requestNetworkStopRecording();

    public void requestStartRecording();

    public void requestStopRecording();

    public void requestUpdate();
}
