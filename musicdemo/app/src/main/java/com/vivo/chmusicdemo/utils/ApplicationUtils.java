package com.vivo.chmusicdemo.utils;

import android.app.Application;
import android.content.Context;

public class ApplicationUtils extends Application {
    private static ApplicationUtils mInstance;
    /**
     * 获取context
     * @return
     */
    public static Context getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationUtils();
        }
        return mInstance;
    }

}

