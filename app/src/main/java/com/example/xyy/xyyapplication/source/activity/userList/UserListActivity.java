package com.example.xyy.xyyapplication.source.activity.userList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.user.UserListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class UserListActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.user_list_back_btn)
    ImageButton userListBackBtn;
    @Bind(R.id.user_list_text)
    TextView userListText;
    @Bind(R.id.user_list)
    ListView userList;
    @Bind(R.id.add_user)
    Button addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("UserListActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //设置用户列表
        DBService dbService = DBService.getInstance(this);
        List<User> mUserList = dbService.getUserList();
        if (mUserList == null || mUserList.size() < 1) {
            userListText.setVisibility(View.GONE);
        }else{
            userListText.setVisibility(View.VISIBLE);
        }
        UserListAdapter userListAdapter = new UserListAdapter(this, mUserList, MApplication.isAdmin);
        userList.setAdapter(userListAdapter);

        //设置添加按钮显示
        if (MApplication.isAdmin) {
            addUser.setVisibility(View.VISIBLE);
        } else {
            addUser.setVisibility(View.GONE);
        }
        addUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_list_back_btn:
                finish();
                break;
            case R.id.add_user:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra(Constant.ADD_USER_TYPE, Constant.ADD_NORMAL_USER);
                startActivityForResult(intent, R.string.add_user);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case R.string.add_user:
                if (resultCode == 0) {
                    //设置用户列表
                    DBService dbService = DBService.getInstance(this);
                    List<User> mUserList = dbService.getUserList();
                    if(mUserList!= null && mUserList.size() > 0){
                        if(userListText.getVisibility() == View.GONE){
                            userListText.setVisibility(View.VISIBLE);
                        }
                    }
                    UserListAdapter mAdapter = (UserListAdapter)userList.getAdapter();
                    mAdapter.setMUserList(mUserList);
                }
                break;
        }
    }
}
