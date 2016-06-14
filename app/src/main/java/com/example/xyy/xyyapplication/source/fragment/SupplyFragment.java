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
import com.example.xyy.xyyapplication.source.activity.supply.AddSupplyActivity;
import com.example.xyy.xyyapplication.source.activity.supply.SupplyDetailActivity;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerListAdapter;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplyListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsVO;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.example.xyy.xyyapplication.source.pojo.supply.SupplyVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/27.
 */
public class SupplyFragment extends Fragment {
    private static final String TAG = "SupplyFragment";
    @Bind(R.id.supply_title)
    TextView supplyTitle;
    @Bind(R.id.add_supply)
    Button addSupply;
    @Bind(R.id.supply_list)
    ListView supplyList;
    @Bind(R.id.supply_search_clear_btn)
    ImageButton supplySearchClearBtn;
    @Bind(R.id.supply_search_edit)
    EditText supplySearchEdit;
    @Bind(R.id.supply_search_btn)
    ImageButton supplySearchBtn;

    private List<SupplyVO> mSupplyList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "SupplyFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "SupplyFragment-----onCreateView");
        View view = inflater.inflate(R.layout.supply_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "SupplyFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "SupplyFragment-----onResume");
        super.onResume();
        supplySearchEdit.clearFocus();
        SupplyListAdapter supplyListAdapter = new SupplyListAdapter(getContext(), mSupplyList, MApplication.isAdmin);
        try {
            supplyList.setAdapter(supplyListAdapter);
        } catch (Exception e) {
            DebugLog.e(e.toString());
        }
        initSupplyList();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "SupplyFragment-----onPause");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(supplySearchEdit.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "SupplyFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        supplySearchEdit.clearFocus();
        addSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSupplyActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_SUPPLY_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        supplySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplySearchEdit.clearFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                searchGoodsList(supplySearchEdit.getText().toString());
            }
        });
        supplySearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplySearchEdit.setText("");
                initSupplyList();
            }
        });
        supplySearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (supplySearchClearBtn.getVisibility() == View.VISIBLE) {
                        supplySearchClearBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        supplySearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        supplyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SupplyVO supply = (SupplyVO) supplyList.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), SupplyDetailActivity.class);
                intent.putExtra(Constant.SUPPLY_ID, supply.getId());
                startActivity(intent);
            }
        });
    }

    //初始化供应商列表
    private void initSupplyList() {
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

    //搜索供应商列表
    private void searchGoodsList(String searchCon) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/supply/search?searchCon="+searchCon
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
        SupplyListAdapter mAdapter = (SupplyListAdapter) supplyList.getAdapter();
        mAdapter.setMSupplyList(mSupplyList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_SUPPLY_CODE:
                initSupplyList();
                break;
        }
    }
}
