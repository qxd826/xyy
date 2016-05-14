package com.example.xyy.xyyapplication.source.activity.customer;

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
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by QXD on 2016/5/14.
 */
public class CustomerDetailActivity extends Activity implements View.OnClickListener {
    private final String TAG = "CustomerDetailActivity";
    private int mCustomerId = 0;
    private Customer mCustomer = null;

    @Bind(R.id.customer_detail_back)
    ImageButton customerDetailBack;
    @Bind(R.id.customer_mobile)
    ImageView customerMobile;
    @Bind(R.id.customer_detail_name_text)
    TextView customerDetailNameText;
    @Bind(R.id.customer_detail_name_edit)
    EditText customerDetailNameEdit;
    @Bind(R.id.customer_detail_mobile_text)
    TextView customerDetailMobileText;
    @Bind(R.id.customer_detail_mobile_edit)
    EditText customerDetailMobileEdit;
    @Bind(R.id.customer_edit_btn)
    Button customerEditBtn;
    @Bind(R.id.customer_edit_view)
    LinearLayout customerEditView;
    @Bind(R.id.customer_edit_save_btn)
    Button customerEditSaveBtn;
    @Bind(R.id.customer_edit_cancel_btn)
    Button customerEditCancelBtn;
    @Bind(R.id.customer_save_view)
    LinearLayout customerSaveView;
    @Bind(R.id.customer_goods_list)
    ListView customerGoodsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("CustomerDetailActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail);
        ButterKnife.bind(this);

        int customerId = getIntent().getIntExtra(Constant.CUSTOMER_ID, 0);
        DebugLog.i(TAG, "customerId:" + customerId);
        if (customerId > 0) {
            mCustomerId = customerId;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        customerDetailBack.setOnClickListener(this);
        customerMobile.setOnClickListener(this);
        customerEditBtn.setOnClickListener(this);
        customerEditSaveBtn.setOnClickListener(this);
        customerEditCancelBtn.setOnClickListener(this);

        if (mCustomerId > 0) {
            DBService dbService = DBService.getInstance(this);
            mCustomer = dbService.getCustomerById(mCustomerId);
            customerDetailNameText.setText(mCustomer.getCustomerName());
            customerDetailMobileText.setText(mCustomer.getCustomerMobile());

            List<GoodsLog> goodsLogList = dbService.getGoodsLogListByCustomerId(mCustomerId);
            GoodsInOutAdapter goodsInOutAdapter = new GoodsInOutAdapter(this,goodsLogList);
            customerGoodsList.setAdapter(goodsInOutAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_detail_back:
                finish();
                break;
            case R.id.customer_mobile:
                String mobile = mCustomer.getCustomerMobile();
                if(!StringUtils.isBlank(mobile)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "当前客户电话为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.customer_edit_btn:
                if (mCustomerId < 1) {
                    Toast.makeText(this, "当前客户信息不存在", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (customerDetailNameEdit.getVisibility() == View.GONE) {
                    customerDetailNameEdit.setVisibility(View.VISIBLE);
                    customerDetailNameEdit.setText(mCustomer.getCustomerName());
                }
                if (customerDetailMobileEdit.getVisibility() == View.GONE) {
                    customerDetailMobileEdit.setVisibility(View.VISIBLE);
                    customerDetailMobileEdit.setText(mCustomer.getCustomerMobile());
                }
                customerDetailMobileText.setVisibility(View.GONE);
                customerDetailNameText.setVisibility(View.GONE);
                customerDetailNameEdit.requestFocus();
                customerEditView.setVisibility(View.GONE);
                customerSaveView.setVisibility(View.VISIBLE);
                break;
            case R.id.customer_edit_save_btn:
                String customerName = customerDetailNameEdit.getText().toString();
                String customerMobile = customerDetailMobileEdit.getText().toString();
                if (StringUtils.isBlank(customerName)) {
                    Toast.makeText(this, "客户姓名为空", Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isBlank(customerMobile)) {
                    Toast.makeText(this, "客户电话为空", Toast.LENGTH_SHORT).show();
                }
                if (mCustomerId < 1) {
                    Toast.makeText(this, "客户信息为空", Toast.LENGTH_SHORT).show();
                }
                DBService dbService = DBService.getInstance(this);
                if (dbService.updateCustomer(mCustomerId, customerName, customerMobile) > 0) {
                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                    mCustomer.setCustomerName(customerName);
                    mCustomer.setCustomerMobile(customerMobile);
                }else{
                    Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                }
                closeEditView();
                customerDetailMobileText.setText(mCustomer.getCustomerMobile());
                customerDetailNameText.setText(mCustomer.getCustomerName());
                break;
            case R.id.customer_edit_cancel_btn:
                closeEditView();
                break;
        }
    }
    private void closeEditView() {
        customerDetailNameEdit.clearFocus();
        customerDetailMobileEdit.clearFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        if (customerDetailNameEdit.getVisibility() == View.VISIBLE) {
            customerDetailNameEdit.setVisibility(View.GONE);
            customerDetailNameEdit.setText(mCustomer.getCustomerName());
        }
        if (customerDetailMobileEdit.getVisibility() == View.VISIBLE) {
            customerDetailMobileEdit.setVisibility(View.GONE);
            customerDetailMobileEdit.setText(mCustomer.getCustomerMobile());
        }
        customerDetailMobileText.setVisibility(View.VISIBLE);
        customerDetailNameText.setVisibility(View.VISIBLE);
        customerEditView.setVisibility(View.VISIBLE);
        customerSaveView.setVisibility(View.GONE);
    }
}
