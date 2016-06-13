package com.example.xyy.xyyapplication.source.activity.userList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.example.xyy.xyyapplication.source.pojo.userLogin.UserLoginLog;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddUserActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_add_activity);
        ButterKnife.bind(this);
        mQueue = Volley.newRequestQueue(this);

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
                addUser();
                break;
        }
    }

    //添加用户
    public void addUser() {
        String account = addUserAccountEdit.getText().toString();
        if (StringUtils.isEmpty(account)) {
            Toast.makeText(this, "账号为空", Toast.LENGTH_LONG).show();
        }
        String password = addUserPasswordEdit.getText().toString();
        if (StringUtils.isEmpty(password)) {
            Toast.makeText(this, "密码为空", Toast.LENGTH_LONG).show();
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("account", account);
        paramMap.put("password", password);
        if(mAddUserType == 1){
            paramMap.put("isAdmin", "1");
        }else{
            paramMap.put("isAdmin", "0");
        }
        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/user/add", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            Toast.makeText(AddUserActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            if (mAddUserType == 1) {
                                setResult(Constant.ADD_ADMIN_USER);
                            } else {
                                setResult(Constant.ADD_NORMAL_USER);
                            }
                        }else {
                            Toast.makeText(AddUserActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        mQueue.add(jsonRequest);
    }
}
