package com.tomasmichalkevic.seevilnius.data.trafi;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrafiResponse implements Serializable
{

    @SerializedName("Stops")
    @Expose
    private List<Stop> stops = null;
    private final static long serialVersionUID = -8279738850900459336L;

    /**
     * No args constructor for use in serialization
     *
     */
    public TrafiResponse() {
    }

    /**
     *
     * @param stops
     */
    public TrafiResponse(List<Stop> stops) {
        super();
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

}