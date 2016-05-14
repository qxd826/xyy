package com.example.xyy.xyyapplication.source.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.activity.customer.AddCustomerActivity;
import com.example.xyy.xyyapplication.source.activity.customer.CustomerDetailActivity;
import com.example.xyy.xyyapplication.source.activity.goods.GoodsDetailActivity;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerListAdapter;
import com.example.xyy.xyyapplication.source.adapter.goods.GoodsListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;

import java.util.ArrayList;
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
    @Bind(R.id.customer_search_clear_btn)
    ImageButton customerSearchClearBtn;
    @Bind(R.id.customer_search_edit)
    EditText customerSearchEdit;
    @Bind(R.id.customer_search_btn)
    ImageButton customerSearchBtn;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "CustomerFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "CustomerFragment-----onResume");
        super.onResume();
        customerSearchEdit.clearFocus();
        initCustomerList();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "CustomerFragment-----onPause");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(customerSearchEdit.getWindowToken(), 0);
        super.onPause();
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
        customerSearchEdit.clearFocus();
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
        customerSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSearchEdit.clearFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                DBService dbService = DBService.getInstance(getContext());
                List<Customer> customerListData = dbService.searchCustomer(customerSearchEdit.getText().toString());
                if (customerListData == null) {
                    customerListData = new ArrayList<Customer>();
                }
                CustomerListAdapter mAdapter = (CustomerListAdapter) customerList.getAdapter();
                mAdapter.setMCustomerList(customerListData);
            }
        });
        customerSearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerSearchEdit.setText("");
                reFreshList();
            }
        });
        customerSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (customerSearchClearBtn.getVisibility() == View.GONE) {
                        customerSearchClearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (customerSearchClearBtn.getVisibility() == View.VISIBLE) {
                        customerSearchClearBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        customerSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = (Customer) customerList.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
                intent.putExtra(Constant.CUSTOMER_ID, customer.getId());
                startActivity(intent);
            }
        });
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
