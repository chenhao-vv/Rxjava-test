package com.vivo.chmusicdemo.utils.imageloader;


import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一级缓存，内存缓存
 */
public class MemoryCache {
    //最大缓存数
    private static final int MAX_CACHE_CAPACITY = 30;
    //用Map软引用的Bitmap对象，保障内存控件足够的情况下不会被垃圾回收
    private HashMap<String, SoftReference<Bitmap>> mCacheMap = new LinkedHashMap<String, SoftReference<Bitmap>>() {
        private static final long serialVersionUID = 1L;
        //当缓存数量超过规定大小（返回true）会清楚最先放入缓存的
        protected  boolean removeOldestEntry(Entry<String, SoftReference<Bitmap>> oldest) {
            return size() > MAX_CACHE_CAPACITY;
        }
    };

    /**
     * 从缓存去图片
     * 如果缓存有，并且没有被释放，则返回该图片，否则返回null
     */
    public Bitmap get(String id) {
        if(!mCacheMap.containsKey(id)) {
            return null;
        }
        SoftReference<Bitmap> ref = mCacheMap.get(id);
        return ref.get();
    }

    /**
     * 将图片加入缓存
     */
    public void put(String id, Bitmap bitmap) {
        mCacheMap.put(id, new SoftReference<Bitmap>(bitmap));
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        try{
            for(Map.Entry<String, SoftReference<Bitmap>>entry : mCacheMap.entrySet()) {
                SoftReference<Bitmap> sr = entry.getValue();
                if(null != sr) {
                    Bitmap bitmap = sr.get();
                    if(null != bitmap) {
                        bitmap.recycle();
                    }
                }
            }
            mCacheMap.clear();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
