package com.example.xyy.xyyapplication.source.adapter.supply;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;
import com.example.xyy.xyyapplication.source.pojo.supply.SupplyVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QXD on 2016/5/14.
 */
public class SupplySpinnerAdapter extends BaseAdapter {
    private List<SupplyVO> mSupplyList = new ArrayList<SupplyVO>();
    private Context mContext;

    public SupplySpinnerAdapter(Context context) {
        mContext = context;
    }

    public SupplySpinnerAdapter(Context context, List<SupplyVO> supplyList) {
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
            convertView = layoutInflater.inflate(R.layout.spinner_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.supplyName = (TextView) convertView.findViewById(R.id.spinner_item_text);
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
    public void setMSupplyList(List<SupplyVO> supplyList){
        this.mSupplyList = supplyList;
        super.notifyDataSetChanged();
    }

    public List<SupplyVO> getSupplyList(){
        return mSupplyList;
    }
}
