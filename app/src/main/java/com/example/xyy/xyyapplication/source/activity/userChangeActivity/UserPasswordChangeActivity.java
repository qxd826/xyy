package com.example.xyy.xyyapplication.source.activity.userChangeActivity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.xyy.xyyapplication.source.common.ApplicationContextUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    private RequestQueue mQueue;
    private String tempPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_password_change);
        ButterKnife.bind(this);
        mQueue = Volley.newRequestQueue(this);
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
                changePassword();
                break;
        }
    }

    private void changePassword() {
        UserVO currentUser = ApplicationContextUtil.getCurrentLoginUser();
        if (currentUser == null) {
            Toast.makeText(this, "当前登录用户为空", Toast.LENGTH_LONG).show();
            return;
        }
        String oldPassword = userOldPsdEdit.getText().toString();
        if (!StringUtils.equals(oldPassword, currentUser.getPassword())) {
            Toast.makeText(this, "旧密码错误", Toast.LENGTH_LONG).show();
            return;
        }
        String newPasswordOne = userNewPsdEdit1.getText().toString();
        String newPasswordTwo = userNewPsdEdit2.getText().toString();
        if (!StringUtils.equals(newPasswordOne, newPasswordTwo)) {
            Toast.makeText(this, "新密码不相同", Toast.LENGTH_LONG).show();
            return;
        }
        tempPassword = newPasswordOne;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", currentUser.getId());
        paramMap.put("newPassword", newPasswordOne);
        paramMap.put("password", currentUser.getPassword());
        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/user/editPassword", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("修改密码", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if (result.isSuccess()) {
                            Toast.makeText(UserPasswordChangeActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            ApplicationContextUtil.getCurrentLoginUser().setPassword(tempPassword);
                        } else {
                            Toast.makeText(UserPasswordChangeActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                Toast.makeText(UserPasswordChangeActivity.this, "网络错误", Toast.LENGTH_LONG).show();
            }
        }) {
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
