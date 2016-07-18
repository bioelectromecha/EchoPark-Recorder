
package com.example.roman.echoparkrecorder.Recording.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longtitude")
    @Expose
    private String longtitude;

    /**
     * 
     * @return
     *     The timeStamp
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * 
     * @param timeStamp
     *     The timeStamp
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 
     * @return
     *     The speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longtitude
     */
    public String getLongtitude() {
        return longtitude;
    }

    /**
     * 
     * @param longtitude
     *     The longtitude
     */
    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

}
