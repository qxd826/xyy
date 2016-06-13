package com.example.xyy.xyyapplication.source.activity.userChangeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.example.xyy.xyyapplication.source.common.DebugLog;
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
public class UserMobileChangeActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.user_mobile_change_back_btn)
    ImageButton userMobileChangeBackBtn;
    @Bind(R.id.user_mobile_chang_save_btn)
    Button userMobileChangSaveBtn;
    @Bind(R.id.user_mobile_change_text)
    TextView userMobileChangeText;
    @Bind(R.id.user_mobile_change_edit)
    EditText userMobileChangeEdit;

    private RequestQueue mQueue;
    private String tempMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_mobile_change);
        ButterKnife.bind(this);
        mQueue = Volley.newRequestQueue(this);

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
                saveMobile();
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
    private void saveMobile() {
        UserVO currentUser = ApplicationContextUtil.getCurrentLoginUser();
        if (currentUser == null || currentUser.getId() == null) {
            DebugLog.e("当前登录用户为空");
            Toast.makeText(this, "当前登录用户为空", Toast.LENGTH_LONG).show();
            return;
        }
        String tempUserMobile = userMobileChangeEdit.getText().toString();
        tempMobile = tempUserMobile;
        if (StringUtils.equals(currentUser.getMobile(), tempUserMobile)) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", currentUser.getId());
        paramMap.put("mobile", tempUserMobile);
        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/user/edit", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("修改用户电话", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if (result.isSuccess()) {
                            Toast.makeText(UserMobileChangeActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            ApplicationContextUtil.getCurrentLoginUser().setMobile(tempMobile);
                            Intent userMobileIntent = new Intent();
                            setResult(Constant.SUCCESS_CODE, userMobileIntent);
                            finish();
                        } else {
                            Toast.makeText(UserMobileChangeActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", error.getMessage(), error);
                Toast.makeText(UserMobileChangeActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Do something.
            return false;
        }*/
        return super.onKeyDown(keyCode, event);
    }
}
