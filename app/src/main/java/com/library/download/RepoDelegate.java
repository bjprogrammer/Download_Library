package com.library.download;

import android.util.ArrayMap;

import com.library.download.listeners.DownloadCallback;
import com.library.download.listeners.CachingCallbacks;
import com.library.download.model.File;
import com.library.download.utils.CachingManager;
import com.library.download.utils.NetworkManager;

import java.util.LinkedList;
import java.util.Objects;

public class RepoDelegate {
    private static RepoDelegate instance;
    private ArrayMap<String, LinkedList<File>> allRequestsByUrl = new ArrayMap<>();
    private CachingManager cachingManager;

    private RepoDelegate() {
        cachingManager = CachingManager.getInstance();
    }

    public static RepoDelegate getInstance() {
        if (instance == null) {
            instance = new RepoDelegate();
        }
        return instance;
    }

    public void getRequest(final File file) {
        String mKey = file.getKey();

        // Check if file is already downloaded
        File fileFromCache = cachingManager.getFile(mKey);
        if (fileFromCache != null) {
            DownloadCallback downloadCallback = file.getDownloadCallback();
            downloadCallback.onStart(fileFromCache);
            downloadCallback.onSuccess(fileFromCache);
            return;
        }

        // Adding to linked list if same request(url) come again
        if(allRequestsByUrl.containsKey(mKey)){
            allRequestsByUrl.get(mKey).add(file);
        } else {
            LinkedList<File> files = new LinkedList<>();
            files.add(file);

            // Put it in the allRequestsByKey to manage it after done or cancel
            allRequestsByUrl.put(mKey, files);
        }

        // Updating the remaining requests in linked list when one request is updated
        File newFile = file.getCallbacks(new DownloadCallback() {
            @Override
            public void onStart(File mDownloadDataType) {
                for (File file : Objects.requireNonNull(allRequestsByUrl.get(mDownloadDataType.getKey()))) {
                    file.setData(mDownloadDataType.getData());
                    file.getDownloadCallback().onStart(file);
                }
            }

            @Override
            public void onSuccess(File mDownloadDataType) {
                for (File file : Objects.requireNonNull(allRequestsByUrl.get(mDownloadDataType.getKey()))) {
                    file.setData(mDownloadDataType.getData());
                    file.getDownloadCallback().onSuccess(file);
                }
                allRequestsByUrl.remove(mDownloadDataType.getKey());
            }

            @Override
            public void onFailure(File mDownloadDataType, int statusCode, byte[] errorResponse, Throwable e) {
                for (File file : Objects.requireNonNull(allRequestsByUrl.get(mDownloadDataType.getKey()))) {
                    file.setData(mDownloadDataType.getData());
                    file.getDownloadCallback().onFailure(file, statusCode, errorResponse, e);
                }
                allRequestsByUrl.remove(mDownloadDataType.getKey());
            }

            @Override
            public void onCancelled(File file) {
                Objects.requireNonNull(allRequestsByUrl.get(file.getKey())).remove(file);
            }
        });

        final NetworkManager networkManager = new NetworkManager();
        networkManager.get(newFile, new CachingCallbacks() {
            @Override
            public void onCompleted(File file) {
                // put in the cache when download completes
                if(allRequestsByUrl.containsKey(file.getKey())) {
                    cachingManager.insertIntoCache(file.getKey(), file);
                }
            }
        });

    }

    public void cancelRequest(File file){
        if(allRequestsByUrl.containsKey(file.getKey())) {
            allRequestsByUrl.get(file.getKey()).remove(file);
        }
    }

    public boolean allFileDownloaded(){
        return allRequestsByUrl.size() == 0;
    }

    public void clearCache(){
        cachingManager.clearCache();
    }
}
