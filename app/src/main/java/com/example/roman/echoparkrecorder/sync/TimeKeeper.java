package com.example.roman.echoparkrecorder.sync;

import android.os.SystemClock;


public class TimeKeeper {
    private static volatile long mOffset = 0;
    private static volatile long mServerTime = 0;

    private static TimeKeeper mOurInstance = new TimeKeeper();

    public static TimeKeeper getInstance() {
        return mOurInstance;
    }

    private TimeKeeper() {
    }

    //TODO: these are not thread safe, might not be a problem - but fix later anyway
    public void setNewOffset(long serverTime) {
       mOffset = serverTime - SystemClock.elapsedRealtime();
        mServerTime = serverTime;
    }

    //TODO: these are not thread safe, might not be a problem - but fix later anyway
    public long getTime() {
        return  SystemClock.elapsedRealtime()+mOffset;
    }

    public long getServerTime() {
        return mServerTime;
    }

    public long getTimeOffset(){
        return mOffset;
    }
}
