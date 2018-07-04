package com.tomasmichalkevic.seevilnius.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours implements Serializable
{

    @SerializedName("open_now")
    @Expose
    private boolean openNow;
    private final static long serialVersionUID = 2920708324756043213L;

    /**
     * No args constructor for use in serialization
     *
     */
    public OpeningHours() {
    }

    /**
     *
     * @param openNow
     */
    public OpeningHours(boolean openNow) {
        super();
        this.openNow = openNow;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

}