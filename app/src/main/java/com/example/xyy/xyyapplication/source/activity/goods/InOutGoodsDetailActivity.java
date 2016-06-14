package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLogVO;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class InOutGoodsDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "InOutGoodsActivity";
    @Bind(R.id.in_out_back)
    ImageButton inOutBack;
    @Bind(R.id.goods_in_out_list)
    ListView goodsInOutList;

    List<GoodsLogVO> goodsLogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("InOutGoodsDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_in_out_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        inOutBack.setOnClickListener(this);
        String goodsCode = getIntent().getStringExtra(Constant.GOODS_CODE);
        DebugLog.i(TAG, "goodsCode:" + goodsCode);
        if (!StringUtils.isBlank(goodsCode)) {
            GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this, goodsLogList);
            goodsInOutList.setAdapter(goodsInOutAdapter);
            //获取出入库明细
            getInOutLog(goodsCode);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_out_back:
                finish();
                break;
        }
    }

    private void getInOutLog(String goodsCode){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/goods/inOutDetail?goodsCode=" + goodsCode
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
                            Toast.makeText(InOutGoodsDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新商品出入库明细
                        reFreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(InOutGoodsDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    private void reFreshList(){
        GoodsInOutAdapter goodsInOutAdapter = (GoodsInOutAdapter)goodsInOutList.getAdapter();
        goodsInOutAdapter.setMGoodsLogList(goodsLogList);
    }
}
