package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Like;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetLikesAction extends GetAction<List<Like>> {

    public GetLikesAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.LIKES;
    }

    @Override
    protected List<Like> processResponse(GraphResponse response) {
        Utils.DataResult<Like> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Like>>() {
        }.getType());
        return dataResult.data;
    }
}
