package com.example.xyy.xyyapplication.source.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.customer.AddCustomerActivity;
import com.example.xyy.xyyapplication.source.activity.supply.AddSupplyActivity;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerListAdapter;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplyListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/28.
 */
public class CustomerFragment extends Fragment {

    private static final String TAG = "CustomerFragment";
    @Bind(R.id.customer_title)
    TextView customerTitle;
    @Bind(R.id.add_customer)
    Button addCustomer;
    @Bind(R.id.customer_list)
    ListView customerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "CustomerFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "CustomerFragment-----onCreateView");
        View view = inflater.inflate(R.layout.customer_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.i(TAG, "CustomerFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "CustomerFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddCustomerActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_CUSTOMER_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        initCustomerList();
    }

    private void initCustomerList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Customer> mCustomerList = dbService.getCustomerList("1");
        CustomerListAdapter customerListAdapter = new CustomerListAdapter(getContext(), mCustomerList, MApplication.isAdmin);
        customerList.setAdapter(customerListAdapter);
    }

    public void reFreshList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Customer> mCustomerList = dbService.getCustomerList("1");
        CustomerListAdapter mAdapter = (CustomerListAdapter) customerList.getAdapter();
        mAdapter.setMCustomerList(mCustomerList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_CUSTOMER_CODE:
                reFreshList();
                break;
        }
    }
}
