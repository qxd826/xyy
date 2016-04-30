package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;


import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by QXD on 2016/4/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Button loginBt;
    private EditText accountEt;
    private EditText pwdEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("登录");
        if (hasLoginUser()) {
            startMainActivity();
        }
        initView();

        User user = new User();
        user.setAccount("qxd");
        user.setPassword("123");
        user.setGmtCreate(new Date().getTime());
        user.setGmtModified(new Date().getTime());
        user.setIsDeleted("N");
        user.setMobile("15158116453");
        user.setUserName("渠项栋");
        user.setIsAdmin("0");
        DBService dbService = DBService.getInstance(this);
        dbService.insert(user);

        dbService = DBService.getInstance(this);
        List<User> userList = dbService.getUserList();
        Log.i("[用户列表]", userList.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String account = accountEt.getText().toString();
                String password = pwdEt.getText().toString();
                if (!checkUser(account, password)) {
                    break;
                }
                startMainActivity();
                break;
        }
    }

    //初始化视图组件
    private void initView() {
        loginBt = (Button) findViewById(R.id.login_button);
        accountEt = (EditText) findViewById(R.id.account);
        pwdEt = (EditText) findViewById(R.id.password);

        loginBt.setOnClickListener(this);
        accountEt.setOnClickListener(this);
        pwdEt.setOnClickListener(this);
    }

    //校验用户名密码
    private Boolean checkUser(String account, String password) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_LONG).show();
            return false;
        }
        DBService dbService = DBService.getInstance(this);
        User user = dbService.getUserByAccount(account);
        Log.i("QXD", user.toString());
        if (user == null) {
            Toast.makeText(this, "当前用户不存在", Toast.LENGTH_LONG).show();
            return false;
        }
        String mPassword = user.getPassword();
        if (!StringUtils.equalsIgnoreCase(password, mPassword)) {
            Log.e("QXD密码错误", password);
            Toast.makeText(this, "用户密码错误", Toast.LENGTH_LONG).show();
            return false;
        }
        MApplication.currentLoginUser = user;
        MApplication.isLogin = true;
        return true;
    }

    //跳转主页面
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //当前是否有登录用户
    private Boolean hasLoginUser() {
        return MApplication.isLogin;
    }
}
