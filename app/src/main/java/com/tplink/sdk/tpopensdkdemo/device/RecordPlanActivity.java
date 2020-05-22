package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class RecordPlanActivity extends DeviceSettingDetailInfoActivity {

    public static IPCDeviceDefines.IPCRecordPlan recordPlan;
    TextView mEnableTv;
    int mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_record_plan);
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        recordPlan = mDev.getRecordPlan();
        mState = recordPlan.getEnable();
    }

    @Override
    public void initView() {
        super.initView();
        mEnableTv = findViewById(R.id.record_Plan_Enable_tv);
        mEnableTv.setText(getString(R.string.record_plan_state) +
                getString(mState == 1 ? R.string.set_open : R.string.set_close));
        mEnableTv.setOnClickListener(this);
        findViewById(R.id.record_Plan_day_1).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_2).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_3).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_4).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_5).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_6).setOnClickListener(this);
        findViewById(R.id.record_Plan_day_7).setOnClickListener(this);
        findViewById(R.id.set_record_day).setOnClickListener(this);
        findViewById(R.id.record_Plan_advance).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_Plan_Enable_tv:
                mState = mState == 1 ? 0 : 1;
                mEnableTv.setText(getString(R.string.record_plan_state) +
                        getString(mState == 1 ? R.string.set_open : R.string.set_close));
                break;
            case R.id.record_Plan_day_1:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 1);
                break;
            case R.id.record_Plan_day_2:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 2);
                break;
            case R.id.record_Plan_day_3:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 3);
                break;
            case R.id.record_Plan_day_4:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 4);
                break;
            case R.id.record_Plan_day_5:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 5);
                break;
            case R.id.record_Plan_day_6:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 6);
                break;
            case R.id.record_Plan_day_7:
                RecordPlanDayActivity.startActivity(RecordPlanActivity.this, 0);
                break;
            case R.id.record_Plan_advance:
                mDevCtx.reqGetPlanAdvance(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                });
                showLoading(null);
                break;
            case R.id.set_record_day:
                // TODO: 2018/10/20 感觉recordPlan接口有点问题，要对比一下App的
                // mDevCtx.reqSetRecordPlan()
                break;
        }
    }

    @Override
    public void onReqSuccess(IPCReqResponse response) {
        RecordPlanAdvanceActivty.startActivity(RecordPlanActivity.this, RecordPlanAdvanceActivty.class, mDev);
    }
}
