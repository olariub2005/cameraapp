package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.PlaceTag;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetTaggedPlacesAction extends GetAction<List<PlaceTag>> {

    public GetTaggedPlacesAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.TAGGED_PLACES;
    }

    @Override
    protected List<PlaceTag> processResponse(GraphResponse response) {
        Utils.DataResult<PlaceTag> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<PlaceTag>>() {
        }.getType());
        return dataResult.data;
    }
}
