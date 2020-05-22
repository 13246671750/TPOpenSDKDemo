package com.tplink.sdk.tpopensdkdemo.device;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tplink.sdk.tpopensdk.bean.IPCDeviceDefines;
import com.tplink.sdk.tpopensdk.openctx.IPCReqListener;
import com.tplink.sdk.tpopensdk.openctx.IPCReqResponse;
import com.tplink.sdk.tpopensdkdemo.R;

public class RecordPlanAdvanceActivty extends DeviceSettingDetailInfoActivity {
    EditText mPreEdt;
    EditText mDelayEdt;
    IPCDeviceDefines.IPCRecordPlanAdvance mRecordPlanAdvance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_record_plan_advance_activty);
        initView();
    }

    @Override
    public void initData() {
        super.initData();
        mRecordPlanAdvance = mDev.getRecordPlanAdvance();
    }

    @Override
    public void initView() {
        super.initView();
        mPreEdt = findViewById(R.id.record_plan_pre);
        mDelayEdt = findViewById(R.id.record_plan_delay);
        mPreEdt.setText(String.valueOf(mRecordPlanAdvance.getPreRecord()));
        mDelayEdt.setText(String.valueOf(mRecordPlanAdvance.getDelayRecord()));
        findViewById(R.id.record_plan_edit_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_plan_edit_tv:
                int iPreRecord = 0;
                int iDelayRecord = 0;
                try {
                    iPreRecord = Integer.valueOf(mPreEdt.getText().toString());
                    iDelayRecord = Integer.valueOf(mDelayEdt.getText().toString());
                } catch (Exception e) {
                    showToast(getString(R.string.enter_common_invalid));
                    return;
                }
                mDevCtx.reqSetPlanAdvance(mDev, new IPCReqListener() {
                    @Override
                    public int callBack(IPCReqResponse ipcReqResponse) {
                        commonCallBack(ipcReqResponse);
                        return 0;
                    }
                }, iPreRecord, iDelayRecord);
                showLoading(null);
                break;
        }
    }
}
