package com.software.android.actions;

import android.os.Bundle;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Score;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetScoresAction extends GetAction<List<Score>> {

    public GetScoresAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", configuration.getAppId(), GraphPath.SCORES);
    }

    @Override
    protected Bundle getBundle() {
        return null;
    }

    @Override
    protected List<Score> processResponse(GraphResponse response) {
        Utils.DataResult<Score> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Score>>() {
        }.getType());
        return dataResult.data;
    }

}
