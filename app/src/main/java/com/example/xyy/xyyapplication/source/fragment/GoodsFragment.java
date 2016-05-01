package com.example.xyy.xyyapplication.source.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.goods.AddGoodsActivity;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.qrcode.QRMainActivity;

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
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG, "GoodsFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
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
                    startActivity(intent);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        initGoodsList();
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
                break;
        }
    }
}
