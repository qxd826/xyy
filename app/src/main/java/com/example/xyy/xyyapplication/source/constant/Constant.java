package com.example.xyy.xyyapplication.source.constant;

/**
 * Created by admin on 16/4/25.
 */
public class Constant {
    public static final String LOG_TAG = "xyy";

    //activity传值用
    public static final String USER_NAME = "user_name";
    public static final String USER_MOBILE = "user_mobile";
    public static final String GOODS_ID = "goods_id";
    public static final String GOODS_CODE = "goods_code";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String SUPPLY_ID = "supply_id";

    //INTENT CODE
    public static final int SUCCESS_CODE = 1;
    public static final int FALSE_CODE = 0;

    //是否是管理员
    public static final String IS_ADMIN = "1";
    public static final String NOT_ADMIN = "0";

    //添加用户类型
    public static final String ADD_USER_TYPE = "add_user_type";
    public static final int ADD_NORMAL_USER = 0;
    public static final int ADD_ADMIN_USER = 1;

    //添加供应商
    public static final int ADD_SUPPLY_SUCCESS = 1;
    public static final int ADD_CUSTOMER_SUCCESS = 1;
    public static final int ADD_GOODS_SUCCESS = 1;

    //Intent code
    public static final int ADD_SUPPLY_CODE = 10;
    public static final int ADD_CUSTOMER_CODE = 11;
    public static final int ADD_GOODS_CODE = 12;
}
