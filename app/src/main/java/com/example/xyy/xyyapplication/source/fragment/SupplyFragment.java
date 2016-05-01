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
import com.example.xyy.xyyapplication.source.activity.supply.AddSupplyActivity;
import com.example.xyy.xyyapplication.source.adapter.supply.SupplyListAdapter;
import com.example.xyy.xyyapplication.source.adapter.user.UserListAdapter;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DebugLog;
import com.example.xyy.xyyapplication.source.constant.Constant;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SupplyFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "SupplyFragment-----onCreateView");
        View view = inflater.inflate(R.layout.supply_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SupplyFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView() {
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
        initSupplyList();
    }

    private void initSupplyList() {
        DBService dbService = DBService.getInstance(getContext());
        List<Supply> mSupplyList = dbService.getSupplyList("1");
        SupplyListAdapter supplyListAdapter = new SupplyListAdapter(getContext(), mSupplyList, MApplication.isAdmin);
        supplyList.setAdapter(supplyListAdapter);
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
