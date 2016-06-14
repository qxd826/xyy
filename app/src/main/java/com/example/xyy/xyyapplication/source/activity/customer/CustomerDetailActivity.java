package com.example.xyy.xyyapplication.source.activity.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsInOutAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.customer.CustomerVO;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLogVO;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
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
public class CustomerDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "CustomerDetailActivity";
    private long mCustomerId = 0;
    private CustomerVO mCustomer = null;
    List<GoodsLogVO> goodsLogList = new ArrayList<>();

    @Bind(R.id.customer_detail_back)
    ImageButton customerDetailBack;
    @Bind(R.id.customer_mobile)
    ImageView customerMobile;
    @Bind(R.id.customer_detail_name_text)
    TextView customerDetailNameText;
    @Bind(R.id.customer_detail_name_edit)
    EditText customerDetailNameEdit;
    @Bind(R.id.customer_detail_mobile_text)
    TextView customerDetailMobileText;
    @Bind(R.id.customer_detail_mobile_edit)
    EditText customerDetailMobileEdit;
    @Bind(R.id.customer_edit_btn)
    Button customerEditBtn;
    @Bind(R.id.customer_edit_view)
    LinearLayout customerEditView;
    @Bind(R.id.customer_edit_save_btn)
    Button customerEditSaveBtn;
    @Bind(R.id.customer_edit_cancel_btn)
    Button customerEditCancelBtn;
    @Bind(R.id.customer_save_view)
    LinearLayout customerSaveView;
    @Bind(R.id.customer_goods_list)
    ListView customerGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("CustomerDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail);
        ButterKnife.bind(this);

        long customerId = getIntent().getLongExtra(Constant.CUSTOMER_ID, 0);
        DebugLog.i(TAG, "customerId:" + customerId);
        if (customerId > 0) {
            mCustomerId = customerId;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        customerDetailBack.setOnClickListener(this);
        customerMobile.setOnClickListener(this);
        customerEditBtn.setOnClickListener(this);
        customerEditSaveBtn.setOnClickListener(this);
        customerEditCancelBtn.setOnClickListener(this);
        GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this,goodsLogList);
        customerGoodsList.setAdapter(goodsInOutAdapter);

        if (mCustomerId > 0) {
            initCustomer(mCustomerId);
            initInOutDetail(mCustomerId);
        }
    }

    //初始化客户详情信息
    private void initCustomer(Long customerId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/customer/info?customerId=" + customerId
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
                            mCustomer = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), CustomerVO.class);
                            if (mCustomer.getId() != null) {
                                customerDetailNameText.setText(mCustomer.getCustomerName());
                                customerDetailMobileText.setText(mCustomer.getCustomerMobile());
                            }
                        } else {
                            Toast.makeText(CustomerDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(CustomerDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    //初始化客户出库信息
    private void initInOutDetail(Long customerId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/customer/inOutDetail?customerId=" + customerId
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
                            goodsLogList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<GoodsLogVO>>() {
                            }.getType());
                        } else {
                            Toast.makeText(CustomerDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新供应商入库明细
                        reFreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(CustomerDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_detail_back:
                finish();
                break;
            case R.id.customer_mobile:
                String mobile = mCustomer.getCustomerMobile();
                if(!StringUtils.isBlank(mobile)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "当前客户电话为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.customer_edit_btn:
                if (mCustomerId < 1) {
                    Toast.makeText(this, "当前客户信息不存在", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (customerDetailNameEdit.getVisibility() == View.GONE) {
                    customerDetailNameEdit.setVisibility(View.VISIBLE);
                    customerDetailNameEdit.setText(mCustomer.getCustomerName());
                }
                if (customerDetailMobileEdit.getVisibility() == View.GONE) {
                    customerDetailMobileEdit.setVisibility(View.VISIBLE);
                    customerDetailMobileEdit.setText(mCustomer.getCustomerMobile());
                }
                customerDetailMobileText.setVisibility(View.GONE);
                customerDetailNameText.setVisibility(View.GONE);
                customerDetailNameEdit.requestFocus();
                customerEditView.setVisibility(View.GONE);
                customerSaveView.setVisibility(View.VISIBLE);
                break;
            case R.id.customer_edit_save_btn:
                String customerName = customerDetailNameEdit.getText().toString();
                String customerMobile = customerDetailMobileEdit.getText().toString();
                if (StringUtils.isBlank(customerName)) {
                    Toast.makeText(this, "客户姓名为空", Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isBlank(customerMobile)) {
                    Toast.makeText(this, "客户电话为空", Toast.LENGTH_SHORT).show();
                }
                if (mCustomerId < 1) {
                    Toast.makeText(this, "客户信息为空", Toast.LENGTH_SHORT).show();
                }

                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("id",mCustomerId);
                paramMap.put("customerMobile", customerMobile);
                paramMap.put("customerName", customerName);

                JSONObject jsonObject = new JSONObject(paramMap);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                        MApplication.IP_SERVICE + "xyy/app/customer/edit", jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("修改客户", "response -> " + response.toString());
                                Gson gson = new Gson();
                                Result result = gson.fromJson(response.toString(), Result.class);
                                if(result.isSuccess()){
                                    Toast.makeText(CustomerDetailActivity.this, "更新成功", Toast.LENGTH_LONG).show();
                                    finish();
                                }else {
                                    Toast.makeText(CustomerDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerDetailActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
                closeEditView();
                customerDetailMobileText.setText(mCustomer.getCustomerMobile());
                customerDetailNameText.setText(mCustomer.getCustomerName());
                break;
            case R.id.customer_edit_cancel_btn:
                closeEditView();
                break;
        }
    }
    private void closeEditView() {
        customerDetailNameEdit.clearFocus();
        customerDetailMobileEdit.clearFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        if (customerDetailNameEdit.getVisibility() == View.VISIBLE) {
            customerDetailNameEdit.setVisibility(View.GONE);
            customerDetailNameEdit.setText(mCustomer.getCustomerName());
        }
        if (customerDetailMobileEdit.getVisibility() == View.VISIBLE) {
            customerDetailMobileEdit.setVisibility(View.GONE);
            customerDetailMobileEdit.setText(mCustomer.getCustomerMobile());
        }
        customerDetailMobileText.setVisibility(View.VISIBLE);
        customerDetailNameText.setVisibility(View.VISIBLE);
        customerEditView.setVisibility(View.VISIBLE);
        customerSaveView.setVisibility(View.GONE);
    }
    private void reFreshList(){
        GoodsInOutAdapter goodsInOutAdapter = (GoodsInOutAdapter)customerGoodsList.getAdapter();
        goodsInOutAdapter.setMGoodsLogList(goodsLogList);
    }
}
