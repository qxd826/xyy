package com.example.xyy.xyyapplication.source.db;

import com.example.xyy.xyyapplication.source.pojo.user.User;

import java.util.List;

/**
 * Created by admin on 16/4/25.
 */
public class DBConstant {

    public static final String DATABASE_NAME = "xyy.db";
    public static final int DATABASE_VERSION = 1;

    //用户表
    public static final String TABLE_USER = "user";
    public static final String TABLE_USER_LOGIN_LOG = "user_login_log";

    //创建用户表
    public static final String CREATE_USER_SQL = "CREATE TABLE IF NOT EXISTS user("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null, "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " user_name varchar(20) not null, "
            + " account varchar(20) not null, "
            + " password varchar(20) not null, "
            + " is_admin char(1) not null default '0', "  //1是管理员,0是非管理员
            + " mobile varchar(20) default '');";

    //创建登录记录表
    public static final String CREATE_USER_LOGIN_LOG_SQL = "CREATE TABLE IF NOT EXISTS user_login_log("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null, "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " password varchar(20) not null, "
            + " account varchar(20) not null);";


/*    *//* 离线消息 *//*
    private static final String OFFLINE_MESSAGE_TABLE_CREATE_SQL = " CREATE TABLE IF NOT EXISTS offline_message ("
            + " _id integer primary key autoincrement, "
            + " touid integer not null, "
            + " fromuid integer not null, "
            + " fromname text not null default '', "
            + " fromsex integer not null default 0, "
            + " roomid integer not null, "
            + " type integer default 0, " ////2chat 3.gift 4.big daoju 5.yanhua caisheng // 10 推广奖励， 11 系统通知   12 获奖通知
            + " has_read integer default 0, "//0 未读 1 已读
            + " content text not null default '', "
            + " created_at integer not null default 0); " +
            "create index index_idx on offline_message(touid,fromuid);";		//索引*/
}
