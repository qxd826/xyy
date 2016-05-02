package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

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
    @Bind(R.id.add_goods_number_edit)
    EditText addGoodsNumberEdit;
    @Bind(R.id.add_goods_code_edit)
    EditText addGoodsCodeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddSupplyActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_add_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goods_back:
                finish();
                break;
            case R.id.add_goods_save:
                saveGoods();
                finish();
                break;
        }
    }

    private void initView() {
        addGoodsBack.setOnClickListener(this);
        addGoodsSave.setOnClickListener(this);
    }

    private void saveGoods() {
        String goodsName = addGoodsNameEdit.getText().toString();
            if (StringUtils.isEmpty(goodsName)) {
                Toast.makeText(this, "商品名称为空", Toast.LENGTH_LONG).show();
                return;
        }
        int goodsNum = Integer.valueOf(addGoodsNumberEdit.getText().toString());
        if (goodsNum <= 0) {
            Toast.makeText(this, "商品数量不能小于0", Toast.LENGTH_LONG).show();
            return;
        }
        String goodsCode = addGoodsCodeEdit.getText().toString();
        if (StringUtils.isEmpty(goodsCode)) {
            Toast.makeText(this, "商品编号为空", Toast.LENGTH_LONG).show();
            return;
        }
        DBService dbService = DBService.getInstance(this);
        Goods goods = new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsNum(goodsNum);
        goods.setGoodsCode(goodsCode);
        goods.setIsDeleted("N");
        goods.setGmtCreate(new Date().getTime());
        goods.setGmtModified(new Date().getTime());

        if (dbService.insertGoods(goods) > 0) {
            setResult(Constant.ADD_GOODS_SUCCESS);
            Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
        }
        return;
    }
}
