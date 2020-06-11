package com.vivo.chmusicdemo.adapter;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vivo.chmusicdemo.utils.ThemeHelper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themePre = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePre);
    }
}
