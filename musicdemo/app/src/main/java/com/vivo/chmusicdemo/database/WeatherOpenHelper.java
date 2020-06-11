package com.vivo.chmusicdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WeatherOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "WeatherOpenHelper";
    /**
     * Province建表
     */
    public static final String CREATE_PROVINCE = "create table Province("
            + "id integer primary key autoincrement,"
            + "province_name text,"
            + "province_code text)";

    /**
     * city建表
     */
    public static final String CREATE_CITY = "create table City("
            + "id integer primary key autoincrement,"
            + "city_name text,"
            + "city_code text,"
            + "province_id integer)";//city关联province的外键
    /**
     * country建表
     */
    public static final String CREATE_COUNTY ="create table County("
            + "id integer primary key autoincrement,"
            + "county_name text,"
            + "county_code text,"
            + "city_id integer)";//county关联city的外键

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        Log.d(TAG, "province created");
        db.execSQL(CREATE_CITY);
        Log.d(TAG, "city created");
        db.execSQL(CREATE_COUNTY);
        Log.d(TAG, "county created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
