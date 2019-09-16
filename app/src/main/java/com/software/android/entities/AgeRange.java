package com.software.android.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Age range (min-max) of the user.
 *
 * 
 */
public class AgeRange {

    @SerializedName("min")
    private String mMin;

    @SerializedName("max")
    private String mMax;

    public String getMin() {
        return mMin;
    }

    public String getMax() {
        return mMax;
    }
}
