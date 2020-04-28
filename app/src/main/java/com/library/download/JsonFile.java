package com.library.download;

import com.google.gson.Gson;
import com.library.download.listeners.DownloadCallback;
import com.library.download.model.File;
import com.library.download.utils.FileType;

import java.lang.reflect.Type;

public class JsonFile extends File {
    public JsonFile(String url, DownloadCallback downloadCallback) {
        super(url, FileType.JSON, downloadCallback);
    }

    @Override
    public File getCallbacks(DownloadCallback downloadCallback) {
        File file = new JsonFile(getUrl(), downloadCallback);
        return file;
    }

    public Object getJson(Type type) {
        Gson gson = new Gson();
        String s= "";
        try {
            s = new String(getData(), "UTF-8");
        } catch (Exception e) { }
        return gson.fromJson(s, type);
    }
}
