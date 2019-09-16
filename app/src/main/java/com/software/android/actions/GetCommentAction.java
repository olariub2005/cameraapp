package com.software.android.actions;

import android.os.Bundle;

import com.facebook.GraphResponse;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Comment;
import com.software.android.entities.Page.Properties;
import com.software.android.utils.JsonUtils;

public class GetCommentAction extends GetAction<Comment> {

    private Properties mProperties;

    public GetCommentAction(SessionManager sessionManager) {
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
    protected Comment processResponse(GraphResponse response) {
        return JsonUtils.fromJson(response.getRawResponse(), Comment.class);
    }

}
