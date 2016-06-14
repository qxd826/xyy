package com.example.xyy.xyyapplication.source.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.customer.AddCustomerActivity;
import com.example.xyy.xyyapplication.source.activity.customer.CustomerDetailActivity;
import com.example.xyy.xyyapplication.source.activity.goods.GoodsDetailActivity;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerListAdapter;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.customer.CustomerVO;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/28.
 */
public class CustomerFragment extends Fragment {
    private static final String TAG = "CustomerFragment";
    @Bind(R.id.customer_title)
    TextView customerTitle;
    @Bind(R.id.add_customer)
    Button addCustomer;
    @Bind(R.id.customer_list)
    ListView customerList;
    @Bind(R.id.customer_search_clear_btn)
    ImageButton customerSearchClearBtn;
    @Bind(R.id.customer_search_edit)
    EditText customerSearchEdit;
    @Bind(R.id.customer_search_btn)
    ImageButton customerSearchBtn;

    List<CustomerVO> mCustomerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CustomerFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "CustomerFragment-----onCreateView");
        View view = inflater.inflate(R.layout.customer_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "CustomerFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "CustomerFragment-----onResume");
        super.onResume();
        customerSearchEdit.clearFocus();
        CustomerListAdapter customerListAdapter = new CustomerListAdapter(getContext(), mCustomerList, MApplication.isAdmin);
        customerList.setAdapter(customerListAdapter);
        initCustomerList();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "CustomerFragment-----onPause");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(customerSearchEdit.getWindowToken(), 0);
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "CustomerFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        customerSearchEdit.clearFocus();
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCustomerActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_CUSTOMER_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        customerSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSearchEdit.clearFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                searchCustomerList(customerSearchEdit.getText().toString());
            }
        });
        customerSearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSearchEdit.setText("");
                initCustomerList();
            }
        });
        customerSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (customerSearchClearBtn.getVisibility() == View.VISIBLE) {
                        customerSearchClearBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        customerSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerVO customer = (CustomerVO) customerList.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
                intent.putExtra(Constant.CUSTOMER_ID, customer.getId());
                startActivity(intent);
            }
        });
    }

    //初始化获取客户列表
    private void initCustomerList() {
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
                            Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新供应商列表
                        reFreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    //搜索客户列表
    private void searchCustomerList(String searchCon){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/customer/search?searchCon="+searchCon
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
                            Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新商品列表
                        reFreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    public void reFreshList() {
        CustomerListAdapter mAdapter = (CustomerListAdapter) customerList.getAdapter();
        mAdapter.setMCustomerList(mCustomerList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_CUSTOMER_CODE:
                initCustomerList();
                break;
        }
    }
}
