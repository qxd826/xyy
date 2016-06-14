package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerSpinnerAdapter;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplySpinnerAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.customer.CustomerVO;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsVO;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.example.xyy.xyyapplication.source.pojo.supply.SupplyVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "GoodsDetailActivity";
    private String mGoodsCode = null;
    private GoodsVO mGoods = null;
    private List<SupplyVO> mSupplyList = new ArrayList<>();
    private List<CustomerVO> mCustomerList = new ArrayList<>();
    private SupplyVO currentSupply = null;
    private CustomerVO currentCustomer = null;

    @Bind(R.id.goods_in_edit)
    EditText goodsInEdit;
    @Bind(R.id.in_goods_supply_spinner)
    Spinner inGoodsSupplySpinner;
    @Bind(R.id.goods_out_edit)
    EditText goodsOutEdit;
    @Bind(R.id.out_goods_customer_spinner)
    Spinner outGoodsCustomerSpinner;
    @Bind(R.id.in_goods_btn)
    Button inGoodsBtn;
    @Bind(R.id.out_goods_btn)
    Button outGoodsBtn;
    @Bind(R.id.in_out_detail_btn)
    Button inOutDetailBtn;
    @Bind(R.id.goods_detail_back)
    ImageButton goodsDetailBack;
    @Bind(R.id.goods_detail_name_text)
    TextView goodsDetailNameText;
    @Bind(R.id.goods_detail_number_text)
    TextView goodsDetailNumberText;
    @Bind(R.id.goods_detail_code_text)
    TextView goodsDetailCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("GoodsDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ButterKnife.bind(this);

        String goodsCode = getIntent().getStringExtra(Constant.GOODS_CODE);
        DebugLog.i(TAG, "goodsCode:" + goodsCode);
        if (!StringUtils.isBlank(goodsCode)) {
            mGoodsCode = goodsCode;
        }
        initView();
    }

    @Override
    protected void onResume() {
        if (!StringUtils.isBlank(mGoodsCode)) {
            initGoods(mGoodsCode);
        }
        initSupplyAndCustomerSpinner();
        super.onResume();
    }
    private void initView(){
        inGoodsBtn.setOnClickListener(this);
        outGoodsBtn.setOnClickListener(this);
        inOutDetailBtn.setOnClickListener(this);
        goodsDetailBack.setOnClickListener(this);
    }

    //初始化商品详情
    private void initGoods(String goodsCode) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/goods/info?goodsCode=" + goodsCode
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
                            mGoods = JsonUtil.fromJson(JsonUtil.toJson(result.getData()),GoodsVO.class);
                            if (mGoods.getId() != null) {
                                goodsDetailNameText.setText(mGoods.getGoodsName());
                                goodsDetailNumberText.setText(mGoods.getGoodsNum() + "");
                                goodsDetailCodeText.setText(mGoods.getGoodsCode());
                            }
                        } else {
                            Toast.makeText(GoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(GoodsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_goods_btn:
                inGoods();
                break;
            case R.id.out_goods_btn:
                outGoods();
                break;
            case R.id.in_out_detail_btn:
                Intent intent = new Intent(this,InOutGoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_CODE, mGoodsCode);
                startActivity(intent);
                break;
            case R.id.goods_detail_back:
                finish();
                break;
        }
    }

    //商品入库
    private void inGoods() {
        String textNum = goodsInEdit.getText().toString();
        if(StringUtils.isBlank(textNum)){
            Toast.makeText(GoodsDetailActivity.this, "入库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //入库数量
        Integer num = Integer.valueOf(goodsInEdit.getText().toString());
        if (num < 1) {
            Toast.makeText(GoodsDetailActivity.this, "入库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //入库供应商
        if (null == currentSupply || currentSupply.getId() < 1) {
            Toast.makeText(GoodsDetailActivity.this, "请选择供应商", Toast.LENGTH_SHORT).show();
            return;
        }
        //商品信息校验
        if (StringUtils.isBlank(mGoodsCode)) {
            Toast.makeText(GoodsDetailActivity.this, "当前商品信息为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("goodsNum", num);
        paramMap.put("goodsCode", mGoodsCode);
        paramMap.put("type", 0);
        paramMap.put("supplyId", currentSupply.getId());

        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/goods/inOrOut", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            goodsInEdit.setText("");
                            Toast.makeText(GoodsDetailActivity.this, "入库成功", Toast.LENGTH_LONG).show();
                            initGoods(mGoodsCode);
                        }else {
                            Toast.makeText(GoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GoodsDetailActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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

    //商品出库
    private void outGoods() {
        String textNum = goodsOutEdit.getText().toString();
        if(StringUtils.isBlank(textNum)){
            Toast.makeText(GoodsDetailActivity.this, "出库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //出库数量
        Integer num = Integer.valueOf(goodsOutEdit.getText().toString());
        if (num < 1) {
            Toast.makeText(GoodsDetailActivity.this, "出库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //出库客户
        if (null == currentCustomer || currentCustomer.getId() < 1) {
            Toast.makeText(GoodsDetailActivity.this, "请选择客户", Toast.LENGTH_SHORT).show();
            return;
        }
        //商品信息校验
        if (StringUtils.isBlank(mGoodsCode)) {
            Toast.makeText(GoodsDetailActivity.this, "当前商品信息为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("goodsNum", num);
        paramMap.put("goodsCode", mGoodsCode);
        paramMap.put("type", 1);
        paramMap.put("customerId", currentCustomer.getId());

        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/goods/inOrOut", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            Toast.makeText(GoodsDetailActivity.this, "出库成功", Toast.LENGTH_LONG).show();
                            goodsOutEdit.setText("");
                            initGoods(mGoodsCode);
                        }else {
                            Toast.makeText(GoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GoodsDetailActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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


    //初始化 供应商和客户列表
    private void initSupplyAndCustomerSpinner() {
        SupplySpinnerAdapter supplySpinnerAdapter = new SupplySpinnerAdapter(this, mSupplyList);
        inGoodsSupplySpinner.setAdapter(supplySpinnerAdapter);
        inGoodsSupplySpinner.setGravity(Gravity.CENTER);
        inGoodsSupplySpinner.setSelection(0, true);
        if (mSupplyList.size() != 0) {
            currentSupply = mSupplyList.get(0);
        }
        inGoodsSupplySpinner.setPrompt("请选择供应商");
        inGoodsSupplySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSupply = mSupplyList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //获取供应商列表
        getSupplyList();

        CustomerSpinnerAdapter customerSpinnerAdapter = new CustomerSpinnerAdapter(this, mCustomerList);
        outGoodsCustomerSpinner.setAdapter(customerSpinnerAdapter);
        outGoodsCustomerSpinner.setGravity(Gravity.CENTER);
        outGoodsCustomerSpinner.setSelection(0, true);
        if (mCustomerList.size() != 0) {
            currentCustomer = mCustomerList.get(0);
        }
        outGoodsCustomerSpinner.setPrompt("请选择客户");
        outGoodsCustomerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCustomer = mCustomerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //获取客户列表
        getCustomerList();
    }

    private void getSupplyList(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/supply/list"
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
                            mSupplyList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<SupplyVO>>() {
                            }.getType());

                        } else {
                            Toast.makeText(GoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新供应商列表
                        reFreshSupplyList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(GoodsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    private void reFreshSupplyList(){
        SupplySpinnerAdapter supplySpinnerAdapter = (SupplySpinnerAdapter)inGoodsSupplySpinner.getAdapter();
        supplySpinnerAdapter.setMSupplyList(mSupplyList);
        if(mSupplyList.size() > 0){
            currentSupply = mSupplyList.get(0);
        }
    }

    private void getCustomerList(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/customer/list"
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
                            mCustomerList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<CustomerVO>>() {
                            }.getType());
                        } else {
                            Toast.makeText(GoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新供应商列表
                        reFreshCustomerList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(GoodsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    private void reFreshCustomerList(){
        CustomerSpinnerAdapter customerSpinnerAdapter = (CustomerSpinnerAdapter)outGoodsCustomerSpinner.getAdapter();
        customerSpinnerAdapter.setMCustomerList(mCustomerList);
        if(mCustomerList.size() > 0){
            currentCustomer = mCustomerList.get(0);
        }
    }
}
