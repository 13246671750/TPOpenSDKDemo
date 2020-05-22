package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.bean.IPCDiscoverDevBean;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: DeviceAutoAddListAdapter
 * @Description: Version 1.0.0, 2018-10-16, Li Wei create file.
 */

public class DeviceAutoAddListAdapter extends RecyclerView.Adapter<DeviceAutoAddListAdapter.MyVH> {
    public static final int TYPE_IPC = 0;       //设备类型IPC
    public static final int TYPE_NVR = 1;       //设备类型NVR
    private ArrayList<IPCDiscoverDevBean> mDeviceList;

    private Context mContext;
    private AddDeviceListener mListener;

    public DeviceAutoAddListAdapter(Context mContext, ArrayList<IPCDiscoverDevBean> mDeviceList, AddDeviceListener listener) {
        this.mContext = mContext;
        this.mDeviceList = mDeviceList;
        this.mListener = listener;
    }

    @Override
    public DeviceAutoAddListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceAutoAddListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_add_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final DeviceAutoAddListAdapter.MyVH holder, final int position) {
        final IPCDiscoverDevBean deviceBean = mDeviceList.get(position);
        holder.mCoverIv.setImageResource(deviceBean.mDevInfo.getType() == TYPE_IPC ? R.drawable.device_add_ipc_wire
                : R.drawable.device_add_nvr);
        holder.mDeviceTypeTv.setText(deviceBean.mDevInfo.getType() == TYPE_NVR ? mContext.getString(R.string.device_add_type_nvr2)
                : mContext.getString(R.string.device_add_type_ipc));
        holder.mIpTv.setText(deviceBean.mDevInfo.getStrAddress());
        if (deviceBean.mIsAdded) {
            holder.mDeviceAddBtn.setText(mContext.getString(R.string.device_add_already));
            holder.mDeviceAddBtn.setBackground(mContext.
                    getResources().getDrawable(R.drawable.shape_device_already_add_item_button));
            holder.mDeviceAddBtn.setEnabled(false);
        } else {
            holder.mDeviceAddBtn.setText(mContext.getString(R.string.device_add));
            holder.mDeviceAddBtn.setBackground(mContext.
                    getResources().getDrawable(R.drawable.selector_device_add_item_button));
            holder.mDeviceAddBtn.setEnabled(true);
        }
        holder.mDeviceAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddDeviceClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class MyVH extends RecyclerView.ViewHolder {
        ImageView mCoverIv;
        TextView mDeviceTypeTv;
        TextView mIpTv;
        Button mDeviceAddBtn;

        MyVH(View itemView) {
            super(itemView);
            mCoverIv = (ImageView) itemView.findViewById(R.id.device_add_list_left_iv);
            mDeviceTypeTv = (TextView) itemView.findViewById(R.id.device_add_list_type_tv);
            mIpTv = (TextView) itemView.findViewById(R.id.device_add_list_ip_tv);
            mDeviceAddBtn = (Button) itemView.findViewById(R.id.device_add_list_btn);
        }
    }

    public interface AddDeviceListener {
        void onAddDeviceClicked(int position);
    }

}
