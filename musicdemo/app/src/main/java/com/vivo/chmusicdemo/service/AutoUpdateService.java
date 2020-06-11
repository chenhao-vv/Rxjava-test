package com.vivo.chmusicdemo.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.vivo.chmusicdemo.receiver.AutoUpdateReceiver;
import com.vivo.chmusicdemo.utils.HttpUtil;
import com.vivo.chmusicdemo.utils.Utility;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000 * 8;//8小时
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent intent1 = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent1, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = sharedPreferences.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
