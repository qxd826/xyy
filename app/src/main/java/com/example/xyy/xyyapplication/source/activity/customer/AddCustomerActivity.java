package com.example.xyy.xyyapplication.source.activity.customer;

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
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class AddCustomerActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.add_customer_back)
    ImageButton addCustomerBack;
    @Bind(R.id.add_customer_save)
    Button addCustomerSave;
    @Bind(R.id.add_customer_name_edit)
    EditText addCustomerNameEdit;
    @Bind(R.id.add_customer_mobile_edit)
    EditText addCustomerMobileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddSupplyActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_add_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_customer_back:
                finish();
                break;
            case R.id.add_customer_save:
                saveCustomer();
                finish();
                break;
        }
    }

    private void initView() {
        addCustomerBack.setOnClickListener(this);
        addCustomerSave.setOnClickListener(this);
    }

    private void saveCustomer() {
        String customerName = addCustomerNameEdit.getText().toString();
        if (StringUtils.isEmpty(customerName)) {
            Toast.makeText(this, "客户名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        String customerMobile = addCustomerMobileEdit.getText().toString();
        if (StringUtils.isEmpty(customerMobile)) {
            Toast.makeText(this, "客户电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        DBService dbService = DBService.getInstance(this);
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setCustomerMobile(customerMobile);
        customer.setIsDeleted("N");
        customer.setGmtCreate(new Date().getTime());
        customer.setGmtModified(new Date().getTime());

        if (dbService.insertCustomer(customer) > 0) {
            setResult(Constant.ADD_CUSTOMER_SUCCESS);
            Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
        }
        return;
    }
}
