package com.software.android.actions;

import com.software.android.cameraapp.SessionManager;
import com.software.android.utils.GraphPath;

public class GetTaggableFriendsAction  extends GetFriendsAction {

    public GetTaggableFriendsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.TAGGABLE_FRIENDS);
    }

}
