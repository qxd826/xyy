package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/29.
 */
public class UserActivity extends Activity implements View.OnClickListener {
    private String currentUserName;
    private String currentUserMobile;

    @Bind(R.id.user_name_text_id)
    TextView userNameTextId;
    @Bind(R.id.user_name_item)
    RelativeLayout userNameItem;
    @Bind(R.id.user_mobile_text_id)
    TextView userMobileTextId;
    @Bind(R.id.user_mobile_item)
    RelativeLayout userMobileItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("UserActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        DebugLog.i("UserActivity onResume ......");
        super.onResume();
    }

    @Override
    protected void onPause(){
        DebugLog.i("UserActivity onPause......");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DebugLog.i("UserActivity onDestroy......");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        DebugLog.i("UserActivity onStop......");
        super.onStop();
    }

    private void initView() {
        User currentUser = ApplicationContextUtil.getCurrentLoginUser(this);
        if (null != currentUser) {
            String userName = currentUser.getUserName();
            String mobile = currentUser.getMobile();
            if (!StringUtils.isEmpty(userName)) {
                userNameTextId.setText(userName);
                currentUserName = userName;
            }
            if (null != currentUser.getMobile()) {
                userMobileTextId.setText(mobile);
                currentUserMobile = mobile;
            }
        }
        userNameItem.setOnClickListener(this);
        userMobileItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_name_item:
                changeUserName();
                break;
            case R.id.user_mobile_item:

                break;
        }
    }

    //修改姓名
    private void changeUserName() {
        Intent intent = new Intent(this, UserNameChangeActivity.class);
        intent.putExtra(Constant.USER_NAME, userNameTextId.getText());
        startActivityForResult(intent, R.string.user_name);
    }

    //修改电话号
    private void changeMobile() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case R.string.user_name:
                if(resultCode == 1){
                    Bundle bundle = data.getExtras();
                    String userName = bundle.getString("user_change_name");
                    userNameTextId.setText(userName);
                }
                break;
        }
    }
}
