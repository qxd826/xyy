package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsVO;
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
public class AddGoodsActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.add_goods_back)
    ImageButton addGoodsBack;
    @Bind(R.id.add_goods_save)
    Button addGoodsSave;
    @Bind(R.id.add_goods_name_edit)
    EditText addGoodsNameEdit;
    @Bind(R.id.add_goods_code_edit)
    EditText addGoodsCodeEdit;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddGoodsActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_add_activity);
        ButterKnife.bind(this);
        mQueue = Volley.newRequestQueue(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goods_back:
                finish();
                break;
            case R.id.add_goods_save:
                saveGoods();
                break;
        }
    }

    private void initView() {
        addGoodsBack.setOnClickListener(this);
        addGoodsSave.setOnClickListener(this);
        //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int screenH = dm.heightPixels;
    }

    private void saveGoods() {
        String goodsName = addGoodsNameEdit.getText().toString();
        if (StringUtils.isEmpty(goodsName)) {
            Toast.makeText(this, "商品名称为空", Toast.LENGTH_LONG).show();
        }
        String goodsCode = addGoodsCodeEdit.getText().toString();
        if (StringUtils.isEmpty(goodsCode)) {
            Toast.makeText(this, "商品编号为空", Toast.LENGTH_LONG).show();
        }
        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setGoodsName(goodsName);
        goodsVO.setGoodsNum(0);
        goodsVO.setGoodsCode(goodsCode);
        goodsVO.setIsDeleted("N");

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("goodsName", goodsVO.getGoodsName());
        paramMap.put("goodsCode", goodsVO.getGoodsCode());
        paramMap.put("goodsNum",0);

        JSONObject jsonObject = new JSONObject(paramMap);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                MApplication.IP_SERVICE + "xyy/app/goods/add", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("添加用户", "response -> " + response.toString());
                        Gson gson = new Gson();
                        Result result = gson.fromJson(response.toString(), Result.class);
                        if(result.isSuccess()){
                            Toast.makeText(AddGoodsActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(AddGoodsActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddGoodsActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
        mQueue.add(jsonRequest);
    }
}
