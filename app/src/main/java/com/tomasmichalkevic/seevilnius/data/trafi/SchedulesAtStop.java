package com.tomasmichalkevic.seevilnius.data.trafi;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchedulesAtStop implements Serializable
{

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Color")
    @Expose
    private String color;
    private final static long serialVersionUID = 1497384518759199804L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SchedulesAtStop() {
    }

    /**
     *
     * @param color
     * @param name
     */
    public SchedulesAtStop(String name, String color) {
        super();
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}