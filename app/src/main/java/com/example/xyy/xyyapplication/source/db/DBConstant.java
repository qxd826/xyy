package com.example.xyy.xyyapplication.source.db;

import com.example.xyy.xyyapplication.source.pojo.user.User;

import java.util.List;

/**
 * Created by admin on 16/4/25.
 */
public class DBConstant {

    public static final String DATABASE_NAME = "xyy.db";
    public static final int DATABASE_VERSION = 1;

    //表
    public static final String TABLE_USER = "user";
    public static final String TABLE_USER_LOGIN_LOG = "user_login_log";
    public static final String TABLE_SUPPLY = "supply";
    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_GOODS = "goods";

    //创建用户表
    public static final String CREATE_USER_SQL = "CREATE TABLE IF NOT EXISTS user("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null default 'N', "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " user_name varchar(20) default '', "
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

    //创建供应商表
    public static final String CREATE_SUPPLY_SQL = "CREATE TABLE IF NOT EXISTS supply("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null default 'N', "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " supply_name varchar(20) not null, "
            + " supply_mobile varchar(20) not null, "
            + " supply_type char(1) not null default '1'); ";

    //创建客户表
    public static final String CREATE_CUSTOMER_SQL = "CREATE TABLE IF NOT EXISTS customer("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null default 'N', "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " customer_name varchar(20) not null, "
            + " customer_mobile varchar(20) not null, "
            + " customer_type char(1) not null default '1'); ";

    //创建商品表
    public static final String CREATE_GOODS_SQL = "CREATE TABLE IF NOT EXISTS goods("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null default 'N', "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " goods_name varchar(20) not null, "
            + " goods_code varchar(30) not null, "
            + " goods_num integer not null default 0, "
            + " goods_type char(1) not null default '1'); ";


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
