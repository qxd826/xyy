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
    public static final String TABLE_GOODS_LOG = "goods_log";

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

    //创建商品进出库流水表
    /**
     * action_type 0 :入库
     * action_type 1 :出库
     *
     * */
    public static final String CREATE_GOODS_LOG_SQL = "CREATE TABLE IF NOT EXISTS goods_log("
            + " _id integer primary key autoincrement, "
            + " is_deleted char(1) not null default 'N', "
            + " gmt_create integer not null, "
            + " gmt_modified integer not null, "
            + " create_id integer not null default 0, "
            + " goods_name varchar(20) not null, "
            + " goods_code varchar(30) not null, "
            + " num integer not null default 0, "
            + " customer_id integer not null default 0, "
            + " customer_name varchar(20), "
            + " supply_id integer not null default 0, "
            + " supply_name varchar(20), "
            + " action_type char(1) not null default '0'); ";
}
