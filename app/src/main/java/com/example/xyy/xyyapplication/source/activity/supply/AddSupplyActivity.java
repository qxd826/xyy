package com.example.xyy.xyyapplication.source.activity.supply;

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
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsVO;
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
public class AddSupplyActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.add_supply_back)
    ImageButton addSupplyBack;
    @Bind(R.id.add_supply_save)
    Button addSupplySave;
    @Bind(R.id.add_supply_name_edit)
    EditText addSupplyNameEdit;
    @Bind(R.id.add_supply_mobile_edit)
    EditText addSupplyMobileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddSupplyActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supply_add_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_supply_back:
                finish();
                break;
            case R.id.add_supply_save:
                saveSupply();
                break;
        }
    }

    private void initView() {
        addSupplyBack.setOnClickListener(this);
        addSupplySave.setOnClickListener(this);
    }

    private void saveSupply() {
        String supplyName = addSupplyNameEdit.getText().toString();
        if (StringUtils.isEmpty(supplyName)) {
            Toast.makeText(this, "供应商名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        String supplyMobile = addSupplyMobileEdit.getText().toString();
        if (StringUtils.isEmpty(supplyMobile)) {
            Toast.makeText(this, "供应商电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        Supply supply = new Supply();
        supply.setSupplyName(supplyName);
        supply.setSupplyMobile(supplyMobile);
        supply.setIsDeleted("N");

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("supplyMobile", supply.getSupplyMobile());
        paramMap.put("supplyName", supply.getSupplyName());

        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/supply/add", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            Toast.makeText(AddSupplyActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(AddSupplyActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddSupplyActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
