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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.goods.AddGoodsActivity;
import com.example.xyy.xyyapplication.source.activity.goods.GoodsDetailActivity;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsVO;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.example.xyy.xyyapplication.source.qrcode.QRMainActivity;
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
public class GoodsFragment extends Fragment {
    private static final String TAG = "GoodsFragment";

    @Bind(R.id.sao_yi_sao_btn)
    Button saoYiSaoBtn;
    @Bind(R.id.add_goods_btn)
    Button addGoodsBtn;
    @Bind(R.id.goods_list)
    ListView goodsList;
    @Bind(R.id.add_goods_icon)
    ImageButton addGoodsIcon;
    @Bind(R.id.add_goods_three_btn)
    LinearLayout addGoodsThreeBtn;
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.search_btn)
    ImageButton searchBtn;
    @Bind(R.id.search_clear_btn)
    ImageButton searchClearBtn;

    private List<GoodsVO> goodsVOList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "GoodsFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "GoodsFragment-----onCreateView");
        View view = inflater.inflate(R.layout.goods_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "GoodsFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "GoodsFragment-----onResume");
        super.onResume();
        searchEdit.clearFocus();
        GoodsListAdapter goodsListAdapter = new GoodsListAdapter(getContext(), goodsVOList, MApplication.isAdmin);
        try {
            goodsList.setAdapter(goodsListAdapter);
        } catch (Exception e) {
            DebugLog.e("e" + e.toString());
        }
        initGoodsList();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "GoodsFragment-----onPause");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "GoodsFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        searchEdit.clearFocus();
        addGoodsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddGoodsActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_GOODS_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        saoYiSaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRMainActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_GOODS_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        addGoodsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addGoodsThreeBtn.getVisibility() == View.VISIBLE) {
                    addGoodsThreeBtn.setVisibility(View.GONE);
                } else {
                    addGoodsThreeBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.clearFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                searchGoodsList(searchEdit.getText().toString());
            }
        });
        searchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
                initGoodsList();
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    if (searchClearBtn.getVisibility() == View.GONE) {
                        searchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (searchClearBtn.getVisibility() == View.GONE) {
                        searchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (searchClearBtn.getVisibility() == View.GONE) {
                        searchClearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (searchClearBtn.getVisibility() == View.VISIBLE) {
                        searchClearBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsVO goods = (GoodsVO) goodsList.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_CODE, goods.getGoodsCode());
                startActivity(intent);
            }
        });
    }

    //初始化商品列表
    private void initGoodsList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/goods/list"
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
                            goodsVOList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<GoodsVO>>() {
                            }.getType());
                        } else {
                            Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新用户列表
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

    //搜索商品列表
    private void searchGoodsList(String searchCon) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/goods/search?searchCon="+searchCon
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
                            goodsVOList = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), new TypeToken<List<GoodsVO>>() {
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


    //刷新商品列表
    public void reFreshList() {
        GoodsListAdapter mAdapter = (GoodsListAdapter) goodsList.getAdapter();
        mAdapter.setMGoodsList(goodsVOList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_GOODS_CODE:
                initGoodsList();
                if (addGoodsThreeBtn.getVisibility() == View.VISIBLE) {
                    addGoodsThreeBtn.setVisibility(View.GONE);
                }
                break;
        }
    }
}
