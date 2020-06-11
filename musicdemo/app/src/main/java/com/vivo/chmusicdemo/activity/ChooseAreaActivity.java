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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vivo.chmusicdemo.R;
import com.vivo.chmusicdemo.database.City;
import com.vivo.chmusicdemo.database.County;
import com.vivo.chmusicdemo.database.Province;
import com.vivo.chmusicdemo.database.WeatherDataBase;
import com.vivo.chmusicdemo.utils.HttpUtil;
import com.vivo.chmusicdemo.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseAreaActivity extends AppCompatActivity {
    private static final String TAG = "ChooseAreaActivity";

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private TextView mTitleText;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private WeatherDataBase mWeatherDB;
    private List<String> mDataList = new ArrayList<>();
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;

    private Province mSelectProvince;
    private City mSelectCity;
    private County mSelectCounty;

    private int mCurrnetLevel;
    private boolean mIsFromWeatherActivity;

    //time
    private long mCurrentBackTime;
    private long mLastBackTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        Log.d(TAG, "mISFromWeatherActivity: " + mIsFromWeatherActivity);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChooseAreaActivity.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_choose_area);

        mListView = (ListView)findViewById(R.id.list_view);
        mTitleText = (TextView)findViewById(R.id.title_text);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(mAdapter);

        mWeatherDB = WeatherDataBase.getInstance(this);//单例模式不能这么写，饿汉与懒汉看一看
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mCurrnetLevel == LEVEL_PROVINCE) {
                    mSelectProvince = mProvinceList.get(position);
                    queryCities();
                } else if(mCurrnetLevel == LEVEL_CITY) {
                    mSelectCity = mCityList.get(position);
                    queryCounties();
                } else if (mCurrnetLevel == LEVEL_COUNTY) {
                    String countyCode = mCountyList.get(position).getmCountyCode();
                    Log.d(TAG, "CountyCode: " + countyCode);
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code", countyCode);
                    try {
                        startActivity(intent);
                    } catch(ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        mProvinceList = mWeatherDB.loadProvince();
        if(mProvinceList.size() > 0) {
            mDataList.clear();
            for(Province province : mProvinceList) {
                mDataList.add(province.getmProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(getApplication().getString(R.string.china));
            mCurrnetLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    private void queryCities() {
        mCityList = mWeatherDB.loadCity(mSelectProvince.getmId());
        if(mCityList.size() > 0 ) {
            mDataList.clear();
            for(City city : mCityList) {
                mDataList.add(city.getmCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(mSelectProvince.getmProvinceName());
            mCurrnetLevel = LEVEL_CITY;
        } else {
            queryFromServer(mSelectProvince.getmProvinceCode(), "city");
        }
    }

    private void queryCounties() {
        mCountyList = mWeatherDB.loadCounty(mSelectCity.getmId());
        if(mCountyList.size() > 0 ) {
            mDataList.clear();
            for(County county : mCountyList) {
                mDataList.add(county.getmCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mTitleText.setText(mSelectCity.getmCityName());
            mCurrnetLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(mSelectCity.getmCityCode(), "county");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        if(!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)) {
                    result = Utility.handleProvinceResponse(mWeatherDB, response);
                } else if("city".equals(type)) {
                    result = Utility.handleCityResponse(mWeatherDB, response, mSelectProvince.getmId());
                } else if("county".equals(type)) {
                    result = Utility.handleCountyResponse(mWeatherDB, response, mSelectCity.getmId());
                }
                if(result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChooseAreaActivity.this, getApplication().getString(R.string.query_failed), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        if(mCurrnetLevel == LEVEL_COUNTY) {
            queryCities();
        } else if(mCurrnetLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            if(mIsFromWeatherActivity) {
                Log.d(TAG, "IsFromWeatherActivity");
                Intent intent = new Intent(this, WeatherActivity.class);
                try{
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(mCurrnetLevel == LEVEL_PROVINCE) {
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
        }

        return super.onKeyDown(keyCode, event);
    }
}
