package com.example.xyy.xyyapplication.source.activity.supply;

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
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/5/1.
 */
public class AddSupplyActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.add_supply_back)
    ImageButton addSupplyBack;
    @Bind(R.id.add_supply_save)
    Button addSupplySave;
    @Bind(R.id.add_supply_name_edit)
    EditText addSupplyNameEdit;
    @Bind(R.id.add_supply_mobile_edit)
    EditText addSupplyMobileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DebugLog.i("AddSupplyActivity onCreate ......");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supply_add_activity);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_supply_back:
                finish();
                break;
            case R.id.add_supply_save:
                saveSupply();
                finish();
                break;
        }
    }

    private void initView() {
        addSupplyBack.setOnClickListener(this);
        addSupplySave.setOnClickListener(this);
    }

    private void saveSupply() {
        String supplyName = addSupplyNameEdit.getText().toString();
        if (StringUtils.isEmpty(supplyName)) {
            Toast.makeText(this, "供应商名称为空", Toast.LENGTH_LONG).show();
            return;
        }
        String supplyMobile = addSupplyMobileEdit.getText().toString();
        if (StringUtils.isEmpty(supplyMobile)) {
            Toast.makeText(this, "供应商电话为空", Toast.LENGTH_LONG).show();
            return;
        }
        DBService dbService = DBService.getInstance(this);
        Supply supply = new Supply();
        supply.setSupplyName(supplyName);
        supply.setSupplyMobile(supplyMobile);
        supply.setIsDeleted("N");
        supply.setGmtCreate(new Date().getTime());
        supply.setGmtModified(new Date().getTime());

        if (dbService.insertSupply(supply) > 0) {
            setResult(Constant.ADD_SUPPLY_SUCCESS);
            Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
        }
        return;
    }
}
