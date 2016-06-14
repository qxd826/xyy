package com.example.xyy.xyyapplication.source.adapter.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.customer.CustomerVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QXD on 2016/5/14.
 */
public class CustomerSpinnerAdapter extends BaseAdapter {
    private List<CustomerVO> mCustomerList = new ArrayList<CustomerVO>();
    private Context mContext;

    public CustomerSpinnerAdapter(Context context) {
        mContext = context;
    }

    public CustomerSpinnerAdapter(Context context, List<CustomerVO> customerList) {
        mContext = context;
        mCustomerList = customerList;
    }
    @Override
    public int getCount() {
        return mCustomerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCustomerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.spinner_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.customerName = (TextView) convertView.findViewById(R.id.spinner_item_text);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.customerName.setText(mCustomerList.get(position).getCustomerName());
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView customerName;
    }
    public void setMCustomerList(List<CustomerVO> customerList){
        this.mCustomerList = customerList;
        super.notifyDataSetChanged();
    }

    public List<CustomerVO> getCustomerList(){
        return mCustomerList;
    }
}
