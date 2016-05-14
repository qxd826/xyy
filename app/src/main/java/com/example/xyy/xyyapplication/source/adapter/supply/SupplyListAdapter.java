package com.example.xyy.xyyapplication.source.adapter.supply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.DateUtil;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 16/5/1.
 */
public class SupplyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Supply> mSupplyList = new ArrayList<Supply>();
    private Boolean mIsAdmin;

    public SupplyListAdapter(Context context) {
        mContext = context;
    }

    public SupplyListAdapter(Context context, List<Supply> userList) {
        mContext = context;
        mSupplyList = userList;
    }

    public SupplyListAdapter(Context context, List<Supply> userList, Boolean isAdmin) {
        mContext = context;
        mSupplyList = userList;
        mIsAdmin = isAdmin;
    }

    @Override
    public int getCount() {
        return mSupplyList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSupplyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.supply_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.supplyName = (TextView) convertView.findViewById(R.id.supply_name);
            mViewHolder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            mViewHolder.supplyMobile = (TextView) convertView.findViewById(R.id.supply_mobile);
            mViewHolder.delButton = (ImageView) convertView.findViewById(R.id.del_button);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.supplyName.setText(mSupplyList.get(position).getSupplyName());
        mViewHolder.supplyMobile.setText(mSupplyList.get(position).getSupplyMobile());
        Date createDate = new Date();
        createDate.setTime(mSupplyList.get(position).getGmtCreate());
        mViewHolder.createTime.setText(DateUtil.convertDateToYMDHM(createDate));

        mViewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBService dbService = DBService.getInstance(mContext);
                if(dbService.delSupplyById(mSupplyList.get(position).getId()) > 0){
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                    mSupplyList.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView supplyName;
        public TextView createTime;
        public TextView supplyMobile;
        public ImageView delButton;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setMSupplyList(List<Supply> supplyList){
        this.mSupplyList = supplyList;
        notifyDataSetChanged();
    }
}
