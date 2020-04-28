package com.library.download.listeners;

import com.library.download.model.File;

public interface CachingCallbacks {
    void onCompleted(File file);
}
