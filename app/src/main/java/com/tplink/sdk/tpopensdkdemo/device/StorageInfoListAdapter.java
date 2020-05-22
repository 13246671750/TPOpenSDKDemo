package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: StorageInfoListAdapter
 * @Description: Version 1.0.0, 2018-10-20, Li Wei create file.
 */

public class StorageInfoListAdapter extends RecyclerView.Adapter<StorageInfoListAdapter.MyVH> {

    private Context mContext;
    private ArrayList<IPCDeviceDefines.IPCStorageInfo> mSDInfoList;
    private OnLoopStatusListener mSetLoopStatusListener;
    private OnFormatSDListener mFormatSDListener;

    public StorageInfoListAdapter(Context mContext, ArrayList<IPCDeviceDefines.IPCStorageInfo> mSDInfoList,
                                  OnLoopStatusListener mSetLoopStatusListener, OnFormatSDListener mFormatSDListener) {
        this.mContext = mContext;
        this.mSDInfoList = mSDInfoList;
        this.mSetLoopStatusListener = mSetLoopStatusListener;
        this.mFormatSDListener = mFormatSDListener;
    }

    public StorageInfoListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StorageInfoListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_sdinfo_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyVH holder, final int position) {
        final IPCDeviceDefines.IPCStorageInfo sdInfo = mSDInfoList.get(position);
        ((TextView)holder.mDiskNameLayout.findViewById(R.id.about_value_tv)).setText(sdInfo.getStrDiskName());
        ((TextView)holder.mRwAttrLayout.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getRWAttr()));
        ((TextView)holder.mFreeSpaceLayout.findViewById(R.id.about_value_tv)).setText(sdInfo.getFreeSpace());
        ((TextView)holder.mTotalSpace.findViewById(R.id.about_value_tv)).setText(sdInfo.getTotalSpace());
        ((TextView)holder.mVideoFreeSpace.findViewById(R.id.about_value_tv)).setText(sdInfo.getVideoFreeSpace());
        ((TextView)holder.mVideoTotalSpace.findViewById(R.id.about_value_tv)).setText(sdInfo.getVideoTotalSpace());
        ((TextView)holder.mStatus.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getStatus()));
        ((TextView)holder.mDetectStatus.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getDetectStatus()));
        ((TextView)holder.mType.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getType()));
        ((TextView)holder.mRecordDuration.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getRecordDuration()));
        ((TextView)holder.mRecordFreeDuration.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getRecordFreeDuration()));
        ((TextView)holder.mRecordStartTime.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getRecordStartTime()));
        ((TextView)holder.mLoopRecordStatus.findViewById(R.id.about_value_tv)).setText(String.valueOf(sdInfo.getLoopRecordStatus()));
        holder.mLoopOperateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = sdInfo.getStatus() == 1 ? 0 : 1;
                mSetLoopStatusListener.onSetLoopStatus(position, status);
            }
        });
        holder.mFormatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFormatSDListener.onFormatSD(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSDInfoList.size();
    }

    static class MyVH extends RecyclerView.ViewHolder {
        LinearLayout mDiskNameLayout;
        LinearLayout mRwAttrLayout;
        LinearLayout mFreeSpaceLayout;
        LinearLayout mTotalSpace;
        LinearLayout mVideoFreeSpace;
        LinearLayout mVideoTotalSpace;
        LinearLayout mStatus;
        LinearLayout mDetectStatus;
        LinearLayout mType;
        LinearLayout mRecordDuration;
        LinearLayout mRecordFreeDuration;
        LinearLayout mRecordStartTime;
        LinearLayout mLoopRecordStatus;
        TextView mLoopOperateTv;
        TextView mFormatTv;

        MyVH(View itemView) {
            super(itemView);
            mDiskNameLayout = itemView.findViewById(R.id.diskName_layout);
            mRwAttrLayout = itemView.findViewById(R.id.rwAttr_layout);
            mFreeSpaceLayout = itemView.findViewById(R.id.freeSpace_layout);
            mTotalSpace = itemView.findViewById(R.id.totalSpace_layout);
            mVideoFreeSpace = itemView.findViewById(R.id.videoFreeSpace_layout);
            mVideoTotalSpace = itemView.findViewById(R.id.VideoTotalSpace_layout);
            mStatus = itemView.findViewById(R.id.status_layout);
            mDetectStatus = itemView.findViewById(R.id.detectStatus_layout);
            mType = itemView.findViewById(R.id.type_layout);
            mRecordDuration = itemView.findViewById(R.id.recordDuration_layout);
            mRecordFreeDuration = itemView.findViewById(R.id.recordFreeDuration_layout);
            mRecordStartTime = itemView.findViewById(R.id.RecordStartTime_layout);
            mLoopRecordStatus = itemView.findViewById(R.id.LoopRecordStatus_layout);
            mLoopOperateTv = itemView.findViewById(R.id.loop_Operate_tv);
            mFormatTv = itemView.findViewById(R.id.format_tv);

            ((TextView)mDiskNameLayout.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_name);
            ((TextView)mRwAttrLayout.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_attr);
            ((TextView)mFreeSpaceLayout.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_freespace);
            ((TextView)mTotalSpace.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_totalspace);
            ((TextView)mVideoFreeSpace.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_videoFreeSpace);
            ((TextView)mVideoTotalSpace.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_VideoTotalSpace);
            ((TextView)mStatus.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_status);
            ((TextView)mDetectStatus.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_detectStatus);
            ((TextView)mType.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_type);
            ((TextView)mRecordDuration.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_recordDuration);
            ((TextView)mRecordFreeDuration.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_recordFreeDuration);
            ((TextView)mRecordStartTime.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_recordStartTime);
            ((TextView)mLoopRecordStatus.findViewById(R.id.about_name_tv)).setText(R.string.storage_info_looprecordStatus);
            mLoopOperateTv.setText(R.string.storage_info_looprecordoperate);
            mFormatTv.setText(R.string.storage_info_format);
        }
    }

    public interface  OnLoopStatusListener {
       void onSetLoopStatus(int position, int status);
    }

    public interface OnFormatSDListener {
       void onFormatSD(int position);
    }
}
