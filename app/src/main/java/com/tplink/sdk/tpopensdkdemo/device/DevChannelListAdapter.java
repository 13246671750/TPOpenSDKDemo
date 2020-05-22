package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.openctx.IPCDeviceChannel;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: DevChannelListAdapter
 * @Description: Version 1.0.0, 2018-10-30, Li Wei create file.
 */

public class DevChannelListAdapter extends RecyclerView.Adapter<DevChannelListAdapter.MyVH> {

    private Context mContext;
    private ArrayList<IPCDeviceChannel> mChannelList;
    private OnChannelClickListner mListener;
    private OnChannelSetClickListener mSetClickListener;

    public DevChannelListAdapter(Context mContext, ArrayList<IPCDeviceChannel> mChannelList,
                                 OnChannelClickListner mListener, OnChannelSetClickListener mSetClickListener) {
        this.mContext = mContext;
        this.mChannelList = mChannelList;
        this.mListener = mListener;
        this.mSetClickListener = mSetClickListener;
    }

    public DevChannelListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DevChannelListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DevChannelListAdapter.MyVH holder, final int position) {
        final IPCDeviceChannel channel = mChannelList.get(position);
        holder.mDeviceNameTv.setText(channel.getAlias());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChannelClicked(position);
            }
        });
        holder.mSetIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetClickListener.onChannelSetClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
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

    public interface OnChannelClickListner {
        void onChannelClicked(int position);
    }

    public interface OnChannelSetClickListener {
        void onChannelSetClick(int position);
    }
}
