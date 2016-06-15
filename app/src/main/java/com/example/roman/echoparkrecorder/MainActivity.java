package com.example.roman.echoparkrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.WindowManager;

public class MainActivity extends AppCompatActivity{

    private RecordersManager mRecordersManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //disable screen turn off
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mRecordersManager = new RecordersManager(this);

    }

    // don't forget onStart() and onStop() for any activity that uses google api services
    @Override
    protected void onStart() {
        //start recording???
        super.onStart();
    }


    @Override
    protected void onStop() {
        //stop recording
        mRecordersManager.stopRecording();
        super.onStop();
    }
}