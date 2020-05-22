package com.tplink.sdk.tpopensdkdemo.player;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tplink.foundation.TPLog;
import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.TPUtils;

import java.util.List;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author caizhenghe
 * @ClassName: VideoAdapter
 * @Description: Version 1.0.0, 2018-10-16, caizhenghe create file.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context mContext;
    private List<IPCDeviceDefines.IPCSearchVideo> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void callback(int position);
    }

    public VideoAdapter(Context context, List<IPCDeviceDefines.IPCSearchVideo> videoList, OnItemClickListener listener) {
        mContext = context;
        mList = videoList;
        mListener = listener;
    }

    public void update(List<IPCDeviceDefines.IPCSearchVideo> videoList) {
        mList = videoList;
        notifyDataSetChanged();
    }

    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_video, parent, false);
        VideoHolder holder = new VideoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoHolder holder, final int position) {
        IPCDeviceDefines.IPCSearchVideo bean = mList.get(position);
        TPLog.d("TAG", "onBindViewHolder::position = " + position + "; start = " + bean.getStartTime() + "; end = " + bean.getEndTime());
        holder.startTimeTv.setText(TPUtils.getTime(mContext, bean.getStartTime() * 1000));
        holder.typeTv.setText(TPUtils.getEventType(mContext, bean.getType()));

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

    class VideoHolder extends RecyclerView.ViewHolder {
        TextView typeTv;

        TextView startTimeTv;

        public VideoHolder(View itemView) {
            super(itemView);
            typeTv = itemView.findViewById(R.id.item_video_type_tv);
            startTimeTv = itemView.findViewById(R.id.item_video_start_time_tv);
        }
    }
}
