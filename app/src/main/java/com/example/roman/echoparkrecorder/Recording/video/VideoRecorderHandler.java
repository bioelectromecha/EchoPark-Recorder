package com.example.roman.echoparkrecorder.Recording.video;



import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.Recorder;

import java.io.IOException;

/**
 * Created by roman on 7/7/16.
 */
public class VideoRecorderHandler extends HandlerThread implements Recorder {
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;
    private Camera mCamera;

    public VideoRecorderHandler() {
        super("VideoRecorderThread", Process.THREAD_PRIORITY_DEFAULT);
        mMediaRecorder = new MediaRecorder();

    }

    @Override
    public void startRecording(String videoFilePath) {
        initRecorder(videoFilePath);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            mMediaRecorder.release();
            LogUtils.d("MediaRecorder prepare() failed");
            e.printStackTrace();
        }
        mMediaRecorder.start();

    }

    @Override
    public void stopRecording() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();

    }

    private void initRecorder(String videoFilePath ) {

        //get camera and unlock
        mCamera = Camera.open();
        mCamera.unlock();
        //give camera to mediaRecorder
        mMediaRecorder.setCamera(mCamera);
        //
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        CamcorderProfile profile = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        mMediaRecorder.setProfile(profile);
        mMediaRecorder.setOutputFile(videoFilePath);
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }

}
