package com.vivo.chmusicdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.vivo.chmusicdemo.database.City;
import com.vivo.chmusicdemo.database.County;
import com.vivo.chmusicdemo.database.Province;
import com.vivo.chmusicdemo.database.WeatherDataBase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    private static final String TAG = "Utility";

    public synchronized static boolean handleProvinceResponse(WeatherDataBase weatherDataBase, String response) {
        if(!TextUtils.isEmpty(response)) {
            Log.d(TAG, "response: " + response);
            String[] allProvince = response.split(",");
            if(allProvince != null && allProvince.length > 0) {
                for(String p : allProvince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setmProvinceName(array[1]);
                    province.setmProvinceCode(array[0]);
                    weatherDataBase.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCityResponse(WeatherDataBase weatherDataBase, String response, int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0) {
                for(String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setmCityCode(array[0]);
                    city.setmCityName(array[1]);
                    city.setmProvinceId(provinceId);
                    weatherDataBase.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(WeatherDataBase weatherDataBase, String response, int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0 ) {
                for(String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setmCountyCode(array[0]);
                    county.setmCountyName(array[1]);
                    county.setmCityId(cityId);
                    weatherDataBase.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务返回的json数据，并将解析的结果存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到sharedPreference中
     */
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp,String publishTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_Desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", simpleDateFormat.format(new Date()));
        editor.commit();
    }
}
