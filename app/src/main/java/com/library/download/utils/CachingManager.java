package com.library.download.utils;

import android.util.LruCache;
import com.library.download.model.File;

public class CachingManager {
    private static CachingManager instance;
    private LruCache<String, File> cache;

    private CachingManager() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int maxCacheSize = maxMemory / 8;
        cache = new LruCache<>(maxCacheSize);
    }

    public static CachingManager getInstance() {
        if (instance == null) {
            instance = new CachingManager();
        }
        return instance;
    }

    public File getFile(String key) {
        return cache.get(key);
    }

    public boolean insertIntoCache(String key, File file) {
        return cache.put(key, file) != null;
    }

    public void clearCache() {
        cache.evictAll();
    }
}
