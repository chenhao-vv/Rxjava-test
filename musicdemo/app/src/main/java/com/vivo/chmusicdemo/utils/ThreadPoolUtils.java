package com.vivo.chmusicdemo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {

    private static volatile ThreadPoolUtils sInstance = null;
    private ExecutorService mCacheThreadPool;

    private ThreadPoolUtils() {
        mCacheThreadPool = Executors.newCachedThreadPool();
    }

    public static ThreadPoolUtils getInstance() {
        if(sInstance == null) {
            synchronized (ThreadPoolUtils.class) {
                if(sInstance == null) {
                    sInstance = new ThreadPoolUtils();
                }
            }
        }
        return sInstance;
    }

    public ExecutorService getCacheThreadPool() {
        return mCacheThreadPool;
    }
}


