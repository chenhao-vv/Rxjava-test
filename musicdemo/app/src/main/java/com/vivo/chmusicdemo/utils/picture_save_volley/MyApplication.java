package com.vivo.chmusicdemo.utils.picture_save_volley;

import android.app.Application;

public class MyApplication extends Application {
    public static String TAG;
    public static MyApplication myApplication;

    public static MyApplication newInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getSimpleName();
        myApplication = this;

    }
}
