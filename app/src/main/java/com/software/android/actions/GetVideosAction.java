package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Video;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetVideosAction extends GetAction<List<Video>> {

    public GetVideosAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.VIDEOS;
    }

    @Override
    protected List<Video> processResponse(GraphResponse response) {
        Utils.DataResult<Video> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Video>>() {
        }.getType());
        return dataResult.data;
    }

}
