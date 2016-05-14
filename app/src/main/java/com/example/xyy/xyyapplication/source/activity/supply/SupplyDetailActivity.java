package com.example.xyy.xyyapplication.source.activity.supply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsInOutAdapter;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class SupplyDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "SupplyDetailActivity";
    private int mSupplyId = 0;
    private Supply mSupply = null;

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

        int supplyId = getIntent().getIntExtra(Constant.SUPPLY_ID, 0);
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

        if (mSupplyId > 0) {
            DBService dbService = DBService.getInstance(this);
            mSupply = dbService.getSupplyById(mSupplyId);
            supplyDetailNameText.setText(mSupply.getSupplyName());
            supplyDetailMobileText.setText(mSupply.getSupplyMobile());

            List<GoodsLog> goodsLogList = dbService.getGoodsLogListBySupplyId(mSupplyId);
            GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this,goodsLogList);
            supplyGoodsList.setAdapter(goodsInOutAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.supply_detail_back:
                finish();
                break;
            case R.id.supply_mobile:
                String mobile = mSupply.getSupplyMobile();
                if(!StringUtils.isBlank(mobile)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                }else{
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
                DBService dbService = DBService.getInstance(this);
                if (dbService.updateSupply(mSupplyId, supplyName, supplyMobile) > 0) {
                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                    mSupply.setSupplyName(supplyName);
                    mSupply.setSupplyMobile(supplyMobile);
                }else{
                    Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                }
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
}
