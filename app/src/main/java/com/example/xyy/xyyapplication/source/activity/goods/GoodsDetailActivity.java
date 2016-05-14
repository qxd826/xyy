package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerSpinnerAdapter;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplySpinnerAdapter;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class GoodsDetailActivity extends Activity implements View.OnClickListener {
    private String TAG = "GoodsDetailActivity";
    private String mGoodsCode = null;
    private Goods mGoods = null;
    private List<Supply> mSupplyList = new ArrayList<>();
    private List<Customer> mCustomerList = new ArrayList<>();
    private Supply currentSupply = null;
    private Customer currentCustomer = null;

    @Bind(R.id.goods_in_edit)
    EditText goodsInEdit;
    @Bind(R.id.in_goods_supply_spinner)
    Spinner inGoodsSupplySpinner;
    @Bind(R.id.goods_out_edit)
    EditText goodsOutEdit;
    @Bind(R.id.out_goods_customer_spinner)
    Spinner outGoodsCustomerSpinner;
    @Bind(R.id.in_goods_btn)
    Button inGoodsBtn;
    @Bind(R.id.out_goods_btn)
    Button outGoodsBtn;
    @Bind(R.id.in_out_detail_btn)
    Button inOutDetailBtn;
    @Bind(R.id.goods_detail_back)
    ImageButton goodsDetailBack;
    @Bind(R.id.goods_detail_name_text)
    TextView goodsDetailNameText;
    @Bind(R.id.goods_detail_number_text)
    TextView goodsDetailNumberText;
    @Bind(R.id.goods_detail_code_text)
    TextView goodsDetailCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("GoodsDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        ButterKnife.bind(this);

        String goodsCode = getIntent().getStringExtra(Constant.GOODS_CODE);
        DebugLog.i(TAG, "goodsCode:" + goodsCode);
        if (!StringUtils.isBlank(goodsCode)) {
            mGoodsCode = goodsCode;
        }
        initView();
    }

    @Override
    protected void onResume() {
        if (!StringUtils.isBlank(mGoodsCode)) {
            initGoods(mGoodsCode);
        }
        initSupplyAndCustomerSpinner();
        super.onResume();
    }
    private void initView(){
        inGoodsBtn.setOnClickListener(this);
        outGoodsBtn.setOnClickListener(this);
        inOutDetailBtn.setOnClickListener(this);
        goodsDetailBack.setOnClickListener(this);
    }

    //初始化商品详情
    private void initGoods(String goodsCode) {
        DBService dbService = DBService.getInstance(this);
        mGoods = dbService.getGoodsByCode(goodsCode);
        if (mGoods.getId() != null) {
            goodsDetailNameText.setText(mGoods.getGoodsName());
            goodsDetailNumberText.setText(mGoods.getGoodsNum() + "");
            goodsDetailCodeText.setText(mGoods.getGoodsCode());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in_goods_btn:
                inGoods();
                break;
            case R.id.out_goods_btn:
                outGoods();
                break;
            case R.id.in_out_detail_btn:
                break;
            case R.id.goods_detail_back:
                finish();
                break;
        }
    }

    //商品入库
    private void inGoods() {
        String textNum = goodsInEdit.getText().toString();
        if(StringUtils.isBlank(textNum)){
            Toast.makeText(GoodsDetailActivity.this, "入库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //入库数量
        Integer num = Integer.valueOf(goodsInEdit.getText().toString());
        if (num < 1) {
            Toast.makeText(GoodsDetailActivity.this, "入库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //入库供应商
        if (null == currentSupply || currentSupply.getId() < 1) {
            Toast.makeText(GoodsDetailActivity.this, "请选择供应商", Toast.LENGTH_SHORT).show();
            return;
        }
        //商品信息校验
        if (StringUtils.isBlank(mGoodsCode)) {
            Toast.makeText(GoodsDetailActivity.this, "当前商品信息为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DBService dbService = DBService.getInstance(this);
        if (dbService.inGoods(mGoodsCode, num, currentSupply) > 0) {
            Toast.makeText(GoodsDetailActivity.this, "入库成功", Toast.LENGTH_SHORT).show();
            initGoods(mGoodsCode);
            goodsInEdit.setText("");
        }else{
            Toast.makeText(GoodsDetailActivity.this, "入库失败", Toast.LENGTH_SHORT).show();
        }
    }

    //商品出库
    private void outGoods() {
        String textNum = goodsOutEdit.getText().toString();
        if(StringUtils.isBlank(textNum)){
            Toast.makeText(GoodsDetailActivity.this, "出库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //出库数量
        Integer num = Integer.valueOf(goodsOutEdit.getText().toString());
        if (num < 1) {
            Toast.makeText(GoodsDetailActivity.this, "出库数量必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        //出库客户
        if (null == currentCustomer || currentCustomer.getId() < 1) {
            Toast.makeText(GoodsDetailActivity.this, "请选择客户", Toast.LENGTH_SHORT).show();
            return;
        }
        //商品信息校验
        if (StringUtils.isBlank(mGoodsCode)) {
            Toast.makeText(GoodsDetailActivity.this, "当前商品信息为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DBService dbService = DBService.getInstance(this);
        if (dbService.outGoods(mGoodsCode, num, currentCustomer) > 0) {
            Toast.makeText(GoodsDetailActivity.this, "出库成功", Toast.LENGTH_SHORT).show();
            initGoods(mGoodsCode);
            goodsOutEdit.setText("");
        }else{
            Toast.makeText(GoodsDetailActivity.this, "出库失败", Toast.LENGTH_SHORT).show();
        }
    }


    //初始化 供应商和客户列表
    private void initSupplyAndCustomerSpinner() {
        DBService dbService = DBService.getInstance(this);
        mSupplyList = dbService.getSupplyList("1");
        SupplySpinnerAdapter supplySpinnerAdapter = new SupplySpinnerAdapter(this, mSupplyList);
        inGoodsSupplySpinner.setAdapter(supplySpinnerAdapter);
        inGoodsSupplySpinner.setGravity(Gravity.CENTER);
        inGoodsSupplySpinner.setSelection(0, true);
        if (mSupplyList.size() != 0) {
            currentSupply = mSupplyList.get(0);
        }
        inGoodsSupplySpinner.setPrompt("请选择供应商");
        inGoodsSupplySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSupply = mSupplyList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCustomerList = dbService.getCustomerList("1");
        CustomerSpinnerAdapter customerSpinnerAdapter = new CustomerSpinnerAdapter(this, mCustomerList);
        outGoodsCustomerSpinner.setAdapter(customerSpinnerAdapter);
        outGoodsCustomerSpinner.setGravity(Gravity.CENTER);
        outGoodsCustomerSpinner.setSelection(0, true);
        if (mCustomerList.size() != 0) {
            currentCustomer = mCustomerList.get(0);
        }
        outGoodsCustomerSpinner.setPrompt("请选择客户");
        outGoodsCustomerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCustomer = mCustomerList.get(position);
                Toast.makeText(GoodsDetailActivity.this, "你选择了:" + mCustomerList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
