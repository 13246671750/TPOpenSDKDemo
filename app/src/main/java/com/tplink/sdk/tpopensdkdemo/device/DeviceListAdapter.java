package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDevice;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: DeviceListAdapter
 * @Description: Version 1.0.0, 2018-10-08, Li Wei create file.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyVH>{

    private ArrayList<IPCDevice> mDeviceList;
    private Context mContext;
    private OnDeviceClickListner mListener;
    private OnDeviceSetClickListener mSetClickListener;
    private onNVRMoreClickListener mNVRClickListener;

    public DeviceListAdapter(Context mContext, ArrayList<IPCDevice> deviceList, OnDeviceClickListner listner,
                             OnDeviceSetClickListener setClickListener, onNVRMoreClickListener nvrMoreClickListener) {
        this.mContext = mContext;
        this.mDeviceList = deviceList;
        this.mListener = listner;
        this.mSetClickListener = setClickListener;
        this.mNVRClickListener = nvrMoreClickListener;
    }

    public DeviceListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyVH holder, final int position) {
        final IPCDevice dev = mDeviceList.get(position);
        holder.mDeviceNameTv.setText(dev.getAlias());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dev.isIPC()) {
                    mListener.onDeviceClicked(position);
                } else {
                    mNVRClickListener.onNVRMoreClick(position);
                }
            }
        });
        holder.mSetIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetClickListener.onDevSetClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class MyVH extends RecyclerView.ViewHolder {
        TextView mDeviceNameTv;
        ImageView mSetIv;

        MyVH(View itemView) {
            super(itemView);
            mDeviceNameTv = itemView.findViewById(R.id.dev_name_tv);
            mSetIv = itemView.findViewById(R.id.device_setting_iv);
        }
    }

    public interface OnDeviceClickListner {
        void onDeviceClicked(int position);
    }

    public interface OnDeviceSetClickListener {
        void onDevSetClick(int position);
    }

    public interface onNVRMoreClickListener {
        void onNVRMoreClick(int position);
    }
}
