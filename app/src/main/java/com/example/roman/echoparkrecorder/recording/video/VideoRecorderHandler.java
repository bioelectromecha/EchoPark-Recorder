package com.example.roman.echoparkrecorder.recording.video;



import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.view.Surface;
import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.Recorder;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import hugo.weaving.DebugLog;


/**
 * Created by roman on 7/7/16.
 */
public class VideoRecorderHandler extends HandlerThread implements Recorder {
    private Camera mServiceCamera;
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;

    private static final String METADATA_SUFFIX = ".metadata";


    public VideoRecorderHandler() {
        super("VideoRecorderThread", Process.THREAD_PRIORITY_DISPLAY);
        mMediaRecorder = new MediaRecorder();
        LogUtils.d("VideoRecorderHandler");
    }

    @Override
    @DebugLog
    public void startRecording(String videoFilePath) {
        stampCmdMetadata(videoFilePath);
        try {
            mServiceCamera = Camera.open();
//            Camera.Parameters params = mServiceCamera.getParameters();
//            mServiceCamera.setParameters(params);
            mServiceCamera.unlock();
            //initialize all the mediarecorder stuff
            initMediaRecorder(videoFilePath);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }
        stampInitMetadata(videoFilePath);
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
    public void stopRecording() {
        LogUtils.d("stopRecording "+ System.currentTimeMillis());

        mMediaRecorder.stop();
        mMediaRecorder.reset();

        mServiceCamera.unlock();
                try {
            mServiceCamera.reconnect();
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            e.printStackTrace();
        }
    }

    private void initMediaRecorder(String videoFilePath ) {
        mMediaRecorder.setCamera(mServiceCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        mMediaRecorder.setOutputFormat(profile.fileFormat);
        mMediaRecorder.setVideoFrameRate(60);
        mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoEncoder(profile.videoCodec);
        mMediaRecorder.setOutputFile(videoFilePath);
        //create and pass a fake surfaceview to mediaRecorder
        Surface surface = new Surface(new SurfaceTexture(10));
        mMediaRecorder.setPreviewDisplay(surface);
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(getLooper());
    }

    public void postTask(Runnable task){
        mHandler.post(task);
    }
}
