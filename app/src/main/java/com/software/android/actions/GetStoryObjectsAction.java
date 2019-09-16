package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Story.StoryObject;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetStoryObjectsAction extends GetAction<List<StoryObject>> {

    private String mObjectName;

    public GetStoryObjectsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setObjectName(String objectName) {
        mObjectName = objectName;
    }

    @Override
    protected String getGraphPath() {
        String namespace = configuration.getNamespace();
        return getTarget() + "/" + GraphPath.OBJECTS + "/" + namespace  + ":" + mObjectName;
    }

    @Override
    protected List<StoryObject> processResponse(GraphResponse response) {
        Utils.DataResult<StoryObject> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<StoryObject>>() {
        }.getType());
        return dataResult.data;
    }

}
