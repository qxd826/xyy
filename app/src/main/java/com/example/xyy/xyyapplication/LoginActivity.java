package com.example.xyy.xyyapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.xml.datatype.Duration;

/**
 * Created by QXD on 2016/4/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    //µ«¬º∞¥≈•
    private Button loginBt;
    //’ ∫≈ ‰»ÎøÚ
    private EditText accountEt;
    //√‹¬Î ‰»ÎøÚ
    private EditText pwdEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("µ«¬º");
        setTitleColor(getResources().getColor(R.color.red_color));
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
        }
    }

    private void initView(){
        loginBt = (Button)findViewById(R.id.login_button);
        accountEt = (EditText)findViewById(R.id.account);
        pwdEt = (EditText)findViewById(R.id.password);

        loginBt.setOnClickListener(this);
        accountEt.setOnClickListener(this);
        pwdEt.setOnClickListener(this);
    }
}
