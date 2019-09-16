package com.software.android.actions;

import com.software.android.cameraapp.SessionManager;
import com.software.android.utils.GraphPath;

public class GetInvitableFriendsAction  extends GetFriendsAction {

    public GetInvitableFriendsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.INVITABLE_FRIENDS);
    }

}
