package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Photo;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetPhotosAction extends GetAction<List<Photo>> {

    public GetPhotosAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.PHOTOS;
    }

    @Override
    protected List<Photo> processResponse(GraphResponse response) {
        Utils.DataResult<Photo> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Photo>>() {
        }.getType());
        return dataResult.data;
    }

}
