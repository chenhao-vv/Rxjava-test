package com.vivo.chmusicdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataBase {
    private static final String TAG = "WeatherDatabase";
    private static final String DB_NAME = "weather";

    private static final int VERSION = 1;
    private static WeatherDataBase sWeatherDB;
    private SQLiteDatabase mSQLiteDatabase;

    private WeatherDataBase(Context context) {
        WeatherOpenHelper dbHelper = new WeatherOpenHelper(context,DB_NAME, null, VERSION);
        mSQLiteDatabase = dbHelper.getWritableDatabase();
    }

    //单例模式注意不能这么写
    public synchronized static WeatherDataBase getInstance(Context context) {
        if(sWeatherDB == null) {
            sWeatherDB = new WeatherDataBase(context);
        }
        return sWeatherDB;
    }

    /**
     * 储存Province实例
     */
    public void saveProvince(Province province) {
        if(province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getmProvinceName());
            values.put("province_code", province.getmProvinceCode());
            mSQLiteDatabase.insert("Province",null, values);
        }
    }

    /**
     * 读取province
     */
    public List<Province> loadProvince() {
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("Province", null, null, null, null, null, null, null);
        Log.d(TAG, "Province ID: " + cursor.getColumnIndex("id"));
        try{
            if(cursor != null && cursor.moveToFirst()) {
                do {
                    Province province = new Province();
                    province.setmId(cursor.getInt(cursor.getColumnIndex("id")));
                    province.setmProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                    province.setmProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                    provinceList.add(province);
                }while(cursor.moveToNext());
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return provinceList;
    }

    /**
     * 存储city
     */
    public void saveCity(City city) {
        if(city != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name", city.getmCityName());
            contentValues.put("city_code", city.getmCityCode());
            contentValues.put("province_id", city.getmProvinceId());
            mSQLiteDatabase.insert("City", null, contentValues);
        }
    }

    public List<City> loadCity(int provinceID) {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("CIty", null, "province_id=?", new String[]{String.valueOf(provinceID)}, null, null, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    City city = new City();
                    city.setmId(cursor.getInt(cursor.getColumnIndex("id")));
                    city.setmCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                    city.setmCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                    city.setmProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                    cityList.add(city);
                }while(cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return cityList;
    }

    public void saveCounty(County county) {
        if(county != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name", county.getmCountyName());
            contentValues.put("county_code", county.getmCountyCode());
            contentValues.put("city_id", county.getmCityId());
            mSQLiteDatabase.insert("County", null, contentValues);
        }
    }

    public List<County> loadCounty(int cityId) {
        List<County> countyList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    County county = new County();
                    county.setmCountyId(cursor.getInt(cursor.getColumnIndex("id")));
                    county.setmCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                    county.setmCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                    county.setmCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                    countyList.add(county);
                }while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return countyList;
    }

}
