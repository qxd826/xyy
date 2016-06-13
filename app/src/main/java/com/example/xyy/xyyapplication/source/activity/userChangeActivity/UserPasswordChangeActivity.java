package com.example.xyy.xyyapplication.source.activity.userChangeActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/30.
 */
public class UserPasswordChangeActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.user_psd_change_back_btn)
    ImageButton userPsdChangeBackBtn;
    @Bind(R.id.user_pwd_change_save_btn)
    Button userPwdChangeSaveBtn;
    @Bind(R.id.user_old_psd_edit)
    EditText userOldPsdEdit;
    @Bind(R.id.user_new_psd_edit1)
    EditText userNewPsdEdit1;
    @Bind(R.id.user_new_psd_edit2)
    EditText userNewPsdEdit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_password_change);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        userPsdChangeBackBtn.setOnClickListener(this);
        userPwdChangeSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_psd_change_back_btn:
                finish();
                break;
            case R.id.user_pwd_change_save_btn:
                if (changePassword()) {
                    finish();
                }
                break;
        }
    }

    private Boolean changePassword() {
        UserVO currentUser = ApplicationContextUtil.getCurrentLoginUser();
        if (currentUser == null) {
            Toast.makeText(this, "重新登录", Toast.LENGTH_LONG).show();
            return false;
        }
        String oldPassword = userOldPsdEdit.getText().toString();
        if (!StringUtils.equals(oldPassword, currentUser.getPassword())) {
            Toast.makeText(this, "旧密码错误", Toast.LENGTH_LONG).show();
            return false;
        }
        String newPasswordOne = userNewPsdEdit1.getText().toString();
        String newPasswordTwo = userNewPsdEdit2.getText().toString();
        if (!StringUtils.equals(newPasswordOne, newPasswordTwo)) {
            Toast.makeText(this, "新密码不相同", Toast.LENGTH_LONG).show();
            return false;
        }

        DBService dbService = DBService.getInstance(this);
        currentUser.setPassword(newPasswordOne);
/*        if (dbService.upDateUser(currentUser) > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
            return true;
        }*/
        return false;
    }
}
