package com.example.roman.echoparkrecorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.audio.AudioRecorderHandler;
import com.example.roman.echoparkrecorder.data.DataRecorderHandler;

import java.io.File;

/**
 * Created by roman on 6/13/16.
 */
public class RecordersManager {

    // external storage directory name
    private final String EXTERNAL_STORAGE_DIR_NAME = "EchoPark_Recorder";

    // file name extensions
    private final String AUDIO_EXTENSION = ".wav";
    private final String DATA_EXTENSION = ".json";

    //base file names - we'll append the filenameid and extension to them
    private final String BASE_AUDIO_FILE_NAME = "/audio";
    private final String BASE_DATA_FILE_NAME = "/data";


    // shared preferences to increment file names
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPrefEditor;

    private AudioRecorderHandler mAudioRecorderHandler;
    private DataRecorderHandler mDataRecorderHandler;

    private Context mContext;

    public RecordersManager(Context context){
        mContext = context;
        //instantiate the shared prefs
        mSharedPreferences = mContext.getSharedPreferences("filenames", mContext.MODE_PRIVATE);
        mSharedPrefEditor = mSharedPreferences.edit();

        mAudioRecorderHandler = new AudioRecorderHandler();
        mDataRecorderHandler = new DataRecorderHandler(mContext);
    }

    public void startRecording(){

        if(!isExternalStorageWritable()){
            Toast.makeText(mContext, "EXTERNAL STORAGE IS NOT WRITEABLE", Toast.LENGTH_SHORT).show();
            return;
        }

        //generate the name id suffix and increment the shared prefs
        int fileNameIdSuffix = mSharedPreferences.getInt("fileId",0)+1;
        mSharedPrefEditor.putInt("fileId", fileNameIdSuffix);
        mSharedPrefEditor.commit();

        //get the path to the folder we're going to write to
        String externalStoragePath = getStorageDirectory();
        String dataFilePath = externalStoragePath + BASE_DATA_FILE_NAME +fileNameIdSuffix+DATA_EXTENSION;
        String audioFilePath = externalStoragePath + BASE_AUDIO_FILE_NAME +fileNameIdSuffix+AUDIO_EXTENSION;

        mDataRecorderHandler.startRecording(dataFilePath);
        mAudioRecorderHandler.startRecording(audioFilePath);

    }

    public void stopRecording(){
        mDataRecorderHandler.stopRecording();
        mAudioRecorderHandler.stopRecording();
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
}
