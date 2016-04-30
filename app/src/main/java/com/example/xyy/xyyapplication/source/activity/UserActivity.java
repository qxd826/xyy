package com.example.xyy.xyyapplication.source.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/29.
 */
public class UserActivity extends Activity implements View.OnClickListener {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        User currentUser = ApplicationContextUtil.getCurrentLoginUser(this);
        if (null != currentUser) {
            String userName = currentUser.getUserName();
            String mobile = currentUser.getMobile();
            if (!StringUtils.isEmpty(userName)) {
                userNameTextId.setText(userName);
            }
            if (null != currentUser.getMobile()) {
                userMobileTextId.setText(mobile);
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

    private void changeUserName() {
        Intent intent = new Intent(this, UserNameChangeActivity.class);
        intent.putExtra(Constant.USER_NAME, userNameTextId.getText());
        startActivity(intent);
    }

    private void changeMobile() {

    }
}
