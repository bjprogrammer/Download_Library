package com.library.download.listeners;

import com.library.download.model.File;

public interface DownloadCallback {
    void onStart(File file);
    void onSuccess(File file);
    void onFailure(File file, int statusCode, byte[] errorResponse, Throwable e);
    void onCancelled(File file);
}
