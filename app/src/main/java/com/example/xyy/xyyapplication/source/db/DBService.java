package com.example.xyy.xyyapplication.source.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16/4/26.
 */
public class DBService {

    private final String TAG = "DB";
    /**
     * 单例
     */
    private static DBService dbInstance = null;

    /**
     * 保存数据库实例变量
     */
    private SQLiteDatabase sqlitedb;

    /**
     * 数据库打开/更新helper
     */
    private DBHelper dbHelper;

    private DBService(Context context) {
        dbHelper = new DBHelper(context, DBConstant.DATABASE_NAME, null,
                DBConstant.DATABASE_VERSION
        );
    }

    public synchronized static DBService getInstance(Context context) {
        if (null == dbInstance) {
            dbInstance = new DBService(context);
        }
        return dbInstance;
    }

    public DBService open() {
        try {
            sqlitedb = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "获取数据库连接对象失败" + e.toString());
        }
        return this;
    }

    public void close() {
        if (sqlitedb != null && sqlitedb.isOpen())
            sqlitedb.close();
    }

    /**
     * 开启事物
     */
    public void beginTransaction() {
        sqlitedb.beginTransaction();
    }

    /**
     * 结束提交事物
     */
    public void endTransaction() {
        sqlitedb.setTransactionSuccessful();
        sqlitedb.endTransaction();
    }


    /**
     * @param user 用户
     * @return
     */
    public Long insert(User user) {
        Log.i(TAG, "添加用户:" + user.toString());
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", user.getId());
        values.put("is_deleted", user.getIsDeleted());
        values.put("gmt_create", user.getGmtCreate());
        values.put("gmt_modified", user.getGmtModified());
        values.put("user_name", user.getUserName());
        values.put("account", StringUtils.upperCase(user.getAccount()));
        values.put("password", user.getPassword());
        values.put("mobile", user.getMobile());
        values.put("is_admin", user.getIsAdmin());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_USER, null, values);
        } catch (Exception e) {
            Log.e(TAG, "添加用户失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * @param id 用户
     * @return
     */
    public int delUserById(int id) {
        Log.i(TAG, "删除用户:" + id);
        this.open();
        String where = "_id = "+ id;
        int i = 0;
        try {
            i = sqlitedb.delete(DBConstant.TABLE_USER, where, null);
        } catch (Exception e) {
            Log.e(TAG, "删除用户失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    public List<User> getUserList() {
        Log.i(TAG, "获取用户列表");
        this.open();
        List<User> userList = new ArrayList<>();
        try {
            String sql = "select * from " + DBConstant.TABLE_USER + " where is_deleted ='N' and is_admin = '0'";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String dbAccount = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
                String isAdmin = cursor.getString(cursor.getColumnIndexOrThrow("is_admin"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                User user = new User();
                user.setId(id);
                user.setIsDeleted(isDeleted);
                user.setGmtCreate(gmtCreate);
                user.setGmtModified(gmtModified);
                user.setUserName(userName);
                user.setAccount(dbAccount);
                user.setPassword(password);
                user.setMobile(mobile);
                user.setIsAdmin(isAdmin);
                userList.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取用户列表失败:" + e.toString());
        } finally {
            this.close();
        }
        return userList;
    }

    /**
     * 根据账号获取用户信息
     *
     * @param account
     * @return
     */
    public User getUserByAccount(String account) {
        Log.i(TAG, "获取用户列表");
        this.open();
        User user = new User();
        try {
            String sql = "select * from " + DBConstant.TABLE_USER + " where is_deleted = 'N' and account = '" + StringUtils.upperCase(account) + "' limit 1;";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String dbAccount = cursor.getString(cursor.getColumnIndexOrThrow("account"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
                String isAdmin = cursor.getString(cursor.getColumnIndexOrThrow("is_admin"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                user.setId(id);
                user.setIsDeleted(isDeleted);
                user.setGmtCreate(gmtCreate);
                user.setGmtModified(gmtModified);
                user.setUserName(userName);
                user.setAccount(dbAccount);
                user.setMobile(mobile);
                user.setIsAdmin(isAdmin);
                user.setPassword(password);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取用户信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public Long upDateUser(User user) {
        Log.i(TAG, "更新用户:" + user.toString());
        if (user.getId() == null || user.getId() < 1) {
            return 0l;
        }
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", user.getId());
        values.put("is_deleted", user.getIsDeleted());
        values.put("gmt_create", user.getGmtCreate());
        values.put("gmt_modified", user.getGmtModified());
        values.put("user_name", user.getUserName());
        values.put("account", StringUtils.upperCase(user.getAccount()));
        values.put("password", user.getPassword());
        values.put("mobile", user.getMobile());
        values.put("is_admin", user.getIsAdmin());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_USER, null, values);
        } catch (Exception e) {
            DebugLog.e("更新用户信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 添加登录记录
     *
     * @param userLoginLog
     * @return
     */
    public Long insertUserLoginLog(UserLoginLog userLoginLog) {
        Log.i(TAG, "添加用户登录记录:" + userLoginLog.toString());
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", userLoginLog.getId());
        values.put("is_deleted", userLoginLog.getIsDeleted());
        values.put("gmt_create", userLoginLog.getGmtCreate());
        values.put("gmt_modified", userLoginLog.getGmtModified());
        values.put("account", StringUtils.upperCase(userLoginLog.getAccount()));
        values.put("password", userLoginLog.getPassword());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_USER_LOGIN_LOG, null, values);
        } catch (Exception e) {
            Log.e(TAG, "添加用户登录记录失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取最后一个登录用户
     * @return
     */
    public User getLastLoginUser() {
        Log.i(TAG, "获取最后的登录用户");
        this.open();
        User user = null;
        String sql = " select account, password from user_login_log order by gmt_create desc limit 1;";
        try {
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String account = cursor.getString(cursor.getColumnIndexOrThrow("account"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                user = new User();
                user.setAccount(account);
                user.setPassword(password);
                break;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取最后的登录用户失败:" + e.toString());
        } finally {
            this.close();
        }
        return user;
    }

    /**
     * 获取管理员账号
     * @return
     */
    public User getAdminUser() {
        Log.i(TAG, "获取管理员账号");
        this.open();
        User user = null;
        String sql = " select account, password from user_login_log where is_admin = '1' order by gmt_create desc limit 1;";
        try {
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String account = cursor.getString(cursor.getColumnIndexOrThrow("account"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                user = new User();
                user.setAccount(account);
                user.setPassword(password);
                break;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取最后的登录用户失败:" + e.toString());
        } finally {
            this.close();
        }
        return user;
    }
}
