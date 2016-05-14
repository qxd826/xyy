package com.example.xyy.xyyapplication.source.adapter.supply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QXD on 2016/5/14.
 */
public class GoodsSupplySpinnerAdapter extends BaseAdapter {
    private List<Supply> mSupplyList = new ArrayList<Supply>();
    private Context mContext;

    public GoodsSupplySpinnerAdapter(Context context) {
        mContext = context;
    }

    public GoodsSupplySpinnerAdapter(Context context, List<Supply> supplyList) {
        mContext = context;
        mSupplyList = supplyList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.goods_supply_spinner_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.supplyName = (TextView) convertView.findViewById(R.id.goods_supply_spinner_item_text);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.supplyName.setText(mSupplyList.get(position).getSupplyName());
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView supplyName;
    }
    public void setMSupplyList(List<Supply> supplyList){
        this.mSupplyList = supplyList;
        super.notifyDataSetChanged();
    }

    public List<Supply> getSupplyList(){
        return mSupplyList;
    }
}
