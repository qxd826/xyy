package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/30.
 */
public class UserNameChangeActivity extends Activity implements View.OnClickListener {
    private String historyUserName = null;

    @Bind(R.id.user_name_change_back_btn)
    ImageButton userNameChangeBackBtn;
    @Bind(R.id.user_name_chang_save_btn)
    Button userNameSaveBtn;
    @Bind(R.id.user_name_change_edit)
    EditText userNameChangeEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name_change);
        ButterKnife.bind(this);

        //设置姓名
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString(Constant.USER_NAME);
        if (!StringUtils.isEmpty(userName)) {
            userNameChangeEdit.setText(userName);
            historyUserName = userName;
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_chang_save_btn:
                saveUserName();
                finish();
                super.onDestroy();
                break;
            case R.id.user_name_change_back_btn:
                //将当前activity移出栈顶 并销毁
                finish();
                super.onDestroy();
                break;
        }
    }

    //初始化监听事件
    private void initView() {
        userNameSaveBtn.setOnClickListener(this);
        userNameChangeBackBtn.setOnClickListener(this);
    }

    //保存修改后的名字
    private void saveUserName() {
        User currentUser = ApplicationContextUtil.getCurrentLoginUser(this);
        if (currentUser == null || currentUser.getId() == null) {
            DebugLog.e("当前登录用户为空");
            Toast.makeText(this,"当前登录用户为空",Toast.LENGTH_LONG).show();
            return;
        }
        String tempUserName = userNameChangeEdit.getText().toString();
        User tempUser = new User();
        tempUser.setId(currentUser.getId());
        tempUser.setUserName(tempUserName);
        DBService dbService = DBService.getInstance(this);
        if (dbService.upDateUser(tempUser) > 0) {
            Intent userNameIntent = new Intent();
            userNameIntent.putExtra("user_change_name", tempUserName);
            setResult(Constant.SUCCESS_CODE, userNameIntent);
        } else {
            DebugLog.e("更新用户信息失败 user:" + tempUser.toString());
            Toast.makeText(this,"更新用户信息失败",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Do something.
            return false;
        }*/
        return super.onKeyDown(keyCode, event);
    }
}
