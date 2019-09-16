package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Album;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetAlbumsAction extends GetAction<List<Album>> {

    public GetAlbumsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return String.format("%s/%s", getTarget(), GraphPath.ALBUMS);
    }

    @Override
    protected List<Album> processResponse(GraphResponse response) {
        Utils.DataResult<Album> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Album>>() {
        }.getType());
        return dataResult.data;
    }

}
