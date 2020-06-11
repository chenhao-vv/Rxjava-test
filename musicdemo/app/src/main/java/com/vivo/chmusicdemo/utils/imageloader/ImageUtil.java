package com.vivo.chmusicdemo.utils.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageUtil {

    private static final int BUFFER_SIZE = 1024;
    private static final int NUMBER_ZERO = 0;
    private static final int WAIT_TIME = 30000;

    //xml中默认图片的大小
    private static final int IMAGE_DEFAULT_SIZE = 60;

    /**
     * 从网络获取图片，并缓存在指定的文件中
     * @param url
     * @param file
     * @return
     */
    public static Bitmap loadBitmapFromWeb(String url, File file) {
        Bitmap bmp = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            is = response.body().byteStream();
            os = new FileOutputStream(file);
            copyStream(is, os);//将图片缓存至磁盘
            bmp = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }


//
//        HttpURLConnection mConnection = null;
//        InputStream mIs = null;
//        OutputStream mOs =null;
//        try {
//            Bitmap bitmap = null;
//            URL imageUrl = new URL(url);
//            mConnection = (HttpURLConnection) imageUrl.openConnection();
//            mConnection.setConnectTimeout(WAIT_TIME);
//            mConnection.setReadTimeout(WAIT_TIME);
//            mConnection.setInstanceFollowRedirects(false);
//            mConnection.setRequestMethod("GET");
//            mConnection.connect();
//            //网络重定向
//            if(mConnection.getResponseCode() == 302){
//                String location = mConnection.getHeaderField("Location");
//                //临时重定向与永久重定向的大小写有区分
//                if(TextUtils.isEmpty(location)) {
//                    location = mConnection.getHeaderField("location");
//                }
//                //有时候会省略host， 只返回后面的path，所以需要补全url
//                if(!(location.startsWith("http://") || location.startsWith("https://"))){
//                    location = imageUrl.getProtocol() + "://" + imageUrl.getHost() + ":" + imageUrl.getPort() + location;
//                }
//                mConnection.disconnect();
//                mConnection = (HttpURLConnection)new URL(location).openConnection();
//                mConnection.setConnectTimeout(15000);
//                mConnection.setReadTimeout(15000);
//                mConnection.setRequestMethod("GET");
//                mConnection.connect();
////                if(mConnection.getResponseCode() == 200) {
////                    mIs = mConnection.getInputStream();
////                    mOs = new FileOutputStream(file);
////                    copyStream(mIs, mOs);//将图片缓存至磁盘
////                    bitmap = BitmapFactory.decodeStream(mIs);//读取图像数据
//////                bitmap = decodeFile(file);
////                }
//            }
//            if(mConnection.getResponseCode() == 200) {
//                mIs = mConnection.getInputStream();
//                mOs = new FileOutputStream(file);
//                copyStream(mConnection.getInputStream(), new FileOutputStream(file));
//                copyStream(mIs, mOs);//将图片缓存至磁盘
////                bitmap = BitmapFactory.decodeStream(mIs);//读取图像数据
//                bitmap = decodeFile(file);
//            }
//            return bitmap;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            try {
//                if(mOs != null) {
//                    mOs.close();
//                }
//                if(mIs != null) {
//                    mIs.close();
//                }
//                if(mConnection != null) {
//                    mConnection.disconnect();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    public static Bitmap decodeFile(File file) {
        try {
            //压缩照片
            FileInputStream fileInputStream = new FileInputStream(file);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            Bitmap bitmap = decodeFromFileDescription(fileDescriptor, IMAGE_DEFAULT_SIZE, IMAGE_DEFAULT_SIZE);
            if(bitmap == null) {
                Log.d("chenhao", "BitmapFactory.decodeStream(fileInputStream, null, options); is null");
            }
            return bitmap;


            //默认方法，不压缩
//            return BitmapFactory.decodeStream(new FileInputStream(file), null, null);
        } catch (FileNotFoundException e) {
//            Log.d(TAG , "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //decode from FileDescriptro
    private static Bitmap decodeFromFileDescription(FileDescriptor fd, int reqHeight, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    private static byte[] getBytes(FileInputStream is) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = -1;
        while((len = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0 , len);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        if(reqHeight == 0 || reqWidth == 0) {
            return 1;
        }
        //获取图片的原始宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d("chenhao", "Image Raw Height = " + height + " , raw width = " + width);
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //计算采样率
            while((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d("chenhao", "inSampleSize = " + inSampleSize);
        return inSampleSize;
    }

    private static void copyStream(InputStream is, OutputStream os) {
//        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            for(;;) {
                int count = is.read(bytes, NUMBER_ZERO, BUFFER_SIZE);
                if(count == -1) {
                    break;
                }
                os.write(bytes, NUMBER_ZERO , count);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
