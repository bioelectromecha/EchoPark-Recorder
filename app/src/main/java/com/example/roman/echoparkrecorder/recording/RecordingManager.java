package com.example.roman.echoparkrecorder.recording;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.audio.AudioRecorderHandler;
import com.example.roman.echoparkrecorder.recording.data.DataRecorderHandler;
import com.example.roman.echoparkrecorder.recording.video.VideoRecorderHandler;
import com.example.roman.echoparkrecorder.sync.FileNameDispenser;

/**
 * Created by roman on 6/13/16.
 */
public class RecordingManager {


    private DataRecorderHandler mDataRecorderHandler;
    private AudioRecorderHandler mAudioRecorderHandler;
    private VideoRecorderHandler mVideoRecorderHandler;

    private Context mContext;

    private boolean mRecordingState = false;

    private FileNameDispenser mFileNameDispenser;


    public RecordingManager(Context context){
        mContext = context;

        //create and start the data recorder
        mDataRecorderHandler = new DataRecorderHandler(mContext);
        mDataRecorderHandler.start();
        //create and start the audio recorder
        mAudioRecorderHandler = new AudioRecorderHandler();
        mAudioRecorderHandler.start();
        //create and start the video recorder
        mVideoRecorderHandler = new VideoRecorderHandler();
        mVideoRecorderHandler.start();

        mFileNameDispenser = new FileNameDispenser();
    }

    public void startRecording(){
        LogUtils.d("startRecording");
        if (mRecordingState) {
            return;
        }
        //don't record if we can't write to external storage
        if(!mFileNameDispenser.isExternalStorageWritable()){
            LogUtils.d("EXTERNAL STORAGE IS NOT WRITEABLE");
            return;
        }
        mRecordingState = true;

        //start recording locations to json file
        mDataRecorderHandler.postTask(RecorderMessage.getStartMessage(mDataRecorderHandler, mFileNameDispenser.getDataFilePath()));
        //start recording audio to .wav file
        mAudioRecorderHandler.postTask(RecorderMessage.getStartMessage(mAudioRecorderHandler, mFileNameDispenser.getAudioFilePath()));
        //start the video recording to .mp4 file
        mVideoRecorderHandler.postTask(RecorderMessage.getStartMessage(mVideoRecorderHandler, mFileNameDispenser.getVideoFilePath()));
    }

    public void stopRecording(){
        LogUtils.d("stopRecording");
        if(!mRecordingState){
            return;
        }
        mRecordingState = false;
        //stop recording location to json file
        mDataRecorderHandler.postTask(RecorderMessage.getStopMessage(mDataRecorderHandler));
        // stop recording audio to .wav file
        mAudioRecorderHandler.postTask(RecorderMessage.getStopMessage(mAudioRecorderHandler));
        //stop recording video to .mp4 file
        mVideoRecorderHandler.postTask(RecorderMessage.getStopMessage(mVideoRecorderHandler));
    }

    public boolean getRecordingState() {
        return mRecordingState;
    }
}
