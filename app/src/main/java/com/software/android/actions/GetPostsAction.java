package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Post;
import com.software.android.entities.Post.PostType;
import com.software.android.utils.Utils;

import java.util.List;

public class GetPostsAction extends GetAction<List<Post>> {

    private PostType mPostType = PostType.ALL; // default

    public GetPostsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setPostType(PostType postType) {
        mPostType = postType;
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + mPostType.getGraphPath();
    }

    @Override
    protected List<Post> processResponse(GraphResponse response) {
        Utils.DataResult<Post> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Post>>() {
        }.getType());
        return dataResult.data;
    }

}
