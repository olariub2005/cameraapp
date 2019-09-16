package com.software.android.cameraapp;

import android.app.Application;

import com.facebook.login.DefaultAudience;
import com.software.android.utils.Logger;
import com.software.android.utils.SharedObjects;

public class SampleApplication extends Application {
    private static final String APP_ID = "1559060157738151";
    private static final String APP_NAMESPACE = "crazy_speed_recorder";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedObjects.context = this;

        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;

        // initialize facebook configuration
        Permission[] permissions = new Permission[] {
                // Permission.PUBLIC_PROFILE,
                Permission.EMAIL,
                Permission.USER_EVENTS,
                Permission.USER_ACTIONS_MUSIC,
                Permission.USER_FRIENDS,
                Permission.USER_GAMES_ACTIVITY,
                Permission.USER_BIRTHDAY,
                Permission.USER_TAGGED_PLACES,
                Permission.USER_MANAGED_GROUPS,
                Permission.PUBLISH_ACTION };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(APP_ID)
                .setNamespace(APP_NAMESPACE)
                .setPermissions(permissions)
                .setDefaultAudience(DefaultAudience.FRIENDS)
                .setAskForAllPermissionsAtOnce(false)
                // .setGraphVersion("v2.3")
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }
}
