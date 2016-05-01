package com.example.xyy.xyyapplication.source.activity.userList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class AddUserActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.add_user_back)
    ImageButton addUserBack;
    @Bind(R.id.add_user_save)
    Button addUserSave;
    @Bind(R.id.add_user_account_edit)
    EditText addUserAccountEdit;
    @Bind(R.id.add_user_password_edit)
    EditText addUserPasswordEdit;

    private int mAddUserType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddUserActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_activity);
        ButterKnife.bind(this);

        int addUserType = getIntent().getIntExtra(Constant.ADD_USER_TYPE, 0);
        mAddUserType = addUserType;
        initView();
    }

    private void initView() {
        addUserSave.setOnClickListener(this);
        addUserBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_user_back:
                finish();
                break;
            case R.id.add_user_save:
                if (addUser()) {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
                    if (mAddUserType == 1) {
                        setResult(Constant.ADD_ADMIN_USER);
                    } else {
                        setResult(Constant.ADD_NORMAL_USER);
                    }
                } else {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_LONG).show();
                }
                finish();
                break;
        }
    }

    //添加用户
    public Boolean addUser() {
        String account = addUserAccountEdit.getText().toString();
        if (StringUtils.isEmpty(account)) {
            Toast.makeText(this, "账号为空", Toast.LENGTH_LONG).show();
            return false;
        }
        String password = addUserPasswordEdit.getText().toString();
        if (StringUtils.isEmpty(password)) {
            Toast.makeText(this, "密码为空", Toast.LENGTH_LONG).show();
            return false;
        }
        DBService dbService = DBService.getInstance(this);
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setIsDeleted("N");
        user.setGmtCreate(new Date().getTime());
        user.setGmtModified(new Date().getTime());
        if (mAddUserType == 1) {
            user.setIsAdmin("1");
        } else {
            user.setIsAdmin("0");
        }

        if (dbService.insert(user) > 0) {
            return true;
        }
        return false;
    }
}
