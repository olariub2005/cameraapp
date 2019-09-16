package com.software.android.actions;

import com.facebook.GraphResponse;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Photo;
import com.software.android.utils.JsonUtils;

public class GetPhotoAction extends GetAction<Photo> {

    public GetPhotoAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget();
    }

    @Override
    protected Photo processResponse(GraphResponse response) {
        return JsonUtils.fromJson(response.getRawResponse(), Photo.class);
    }

}
