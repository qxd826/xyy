package com.example.xyy.xyyapplication.source.common;

import android.app.Activity;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.pojo.user.User;

/**
 * Created by admin on 16/4/30.
 */
public class ApplicationContextUtil {

    //获取application对象
    public static MApplication getApplication(Activity activity) {
        try {
            MApplication application = (MApplication) activity.getApplication();
            return application;
        } catch (Exception e) {
            DebugLog.e("获取application对象失败......e:" + e.toString());
        }
        return null;
    }

    //存入当前登录账户
    public static void setCurrentLoginUser(Activity activity, User user) {
        try {
            MApplication application = (MApplication) activity.getApplication();
            if(application != null){
                application.setIsLogin(true);
                application.setUser(user);
                return;
            }
        } catch (Exception e) {
            DebugLog.e("获取application对象失败......e:" + e.toString());
            return;
        }
    }
    //获取当前登录账号
    public static User getCurrentLoginUser(Activity activity){
        try {
            MApplication application = (MApplication) activity.getApplication();
            if(application != null){
                return application.getUser();
            }
        } catch (Exception e) {
            DebugLog.e("获取application对象失败......e:" + e.toString());
        }
        return null;
    }
}

