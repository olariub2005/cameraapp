package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.AppRequest;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetAppRequestsAction extends GetAction<List<AppRequest>> {

    public GetAppRequestsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.APPREQUESTS);
    }

    @Override
    protected List<AppRequest> processResponse(GraphResponse response) {
        Utils.DataResult<AppRequest> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<AppRequest>>() {
        }.getType());
        return dataResult.data;
    }

}
