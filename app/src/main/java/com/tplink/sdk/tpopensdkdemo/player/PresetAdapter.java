package com.tplink.sdk.tpopensdkdemo.player;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.List;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: PresetAdapter
 * @Description: Version 1.0.0, 2018-10-16, caizhenghe create file.
 */

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.PresetHolder> {
    private Context mContext;
    private List<IPCDeviceDefines.IPCPreset> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void callback(int position);
    }

    public PresetAdapter(Context context, List<IPCDeviceDefines.IPCPreset> videoList, OnItemClickListener listener) {
        mContext = context;
        mList = videoList;
        mListener = listener;
    }

    public void update(List<IPCDeviceDefines.IPCPreset> videoList) {
        mList = videoList;
        notifyDataSetChanged();
    }

    @Override
    public PresetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preset, parent, false);
        PresetHolder holder = new PresetHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PresetHolder holder, final int position) {
        holder.presetIdTv.setText(mContext.getString(R.string.preset) + position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.callback(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PresetHolder extends RecyclerView.ViewHolder {
        TextView presetIdTv;

        public PresetHolder(View itemView) {
            super(itemView);
            presetIdTv = itemView.findViewById(R.id.item_preset_tv);
        }
    }
}
