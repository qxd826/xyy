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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.goods.AddGoodsActivity;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.qrcode.QRMainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    @Bind(R.id.add_goods_two_btn)
    RelativeLayout addGoodsTwoBtn;
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.search_btn)
    ImageButton searchBtn;
    @Bind(R.id.search_clear_btn)
    ImageButton searchClearBtn;

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
        initGoodsList();
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
                if (addGoodsTwoBtn.getVisibility() == View.VISIBLE) {
                    addGoodsTwoBtn.setVisibility(View.GONE);
                } else {
                    addGoodsTwoBtn.setVisibility(View.VISIBLE);
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
                DBService dbService = DBService.getInstance(getContext());
                List<Goods> goodsListData = dbService.searchGoods(searchEdit.getText().toString());
                if (goodsListData == null) {
                    goodsListData = new ArrayList<Goods>();
                }
                GoodsListAdapter mAdapter = (GoodsListAdapter) goodsList.getAdapter();
                mAdapter.setMGoodsList(goodsListData);
            }
        });
        searchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
                reFreshList();
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
    }

    private void initGoodsList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Goods> mGoodsList = dbService.getGoodsList("1");
        GoodsListAdapter goodsListAdapter = new GoodsListAdapter(getContext(), mGoodsList, MApplication.isAdmin);
        try {
            goodsList.setAdapter(goodsListAdapter);
        } catch (Exception e) {
            DebugLog.e("e" + e.toString());
        }
    }

    public void reFreshList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Goods> mGoodsList = dbService.getGoodsList("1");
        GoodsListAdapter mAdapter = (GoodsListAdapter) goodsList.getAdapter();
        mAdapter.setMGoodsList(mGoodsList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_GOODS_CODE:
                reFreshList();
                if (addGoodsTwoBtn.getVisibility() == View.VISIBLE) {
                    addGoodsTwoBtn.setVisibility(View.GONE);
                }
                break;
        }
    }
}
