package com.example.xyy.xyyapplication.source.fragment;

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
import com.example.xyy.xyyapplication.source.adapter.user.UserListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by admin on 16/4/28.
 */
public class UserFragment extends Fragment {
    private static final String TAG = "UserFragment";

    @Bind(R.id.user_title)
    TextView userTitle;
/*    @Bind(R.id.user_list)
    ListView userList;
    @Bind(R.id.user_button)
    Button userButton;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "UserFragment-----onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "UserFragment-----onCreateView");
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        ButterKnife.bind(this, view);
        //initView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "UserFragment-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

/*    private void initView() {
        //设置适配器
        UserListAdapter userListAdapter = new UserListAdapter(getContext());
        userList.setAdapter(userListAdapter);
    }*/
}
