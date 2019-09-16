package com.software.android.actions;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.software.android.cameraapp.SessionManager;
import com.software.android.listeners.OnDeleteListener;
import com.software.android.utils.Errors;
import com.software.android.utils.Errors.ErrorMsg;
import com.software.android.utils.Logger;

public class DeleteRequestAction extends AbstractAction {

    private OnDeleteListener mOnDeleteListener;
    private String mRequestId;

    public DeleteRequestAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setRequestId(String requestId) {
        mRequestId = requestId;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteRequestListener) {
        mOnDeleteListener = onDeleteRequestListener;
    }

    @Override
    protected void executeImpl() {
        if (sessionManager.isLogin()) {
            AccessToken accessToken = sessionManager.getAccessToken();
            GraphRequest request = new GraphRequest(accessToken, mRequestId, null, HttpMethod.DELETE, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Logger.logError(DeleteRequestAction.class, "failed to delete requests", error.getException());
                        if (mOnDeleteListener != null) {
                            mOnDeleteListener.onException(error.getException());
                        }
                    }
                    else {
                        if (mOnDeleteListener != null) {
                            mOnDeleteListener.onComplete(null);
                        }
                    }
                }
            });
            GraphRequest.executeBatchAsync(request);
        }
        else {
            String reason = Errors.getError(ErrorMsg.LOGIN);
            Logger.logError(DeleteRequestAction.class, reason, null);
            if (mOnDeleteListener != null) {
                mOnDeleteListener.onFail(reason);
            }
        }
    }

}
