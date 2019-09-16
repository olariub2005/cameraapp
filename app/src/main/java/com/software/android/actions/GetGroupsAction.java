package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Group;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetGroupsAction extends GetAction<List<Group>> {

    public GetGroupsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.GROUPS;
    }

    @Override
    protected List<Group> processResponse(GraphResponse response) {
        Utils.DataResult<Group> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Group>>() {
        }.getType());
        return dataResult.data;
    }

}
