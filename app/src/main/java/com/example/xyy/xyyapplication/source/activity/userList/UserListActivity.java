package com.example.xyy.xyyapplication.source.activity.userList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.user.UserListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class UserListActivity extends Activity implements View.OnClickListener {
    private final String TAG = "UserListActivity";
    @Bind(R.id.user_list_back_btn)
    ImageButton userListBackBtn;
    @Bind(R.id.user_list_text)
    TextView userListText;
    @Bind(R.id.user_list)
    ListView userList;
    @Bind(R.id.add_user)
    Button addUser;

    private List<UserVO> userVOList = new ArrayList<>();
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("UserListActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_activity);
        ButterKnife.bind(this);
        mQueue = Volley.newRequestQueue(this);
        initView();
    }

    private void initView() {
        userListText.setVisibility(View.GONE);
        UserListAdapter userListAdapter = new UserListAdapter(this, userVOList, MApplication.isAdmin, mQueue);
        userList.setAdapter(userListAdapter);
        getUserList();

        //设置添加按钮显示
        if (MApplication.isAdmin) {
            addUser.setVisibility(View.VISIBLE);
        } else {
            addUser.setVisibility(View.GONE);
        }
        addUser.setOnClickListener(this);
        userListBackBtn.setOnClickListener(this);
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
                    //刷新用户列表
                    getUserList();
                    refreshList();
                }
                break;
        }
    }

    //刷新用户列表显示
    private void refreshList() {
        if (userVOList.size() > 0) {
            userListText.setVisibility(View.VISIBLE);
        } else {
            userListText.setVisibility(View.GONE);
        }
        UserListAdapter mAdapter = (UserListAdapter) userList.getAdapter();
        mAdapter.setMUserList(userVOList);
    }

    //获取用户列表
    private void getUserList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/user/list"
                , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        Gson gson = new Gson();
                        Log.i(TAG, "获取结果");
                        Result result = gson.fromJson(response.toString(), Result.class);
                        Log.i(TAG, "获取结果:" + result.toString());
                        if (result.isSuccess()) {
                            userVOList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<UserVO>>() {
                            }.getType());
                        } else {
                            Toast.makeText(UserListActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        refreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(UserListActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }
}
