package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Comment;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

public class GetCommentsAction extends GetAction<List<Comment>> {

    public GetCommentsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.COMMENTS;
    }

    @Override
    protected List<Comment> processResponse(GraphResponse response) {
        Utils.DataResult<Comment> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Comment>>() {
        }.getType());
        return dataResult.data;
    }

}
