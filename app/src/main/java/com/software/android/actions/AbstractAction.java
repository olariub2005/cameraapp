package com.software.android.actions;

import com.software.android.cameraapp.SessionManager;
import com.software.android.cameraapp.SimpleFacebook;
import com.software.android.cameraapp.SimpleFacebookConfiguration;

public abstract class AbstractAction {

    protected SessionManager sessionManager;
    protected SimpleFacebookConfiguration configuration = SimpleFacebook.getConfiguration();

    public AbstractAction(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void execute() {
        executeImpl();
    }

    protected abstract void executeImpl();
}
