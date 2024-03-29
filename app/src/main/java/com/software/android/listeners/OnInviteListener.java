package com.software.android.listeners;

import java.util.List;

public interface OnInviteListener extends OnErrorListener {
    void onComplete(List<String> invitedFriends, String requestId);

    void onCancel();
}
