package com.example.xyy.xyyapplication.source.application;

import android.app.Application;

import com.example.xyy.xyyapplication.source.pojo.user.User;

import lombok.Data;

/**
 * Created by admin on 16/4/29.
 */
public class MApplication extends Application {
    //是否有用户登录状态
    private Boolean isLogin = false;
    //当前登录用户
    private User user = null;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public Boolean getIsLogin(){
        return isLogin;
    }
    public void setIsLogin(Boolean mIsLogin){
        this.isLogin = mIsLogin;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User mUser){
        this.user = mUser;
    }
}
