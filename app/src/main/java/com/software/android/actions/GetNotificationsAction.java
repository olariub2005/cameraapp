package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Notification;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetNotificationsAction extends GetAction<List<Notification>> {

    public GetNotificationsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.NOTIFICATIONS);
    }

    @Override
    protected List<Notification> processResponse(GraphResponse response) {
        Utils.DataResult<Notification> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Notification>>() {
        }.getType());
        return dataResult.data;
    }

}
