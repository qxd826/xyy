package com.example.xyy.xyyapplication.source.activity.supply;

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
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLogVO;
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
public class SupplyDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "SupplyDetailActivity";
    private Long mSupplyId = 0l;
    private SupplyVO mSupply = null;
    List<GoodsLogVO> goodsLogList = new ArrayList<>();

    @Bind(R.id.supply_detail_back)
    ImageButton supplyDetailBack;
    @Bind(R.id.supply_mobile)
    ImageView supplyMobile;
    @Bind(R.id.supply_detail_name_text)
    TextView supplyDetailNameText;
    @Bind(R.id.supply_detail_name_edit)
    EditText supplyDetailNameEdit;
    @Bind(R.id.supply_detail_mobile_text)
    TextView supplyDetailMobileText;
    @Bind(R.id.supply_detail_mobile_edit)
    EditText supplyDetailMobileEdit;
    @Bind(R.id.supply_edit_btn)
    Button supplyEditBtn;
    @Bind(R.id.supply_edit_view)
    LinearLayout supplyEditView;
    @Bind(R.id.supply_edit_save_btn)
    Button supplyEditSaveBtn;
    @Bind(R.id.supply_edit_cancel_btn)
    Button supplyEditCancelBtn;
    @Bind(R.id.supply_save_view)
    LinearLayout supplySaveView;
    @Bind(R.id.supply_goods_list)
    ListView supplyGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("supplyDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supply_detail);
        ButterKnife.bind(this);

        long supplyId = getIntent().getLongExtra(Constant.SUPPLY_ID, 0);
        DebugLog.i(TAG, "supplyId:" + supplyId);
        if (supplyId > 0) {
            mSupplyId = supplyId;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        supplyDetailBack.setOnClickListener(this);
        supplyMobile.setOnClickListener(this);
        supplyEditBtn.setOnClickListener(this);
        supplyEditSaveBtn.setOnClickListener(this);
        supplyEditCancelBtn.setOnClickListener(this);
        GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this, goodsLogList);
        supplyGoodsList.setAdapter(goodsInOutAdapter);

        if (mSupplyId > 0) {
            initSupply(mSupplyId);
            initInOutDetail(mSupplyId);
        }
    }

    //初始化供应商详情信息
    private void initSupply(Long supplyId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/supply/info?supplyId=" + supplyId
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
                            mSupply = JsonUtil.fromJson(JsonUtil.toJson(result.getData()), SupplyVO.class);
                            if (mSupply.getId() != null) {
                                supplyDetailNameText.setText(mSupply.getSupplyName());
                                supplyDetailMobileText.setText(mSupply.getSupplyMobile());
                            }
                        } else {
                            Toast.makeText(SupplyDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(SupplyDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    //初始化供应商入库信息
    private void initInOutDetail(Long supplyId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                MApplication.IP_SERVICE + "xyy/app/supply/inOutDetail?supplyId=" + supplyId
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
                            Toast.makeText(SupplyDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //刷新供应商入库明细
                        reFreshList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage(), error);
                Toast.makeText(SupplyDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        MApplication.mQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supply_detail_back:
                finish();
                break;
            case R.id.supply_mobile:
                String mobile = mSupply.getSupplyMobile();
                if (!StringUtils.isBlank(mobile)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "当前供应商电话为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.supply_edit_btn:
                if (mSupplyId < 1) {
                    Toast.makeText(this, "当前供应商信息不存在", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (supplyDetailNameEdit.getVisibility() == View.GONE) {
                    supplyDetailNameEdit.setVisibility(View.VISIBLE);
                    supplyDetailNameEdit.setText(mSupply.getSupplyName());
                }
                if (supplyDetailMobileEdit.getVisibility() == View.GONE) {
                    supplyDetailMobileEdit.setVisibility(View.VISIBLE);
                    supplyDetailMobileEdit.setText(mSupply.getSupplyMobile());
                }
                supplyDetailMobileText.setVisibility(View.GONE);
                supplyDetailNameText.setVisibility(View.GONE);
                supplyDetailNameEdit.requestFocus();
                supplyEditView.setVisibility(View.GONE);
                supplySaveView.setVisibility(View.VISIBLE);
                break;
            case R.id.supply_edit_save_btn:
                String supplyName = supplyDetailNameEdit.getText().toString();
                String supplyMobile = supplyDetailMobileEdit.getText().toString();
                if (StringUtils.isBlank(supplyName)) {
                    Toast.makeText(this, "供应商姓名为空", Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isBlank(supplyMobile)) {
                    Toast.makeText(this, "供应商电话为空", Toast.LENGTH_SHORT).show();
                }
                if (mSupplyId < 1) {
                    Toast.makeText(this, "供应商信息为空", Toast.LENGTH_SHORT).show();
                }
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("id",mSupplyId);
                paramMap.put("supplyMobile", supplyMobile);
                paramMap.put("supplyName", supplyName);

                JSONObject jsonObject = new JSONObject(paramMap);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                        MApplication.IP_SERVICE + "xyy/app/supply/edit", jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("添加用户", "response -> " + response.toString());
                                Gson gson = new Gson();
                                Result result = gson.fromJson(response.toString(), Result.class);
                                if(result.isSuccess()){
                                    Toast.makeText(SupplyDetailActivity.this, "更新成功", Toast.LENGTH_LONG).show();
                                    finish();
                                }else {
                                    Toast.makeText(SupplyDetailActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SupplyDetailActivity.this, "网络错误", Toast.LENGTH_LONG).show();
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
                supplyDetailMobileText.setText(mSupply.getSupplyMobile());
                supplyDetailNameText.setText(mSupply.getSupplyName());
                break;
            case R.id.supply_edit_cancel_btn:
                closeEditView();
                break;
        }
    }

    private void closeEditView() {
        supplyDetailNameEdit.clearFocus();
        supplyDetailMobileEdit.clearFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        if (supplyDetailNameEdit.getVisibility() == View.VISIBLE) {
            supplyDetailNameEdit.setVisibility(View.GONE);
            supplyDetailNameEdit.setText(mSupply.getSupplyName());
        }
        if (supplyDetailMobileEdit.getVisibility() == View.VISIBLE) {
            supplyDetailMobileEdit.setVisibility(View.GONE);
            supplyDetailMobileEdit.setText(mSupply.getSupplyMobile());
        }
        supplyDetailMobileText.setVisibility(View.VISIBLE);
        supplyDetailNameText.setVisibility(View.VISIBLE);
        supplyEditView.setVisibility(View.VISIBLE);
        supplySaveView.setVisibility(View.GONE);
    }

    private void reFreshList(){
        GoodsInOutAdapter goodsInOutAdapter = (GoodsInOutAdapter)supplyGoodsList.getAdapter();
        goodsInOutAdapter.setMGoodsLogList(goodsLogList);
    }
}
