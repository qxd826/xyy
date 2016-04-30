package com.example.xyy.xyyapplication.source.application;

import android.app.Application;

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

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }
}
