package com.example.xyy.xyyapplication.source.common;

import android.app.Activity;

import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

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
    public static void setCurrentLoginUser(UserVO user) {
        try {
            MApplication.currentLoginUser = user;
            MApplication.isLogin = true;
            if (StringUtils.equals(user.getIsAdmin(), Constant.IS_ADMIN)) {
                MApplication.isAdmin = true;
            } else {
                MApplication.isAdmin = false;
            }
        } catch (Exception e) {
            DebugLog.e("获取application对象失败......e:" + e.toString());
        }
    }

    //获取当前登录账号
    public static UserVO getCurrentLoginUser() {
        try {
            return MApplication.currentLoginUser;
        } catch (Exception e) {
            DebugLog.e("获取application对象失败......e:" + e.toString());
        }
        return null;
    }
}

