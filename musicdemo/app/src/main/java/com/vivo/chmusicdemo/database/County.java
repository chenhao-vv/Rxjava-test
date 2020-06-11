package com.vivo.chmusicdemo.database;

public class County {
    private int mCountyId;
    private String mCountyName;
    private String mCountyCode;
    private int mCityId;

    public int getmCountyId() {
        return mCountyId;
    }

    public void setmCountyId(int id) {
        mCountyId = id;
    }

    public String getmCountyName() {
        return mCountyName;
    }

    public void setmCountyName(String name) {
        mCountyName = name;
    }

    public String getmCountyCode() {
        return mCountyCode;
    }

    public void setmCountyCode(String countyCode) {
        mCountyCode = countyCode;
    }

    public int getmCityId() {
        return mCityId;
    }

    public void setmCityId(int cityId) {
        mCityId = cityId;
    }
}
