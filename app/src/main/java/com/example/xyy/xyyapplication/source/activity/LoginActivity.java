package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.constant.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by QXD on 2016/4/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    private static final Logger LOG = LoggerFactory.getLogger(LoginActivity.class);
    private Button loginBt;
    private EditText accountEt;
    private EditText pwdEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("登录");
        //setTitleColor(getResources().getColor(R.color.red_color));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_button:
                Toast.makeText(this,"this",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void initView(){
        LOG.info(Constant.LOG_TAG, "initView");
        loginBt = (Button)findViewById(R.id.login_button);
        accountEt = (EditText)findViewById(R.id.account);
        pwdEt = (EditText)findViewById(R.id.password);

        loginBt.setOnClickListener(this);
        accountEt.setOnClickListener(this);
        pwdEt.setOnClickListener(this);
    }
}
