package com.example.xyy.xyyapplication.source.activity.userChangeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
public class UserMobileChangeActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.user_mobile_change_back_btn)
    ImageButton userMobileChangeBackBtn;
    @Bind(R.id.user_mobile_chang_save_btn)
    Button userMobileChangSaveBtn;
    @Bind(R.id.user_mobile_change_text)
    TextView userMobileChangeText;
    @Bind(R.id.user_mobile_change_edit)
    EditText userMobileChangeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mobile_change);
        ButterKnife.bind(this);

        //设置姓名
        Bundle bundle = getIntent().getExtras();
        String userMobile = bundle.getString(Constant.USER_MOBILE);
        if (!StringUtils.isEmpty(userMobile)) {
            userMobileChangeEdit.setText(userMobile);
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_mobile_chang_save_btn:
                saveUserName();
                finish();
                super.onDestroy();
                break;
            case R.id.user_mobile_change_back_btn:
                //将当前activity移出栈顶 并销毁
                finish();
                super.onDestroy();
                break;
        }
    }

    //初始化监听事件
    private void initView() {
        userMobileChangSaveBtn.setOnClickListener(this);
        userMobileChangeBackBtn.setOnClickListener(this);
    }

    //保存修改后的名字
    private void saveUserName() {
        User currentUser = ApplicationContextUtil.getCurrentLoginUser();
        if (currentUser == null || currentUser.getId() == null) {
            DebugLog.e("当前登录用户为空");
            Toast.makeText(this, "当前登录用户为空", Toast.LENGTH_LONG).show();
            return;
        }
        String tempUserMobile = userMobileChangeEdit.getText().toString();
        if (StringUtils.equals(currentUser.getMobile(), tempUserMobile)) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
            return;
        }
        currentUser.setMobile(tempUserMobile);
        DBService dbService = DBService.getInstance(this);
        if (dbService.upDateUser(currentUser) > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
            Intent userMobileIntent = new Intent();
            userMobileIntent.putExtra(Constant.USER_MOBILE, tempUserMobile);
            setResult(Constant.SUCCESS_CODE, userMobileIntent);
        } else {
            DebugLog.e("更新用户信息失败 user:" + currentUser.toString());
            Toast.makeText(this, "更新用户信息失败", Toast.LENGTH_LONG).show();
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
