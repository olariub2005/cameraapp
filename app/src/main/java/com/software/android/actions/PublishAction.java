package com.software.android.actions;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.software.android.cameraapp.Permission;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Publishable;
import com.software.android.listeners.OnLoginListener;
import com.software.android.listeners.OnPublishListener;
import com.software.android.utils.Errors;
import com.software.android.utils.Logger;
import com.software.android.utils.Utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PublishAction extends AbstractAction {

    private OnPublishListener mOnPublishListener;
    private Publishable mPublishable;
    private byte[] video;
    private String mTarget = "me";
    private String fileName;

    public PublishAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setPublishable(Publishable publishable) {
        mPublishable = publishable;
    }

    public void setTarget(String target) {
        mTarget = target;
    }

    public void setOnPublishListener(OnPublishListener onPublishListener) {
        mOnPublishListener = onPublishListener;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected void executeImpl() {
        if (sessionManager.isLogin()) {
            if (!sessionManager.hasPendingRequest()) {
                // if we defined the publish permission

				/*
				 * We need also add one more check of next case: - if we gave
				 * extended permissions in runtime, but we don't have these
				 * permissions in the configuration
				 */
                final String neededPermission = mPublishable.getPermission().getValue();

                if (mOnPublishListener != null) {
                    mOnPublishListener.onThinking();
                }

                /*
                 * Check if session to facebook has needed publish
                 * permission. If not, we will ask user for this permission.
                 */
                if (!sessionManager.hasAccepted(neededPermission)) {
                    sessionManager.getLoginCallback().loginListener = new OnLoginListener() {

                        @Override
                        public void onException(Throwable throwable) {
                            returnFail(throwable != null ? String.valueOf(throwable.getMessage()) : "Got exception on asking for publish permissions");
                        }

                        @Override
                        public void onFail(String reason) {
                            returnFail(reason);
                        }

                        @Override
                        public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
                            if (sessionManager.hasAccepted(neededPermission)) {
                                publishImpl(mPublishable, mOnPublishListener);
                            }
                        }

                        @Override
                        public void onCancel() {
                            returnFail("User canceled the publish dialog");
                        }

                        private void returnFail(String reason) {
                            Logger.logError(PublishAction.class, reason, null);
                            if (mOnPublishListener != null) {
                                mOnPublishListener.onFail(reason);
                            }
                        }

                    };
                    // build the needed permission for this action and request
                    Permission permission = mPublishable.getPermission();
                    List<String> permissions = new ArrayList<String>();
                    permissions.add(permission.getValue());
                    sessionManager.requestPublishPermissions(permissions);

                } else {
                    publishImpl(mPublishable, mOnPublishListener);
                }

            } else {
                return;
            }
        } else {
            if (mOnPublishListener != null) {
                String reason = Errors.getError(Errors.ErrorMsg.LOGIN);
                Logger.logError(PublishAction.class, reason, null);
                mOnPublishListener.onFail(reason);
            }
        }
    }

    private void publishImpl(Publishable publishable, final OnPublishListener onPublishListener) {

        AccessToken accessToken = sessionManager.getAccessToken();
        //GraphRequest request = new GraphRequest(accessToken, mTarget + "/" + publishable.getPath(), publishable.getBundle(), HttpMethod.POST, new GraphRequest.Callback() {
        GraphRequest request = new GraphRequest(accessToken, mTarget + "/" + publishable.getPath(), null, HttpMethod.POST, new GraphRequest.Callback() {
              @Override
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    Logger.logError(GetAction.class, "Failed to publish", error.getException());
                    if (onPublishListener != null) {
                        onPublishListener.onException(error.getException());
                    }
                } else {
                    if (response.getRawResponse() == null) {
                        Logger.logError(GetAction.class, "The response GraphObject has null value. Response=" + response.toString(), null);
                    } else {
                        if (onPublishListener != null) {
                            JSONObject jsonObject = response.getJSONObject();
                            String id = jsonObject.optString("id");
                            onPublishListener.onComplete(id);
                        }
                    }
                }
            }
        });
        Bundle params = request.getParameters();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", "EVERYONE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            //byte[] data = readBytes("/storage/emulated/0/CrazySpeedRecorder/video_29-56-2017_11-56-38.mkv");
            //params.putByteArray(fileName, data);
            params.putByteArray(fileName, video);
            params.putString("title", "Albun Name - CrazySpeedRecorder movie");
            params.putString("description", "Video " + fileName + " recorded and pushed to Facebook with CrazySpeedRecorder application");
            params.putString("privacy", jsonObject.toString());
            params.putInt("file_size", video.length);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        request.setVersion(configuration.getGraphVersion());
        request.setParameters(params);
        GraphRequestAsyncTask task = new GraphRequestAsyncTask(request);
        task.execute();
    }

    public byte[] readBytes(String dataPath) throws IOException {

        InputStream inputStream = new FileInputStream(dataPath);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

}
