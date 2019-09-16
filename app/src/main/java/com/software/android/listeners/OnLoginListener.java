package com.software.android.listeners;

import com.software.android.cameraapp.Permission;

import java.util.List;

/**
 * On login/logout actions listener
 *
 * 
 */
public interface OnLoginListener extends OnErrorListener {

    void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions);

    void onCancel();

}