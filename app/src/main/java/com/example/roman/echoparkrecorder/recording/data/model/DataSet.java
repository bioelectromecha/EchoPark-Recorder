
package com.example.roman.echoparkrecorder.recording.data.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataSet {

    @SerializedName("dataset")
    @Expose
    private List<Location> dataset = new ArrayList<Location>();

    /**
     * 
     * @return
     *     The dataset
     */
    public List<Location> getDataset() {
        return dataset;
    }

    /**
     * 
     * @param dataset
     *     The dataset
     */
    public void setDataset(List<Location> dataset) {
        this.dataset = dataset;

    }

    public void addLocationEvent(Location location){
        this.dataset.add(location);
    }
}
