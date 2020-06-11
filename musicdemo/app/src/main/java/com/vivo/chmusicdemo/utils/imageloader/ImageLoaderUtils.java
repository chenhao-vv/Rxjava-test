package com.vivo.chmusicdemo.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.vivo.chmusicdemo.utils.picture_save_volley.MD5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class ImageLoaderUtils {

    private static final int MAX_CAPACITY = 20;
    private static volatile ImageLoaderUtils sInstance = null;
    private static Context mContext;

    private static final LinkedHashMap<String, SoftReference<Bitmap>> firstCacheMap = new LinkedHashMap<String, SoftReference<Bitmap>>(MAX_CAPACITY) {
      protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
          if(this.size() > MAX_CAPACITY) {
              return true;
          } else {
              diskCache(eldest.getKey(), eldest.getValue());
              return false;
          }
      }
    };

    /**
     * 单例模式加载ImageLoaderutils
     * @return
     */
    public static final ImageLoaderUtils getInstance() {
        if(sInstance == null) {
            synchronized (ImageLoaderUtils.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoaderUtils();
                }
            }
        }
        return sInstance;
    }

    private ImageLoaderUtils() {}

    /**
     * 加载图片到对应组件
     * @param imageView
     * @param url
     */
    public void loadImage(ImageView imageView, String url, Context context) {
        mContext = context;
        synchronized (imageView) {
            Bitmap bitmap = getFromCache(url);
            if(bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                new ImageLoaderTask(imageView).execute(url);
            }
        }
    }

    /**
     * 判断缓存中是否已经存在，若存在，则直接取出
     * @param url
     * @return
     */
    private Bitmap getFromCache(String url) {
        synchronized (firstCacheMap) {
            if(firstCacheMap.get(url) != null) {
                Bitmap bitmap = firstCacheMap.get(url).get();
                if(bitmap != null) {
                    firstCacheMap.put(url, new SoftReference<Bitmap>(bitmap));
                    return bitmap;
                }
            }
        }

        Bitmap bitmap = getFromLocalSD(url);
        if(bitmap != null) {
            firstCacheMap.put(url, new SoftReference<Bitmap>(bitmap));
            return bitmap;
        }
        return null;
    }

    /**
     * 判断本地磁盘中是否已经有了该图片，如果有了就从本地磁盘中取出
     * @param key
     * @return
     */
    private Bitmap getFromLocalSD(String key) {
//        String fileName = MD5Util.getMD5Str(key);
        if (key == null) {// 如果文件名为Null，直接返回null
            return null;
        } else {
            String filePath = mContext.getCacheDir().getAbsolutePath() + File.separator + key;
            InputStream is = null;
            try {
                is = new FileInputStream(new File(filePath));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 把图片缓存到本地磁盘，拿到图片，写到SD卡中
     *
     * @param key 图片的URL
     * @param value Bitmap
     */
    private static void diskCache(String key, SoftReference<Bitmap> value) {
        // 把写入SD的图片名字改为基于MD5加密算法加密后的名字
//        String fileName = MD5Util.getMD5Str(key);
        String filePath = mContext.getCacheDir().getAbsolutePath() + File.separator + key;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            if (value.get() != null) {
                value.get().compress(Bitmap.CompressFormat.JPEG, 60, os);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @ClassName: MyAsyncImageLoaderTask
     * @Description: 异步加载图片
     * @author
     */
    class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;// 图片组件
        private String key;//图片路径

        public ImageLoaderTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.key = params[0];// 图片的路径
            Bitmap bitmap = castielDownload(key);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {// 说明已经下载下来了
                addFirstCache(key,result);
                imageView.setImageBitmap(result);// 加载网络中的图片
            }
        }
    }

    /**
     * 根据图片路径执行图片下载
     * @param key
     * @return
     */
    public Bitmap castielDownload(String key) {
        InputStream is = null;
        try {
            is = HttpUtils.castielDownLoad(key);
            return BitmapFactory.decodeStream(is);// InputStream这种加载方式暂用内存最小
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加到缓存中去
     * @param key
     * @param result
     */
    public void addFirstCache(String key, Bitmap result) {
        if (result != null) {
            synchronized (firstCacheMap) {
                firstCacheMap.put(key, new SoftReference<Bitmap>(result));
            }
        }
    }
}
