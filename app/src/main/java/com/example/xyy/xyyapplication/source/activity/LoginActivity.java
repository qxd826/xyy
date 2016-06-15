package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.userList.AddUserActivity;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/4/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private final String TAG = "LoginActivity";

    private DBService dbService;
    private RequestQueue mQueue;
    private UserVO loginUser = null;
    private String lastLoginAccount = null;

    @Bind(R.id.personal_info)
    TextView personalInfo;
    @Bind(R.id.login_account)
    EditText loginAccount;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.add_admin_user)
    Button addAdminUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        setTitle("登录");
        mQueue = Volley.newRequestQueue(this);

        if (hasLastLoginUser()) {
            if (!StringUtils.isBlank(MApplication.IP_SERVICE)) {
                hasAdminUser();
                getLastLoginUser(lastLoginAccount);
            }
        }
        initView();
    }

    @Override
    protected void onResume() {
        if (dbService == null) {
            dbService = DBService.getInstance(this);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "LoginActivity destroy......");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        dbService = null;
        Log.i(TAG, "LoginActivity stop......");
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String account = loginAccount.getText().toString();
                String password = loginPassword.getText().toString();
                checkUser(account, password);
                break;
            case R.id.add_admin_user:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra(Constant.ADD_USER_TYPE, Constant.ADD_ADMIN_USER);
                startActivityForResult(intent, R.string.add_user);
                break;
            case R.id.personal_info:
                Intent intentIp = new Intent(this, IPSettingActivity.class);
                startActivity(intentIp);
                break;
        }
    }

    //初始化视图组件
    private void initView() {
        loginButton.setOnClickListener(this);
        loginAccount.setOnClickListener(this);
        loginPassword.setOnClickListener(this);
        addAdminUser.setOnClickListener(this);
        personalInfo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.string.add_user:
                if (resultCode == 1) {
                    addAdminUser.setVisibility(View.GONE);
                }
                break;
        }
    }

    //校验用户名密码
    private void checkUser(String account, String password) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_LONG).show();
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/login/loginIn?account=" + account + "&password=" + password
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG , response.toString());
                        Gson gson = new Gson();
                        Log.i(TAG , "获取结果");
                        Result<UserVO> result = gson.fromJson(response.toString(),Result.class);
                        Log.i(TAG , "获取结果:"+result.toString());
                        if (result.isSuccess()) {
                            loginUser = JsonUtil.fromJson(JsonUtil.toJson(result.getData()),UserVO.class);
                            MApplication.currentLoginUser = loginUser;
                            MApplication.isLogin = true;
                            if (StringUtils.equals(Constant.IS_ADMIN, loginUser.getIsAdmin())) {
                                MApplication.isAdmin = true;
                            } else {
                                MApplication.isAdmin = false;
                            }
                            //插入登录记录
                            UserLoginLog userLoginLog = new UserLoginLog();
                            userLoginLog.setGmtCreate(new Date().getTime());
                            userLoginLog.setGmtModified(new Date().getTime());
                            userLoginLog.setIsDeleted("N");
                            userLoginLog.setPassword(loginUser.getPassword());
                            userLoginLog.setAccount(loginUser.getAccount());
                            dbService.insertUserLoginLog(userLoginLog);
                            Log.i(TAG, "启动主界面");
                            startMainActivity();
                        }else{
                            Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    //跳转主页面
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        onDestroy();
    }

    //获取最后的登录用户信息
    private void getLastLoginUser(String account){
        if(null == account){
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/user/info?account=" + account
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG + "获取最后的登录用户", response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(),Result.class);
                        if (result.isSuccess()) {
                            loginUser = JsonUtil.fromJson(JsonUtil.toJson(result.getData()),UserVO.class);
                            MApplication.currentLoginUser = loginUser;
                            MApplication.isLogin = true;
                            if (StringUtils.equals(Constant.IS_ADMIN, loginUser.getIsAdmin())) {
                                MApplication.isAdmin = true;
                            } else {
                                MApplication.isAdmin = false;
                            }
                            startMainActivity();
                        }else{
                            Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    //获取最后的登录用户
    private Boolean hasLastLoginUser() {
        dbService = DBService.getInstance(this);
        User user = dbService.getLastLoginUser();
        if (user == null || StringUtils.isEmpty(user.getAccount())) {
            return false;
        }
        return true;
    }

    //是否有管理员账号
    private void hasAdminUser() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/user/isHasAdmin"
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        Gson gson = new Gson();
                        Result<Boolean> result = gson.fromJson(response.toString(), new TypeToken<Result<Boolean>>() {
                        }.getType());
                        if (result.isSuccess()) {
                            if((Boolean) result.getData()){
                                addAdminUser.setVisibility(View.GONE);
                            }else{
                                addAdminUser.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                if(null != addAdminUser){
                    addAdminUser.setVisibility(View.GONE);
                }
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
