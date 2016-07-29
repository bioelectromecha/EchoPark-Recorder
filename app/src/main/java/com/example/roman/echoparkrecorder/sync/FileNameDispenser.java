package com.example.roman.echoparkrecorder.sync;

import android.os.Build;
import android.os.Environment;

import com.apkfuns.logutils.LogUtils;

import java.io.File;

/**
 * Created by roman on 7/29/16.
 */
public class FileNameDispenser {
    // external storage directory name
    private final String EXTERNAL_STORAGE_DIR_NAME = "EchoPark_Recorder/";

    // file name extensions
    private final String AUDIO_EXTENSION = ".wav";
    private final String DATA_EXTENSION = ".json";
    private final String VIDEO_EXTENSION = ".mp4";

    //base file names - we'll append the filenameid and extension to them
    private final String BASE_AUDIO_FILE_NAME = "/audio";
    private final String BASE_DATA_FILE_NAME = "/data";
    private final String BASE_VIDEO_FILE_NAME = "/video";

    // metadata suffix
    private static final String METADATA_SUFFIX = ".metadata";

    private final String SERVER_TIME_OFFSET_LOG_FILE_NAME = "/server_offset_logs.txt";

    private TimeKeeper mTimeKeeper;

    public FileNameDispenser() {
        mTimeKeeper = TimeKeeper.getInstance();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        LogUtils.d("EXTERNAL STORAGE NOT WRITEABLE");
        return false;
    }

    private String getStorageDirectory() {
        // Get the directory for the user's external storage.
        File file = new File(Environment.getExternalStorageDirectory(), EXTERNAL_STORAGE_DIR_NAME+getDeviceName());

        if(file.mkdirs()) {
            //file was created
        }else{
            //failed or already exists
        }
        return file.getAbsolutePath();
    }

    /**
     * get the device model
     * @return the device model name (e.g for a galaxy s2, will return GT-I9100)
     */
    private String getDeviceName(){
        return Build.MODEL;
    }

    public String getDataFilePath() {
        return getStorageDirectory() + BASE_DATA_FILE_NAME +mTimeKeeper.getServerTime()+DATA_EXTENSION;
    }

    public String getAudioFilePath() {
        return getStorageDirectory() + BASE_AUDIO_FILE_NAME +mTimeKeeper.getServerTime()+AUDIO_EXTENSION;
    }
    public String getVideoFilePath() {
        return getStorageDirectory() + BASE_VIDEO_FILE_NAME +mTimeKeeper.getServerTime()+VIDEO_EXTENSION;
    }
    public String getDataFileMetadataPath(){
        return getDataFilePath() + METADATA_SUFFIX;
    }
    public String getAudioFileMetadataPath(){
        return getAudioFilePath() + METADATA_SUFFIX;

    }
    public String getVideoFileMetadataPath(){
        return getVideoFilePath() + METADATA_SUFFIX;
    }
    public String getServerTimeOffsetLogFilePath(){
        return getStorageDirectory() + SERVER_TIME_OFFSET_LOG_FILE_NAME;
    }
}
