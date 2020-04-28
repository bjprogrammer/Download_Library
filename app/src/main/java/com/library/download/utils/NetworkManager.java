package com.library.download.utils;

import com.library.download.listeners.CachingCallbacks;
import com.library.download.model.File;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class NetworkManager {
    public AsyncHttpClient get(final File file, final CachingCallbacks CachingCallbacks) {
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(file.getUrl(), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                file.getDownloadCallback().onStart(file);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                file.setData(response);
                file.getDownloadCallback().onSuccess(file);
                CachingCallbacks.onCompleted(file);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                file.getDownloadCallback().onFailure(file, statusCode, errorResponse, e);
            }

            @Override
            public void onRetry(int retryNo) { }

            @Override
            public void onCancel() {
                super.onCancel();
                file.getDownloadCallback().onCancelled(file);
            }
        });

        return client;
    }
}


