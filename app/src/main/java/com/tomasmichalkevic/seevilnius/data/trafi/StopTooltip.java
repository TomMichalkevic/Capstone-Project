package com.tomasmichalkevic.seevilnius.data.trafi;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopTooltip implements Serializable
{

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DirectionText")
    @Expose
    private String directionText;
    @SerializedName("DistanceText")
    @Expose
    private String distanceText;
    @SerializedName("SchedulesAtStop")
    @Expose
    private List<SchedulesAtStop> schedulesAtStop = null;
    private final static long serialVersionUID = -3114428203319303722L;

    /**
     * No args constructor for use in serialization
     *
     */
    public StopTooltip() {
    }

    /**
     *
     * @param schedulesAtStop
     * @param name
     * @param distanceText
     * @param directionText
     */
    public StopTooltip(String name, String directionText, String distanceText, List<SchedulesAtStop> schedulesAtStop) {
        super();
        this.name = name;
        this.directionText = directionText;
        this.distanceText = distanceText;
        this.schedulesAtStop = schedulesAtStop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectionText() {
        return directionText;
    }

    public void setDirectionText(String directionText) {
        this.directionText = directionText;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public List<SchedulesAtStop> getSchedulesAtStop() {
        return schedulesAtStop;
    }

    public void setSchedulesAtStop(List<SchedulesAtStop> schedulesAtStop) {
        this.schedulesAtStop = schedulesAtStop;
    }

}