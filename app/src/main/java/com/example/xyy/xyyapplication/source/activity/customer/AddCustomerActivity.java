package com.example.xyy.xyyapplication.source.activity.customer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
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
public class AddCustomerActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.add_customer_back)
    ImageButton addCustomerBack;
    @Bind(R.id.add_customer_save)
    Button addCustomerSave;
    @Bind(R.id.add_customer_name_edit)
    EditText addCustomerNameEdit;
    @Bind(R.id.add_customer_mobile_edit)
    EditText addCustomerMobileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddSupplyActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_add_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_customer_back:
                finish();
                break;
            case R.id.add_customer_save:
                saveCustomer();
                finish();
                break;
        }
    }

    private void initView() {
        addCustomerBack.setOnClickListener(this);
        addCustomerSave.setOnClickListener(this);
    }

    private void saveCustomer() {
        String customerName = addCustomerNameEdit.getText().toString();
        if (StringUtils.isEmpty(customerName)) {
            Toast.makeText(this, "客户名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        String customerMobile = addCustomerMobileEdit.getText().toString();
        if (StringUtils.isEmpty(customerMobile)) {
            Toast.makeText(this, "客户电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setCustomerMobile(customerMobile);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("customerName", customer.getCustomerName());
        paramMap.put("customerMobile", customer.getCustomerMobile());

        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/customer/add", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            Toast.makeText(AddCustomerActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(AddCustomerActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCustomerActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
        MApplication.mQueue.add(jsonRequest);
    }
}
