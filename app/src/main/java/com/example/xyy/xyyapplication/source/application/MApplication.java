package com.example.xyy.xyyapplication.source.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;

import lombok.Data;

/**
 * Created by admin on 16/4/29.
 */
public class MApplication extends Application {
    //是否有用户登录状态
    public static Boolean isLogin = false;
    //当前登录用户
    public static UserVO currentLoginUser = null;
    //当前是否是管理员登录
    public static Boolean isAdmin = false;
    //数据库锁
    public static Object SQL_LOCK = new Object();
    //服务器ip地址
    public static String IP_SERVICE = "http://192.168.1.105:8080/";
    //服务器地址存放
    public static String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    public static String SHARE_PREFERENCE_IP_KEY = "SHARE_PREFERENCE_IP_KEY";

    public static RequestQueue mQueue;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mQueue = Volley.newRequestQueue(this);
        try{
            SharedPreferences sharedPreferences = getSharedPreferences(MApplication.SHARE_PREFERENCE, Context.MODE_PRIVATE);
            String s = sharedPreferences.getString(MApplication.SHARE_PREFERENCE_IP_KEY, "");
            MApplication.IP_SERVICE = s;
        }catch (Exception e){

        }
    }
}
