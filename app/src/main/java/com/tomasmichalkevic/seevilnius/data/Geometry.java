package com.tomasmichalkevic.seevilnius.data;

import com.tomasmichalkevic.seevilnius.data.Location;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry implements Serializable
{

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;
    private final static long serialVersionUID = 7215710196895825594L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Geometry() {
    }

    /**
     *
     * @param viewport
     * @param location
     */
    public Geometry(Location location, Viewport viewport) {
        super();
        this.location = location;
        this.viewport = viewport;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}