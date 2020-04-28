package com.library.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.library.download.listeners.DownloadCallback;
import com.library.download.model.File;
import com.library.download.utils.FileType;

public class ImageFile extends File {

    public ImageFile(String url, DownloadCallback downloadCallback) {
        super(url, FileType.IMAGE, downloadCallback);
    }

    @Override
    public File getCallbacks(DownloadCallback downloadCallback) {
        File file = new ImageFile(this.getUrl(), downloadCallback);
        return file;
    }

    public Bitmap getBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(getData(), 0, getData().length);
        return bitmap;
    }
}
