package com.software.android.entities;

import android.os.Bundle;

import com.software.android.cameraapp.Permission;

public interface Publishable {

    /**
     * Is used for publishing actions. This bundle is actually contains what is
     * published to Facebook.
     *
     * @return The bundle with the data.
     */
    Bundle getBundle();

    /**
     * https://developers.facebook.com/docs/reference/api/publishing/
     *
     * @return Kinds of object via the Graph API
     */
    String getPath();

    /**
     * Get the needed permission for being able to publish this item
     *
     * @return {@link com.software.android.cameraapp.Permission}
     */
    Permission getPermission();

}
