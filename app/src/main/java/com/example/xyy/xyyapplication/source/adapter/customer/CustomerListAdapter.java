package com.example.xyy.xyyapplication.source.adapter.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.xyy.xyyapplication.source.pojo.customer.Customer;
import com.example.xyy.xyyapplication.source.pojo.supply.Supply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 16/4/29.
 */
public class CustomerListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Customer> mCustomerList = new ArrayList<Customer>();
    private Boolean mIsAdmin;

    public CustomerListAdapter(Context context) {
        mContext = context;
    }

    public CustomerListAdapter(Context context, List<Customer> userList) {
        mContext = context;
        mCustomerList = userList;
    }

    public CustomerListAdapter(Context context, List<Customer> userList, Boolean isAdmin) {
        mContext = context;
        mCustomerList = userList;
        mIsAdmin = isAdmin;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.customer_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.customerName = (TextView) convertView.findViewById(R.id.customer_name);
            mViewHolder.createTime = (TextView) convertView.findViewById(R.id.create_time);
            mViewHolder.customerMobile = (TextView) convertView.findViewById(R.id.customer_mobile);
            mViewHolder.delButton = (ImageView) convertView.findViewById(R.id.del_button);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.customerName.setText(mCustomerList.get(position).getCustomerName());
        mViewHolder.customerMobile.setText(mCustomerList.get(position).getCustomerMobile());
        Date createDate = new Date();
        createDate.setTime(mCustomerList.get(position).getGmtCreate());
        mViewHolder.createTime.setText(DateUtil.convertDateToYMDHM(createDate));

        mViewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提示"); //设置标题
                builder.setMessage("是否确认删除?"); //设置内容
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        DBService dbService = DBService.getInstance(mContext);
                        if(dbService.delCustomerById(mCustomerList.get(position).getId()) > 0){
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                            mCustomerList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
        return convertView;
    }

    //viewHolder对象
    private final class ViewHolder {
        public TextView customerName;
        public TextView createTime;
        public TextView customerMobile;
        public ImageView delButton;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setMCustomerList(List<Customer> customerList){
        this.mCustomerList = customerList;
        notifyDataSetChanged();
    }
}
