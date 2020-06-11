package com.vivo.chmusicdemo.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.service.AutoUpdateService;
import com.vivo.chmusicdemo.utils.HttpUtil;
import com.vivo.chmusicdemo.utils.Utility;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WeatherActivity";
    //上次按下返回键的时间
    private long mLastBackTime = 0;
    //当下按下返回键的时间
    private long mCurrentBackTime = 0;
    private LinearLayout mWeatherInfoLayout;

    private TextView mCityName;
    private TextView mPublishTime;
    private TextView mWeatherDesp;
    private TextView mTemp1;
    private TextView mTemp2;
    private TextView mCurrentDate;

    private Button mSwitchCity;
    private Button mRefreshWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        init();
        String countyCode = getIntent().getStringExtra("county_code");
        Log.d(TAG, "countycode: " + countyCode);
        if(!countyCode.isEmpty()) {
            //有县级代号就去查询天气
            mPublishTime.setText(getApplication().getString(R.string.publish_time));
            mWeatherInfoLayout.setVisibility(View.INVISIBLE);
            mCityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            //没有县级代号就显示本地天气
            showWeather();
        }
    }

    public void init() {
        mWeatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        mCityName = (TextView)findViewById(R.id.city_name);
        mPublishTime = (TextView)findViewById(R.id.publish_text);
        mWeatherDesp = (TextView)findViewById(R.id.weather_desp);
        mTemp1 = (TextView)findViewById(R.id.temp1);
        mTemp2 = (TextView)findViewById(R.id.temp2);
        mCurrentDate = (TextView)findViewById(R.id.current_date);
        mSwitchCity = (Button)findViewById(R.id.switch_city);
        mRefreshWeather = (Button)findViewById(R.id.refresh_weather);
        mSwitchCity.setOnClickListener(this);
        mRefreshWeather.setOnClickListener(this);
    }

    private void showWeather() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCityName.setText(sharedPreferences.getString("city_name", ""));
        mTemp1.setText(sharedPreferences.getString("temp1", ""));
        mTemp2.setText(sharedPreferences.getString("temp2", ""));
        mWeatherDesp.setText(sharedPreferences.getString("weather_Desp", ""));
        mPublishTime.setText(sharedPreferences.getString("publish_time", ""));
        mCurrentDate.setText(sharedPreferences.getString("current_date", ""));
        mWeatherInfoLayout.setVisibility(View.VISIBLE);
        mCityName.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        try{
            startService(intent);
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address,"countyCode");
    }

    private void  queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type)) {
                    if(!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if(array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPublishTime.setText(getApplication().getString(R.string.publish_time_failed));
                    }
                });

            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        Log.d(TAG, "queryWeatherInfo address： " + address);
        queryFromServer(address, "weatherCode");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_city:
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.refresh_weather:
                mPublishTime.setText(getApplication().getString(R.string.publish_time));
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = sharedPreferences.getString("weather_code", "");
                if(!TextUtils.isEmpty(weatherCode)) {
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;

        }
    }

    //按两次返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            mCurrentBackTime = System.currentTimeMillis();
            //对比上次按下返回键的时间，如果大于两秒，则提示再按一次退出
            if(mCurrentBackTime - mLastBackTime > 2 * 1000) {
                Toast.makeText(this, getApplication().getString(R.string.back), Toast.LENGTH_SHORT).show();
                mLastBackTime = mCurrentBackTime;
            } else {
                //如果两次按下的时间小于2秒,退出程序(回到博客首页)
                //finish();
                Intent intent = new Intent(this, MainActivity.class);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
