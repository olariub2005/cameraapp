package com.software.android.actions;

import com.facebook.GraphResponse;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Attachment;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

/**
 * GetAttachmentAction.
 */
public class GetAttachmentAction extends GetAction<Attachment> {

    public GetAttachmentAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected String getGraphPath() {
        return getTarget() + "/" + GraphPath.ATTACHMENTS;
    }

    @Override
    protected Attachment processResponse(GraphResponse response) {
        List<Attachment> attachments = Utils.typedListFromResponse(response);
        if (attachments != null && attachments.size() > 0) {
            return attachments.get(0);
        }
        return new Attachment();
    }
}
