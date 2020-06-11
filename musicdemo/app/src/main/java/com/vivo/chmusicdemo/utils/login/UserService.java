package com.vivo.chmusicdemo.utils.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UserService {

    private static final int FIRST_POSITION = 1;

    private ArrayList<String> mAccountList = new ArrayList<>();
    private DatabaseHelper mDbHelper;

    public UserService(Context context) {
        mDbHelper = new DatabaseHelper(context);
    }

    //登录时查询是否存在当前用户
    public boolean exiUser(String account, String password) {
        SQLiteDatabase sdb =mDbHelper.getWritableDatabase();
        String sql = "select * from user where account=? and password=?";
        Cursor cursor = sdb.rawQuery(sql, new String[]{account, password});
        if(cursor.moveToFirst() == true) {
            return true;
        }
        return false;
    }

    //注册时往数据库添加
    public boolean register(User user) {
        SQLiteDatabase sdb = mDbHelper.getWritableDatabase();
        String querySql = "select * from user where account=?";
        Cursor queryCursor = sdb.rawQuery(querySql, new String[]{user.getmAccount()});
        if(queryCursor.moveToFirst() == true) {
            return false;
        } else {
            String insertSql = "insert into user(account,password) values(?,?)";
            Object object[] = {user.getmAccount(),user.getmPassword()};
            sdb.execSQL(insertSql, object);
            return true;
        }
    }

    public ArrayList<String> getAll() {
        SQLiteDatabase sdb = mDbHelper.getWritableDatabase();
        //获得查询游标
        Cursor cursor = sdb.query("account", null, null, null, null, null, null);
        //判断游标是否为空
        if(cursor.moveToFirst()) {
            do {
                mAccountList.add(cursor.getString(FIRST_POSITION));
            }while(cursor.moveToNext());
            try{
                cursor.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return mAccountList;
    }

}
