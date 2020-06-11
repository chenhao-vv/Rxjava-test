package com.vivo.chmusicdemo.utils.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncImageLoader {

    private static final String TAG = "AsyncImage";
    //内存缓存
    private MemoryCache mMemoryCache;
    //文件缓存
    private FileCache mFileCache;
    //线程池
    private ExecutorService mThreadPool;
    //记录已经加载图片的ImageView
    private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    //保存正在加载图片的url
    private List<LoadPhotoTask> mTaskQueue = new ArrayList<LoadPhotoTask>();

    //判断使用哪一个构造函数
    private static final int EXPAN_CONSTRU = 0;
    private static final int NON_EXPAN_CONSTRU = 1;

    private int mCount;

    /**
     * 默认采用一个大小为5的线程池
     * @param context
     * @param memoryCache 所采用的高速缓存
     * @param fileCache 所采用的文件缓存
     */
    public AsyncImageLoader(Context context, MemoryCache memoryCache, FileCache fileCache) {
        mMemoryCache = memoryCache;
        mFileCache = fileCache;
        //建立容量为5的固定尺寸的线程池
        mThreadPool = Executors.newFixedThreadPool(5);
    }

    /**
     * 根据url加载相应的图片
     * @param imageView
     * @param url
     * @return 先从一级缓存取图片，有则直接返回，没有则异步从文件中取，如果再没有就从网络获取
     */
    public Bitmap loadBitmap(ImageView imageView, String url) {
        //先将Image View记录到Map中，表示该ui已经执行过图片加载了
        mImageViews.put(imageView, url);
        //先从一级缓存获取
        Bitmap bitmap = mMemoryCache.get(url);
        if(bitmap == null) {
            enqueueLoadPhoto(url, imageView);//再从二级缓存和网络读取
        }
        mCount = NON_EXPAN_CONSTRU;
        return bitmap;
    }

    private void enqueueLoadPhoto(String url, ImageView imageView) {
        //如果任务已经存在，则不重新加载
        if(isTaskExisted(url)) {
            return;
        }
        LoadPhotoTask task = new LoadPhotoTask(url, imageView);
        synchronized (mTaskQueue) {
            mTaskQueue.add(task);
        }
        //向线程池中提交任务，如果没有达到上限，则运行，否则会阻塞
        mThreadPool.execute(task);
    }

    /**
     * 判断下载队列中是否已经存在任务
     * @param url
     * @return
     */
    private boolean isTaskExisted(String url) {
        if(url == null) {
            return false;
        }
        synchronized (mTaskQueue) {
            int size = mTaskQueue.size();
            for(int i = 0; i < size; i++) {
                LoadPhotoTask task = mTaskQueue.get(i);
                if(task != null && task.getUrl().equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从缓存文件或者网络端获取图片
     */
    private Bitmap getBitmapByUrl(String url) {
        //获取缓存图片路径
        File file = mFileCache.getFile(url);
        //获取文件的Bitmap信息
        Bitmap bitmap = ImageUtil.decodeFile(file);
        if(bitmap != null) {
            return bitmap;
        }
        //找不到则从网络获取
        return ImageUtil.loadBitmapFromWeb(url, file);
    }

    /**
     * 判断该Image View是否已经加载过图片
     * （可用于判断是否需要进行加载图片）
     */
    private boolean imageViewReused(ImageView imageView, String url) {
        String tag = mImageViews.get(imageView);
        if(tag == null || !tag.equals(url)) {
            return true;    //反了吧？？？
        }
        return false;
    }

    private void removeTask(LoadPhotoTask task) {
        synchronized (mTaskQueue) {
            mTaskQueue.remove(task);
        }
    }


    class LoadPhotoTask implements Runnable {
        private String mUrl;
        private ImageView mImageView;

        /**
         *
         * @param url
         * @param imageView
         */
        public LoadPhotoTask(String url, ImageView imageView) {
            mUrl = url;
            mImageView = imageView;
        }

        @Override
        public void run() {
            //判断Image View是否已经被复用
            if(imageViewReused(mImageView, mUrl)) {
                removeTask(this);
                return;
            }
            Bitmap bitmap = getBitmapByUrl(mUrl);
            mMemoryCache.put(mUrl, bitmap);//将图片放入一级缓存
            //若Image View未加图片，则在ui线程显示图片
            if(!imageViewReused(mImageView, mUrl)) {
                BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(bitmap, mImageView, mUrl);
                Activity activity = (Activity) mImageView.getContext();
                //在ui线程调用bitmapplayer的run方法，实现Image view加载图片
                activity.runOnUiThread(bitmapDisplayer);
            }
            //加载完成则移除任务
            removeTask(this);
        }
        public String getUrl() {
            return mUrl;
        }
    }

    /**
     * 由Ui线程执行该组件的run 方法
     */
    class BitmapDisplayer implements Runnable {
        private Bitmap mBitmap;
        private ImageView mImageView;
        private String mUrl;
        public BitmapDisplayer(Bitmap bitmap, ImageView imageView, String url) {
            mBitmap = bitmap;
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        public void run() {
            if(imageViewReused(mImageView, mUrl)) {
                return;
            }
            if(mBitmap != null) {
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }

    /**
     * 释放资源
     */
    public void destroy() {
        mMemoryCache.clear();
        mMemoryCache = null;
        mImageViews.clear();
        mImageViews = null;
        mTaskQueue.clear();
        mTaskQueue = null;
        mThreadPool.shutdown();
        mThreadPool = null;
    }
}
