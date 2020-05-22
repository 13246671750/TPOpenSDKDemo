package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: WifiListAdapter
 * @Description: Version 1.0.0, 2018-10-25, Li Wei create file.
 */

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyVH>{
    private ArrayList<IPCDeviceDefines.IPCOnboardWifiInfo> mWifiList;
    private Context mContext;
    private OnWifiClickListener mListener;

    public WifiListAdapter(ArrayList<IPCDeviceDefines.IPCOnboardWifiInfo> mWifiList, Context mContext, OnWifiClickListener mListener) {
        this.mWifiList = mWifiList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    public WifiListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WifiListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyVH holder, final int position) {
        final IPCDeviceDefines.IPCOnboardWifiInfo wifiInfo = mWifiList.get(position);
        holder.mDeviceNameTv.setText(wifiInfo.getSsid());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWifiClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWifiList.size();
    }

    static class MyVH extends RecyclerView.ViewHolder {
        TextView mDeviceNameTv;
        ImageView mSetIv;

        MyVH(View itemView) {
            super(itemView);
            mDeviceNameTv = itemView.findViewById(R.id.dev_name_tv);
            mSetIv = itemView.findViewById(R.id.device_setting_iv);
            mSetIv.setVisibility(View.GONE);
        }
    }

    public interface OnWifiClickListener {
        void onWifiClick(int position);
    }
}
