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
import com.example.xyy.xyyapplication.source.activity.supply.AddSupplyActivity;
import com.example.xyy.xyyapplication.source.adapter.customer.CustomerListAdapter;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplyListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/4/27.
 */
public class SupplyFragment extends Fragment {
    private static final String TAG = "SupplyFragment";
    @Bind(R.id.supply_title)
    TextView supplyTitle;
    @Bind(R.id.add_supply)
    Button addSupply;
    @Bind(R.id.supply_list)
    ListView supplyList;
    @Bind(R.id.supply_search_clear_btn)
    ImageButton supplySearchClearBtn;
    @Bind(R.id.supply_search_edit)
    EditText supplySearchEdit;
    @Bind(R.id.supply_search_btn)
    ImageButton supplySearchBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "SupplyFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "SupplyFragment-----onCreateView");
        View view = inflater.inflate(R.layout.supply_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "SupplyFragment-----onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "SupplyFragment-----onResume");
        super.onResume();
        supplySearchEdit.clearFocus();
        initSupplyList();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "SupplyFragment-----onPause");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(supplySearchEdit.getWindowToken(), 0);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "SupplyFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
        supplySearchEdit.clearFocus();
        addSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSupplyActivity.class);
                try {
                    startActivityForResult(intent, Constant.ADD_SUPPLY_CODE);
                } catch (Exception e) {
                    DebugLog.e("e" + e.toString());
                }
            }
        });
        supplySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplySearchEdit.clearFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                DBService dbService = DBService.getInstance(getContext());
                List<Supply> supplyListData = dbService.searchSupply(supplySearchEdit.getText().toString());
                if (supplyListData == null) {
                    supplyListData = new ArrayList<Supply>();
                }
                SupplyListAdapter mAdapter = (SupplyListAdapter) supplyList.getAdapter();
                mAdapter.setMSupplyList(supplyListData);
            }
        });
        supplySearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplySearchEdit.setText("");
                reFreshList();
            }
        });
        supplySearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (supplySearchClearBtn.getVisibility() == View.GONE) {
                        supplySearchClearBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (supplySearchClearBtn.getVisibility() == View.VISIBLE) {
                        supplySearchClearBtn.setVisibility(View.GONE);
                    }
                }
            }
        });
        supplySearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        supplyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Supply supply = (Supply) supplyList.getAdapter().getItem(position);
/*                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constant.GOODS_CODE, goods.getGoodsCode());
                startActivity(intent);*/
            }
        });
    }

    private void initSupplyList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Supply> mSupplyList = dbService.getSupplyList("1");
        SupplyListAdapter supplyListAdapter = new SupplyListAdapter(getContext(), mSupplyList, MApplication.isAdmin);
        try {
            supplyList.setAdapter(supplyListAdapter);
        } catch (Exception e) {
            DebugLog.e(e.toString());
        }
    }

    public void reFreshList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Supply> mSupplyList = dbService.getSupplyList("1");
        SupplyListAdapter mAdapter = (SupplyListAdapter) supplyList.getAdapter();
        mAdapter.setMSupplyList(mSupplyList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.ADD_SUPPLY_CODE:
                reFreshList();
                break;
        }
    }
}
