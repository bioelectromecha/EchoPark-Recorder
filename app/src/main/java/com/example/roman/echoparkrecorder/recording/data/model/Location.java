
package com.example.roman.echoparkrecorder.recording.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("velocity")
    @Expose
    private String velocity;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longtitude")
    @Expose
    private String longtitude;
    @SerializedName("altitude")
    @Expose
    private String altitude;
    @SerializedName("eladTimeStamp")
    @Expose
    private String eladTimeStamp;
    @SerializedName("accuracy")
    @Expose
    private String accuracy;
    @SerializedName("bearing")
    @Expose
    private String bearing;

    public Location(android.location.Location androidLocation, long eladTimeStamp){
        this.timeStamp =  String.valueOf(androidLocation.getTime());
        this.velocity =  String.valueOf(androidLocation.getSpeed());
        this.latitude =  String.valueOf(androidLocation.getLatitude());
        this.longtitude = String.valueOf(androidLocation.getLongitude());
        this.altitude = String.valueOf(androidLocation.getAltitude());
        this.eladTimeStamp = String.valueOf(eladTimeStamp);
        this.accuracy = String.valueOf(androidLocation.getAccuracy());
        this.bearing = String.valueOf(androidLocation.getBearing());
    }
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
     *     The velocity
     */
    public String getVelocity() {
        return velocity;
    }

    /**
     * 
     * @param velocity
     *     The velocity
     */
    public void setVelocity(String velocity) {
        this.velocity = velocity;
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

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getEladTimeStamp() {
        return eladTimeStamp;
    }

    public void setEladTimeStamp(String eladTimeStamp) {
        this.eladTimeStamp = eladTimeStamp;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }
}
