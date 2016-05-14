package com.example.xyy.xyyapplication.source.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QRMainActivity extends Activity implements OnClickListener {
    private Goods goods = null;
    private final static int SCANNIN_GREQUEST_CODE = 1;

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
    @Bind(R.id.add_goods_num)
    TextView addGoodsNum;
    @Bind(R.id.add_goods_num_text)
    TextView addGoodsNumText;
    @Bind(R.id.add_goods_num_edit)
    EditText addGoodsNumEdit;

    @Bind(R.id.add_goods_btn)
    Button addGoodsBtn;
    @Bind(R.id.add_goods_layout)
    LinearLayout addGoodsLayout;
    @Bind(R.id.edit_goods_btn)
    Button editGoodsBtn;
    @Bind(R.id.edit_goods_save_btn)
    Button editGoodsSaveBtn;
    @Bind(R.id.edit_goods_layout)
    RelativeLayout editGoodsLayout;

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
        goods = null;
    }

    private void initView() {
        addGoodsBtn.setOnClickListener(this);
        editGoodsBtn.setOnClickListener(this);
        editGoodsSaveBtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String goodsCode = bundle.getString("result");
                    initGoodsView(goodsCode);
                    view1.setVisibility(View.VISIBLE);
                    mTextView.setText(goodsCode);
                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    private void initGoodsView(String goodsCode) {
        DBService dbService = DBService.getInstance(this);
        Goods good = dbService.getGoodsByCode(goodsCode);
        if (null != good && (good.getId() != null && good.getId() > 0)) {
            this.goods = good;
            belowView.setVisibility(View.VISIBLE);
            goodsView.setVisibility(View.VISIBLE);

            addGoodsCodeEdit.setVisibility(View.GONE);
            addGoodsNameEdit.setVisibility(View.GONE);
            addGoodsNumEdit.setVisibility(View.GONE);

            addGoodsCodeText.setVisibility(View.VISIBLE);
            addGoodsNameText.setVisibility(View.VISIBLE);
            addGoodsNumText.setVisibility(View.VISIBLE);

            addGoodsCodeText.setText(goods.getGoodsCode());
            addGoodsNameText.setText(goods.getGoodsName());
            addGoodsNumText.setText(goods.getGoodsNum()+"");

            addGoodsLayout.setVisibility(View.GONE);
            editGoodsLayout.setVisibility(View.VISIBLE);
        } else {
            belowView.setVisibility(View.VISIBLE);
            goodsView.setVisibility(View.VISIBLE);

            addGoodsCodeText.setVisibility(View.GONE);
            addGoodsNameText.setVisibility(View.GONE);
            addGoodsNumText.setVisibility(View.GONE);
            addGoodsCodeEdit.setVisibility(View.VISIBLE);
            addGoodsNameEdit.setVisibility(View.VISIBLE);
            addGoodsNumEdit.setVisibility(View.VISIBLE);

            addGoodsLayout.setVisibility(View.VISIBLE);
            editGoodsLayout.setVisibility(View.GONE);

            addGoodsCodeEdit.setText(goodsCode);
            addGoodsNameEdit.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_goods_btn:
                addGoods();
                finish();
                break;
            case R.id.edit_goods_btn:
                if(null == goods){
                    Toast.makeText(this, "当前商品为空", Toast.LENGTH_LONG).show();
                    break;
                }
                addGoodsCodeText.setVisibility(View.GONE);
                addGoodsNameText.setVisibility(View.GONE);
                addGoodsNumText.setVisibility(View.GONE);
                addGoodsCodeEdit.setVisibility(View.VISIBLE);
                addGoodsNameEdit.setVisibility(View.VISIBLE);
                addGoodsNumEdit.setVisibility(View.VISIBLE);
                addGoodsCodeEdit.setText(goods.getGoodsCode());
                addGoodsNameEdit.setText(goods.getGoodsName());
                addGoodsNumEdit.setText(goods.getGoodsNum()+"");
                addGoodsCodeEdit.requestFocus();
                break;
            case R.id.edit_goods_save_btn:
                editGoods();
                finish();
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
        int goodsNum = Integer.valueOf(addGoodsNumEdit.getText().toString());
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

        if (dbService.insertGoods(goods,null) > 0) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
        }
        return;
    }

    //修改商品
    private void editGoods() {
        if (goods == null) {
            Toast.makeText(this, "当前商品为空", Toast.LENGTH_LONG).show();
            return;
        }
        String goodsName = addGoodsNameEdit.getText().toString();
        if (StringUtils.isEmpty(goodsName)) {
            Toast.makeText(this, "商品名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        int goodsNum = Integer.valueOf(addGoodsNumEdit.getText().toString());
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
        goods.setGoodsName(goodsName);
        goods.setGoodsNum(goodsNum);
        goods.setGoodsCode(goodsCode);
        goods.setGmtModified(new Date().getTime());

        if (dbService.upDateGoods(goods) > 0) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
        }
        return;
    }
}
