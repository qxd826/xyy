package com.example.xyy.xyyapplication.source.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.user.User;

import java.util.List;

/**
 * Created by admin on 16/4/29.
 */
public class UserListAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mUserList;
    private ViewHolder viewHolder;

    public UserListAdapter(Context context) {
        mContext = context;
        initUserList();
    }

    public UserListAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.user_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.userName.setText(mUserList.get(position).getUserName());
        return convertView;
    }

    private void initUserList() {
        DBService dbService = DBService.getInstance(mContext);
        mUserList = dbService.getUserList();
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView userName;
    }
}
