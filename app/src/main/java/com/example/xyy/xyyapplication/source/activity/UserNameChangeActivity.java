package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.constant.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/30.
 */
public class UserNameChangeActivity extends Activity {
    @Bind(R.id.user_name_change_back_btn)
    ImageButton userNameChangeBackBtn;
    @Bind(R.id.user_name_save_btn)
    Button userNameSaveBtn;
    @Bind(R.id.user_name_change_edit)
    EditText userNameChangeEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name_change);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString(Constant.USER_NAME);
        userNameChangeEdit.setText(userName);
    }
}
