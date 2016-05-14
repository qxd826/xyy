package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplySpinnerAdapter;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class AddGoodsActivity extends Activity implements View.OnClickListener {
    private Supply currentSupply = null;
    private List<Supply> mSupplyList = new ArrayList<>();

    @Bind(R.id.add_goods_back)
    ImageButton addGoodsBack;
    @Bind(R.id.add_goods_save)
    Button addGoodsSave;
    @Bind(R.id.add_goods_name_edit)
    EditText addGoodsNameEdit;
    @Bind(R.id.add_goods_number_edit)
    EditText addGoodsNumberEdit;
    @Bind(R.id.add_goods_code_edit)
    EditText addGoodsCodeEdit;
    @Bind(R.id.add_goods_supply_spinner)
    Spinner addGoodsSupplySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddGoodsActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_add_activity);
        ButterKnife.bind(this);
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
                if(saveGoods()){
                    finish();
                }
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

        DBService dbService = DBService.getInstance(this);
        mSupplyList = dbService.getSupplyList("1");
        SupplySpinnerAdapter goodsSupplySpinnerAdapter = new SupplySpinnerAdapter(this, mSupplyList);
        addGoodsSupplySpinner.setAdapter(goodsSupplySpinnerAdapter);
        addGoodsSupplySpinner.setGravity(Gravity.CENTER);
        addGoodsSupplySpinner.setSelection(0, true);
        if(mSupplyList.size() != 0){
            currentSupply = mSupplyList.get(0);
        }
        addGoodsSupplySpinner.setPrompt("请选择供应商");
        addGoodsSupplySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSupply = mSupplyList.get(position);
                Toast.makeText(AddGoodsActivity.this, "你选择了:" + mSupplyList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean saveGoods() {
        String goodsName = addGoodsNameEdit.getText().toString();
        if (StringUtils.isEmpty(goodsName)) {
            Toast.makeText(this, "商品名称为空", Toast.LENGTH_LONG).show();
            return false;
        }
        int goodsNum = Integer.valueOf(addGoodsNumberEdit.getText().toString());
        if (goodsNum <= 0) {
            Toast.makeText(this, "商品数量不能小于0", Toast.LENGTH_LONG).show();
            return false;
        }
        String goodsCode = addGoodsCodeEdit.getText().toString();
        if (StringUtils.isEmpty(goodsCode)) {
            Toast.makeText(this, "商品编号为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (currentSupply == null) {
            Toast.makeText(this, "供应商为空", Toast.LENGTH_LONG).show();
            return false;
        }
        DBService dbService = DBService.getInstance(this);
        Goods searchGoods = dbService.getGoodsByCode(goodsCode);
        if(null != searchGoods.getId()){
            Toast.makeText(this, "当前编号商品已存在", Toast.LENGTH_LONG).show();
            return false;
        }

        Goods goods = new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsNum(goodsNum);
        goods.setGoodsCode(goodsCode);
        goods.setIsDeleted("N");
        goods.setGmtCreate(new Date().getTime());
        goods.setGmtModified(new Date().getTime());

        if (dbService.insertGoods(goods, currentSupply) > 0) {
            setResult(Constant.ADD_GOODS_SUCCESS);
            Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
