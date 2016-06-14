package com.example.xyy.xyyapplication.source.adapter.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.application.MApplication;
import com.example.xyy.xyyapplication.source.common.DateUtil;
import com.example.xyy.xyyapplication.source.common.JsonUtil;
import com.example.xyy.xyyapplication.source.common.Result;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;
import com.example.xyy.xyyapplication.source.pojo.user.UserVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 16/4/29.
 */
public class UserListAdapter extends BaseAdapter {
    private final String TAG = "UserListAdapter";
    private Context mContext;
    private List<UserVO> mUserList = new ArrayList<UserVO>();
    private Boolean mIsAdmin;
    private RequestQueue mQueue;

    public UserListAdapter(Context context) {
        mContext = context;
    }

    public UserListAdapter(Context context, List<UserVO> userList) {
        mContext = context;
        mUserList = userList;
    }

    public UserListAdapter(Context context, List<UserVO> userList, Boolean isAdmin,RequestQueue queue) {
        mContext = context;
        mUserList = userList;
        mIsAdmin = isAdmin;
        mQueue = queue;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //convertView是滚出屏幕的 view
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.user_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            mViewHolder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            mViewHolder.mobile = (TextView) convertView.findViewById(R.id.mobile);
            mViewHolder.delButton = (Button) convertView.findViewById(R.id.del_button);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.userName.setText(mUserList.get(position).getUserName());
        mViewHolder.mobile.setText(mUserList.get(position).getMobile());
        Date createDate = new Date();
        createDate.setTime(mUserList.get(position).getGmtCreate());
        mViewHolder.createTime.setText(DateUtil.convertDateToYMDHM(createDate));

        if (!mIsAdmin) {
            mViewHolder.delButton.setVisibility(View.GONE);
        } else {
            if(StringUtils.equalsIgnoreCase(mUserList.get(position).getIsAdmin(), "1")){
                mViewHolder.delButton.setVisibility(View.GONE);
            }else{
                mViewHolder.delButton.setVisibility(View.VISIBLE);
            }
        }

        mViewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        MApplication.IP_SERVICE + "xyy/app/user/del?userId=" + mUserList.get(position).getId()
                        , null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, response.toString());
                                Gson gson = new Gson();
                                Log.i(TAG, "获取结果");
                                Result result = gson.fromJson(response.toString(), Result.class);
                                Log.i(TAG, "获取结果:" + result.toString());
                                if (result.isSuccess()) {
                                    mUserList.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, result.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.getMessage(), error);
                        Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(jsonObjectRequest);
            }
        });
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView userName;
        public TextView createTime;
        public TextView mobile;
        public Button delButton;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setMUserList(List<UserVO> userList){
        this.mUserList = userList;
        notifyDataSetChanged();
    }
}
