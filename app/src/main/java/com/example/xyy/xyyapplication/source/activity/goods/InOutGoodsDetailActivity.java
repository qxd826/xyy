package com.example.xyy.xyyapplication.source.activity.goods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsInOutAdapter;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class InOutGoodsDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "InOutGoodsDetailActivity";
    @Bind(R.id.in_out_back)
    ImageButton inOutBack;
    @Bind(R.id.goods_in_out_list)
    ListView goodsInOutList;

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
            DBService dbService = DBService.getInstance(this);
            List<GoodsLog> goodsLogList = dbService.getGoodsLogList(goodsCode);
            GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this, goodsLogList);
            goodsInOutList.setAdapter(goodsInOutAdapter);
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
}
