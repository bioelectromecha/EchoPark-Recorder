package com.example.roman.echoparkrecorder.Recording.video;



import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.view.Surface;
import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.Recorder;


/**
 * Created by roman on 7/7/16.
 */
public class VideoRecorderHandler extends HandlerThread implements Recorder {
    private Camera mServiceCamera;
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;


    public VideoRecorderHandler() {
        super("VideoRecorderThread", Process.THREAD_PRIORITY_DISPLAY);
        mMediaRecorder = new MediaRecorder();
        LogUtils.d("VideoRecorderHandler");
    }

    @Override
    public void startRecording(String videoFilePath) {
        LogUtils.d("startRecording "+ System.currentTimeMillis());

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
    }

    @Override
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
        LogUtils.d("");
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
