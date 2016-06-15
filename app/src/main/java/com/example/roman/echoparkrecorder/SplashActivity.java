package com.example.roman.echoparkrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSIONS_CODE = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestPermissions();
    }

    public void requestPermissions() {
        //check if premission was granted and ask if not granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            //ask the user for location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA },
                    MULTIPLE_PERMISSIONS_CODE);
        }else{
            //if permission already granted
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS_CODE: {
                // TODO: WE ARE ASKING FOR MULTIPLE PERMISSIONS - IF ONE REQUEST IS DENIED IT WILL CURRENTLY STILL GO TO NEXT ACTIVITY (fix this!)
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    // launch the main activity
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // permission denied, boo!
                    Toast.makeText(SplashActivity.this, "THIS APP NEEDS PERMISSIONS TO WORK!", Toast.LENGTH_LONG).show();
                    requestPermissions();
                }
                return;
            }

            default: {
                Toast.makeText(SplashActivity.this, "DEFAULT PERMISSION CASE CALLED! WTF?!", Toast.LENGTH_LONG).show();
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
