package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.FamilyUser;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

/**
 * 
 * // @see https://developers.facebook.com/docs/graph-api/reference/user/family/
 */
public class GetFamilyAction extends GetAction<List<FamilyUser>> {

    public GetFamilyAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.FAMILY;
    }

    @Override
    protected List<FamilyUser> processResponse(GraphResponse response) {
        Utils.DataResult<FamilyUser> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<FamilyUser>>() {
        }.getType());
        return dataResult.data;
    }
}
