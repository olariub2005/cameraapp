package com.software.android.listeners;

/**
 * On authorization request listener
 */
public interface OnAuthorizationDeviceListener extends OnThinkingListetener {

    void onComplete(String accessToken);
}
