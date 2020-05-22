package com.tplink.sdk.tpopensdkdemo.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tplink.sdk.tpopensdkdemo.R;
import com.tplink.sdk.tpopensdkdemo.common.BaseActivity;

public class RecordPlanDayActivity extends BaseActivity {
    public static String RECORD_PLAN_DAY = "plan_day";

    RecyclerView mItemListView;
    RecordPlanListAdapter mAdapter;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_record_plan_day);
        initView();
    }

    private void initData() {
        mDay = getIntent().getIntExtra(RECORD_PLAN_DAY, 0);
        mAdapter = new RecordPlanListAdapter(this, RecordPlanActivity.recordPlan.getDayPlan().get(mDay).getPlanSection());
    }

    private void initView() {
        mItemListView = findViewById(R.id.record_plan_item_list);
        mItemListView.setAdapter(mAdapter);
        mItemListView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static void startActivity(Activity activity, int iDay) {
        Intent intent = new Intent(activity, RecordPlanDayActivity.class);
        intent.putExtra(RECORD_PLAN_DAY, iDay);
        activity.startActivity(intent);
    }
}
