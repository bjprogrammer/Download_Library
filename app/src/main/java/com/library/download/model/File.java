package com.library.download.model;

import com.library.download.listeners.DownloadCallback;
import com.library.download.utils.FileType;
import com.library.download.utils.HelperFunctions;

public abstract class File {
    private String url;
    private byte[] data;
    private FileType fileType;
    private DownloadCallback downloadCallback;
    private String key;

    public File(String url, FileType fileType, DownloadCallback downloadCallback){
        this.url = url;
        this.fileType = fileType;
        this.downloadCallback = downloadCallback;
        this.key = HelperFunctions.SHA1(this.url);
    }

    public String getKey() {
        return key;
    }
    
    public String getUrl() {
        return url;
    }

    public FileType getFileType() {
        return fileType;
    }

    public DownloadCallback getDownloadCallback() {
        return downloadCallback;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public abstract File getCallbacks(DownloadCallback downloadCallback);
}
