package com.example.roman.echoparkrecorder.recording.data;

import com.apkfuns.logutils.LogUtils;
import com.example.roman.echoparkrecorder.recording.data.model.DataSet;
import com.example.roman.echoparkrecorder.recording.data.model.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by roman on 6/12/16.
 */
public class JSONDataRecorder {
    // file naming stuff
    private DataSet mDataSet;

    GsonBuilder mBuilder;
    Gson mGson;

    public JSONDataRecorder(){

        //generate a data model
        mDataSet = new DataSet();

        //instantiate GSON
        mBuilder = new GsonBuilder();
        mGson = mBuilder.create();
    }

    public void recordLocation(android.location.Location androidLocation, String dataFilePath){

        //TODO: location is written with network time and not TimeKeeper time
        // the file we're going to write to
        File file = new File(dataFilePath);

        if(file.exists()) {
            try {
                FileInputStream fIn = new FileInputStream(file);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = ""; //Holds the text
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow;
                }
                myReader.close();
                mDataSet = (mGson.fromJson(aBuffer, DataSet.class));
            } catch (IOException e) {
                LogUtils.d("READ FROM FILE FAILED");
                e.printStackTrace();
            }
        }

        //create the location POJO
        Location location = new Location();
        location.setLatitude(String.valueOf(androidLocation.getLatitude()));
        location.setLongtitude(String.valueOf(androidLocation.getLongitude()));
        location.setSpeed(String.valueOf(androidLocation.getSpeed()));
        location.setTimeStamp(String.valueOf(androidLocation.getTime()));
        //add to pojo tree
        mDataSet.addLocationEvent(location);


        //write json to file
        try {
            String s = mGson.toJson(mDataSet);
            FileOutputStream outputStream =  new FileOutputStream(file);
            outputStream.write(s.getBytes());
            outputStream.close();
        }catch (IOException e){
            LogUtils.d(" WRITE TO JSON FAILED");
            e.printStackTrace();
        }
    }
}
