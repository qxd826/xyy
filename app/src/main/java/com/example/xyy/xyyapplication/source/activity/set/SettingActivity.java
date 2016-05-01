package com.example.xyy.xyyapplication.source.activity.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.LoginActivity;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.db.DBService;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by admin on 16/5/1.
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.setting_back)
    ImageButton settingBack;
    @Bind(R.id.login_out_btn)
    Button loginOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("SettingActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_back:
                finish();
                break;
            case R.id.login_out_btn:
                loginOut();
                finish();
                break;
        }
    }
    public void initView(){
        settingBack.setOnClickListener(this);
        loginOutBtn.setOnClickListener(this);
    }

    private void loginOut() {
        MApplication.isAdmin = false;
        MApplication.isLogin = false;
        MApplication.currentLoginUser = null;
        DBService dbService = DBService.getInstance(this);
        dbService.clearUserLoginLog();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
