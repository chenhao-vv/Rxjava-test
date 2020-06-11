package com.vivo.chmusicdemo.utils.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String sDbName = "userInfo.db";
    private static int sDbVersion = 1;

    public DatabaseHelper(Context context) {
        super(context, sDbName, null, sDbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(id integer primary key, account varchar(80), password varchar(80))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
