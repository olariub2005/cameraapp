package com.software.android.actions;

import android.os.Bundle;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Profile;
import com.software.android.entities.Profile.Properties;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetFriendsAction extends GetAction<List<Profile>> {

    private Properties mProperties;

    public GetFriendsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setProperties(Properties properties) {
        mProperties = properties;
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.FRIENDS);
    }

    @Override
    protected Bundle getBundle() {
        if (mProperties != null) {
            return mProperties.getBundle();
        }
        return null;
    }

    @Override
    protected List<Profile> processResponse(GraphResponse response) {
        Utils.DataResult<Profile> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Profile>>() {
        }.getType());
        return dataResult.data;
    }

}
