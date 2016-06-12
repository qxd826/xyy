package com.example.xyy.xyyapplication.source.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.xyy.xyyapplication.source.pojo.user.User;

import lombok.Data;

/**
 * Created by admin on 16/4/29.
 */
public class MApplication extends Application {
    //是否有用户登录状态
    public static Boolean isLogin = false;
    //当前登录用户
    public static User currentLoginUser = null;
    //当前是否是管理员登录
    public static Boolean isAdmin = false;
    //数据库锁
    public static Object SQL_LOCK = new Object();
    //服务器ip地址
    public static String IP_SERVICE = "http://192.168.1.105:8080/";
    //服务器地址存放
    public static String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    public static String SHARE_PREFERENCE_IP_KEY = "SHARE_PREFERENCE_IP_KEY";


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }
}
