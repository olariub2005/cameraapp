package com.software.android.actions;

import android.os.Bundle;

import com.facebook.GraphResponse;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Page;
import com.software.android.entities.Page.Properties;
import com.software.android.utils.JsonUtils;

public class GetPageAction extends GetAction<Page> {

    private Properties mProperties;

    public GetPageAction(SessionManager sessionManager) {
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
    protected Page processResponse(GraphResponse response) {
        return JsonUtils.fromJson(response.getRawResponse(), Page.class);
    }

}
