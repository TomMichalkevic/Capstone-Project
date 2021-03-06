package com.tomasmichalkevic.seevilnius.data.details;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Open implements Serializable
{

    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("time")
    @Expose
    private String time;
    private final static long serialVersionUID = 3260073834438878488L;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}