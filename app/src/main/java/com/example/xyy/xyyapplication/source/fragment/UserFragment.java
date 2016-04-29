package com.example.xyy.xyyapplication.source.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyy.xyyapplication.R;

/**
 * Created by admin on 16/4/28.
 */
public class UserFragment extends Fragment {

    private static final String TAG = "UserFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "UserFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "UserFragment-----onCreateView");
        View view = inflater.inflate(R.layout.goods_fragment, container, false);
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "UserFragment-----onDestroy");
    }
}
