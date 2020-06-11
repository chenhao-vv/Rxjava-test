package com.vivo.chmusicdemo.database;

public class City {
    private int mId;
    private String mCityName;
    private String mCityCode;
    private int mProvinceId;

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        mId = id;
    }

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String name) {
        mCityName = name;
    }

    public String getmCityCode() {
        return mCityCode;
    }

    public void setmCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public int getmProvinceId() {
        return mProvinceId;
    }

    public void setmProvinceId(int id) {
        mProvinceId = id;
    }
}
