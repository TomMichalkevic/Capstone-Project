package com.tomasmichalkevic.seevilnius.data;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlusCode implements Serializable
{

    @SerializedName("compound_code")
    @Expose
    private String compoundCode;
    @SerializedName("global_code")
    @Expose
    private String globalCode;
    private final static long serialVersionUID = 5080576860693083551L;

    /**
     * No args constructor for use in serialization
     *
     */
    public PlusCode() {
    }

    /**
     *
     * @param compoundCode
     * @param globalCode
     */
    public PlusCode(String compoundCode, String globalCode) {
        super();
        this.compoundCode = compoundCode;
        this.globalCode = globalCode;
    }

    public String getCompoundCode() {
        return compoundCode;
    }

    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }

}