package com.example.roman.echoparkrecorder.recording.data;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.data.model.Location;
import com.example.roman.echoparkrecorder.sync.TimeKeeper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by roman on 6/12/16.
 */
public class JSONDataRecorder {
    // file naming stuff
    GsonBuilder mBuilder;
    Gson mGson;
    private boolean isFirstWrite = true;

    public JSONDataRecorder(){

        //instantiate GSON
        mBuilder = new GsonBuilder();
        mGson = mBuilder.create();
    }

    public void recordLocation(android.location.Location androidLocation, String dataFilePath){
        // the file we're going to write to
        File file = new File(dataFilePath);
        //create the location POJO
        Location location = new Location(androidLocation, TimeKeeper.getInstance().getTime());

        //write json to file
        try {
            String s = mGson.toJson(location);
            LogUtils.d(isFirstWrite);
            LogUtils.d(s);
            FileOutputStream outputStream =  new FileOutputStream(file,true);
            if(!isFirstWrite){
                String seperator = ",";
                outputStream.write(seperator.getBytes());
            }
            outputStream.write(s.getBytes());
            isFirstWrite = false;
            outputStream.close();
        }catch (IOException e){
            LogUtils.d(" WRITE TO JSON FAILED");
            e.printStackTrace();
        }
    }

    public void signStart(String dataFilePath) {
        File file = new File(dataFilePath);
        try {
            String s = "[";
            FileOutputStream outputStream =  new FileOutputStream(file,true);
            outputStream.write(s.getBytes());
            outputStream.close();
        }catch (IOException e){
            LogUtils.d("signStart failed");
            e.printStackTrace();
        }
    }

    public void signStop(String dataFilePath) {
        File file = new File(dataFilePath);
        isFirstWrite = true;
        try {
            String s = "]";
            FileOutputStream outputStream =  new FileOutputStream(file,true);
            outputStream.write(s.getBytes());
            outputStream.close();
        }catch (IOException e){
            LogUtils.d("signStart failed");
            e.printStackTrace();
        }
    }

}
