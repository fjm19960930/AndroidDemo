package com.pivot.myandroiddemo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pivot.myandroiddemo.sql.MyDataBaseHelper;

/**
 * 用户表数据库处理工具类
 */
public class UserDbUtils {
    private SQLiteDatabase db;

    public UserDbUtils(Context context) {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(context, "MyLife.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 注册新用户的数据
     */
    public long register(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("todaystep", 0);
        return db.insert("User", null, values);
    }

    /**
     * 查询数据库中是否已经存在该用户名
     */
    public boolean isExist(String username) {
        Cursor cursor = db.query("User", new String[]{"username"}, "username=?", new String[]{username}, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    /**
     * 查询今日步数
     */
    public int queryTodayStep(String username) {
        Cursor cursor = db.query("User", new String[]{"todaystep"}, "username=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * 更新今日步数
     */
    public void updateTodayStep(String username, int step) {
        ContentValues values = new ContentValues();
        values.put("todaystep", step);
        db.update("User", values, "username=?", new String[]{username});
    }
    
    /**
     * 查询登录用户的密码并返回
     */
    public Cursor queryPwd(String username) {
        return db.query("User", new String[]{"username", "password"}, "username=?", new String[]{username}, null, null, null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
