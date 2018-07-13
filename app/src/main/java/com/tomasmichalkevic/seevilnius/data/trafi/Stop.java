package com.tomasmichalkevic.seevilnius.data.trafi;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stop implements Serializable
{

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Direction")
    @Expose
    private String direction;
    @SerializedName("Coordinate")
    @Expose
    private Coordinate coordinate;
    @SerializedName("IconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("StopTooltip")
    @Expose
    private StopTooltip stopTooltip;
    private final static long serialVersionUID = 8769498090621342650L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Stop() {
    }

    /**
     *
     * @param id
     * @param coordinate
     * @param direction
     * @param iconUrl
     * @param name
     * @param stopTooltip
     */
    public Stop(String id, String name, String direction, Coordinate coordinate, String iconUrl, StopTooltip stopTooltip) {
        super();
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.coordinate = coordinate;
        this.iconUrl = iconUrl;
        this.stopTooltip = stopTooltip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public StopTooltip getStopTooltip() {
        return stopTooltip;
    }

    public void setStopTooltip(StopTooltip stopTooltip) {
        this.stopTooltip = stopTooltip;
    }

}