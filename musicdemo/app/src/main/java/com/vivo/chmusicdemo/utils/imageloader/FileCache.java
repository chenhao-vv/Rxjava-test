package com.vivo.chmusicdemo.utils.imageloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 二级缓存，磁盘缓存
 */
public class FileCache {
    //缓存文件目录
    private File mCacheDir;
    /**
     * 创建缓存文件目录，如果有SD卡，则使用SD，如果没有则使用系统自带的缓存目录
     */
    public FileCache(Context context, File cacheDir, String dir) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(cacheDir, dir);
        } else {
            mCacheDir = context.getCacheDir();//如何获取系统内置的缓存路径
        }
        if(!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        File file = null;
        try {
            //对URL进行编辑，解决中文路径问题
            String fileName = URLEncoder.encode(url, "utf-8");
            file = new File(mCacheDir, fileName);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return file;
    }

    //清楚缓存文件
    public void clear() {
        File[] files = mCacheDir.listFiles();
        for(File f : files) {
            f.delete();
        }
    }
}
