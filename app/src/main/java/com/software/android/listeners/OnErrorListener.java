package com.software.android.listeners;

public interface OnErrorListener {

    void onException(Throwable throwable);

    void onFail(String reason);
}