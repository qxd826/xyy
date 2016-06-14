package com.example.xyy.xyyapplication.source.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.goods.GoodsDetailActivity;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
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

public class QRMainActivity extends Activity implements OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private final static String TAG = "QRMainActivity";
    private String mGoodsCode = null;

    @Bind(R.id.below_view)
    LinearLayout belowView;
    @Bind(R.id.view_1)
    View view1;
    @Bind(R.id.add_goods_code)
    TextView addGoodsCode;
    @Bind(R.id.add_goods_code_text)
    TextView addGoodsCodeText;
    @Bind(R.id.add_goods_code_edit)
    EditText addGoodsCodeEdit;
    @Bind(R.id.goods_view)
    RelativeLayout goodsView;
    @Bind(R.id.add_goods_name)
    TextView addGoodsName;
    @Bind(R.id.add_goods_name_text)
    TextView addGoodsNameText;
    @Bind(R.id.add_goods_name_edit)
    EditText addGoodsNameEdit;

    @Bind(R.id.add_goods_btn)
    Button addGoodsBtn;
    @Bind(R.id.add_goods_layout)
    LinearLayout addGoodsLayout;

    private TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_main);
        ButterKnife.bind(this);
        mTextView = (TextView) findViewById(R.id.goods_code_result);
        mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
        Button mButton = (Button) findViewById(R.id.span_button);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(QRMainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView() {
        addGoodsBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    mGoodsCode = bundle.getString("result");
                    initGoodsView(mGoodsCode);
                    view1.setVisibility(View.VISIBLE);
                    mTextView.setText(mGoodsCode);
                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    private void initGoodsView(String goodsCode) {
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
                            GoodsVO mGoods = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), GoodsVO.class);
                            if (mGoods != null) {
                                Intent intent = new Intent(QRMainActivity.this, GoodsDetailActivity.class);
                                intent.putExtra(Constant.GOODS_CODE, mGoods.getGoodsCode());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            belowView.setVisibility(View.VISIBLE);
                            goodsView.setVisibility(View.VISIBLE);

                            addGoodsCodeText.setVisibility(View.GONE);
                            addGoodsNameText.setVisibility(View.GONE);
                            addGoodsCodeEdit.setVisibility(View.VISIBLE);
                            addGoodsNameEdit.setVisibility(View.VISIBLE);

                            addGoodsLayout.setVisibility(View.VISIBLE);
                            addGoodsCodeEdit.setText(mGoodsCode);
                            addGoodsNameEdit.requestFocus();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(QRMainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goods_btn:
                addGoods();
                break;
        }
    }

    //添加商品
    private void addGoods() {
        String goodsName = addGoodsNameEdit.getText().toString();
        if (StringUtils.isEmpty(goodsName)) {
            Toast.makeText(this, "商品名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        String goodsCode = addGoodsCodeEdit.getText().toString();
        if (StringUtils.isEmpty(goodsCode)) {
            Toast.makeText(this, "商品编号为空", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("goodsName", goodsName);
        paramMap.put("goodsCode", goodsCode);
        paramMap.put("goodsNum", 0);

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
                            Toast.makeText(QRMainActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(QRMainActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRMainActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
