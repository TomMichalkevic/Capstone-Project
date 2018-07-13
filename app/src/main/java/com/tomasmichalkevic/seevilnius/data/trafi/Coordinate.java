package com.tomasmichalkevic.seevilnius.data.trafi;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinate implements Serializable
{

    @SerializedName("Lat")
    @Expose
    private double lat;
    @SerializedName("Lng")
    @Expose
    private double lng;
    private final static long serialVersionUID = -7627795196087126964L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Coordinate() {
    }

    /**
     *
     * @param lng
     * @param lat
     */
    public Coordinate(double lat, double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}