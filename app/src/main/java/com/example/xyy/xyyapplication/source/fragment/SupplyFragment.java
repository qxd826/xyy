package com.example.xyy.xyyapplication.source.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.xyy.xyyapplication.R;

import java.util.Map;
import java.util.Objects;

/**
 * Created by admin on 16/4/27.
 */
public class SupplyFragment extends Fragment{
    private static final String TAG = "SupplyFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SupplyFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "SupplyFragment-----onCreateView");
        View view = inflater.inflate(R.layout.supply_fragment, container, false);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SupplyFragment-----onDestroy");
    }

}
