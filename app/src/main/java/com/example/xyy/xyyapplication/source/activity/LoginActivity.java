package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.userList.AddUserActivity;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/4/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener {


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
        if (hasLastLoginUser()) {
            startMainActivity();
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String account = loginAccount.getText().toString();
                String password = loginPassword.getText().toString();
                if (!checkUser(account, password)) {
                    break;
                }
                startMainActivity();
                break;
            case R.id.add_admin_user:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra(Constant.ADD_USER_TYPE, Constant.ADD_ADMIN_USER);
                startActivityForResult(intent, R.string.add_user);
                break;
        }
    }

    //初始化视图组件
    private void initView() {
        loginButton.setOnClickListener(this);
        loginAccount.setOnClickListener(this);
        loginPassword.setOnClickListener(this);
        addAdminUser.setOnClickListener(this);
        if (hasAdminUser()) {
            addAdminUser.setVisibility(View.GONE);
        } else {
            addAdminUser.setVisibility(View.VISIBLE);
        }
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
    private Boolean checkUser(String account, String password) {
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_LONG).show();
            return false;
        }
        DBService dbService = DBService.getInstance(this);
        User user = dbService.getUserByAccount(account);
        if (user == null) {
            Toast.makeText(this, "当前用户不存在", Toast.LENGTH_LONG).show();
            return false;
        }
        String mPassword = user.getPassword();
        if (!StringUtils.equalsIgnoreCase(password, mPassword)) {
            Toast.makeText(this, "用户密码错误", Toast.LENGTH_LONG).show();
            return false;
        }

        //设置当前登录用户全局值
        MApplication.currentLoginUser = user;
        MApplication.isLogin = true;
        if (StringUtils.equals(Constant.IS_ADMIN, user.getIsAdmin())) {
            MApplication.isAdmin = true;
        } else {
            MApplication.isAdmin = false;
        }

        //插入登录记录
        UserLoginLog userLoginLog = new UserLoginLog();
        userLoginLog.setGmtCreate(new Date().getTime());
        userLoginLog.setGmtModified(new Date().getTime());
        userLoginLog.setIsDeleted("N");
        userLoginLog.setPassword(user.getPassword());
        userLoginLog.setAccount(user.getAccount());
        dbService.insertUserLoginLog(userLoginLog);
        return true;
    }

    //跳转主页面
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        onDestroy();
    }

    //获取最后的登录用户
    private Boolean hasLastLoginUser() {
        DBService dbService = DBService.getInstance(this);
        User user = dbService.getLastLoginUser();
        if (user == null || StringUtils.isEmpty(user.getAccount())) {
            return false;
        } else {
            User loginUser = dbService.getUserByAccount(user.getAccount());
            MApplication.currentLoginUser = loginUser;
            MApplication.isLogin = true;
            if (StringUtils.equals(Constant.IS_ADMIN, loginUser.getIsAdmin())) {
                MApplication.isAdmin = true;
            } else {
                MApplication.isAdmin = false;
            }
        }
        return true;
    }

    //是否有管理员账号
    private Boolean hasAdminUser() {
        DBService dbService = DBService.getInstance(this);
        User user = dbService.getAdminUser();
        if (null != user) {
            return true;
        }
        return false;
    }
}
