package com.example.roman.echoparkrecorder.Recording;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.Recording.audio.AudioRecorderHandler;
import com.example.roman.echoparkrecorder.Recording.data.DataRecorderHandler;
import com.example.roman.echoparkrecorder.Recording.video.VideoRecorderHandler;

import java.io.File;

/**
 * Created by roman on 6/13/16.
 */
public class RecordingManager {

    // external storage directory name
    private final String EXTERNAL_STORAGE_DIR_NAME = "EchoPark_Recorder";

    // file name extensions
    private final String AUDIO_EXTENSION = ".wav";
    private final String DATA_EXTENSION = ".json";
    private final String VIDEO_EXTENSION = ".mp4";

    //base file names - we'll append the filenameid and extension to them
    private final String BASE_AUDIO_FILE_NAME = "/audio";
    private final String BASE_DATA_FILE_NAME = "/data";
    private final String BASE_VIDEO_FILE_NAME = "/video";



    // shared preferences to increment file names
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPrefEditor;


    private DataRecorderHandler mDataRecorderHandler;
    private AudioRecorderHandler mAudioRecorderHandler;
    private VideoRecorderHandler mVideoRecorderHandler;

    private Context mContext;

    private boolean mRecordingState = false;


    public RecordingManager(Context context){
        mContext = context;
        //instantiate the shared prefs
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        mContext.getSharedPreferences("filenames", mContext.MODE_PRIVATE);
        mSharedPrefEditor = mSharedPreferences.edit();

        //create and start the data recorder
        mDataRecorderHandler = new DataRecorderHandler(mContext);
        mDataRecorderHandler.start();
        //create and start the audio recorder
        mAudioRecorderHandler = new AudioRecorderHandler();
        mAudioRecorderHandler.start();
        //create and start the video recorder
        mVideoRecorderHandler = new VideoRecorderHandler();
        mVideoRecorderHandler.start();

    }

    public void startRecording(){
        LogUtils.d("startRecording "+ System.currentTimeMillis());
        if (mRecordingState) {
            return;
        }

        if(!isExternalStorageWritable()){
            LogUtils.d("EXTERNAL STORAGE IS NOT WRITEABLE");
            return;
        }
        mRecordingState = true;

        //generate the name id suffix and increment the shared prefs
        int fileNameIdSuffix = mSharedPreferences.getInt("fileId",0)+1;
        mSharedPrefEditor.putInt("fileId", fileNameIdSuffix);
        mSharedPrefEditor.commit();

        //get the path to the folder we're going to write to
        String externalStoragePath = getStorageDirectory();
        //create the file path and file names for each recording type
        final String dataFilePath = externalStoragePath + BASE_DATA_FILE_NAME +fileNameIdSuffix+DATA_EXTENSION;
        final String audioFilePath = externalStoragePath + BASE_AUDIO_FILE_NAME +fileNameIdSuffix+AUDIO_EXTENSION;
        final String videoFilePath = externalStoragePath + BASE_VIDEO_FILE_NAME +fileNameIdSuffix+VIDEO_EXTENSION;

        //start recording locations to json file
        mDataRecorderHandler.postTask(RecorderMessage.getStartMessage(mDataRecorderHandler,dataFilePath));
        //start recording audio to .wav file
        mAudioRecorderHandler.postTask(RecorderMessage.getStartMessage(mAudioRecorderHandler,audioFilePath));
        //start the video recording to .mp4 file
        mVideoRecorderHandler.postTask(RecorderMessage.getStartMessage(mVideoRecorderHandler,videoFilePath));
    }

    public void stopRecording(){
        LogUtils.d("stopRecording "+ System.currentTimeMillis());
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

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        LogUtils.d("EXTERNAL STORAGE NOT WRITEABLE");
        return false;
    }

    private String getStorageDirectory() {
        // Get the directory for the user's external storage.
        File file = new File(Environment.getExternalStorageDirectory(), EXTERNAL_STORAGE_DIR_NAME);

        if(file.mkdirs()) {
            //file was created
        }else{
            //failed or already exists
        }
        return file.getAbsolutePath();
    }

    public boolean getRecordingState() {
        return mRecordingState;
    }
}
