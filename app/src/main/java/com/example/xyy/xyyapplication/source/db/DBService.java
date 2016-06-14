package com.example.xyy.xyyapplication.source.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;
import com.example.xyy.xyyapplication.source.zxing.decoding.Intents;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
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
     * 提交事物
     */
    public void commitTransaction() {
        sqlitedb.setTransactionSuccessful();
    }

    /**
     * 结束事物
     */
    public void endTransaction() {
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
        String where = "_id = " + id;
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
        List<User> userList = new ArrayList<User>();
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
     * 删除登录记录
     *
     * @return
     */
    public int clearUserLoginLog() {
        Log.i(TAG, "清空登录记录:");
        this.open();
        int i = 0;
        try {
            i = sqlitedb.delete(DBConstant.TABLE_USER_LOGIN_LOG, null, null);
        } catch (Exception e) {
            Log.e(TAG, "清空登录记录失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取最后一个登录用户
     *
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
     *
     * @return
     */
    public User getAdminUser() {
        Log.i(TAG, "获取管理员账号");
        this.open();
        User user = null;
        String sql = " select account, password from user where is_admin = '1' order by gmt_create desc limit 1;";
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
     * 插入供应商信息
     *
     * @param supply 供应商
     * @return
     */
    public Long insertSupply(Supply supply) {
        Log.i(TAG, "添加供应商:" + supply.toString());
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", supply.getId());
        values.put("is_deleted", supply.getIsDeleted());
        values.put("gmt_create", supply.getGmtCreate());
        values.put("gmt_modified", supply.getGmtModified());
        values.put("supply_name", supply.getSupplyName());
        values.put("supply_mobile", supply.getSupplyMobile());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_SUPPLY, null, values);
        } catch (Exception e) {
            Log.e(TAG, "添加供应商失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 删除供应商
     *
     * @param id 供应商id
     * @return
     */
    public int delSupplyById(int id) {
        Log.i(TAG, "删除供应商:" + id);
        this.open();
        String where = "_id = " + id;
        int i = 0;
        try {
            i = sqlitedb.delete(DBConstant.TABLE_SUPPLY, where, null);
        } catch (Exception e) {
            Log.e(TAG, "删除供应商失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取所有供应商列表
     *
     * @return
     */
    public List<Supply> getSupplyList(String supplyType) {
        Log.i(TAG, "获取用户列表");
        this.open();
        List<Supply> supplyList = new ArrayList<Supply>();
        try {
            String sql = "select * from " + DBConstant.TABLE_SUPPLY + " where is_deleted ='N' and supply_type = '" + supplyType + "'";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String supplyMobile = cursor.getString(cursor.getColumnIndexOrThrow("supply_mobile"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("supply_type"));

                Supply supply = new Supply();
                supply.setId(id);
                supply.setIsDeleted(isDeleted);
                supply.setGmtCreate(gmtCreate);
                supply.setGmtModified(gmtModified);
                supply.setSupplyName(supplyName);
                supply.setSupplyMobile(supplyMobile);
                supplyList.add(supply);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取用户列表失败:" + e.toString());
        } finally {
            this.close();
        }
        return supplyList;
    }

    /**
     * 根据ID获取供应商信息
     *
     * @param supplyId
     * @return
     */
    public Supply getSupplyById(int supplyId) {
        Log.i(TAG, "获取供应商信息 supplyId:" + supplyId);
        this.open();
        Supply supply = new Supply();
        try {
            String sql = "select * from " + DBConstant.TABLE_SUPPLY + " where is_deleted = 'N' and _id = '" + supplyId + "' limit 1;";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String supplyMobile = cursor.getString(cursor.getColumnIndexOrThrow("supply_mobile"));
                String supplyType = cursor.getString(cursor.getColumnIndexOrThrow("supply_type"));

                supply.setId(id);
                supply.setIsDeleted(isDeleted);
                supply.setGmtCreate(gmtCreate);
                supply.setGmtModified(gmtModified);
                supply.setSupplyName(supplyName);
                supply.setSupplyMobile(supplyMobile);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取供应商信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return supply;
    }
    /**
     * 更新供应商信息
     *
     * @param supplyId
     * @param supplyName
     * @param supplyMobile
     * @return
     */
    public int updateSupply(int supplyId, String supplyName, String supplyMobile) {
        Log.i(TAG, "更新客户信息: supplyId:" + supplyId + " supplyName:" + supplyName + " supplyMobile:" + supplyMobile);
        if (supplyId < 1) {
            return 0;
        }
        ContentValues values = new ContentValues();
        values.put("gmt_modified", new Date().getTime());
        if(!StringUtils.isBlank(supplyName)){
            values.put("supply_name", supplyName);
        }
        if(!StringUtils.isBlank(supplyMobile)){
            values.put("supply_mobile", supplyMobile);
        }
        String whereClause = "_id=?";
        String[] whereArgs = {"" + supplyId};

        this.open();
        int i = 0;
        try {
            i = sqlitedb.update(DBConstant.TABLE_SUPPLY, values, whereClause, whereArgs);
        } catch (Exception e) {
            DebugLog.e(TAG, "更新供应商信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.close();
        }
        return i;
    }


    /**
     * 根据供应商姓名/手机号 搜索供应商信息
     *
     * @param key
     * @return
     */
    public List<Supply> searchSupply(String key) {
        Log.i(TAG, "搜索客户列表");
        this.open();
        List<Supply> supplyList = new ArrayList<>();
        try {
            String sql = "select * from " + DBConstant.TABLE_SUPPLY +
                    " where is_deleted = 'N' and (supply_name like '%" + key + "%' or supply_mobile like '%" + key + "%');";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Supply supply = new Supply();
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String supplyMobile = cursor.getString(cursor.getColumnIndexOrThrow("supply_mobile"));
                String supplyType = cursor.getString(cursor.getColumnIndexOrThrow("supply_type"));

                supply.setId(id);
                supply.setIsDeleted(isDeleted);
                supply.setGmtCreate(gmtCreate);
                supply.setGmtModified(gmtModified);
                supply.setSupplyName(supplyName);
                supply.setSupplyMobile(supplyMobile);
                supplyList.add(supply);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "搜索客户信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return supplyList;
    }

    /**
     * 插入客户信息
     *
     * @param customer 客户
     * @return
     */
    public Long insertCustomer(Customer customer) {
        Log.i(TAG, "添加客户:" + customer.toString());
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", customer.getId());
        values.put("is_deleted", customer.getIsDeleted());
        values.put("gmt_create", customer.getGmtCreate());
        values.put("gmt_modified", customer.getGmtModified());
        values.put("customer_name", customer.getCustomerName());
        values.put("customer_mobile", customer.getCustomerMobile());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_CUSTOMER, null, values);
        } catch (Exception e) {
            Log.e(TAG, "添加客户失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 删除客户
     *
     * @param id 客户id
     * @return
     */
    public int delCustomerById(int id) {
        Log.i(TAG, "删除客户:" + id);
        this.open();
        String where = "_id = " + id;
        int i = 0;
        try {
            i = sqlitedb.delete(DBConstant.TABLE_CUSTOMER, where, null);
        } catch (Exception e) {
            Log.e(TAG, "删除客户失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取所有客户列表
     *
     * @return
     */
    public List<Customer> getCustomerList(String customerType) {
        Log.i(TAG, "获取用户列表");
        this.open();
        List<Customer> customerList = new ArrayList<Customer>();
        try {
            String sql = "select * from " + DBConstant.TABLE_CUSTOMER + " where is_deleted ='N' and customer_type = '" + customerType + "'";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                String customerMobile = cursor.getString(cursor.getColumnIndexOrThrow("customer_mobile"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("customer_type"));

                Customer customer = new Customer();
                customer.setId(id);
                customer.setIsDeleted(isDeleted);
                customer.setGmtCreate(gmtCreate);
                customer.setGmtModified(gmtModified);
                customer.setCustomerName(customerName);
                customer.setCustomerMobile(customerMobile);
                customerList.add(customer);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取用户列表失败:" + e.toString());
        } finally {
            this.close();
        }
        return customerList;
    }

    /**
     * 根据ID获取客户信息
     *
     * @param customerId
     * @return
     */
    public Customer getCustomerById(int customerId) {
        Log.i(TAG, "获取客户信息 customerId:" + customerId);
        this.open();
        Customer customer = new Customer();
        try {
            String sql = "select * from " + DBConstant.TABLE_CUSTOMER + " where is_deleted = 'N' and _id = '" + customerId + "' limit 1;";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                String customerMobile = cursor.getString(cursor.getColumnIndexOrThrow("customer_mobile"));
                String customerType = cursor.getString(cursor.getColumnIndexOrThrow("customer_type"));

                customer.setId(id);
                customer.setIsDeleted(isDeleted);
                customer.setGmtCreate(gmtCreate);
                customer.setGmtModified(gmtModified);
                customer.setCustomerName(customerName);
                customer.setCustomerMobile(customerMobile);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取客户信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return customer;
    }

    /**
     * 更新客户信息
     *
     * @param customerId
     * @param customerName
     * @param customerMobile
     * @return
     */
    public int updateCustomer(int customerId, String customerName, String customerMobile) {
        Log.i(TAG, "更新客户信息: customerId:" + customerId + " customerName:" + customerName + " customerMobile:" + customerMobile);
        if (customerId < 1) {
            return 0;
        }
        ContentValues values = new ContentValues();
        values.put("gmt_modified", new Date().getTime());
        if(!StringUtils.isBlank(customerName)){
            values.put("customer_name", customerName);
        }
        if(!StringUtils.isBlank(customerMobile)){
            values.put("customer_mobile", customerMobile);
        }
        String whereClause = "_id=?";
        String[] whereArgs = {"" + customerId};

        this.open();
        int i = 0;
        try {
            i = sqlitedb.update(DBConstant.TABLE_CUSTOMER, values, whereClause, whereArgs);
        } catch (Exception e) {
            DebugLog.e(TAG, "更新客户信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 根据客户姓名/手机号 搜索客户信息
     *
     * @param key
     * @return
     */
    public List<Customer> searchCustomer(String key) {
        Log.i(TAG, "搜索客户列表");
        this.open();
        List<Customer> customerLists = new ArrayList<>();
        try {
            String sql = "select * from " + DBConstant.TABLE_CUSTOMER +
                    " where is_deleted = 'N' and (customer_name like '%" + key + "%' or customer_mobile like '%" + key + "%');";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Customer customer = new Customer();
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                String customerMobile = cursor.getString(cursor.getColumnIndexOrThrow("customer_mobile"));
                String customerType = cursor.getString(cursor.getColumnIndexOrThrow("customer_type"));

                customer.setId(id);
                customer.setIsDeleted(isDeleted);
                customer.setGmtCreate(gmtCreate);
                customer.setGmtModified(gmtModified);
                customer.setCustomerName(customerName);
                customer.setCustomerMobile(customerMobile);
                customerLists.add(customer);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "搜索客户信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return customerLists;
    }

    /**
     * 插入商品信息
     *
     * @param goods 商品信息
     * @return
     */
    public Long insertGoods(Goods goods) {
        Log.i(TAG, "添加商品:" + goods.toString());
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", goods.getId());
        values.put("is_deleted", goods.getIsDeleted());
        values.put("gmt_create", goods.getGmtCreate());
        values.put("gmt_modified", goods.getGmtModified());
        values.put("goods_name", goods.getGoodsName());
        values.put("goods_num", goods.getGoodsNum());
        values.put("goods_code", goods.getGoodsCode());
        values.put("goods_type", goods.getGoodsType());

        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_GOODS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "添加商品失败:" + e.toString());
            return i;
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 删除商品
     *
     * @param id 客户id
     * @return
     */
    public int delGoodsById(int id) {
        Log.i(TAG, "删除商品:" + id);
        this.open();
        String where = "_id = " + id;
        int i = 0;
        try {
            i = sqlitedb.delete(DBConstant.TABLE_GOODS, where, null);
        } catch (Exception e) {
            Log.e(TAG, "删除商品失败:" + e.toString());
        } finally {
            this.close();
        }
        return i;
    }

    /**
     * 获取所有商品列表
     *
     * @return
     */
    public List<Goods> getGoodsList(String goodsType) {
        Log.i(TAG, "获取商品列表");
        this.open();
        List<Goods> goodsList = new ArrayList<Goods>();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS + " where is_deleted ='N' and goods_type = '" + goodsType + "'";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                int goodsNum = cursor.getInt(cursor.getColumnIndexOrThrow("goods_num"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("goods_type"));
                String goodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));

                Goods goods = new Goods();
                goods.setId(id);
                goods.setIsDeleted(isDeleted);
                goods.setGmtCreate(gmtCreate);
                goods.setGmtModified(gmtModified);
                goods.setGoodsName(goodsName);
                goods.setGoodsNum(goodsNum);
                goods.setGoodsType(type);
                goods.setGoodsCode(goodsCode);
                goodsList.add(goods);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取商品列表失败:" + e.toString());
        } finally {
            this.close();
        }
        Log.i(TAG, "获取商品列表 result:" + goodsList);
        return goodsList;
    }

    /**
     * 根据商品编号获取商品信息
     *
     * @param goodsCode
     * @return
     */
    public Goods getGoodsByCode(String goodsCode) {
        Log.i(TAG, "获取商品列表");
        this.open();
        Goods goods = new Goods();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS + " where is_deleted = 'N' and goods_code = '" + goodsCode + "' limit 1;";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                String mGoodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));
                Integer goodsNum = cursor.getInt(cursor.getColumnIndexOrThrow("goods_num"));
                String goodsType = cursor.getString(cursor.getColumnIndexOrThrow("goods_type"));

                goods.setId(id);
                goods.setIsDeleted(isDeleted);
                goods.setGmtCreate(gmtCreate);
                goods.setGmtModified(gmtModified);
                goods.setGoodsCode(mGoodsCode);
                goods.setGoodsName(goodsName);
                goods.setGoodsNum(goodsNum);
                goods.setGoodsType(goodsType);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取商品信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return goods;
    }

    /**
     * 根据商品编号/名称 搜索商品信息
     *
     * @param key
     * @return
     */
    public List<Goods> searchGoods(String key) {
        Log.i(TAG, "搜索商品列表");
        this.open();
        List<Goods> goodsList = new ArrayList<>();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS +
                    " where is_deleted = 'N' and (goods_code like '%" + key + "%' or goods_name like '%" + key + "%');";
            Log.i("QXD", sql);
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Goods goods = new Goods();
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                String mGoodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));
                Integer goodsNum = cursor.getInt(cursor.getColumnIndexOrThrow("goods_num"));
                String goodsType = cursor.getString(cursor.getColumnIndexOrThrow("goods_type"));

                goods.setId(id);
                goods.setIsDeleted(isDeleted);
                goods.setGmtCreate(gmtCreate);
                goods.setGmtModified(gmtModified);
                goods.setGoodsCode(mGoodsCode);
                goods.setGoodsName(goodsName);
                goods.setGoodsNum(goodsNum);
                goods.setGoodsType(goodsType);
                goodsList.add(goods);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "搜索商品信息失败:" + e.toString());
            return null;
        } finally {
            this.close();
        }
        return goodsList;
    }

    /**
     * 更新商品信息
     *
     * @param goods
     * @return
     */
    public Long upDateGoods(Goods goods) {
        Log.i(TAG, "更新商品:" + goods.toString());
        if (goods.getId() == null || goods.getId() < 1) {
            return 0l;
        }
        this.open();
        ContentValues values = new ContentValues();
        values.put("_id", goods.getId());
        values.put("is_deleted", goods.getIsDeleted());
        values.put("gmt_create", goods.getGmtCreate());
        values.put("gmt_modified", goods.getGmtModified());
        values.put("goods_name", goods.getGoodsName());
        values.put("goods_code", goods.getGoodsCode());
        values.put("goods_num", goods.getGoodsNum());
        values.put("goods_type", goods.getGoodsType());
        Long i = 0l;
        try {
            i = sqlitedb.replaceOrThrow(DBConstant.TABLE_GOODS, null, values);
        } catch (Exception e) {
            DebugLog.e(TAG, "更新商品信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.close();
        }
        return i;
    }


    /**
     * 商品入库
     *
     * @param goodsCode
     * @param num
     * @param supply
     * @return
     */
    public int inGoods(String goodsCode, Integer num, Supply supply) {
        Log.i(TAG, "商品入库: goodsCode:" + goodsCode + " num:" + num + " supply:" + supply);
        if (StringUtils.isBlank(goodsCode)) {
            return 0;
        }
        //获取商品当前数量
        Goods goods = getGoodsByCode(goodsCode);
        if (goods.getId() == null) {
            return 0;
        }
        //更新商品表
        Integer currentNum = goods.getGoodsNum();
        Integer totalNum = currentNum + num;
        ContentValues values = new ContentValues();
        values.put("gmt_modified", new Date().getTime());
        values.put("goods_num", totalNum);
        String whereClause = "_id=?";
        String[] whereArgs = {"" + goods.getId()};
        //插入流水表
        ContentValues valuesSupply = new ContentValues();
        valuesSupply.put("is_deleted", "N");
        valuesSupply.put("gmt_create", new Date().getTime());
        valuesSupply.put("gmt_modified", new Date().getTime());
        valuesSupply.put("create_id", MApplication.currentLoginUser.getId());
        valuesSupply.put("goods_name", goods.getGoodsName());
        valuesSupply.put("goods_code", goods.getGoodsCode());
        valuesSupply.put("num", num);
        valuesSupply.put("supply_id", supply.getId());
        valuesSupply.put("supply_name", supply.getSupplyName());
        valuesSupply.put("action_type", "0");
        this.open();
        int i = 0;
        long j = 0;
        try {
            this.beginTransaction();
            i = sqlitedb.update(DBConstant.TABLE_GOODS, values, whereClause, whereArgs);
            j = sqlitedb.replaceOrThrow(DBConstant.TABLE_GOODS_LOG, null, valuesSupply);
            this.commitTransaction();
        } catch (Exception e) {
            DebugLog.e(TAG, "更新商品信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.endTransaction();
            this.close();
        }
        return i > 0 && j > 0 ? 1 : 0;
    }

    /**
     * 商品出库
     *
     * @param goodsCode
     * @param num
     * @param customer
     * @return
     */
    public int outGoods(String goodsCode, Integer num, Customer customer) {
        Log.i(TAG, "商品入库: goodsCode:" + goodsCode + " num:" + num + " customer:" + customer);
        if (StringUtils.isBlank(goodsCode)) {
            return 0;
        }

        //获取商品当前数量
        Goods goods = getGoodsByCode(goodsCode);
        if (goods.getId() == null) {
            return 0;
        }
        //更新商品表
        Integer currentNum = goods.getGoodsNum();
        if (currentNum < num) {
            return 0;
        }
        Integer totalNum = currentNum - num;
        ContentValues values = new ContentValues();
        values.put("gmt_modified", new Date().getTime());
        values.put("goods_num", totalNum);
        String whereClause = "_id=?";
        String[] whereArgs = {"" + goods.getId()};
        //插入流水表
        ContentValues valuesSupply = new ContentValues();
        valuesSupply.put("is_deleted", "N");
        valuesSupply.put("gmt_create", new Date().getTime());
        valuesSupply.put("gmt_modified", new Date().getTime());
        valuesSupply.put("create_id", MApplication.currentLoginUser.getId());
        valuesSupply.put("goods_name", goods.getGoodsName());
        valuesSupply.put("goods_code", goods.getGoodsCode());
        valuesSupply.put("num", num);
        valuesSupply.put("customer_id", customer.getId());
        valuesSupply.put("customer_name", customer.getCustomerName());
        valuesSupply.put("action_type", "1");
        this.open();
        int i = 0;
        long j = 0;
        try {
            this.beginTransaction();
            i = sqlitedb.update(DBConstant.TABLE_GOODS, values, whereClause, whereArgs);
            j = sqlitedb.replaceOrThrow(DBConstant.TABLE_GOODS_LOG, null, valuesSupply);
            this.commitTransaction();
        } catch (Exception e) {
            DebugLog.e(TAG, "更新商品信息失败. e:" + e.toString() + " values:" + values.toString());
        } finally {
            this.endTransaction();
            this.close();
        }
        return i > 0 && j > 0 ? 1 : 0;
    }

    /**
     * 获取商品出入库明细列表
     *
     * @return
     */
    public List<GoodsLog> getGoodsLogList(String mGoodsCode) {
        Log.i(TAG, "获取商品出入库明细列表");
        this.open();
        List<GoodsLog> goodsLogList = new ArrayList<GoodsLog>();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS_LOG + " where is_deleted ='N' and goods_code = '" + mGoodsCode + "' order by _id desc";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                Integer createId = cursor.getInt(cursor.getColumnIndexOrThrow("create_id"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                int num = cursor.getInt(cursor.getColumnIndexOrThrow("num"));
                String goodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customer_id"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                int supplyId = cursor.getInt(cursor.getColumnIndexOrThrow("supply_id"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String actionType = cursor.getString(cursor.getColumnIndexOrThrow("action_type"));

                GoodsLog goodsLog = new GoodsLog();
                goodsLog.setId(id);
                goodsLog.setIsDeleted(isDeleted);
                goodsLog.setGmtCreate(gmtCreate);
                goodsLog.setGmtModified(gmtModified);
                goodsLog.setCreateId(createId);
                goodsLog.setNum(num);
                goodsLog.setCustomerId(customerId);
                goodsLog.setCustomerName(customerName);
                goodsLog.setSupplyId(supplyId);
                goodsLog.setSupplyName(supplyName);
                goodsLog.setActionType(actionType);
                goodsLog.setGoodsName(goodsName);
                goodsLog.setGoodsCode(goodsCode);
                goodsLogList.add(goodsLog);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取商品出入库列表失败:" + e.toString());
        } finally {
            this.close();
        }
        Log.i(TAG, "获取商品出入库列表 result:" + goodsLogList);
        return goodsLogList;
    }

    /**
     * 根据客户id获取商品出入库明细列表
     *
     * @return
     */
    public List<GoodsLog> getGoodsLogListByCustomerId(int mCustomerId) {
        Log.i(TAG, "获取商品出入库明细列表");
        this.open();
        List<GoodsLog> goodsLogList = new ArrayList<GoodsLog>();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS_LOG + " where is_deleted ='N' and customer_id = '" + mCustomerId + "' order by _id desc";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                Integer createId = cursor.getInt(cursor.getColumnIndexOrThrow("create_id"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                int num = cursor.getInt(cursor.getColumnIndexOrThrow("num"));
                String goodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customer_id"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                int supplyId = cursor.getInt(cursor.getColumnIndexOrThrow("supply_id"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String actionType = cursor.getString(cursor.getColumnIndexOrThrow("action_type"));

                GoodsLog goodsLog = new GoodsLog();
                goodsLog.setId(id);
                goodsLog.setIsDeleted(isDeleted);
                goodsLog.setGmtCreate(gmtCreate);
                goodsLog.setGmtModified(gmtModified);
                goodsLog.setCreateId(createId);
                goodsLog.setNum(num);
                goodsLog.setCustomerId(customerId);
                goodsLog.setCustomerName(customerName);
                goodsLog.setSupplyId(supplyId);
                goodsLog.setSupplyName(supplyName);
                goodsLog.setActionType(actionType);
                goodsLog.setGoodsName(goodsName);
                goodsLog.setGoodsCode(goodsCode);
                goodsLogList.add(goodsLog);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取商品出入库列表失败:" + e.toString());
        } finally {
            this.close();
        }
        Log.i(TAG, "获取商品出入库列表 result:" + goodsLogList);
        return goodsLogList;
    }

    /**
     * 根据供应商id获取商品出入库明细列表
     *
     * @return
     */
    public List<GoodsLog> getGoodsLogListBySupplyId(int mSupplyId) {
        Log.i(TAG, "获取商品出入库明细列表");
        this.open();
        List<GoodsLog> goodsLogList = new ArrayList<GoodsLog>();
        try {
            String sql = "select * from " + DBConstant.TABLE_GOODS_LOG + " where is_deleted ='N' and supply_id = '" + mSupplyId + "' order by _id desc";
            Cursor cursor = sqlitedb.rawQuery(sql, null);
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String isDeleted = cursor.getString(cursor.getColumnIndexOrThrow("is_deleted"));
                Long gmtCreate = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_create"));
                Long gmtModified = cursor.getLong(cursor.getColumnIndexOrThrow("gmt_modified"));
                Integer createId = cursor.getInt(cursor.getColumnIndexOrThrow("create_id"));
                String goodsName = cursor.getString(cursor.getColumnIndexOrThrow("goods_name"));
                int num = cursor.getInt(cursor.getColumnIndexOrThrow("num"));
                String goodsCode = cursor.getString(cursor.getColumnIndexOrThrow("goods_code"));
                int customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customer_id"));
                String customerName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
                int supplyId = cursor.getInt(cursor.getColumnIndexOrThrow("supply_id"));
                String supplyName = cursor.getString(cursor.getColumnIndexOrThrow("supply_name"));
                String actionType = cursor.getString(cursor.getColumnIndexOrThrow("action_type"));

                GoodsLog goodsLog = new GoodsLog();
                goodsLog.setId(id);
                goodsLog.setIsDeleted(isDeleted);
                goodsLog.setGmtCreate(gmtCreate);
                goodsLog.setGmtModified(gmtModified);
                goodsLog.setCreateId(createId);
                goodsLog.setNum(num);
                goodsLog.setCustomerId(customerId);
                goodsLog.setCustomerName(customerName);
                goodsLog.setSupplyId(supplyId);
                goodsLog.setSupplyName(supplyName);
                goodsLog.setActionType(actionType);
                goodsLog.setGoodsName(goodsName);
                goodsLog.setGoodsCode(goodsCode);
                goodsLogList.add(goodsLog);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "获取商品出入库列表失败:" + e.toString());
        } finally {
            this.close();
        }
        Log.i(TAG, "获取商品出入库列表 result:" + goodsLogList);
        return goodsLogList;
    }
}
