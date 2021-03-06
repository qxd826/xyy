package com.example.xyy.xyyapplication.source.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 16/4/25.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "[DB]";

    public DBHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库（只会做一次，在程序安装的时候）
        Log.i(TAG, "数据库初始化");
        db.execSQL(DBConstant.CREATE_USER_SQL);
        db.execSQL(DBConstant.CREATE_USER_LOGIN_LOG_SQL);
        db.execSQL(DBConstant.CREATE_SUPPLY_SQL);
        db.execSQL(DBConstant.CREATE_CUSTOMER_SQL);
        db.execSQL(DBConstant.CREATE_GOODS_SQL);
        db.execSQL(DBConstant.CREATE_GOODS_LOG_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_USER_LOGIN_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_SUPPLY);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_GOODS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.TABLE_GOODS_LOG);
        onCreate(db);
    }
}
