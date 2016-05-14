package com.example.xyy.xyyapplication.source.adapter.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xyy.xyyapplication.R;
import com.example.xyy.xyyapplication.source.common.DateUtil;
import com.example.xyy.xyyapplication.source.db.DBService;
import com.example.xyy.xyyapplication.source.pojo.goods.Goods;
import com.example.xyy.xyyapplication.source.pojo.goods.GoodsLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QXD on 2016/5/14.
 */
public class GoodsInOutAdapter extends BaseAdapter {
    private List<GoodsLog> mGoodsLogList = new ArrayList<>();
    private Context mContext;

    public GoodsInOutAdapter(Context context) {
        mContext = context;
    }

    public GoodsInOutAdapter(Context context, List<GoodsLog> goodsLogList) {
        mContext = context;
        mGoodsLogList = goodsLogList;
    }

    @Override
    public int getCount() {
        return mGoodsLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsLogList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.goods_in_out_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.inOutItemType = (TextView) convertView.findViewById(R.id.in_out_item_type);
            mViewHolder.inOutItemName = (TextView) convertView.findViewById(R.id.in_out_item_name);
            mViewHolder.inOutItemNum = (TextView) convertView.findViewById(R.id.in_out_item_num);
            mViewHolder.inOutItemTime = (TextView) convertView.findViewById(R.id.in_out_item_time);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if(mGoodsLogList.get(position).getActionType().equals("1")){
            mViewHolder.inOutItemType.setText("出库");
            mViewHolder.inOutItemType.setTextColor(mContext.getResources().getColor(R.color.red_color));
            mViewHolder.inOutItemName.setText(mGoodsLogList.get(position).getCustomerName());
            mViewHolder.inOutItemNum.setText("-"+mGoodsLogList.get(position).getNum());
        }else{
            mViewHolder.inOutItemType.setText("入库");
            mViewHolder.inOutItemType.setTextColor(mContext.getResources().getColor(R.color.green_color));
            mViewHolder.inOutItemName.setText(mGoodsLogList.get(position).getSupplyName());
            mViewHolder.inOutItemNum.setText("+"+mGoodsLogList.get(position).getNum());
        }
        Date createDate = new Date();
        createDate.setTime(mGoodsLogList.get(position).getGmtCreate());
        mViewHolder.inOutItemTime.setText(DateUtil.convertDateToYMDHM(createDate));
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView inOutItemType;
        public TextView inOutItemName;
        public TextView inOutItemNum;
        public TextView inOutItemTime;
    }

    public void setMGoodsLogList(List<GoodsLog> goodsLogList){
        this.mGoodsLogList = goodsLogList;
        super.notifyDataSetChanged();
    }
}
