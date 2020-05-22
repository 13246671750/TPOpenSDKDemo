package com.tplink.sdk.tpopensdkdemo.device;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdkdemo.R;

import java.util.ArrayList;

/**
 * Copyright (C), 2018, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * @author Li Wei
 * @ClassName: RecordPlanListAdapter
 * @Description: Version 1.0.0, 2018-10-20, Li Wei create file.
 */

public class RecordPlanListAdapter extends  RecyclerView.Adapter<RecordPlanListAdapter.MyVH>{

    private Context mContext;
    ArrayList<IPCDeviceDefines.IPCRecordPlanSection> mPlanSectionList;

    public RecordPlanListAdapter(Context mContext, ArrayList<IPCDeviceDefines.IPCRecordPlanSection> mPlanSectionList) {
        this.mContext = mContext;
        this.mPlanSectionList = mPlanSectionList;
    }

    public RecordPlanListAdapter.MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordPlanListAdapter.MyVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_record_plan_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyVH holder, final int position) {
        final IPCDeviceDefines.IPCRecordPlanSection section = mPlanSectionList.get(position);
        holder.mItemTv.setText(String.valueOf(position));
        holder.mStartHourEdt.setText(String.valueOf(section.getStartHour()));
        holder.mStartMinEdt.setText(String.valueOf(section.getStartMin()));
        holder.mEndHourEdt.setText(String.valueOf(section.getEndHour()));
        holder.mEndMinEdt.setText(String.valueOf(section.getEndMin()));
        holder.mTypeEdt.setText(String.valueOf(section.getType()));
        holder.mModiftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section.setStartHour(Integer.valueOf(holder.mStartHourEdt.getText().toString()));
                section.setStartMin(Integer.valueOf(holder.mStartMinEdt.getText().toString()));
                section.setEndHour(Integer.valueOf(holder.mEndHourEdt.getText().toString()));
                section.setEndMin(Integer.valueOf(holder.mEndMinEdt.getText().toString()));
                section.setType(Integer.valueOf(holder.mTypeEdt.getText().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlanSectionList.size();
    }

    static class MyVH extends RecyclerView.ViewHolder {
        EditText mStartHourEdt;
        EditText mEndHourEdt;
        EditText mStartMinEdt;
        EditText mEndMinEdt;
        EditText mTypeEdt;
        TextView mItemTv;
        TextView mModiftTv;

        MyVH(View itemView) {
            super(itemView);
            mItemTv = itemView.findViewById(R.id.record_plan_day_tv);
            mStartHourEdt = itemView.findViewById(R.id.record_plan_start_hour);
            mEndHourEdt = itemView.findViewById(R.id.record_plan_end_hour);
            mStartMinEdt = itemView.findViewById(R.id.record_plan_start_min);
            mEndMinEdt = itemView.findViewById(R.id.record_plan_end_min);
            mTypeEdt = itemView.findViewById(R.id.record_plan_type);
            mModiftTv = itemView.findViewById(R.id.record_plan_edit_tv);
        }
    }
}
