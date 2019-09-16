package com.software.android.actions;

import android.os.Bundle;

import com.facebook.GraphResponse;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Profile;
import com.software.android.entities.Profile.Properties;
import com.software.android.utils.JsonUtils;

public class GetProfileAction extends GetAction<Profile> {

    private Properties mProperties;

    public GetProfileAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setProperties(Properties properties) {
        mProperties = properties;
    }

    @Override
    protected String getGraphPath() {
        return getTarget();
    }

    @Override
    protected Bundle getBundle() {
        if (mProperties != null) {
            return mProperties.getBundle();
        }
        return null;
    }

    @Override
    protected Profile processResponse(GraphResponse response) {
        return JsonUtils.fromJson(response.getRawResponse(), Profile.class);
    }

}
