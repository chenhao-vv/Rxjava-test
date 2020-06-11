package com.vivo.chmusicdemo.utils.login;

import java.io.Serializable;

public class User implements Serializable {

    private int mId;
    private String mAccount;
    private String mPassword;

    public User() {
        super();
    }

    public User(String name, String password) {
        super();
        mAccount = name;
        mPassword = password;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int id) {
        mId = id;
    }

    public String getmAccount() {
        return mAccount;
    }

    public void  setmAccount(String name) {
        mAccount = name;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String password) {
        mPassword = password;
    }

    @Override
    public String toString() {
        String info = "user [id = " + mId + ",Account = " + mAccount + ",password = " + mPassword + "]";
        return info;
    }
}
