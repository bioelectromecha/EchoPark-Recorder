package com.example.roman.echoparkrecorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //this is where the magic happens
        goToNextActivity();
    }

    /**
     * this one will open the next activity and prevent from going back to splash screen
     */
    private void goToNextActivity(){
        Intent intent = new Intent(this,PermissionsActivity.class);
        startActivity(intent);
        finish();
        return;
    }

}
