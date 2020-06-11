package com.vivo.chmusicdemo.utils.imageloader;

import android.content.Context;

import java.io.File;

public class BitmapCache {

    private static volatile BitmapCache sInstance = null;

    private static Context mContext;
    public AsyncImageLoader mImageLoader;

    private BitmapCache (Context context) {
        mContext = context;
        initBitmapCache();
    }

    public static BitmapCache getsInstance(Context context) {
        if(sInstance == null) {
            synchronized (BitmapCache.class) {
                if(sInstance == null) {
                    sInstance = new BitmapCache(context);
                }
            }
        }
        return sInstance;
    }

    public void initBitmapCache() {
        //内存缓存
        MemoryCache memoryCache = new MemoryCache();
        //获得SD卡
        File sdCard = android.os.Environment.getExternalStorageDirectory();
        //缓存根目录
        File cacheDir = new File(sdCard, "image_cache");
        //文件缓存
        FileCache fileCache = new FileCache(mContext, cacheDir, "news_img");
        mImageLoader = new AsyncImageLoader(mContext, memoryCache, fileCache);
    }
}
