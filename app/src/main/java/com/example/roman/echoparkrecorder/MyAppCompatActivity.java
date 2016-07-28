package com.example.roman.echoparkrecorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by roman on 7/24/16.
 */
public class MyAppCompatActivity extends AppCompatActivity {
    // stuff to protect against button mashing
    private static final long CLICK_TIMEOUT = 700; //milliseconds
    private static long mLastClickTime = 0;
    /**
     * key mashing protection helper method
     * @return true if click timeout has passed, false otherwise
     */
    protected boolean clickTimeoutOverCheck() {
        if( System.currentTimeMillis() < mLastClickTime + CLICK_TIMEOUT ){
            mLastClickTime = System.currentTimeMillis();
            return false;
        }else{
            mLastClickTime = System.currentTimeMillis();
            return true;
        }
    }
    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
