package com.pivot.myandroiddemo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pivot.myandroiddemo.base.ActivityParam;

/**
 * 创建用户表辅助类
 */
@ActivityParam(isShowToolBar = false)
public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_USER = "create table User (" 
            + "id integer primary key autoincrement,"
            + "username text,"
            + "password text,"
            + "todaystep integer)";
    
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);//创建名为User的表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//当数据库版本号更新时会调用此方法，即构造方法中的最后一个参数发生变化

    }
}
