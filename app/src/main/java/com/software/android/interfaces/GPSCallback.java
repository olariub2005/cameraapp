package com.software.android.interfaces;

import android.location.Location;

/**
 * Created by Administrator on 1/15/2016.
 */
public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}
